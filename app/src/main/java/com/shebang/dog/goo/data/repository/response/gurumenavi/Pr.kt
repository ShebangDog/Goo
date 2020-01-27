package com.shebang.dog.goo.data.repository.response.gurumenavi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Pr {

    @SerializedName("pr_short")
    @Expose
    var prShort: String? = null
    @SerializedName("pr_long")
    @Expose
    var prLong: String? = null

}
