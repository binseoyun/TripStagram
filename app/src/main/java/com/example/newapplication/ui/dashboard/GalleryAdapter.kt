package com.example.newapplication.ui.dashboard

import android.view.*
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.newapplication.R

class GalleryAdapter(private val imageList: List<Int>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageResource(imageList[position])
    }
}
