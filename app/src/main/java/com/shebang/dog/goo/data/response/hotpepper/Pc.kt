package com.shebang.dog.goo.data.response.hotpepper

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Pc {

    @SerializedName("l")
    @Expose
    var l: String? = null
    @SerializedName("m")
    @Expose
    var m: String? = null
    @SerializedName("s")
    @Expose
    var s: String? = null

}
