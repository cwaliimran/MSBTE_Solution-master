package com.msbte.modelanswerpaper.interfaces

interface OnItemClick {
    fun onClick(position: Int, type: String? = "", data: Any? = null) {}
}