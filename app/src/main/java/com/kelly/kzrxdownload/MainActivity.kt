package com.kelly.kzrxdownload

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import butterknife.bindView
import com.kelly.kzrxdownload.databinding.ActivityMainBinding

import zlc.season.rxdownload2.RxDownload
import zlc.season.rxdownload2.function.Utils
import kotlin.reflect.KProperty

class MainActivity : AppCompatActivity() {
    var mToorBar: Toolbar by bindView(R.id.toolbar)
    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.presenter = Presenter()
        setSupportActionBar(binding.toolbar)

        Utils.setDebug(true)
        RxDownload.getInstance(this)
                .maxDownloadNumber(2)
                .maxThread(3)
        bindEvent()
    }

    private fun bindEvent(){
        mToorBar.setNavigationOnClickListener {
            Toast.makeText(this@MainActivity, "Why did you click me?", Toast.LENGTH_SHORT).show()
//            EventBus.getDefault().post(OpenDrawerEvent())
        }
    }


    inner class Presenter {
        fun onClick(view: View) {
            when (view.id) {
                R.id.fab -> Toast.makeText(this@MainActivity, "Why did you click me?", Toast.LENGTH_SHORT).show()
                R.id.single_task_download -> startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                R.id.multi_task_download -> startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            }
        }
    }

}

private operator fun  Any.setValue(mainActivity: MainActivity, property: KProperty<*>, toolbar: Toolbar) {}
