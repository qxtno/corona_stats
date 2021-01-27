package com.github.qxtno.coronastats.helpers

class ContinentsHelper {
    fun translateContinent(continent: String): String {
        var continentTranslated = ""
        when (continent) {
            "North America" -> continentTranslated = "Ameryka Północna"
            "Asia" -> continentTranslated = "Azja"
            "South America" -> continentTranslated = "Ameryka Południowa"
            "Europe" -> continentTranslated = "Europa"
            "Africa" -> continentTranslated = "Afryka"
            "Australia/Oceania" -> continentTranslated = "Austrailia i Oceania"
        }
        return continentTranslated
    }
}