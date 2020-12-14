package com.example.arvocado_android.ui.camera

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.ArraySet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.response.CategoryWordResponse
import com.example.arvocado_android.util.setVisible
import com.google.android.filament.gltfio.Animator
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.PlaneDiscoveryController
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.acitivity_ux.*
import kotlinx.android.synthetic.main.element_card_view.*
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Function

class Arcore16Activity : AppCompatActivity() {
    private var arFragment: ArFragment? = null
    private var renderable: Renderable? = null
    private lateinit var word: CategoryWordResponse

    private class AnimationInstance internal constructor(
        var animator: com.google.android.filament.gltfio.Animator,
        index: Int,
        var startTime: Long
    ) {
        var duration: Float
        var index: Int

        init {
            duration = animator.getAnimationDuration(index)
            this.index = index
        }
    }

    private val animators: MutableSet<AnimationInstance> =
        ArraySet()
    private val colors =
        Arrays.asList(
            Color(0F, 0F, 0F, 1F),
            Color(1F, 0F, 0F, 1F),
            Color(0F, 1F, 0F, 1F),
            Color(0F, 0F, 1F, 1F),
            Color(1F, 1F, 0F, 1F),
            Color(0F, 1F, 1F, 1F),
            Color(1F, 0F, 1F, 1F),
            Color(1F, 1F, 1F, 1F)
        )
    private var nextColor = 0

    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return
        }
        setContentView(R.layout.acitivity_ux)
        arFragment = getSupportFragmentManager().findFragmentById(R.id.ux_fragment) as ArFragment
        val planeRenderer = arFragment!!.arSceneView.planeRenderer
        if(planeRenderer.isEnabled) {
            ar_guide.setText(" 핸드폰을 바닥에 비추어 하얀 점이 나오면, 점을 클릭하여 ${word.w_kor}를 띄워보세요 ")
        }
        renderModel()
        arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane?, motionEvent: MotionEvent? ->
            if (renderable == null) {
                return@setOnTapArPlaneListener
            }
            // Create the Anchor.
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment!!.arSceneView.scene)
            ar_guide.visibility = View.INVISIBLE
            // Create the transformable model and add it to the anchor.
            val model = TransformableNode(arFragment!!.transformationSystem)
            model.setParent(anchorNode)
            model.renderable = renderable
            model.select()
            model.rotationController.isEnabled = false
            //if model is tapped
            model.setOnTapListener {_, _ ->
                if(!model.isTransforming) {
                    ar_word.visibility = View.VISIBLE
                    ar_word.setText(" " + word.w_eng + " ")
                    val path: Uri = Uri.parse(word.audio_eng)
                    val r3: Ringtone = RingtoneManager.getRingtone(baseContext, path)
                    r3.play()
                    Handler().postDelayed({
                        ar_word.visibility = View.INVISIBLE
                    }, 2000 )
                }
            }
            val filamentAsset = model.renderableInstance!!.filamentAsset
            if (filamentAsset!!.animator.animationCount > 0) {
                animators.add(
                    AnimationInstance(
                        filamentAsset.animator,
                        0,
                        System.nanoTime()
                    )
                )
            }
            val color = colors[nextColor]
            nextColor++
            for (i in 0 until renderable!!.submeshCount) {
                val material =
                    renderable!!.getMaterial(i)
                material.setFloat4("baseColorFactor", color)
            }
        }
        arFragment!!
            .getArSceneView()
            .scene
            .addOnUpdateListener { frameTime: FrameTime? ->
                val time = System.nanoTime()
                for (animator: AnimationInstance in animators) {
                    animator.animator.applyAnimation(
                        animator.index, ((time - animator.startTime) / TimeUnit.SECONDS.toNanos(
                            1
                        ).toDouble()).toFloat()
                                % animator.duration
                    )
                    animator.animator.updateBoneMatrices()
                }
            }
        ar_end.setOnDebounceClickListener {
            finish()
        }
    }
    private fun init() {
        word= intent?.getSerializableExtra("wordData") as CategoryWordResponse
        Timber.e("wordArcore :: ${word.w_img}")
    }
    private fun renderModel() {
        val weakActivity = WeakReference(this)
        ModelRenderable.builder()
            .setSource(
                this,
                Uri.parse(
                    word.w_AR
                )
            )
            .setIsFilamentGltf(true)
            .build()
            .thenAccept(
                Consumer<ModelRenderable> { modelRenderable: ModelRenderable? ->
                    val activity = weakActivity.get()
                    if (activity != null) {
                        activity.renderable = modelRenderable
                    }
                }
            )
            .exceptionally(
                Function<Throwable, Void?> { throwable: Throwable? ->
                    val toast: Toast = Toast.makeText(
                        this,
                        "Ar object를 로딩하는데 실패했습니다.",
                        Toast.LENGTH_SHORT
                    )
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    null
                }
            )
    }
    companion object {
        private val TAG = Arcore16Activity::class.java.simpleName
        private const val MIN_OPENGL_VERSION = 3.0

        fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Log.e(
                    TAG,
                    "Sceneform requires Android N or later"
                )
                Toast.makeText(
                    activity,
                    "Sceneform requires Android N or later",
                    Toast.LENGTH_LONG
                ).show()
                activity.finish()
                return false
            }
            val openGlVersionString =
                (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                    .deviceConfigurationInfo
                    .glEsVersion
            if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
                Log.e(
                    TAG,
                    "Sceneform requires OpenGL ES 3.0 later"
                )
                Toast.makeText(
                    activity,
                    "Sceneform requires OpenGL ES 3.0 or later",
                    Toast.LENGTH_LONG
                )
                    .show()
                activity.finish()
                return false
            }
            return true
        }
    }
}