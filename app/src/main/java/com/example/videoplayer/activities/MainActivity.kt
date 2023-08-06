package com.example.videoplayer.activities

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.videoplayer.R
import com.example.videoplayer.databinding.ActivityMainBinding
import com.example.videoplayer.extentions.toast
import com.example.videoplayer.fragments.VideosFragment
import com.example.videoplayer.models.FolderModel
import com.example.videoplayer.models.VideoModel
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var toggle: ActionBarDrawerToggle

    companion object {
        val videoList: ArrayList<VideoModel> = ArrayList()
        val foldersList: ArrayList<FolderModel> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setupBottomNav()
        requestPermission()
        setupNavigationDrawer()
        onDrawerItemSelected()
        changeBottomNavVisibility()
    }

    private fun changeBottomNavVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.videosFragment -> binding.bottomNav.visibility = View.VISIBLE
                R.id.foldersFragment -> binding.bottomNav.visibility = View.VISIBLE
                else -> binding.bottomNav.visibility = View.GONE
            }
        }
    }

    private fun setupBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_MEDIA_VIDEO
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_VIDEO)
            } else getAllVideos()
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else getAllVideos()
        }
    }

    private fun setupNavigationDrawer() {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onDrawerItemSelected() {
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_feedback -> {
                    toast("Feedback")
                }

                R.id.item_theme -> {
                    toast("Theme")
                }

                R.id.item_sort_order -> {
                    toast("Sort Order")
                }

                R.id.item_about -> {
                    toast("About")
                }

                R.id.item_exit -> {
                    toast("Exit")
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("Recycle", "Range", "NotifyDataSetChanged")
    private fun getAllVideos() {
        val folderNameList: ArrayList<String> = ArrayList()

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

        val cursor = this.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
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
                    val folderId =
                        itCursor.getString(itCursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
                    val path =
                        itCursor.getString(itCursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    val duration =
                        itCursor.getString(itCursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                            .toLong()

                    try {
                        val file = File(path)
                        val uri = Uri.fromFile(file)
                        val video = VideoModel(id, title, duration, folderName, size, path, uri)

                        if (!folderNameList.contains(folderName)) {
                            folderNameList.add(folderName)
                            foldersList.add(FolderModel(id = folderId, folderName = folderName))
                        }

                        if (file.exists()) videoList.add(video)
                        VideosFragment.adapter.notifyDataSetChanged()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } while (itCursor.moveToNext())
            }
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                getAllVideos()
            } else requestPermission()
        }

}