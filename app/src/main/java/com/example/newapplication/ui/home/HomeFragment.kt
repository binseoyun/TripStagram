package com.example.newapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.ListViewAdapter
import com.example.myapplication.ListViewModel
import com.example.newapplication.DetailActivity
import com.example.newapplication.R
import com.example.newapplication.databinding.FragmentHomeBinding
import android.content.Intent

//홈 탭에 ListView를 띄우기 위한 프래그먼트
class HomeFragment : Fragment() {

    //추가한 부분
    private lateinit var listView: ListView


    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val listView = binding.listVIew

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

        val adapter = ListViewAdapter(items)
        listView.adapter = adapter
        listView.setOnItemClickListener{_,_,position,_ ->
            val clickedItem = items[position]
            val intent = Intent(requireContext(),DetailActivity::class.java)
            intent.putExtra("itemText",clickedItem.text)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}