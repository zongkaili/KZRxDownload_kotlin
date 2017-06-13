package com.kelly.kzrxdownload.adapter

import android.view.ViewGroup
import com.kelly.kzrxdownload.model.AppInfoBean
import com.kelly.kzrxdownload.viewholder.AppInfoViewHolder

import zlc.season.practicalrecyclerview.AbstractAdapter

class AppInfoAdapter : AbstractAdapter<AppInfoBean, AppInfoViewHolder>() {
    private var mOldPosition = 0
    override fun onNewCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoViewHolder {
        return AppInfoViewHolder(parent)
    }

    override fun onNewBindViewHolder(holder: AppInfoViewHolder, position: Int) {
        holder.setData(get(position))
    }

    override fun onViewAttachedToWindow(holder: AppInfoViewHolder?) {
        if(holder?.layoutPosition!! > mOldPosition) {
            holder.addItemAnimation()
            mOldPosition = holder.layoutPosition
        }
    }
}