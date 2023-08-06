package com.example.videoplayer.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoModel(
    val id: String,
    val title: String,
    val duration: Long = 0,
    val folderName: String,
    val size: String,
    val path: String,
    val videoUri: Uri
) : Parcelable