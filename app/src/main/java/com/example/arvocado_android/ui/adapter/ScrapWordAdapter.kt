package com.example.arvocado_android.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arvocado_android.R
import com.example.arvocado_android.data.response.ScrapWordResponse
import com.example.arvocado_android.util.inflate


class ScrapWordAdapter(context : Context) : RecyclerView.Adapter<ScrapWordAdapter.ViewHolder>() {
    private var data : List<ScrapWordResponse> = listOf()
    private val context = context

    fun initData(data: List<ScrapWordResponse>) {
        this.data = data
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(v:View, data: ScrapWordResponse, pos : Int)
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
        private val img : ImageView = itemView.findViewById(R.id.imgRvWord)
        private val txt : TextView = itemView.findViewById(R.id.tvRvWordTitle)
        private val txtKo : TextView = itemView.findViewById(R.id.tvRvWordKor)

        fun bind(item: ScrapWordResponse, listener: OnItemClickListener?) {

            Glide.with(itemView).load(item.w_img!!).into(img)
            txt.text = item.w_eng
            txtKo.text = "("+item.w_kor+")"

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