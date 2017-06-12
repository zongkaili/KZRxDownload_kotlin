package com.kelly.kzrxdownload.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kelly.kzrxdownload.R
import com.twobbble.view.fragment.BaseFragment

/**
 * Created by zongkaili on 2017/6/12.
 */
class ExploreFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_explore, null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}