package com.shebang.dog.goo.data.repository.response.gurumenavi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Code {

    @SerializedName("areacode")
    @Expose
    var areacode: String? = null
    @SerializedName("areaname")
    @Expose
    var areaname: String? = null
    @SerializedName("prefcode")
    @Expose
    var prefcode: String? = null
    @SerializedName("prefname")
    @Expose
    var prefname: String? = null
    @SerializedName("areacode_s")
    @Expose
    var areacodeS: String? = null
    @SerializedName("areaname_s")
    @Expose
    var areanameS: String? = null
    @SerializedName("category_code_l")
    @Expose
    var categoryCodeL: List<String>? = null
    @SerializedName("category_name_l")
    @Expose
    var categoryNameL: List<String>? = null
    @SerializedName("category_code_s")
    @Expose
    var categoryCodeS: List<String>? = null
    @SerializedName("category_name_s")
    @Expose
    var categoryNameS: List<String>? = null

}
