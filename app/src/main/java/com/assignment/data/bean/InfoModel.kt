package com.assignment.data.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class InfoModel(
    @SerializedName("rows")
    val rows: List<Row>? = emptyList(),
    @SerializedName("title")
    val title: String? = "" // About Canada
) : Serializable

data class Row(
    @SerializedName("description")
    val description: String? = "", // Nous parlons tous les langues importants.
    @SerializedName("imageHref")
    val image: String? = "", // http://3.bp.blogspot.com/__mokxbTmuJM/RnWuJ6cE9cI/AAAAAAAAATw/6z3m3w9JDiU/s400/019843_31.jpg
    @SerializedName("title")
    val title: String? = "" // Language
) : Serializable {


}