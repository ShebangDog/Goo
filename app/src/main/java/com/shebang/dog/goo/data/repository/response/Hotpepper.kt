package com.shebang.dog.goo.data.repository.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Hotpepper {

    @SerializedName("results")
    @Expose
    var results: Results? = null

}
