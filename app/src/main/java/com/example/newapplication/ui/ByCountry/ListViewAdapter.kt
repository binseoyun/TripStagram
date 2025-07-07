package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import com.example.newapplication.R

class ListViewAdapter(private val items: List<ListViewModel>) : BaseAdapter() {
    override fun getCount() = items.size
    override fun getItem(position: Int) = items[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(parent?.context)

            .inflate(R.layout.item_listview, parent, false)

        val textView = view.findViewById<TextView>(R.id.textItem)
        textView.text = items[position].text

        //이미지 부분 추가
        val image = view.findViewById<ImageView>(R.id.galleryItem)
       image.setImageResource(items[position].img)


        return view
    }
}