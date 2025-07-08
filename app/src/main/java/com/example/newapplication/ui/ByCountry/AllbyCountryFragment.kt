package com.example.newapplication.ui.ByCountry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newapplication.R
import com.example.newapplication.ui.All.AllFragmentDirections
import com.example.newapplication.ui.All.GalleryAdapter
import com.example.newapplication.ui.All.ImagesInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class AllbyCountryFragment : Fragment() {

    // SafeArgs로 전달받은 값 받기
    private val args: AllbyCountryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_detail.xml inflate
        val view = inflater.inflate(R.layout.fragment_all_bycountry, container, false)
        setHasOptionsMenu(true)
        // TextView에 값 설정
        //println(args.countryName) //이게 나라이름!!!!

        val db = FirebaseFirestore.getInstance()

        //해당 리스트에서 선택받도록 아이디 연결
        val selectedCountry = args.countryName


        db.collection("images")
            .whereEqualTo("country", selectedCountry) // 선택한 국가만 조회
            .get()
            .addOnSuccessListener { result ->
                val imageList = mutableListOf<ImagesInfo>()

                for (document in result) {
                    val country = document.getString("country") ?: ""
                    val location = document.getString("locationinfo") ?: ""
                    val locationInfoDetail=document.getString("locationInfoDetail")?:""
                    val url = document.getString("url") ?: ""
                    val starbar= document.getString("starbar")?.toInt() ?: 0
                    val userId=document.getString("userId")?:""

                    imageList.add(ImagesInfo(country, userId, url,starbar,location,locationInfoDetail))
                }

                Log.d("Firestore", "${selectedCountry} 이미지 ${imageList.size}개 불러옴")

                val recyclerView = view.findViewById<RecyclerView>(R.id.galleryView)
                recyclerView.setPadding(0, 0, 0, requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).height)
                recyclerView.clipToPadding = false
                recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

                val adapter = GalleryAdapter(imageList) { pos ->
                   val action = AllbyCountryFragmentDirections.actionAllbycountryToDetailFragment(
                        locationName = imageList[pos].locationinfo,
                        countryName = imageList[pos].country,
                        locationInfoDetail = imageList[pos].locationInfoDetail,
                        url = imageList[pos].url,
                       user=imageList[pos].user,
                       starbar=imageList[pos].starbar.toString()

                    )
                    findNavController().navigate(action)
                }

                recyclerView.adapter = adapter
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "데이터 불러오기 실패", e)
            }


        return view

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}