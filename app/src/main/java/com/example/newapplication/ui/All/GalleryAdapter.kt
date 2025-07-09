package com.example.newapplication.ui.All

import android.view.*
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newapplication.R

//RecyclerView 어댑터: 이미지 리스트를 그리드로 보여주기 위한 어댑터
class GalleryAdapter(
    private val imageList: List<ImagesInfo>, //이미지 정보 리스트
    private val onItemCLick:(Int)->Unit) : //클릭 이번트
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    //ViewHolder 클래스: 각각의 아이템 뷰를 보유하며 재사용 가능하게 함
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageView)

        init{
            //이미지 클릭 시 해당 위치를 onItemClick 콜백으로 전달
            image.setOnClickListener{
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    onItemCLick(position)
                }
            }
        }
    }
//뷰홀더가 생성될 때 호출:item_gallery.xml 레이아웃을 View로 변환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(view)
    }

    //전체 아이템 수 반환
    override fun getItemCount(): Int = imageList.size

    //뷰 홀더에 데이터 바인딩: 해당 위치(position)의 이미지 URL을 ImageView에 로드
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = imageList[position].url

        //비동기로 이미지 로드
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.image)
    }
}
