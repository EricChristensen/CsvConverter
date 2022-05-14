package com.csvconverter

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class TesterConverter {

    private val converter: Converter = Converter()
    private val testFileDirectory = "src/test/kotlin/com/csvconverter/testfiles/"
    private val logFile = testFileDirectory + "log.txt"
    private val outputFile = testFileDirectory + "output.csv"

    @BeforeEach
    fun setUp() {
        File(logFile).createNewFile()
        File(outputFile).createNewFile()
    }

    @AfterEach
    fun tearDown() {
        File(logFile).delete()
        File(outputFile).delete()
    }


    @Test
    fun bigDecimalConversionCountWithCommasIsConvertedToNotHaveCommas() {
        // big decimal does not contain commas even when input string has them
        val inputWithCommas = "5,000,000.00"
        val result = converter.stringToBigDecimal(inputWithCommas)

        assertFalse(result.toString().contains(","))
    }

    @Test
    fun testTheConverter() {
        val expectedId = 1
        val inputYear = "2018"
        val inputMonth = "5"
        val inputDay = "5"
        val expectedDate = LocalDate.of(inputYear.toInt(), inputMonth.toInt(), inputDay.toInt())


        val input = Converter.InputData(
            id = "id",
            orderNumber = expectedId.toString(),
            year = inputYear,
            month = inputMonth,
            day = inputDay,
            productNumber = "P-10001",
            productName = "Arugola",
            count = "500",
            extraCol1 = "a",
            extraCol2 = "b",
            emptyColumn = ""
        )

        val result = converter.inputToOutput(input)
        assertEquals(expectedDate, result.orderDate)
        assertEquals(expectedId, result.orderId)
        assertEquals(input.productName, result.productName)
        assertEquals(BigDecimal("500.00"), result.quantity)
        assertEquals("kg", result.unit)
    }

    @Test
    fun inputFileWorksFine() {
        val inputFile = testFileDirectory + "orders_with_two_good_lines_and_no_bad.csv"

        converter.convertAndOutput(inputFile, outputFile, logFile)

        assertEquals(3, linesInFile(outputFile))
        assertEquals(0, linesInFile(logFile))
    }

    @Test
    fun inputFileWithUnReadableRowsLogsErrors() {
        val inputFile = testFileDirectory + "orders_with_one_good_line_and_one_bad_line.csv"

        converter.convertAndOutput(inputFile, outputFile, logFile)

        assertEquals(2, linesInFile(outputFile))
        assertEquals(1, linesInFile(logFile))
    }

    private fun linesInFile(file: String): Int {
        var numberOfLines = 0
        File(file).useLines { numberOfLines = it.count() }
        return numberOfLines
    }
}