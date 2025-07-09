package com.example.newapplication.ui.ByCountry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//Fragment회전 시에도 데이터 유지가 필요할 때,비동기 데이터 처리할 때(Firebase,API) 사용
class ByCountryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text


}