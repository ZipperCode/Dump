package com.zipper.auto.api.bean

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

data class TitleBean(
    val title: String,
    val checked: ObservableBoolean = ObservableBoolean(false)
)
