package com.csvconverter

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDate

class Converter : SuperConverter<Converter.InputData, Converter.OutputData> {
    override val csvHelper: CsvHelper
        get() = CsvHelper()

    override fun inputToOutput(input: InputData): OutputData {
        return OutputData(
            id = input.orderNumber,
            orderId = input.orderNumber.toInt(),
            orderDate = LocalDate.of(input.year.toInt(), input.month.toInt(), input.day.toInt()),
            productId = input.productNumber,
            productName = input.productName,
            quantity = stringToBigDecimal(input.count),
            unit = "kg"
        )
    }

    data class InputData(
        @JsonProperty("Id")
        val id: String,
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

    class OutputData(
        @JsonProperty("Id")
        val id: String,

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