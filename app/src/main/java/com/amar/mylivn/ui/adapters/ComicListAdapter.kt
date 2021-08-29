package com.amar.mylivn.ui.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.amar.mylivn.R
import com.amar.mylivn.model.Comic
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class ComicListAdapter (private var values: List<Comic>) : RecyclerView.Adapter<ComicListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comic_listview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = values[position]

        Glide.with(holder.imageView.context).clear(holder.imageView)
        Glide.with(holder.imageView)
            .asBitmap()
            .load(String.format("%s.%s", item.thumbnail.path, item.thumbnail.extension))
            .placeholder(R.drawable.ic_image_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    val bitmap = Bitmap.createBitmap(resource, 0, 0, resource.width, resource.height)
                    holder.imageView.setImageBitmap(bitmap)

                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })

        holder.nameTextView.text = item.title
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: AppCompatImageView = view.findViewById(R.id.image)
        val nameTextView: TextView = view.findViewById(R.id.title)
    }

    fun setItems(items: List<Comic>) {
        this.values = items
    }


}