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
import com.example.newapplication.databinding.FragmentNotificationsBinding

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
        val file = File(filePath)

//Cloudinary는 file과 upload_preset가 필요

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
            .addFormDataPart("upload_preset", "ml_default") // 사전 설정한 preset
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/ml_default/CLOUDINARY_URL=cloudinary://<your_api_key>:<your_api_secret>@daw58uvei/image/upload")
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("Upload", "Failed")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseData = response.body?.string()
                Log.d("Upload", "Response: $responseData")
                /*activity?.runOnUiThread {
                    imageUrlTextView.text = "업로드 성공!\n$responseData"
                }*/
            }
        })
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