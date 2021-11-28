package com.zipper.core.model.binding

import androidx.databinding.ObservableField

class ObservableString: ObservableField<String> {

    constructor(): super("")

    constructor(value: String): super(value)

    override fun get(): String {
        return super.get() ?: ""
    }
}