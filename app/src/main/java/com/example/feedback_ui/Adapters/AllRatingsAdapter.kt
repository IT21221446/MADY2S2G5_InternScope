package com.example.feedback_ui.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.feedback_ui.R
import com.example.feedback_ui.model.FeedbackData

class AllRatingsAdapter(var mList: List<FeedbackData>) :
    RecyclerView.Adapter<AllRatingsAdapter.viewHolder>() {

    private lateinit var mListner : onItemClickListner

    //Setting up onClick listener interface
    interface onItemClickListner{
        fun onItemClick( position: Int)
    }

    fun setOnItemClickListner(listner: onItemClickListner){
        mListner = listner
    }

    inner class viewHolder(itemView: View, listner: onItemClickListner) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textView5)
        val rating: RatingBar = itemView.findViewById(R.id.stars)



        init{
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)



        return viewHolder(view, mListner)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.title.text = mList[position].feedback
        holder.rating.rating = mList[position].rating

    }


}