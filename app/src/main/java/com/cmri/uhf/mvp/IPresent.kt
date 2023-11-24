package com.cmri.uhf.mvp

interface IPresent<V> {
    fun attachV(view: V)
    fun detachV()
    fun hasV(): Boolean
}