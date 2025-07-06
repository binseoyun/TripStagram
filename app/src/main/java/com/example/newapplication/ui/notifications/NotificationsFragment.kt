package com.example.newapplication.ui.notifications

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.newapplication.databinding.FragmentNotificationsBinding
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//notification에서 버튼 클릭 =>
// openGallery() 를 통해 갤러리에서 이미지 선택 =>
// onActicityResult() => uploadImageToCloudinary() => Cloudinary의 응답 => 성공

    private val PICK_IMAGE_REQUEST = 1
   // private lateinit var imageUrlTextView: TextView
    private lateinit var uploadButton: Button
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       /* val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

*/

        _binding = FragmentNotificationsBinding.inflate(inflater,container,false)
        val root: View = binding.root

        uploadButton = binding.button


        uploadButton.setOnClickListener {
            openGallery()

        }


        return root
    }
    private fun openGallery() {
        //갤러리에서 이미지를 고르게 시스템 갤러리 앱을 띄움, 선택 후
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
        //onActivityResult로 넘어가게 변경
    // onActivityResult(intent,)

    }

    //이미지 선택 결과 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            //사용자가 이미지를 고르면 URL을 imageUri에 저장
            imageUri = data.data
            //imageUri?.let {

            uploadImageToCloudinary(imageUri!!)
            //}
        }
    }

    //이미지 업로드
    private fun uploadImageToCloudinary(imageUri: Uri) {
        val filePath = getRealPathFromURI(imageUri) ?: return
        MediaManager.get().upload(filePath)
            .unsigned("basic_preset")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Log.d("Cloudinary", "업로드 시작")
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    val url = resultData?.get("secure_url").toString()
                    Log.d("Cloudinary", "업로드 성공: $url")
                    Toast.makeText(requireContext(), "URL: $url", Toast.LENGTH_SHORT).show()
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Log.e("Cloudinary", "업로드 실패: ${error?.description}")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            })
            .dispatch()
        val db = FirebaseFirestore.getInstance()

        // 추가할 데이터 정의
        val imageData = hashMapOf(
            "country" to "JAPAN",
            "url" to "https://res.cloudinary.com/your_cloud_name/image/upload/your_image.jpg",
            "user" to "root"
        )

        // 컬렉션 이름은 "images", 문서는 자동 ID로 추가
        db.collection("images")
            .add(imageData)
            .addOnSuccessListener { documentReference ->
                println("문서 추가 성공: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("문서 추가 실패: $e")
            }

    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val cursor = activity?.contentResolver?.query(contentUri, null, null, null, null)
        return if (cursor != null) {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = cursor.getString(idx)
            cursor.close()
            path
        } else {
            contentUri.path
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}