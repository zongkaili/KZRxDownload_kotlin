package com.kelly.kzrxdownload

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View

import java.util.ArrayList

import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.bindView
import com.kelly.kzrxdownload.adapter.DownloadAdapter
import com.kelly.kzrxdownload.model.DownloadItem
import io.reactivex.functions.Consumer
import zlc.season.practicalrecyclerview.PracticalRecyclerView
import zlc.season.rxdownload2.RxDownload
import zlc.season.rxdownload2.function.Utils
import kotlin.reflect.KProperty

class DownloadManagerActivity : AppCompatActivity() {
   var mToolbar: Toolbar by bindView(R.id.toolbar)
   var mRecycler: PracticalRecyclerView by bindView(R.id.recycler)

    private var mAdapter: DownloadAdapter? = null
    private var rxDownload: RxDownload? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_manager)
        ButterKnife.bind(this)
        setSupportActionBar(mToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rxDownload = RxDownload.getInstance(this)
        mAdapter = DownloadAdapter()
        mRecycler!!.setLayoutManager(LinearLayoutManager(this))
        mRecycler!!.setAdapterWithLoading(mAdapter)
        loadData()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @OnClick(R.id.start, R.id.pause)
    fun onClick(view: View) {
        val list = mAdapter!!.getData()
        when (view.id) {
            R.id.start -> for (each in list) {
                rxDownload!!.serviceDownload(each.record.getUrl())
                        .subscribe(Consumer<Any> { }, Consumer<Throwable> { throwable -> Utils.log(throwable) })
            }
            R.id.pause -> for (each in list) {
                rxDownload!!.pauseServiceDownload(each.record.getUrl())
                        .subscribe({ }) { throwable -> Utils.log(throwable) }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val list = mAdapter!!.getData()
        for (each in list) {
            Utils.dispose(each.disposable)
        }
    }

    private fun loadData() {
        RxDownload.getInstance(this).totalDownloadRecords
                .map<List<DownloadItem>> { downloadRecords ->
                    val result = ArrayList<DownloadItem>()
                    for (each in downloadRecords) {
                        val bean = DownloadItem()
                        bean.record = each
                        result.add(bean)
                    }
                    result
                }
                .subscribe { downloadBeen -> mAdapter!!.addAll(downloadBeen) }
    }
}

private operator fun  Any.setValue(downloadManagerActivity: DownloadManagerActivity, property: KProperty<*>, practicalRecyclerView: PracticalRecyclerView) {}
private operator fun  Any.setValue(downloadManagerActivity: DownloadManagerActivity, property: KProperty<*>, toolbar: Toolbar) {}
