package com.se3.payme.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Country {
    @SerializedName("numeric")
    @Expose
    var numeric: String? = null
    @SerializedName("alpha2")
    @Expose
    var alpha2: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("emoji")
    @Expose
    var emoji: String? = null
    @SerializedName("currency")
    @Expose
    var currency: String? = null
    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null
    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null
}