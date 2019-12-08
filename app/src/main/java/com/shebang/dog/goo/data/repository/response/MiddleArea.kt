package com.shebang.dog.goo.data.repository.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MiddleArea {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null

}
