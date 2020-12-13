package com.example.arvocado_android.ui.camera

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.arvocado_android.R
import com.example.arvocado_android.data.response.CategoryWordResponse
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode
import org.w3c.dom.Node
import timber.log.Timber


class ArcoreActivity : AppCompatActivity() {
    private lateinit var arFragment: ArFragment //The ARFragment where you detect and tap on plane
    val viewNodes = mutableListOf<Node>() // List of all nodes.
    private lateinit var word: CategoryWordResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arcore2)
        arFragment= supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        arFragment.setOnTapArPlaneListener(BaseArFragment.OnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor: Anchor = hitResult.createAnchor()

            ModelRenderable.builder()
                .setSource(this, Uri.parse("kiwi.sfb"))
                .build()
                .thenAccept{ addModelToScene(anchor, it)}
                .exceptionally {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage(it.localizedMessage)
                        .show()
                    return@exceptionally null
                }
        })
        // When you tap on a detected plane, a callback function is called which adds a Node to the Scene
//        arFragment.setOnTapArPlaneListener{hitResult,x,y ->
//            loadModel{modelRenderable ->
//                addNodeToScene(hitResult.createAnchor(), modelRenderable)
//            }
//        }
        init()
    }
    private fun init() {
        /**
         * 데이터
         *
         */
        word= intent!!.getSerializableExtra("wordData") as CategoryWordResponse
        Timber.e("wordArcore2 :: ${word.w_img}")
    }

//    override fun onDestroy() {
//        if(session != null) {
//            session!!.close()
//            session = null
//        }
//        super.onDestroy()
//    }
    // Uses the ModelRenderable and the ViewRenderable objects to add a Node to the Scene
//    private fun addNodeToScene(
//        anchor: Anchor,
//        modelRenderable: ModelRenderable
//    ) {
//        val anchorNode = AnchorNode(anchor)
//        val modelNode = TransformableNode(arFragment.transformationSystem).apply {
//            renderable = modelRenderable
//            setParent(anchorNode)
//            arFragment.arSceneView.scene.addChild(anchorNode)
//            select()
//        }
//
//        modelNode.setOnTapListener { _, _ ->
//            if(!modelNode.isTransforming) {
//
//            }
//        }
//    }

    private fun addModelToScene(anchor: Anchor, it: ModelRenderable?) {
        val anchorNode: AnchorNode = AnchorNode(anchor)
        val transform: TransformableNode = TransformableNode(arFragment.transformationSystem)
        transform.setParent(anchorNode)
        transform.renderable = it
        arFragment.arSceneView.scene.addChild(anchorNode)
        transform.select()
    }

    //This functions load the model
//    private fun loadModel(callback: (ModelRenderable, ) -> Unit) {
//        val modelRenderable = ModelRenderable.builder()
//            .setSource(this, R.raw.kiwi)
//            .build()
//
//        CompletableFuture.allOf(modelRenderable)
//            .thenAccept {
//                callback(modelRenderable.get())
//            }
//            .exceptionally {
//                Toast.makeText(this, "Error loading model: $it", Toast.LENGTH_LONG).show()
//                null
//            }
//    }

}