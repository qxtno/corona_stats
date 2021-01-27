package com.github.qxtno.coronastats.model

import com.google.gson.annotations.SerializedName

class SummaryResponse {
    @SerializedName("countryInfo")
    var countryInfo: CountryInfo = CountryInfo()

    @SerializedName("country")
    var country: String = ""

    @SerializedName("updated")
    var updated: Long = 0.toLong()

    @SerializedName("cases")
    var cases: Int = 0

    @SerializedName("todayCases")
    var todayCases: Int = 0

    @SerializedName("deaths")
    var deaths: Int = 0

    @SerializedName("todayDeaths")
    var todayDeaths: Int = 0

    @SerializedName("recovered")
    var recovered: Int = 0

    @SerializedName("todayRecovered")
    var todayRecovered: Int = 0

    @SerializedName("active")
    var active: Int = 0

    @SerializedName("critical")
    var critical: Int = 0

}

class CountryInfo {
    @SerializedName("iso2")
    var iso2: String? = null

    @SerializedName("flag")
    val flag: String = ""
}
