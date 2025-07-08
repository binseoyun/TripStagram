package com.example.newapplication.ui.Upload

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.newapplication.R
import com.example.newapplication.databinding.FragmentUploadBinding
import com.google.firebase.firestore.FirebaseFirestore

class UploadFragment : Fragment() {

    private var _binding: FragmentUploadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//드롭바
    private lateinit var countrySpinner : Spinner
    //여행지 정보 입력
    private lateinit var locationInfo: EditText

    //Rating Bar
    private lateinit var ratingBar: RatingBar

    private lateinit var imagePreview: ImageView

    //전체 등록버튼
    private lateinit var submitButton: Button






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



        _binding = FragmentUploadBinding.inflate(inflater,container,false)
        val root: View = binding.root

        //spinner
        countrySpinner = binding.spinner
        //이미지 업로드 버튼
        uploadButton = binding.button
        //여행지 정보 입력
        locationInfo=binding.editTextCountryInfo
        //별점
        ratingBar=binding.ratingBar
        //이미지 미리보기 버튼
        imagePreview=binding.imageView2
        //등록 버튼
        submitButton=binding.button3



        //이미지 선택 버튼
        uploadButton.setOnClickListener {
            checkAndRequestStoragePermission()

        }

        submitButton.setOnClickListener{
            uploadImageToCloudinary(imageUri!!)
        }
        val navView = requireActivity().findViewById<View>(R.id.nav_view)
        val scrollView = binding.scrollView// ScrollView에 id 부여 필요
        navView.post {
            scrollView.setPadding(
                scrollView.paddingLeft,
                scrollView.paddingTop,
                scrollView.paddingRight,
                navView.height
            )
            scrollView.clipToPadding = false
        }

        return root
    }
    private fun openGallery() {
        //갤러리에서 이미지를 고르게 시스템 갤러리 앱을 띄움, 선택 후
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)



    }

    //이미지 선택 결과 처리
    //여기서 imagePreview가 호출되게 코드를 바꾸고, uploadImageToCloudinary함수 호출을 등록 버튼으로 변경함
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            //사용자가 이미지를 고르면 URL을 imageUri에 저장
            imageUri = data.data
            imagePreview.setImageURI(imageUri)

            // 버튼 배경색을 파란색으로 변경
            val buttonSelectImages = view?.findViewById<Button>(R.id.button)
            buttonSelectImages?.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blackblue))

                //ContextCompat.getColorStateList(requireContext(), android.R.color.holo_blue_light)
            )
            binding.scrollView.fullScroll(View.FOCUS_DOWN)



        }
    }

    private fun checkAndRequestStoragePermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission), 1001)
        } else {
            openGallery()
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

                    val db = FirebaseFirestore.getInstance()

                    // 추가할 데이터 정의
                    //받아온 값으로 저장하게 지정
                    val selectedCountry=countrySpinner.selectedItem.toString()

                    //EditText에서 텍스트 가져오기
                   val locationInfo = binding.editTextCountryInfo.text.toString()

                    //별점 기능
                    val starBar=binding.ratingBar.rating.toInt().toString()

                    val imageData= hashMapOf(
                        "country" to selectedCountry,
                        "url" to url,
                        "locationInfo" to locationInfo,
                        "starbar" to starBar,
                        "user" to "root"
                    )


                    // 컬렉션 이름은 "images", 문서는 자동 ID로 추가
                    db.collection("images")
                        .add(imageData)
                        .addOnSuccessListener { documentReference ->
                            println("문서 추가 성공: ${documentReference.id}")
                            val action= UploadFragmentDirections.actionUploadToDetailFragment(locationInfo,selectedCountry,url)
                            findNavController().navigate(action)
                        }
                        .addOnFailureListener { e ->
                            println("문서 추가 실패: $e")
                            Toast.makeText(requireContext(),"Upload Failed",Toast.LENGTH_SHORT)
                        }
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Log.e("Cloudinary", "업로드 실패: ${error?.description}")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            })
            .dispatch()



        //업로드 성공하면 성공이라고 알림창

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