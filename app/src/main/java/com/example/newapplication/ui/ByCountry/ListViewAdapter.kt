package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import com.example.newapplication.R

/*ListView에서 각 항목(나라 + 이미지)를 표시하는 어댑터
  ByCountryFragment.kt에서
  val adapter = ListViewAdapter(items)
  listView.adapter = adapter
를 통해 ListView가 자동으로 각 나라와 이미지를 보여줌
 */
class ListViewAdapter(private val items: List<ListViewModel>) : BaseAdapter() {

    //항목의 총 개수 반환
    override fun getCount() = items.size

    //특정 position의 아이템 반환
    override fun getItem(position: Int) = items[position]

    //고유 ID 반환
    override fun getItemId(position: Int) = position.toLong()

    //실질적으로 뷰를 생성하거나 재사용해서 ListView에 표시
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        // convertView가 null이면 새로 inflate, 아니면 재사용 (리스트 최적화)
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_listview, parent, false)

        //텍스트뷰와 연결해서 나라 이름 설정
        val textView = view.findViewById<TextView>(R.id.textItem)
        textView.text = items[position].text

        //이미지뷰와 연결해서 이미지 설정
        val image = view.findViewById<ImageView>(R.id.galleryItem)
       image.setImageResource(items[position].img)


        return view
    }
}