package com.kelly.kzrxdownload.fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import butterknife.bindView
import com.kelly.kzrxdownload.DownloadManagerActivity
import com.kelly.kzrxdownload.R
import com.kelly.kzrxdownload.adapter.AppInfoAdapter
import com.kelly.kzrxdownload.model.AppInfoBean
import com.twobbble.event.OpenDrawerEvent
import com.twobbble.view.fragment.BaseFragment
import org.greenrobot.eventbus.EventBus
import zlc.season.practicalrecyclerview.PracticalRecyclerView
import zlc.season.rxdownload2.function.Utils
import java.util.ArrayList
import kotlin.reflect.KProperty

/**
 * Created by zongkaili on 2017/6/12.
 */
class HomeFragment : BaseFragment() {
    var mToolbar: Toolbar by bindView(R.id.toolbar)
    var mFab: FloatingActionButton by bindView(R.id.fab)
    var mRecycler: PracticalRecyclerView by bindView(R.id.recycler)
    var mAdapter: AppInfoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_home, null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        init()
        initView()
        loadData()
    }

    override fun onStart() {
        super.onStart()
        bindEvent()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity.menuInflater.inflate(R.menu.menu_download_manage,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_download_manage -> startActivity(Intent(activity, DownloadManagerActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        mAdapter = AppInfoAdapter()
        mRecycler?.setLayoutManager(LinearLayoutManager(activity))
        mRecycler?.setAdapterWithLoading(mAdapter)
    }

    private fun initView() {
        mToolbar.inflateMenu(R.menu.menu_download_manage)
    }

    inner class Presenter {
        fun onClick(view: View) {
            when (view.id) {
                R.id.fab -> Toast.makeText(activity, "Why did you click me?", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindEvent() {
        mFab.setOnClickListener { v: View? -> Toast.makeText(activity,"Why did you click me?", Toast.LENGTH_SHORT).show() }
        mToolbar.setNavigationOnClickListener {
            EventBus.getDefault().post(OpenDrawerEvent())
        }
        mToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_download_manage -> startActivity(Intent(activity,DownloadManagerActivity::class.java))
            }
            true
        }
    }

    fun loadData() {
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

    override fun onDestroy() {
        super.onDestroy()
        val list = mAdapter?.getData()
        list?.forEach { each -> Utils.dispose(each.disposable) }
    }
}

private operator fun  Any.setValue(homeFragment: HomeFragment, property: KProperty<*>, relativeLayout: RelativeLayout) {}
private operator fun  Any.setValue(homeFragment: HomeFragment, property: KProperty<*>, toolbar: Toolbar) {}
private operator fun  Any.setValue(homeFragment: HomeFragment, property: KProperty<*>, practicalRecyclerView: PracticalRecyclerView) {}
private operator fun  Any.setValue(homeFragment: HomeFragment, property: KProperty<*>, floatingActionButton: FloatingActionButton) {}
