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
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import java.io.File

class PlayerFragment : Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private val args: PlayerFragmentArgs by navArgs()
    private lateinit var player: ExoPlayer
    private var playerList: ArrayList<VideoModel> = ArrayList()
    private var videoPosition = 0
    private var repeatVideo: Boolean = false
    private var isFullScreen: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    @SuppressLint("PrivateResource")
    private fun init() {
        if (repeatVideo) {
            binding.btnRepeat.setImageResource(com.google.android.exoplayer2.ui.R.drawable.exo_controls_repeat_all)
        } else binding.btnRepeat.setImageResource(com.google.android.exoplayer2.ui.R.drawable.exo_controls_repeat_off)
        initPlayerList()
        setFullScreen()
        videoPosition = args.actualPosition
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        binding.tvVideoTitle.text = playerList[videoPosition].title
        binding.tvVideoTitle.isSelected = true
        createPlayer()
        setupListeners()
    }

    private fun createPlayer() {
        try {
            player.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player
        val mediaItem = MediaItem.fromUri(playerList[videoPosition].videoUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        playVideo()
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_ENDED) nextPrevVideo(true)
            }
        })
        playInFullScreen(isFullScreen)
    }

    private fun playInFullScreen(enable: Boolean) {
        if (enable) {
            binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            binding.btnFullScreen.setImageResource(R.drawable.ic_fullscreen_exit)
        } else {
            binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            binding.btnFullScreen.setImageResource(R.drawable.ic_fullscreen)
        }
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

    @SuppressLint("PrivateResource")
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

        binding.btnRepeat.setOnClickListener {
            if (repeatVideo) {
                repeatVideo = false
                player.repeatMode = Player.REPEAT_MODE_OFF
                binding.btnRepeat.setImageResource(com.google.android.exoplayer2.ui.R.drawable.exo_controls_repeat_one)
            } else {
                repeatVideo = true
                player.repeatMode = Player.REPEAT_MODE_ONE
                binding.btnRepeat.setImageResource(com.google.android.exoplayer2.ui.R.drawable.exo_controls_repeat_all)
            }
        }

        binding.btnFullScreen.setOnClickListener {
            if (isFullScreen) {
                isFullScreen = false
                playInFullScreen(false)
            } else {
                isFullScreen = true
                playInFullScreen(true)
            }
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
        if (!repeatVideo) {
            when (isNext) {
                true -> {
                    if (videoPosition == playerList.size - 1) {
                        videoPosition = 0
                    } else ++videoPosition
                }

                false -> {
                    if (videoPosition == 0) {
                        videoPosition = playerList.size - 1
                    } else --videoPosition
                }
            }
            createPlayer()
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