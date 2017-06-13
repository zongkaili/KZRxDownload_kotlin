package com.kelly.kzrxdownload

import android.app.Application
import android.os.Environment
import com.facebook.drawee.backends.pipeline.Fresco
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.twobbble.tools.delegates.NotNullSingleValueVar
import zlc.season.rxdownload2.function.Constant

/**
 * Created by zongkaili on 2017/6/10.
 */
class App : Application() {
    companion object {
        var instance: App by NotNullSingleValueVar.DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        instance = this
        Thread {
            Fresco.initialize(this)
        }.start()
        initBugly()
    }

    fun initBugly() {
        Beta.initDelay = 3000
        Beta.largeIconId = R.mipmap.ic_launcher
        Beta.canShowUpgradeActs.add(HomeActivity::class.java)
        Beta.smallIconId = R.drawable.ic_update_black_24dp
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        Bugly.init(applicationContext, Constants.BUGLY_ID, true)
    }
}