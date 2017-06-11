package com.kelly.kzrxdownload.viewholder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.ListPopupWindow
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions2.RxPermissions

import java.io.File

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import zlc.season.practicalrecyclerview.AbstractAdapter
import zlc.season.practicalrecyclerview.AbstractViewHolder
import zlc.season.rxdownload2.RxDownload
import zlc.season.rxdownload2.entity.DownloadEvent
import zlc.season.rxdownload2.entity.DownloadFlag
import zlc.season.rxdownload2.entity.DownloadStatus
import zlc.season.rxdownload2.function.Utils

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import butterknife.bindView
import com.kelly.kzrxdownload.R
import com.kelly.kzrxdownload.model.DownloadController
import com.kelly.kzrxdownload.model.DownloadItem
import zlc.season.rxdownload2.function.Utils.dispose
import zlc.season.rxdownload2.function.Utils.empty
import zlc.season.rxdownload2.function.Utils.log
import kotlin.reflect.KProperty

class DownloadViewHolder(parent: ViewGroup, private val mAdapter: AbstractAdapter<*, *>) : AbstractViewHolder<DownloadItem>(parent, R.layout.item_download_manager) {
    var mImg: ImageView by bindView(R.id.img)
    var mPercent: TextView by bindView(R.id.percent)
    var mProgress: ProgressBar by bindView(R.id.progress)
    var mSize: TextView by bindView(R.id.size)
    var mStatusText: TextView by bindView(R.id.status)
    var mActionButton: Button by bindView(R.id.action)
    var mName: TextView by bindView(R.id.name)
    var mMore: Button by bindView(R.id.more)

    private val mDownloadController: DownloadController
    private val mContext: Context
    private var data: DownloadItem? = null
    private val mRxDownload: RxDownload
    private var flag: Int = 0

    init {
        ButterKnife.bind(this, itemView)
        mContext = parent.context
        mRxDownload = RxDownload.getInstance(mContext)
        mDownloadController = DownloadController(mStatusText!!, mActionButton!!)
        bindEvent()
    }

    override fun setData(param: DownloadItem) {
        this.data = param
        if (empty(param.record.getExtra1())) {
            Picasso.with(mContext).load(R.mipmap.ic_file_download).into(mImg)
        } else {
            Picasso.with(mContext).load(param.record.getExtra1()).into(mImg)
        }

        val name = if (empty(param.record.getExtra2())) param.record.getSaveName() else param.record.getExtra2()
        mName!!.setText(name)


        Utils.log(data!!.record.getUrl())
        data!!.disposable = mRxDownload.receiveDownloadStatus(data!!.record.getUrl())
                .subscribe { downloadEvent ->
                    if (flag != downloadEvent.flag) {
                        flag = downloadEvent.flag
                        log(flag.toString() + "")
                    }

                    if (downloadEvent.flag == DownloadFlag.FAILED) {
                        val throwable = downloadEvent.error
                        Log.w("TAG", throwable)
                    }
                    mDownloadController?.setEvent(downloadEvent)
                    updateProgressStatus(downloadEvent.downloadStatus)
                }
    }

    private fun bindEvent() {
        mActionButton?.setOnClickListener { v: View? ->
            mDownloadController?.handleClick(object : DownloadController.Callback {
                override fun startDownload() {
                    start()
                }

                override fun pauseDownload() {
                    pause()
                }

                override fun install() {
                    installApk()
                }
            })
        }
        mMore?.setOnClickListener { v: View? -> showPopUpWindow(v!!) }
    }

    /*@OnClick(R.id.action, R.id.more)
    fun onClick(view: View) {
        when (view.id) {
            R.id.action -> mDownloadController.handleClick(object : DownloadController.Callback() {
                fun startDownload() {
                    start()
                }

                fun pauseDownload() {
                    pause()
                }

                fun install() {
                    installApk()
                }
            })
            R.id.more -> showPopUpWindow(view)
        }
    }*/

    private fun updateProgressStatus(status: DownloadStatus) {
        mProgress!!.isIndeterminate = status.isChunked
        mProgress!!.max = status.totalSize.toInt()
        mProgress!!.progress = status.downloadSize.toInt()
        mPercent!!.text = status.percent
        mSize!!.text = status.formatStatusString
    }

    private fun installApk() {
        val files = mRxDownload.getRealFiles(data!!.record.getUrl())
        if (files != null) {
            val uri = Uri.fromFile(files[0])
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            mContext.startActivity(intent)
        } else {
            Toast.makeText(mContext, "File not exists", Toast.LENGTH_SHORT).show()
        }
    }

    private fun start() {
        RxPermissions.getInstance(mContext)
                .request(WRITE_EXTERNAL_STORAGE)
                .doOnNext { granted ->
                    if (!granted) {
                        throw RuntimeException("no permission")
                    }
                }
                .compose<Any>(mRxDownload.transformService<Boolean>(data!!.record.getUrl()))
                .subscribe { Toast.makeText(mContext, "下载开始", Toast.LENGTH_SHORT).show() }
    }

    private fun pause() {
        mRxDownload.pauseServiceDownload(data!!.record.getUrl()).subscribe()
    }

    private fun delete() {
        dispose(data!!.disposable)
        mRxDownload.deleteServiceDownload(data!!.record.getUrl(), true)
                .doFinally { mAdapter.remove(adapterPosition) }
                .subscribe()

    }

    private fun showPopUpWindow(view: View) {
        val listPopupWindow = ListPopupWindow(mContext)
        listPopupWindow.setAdapter(ArrayAdapter(mContext, android.R.layout.simple_list_item_1,
                arrayOf("删除")))
        listPopupWindow.setOnItemClickListener { parent, view, pos, id ->
            if (pos == 0) {
                delete()
                listPopupWindow.dismiss()
            }
        }
        listPopupWindow.width = 200
        listPopupWindow.anchorView = view
        listPopupWindow.isModal = false
        listPopupWindow.show()
    }
}

private operator fun Any.setValue(downloadViewHolder: DownloadViewHolder, property: KProperty<*>, progressBar: ProgressBar) {}

private operator fun Any.setValue(downloadViewHolder: DownloadViewHolder, property: KProperty<*>, textView: TextView) {}

private operator fun Any.setValue(downloadViewHolder: DownloadViewHolder, property: KProperty<*>, imageView: ImageView) {}
