package com.shebang.dog.goo.data.response.hotpepper

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Genre {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("catch")
    @Expose
    var catch: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null

}
