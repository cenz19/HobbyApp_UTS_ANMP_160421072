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
import com.vincentui.hobbyapp_160421072.model.HobbyDetail

class ListViewModel(application: Application): AndroidViewModel(application) {
    val hobbyLD = MutableLiveData<ArrayList<Hobby>>()
    val hobbyLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()

    val TAG = "vollleyTag"
    private var queue: RequestQueue?= null

    fun refresh() {
        hobbyLoadErrorLD.value = false
        loadingLD.value = true

        queue = Volley.newRequestQueue(getApplication())
        val url = "https://anmp160421072.000webhostapp.com/getAllHobby.php"
//        val url = "http://10.0.2.2/UTSANMP/getAllHobby.php"
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                loadingLD.value = false
                Log.d("showvoley", it)
                val sType = object : TypeToken<List<Hobby>>() { }.type
                val result = Gson().fromJson<List<Hobby>>(it, sType)
                Log.d("showResult", result.toString())
                hobbyLD.value = result as ArrayList<Hobby>?
                loadingLD.value = false
            },
            {
                Log.e("showvoley", it.toString())
                hobbyLoadErrorLD.value = false
                loadingLD.value = false
            })
        queue?.add(stringRequest)
    }
    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}