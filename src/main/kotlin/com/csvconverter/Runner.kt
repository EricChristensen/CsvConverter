package com.csvconverter

class Runner {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            args.forEach { println(it) }
            val converter = Converter()
            converter.convertAndOutput(args[0], args[1], args[2])
        }
    }
}