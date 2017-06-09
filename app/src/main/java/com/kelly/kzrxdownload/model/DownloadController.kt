package com.kelly.kzrxdownload.model

import android.widget.Button
import android.widget.TextView

import zlc.season.rxdownload2.entity.DownloadEvent
import zlc.season.rxdownload2.entity.DownloadFlag

class DownloadController(private val mStatus: TextView, private val mAction: Button) {
    private var mState: DownloadState? = null

    init {
        setState(Normal())
    }

    fun setState(state: DownloadState) {
        mState = state
        mState!!.setText(mStatus, mAction)
    }

    fun setEvent(event: DownloadEvent) {
        val flag = event.flag
        when (flag) {
            DownloadFlag.NORMAL -> setState(DownloadController.Normal())
            DownloadFlag.WAITING -> setState(DownloadController.Waiting())
            DownloadFlag.STARTED -> setState(DownloadController.Started())
            DownloadFlag.PAUSED -> setState(DownloadController.Paused())
            DownloadFlag.COMPLETED -> setState(DownloadController.Completed())
            DownloadFlag.FAILED -> setState(DownloadController.Failed())
            DownloadFlag.DELETED -> setState(DownloadController.Deleted())
        }
    }

    fun handleClick(callback: Callback) {
        mState!!.handleClick(callback)
    }

    interface Callback {
        fun startDownload()

        fun pauseDownload()

        fun install()
    }

    companion object {
        abstract class DownloadState {
            abstract fun setText(status: TextView, button: Button)
            abstract fun handleClick(callback: Callback)
        }
    }

    class Normal : DownloadState() {

        override fun setText(status: TextView, button: Button) {
            button.text = "下载"
            status.text = ""
        }

        override fun handleClick(callback: Callback) {
            callback.startDownload()
        }
    }

    class Waiting : DownloadState() {
        override fun setText(status: TextView, button: Button) {
            button.text = "等待中"
            status.text = "等待中..."
        }

        override fun handleClick(callback: Callback) {
            callback.pauseDownload()
        }
    }

    class Started : DownloadState() {
        override fun setText(status: TextView, button: Button) {
            button.text = "暂停"
            status.text = "下载中..."
        }

        override fun handleClick(callback: Callback) {
            callback.pauseDownload()
        }
    }

    class Paused : DownloadState() {
        override fun setText(status: TextView, button: Button) {
            button.text = "继续"
            status.text = "已暂停"
        }

        override fun handleClick(callback: Callback) {
            callback.startDownload()
        }
    }

    class Failed : DownloadState() {
        override fun setText(status: TextView, button: Button) {
            button.text = "继续"
            status.text = "下载失败"
        }

        override fun handleClick(callback: Callback) {
            callback.startDownload()
        }
    }


    class Completed : DownloadState() {
        override fun setText(status: TextView, button: Button) {
            button.text = "安装"
            status.text = "下载已完成"
        }

        override fun handleClick(callback: Callback) {
            callback.install()
        }
    }

    class Deleted : DownloadState() {

        override fun setText(status: TextView, button: Button) {
            button.text = "下载"
            status.text = "下载已取消"
        }

        override fun handleClick(callback: Callback) {
            callback.startDownload()
        }
    }
}
