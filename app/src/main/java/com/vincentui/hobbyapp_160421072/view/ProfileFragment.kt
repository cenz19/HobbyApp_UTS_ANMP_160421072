package com.vincentui.hobbyapp_160421072.view

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vincentui.hobbyapp_160421072.R
import com.vincentui.hobbyapp_160421072.databinding.FragmentProfileBinding
import com.vincentui.hobbyapp_160421072.model.User
import org.json.JSONObject

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        if(arguments != null) {
            val id = ProfileFragmentArgs.fromBundle(requireArguments()).idUser
    //        binding.textView21.setText("$id")
            getUser_byID(id)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackProfile.setOnClickListener {
            if(arguments != null) {
                val id = ProfileFragmentArgs.fromBundle(requireArguments()).idUser
                val action = ProfileFragmentDirections.actionProfileFragmentToMainFragment(id)
                Navigation.findNavController(it).navigate(action)
            }
        }
        binding.btnUpdateProfile.setOnClickListener {
            if(arguments != null) {
                val old_pass = binding.txtOldPasswordProfile.text.toString()
                val new_pass = binding.txtNewPasswordProfile.text.toString()
                val user_id = ProfileFragmentArgs.fromBundle(requireArguments()).idUser
                val firstName = binding.txtFirstNameProfile.text.toString()
                val lastName = binding.txtLastNameProfile.text.toString()
                updateUser(old_pass,new_pass,user_id, firstName,lastName)
            }
        }
        binding.btnLogOutProfile.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToWelcomeFragment()
            Navigation.findNavController(it).navigate(action)
        }

    }

    fun updateUser(old_pass: String, new_pass: String, users_id: Int, firstName:String, lastName:String) {
        val q = Volley.newRequestQueue(activity)
        val url = "https://anmp160421072.000webhostapp.com/update_profile.php"
//        val url = "http://10.0.2.2/UTSANMP/update_profile.php"
        val dialog = AlertDialog.Builder(requireActivity())

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    dialog.setMessage("Successfully changed the password")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        clearData()
                        dialog.dismiss()
                    })
                    dialog.create().show()
                } else {
                    dialog.setMessage("Cannot change the password\nPlease check again")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        clearData()
                        dialog.dismiss()
                    })
                    dialog.create().show()
                }
            },
            {
                Log.e("apiresult", it.toString())
                dialog.setMessage("Cannot change the password\nPlease check again")
                dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    clearData()
                    dialog.dismiss()
                })
                dialog.create().show()
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["old_pass"] = old_pass
                params["new_pass"] = new_pass
                params["id"] = users_id.toString()
                params["firstName"] = firstName
                params["lastName"] = lastName
                return params
            }
        }
        q.add(stringRequest)
    }

    fun getUser_byID(userID:Int) {
        val q = Volley.newRequestQueue(context)
        val url = "https://anmp160421072.000webhostapp.com/get_user_by_id.php"
//        val url = "http://10.0.2.2/UTSANMP/get_user_by_id.php"
        val dialog = android.app.AlertDialog.Builder(context)
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            {
                Log.d("profile", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    if (data.length() > 0) {
                        val dataUser = data.getJSONObject(0)
                        val sType = object : TypeToken<User>() { }.type
                        val user = Gson().fromJson(dataUser.toString(), sType) as User
                        Log.d("profileResult", user.toString())
                        binding.txtFirstNameProfile.setText(user.firstName.toString())
                        binding.txtLastNameProfile.setText(user.lastName.toString())
                    }
                } else if (obj.getString("result") == "ERROR") {
                    dialog.setMessage("Username is not found")
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
                params["id"] = userID.toString()
                return params
            }
        }
        q.add(stringRequest)
    }

    fun clearData() {
        binding.txtOldPasswordProfile.text?.clear()
        binding.txtNewPasswordProfile.text?.clear()
    }
}