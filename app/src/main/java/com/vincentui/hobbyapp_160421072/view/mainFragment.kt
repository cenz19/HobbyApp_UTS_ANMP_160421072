package com.vincentui.hobbyapp_160421072.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.vincentui.hobbyapp_160421072.R
import com.vincentui.hobbyapp_160421072.databinding.FragmentMainBinding
import com.vincentui.hobbyapp_160421072.viewmodel.ListViewModel

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: ListViewModel
    private val hobbyListAdapter  = MainAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
//        val id = MainFragmentArgs.fromBundle(requireArguments()).id
//        binding.txtHello.setText("$id")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()
        binding.recView.layoutManager = LinearLayoutManager(context)
        binding.recView.adapter = hobbyListAdapter

        observeViewModel();

        binding.refreshLayout.setOnRefreshListener {
            binding.recView.visibility = View.GONE
            binding.txtError.visibility = View.GONE
            binding.progressLoad.visibility = View.VISIBLE
            viewModel.refresh()
            binding.refreshLayout.isRefreshing = false

        }
        binding.btnProfileMain.setOnClickListener {
            if(arguments != null) {
                val id = MainFragmentArgs.fromBundle(requireArguments()).id
                val action = MainFragmentDirections.actionMainFragmentToProfileFragment(id)
                Navigation.findNavController(it).navigate(action)
            }
        }
    }
    fun observeViewModel() {
        viewModel.hobbyLD.observe(viewLifecycleOwner, Observer {
            hobbyListAdapter.updateHobbyList(it)
        })

        viewModel.hobbyLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.txtError?.visibility = View.VISIBLE
            } else {
                binding.txtError?.visibility = View.GONE
            }
        })
        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.recView.visibility = View.GONE
                binding.progressLoad.visibility = View.VISIBLE
            } else {
                binding.recView.visibility = View.VISIBLE
                binding.progressLoad.visibility = View.GONE
            }
        })
    }
}