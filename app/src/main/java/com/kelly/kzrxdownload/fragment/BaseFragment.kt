package com.twobbble.view.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent

/**
 * Created by zongkaili on 2017/6/12.
 */
abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun onBackPressed() {}

    open fun onKeyDown(keyCode: Int, event: KeyEvent?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}