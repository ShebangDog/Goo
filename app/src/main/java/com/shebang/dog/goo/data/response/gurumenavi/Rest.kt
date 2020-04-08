package com.shebang.dog.goo.data.response.gurumenavi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rest {

    @SerializedName("@attributes")
    @Expose
    var attributes: Attributes_? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("update_date")
    @Expose
    var updateDate: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("name_kana")
    @Expose
    var nameKana: String? = null
    @SerializedName("latitude")
    @Expose
    var latitude: String? = null
    @SerializedName("longitude")
    @Expose
    var longitude: String? = null
    @SerializedName("category")
    @Expose
    var category: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("url_mobile")
    @Expose
    var urlMobile: String? = null
    @SerializedName("coupon_url")
    @Expose
    var couponUrl: CouponUrl? = null
    @SerializedName("image_url")
    @Expose
    var imageUrl: ImageUrl? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("tel")
    @Expose
    var tel: String? = null
    @SerializedName("tel_sub")
    @Expose
    var telSub: String? = null
    @SerializedName("fax")
    @Expose
    var fax: String? = null
    @SerializedName("opentime")
    @Expose
    var opentime: String? = null
    @SerializedName("holiday")
    @Expose
    var holiday: String? = null
    @SerializedName("access")
    @Expose
    var access: Access? = null
    @SerializedName("parking_lots")
    @Expose
    var parkingLots: String? = null
    @SerializedName("pr")
    @Expose
    var pr: Pr? = null
    @SerializedName("code")
    @Expose
    var code: Code? = null
    @SerializedName("budget")
    @Expose
    var budget: String? = null
    @SerializedName("party")
    @Expose
    var party: String? = null
    @SerializedName("lunch")
    @Expose
    var lunch: String? = null
    @SerializedName("credit_card")
    @Expose
    var creditCard: String? = null
    @SerializedName("e_money")
    @Expose
    var eMoney: String? = null
    @SerializedName("flags")
    @Expose
    var flags: Flags? = null

}
