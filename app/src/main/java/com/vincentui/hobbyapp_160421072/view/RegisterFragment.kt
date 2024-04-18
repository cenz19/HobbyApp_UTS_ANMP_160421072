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
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vincentui.hobbyapp_160421072.R
import com.vincentui.hobbyapp_160421072.databinding.FragmentLoginBinding
import com.vincentui.hobbyapp_160421072.databinding.FragmentRegisterBinding
import com.vincentui.hobbyapp_160421072.model.User
import org.json.JSONObject


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBackRegister.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToWelcomeFragment()
            Navigation.findNavController(it).navigate(action)
        }

        binding.btnSignUpRegister.setOnClickListener {
            val username = binding.txtUsernameRegister.text.toString()
            val password = binding.txtPasswordRegister.text.toString()
            val retypePassword = binding.txtRetypePasswordRegister.text.toString()
            val firstName = binding.txtFirstNameRegister.text.toString()
            val lastName = binding.txtLastNameRegister.text.toString()
            register(username,password,retypePassword,firstName,lastName)
        }
    }

    fun register(username:String, password:String, retypepassword:String, firstName:String, lastName:String) {
        val q = Volley.newRequestQueue(context)
        val url = "https://anmp160421072.000webhostapp.com/get_user.php"
//        val url = "http://10.0.2.2/get_user.php"
        val dialog = AlertDialog.Builder(context)
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "ERROR") {
                    if (password == retypepassword) {
                        val regUrl = "https://anmp160421072.000webhostapp.com/create_user.php"
//                        val regUrl = "http://10.0.2.2/UTSANMP/create_user.php"
                        val regStrRequest = object : StringRequest(
                            Request.Method.POST, regUrl,
                            {
                                Log.d("apiresult", it)
                                dialog.setMessage("Thanks for registering\nPlease login with your new account")
                                dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                                    Navigation.findNavController(binding.root).navigate(action)
                                })
                                dialog.create().show()
                            },
                            {
                                Log.e("apiresult", it.printStackTrace().toString())
                                dialog.setMessage("Failed to register")
                                dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                                    dialog.dismiss()
                                })
                                dialog.create().show()
                            }
                        ) {
                            override fun getParams(): MutableMap<String, String>? {
                                val params = HashMap<String, String>()
                                params["firstName"] = firstName
                                params["lastName"] = lastName
                                params["username"] = username
                                params["password"] = password
                                return params
                            }
                        }
                        q.add(regStrRequest)
                    } else {
                        dialog.setMessage("Password doesn't match")
                        dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                        })
                        dialog.create().show()
                    }
                } else if (obj.getString("result") == "OK") {
                    dialog.setMessage("Username already exists")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    dialog.create().show()
                }
            },
            {
                Log.e("apiresult", it.printStackTrace().toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = username
                return params
            }
        }
        q.add(stringRequest)
    }
}