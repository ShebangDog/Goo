package com.shebang.dog.goo.data.response.gurumenavi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CouponUrl {

    @SerializedName("pc")
    @Expose
    var pc: String? = null
    @SerializedName("mobile")
    @Expose
    var mobile: String? = null

}
