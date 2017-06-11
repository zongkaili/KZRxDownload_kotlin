package com.kelly.kzrxdownload.adapter

import android.view.ViewGroup

import com.kelly.kzrxdownload.model.DownloadItem
import com.kelly.kzrxdownload.viewholder.DownloadViewHolder

import zlc.season.practicalrecyclerview.AbstractAdapter

class DownloadAdapter : AbstractAdapter<DownloadItem, DownloadViewHolder>() {
    override fun onNewCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        return DownloadViewHolder(parent, this)
    }

    override fun onNewBindViewHolder(holder: DownloadViewHolder, position: Int) {
        holder.setData(get(position))
    }
}
