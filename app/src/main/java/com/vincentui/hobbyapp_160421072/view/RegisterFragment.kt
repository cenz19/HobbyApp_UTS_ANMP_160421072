package com.vincentui.hobbyapp_160421072.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.vincentui.hobbyapp_160421072.R
import com.vincentui.hobbyapp_160421072.databinding.FragmentLoginBinding
import com.vincentui.hobbyapp_160421072.databinding.FragmentRegisterBinding


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
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

}