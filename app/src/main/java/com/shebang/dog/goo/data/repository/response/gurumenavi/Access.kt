package com.shebang.dog.goo.data.repository.response.gurumenavi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Access {

    @SerializedName("line")
    @Expose
    var line: String? = null
    @SerializedName("station")
    @Expose
    var station: String? = null
    @SerializedName("station_exit")
    @Expose
    var stationExit: String? = null
    @SerializedName("walk")
    @Expose
    var walk: String? = null
    @SerializedName("note")
    @Expose
    var note: String? = null

}
