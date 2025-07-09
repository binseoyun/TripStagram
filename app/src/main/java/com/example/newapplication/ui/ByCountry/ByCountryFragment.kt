package com.example.newapplication.ui.ByCountry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.ListViewAdapter
import com.example.myapplication.ListViewModel
import com.example.newapplication.R
import com.example.newapplication.databinding.FragmentBycountryBinding
import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.example.newapplication.ui.ByCountry.ByCountryFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView

//"국가별 보기"탭을 구성하는 Fragment
class ByCountryFragment : Fragment() {
    //ListView 변수
    private lateinit var listView: ListView

    //ViewBinding용 변수
    private var _binding: FragmentBycountryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

     //ViewBinding 설정
        _binding = FragmentBycountryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //ListView 가져오기
        val listView = binding.listVIew

        //하단 탭바(nav_view)의 높이만큼 패딩 추가해서 콘텐츠 가림 방지
        listView.viewTreeObserver.addOnGlobalLayoutListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            listView.setPadding(0, 0, 0, bottomNav.height)
        }

        //보여줄 나라 목록 데이터 생성
        val items = listOf(
            ListViewModel("대한민국",R.drawable.korea),
            ListViewModel("일본",R.drawable.japan),
            ListViewModel("영국",R.drawable.england),
            ListViewModel("프랑스",R.drawable.french),
            ListViewModel("중국",R.drawable.china),
            ListViewModel("싱가포르",R.drawable.singapore),
            ListViewModel("대만",R.drawable.taiwanpng),
            ListViewModel("필리핀",R.drawable.philippines),
            ListViewModel("미국",R.drawable.usa),
            ListViewModel("베트남",R.drawable.vetnampng),
            ListViewModel("호주",R.drawable.australia),
            ListViewModel("캐나다",R.drawable.canada),
            ListViewModel("브라질",R.drawable.brazil),
            ListViewModel("몽골",R.drawable.mongolia),
            ListViewModel("독일",R.drawable.germany)
            )
       //커스텀 어댑터를 생성해서 ListView에 연결
        val adapter = ListViewAdapter(items)
        listView.adapter = adapter
        listView.setOnItemClickListener{_,_,position,_ ->
            //val clickedItem = items[position]

            //국가명을 가지고 이동
            val action = ByCountryFragmentDirections.
                   actionBycountryToAllBycountryFragment(items[position].text)
            findNavController().navigate(action)
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}