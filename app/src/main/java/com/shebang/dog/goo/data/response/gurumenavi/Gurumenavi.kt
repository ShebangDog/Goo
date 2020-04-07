package com.shebang.dog.goo.data.response.gurumenavi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Gurumenavi {

    @SerializedName("@attributes")
    @Expose
    var attributes: Attributes? = null
    @SerializedName("total_hit_count")
    @Expose
    var totalHitCount: Int? = null
    @SerializedName("hit_per_page")
    @Expose
    var hitPerPage: Int? = null
    @SerializedName("page_offset")
    @Expose
    var pageOffset: Int? = null
    @SerializedName("rest")
    @Expose
    var rest: List<Rest>? = null

}
