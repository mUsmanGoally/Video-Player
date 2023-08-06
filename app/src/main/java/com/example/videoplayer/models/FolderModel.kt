package com.example.videoplayer.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FolderModel(
    val id: String,
    val folderName: String
) : Parcelable