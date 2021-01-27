package com.github.qxtno.coronastats.helpers

class StringHelper {
    fun stringBuilderThreeParams(first: String, second: String, third: String): String =
        "$second\n\uD83D\uDD3A$third\n$first"

    fun stringBuilderThreeParamsHorizontal(first: String, second: String, third: String): String =
        "$second \uD83D\uDD3A$third\n$first"

    fun stringBuilderTwoParams(first: String, second: String): String =
        "$second\n$first"
}