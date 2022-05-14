package com.csvconverter

import java.io.File
import java.math.BigDecimal
import java.text.DecimalFormat

interface SuperConverter<I, O> {
    val csvHelper: CsvHelper
    fun convertAndOutput(inputFilePath: String, outputFilePath: String, logFile: String = "log.txt") {
        val items: List<Class<I>> = csvHelper.readCsvFile(inputFilePath)
        val items2: List<I> = items.map { it.cast(Any()) }
        val outputItems: List<O> = items2.mapIndexedNotNull { index, it ->
            try {
                inputToOutput(it)
            } catch (e: Exception) {
                File(logFile).writeText("Row " + index + " had a problem and didn't get converted. Exception message: " + e.message)
                null
            }
        }
        val outputItems2 = outputItems.map {
            it!!::class.java
        }
        csvHelper.writeCsvFile(outputItems2, outputFilePath)
    }

    fun inputToOutput(input: I) : O


    /**
     * We format the big decimal to remove commas, that way we don't
     * have extra commas in our csv or need "quotes" around the value
     * like the example input file does it
     */
    fun stringToBigDecimal(count: String): BigDecimal {
        val decimalFormat = DecimalFormat("###,###.###")
        decimalFormat.isParseBigDecimal = true
        return (decimalFormat.parse(count) as BigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP)
    }
}
