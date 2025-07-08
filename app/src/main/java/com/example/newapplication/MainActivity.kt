package com.example.newapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.InputType
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

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = prefs.getString("userId", null)

        if (userId == null) {
            showUserIdInputDialog()
        } else {
            //이미 등록된 사용자
            startAppNormally(userId)
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val config: HashMap<String, String> = HashMap()
        config["cloud_name"] = "djsbyqbek"
        MediaManager.init(this, config)

        //toolbar2 아이디의 Toolbar를 앱의 label의 ActionBar 제목으로 자동 설정
        val toolbar = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar)

        //색상 변경
        //toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))

    }

    private fun showUserIdInputDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("사용자 등록")

        val input = EditText(this)
        input.hint = "아이디를 입력하세요"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("등록") { dialog, _ ->
            val userId = input.text.toString().trim()
            if (userId.isNotEmpty()) {
                //userID가 이미 존재gksekaus prefs에서 아이디를 가져와서 userid로
                val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                prefs.edit().putString("userId", userId).apply()
                startAppNormally(userId)

            } else {
                Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT)
                showUserIdInputDialog() //아이디 입력 받게
            }
        }
        builder.setCancelable(false)
        builder.show()
    }

    //아이디 저장되어 있을 때
    private fun startAppNormally(userId: String) {
        Toast.makeText(this, "환영합니다,$userId 닙!", Toast.LENGTH_SHORT).show()

    }
    //아이디 반환
     fun getUserId(context: Context):String?{
        val sharedPreferences:SharedPreferences=context.getSharedPreferences("prefs",Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId",null)
    }


}