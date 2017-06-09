package com.kelly.kzrxdownload.adapter

import android.view.ViewGroup
import com.kelly.kzrxdownload.model.AppInfoBean
import com.kelly.kzrxdownload.viewholder.AppInfoViewHolder

import zlc.season.practicalrecyclerview.AbstractAdapter

class AppInfoAdapter : AbstractAdapter<AppInfoBean, AppInfoViewHolder>() {
    override fun onNewCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoViewHolder {
        return AppInfoViewHolder(parent)
    }

    override fun onNewBindViewHolder(holder: AppInfoViewHolder, position: Int) {
        holder.setData(get(position))
    }
}