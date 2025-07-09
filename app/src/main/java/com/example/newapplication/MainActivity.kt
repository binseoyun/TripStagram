package com.example.newapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cloudinary.android.MediaManager
import com.example.newapplication.databinding.ActivityMainBinding
import com.google.firebase.database.collection.LLRBNode.Color
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //viewBinding 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SharedPreferences를 통해 지정된 사용자 ID를 확인
        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = prefs.getString("userId", null)

        //사용자 ID가 없으면 입력 창이 뜨게 만듦
        if (userId == null) {
            showUserIdInputDialog() //입력 다이얼로그 표시
        } else {
            //이미 사용자가 등록되어 있다면(사용자 ID가 있다면) 앱 정상 실행
            startAppNormally(userId)
        }

        //하단 네비게이션 설정
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main) //NavController(Fragment 간의 이동을 제어하는 객체)를 찾음



/*
        //최상단 Fragment 지정(뒤로 가기 버튼이 필요 없는 최상위 화면)
        val appBarConfiguration = AppBarConfiguration( //탭 이동시 자동으로 뒤로가기 버튼이 안보이게 처리
            setOf(
                R.id.navigation_home, //By Country
                R.id.navigation_dashboard, //All
                R.id.navigation_notifications //Upload
            )
        )

        //아래 주석처리된 함수는 자동으로 각 탭의 title,memu,button에 맞게 toolbar를 커스텀마이즈
        //setupActionBarWithNavController(navController, appBarConfiguration)

        //onSupportNavigateUp()를 통해 최상단 Fragment제외 자동 뒤로가기 <- 생성
*/

        //하단 탭 바와 navController 연결
        navView.setupWithNavController(navController)

        //기본 뒤로 가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Cloudinary 초기화
        val config: HashMap<String, String> = HashMap()
        config["cloud_name"] = "djsbyqbek"
        MediaManager.init(this, config)

        //커스텀 Toolbar 설정
        val toolbar = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar) //액션바처럼 작동하게 설정

    }

    //Toolbar 메뉴를 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(
            R.menu.template_toolbar_menu, //toolbar에 커스텀 추가
            menu
        )


        return true
    }


//툴바의 뒤로가기 버튼을 눌렀을 때 네비게이션을 뒤로 보내는 함수(최상단 Fragment 제외 자동 적용)
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    //Toolbar 버튼 클릭 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
            //toolbar_button 클릭시 사용자 ID 입력 다이얼로그 호출
            R.id.toolbar_button -> {
                val view=findViewById<View>(R.id.toolbar_button)
                Toast.makeText(this,"클릭",Toast.LENGTH_SHORT).show()
                showUserIdInputDialog()
            }

             android.R.id.home -> {
                 findNavController(R.id.nav_host_fragment_activity_main).navigateUp()

             }
             //뒤로가기 처리

        }
        return true
    }



/*사용자 ID 입력을 받는 다이얼로그
AlertDialog를 띄워 사용자로부터 아이디를 입력 받고,
 유효시 SharedPreferences에 저장 or 다시 입력 요구*/
    private fun showUserIdInputDialog() {

        val builder = AlertDialog.Builder(this) //AlertDialog를 띄움(아이디 입력 창)
        builder.setTitle("사용자 등록")

        val input = EditText(this) //텍스트 필드 생성 후 아이디 입력 받음
        input.hint = "아이디를 입력하세요"
        input.inputType = InputType.TYPE_CLASS_TEXT //일반 텍스트로 입력
        builder.setView(input) //다이얼로그 내부 뷰로 추가

    //등록 버튼 클릭 시
        builder.setPositiveButton("등록") { dialog, _ ->
            val userId = input.text.toString().trim()

            if (userId.isNotEmpty()) {
                val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE) //SharedPreferences에 "UserPrefs"로 접근
                prefs.edit().putString("userId", userId).apply() //userId 키에 사용자가 입력한 아이디 저장
                startAppNormally(userId) //앱 정상 실행

            } else {
                Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT)
                showUserIdInputDialog() //아이디 입력 다이얼로그 호출
            }
        }
        //builder.setCancelable(false) //바깥 터치로 다이얼로그를 끌 수 없게 함(반드시 입력하게)
        builder.show() //다이얼로그 화면에 표시
    }

    //앱 정상 실행(사용자 ID 존재)
    private fun startAppNormally(userId: String) {
        Toast.makeText(this, "환영합니다,$userId 님!", Toast.LENGTH_SHORT).show()

    }

    //아이디 반환
     /*fun getUserId(context: Context):String?{
        val sharedPreferences:SharedPreferences=context.getSharedPreferences("prefs",Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId",null)
    }*/


}