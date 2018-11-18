package com.shivam.android.photofinder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shivam.android.photofinder.R
import com.shivam.android.photofinder.utils.PhotoFinderLog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_grid_image.view.*

class ImageGridRecyclerAdapter(var context: Context, var imagesUrlList: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        PhotoFinderLog.d("ImageGridRecyclerAdapter onCreateViewHolder ")

        var view = LayoutInflater.from(context).inflate(R.layout.item_grid_image, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {

        PhotoFinderLog.d("ImageGridRecyclerAdapter getItemCount size "+imagesUrlList.size)

        return imagesUrlList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        PhotoFinderLog.d("ImageGridRecyclerAdapter onBindViewHolder pos = $position")

        Picasso.get()
                .load(imagesUrlList[position])
                .placeholder(R.drawable.grey_background_drawable)
                .error(R.drawable.grey_background_drawable)
                .into(holder.itemView.grid_image_view)

    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}