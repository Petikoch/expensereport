package com.nelkinda.training

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*

class `ExpenseReport should` {

    private val originalStdout = System.out
    private val interceptedStdout = ByteArrayOutputStream().also {
        System.setOut(PrintStream(it))
    }

    @AfterEach
    fun `restore stdout`() = System.setOut(originalStdout)

    @Test
    fun `print header and 0 sums for empty expenses list`() {
        val testee = ExpenseReport()

        testee.printReport(emptyList(), GregorianCalendar(2022, 0, 1).time)

        val capturedReport = interceptedStdout.toString()
        assertThat(capturedReport).isEqualTo("""Expenses Sat Jan 01 00:00:00 CET 2022
Meal expenses: 0
Total expenses: 0
""")
    }

}
