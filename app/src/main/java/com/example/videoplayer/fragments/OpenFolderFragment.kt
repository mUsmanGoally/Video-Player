package com.example.videoplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.videoplayer.databinding.FragmentOpenFolderBinding

class OpenFolderFragment : Fragment() {
    private lateinit var binding: FragmentOpenFolderBinding
    private val args: OpenFolderFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOpenFolderBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = args.folderName
    }

}