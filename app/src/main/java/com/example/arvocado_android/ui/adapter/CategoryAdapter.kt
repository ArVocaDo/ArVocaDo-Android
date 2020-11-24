package com.example.arvocado_android.ui.adapter

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
import com.example.arvocado_android.util.inflate


class CategoryAdapter() : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    private var data : List<CategoryResponse> = listOf()

    fun initData(data: List<CategoryResponse>) {
        this.data = data
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(v:View, data: CategoryResponse, pos : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(parent.inflate(R.layout.item_rv_category))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(data[position],listener)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val img : ImageView = itemView.findViewById(R.id.imgRvCategory)
        private val txt : TextView = itemView.findViewById(R.id.tvRvCategory)

        fun bind(item: CategoryResponse, listener: OnItemClickListener?) {
            if(!item.c_img.isNullOrBlank()) {
                Glide.with(itemView).load(item.c_img!!).into(img)
            }
            txt.text = item.c_name

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