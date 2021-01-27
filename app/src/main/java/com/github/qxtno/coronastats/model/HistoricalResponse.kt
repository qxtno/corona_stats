package com.github.qxtno.coronastats.model

import com.google.gson.annotations.SerializedName

class HistoricalResponse {
    @SerializedName("country")
    var country: String = ""

    @SerializedName("timeline")
    var timeline: TimeLine = TimeLine()
}

class TimeLine{
    @SerializedName("cases")
    val casesMap: LinkedHashMap<String, Int> = linkedMapOf()

    @SerializedName("deaths")
    val deathsMap: LinkedHashMap<String, Int> = linkedMapOf()

    @SerializedName("recovered")
    val recoveredMap: LinkedHashMap<String, Int> = linkedMapOf()
}