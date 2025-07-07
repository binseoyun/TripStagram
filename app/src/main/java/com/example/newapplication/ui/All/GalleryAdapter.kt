package com.example.newapplication.ui.All

import android.view.*
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newapplication.R


class GalleryAdapter(private val imageList: List<ImagesInfo>,
    private val onItemCLick:(Int)->Unit) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageView)
        init{
            image.setOnClickListener{
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    onItemCLick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = imageList[position].url
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.image)
    }
}
