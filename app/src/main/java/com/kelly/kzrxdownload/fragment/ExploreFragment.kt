package com.kelly.kzrxdownload.fragment

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.kelly.kzrxdownload.R
import com.twobbble.event.OpenDrawerEvent
import com.twobbble.view.fragment.BaseFragment
import org.greenrobot.eventbus.EventBus
import kotlin.reflect.KProperty

/**
 * Created by zongkaili on 2017/6/12.
 */
class ExploreFragment : BaseFragment() {
    private var mToolbar: Toolbar by bindView(R.id.toolbar)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_explore, null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        bindEvent()
    }

    private fun bindEvent() {
        mToolbar.setNavigationOnClickListener {
            EventBus.getDefault().post(OpenDrawerEvent())
        }
    }
}

private operator fun  Any.setValue(exploreFragment: ExploreFragment, property: KProperty<*>, toolbar: Toolbar) {}
