package com.example.videoplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.videoplayer.R
import com.example.videoplayer.databinding.FragmentPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class PlayerFragment : Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private val args: PlayerFragmentArgs by navArgs()
    private lateinit var player: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        binding.tvVideoTitle.text = args.videoData.title
        binding.tvVideoTitle.isSelected = true
        createPlayer()
        setupListeners()
    }

    private fun createPlayer() {
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player
        val mediaItem = MediaItem.fromUri(args.videoData.videoUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        playVideo()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            player.stop()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnPlayPause.setOnClickListener {
            if (player.isPlaying) pauseVideo() else playVideo()
        }
    }

    private fun playVideo() {
        binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
        player.play()
    }

    private fun pauseVideo() {
        binding.btnPlayPause.setImageResource(R.drawable.ic_play)
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}