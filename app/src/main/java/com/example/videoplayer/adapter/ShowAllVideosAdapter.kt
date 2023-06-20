package com.example.videoplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayer.databinding.ListItemVideoViewBinding

class ShowAllVideosAdapter(
    private val videosList: ArrayList<String>
) : RecyclerView.Adapter<ShowAllVideosAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListItemVideoViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemVideoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvVideoName.text = videosList[position]
    }

    override fun getItemCount(): Int {
        return videosList.size
    }
}