package com.example.videoplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayer.callback.SimpleCallback
import com.example.videoplayer.databinding.ListItemFolderViewBinding
import com.example.videoplayer.models.FolderModel

class ShowAllFoldersAdapter(
    private val foldersList: ArrayList<FolderModel>,
    private val simpleCallback: SimpleCallback
) : RecyclerView.Adapter<ShowAllFoldersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListItemFolderViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                simpleCallback.simpleCallback(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemFolderViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foldersList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvFolderName.text = foldersList[position].folderName
    }
}