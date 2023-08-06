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
import com.example.videoplayer.adapter.VideosAdapter
import com.example.videoplayer.databinding.FragmentVideosBinding

class VideosFragment : Fragment() {
    private lateinit var binding: FragmentVideosBinding

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var adapter: VideosAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideosBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Video Player"
        binding.tvTotalVideos.text = "Total Videos: ${MainActivity.videoList.size}"
        setAdapter()
    }

    private fun setAdapter() {
        adapter = VideosAdapter(requireContext(), MainActivity.videoList) { position ->
            val action =
                VideosFragmentDirections.actionVideosFragmentToPlayerFragment(MainActivity.videoList[position])
            findNavController().navigate(action)
        }
        binding.rvTotalVideos.adapter = adapter
    }

}