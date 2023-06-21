package com.example.videoplayer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.videoplayer.R
import com.example.videoplayer.databinding.ListItemVideoViewBinding
import com.example.videoplayer.models.VideoModel
import java.util.concurrent.TimeUnit

class ShowAllVideosAdapter(
    private val context: Context,
    private val videosList: ArrayList<VideoModel>
) : RecyclerView.Adapter<ShowAllVideosAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemVideoViewBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(video: VideoModel) {
            with(binding) {
                tvVideoName.text = video.title
                tvFolderName.text = video.folderName
                tvDuration.text = DateUtils.formatElapsedTime(video.duration/1000)
                Glide.with(context)
                    .asBitmap()
                    .load(video.videoUri)
                    .apply(RequestOptions.placeholderOf(R.mipmap.ic_video_player_round).centerCrop())
                    .into(binding.ivVideo)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemVideoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(videosList[position])
    }

    override fun getItemCount(): Int {
        return videosList.size
    }
}