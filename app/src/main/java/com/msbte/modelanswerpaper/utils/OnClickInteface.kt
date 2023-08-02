package com.msbte.modelanswerpaper.utils

import com.msbte.modelanswerpaper.models.CommonItemModel

interface OnClickInteface {
    fun onCLickItem(exerciseId: Int, title: CommonItemModel)
}