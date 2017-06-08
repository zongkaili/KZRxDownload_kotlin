package com.kelly.kzrxdownload

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import zlc.season.rxdownload2.RxDownload

class MultiDownloadActivity : AppCompatActivity() {
    var mRxDownload: RxDownload = null!!
//    var mDownloadController: DownloadController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_download)

        mRxDownload = RxDownload.getInstance(this)

    }
}
