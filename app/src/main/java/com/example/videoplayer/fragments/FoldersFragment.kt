package com.example.videoplayer.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.videoplayer.activities.MainActivity
import com.example.videoplayer.adapter.FoldersAdapter
import com.example.videoplayer.databinding.FragmentFoldersBinding

class FoldersFragment : Fragment() {
    private lateinit var binding: FragmentFoldersBinding
    private lateinit var adapter: FoldersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoldersBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Video Player"
        binding.tvTotalFolders.text = "Total Folders: ${MainActivity.foldersList.size}"
        setAdapter()
    }

    private fun setAdapter() {
        adapter = FoldersAdapter(MainActivity.foldersList) { position ->
            val action =
                FoldersFragmentDirections.actionFoldersFragmentToFolderRelatedVideoFragment(position)
            findNavController().navigate(action)
        }
        binding.rvTotalFolders.adapter = adapter
    }


}