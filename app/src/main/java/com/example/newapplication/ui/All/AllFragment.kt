package com.example.newapplication.ui.All

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapplication.R
import com.example.newapplication.databinding.FragmentAllBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.bottomnavigation.BottomNavigationView
/*
Firestore 연동 =>"images" collection 접근
RecyclerView + GridLayout => 3열 구성
Navigation으로 DetailFragment 이동

*/

class AllFragment : Fragment() {

    private var _binding: FragmentAllBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //사용 X
        val dashboardViewModel =
            ViewModelProvider(this).get(AllViewModel::class.java)

        //ViewBinding 연결
        _binding = FragmentAllBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Firebase에서 이미지 데이터를 가져오기
        val db = FirebaseFirestore.getInstance()
        val imageList = mutableListOf<ImagesInfo>()

        //"images" 컬렉션에서 모든 문서 조회
        db.collection("images").get()
            .addOnSuccessListener{ result ->
                for(document in result){
                    //각 필드의 값 추출
                    val country = document.getString("country")?:""
                    val user = document.getString("user")?:""
                    val url = document.getString("url")?:""
                    val locationInfo = document.getString("locationInfo")?:""
                    val locationInfoDetail=document.getString("locationInfoDetail")
                    val starbar = document.getString("starbar")?.toIntOrNull()?:0

                    //데이터를 리스트에 추가
                    imageList.add(ImagesInfo(
                            country, user, url,starbar,locationInfo,locationInfoDetail.toString())
                    )
                }

                Log.d("Firestore", "총 ${imageList.size}개의 이미지 로드됨")
                imageList.forEach {
                    Log.d("Firestore", it.toString())
                }

                //RecyclerView 연결
                recyclerView=binding.galleryView

                //하단 Bottomnavigation 높이를 고려한 패딩 설정
                val bottomNavHeight = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).height
                recyclerView.setPadding(0, 0, 0, bottomNavHeight)
                recyclerView.clipToPadding = false

                //Adapter 설정(클릭 시 DetailFragment로 이동)
                adapter=GalleryAdapter(imageList,{pos->
                    val action = AllFragmentDirections.actionNavigationDashboardToDetailFragment(locationName = imageList[pos].locationinfo, countryName = imageList[pos].country, locationInfoDetail = imageList[pos].locationinfo,url=imageList[pos].url, user = imageList[pos].user,starbar=imageList[pos].starbar.toString())
                    findNavController().navigate(action)
                })

                //한 줄에 3칸씩 표시하는 그리드 레이아웃 매니저 적용
                recyclerView.layoutManager=GridLayoutManager(requireContext(),3)
                recyclerView.adapter=adapter

            }
            .addOnFailureListener{ //Firestore에서 데이터 로드 실패 시
                e->Log.e("Firestore","데이터 가져오기 실패",e)
            }
        return root
    }
//Fragment가 파괴될 때 ViewBinding 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}