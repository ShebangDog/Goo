package com.shebang.dog.goo.data.response.gurumenavi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ImageUrl {

    @SerializedName("shop_image1")
    @Expose
    var shopImage1: String? = null
    @SerializedName("shop_image2")
    @Expose
    var shopImage2: String? = null
    @SerializedName("qrcode")
    @Expose
    var qrcode: String? = null

}
