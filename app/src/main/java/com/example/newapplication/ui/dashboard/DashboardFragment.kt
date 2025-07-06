package com.example.newapplication.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapplication.DetailActivity
import com.example.newapplication.DetailPhotoActivity
import com.example.newapplication.R
import com.example.newapplication.databinding.FragmentDashboardBinding
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
//추가
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter
    //
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //추가한 부분
        val db = FirebaseFirestore.getInstance()
        val imageList = mutableListOf<ImagesInfo>()
        db.collection("images").get()
            .addOnSuccessListener{ result ->
                for(document in result){
                    val country = document.getString("country")?:""
                    val user = document.getString("user")?:""
                    val url = document.getString("url")?:""
                    imageList.add(ImagesInfo(country, user, url))
                }

                Log.d("Firestore", "총 ${imageList.size}개의 이미지 로드됨")
                imageList.forEach {
                    Log.d("Firestore", it.toString())
                }
                recyclerView=binding.galleryView
                //랜덤 이미지 리스트
                val images= listOf(
                    R.drawable.gallerykorea, R.drawable.germany, R.drawable.china,
                    R.drawable.taiwanpng, R.drawable.england, R.drawable.japan
                ) //랜덤 이미지 생성

                adapter=GalleryAdapter(imageList,{pos->
                    Toast.makeText(requireContext(),pos.toString(),Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(),DetailPhotoActivity::class.java)
                    intent.putExtra("itemText",pos.toString())
                    startActivity(intent)
                })
                //그레이드 레이아웃으로 한 줄3분할
                recyclerView.layoutManager=GridLayoutManager(requireContext(),3)
                recyclerView.adapter=adapter
            }.addOnFailureListener{
                e->Log.e("Firestore","데이터 가져오기 실패",e)
            }




        //추가한 부분

        //

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}