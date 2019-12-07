package com.assignment.data.mapper

import com.assignment.data.bean.InfoModel
import com.assignment.extension.G

fun List<InfoModel>.localCopy() {
    val json = G.gson.toJson(this)
}