package com.kelly.kzrxdownload

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.*
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.bindView
import com.facebook.drawee.view.SimpleDraweeView
import com.kelly.kzrxdownload.fragment.ExploreFragment
import com.kelly.kzrxdownload.fragment.HomeFragment
import com.twobbble.event.OpenDrawerEvent
import com.twobbble.view.activity.BaseActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import zlc.season.rxdownload2.RxDownload
import zlc.season.rxdownload2.function.Utils
import kotlin.reflect.KProperty


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var mHomeFragment: HomeFragment? = null
    private var mExploreFragment: ExploreFragment? = null

    private var mDrawerLayout: DrawerLayout by bindView(R.id.mDrawerLayout)
    private var mNavigation: NavigationView by bindView(R.id.mNavigation)
//    private var mHeadimg: SimpleDraweeView by bindView(R.id.mHeadimg)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ButterKnife.bind(this)
        init()
        EventBus.getDefault().register(this)
        Utils.setDebug(true)
        RxDownload.getInstance(this)
                .maxDownloadNumber(2)
                .maxThread(3)
        if (savedInstanceState == null) initFragment()
    }

    override fun onStart() {
        super.onStart()
        bindEvent()
    }

    private fun init() {
        var toggle = ActionBarDrawerToggle(this,mDrawerLayout,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        var params = mNavigation.layoutParams
        params.width = screenWidth - screenWidth / 4
        mNavigation.layoutParams = params

    }

    private fun initFragment() {
        mHomeFragment = HomeFragment()
        mExploreFragment = ExploreFragment()
        addFragment(mHomeFragment)
        mNavigation.setCheckedItem(R.id.mHomeMenu)
    }

    fun addFragment(fragment: Fragment?) {
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_enter,
                R.anim.fragment_exit, R.anim.fragment_enter, R.anim.fragment_exit)
                .hide(mExploreFragment)
                .hide(mHomeFragment)
                .add(R.id.mContentLayout, fragment)
                .show(fragment)
                .commit()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        mHomeFragment?.onKeyDown(keyCode, event)
        mExploreFragment?.onKeyDown(keyCode, event)
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        if (mHomeFragment?.isVisible!!) {
            mHomeFragment?.onBackPressed()
            return
        }

        if (mExploreFragment?.isVisible!!) {
            mExploreFragment?.onBackPressed()
            return
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun bindEvent(){
        mNavigation.setNavigationItemSelectedListener(this)
        mDrawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {}
            override fun onDrawerClosed(drawerView: View?) {}
            override fun onDrawerOpened(drawerView: View?) {
            }
        })
    }

    @Subscribe fun DrawerOpen(drawerEvent: OpenDrawerEvent?) {
        mDrawerLayout.openDrawer(GravityCompat.START)
    }

    inner class Presenter {
        fun onClick(view: View) {
            when (view.id) {
                R.id.fab -> Toast.makeText(this@HomeActivity, "Why did you click me?", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mDrawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.mHomeMenu -> if (!mHomeFragment!!.isAdded) {
                addFragment(mHomeFragment)
            } else replaceFragment(mHomeFragment)
            R.id.mExploreMenu -> if(!mExploreFragment!!.isAdded) {
                addFragment(mExploreFragment)
            } else replaceFragment(mExploreFragment)
            R.id.mSettingsMenu -> {
                Toast.makeText(this@HomeActivity, "no setting! ?", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun replaceFragment(fragment: Fragment?, addBackStack: Boolean = false) {
        if (fragment != null && !fragment.isVisible) {
            val transaction = fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_enter, R.anim.fragment_exit)
                    .hide(mExploreFragment)
                    .hide(mHomeFragment)
                    .show(fragment)
            if (addBackStack) transaction.addToBackStack(fragment.javaClass.simpleName).commit() else transaction.commit()
        }
    }
}

private operator fun  Any.setValue(homeActivity: HomeActivity, property: KProperty<*>, simpleDraweeView: SimpleDraweeView) {}
private operator fun  Any.setValue(homeActivity: HomeActivity, property: KProperty<*>, navigationView: NavigationView) {}
private operator fun  Any.setValue(homeActivity: HomeActivity, property: KProperty<*>, drawerLayout: DrawerLayout) {}
