package com.shebang.dog.goo.data.response.gurumenavi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Flags {

    @SerializedName("mobile_site")
    @Expose
    var mobileSite: Int? = null
    @SerializedName("mobile_coupon")
    @Expose
    var mobileCoupon: Int? = null
    @SerializedName("pc_coupon")
    @Expose
    var pcCoupon: Int? = null

}
