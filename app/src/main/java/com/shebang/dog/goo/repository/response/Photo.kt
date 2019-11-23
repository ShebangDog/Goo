package com.shebang.dog.goo.repository.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Photo {

    @SerializedName("pc")
    @Expose
    var pc: Pc? = null
    @SerializedName("mobile")
    @Expose
    var mobile: Mobile? = null

}