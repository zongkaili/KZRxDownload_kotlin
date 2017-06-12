package com.kelly.kzrxdownload.fragment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.bindView
import com.kelly.kzrxdownload.R
import com.kelly.kzrxdownload.adapter.AppInfoAdapter
import com.kelly.kzrxdownload.model.AppInfoBean
import com.twobbble.view.fragment.BaseFragment
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
        var rootView = LayoutInflater.from(activity).inflate(R.layout.fragment_home, null)
        mRecycler = rootView.findViewById(R.id.recycler) as PracticalRecyclerView
        mAdapter = AppInfoAdapter()
        mRecycler?.setLayoutManager(LinearLayoutManager(activity))
        mRecycler?.setAdapterWithLoading(mAdapter)
        loadData()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_home, null)
    }

    override fun onStart() {
        super.onStart()
        bindEvent()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    inner class Presenter {
        fun onClick(view: View) {
            when (view.id) {
                R.id.fab -> Toast.makeText(activity, "Why did you click me?", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindEvent() {
        mFab?.setOnClickListener { v: View? -> Toast.makeText(activity,"Why did you click me?", Toast.LENGTH_SHORT).show() }
        mToolbar.setNavigationOnClickListener {
            Toast.makeText(activity,"Why did you click me?", Toast.LENGTH_SHORT).show()
//            EventBus.getDefault().post(OpenDrawerEvent())
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

private operator fun  Any.setValue(homeFragment: HomeFragment, property: KProperty<*>, toolbar: Toolbar) {}
private operator fun  Any.setValue(homeFragment: HomeFragment, property: KProperty<*>, practicalRecyclerView: PracticalRecyclerView) {}
private operator fun  Any.setValue(homeFragment: HomeFragment, property: KProperty<*>, floatingActionButton: FloatingActionButton) {}
