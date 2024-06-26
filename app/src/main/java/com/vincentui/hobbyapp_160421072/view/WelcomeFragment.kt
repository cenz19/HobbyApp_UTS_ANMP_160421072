package com.vincentui.hobbyapp_160421072.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.vincentui.hobbyapp_160421072.R
import com.vincentui.hobbyapp_160421072.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private lateinit var binding:FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLoginWelcome.setOnClickListener{
            val actionToLogin = WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment()
            Navigation.findNavController(it).navigate(actionToLogin)
        }

        binding.btnSignUpWelcome.setOnClickListener {
            val actionToRegister = WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment()
            Navigation.findNavController(it).navigate(actionToRegister)
        }
    }

}