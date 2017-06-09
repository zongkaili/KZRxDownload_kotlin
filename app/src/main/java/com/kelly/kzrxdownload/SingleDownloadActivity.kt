//package com.kelly.kzrxdownload
//
//import android.databinding.DataBindingUtil
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import com.kelly.kzrxdownload.model.DownloadController
//import zlc.season.rxdownload2.RxDownload
//
//class SingleDownloadActivity : AppCompatActivity() {
//    var mRxDownload: RxDownload = null!!
//    var mDownloadController: DownloadController
//    var binding: ActivityServiceDownloadBinding? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_single_download)
//
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_download)
//        binding.setItem(serviceModel)
//        binding.contentServiceDownload.setPresenter(Presenter())
//
//        mRxDownload = RxDownload.getInstance(this)
//        mDownloadController = DownloadController(binding.contentServiceDownload.status, binding.contentServiceDownload.action)
//    }
//
//    inner class Presenter {
//        fun onClick(view: View) {
//            mDownloadController.handleClick(object : DownloadController.Callback() {
//                fun startDownload() {
//                    start()
//                }
//
//                fun pauseDownload() {
//                    pause()
//                }
//
//                fun install() {
//                    installApk()
//                }
//            })
//        }
//
//        fun onClickFinish(view: View) {
//            this@ServiceDownloadActivity.finish()
//        }
//    }
//}
