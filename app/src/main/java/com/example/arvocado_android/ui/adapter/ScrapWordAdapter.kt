package com.example.arvocado_android.ui.adapter

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arvocado_android.R
import com.example.arvocado_android.data.response.CategoryWordResponse
import com.example.arvocado_android.util.Obj
import com.example.arvocado_android.util.inflate
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class ScrapWordAdapter(context : Context) : RecyclerView.Adapter<ScrapWordAdapter.ViewHolder>() {
    private var data : List<CategoryWordResponse> = listOf()
    private val context = context

    fun initData(data: List<CategoryWordResponse>) {
        this.data = data
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(v:View, data: CategoryWordResponse, pos : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(parent.inflate(R.layout.item_rv_scrap_word))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(data[position],listener)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var obj : Obj
        private val img : GLSurfaceView = itemView.findViewById(R.id.imgRvWord)
        private val txt : TextView = itemView.findViewById(R.id.tvRvWordTitle)

        fun bind(item: CategoryWordResponse, listener: OnItemClickListener?) {

            img.setEGLContextClientVersion(2)
            img.setRenderer(object :GLSurfaceView.Renderer{
                override fun onDrawFrame(gl: GL10?) {
                    obj.draw()

                }

                override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                    GLES20.glViewport(0, 0, width, height)
                }

                override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                    img.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
                    obj = Obj(context = context, obj = item.AR_obj)
                }

            })
            txt.text = item.w_kor

            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,item,pos)
                }
            }

        }
    }

}