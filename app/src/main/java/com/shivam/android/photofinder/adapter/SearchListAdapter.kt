package com.shivam.android.photofinder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shivam.android.photofinder.R
import com.shivam.android.photofinder.utils.PhotoFinderLog
import kotlinx.android.synthetic.main.item_search_list.view.*

class SearchListAdapter(var context: Context, var searchList: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        PhotoFinderLog.d("SearchListAdapter onCreateViewHolder ")

        var view = LayoutInflater.from(context).inflate(R.layout.item_search_list, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {

        PhotoFinderLog.d("SearchListAdapter getItemCount size " + searchList.size)

        return searchList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.tv_search_item.text = searchList[position]
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}