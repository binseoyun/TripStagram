package com.example.newapplication.ui.detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newapplication.R
import com.example.newapplication.ui.Upload.UploadFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class DetailFragment : Fragment() {

    // SafeArgs로 전달받은 값 받기
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_detail.xml inflate
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        setHasOptionsMenu(true)
        // TextView에 값 설정
        val textView: TextView = view.findViewById(R.id.textLocationName)
        textView.text = args.locationName
        view.findViewById<TextView>(R.id.textCountry).text=args.countryName
        view.findViewById<TextView>(R.id.textDescription).text=args.locationInfoDetail
        view.findViewById<TextView>(R.id.textAuthor).text=args.user
        view.findViewById<RatingBar>(R.id.ratingBar).rating=args.starbar.toInt().toFloat()
        Glide.with(requireContext())
            .load(args.url)
            .into(view.findViewById<ImageView>(R.id.imageLocation))
        val bottomNavHeight = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).height
        view.findViewById<ScrollView>(R.id.scrollDetailView).setPadding(0, 0, 0, bottomNavHeight)
        view.findViewById<ScrollView>(R.id.scrollDetailView).clipToPadding=true
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Information"
        }

        view.findViewById<Button>(R.id.buttonadd).setOnClickListener{

           val comments=view.findViewById<TextView>(R.id.editTextComment).text.toString()

            //아이디를 받아옴
            val sharedPreferences: SharedPreferences =requireContext().getSharedPreferences("UserPrefs",
                Context.MODE_PRIVATE)
            val userId=sharedPreferences.getString("userId",null)

            println(userId)


            val commentData= hashMapOf(

                "url" to args.url,
                "comment" to comments,
                "userId" to userId
            )

            val db = FirebaseFirestore.getInstance()
            // 컬렉션 이름은 "images", 문서는 자동 ID로 추가
            db.collection("comment")
                .add(commentData)
                .addOnSuccessListener { documentReference ->
                    println("문서 추가 성공: ${documentReference.id}")
                    //빈칸으로 초기화
                    view.findViewById<TextView>(R.id.editTextComment).text=" "
                }
                .addOnFailureListener { e ->
                    println("문서 추가 실패: $e")
                    Toast.makeText(requireContext(),"add comment failed", Toast.LENGTH_SHORT)
                }

        }
        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            title = "TripStagram"
        }
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