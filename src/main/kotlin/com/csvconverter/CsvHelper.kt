package com.csvconverter

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.FileReader
import java.io.FileWriter

class CsvHelper {
    val csvMapper = CsvMapper().apply {
        registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
        registerModule(JavaTimeModule())
    }

    inline fun <reified T> readCsvFile(fileName: String): List<T> {
        FileReader(fileName).use { reader ->
            return csvMapper
                .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .readerFor(T::class.java)
                .with(CsvSchema.emptySchema().withHeader())
                .readValues<T>(reader)
                .readAll()
                .toList()
        }
    }

    inline fun <reified T> writeCsvFile(data: Collection<T>, fileName: String) {
        FileWriter(fileName).use { writer ->
            csvMapper.writer(csvMapper.schemaFor(T::class.java).withHeader())
                .writeValues(writer)
                .writeAll(data)
                .close()
        }
    }
}