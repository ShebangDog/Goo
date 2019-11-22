package com.shebang.dog.goo.repository.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Budget {

    @SerializedName("average")
    @Expose
    var average: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null

}
