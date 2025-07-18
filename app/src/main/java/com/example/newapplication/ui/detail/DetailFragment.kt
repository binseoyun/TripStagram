package com.example.newapplication.ui.detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newapplication.R
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

        val db = FirebaseFirestore.getInstance()

        //해당 리스트에서 선택받도록 아이디 연결
        val selectedURl = args.url


        db.collection("comment")
            .whereEqualTo("url", selectedURl) // 선택한 국가만 조회
            .get()
            .addOnSuccessListener { result ->
                val commentList = mutableListOf<CommentInfo>()

                for (document in result) {

                    val url = document.getString("url") ?: ""
                    val comment=document.getString("comment")?:""
                    val userId=document.getString("userId")?:""

                    commentList.add(CommentInfo(userId, url,comment))
                    println(userId)
                    val textView = TextView(requireContext())
                    textView.text = "$userId: $comment" // userId와 comment를 합쳐서 텍스트로 설정
                    textView.textSize = 16f // 원하는 크기로 조정 가능
                    textView.setPadding(8, 8, 8, 8) // 패딩 추가 (선택 사항)

// commentListContainer에 TextView 추가
                    view.findViewById<LinearLayout>(R.id.commentListContainer).addView(textView)
                }}

        view.findViewById<Button>(R.id.buttonadd).setOnClickListener{

           val comments=view.findViewById<TextView>(R.id.editTextComment).text.toString()

            //아이디를 받아옴
            val sharedPreferences: SharedPreferences =requireContext().getSharedPreferences("UserPrefs",
                Context.MODE_PRIVATE)
            val userId=sharedPreferences.getString("userId",null)
            val androidId = Settings.Secure.getString(
                requireContext().contentResolver,
                Settings.Secure.ANDROID_ID
            )
            println(userId)


            val commentData= hashMapOf(

                "url" to args.url,
                "comment" to comments,
                "userId" to userId,
                "androidId" to androidId
            )

            val db = FirebaseFirestore.getInstance()
            // 컬렉션 이름은 "images", 문서는 자동 ID로 추가
            db.collection("comment")
                .add(commentData)
                .addOnSuccessListener { documentReference ->
                    println("문서 추가 성공: ${documentReference.id}")
                    //빈칸으로 초기화
                    view.findViewById<TextView>(R.id.editTextComment).text=" "
                    val textView = TextView(requireContext())
                    textView.text = "$userId: $comments" // userId와 comment를 합쳐서 텍스트로 설정
                    textView.textSize = 16f // 원하는 크기로 조정 가능
                    textView.setPadding(8, 8, 8, 8) // 패딩 추가 (선택 사항)

// commentListContainer에 TextView 추가
                    view.findViewById<LinearLayout>(R.id.commentListContainer).addView(textView)
                }
                .addOnFailureListener { e ->
                    println("문서 추가 실패: $e")
                    Toast.makeText(requireContext(),"add comment failed", Toast.LENGTH_SHORT)
                }

        }
        return view

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //inflater.inflate(R.menu.template_toolbar_menu, menu)

        // 사용자 ID 등 조건에 따라 삭제 버튼 숨김/표시
        if(Settings.Secure.getString(
            requireContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )==args.androidId){
            menu.findItem(R.id.menu_delete)?.isVisible = true
        }else
            menu.findItem(R.id.menu_delete)?.isVisible = false
    }

    fun delete(){
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this post?")
            .setPositiveButton("Yes") { _, _ ->
                val db = FirebaseFirestore.getInstance()

                db.collection("comment") // 컬렉션 이름 (예: posts)
                    .whereEqualTo("url", args.url) // url이 일치하는 문서 찾기
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot) {
                            db.collection("comment").document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Document deleted successfully: ${document.id}")

                                }
                                .addOnFailureListener { e ->
                                    Log.w("Firestore", "Error deleting document", e)
                                }
                        }
                        db.collection("images")
                            .whereEqualTo("url",args.url)
                            .get()
                            .addOnSuccessListener { querySnapshot->
                                for(document in querySnapshot){
                                    db.collection("images").document(document.id)
                                        .delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(requireContext(),"삭제성공",Toast.LENGTH_SHORT).show()
                                            findNavController().navigateUp()
                                        }
                                }
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error getting documents: ", e)
                    }
            }
            .setNegativeButton("No", null)
            .show()
    }



}