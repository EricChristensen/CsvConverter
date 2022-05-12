package com.csvconverter

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.File
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate

class Converter {

    private val csvHelper = CsvHelper()

    fun convertAndOutput(inputFilePath: String, outputFilePath: String, logFile: String = "log.txt") {
        val items: List<InputData> = csvHelper.readCsvFile(inputFilePath)
        val outputItems: List<OutputData> = items.mapIndexedNotNull { index, it ->
            try {
                inputToOutput(it)
            } catch (e: Exception) {
                File(logFile).writeText("Row " + index + " had a problem and didn't get converted. Exception message: " + e.message)
                null
            }
        }
        csvHelper.writeCsvFile(outputItems, outputFilePath)
    }

    fun inputToOutput(input: InputData) : OutputData {
        return OutputData(
            orderId = input.orderNumber.toInt(),
            orderDate = LocalDate.of(input.year.toInt(), input.month.toInt(), input.day.toInt()),
            productId = input.productNumber,
            productName = input.productName,
            quantity = stringToBigDecimal(input.count),
            unit = "kg"
        )
    }

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

    data class InputData(
        @JsonProperty("Order Number")
        val orderNumber: String,
        @JsonProperty("Year")
        val year: String,
        @JsonProperty("Month")
        val month: String,
        @JsonProperty("Day")
        val day: String,
        @JsonProperty("Product Number")
        val productNumber: String,
        @JsonProperty("Product Name")
        val productName: String,
        @JsonProperty("Count")
        val count: String,
        @JsonProperty("Extra Col1")
        val extraCol1: String,
        @JsonProperty("Extra Col2")
        val extraCol2: String,
        @JsonProperty("Empty Column")
        val emptyColumn: String
    )

    data class OutputData(
        @JsonProperty("OrderID")
        val orderId: Int,
        @JsonProperty("OrderDate")
        val orderDate: LocalDate,
        @JsonProperty("ProductId")
        val productId: String,
        @JsonProperty("ProductName")
        val productName: String,
        @JsonProperty("Quantity")
        val quantity: BigDecimal,
        @JsonProperty("Unit")
        val unit: String
    )
}