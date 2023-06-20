package com.example.videoplayer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.videoplayer.R
import com.example.videoplayer.databinding.FragmentFoldersBinding

class FoldersFragment : Fragment() {
    private lateinit var binding: FragmentFoldersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFoldersBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {

    }


}