package com.kelly.kzrxdownload.model

import io.reactivex.disposables.Disposable
import zlc.season.practicalrecyclerview.ItemType

class AppInfoBean(var name: String, var avatar: String, var info: String, var img: String, var downloadUrl: String) : ItemType {
    var saveName: String
    var disposable: Disposable? = null

    init {
        this.saveName = getSaveNameByUrl(downloadUrl)
    }

    override fun itemType(): Int {
        return 0
    }


    /**
     * 截取Url最后一段作为文件保存名称

     * @param url url
     * *
     * @return saveName
     */
    private fun getSaveNameByUrl(url: String): String {
        return url.substring(url.lastIndexOf('/') + 1)
    }
}
