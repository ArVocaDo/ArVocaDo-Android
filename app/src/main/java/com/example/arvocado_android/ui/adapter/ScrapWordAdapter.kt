package com.example.arvocado_android.ui.adapter

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.GlideContext
import com.example.arvocado_android.R
import com.example.arvocado_android.data.response.CategoryResponse
import com.example.arvocado_android.data.response.CategoryWordResponse
import com.example.arvocado_android.util.MyGLRenderer
import com.example.arvocado_android.util.ObjLoader
import com.example.arvocado_android.util.inflate


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
        private val img : GLSurfaceView = itemView.findViewById(R.id.imgRvWord)
        private val txt : TextView = itemView.findViewById(R.id.tvRvWordTitle)

        fun bind(item: CategoryWordResponse, listener: OnItemClickListener?) {

            img.setEGLContextClientVersion(2)
            val  renderer = MyGLRenderer()
            img.setRenderer(renderer)
            img.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
            val objLoader = ObjLoader(context,item.AR_obj)
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