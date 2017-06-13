package com.kelly.kzrxdownload.viewholder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.tbruyelle.rxpermissions2.RxPermissions

import butterknife.ButterKnife
import io.reactivex.functions.Consumer
import zlc.season.practicalrecyclerview.AbstractViewHolder
import zlc.season.rxdownload2.RxDownload
import zlc.season.rxdownload2.entity.DownloadBean
import zlc.season.rxdownload2.entity.DownloadFlag
import zlc.season.rxdownload2.function.Utils

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.v7.widget.CardView
import android.view.View
import butterknife.bindView
import com.facebook.drawee.view.SimpleDraweeView
import com.kelly.kzrxdownload.R
import com.kelly.kzrxdownload.model.AppInfoBean
import com.kelly.kzrxdownload.model.DownloadController
import com.twobbble.tools.FrescoUtil
import zlc.season.rxdownload2.function.Utils.log
import kotlin.reflect.KProperty

class AppInfoViewHolder(parent: ViewGroup) : AbstractViewHolder<AppInfoBean>(parent, R.layout.item_app_info) {
    var mHead: SimpleDraweeView by bindView(R.id.head)
    var mTitle: TextView by bindView(R.id.title)
    var mContent: TextView by bindView(R.id.content)
    var mAction: Button by bindView(R.id.action)
    var mContentImg: SimpleDraweeView by bindView(R.id.mContentImg)
    var mItemCard: CardView by bindView(R.id.mItemCard)

    private var mData: AppInfoBean? = null
    private val mDownloadController: DownloadController
    private val mContext: Context
    private val mRxDownload: RxDownload
    private var downloadBean: DownloadBean? = null
    private var flag: Int = 0

    init {
        ButterKnife.bind(this, itemView)
        mContext = parent.context
        mRxDownload = RxDownload.getInstance(mContext)
        mDownloadController = DownloadController(TextView(mContext), mAction!!)
        bindEvent()
    }

    override fun setData(data: AppInfoBean) {
        this.mData = data
        FrescoUtil.frescoLoadCircle(mHead, data.avatar)
        FrescoUtil.frescoLoadNormal(mContentImg, null,data.img, null)
        mTitle!!.setText(data.name)
        mContent!!.setText(data.info)

        downloadBean = DownloadBean.Builder(data.downloadUrl)
                .setSaveName(null)      //not need.
                .setSavePath(null)      //not need
                .setExtra1(mData!!.avatar)   //save extra info into database.
                .setExtra2(mData!!.name)  //save extra info into database.
                .build()

        Utils.log(mData!!.downloadUrl)
        mData!!.disposable = mRxDownload.receiveDownloadStatus(mData!!.downloadUrl)
                .subscribe { downloadEvent ->
                    if (flag != downloadEvent.flag) {
                        flag = downloadEvent.flag
                        log(flag.toString() + "")
                    }

                    if (downloadEvent.flag == DownloadFlag.FAILED) {
                        val throwable = downloadEvent.error
                        Log.w("TAG", throwable)
                    }
                    mDownloadController.setEvent(downloadEvent)
                }
    }

    private fun bindEvent() {
        mAction.setOnClickListener { v: View? -> mDownloadController.handleClick(object : DownloadController.Callback {
            override fun startDownload() {
                start()
            }

            override fun pauseDownload() {
                pause()
            }

            override fun install() {
                installApk()
            }
        }) }
    }

    private fun start() {
        RxPermissions.getInstance(mContext)
                .request(WRITE_EXTERNAL_STORAGE)
                .doOnNext(Consumer<Boolean> { granted ->
                    if (!granted) {
                        throw RuntimeException("no permission")
                    }
                })
                .compose(mRxDownload.transformService<Boolean>(downloadBean))
                .subscribe(Consumer<Any> { Toast.makeText(mContext, "下载开始", Toast.LENGTH_SHORT).show() })
    }

    private fun pause() {
        mRxDownload.pauseServiceDownload(mData!!.downloadUrl).subscribe()
    }

    private fun installApk() {
        val files = mRxDownload.getRealFiles(mData!!.downloadUrl)
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

    fun addItemAnimation() {
        val scaleX = ObjectAnimator.ofFloat(mItemCard, "translationY", 500f, 0f)
        val scaleY = ObjectAnimator.ofFloat(mItemCard, "scaleY", 0.5f, 1f)
        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY)
        scaleX.duration = 1000
        scaleY.duration = 1000
        scaleX.start()
        scaleY.start()
    }
}

private operator fun Any.setValue(appInfoViewHolder: AppInfoViewHolder, property: KProperty<*>, cardView: CardView) {}
private operator fun Any.setValue(appInfoViewHolder: AppInfoViewHolder, property: KProperty<*>, simpleDraweeView: SimpleDraweeView) {}
private operator fun Any.setValue(appInfoViewHolder: AppInfoViewHolder, property: KProperty<*>, textView: TextView) {}
private operator fun Any.setValue(appInfoViewHolder: AppInfoViewHolder, property: KProperty<*>, button: Button) {}
