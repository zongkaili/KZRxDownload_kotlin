package com.kelly.kzrxdownload.model

import android.databinding.BaseObservable
import android.databinding.Bindable

/**
 * Target: 界面元素的双向绑定
 */

open class RxDownloadModel : BaseObservable() {
    @get:Bindable
    var percent = "0.00%"
        set(percent) {
            field = percent
//            notifyPropertyChanged(com.kelly.kzrxdownload.BR.percent)
        }
    @get:Bindable
    var size = "0.0KB/0.0KB"
        set(size) {
            field = size
//            notifyPropertyChanged(com.kelly.kzrxdownload.BR.size)
        }
    @get:Bindable
    var status = ""
        set(status) {
            field = status
//            notifyPropertyChanged(com.kelly.kzrxdownload.BR.status)
        }
    @get:Bindable
    var action = "开始"
        set(action) {
            field = action
//            notifyPropertyChanged(com.kelly.kzrxdownload.BR.action)
        }
}
