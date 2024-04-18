package com.vincentui.hobbyapp_160421072.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.vincentui.hobbyapp_160421072.databinding.FragmentMainDetailBinding
import com.vincentui.hobbyapp_160421072.viewmodel.DetailViewModel
import java.lang.Exception


class MainDetailFragment : Fragment() {
    private lateinit var binding: FragmentMainDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    var paragraphIndex = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainDetailBinding.inflate(inflater, container, false)
        if(arguments != null) {
            val hobbyDetailbyID =
                MainDetailFragmentArgs.fromBundle(requireArguments()).hobbyId
            detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
            detailViewModel.fetch(hobbyDetailbyID)
            observeHobby()
            observeParagraph()
        }
        binding.btnPreviousParagraph.isEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnNextParagraph.setOnClickListener {
            paragraphIndex++
            observeParagraph()
            val currentPage = paragraphIndex
            val totalParagraphs = detailViewModel.hobbyLD.value?.details?.paragraph?.size ?: 0
            binding.btnNextParagraph.isEnabled = currentPage+1 < totalParagraphs
            binding.btnPreviousParagraph.isEnabled = currentPage+1 > 1
        }

        binding.btnPreviousParagraph.setOnClickListener {
            paragraphIndex--
            observeParagraph()
            val currentIndex = paragraphIndex
            val totalParagraphs = detailViewModel.hobbyLD.value?.details?.paragraph?.size ?: 0
            binding.btnPreviousParagraph.isEnabled = currentIndex+1 > 1
            binding.btnNextParagraph.isEnabled = currentIndex+1 < totalParagraphs
        }


        binding.btnBackMainDetail.setOnClickListener {
            val action = MainDetailFragmentDirections.actionMainDetailFragmentToMainFragment(1)
            Navigation.findNavController(it).navigate(action)
        }
    }
    fun observeHobby() {
        detailViewModel.hobbyLD.observe(viewLifecycleOwner, Observer {
            binding.txtTitleListItem.setText(detailViewModel.hobbyLD.value?.title)
            binding.txtMakerListItem.setText("@" + detailViewModel.hobbyLD.value?.maker)
            binding.txtShortDescListItem.setText(detailViewModel.hobbyLD.value?.shortdesc)
            val picasso = this.context?.let { it1 -> Picasso.Builder(it1) }
            picasso?.listener { picasso, uri, exception ->
                exception.printStackTrace()
            }
            picasso?.build()?.load(detailViewModel.hobbyLD.value?.img)?.into(binding.imgMainDetail)
            picasso?.build()?.load(detailViewModel.hobbyLD.value?.img)
                ?.into(binding.imgMainDetail,
                    object: Callback {
                        override fun onSuccess() {
                            binding.progressImgDetail.visibility = View.INVISIBLE
                            binding.imgMainDetail.visibility = View.VISIBLE
                        }
                        override fun onError(e: Exception?) {
                            Log.e("picasso_error", e.toString())
                        }
                    })
        })
    }

    fun observeParagraph(){
        detailViewModel.hobbyLD.observe(viewLifecycleOwner, Observer {
            binding.txtParagraphMainDetail.setText(detailViewModel.hobbyLD.value?.details?.paragraph?.get(paragraphIndex))
        })
    }
}