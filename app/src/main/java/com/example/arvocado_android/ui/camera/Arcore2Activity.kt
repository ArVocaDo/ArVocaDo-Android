package com.example.arvocado_android.ui.camera
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.arvocado_android.R
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.collision.Box
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.CompletableFuture

class Arcore2Activity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment //The ARFragment where you detect and tap on plane
    val viewNodes = mutableListOf<Node>() // List of all nodes.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arcore2)

        arFragment= ArFragment() //Initialise the ARFragment
        //arFragment = supportFragmentManager.findFragmentById(R.id.arFragment)

        // When you tap on a detected plane, a callback function is called which adds a Node to the Scene
        arFragment.setOnTapArPlaneListener{hitResult,x,y ->
            loadModel{modelRenderable ->
                addNodeToScene(hitResult.createAnchor(), modelRenderable)
            }
        }

    }

    // Uses the ModelRenderable and the ViewRenderable objects to add a Node to the Scene
    private fun addNodeToScene(
        anchor: Anchor,
        modelRenderable: ModelRenderable
    ) {
        val anchorNode = AnchorNode(anchor)
        val modelNode = TransformableNode(arFragment.transformationSystem).apply {
            renderable = modelRenderable
            setParent(anchorNode)
            arFragment.arSceneView.scene.addChild(anchorNode)
            select()
        }

        modelNode.setOnTapListener { _, _ ->
            if(!modelNode.isTransforming) {

            }
        }
    }


    //This functions load the model
    private fun loadModel(callback: (ModelRenderable, ) -> Unit) {
        val modelRenderable = ModelRenderable.builder()
            .setSource(this, R.raw.kiwi)
            .build()

        CompletableFuture.allOf(modelRenderable)
            .thenAccept {
                callback(modelRenderable.get())
            }
            .exceptionally {
                Toast.makeText(this, "Error loading model: $it", Toast.LENGTH_LONG).show()
                null
            }
    }



}