package com.vincentui.hobbyapp_160421072.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vincentui.hobbyapp_160421072.model.Hobby

class DetailViewModel(application: Application): AndroidViewModel(application) {
    val hobbyLD = MutableLiveData<Hobby>()
    val hobbyLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()

    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    fun fetch(student_id:String) {
        loadingLD.value = true
        hobbyLoadErrorLD.value = false

        queue = Volley.newRequestQueue(getApplication())
        val url = "https://anmp160421072.000webhostapp.com/getDetailHobby.php?id=$student_id"
//        val url = "http://10.0.2.2/UTSANMP/getDetailHobby.php?id=$student_id"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            {
                Log.d("showvoleydetail", it)
                val sType = object : TypeToken<Hobby>() { }.type
                val result = Gson().fromJson<Hobby>(it, sType)
                hobbyLD.value = result
                loadingLD.value = false
                Log.d("showvoleydetailResult", result.toString())
            },
            {
                Log.d("showvoleydetail", it.toString())
                hobbyLoadErrorLD.value = false
                loadingLD.value = false
            })

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}