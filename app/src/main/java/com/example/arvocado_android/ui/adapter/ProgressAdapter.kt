package com.example.arvocado_android.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arvocado_android.R
import com.example.arvocado_android.data.response.CategoryResponse
import com.example.arvocado_android.util.inflate
import timber.log.Timber
import kotlin.math.roundToInt


class ProgressAdapter(context : Context) : RecyclerView.Adapter<ProgressAdapter.ViewHolder>() {
    private var data : List<CategoryResponse> = listOf()
    private val context = context

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
            = ViewHolder(parent.inflate(R.layout.item_rv_progress))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(data[position],listener)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val btnLearn : Button = itemView.findViewById(R.id.btnRvLearn)
        private val txt : TextView = itemView.findViewById(R.id.tvRvProgress)
        private val percent : TextView = itemView.findViewById(R.id.tvRvProgressPercent)
        private val progress : ProgressBar = itemView.findViewById(R.id.pbRvProgress)

        fun bind(item: CategoryResponse, listener: OnItemClickListener?) {

            txt.text = item.c_name
            var p = 0.0
            if(item.index!=0 && item.c_count!=0) {
                p = (((item.index.toDouble() / item.c_count.toDouble()) * 100).toDouble())
            }
            percent.text = p.roundToInt().toString()+ "%"
            progress.progress = p.roundToInt()
            Timber.e(p.toString())
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                btnLearn.setOnClickListener {
                    listener?.onItemClick(itemView,item,pos)

                }
            }

        }
    }

}