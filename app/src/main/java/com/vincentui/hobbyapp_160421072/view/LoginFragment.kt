package com.vincentui.hobbyapp_160421072.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vincentui.hobbyapp_160421072.databinding.FragmentLoginBinding
import com.vincentui.hobbyapp_160421072.model.User
import org.json.JSONObject


class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBackLogin.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToWelcomeFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.btnLoginLogin.setOnClickListener {
            val username = binding.txtUsernameLogin.text.toString()
            val password = binding.txtPasswordLogin.text.toString()
            cek_login(username, password)
        }
    }

    fun cek_login(username:String, password:String) {
        val q = Volley.newRequestQueue(context)
        val url = "https://anmp160421072.000webhostapp.com/cek_login.php"
//        val url = "http://10.0.2.2/UTSANMP/cek_login.php"
        val dialog = AlertDialog.Builder(context)
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            {
                Log.d("login", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    if (data.length() > 0) {
                        val dataUser = data.getJSONObject(0)
                        val sType = object : TypeToken<User>() { }.type
                        val user = Gson().fromJson(dataUser.toString(), sType) as User
                        Log.d("loginResult", user.toString())
                        dialog.setMessage("Login Successful, Welcome ${user.username}")
                        dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->


                            val action = LoginFragmentDirections.actionLoginFragmentToMainFragment(user.id!!.toInt())
                            Navigation.findNavController(binding.root).navigate(action)


                        })
                        dialog.create().show()
                    }
                } else{
                    dialog.setMessage("Username or Password is incorrect")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    dialog.create().show()
                }
            },
            {
                Log.e("apiresult", it.printStackTrace().toString())
                dialog.setMessage("Username or Password is incorrect")
                dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                dialog.create().show()
            }
        )
        {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }
        q?.add(stringRequest)
    }
}
