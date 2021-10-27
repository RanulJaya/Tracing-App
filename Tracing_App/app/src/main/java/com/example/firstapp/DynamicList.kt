package com.example.firstapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DynamicList(private val mList: ArrayList<ItemsViewModel>) :
    RecyclerView.Adapter<DynamicList.ViewHolder>() {


    var context:Context? = null
    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_design, parent, false)

        this.context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel: ItemsViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.textView.text = itemsViewModel.text

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context , ShowActivity::class.java)
            intent.putExtra("list", itemsViewModel.text)
            context?.startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        return mList.size
    }


}