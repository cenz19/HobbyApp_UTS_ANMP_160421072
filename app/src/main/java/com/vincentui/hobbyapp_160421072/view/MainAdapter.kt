package com.vincentui.hobbyapp_160421072.view

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.vincentui.hobbyapp_160421072.databinding.HobbyListItemBinding
import com.vincentui.hobbyapp_160421072.model.Hobby
import java.lang.Exception

class MainAdapter(val hobbyList:ArrayList<Hobby>)
    :RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    class MainViewHolder(var binding:HobbyListItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = HobbyListItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return hobbyList.size
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.binding.txtMakerListItem.text = "@" + hobbyList[position].maker
        holder.binding.txtTitleReadItem.text = hobbyList[position].title
        holder.binding.txtShortDescListItem.text = hobbyList[position].shortdesc



        val picasso = Picasso.Builder(holder.itemView.context)
        picasso.listener{picasso, uri, exception ->
            exception.printStackTrace()
        }
        picasso.build().load(hobbyList[position].img).into(holder.binding.imgItemList)
        picasso.build().load(hobbyList[position].img)
            .into(holder.binding.imgItemList,
                object: Callback {
                    override fun onSuccess() {
                        holder.binding.progressImg.visibility = View.INVISIBLE
                        holder.binding.imgItemList.visibility = View.VISIBLE
                    }
                    override fun onError(e: Exception?) {
                        Log.e("picasso_error", e.toString())
                    }
                })

        holder.binding.btnReadMoreListItem.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToMainDetailFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }
    fun updateHobbyList(newStudentList: ArrayList<Hobby>) {
        hobbyList.clear()
        hobbyList.addAll(newStudentList)
        notifyDataSetChanged()
    }
}