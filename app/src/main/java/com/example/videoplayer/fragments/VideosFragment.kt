package com.example.videoplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.videoplayer.adapter.ShowAllVideosAdapter
import com.example.videoplayer.databinding.FragmentVideosBinding

class VideosFragment : Fragment() {
    private lateinit var binding: FragmentVideosBinding
    private lateinit var adapter: ShowAllVideosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideosBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {
        setAdapter()
    }

    private fun setAdapter() {
        val videoList: ArrayList<String> = ArrayList()
        videoList.add("usman")
        videoList.add("irfan")
        videoList.add("ghulam")
        videoList.add("nasir")
        videoList.add("haider")
        videoList.add("haider")
        videoList.add("haider")
        videoList.add("haider")
        videoList.add("haider")
        videoList.add("haider")
        videoList.add("haider")
        videoList.add("haider")
        videoList.add("haider")
        videoList.add("haider")
        videoList.add("haider")
        videoList.add("haider")
        videoList.add("haider")
        videoList.add("haider")

        adapter = ShowAllVideosAdapter(videoList)
        binding.rvTotalVideos.adapter = adapter
    }
}