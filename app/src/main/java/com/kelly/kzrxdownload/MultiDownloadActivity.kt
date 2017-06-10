package com.kelly.kzrxdownload

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.bindView
import com.kelly.kzrxdownload.adapter.AppInfoAdapter
import com.kelly.kzrxdownload.model.AppInfoBean
import zlc.season.practicalrecyclerview.PracticalRecyclerView
import java.util.ArrayList
import zlc.season.rxdownload2.function.Utils.dispose
import kotlin.reflect.KProperty


class MultiDownloadActivity : AppCompatActivity() {
    var mToolbar: Toolbar by bindView(R.id.toolbar)
    var mRecycler: PracticalRecyclerView by bindView(R.id.recycler)
    var mAdapter: AppInfoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_download)
        ButterKnife.bind(this)
        setSupportActionBar(mToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAdapter = AppInfoAdapter()
        mRecycler?.setLayoutManager(LinearLayoutManager(this))
        mRecycler?.setAdapterWithLoading(mAdapter)
        loadData()
    }

    fun loadData(){
        val res = resources
        val names = res.getStringArray(R.array.name)
        val images = res.getStringArray(R.array.image)
        val infos = res.getStringArray(R.array.info)
        val urls = res.getStringArray(R.array.url)
        val list = ArrayList<AppInfoBean>()
        for (i in images.indices) {
            val temp = AppInfoBean(names[i], images[i], infos[i], urls[i])
            list.add(temp)
        }
        mAdapter?.clear()
        mAdapter?.addAll(list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_download_manage,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home-> finish()
            R.id.action_download_manage -> startActivity(Intent(this,MultiDownloadActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        val list = mAdapter?.getData()
        list?.forEach { each -> dispose(each.disposable) }
    }
}

private operator fun  Any.setValue(multiDownloadActivity: MultiDownloadActivity, property: KProperty<*>, toolbar: Toolbar) {}
private operator fun  Any.setValue(multiDownloadActivity: MultiDownloadActivity, property: KProperty<*>, practicalRecyclerView: PracticalRecyclerView) {}
