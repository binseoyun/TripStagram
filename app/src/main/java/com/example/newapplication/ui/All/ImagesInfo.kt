package com.example.newapplication.ui.All
//Firestore에서 가져온 이미지를 담음
data class ImagesInfo(val country:String,val user:String, val url:String, val starbar:Int, val locationinfo:String,val locationInfoDetail:String,val androidId:String)

/*
AllFragment에서 Firestore데이터를 불러와 ImageInfo 객체로 변환 후 리스트에 저장
GalleryAdapter에서 이 리스트를 사용해 이미지 표시
클릭 이벤트에서 이 데이터를 DetailFragment로 전달
*/