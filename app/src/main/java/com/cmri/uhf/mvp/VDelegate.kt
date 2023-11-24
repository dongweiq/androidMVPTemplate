package com.cmri.uhf.mvp

import android.view.View


interface VDelegate {
    fun resume()
    fun pause()
    fun destory()
    fun visible(flag: Boolean, view: View?)
    fun gone(flag: Boolean, view: View?)
    fun inVisible(view: View?)
    fun toastShort(msg: String?)
    fun toastLong(msg: String?)
}