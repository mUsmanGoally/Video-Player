package com.example.videoplayer.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.videoplayer.activities.MainActivity
import com.example.videoplayer.adapter.VideosAdapter
import com.example.videoplayer.databinding.FragmentFolderRelatedVideoBinding
import com.example.videoplayer.models.VideoModel
import java.io.File

class FolderRelatedVideoFragment : Fragment() {
    private lateinit var binding: FragmentFolderRelatedVideoBinding
    private lateinit var adapter: VideosAdapter
    private val args: FolderRelatedVideoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFolderRelatedVideoBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        val folderPosition = MainActivity.foldersList[args.folderPosition]
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = folderPosition.folderName
        binding.tvTotalVideos.text = "Total Videos: ${getAllVideos(folderPosition.id).size}"

        setAdapter()
    }

    private fun setAdapter() {
        val videoList = getAllVideos(MainActivity.foldersList[args.folderPosition].id)
        adapter =
            VideosAdapter(
                requireContext(),
                videoList
            ) { position ->
                val action =
                    FolderRelatedVideoFragmentDirections.actionFolderRelatedVideoFragmentToPlayerFragment2(
                        args.folderPosition,
                        position
                    )
                findNavController().navigate(action)
            }
        binding.rvTotalVideos.adapter = adapter
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
                    val id =
                        itCursor.getString(itCursor.getColumnIndex(MediaStore.Video.Media._ID))
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

                        if (file.exists())
                            videoList.add(video)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } while (itCursor.moveToNext())
            }
        }
        return videoList
    }
}