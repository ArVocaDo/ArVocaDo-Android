package com.example.arvocado_android.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.example.arvocado_android.R
import com.example.arvocado_android.data.response.CategoryResponse
import com.example.arvocado_android.util.inflate
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
        private val progress : RoundCornerProgressBar = itemView.findViewById(R.id.pbRvProgress)

        fun bind(item: CategoryResponse, listener: OnItemClickListener?) {

            txt.text = item.c_name
            if(item.c_idx%3== 0) {
                progress.progressColor = context.resources.getColor(R.color.colorYellow)
                progress.secondaryProgressColor = context.resources.getColor(R.color.colorYellow50)

            } else if (item.c_idx%3 == 1) {
                progress.progressColor = context.resources.getColor(R.color.colorMain)
                progress.secondaryProgressColor = context.resources.getColor(R.color.colorGreen50)

            } else {
                progress.progressColor = context.resources.getColor(R.color.colorPink)
                progress.secondaryProgressColor = context.resources.getColor(R.color.colorPink50)

            }
            var p = 0.0
            if(item.index!=0 && item.c_count!=0) {
                p = (((item.index) / item.c_count.toDouble()) * 100)
            }
            percent.text = p.roundToInt().toString()+ "%"
            progress.setProgress(p.roundToInt())
            progress.max = 100f
            progress.setSecondaryProgress((p*1.3).roundToInt())
            //            Timber.e(p.toString())
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