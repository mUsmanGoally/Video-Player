package com.example.videoplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.videoplayer.databinding.FragmentVideosBinding

class VideosFragment : Fragment() {
    private lateinit var binding: FragmentVideosBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentVideosBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {

    }
}