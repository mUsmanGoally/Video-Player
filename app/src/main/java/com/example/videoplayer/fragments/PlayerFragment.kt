package com.example.videoplayer.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.videoplayer.R
import com.example.videoplayer.activities.MainActivity
import com.example.videoplayer.databinding.FragmentPlayerBinding
import com.example.videoplayer.models.VideoModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import java.io.File

class PlayerFragment : Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private val args: PlayerFragmentArgs by navArgs()
    private lateinit var player: ExoPlayer
    private var playerList: ArrayList<VideoModel> = ArrayList()
    private var videoPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {
        initPlayerList()
        setFullScreen()
        videoPosition = args.actualPosition
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        binding.tvVideoTitle.text = playerList[videoPosition].title
        binding.tvVideoTitle.isSelected = true
        createPlayer()
        setupListeners()
    }

    private fun setFullScreen() {
        activity?.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(
                window,
                binding.root
            ).hide(WindowInsetsCompat.Type.systemBars())
        }
    }

    private fun initPlayerList() {
        playerList = if (args.position != -1) {
            getAllVideos(MainActivity.foldersList[args.position].id)
        } else MainActivity.videoList
    }

    private fun createPlayer() {
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player
        val mediaItem = MediaItem.fromUri(playerList[videoPosition].videoUri)
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

        binding.btnNext.setOnClickListener {
            nextPrevVideo(true)
        }

        binding.btnPrev.setOnClickListener {
            nextPrevVideo(false)
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

    private fun nextPrevVideo(isNext: Boolean) {
        if (isNext) setPosition(true)
        else setPosition(false)
        player.stop()
        createPlayer()
        binding.tvVideoTitle.text = playerList[videoPosition].title
    }

    private fun setPosition(isIncrement: Boolean) {
        if (isIncrement) {
            if (videoPosition == playerList.size - 1) {
                videoPosition = 0
            } else ++videoPosition
        } else {
            if (videoPosition == 0) {
                videoPosition = playerList.size - 1
            } else --videoPosition
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    @SuppressLint("Recycle", "Range", "NotifyDataSetChanged")
    fun getAllVideos(folderId: String): ArrayList<VideoModel> {
        val videoList: ArrayList<VideoModel> = ArrayList()

        val selection = MediaStore.Video.Media.BUCKET_ID + " like? "
        val projection = arrayOf(
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION
        )

        val cursor = requireActivity().contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            arrayOf(folderId),
            MediaStore.Video.Media.DATE_ADDED + " DESC"
        )

        cursor?.use { itCursor ->
            if (itCursor.moveToNext()) {
                do {
                    val title =
                        itCursor.getString(itCursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val size =
                        itCursor.getString(itCursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val id = itCursor.getString(itCursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val folderName =
                        itCursor.getString(itCursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val path =
                        itCursor.getString(itCursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    val duration =
                        itCursor.getString(itCursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                            .toLong()

                    try {
                        val file = File(path)
                        val uri = Uri.fromFile(file)
                        val video = VideoModel(id, title, duration, folderName, size, path, uri)

                        if (file.exists()) videoList.add(video)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } while (itCursor.moveToNext())
            }
        }
        return videoList
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onResume() {
        super.onResume()
        player.play()
    }

}