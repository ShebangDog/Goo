package com.shebang.dog.goo.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ServiceArea {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null

}
