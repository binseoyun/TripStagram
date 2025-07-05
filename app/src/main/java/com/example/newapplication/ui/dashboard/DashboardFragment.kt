package com.example.newapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapplication.R
import com.example.newapplication.databinding.FragmentDashboardBinding

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



        //추가한 부분
        recyclerView=binding.galleryView
        //랜덤 이미지 리스트
        val images= listOf(
            R.drawable.gallerykorea, R.drawable.germany, R.drawable.china,
            R.drawable.taiwanpng, R.drawable.england, R.drawable.japan
        ) //랜덤 이미지 생성

        adapter=GalleryAdapter(images)
        //그레이드 레이아웃으로 한 줄3분할
        recyclerView.layoutManager=GridLayoutManager(requireContext(),3)
        recyclerView.adapter=adapter
        //

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}