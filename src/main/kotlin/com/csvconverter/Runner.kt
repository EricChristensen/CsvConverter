package com.csvconverter

import java.io.File

class Runner {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Input file: " + args[0] + "\nOuput file: " + args[1] + "\nLog file: " + args[2])
            val converter = Converter()
            if (!File(args[0]).exists()) {
                println("Input file needs to exist before program is run. Please specify a valid input file or use the default.")
                return
            }
            converter.convertAndOutput(args[0], args[1], args[2])
        }
    }
}