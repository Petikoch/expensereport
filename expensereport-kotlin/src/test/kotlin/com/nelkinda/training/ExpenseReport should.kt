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

    @Test
    fun `print report using all types of expenses with corner cases for meal limits`() {
        val testee = ExpenseReport()
        val expenses = listOf(
            Expense().apply { type = ExpenseType.DINNER; amount = 5000 },
            Expense().apply { type = ExpenseType.DINNER; amount = 5001 },

            Expense().apply { type = ExpenseType.BREAKFAST; amount = 1000 },
            Expense().apply { type = ExpenseType.BREAKFAST; amount = 1001 },

            Expense().apply { type = ExpenseType.CAR_RENTAL; amount = 1 },
            Expense().apply { type = ExpenseType.CAR_RENTAL; amount = 10000 },
        )

        testee.printReport(expenses, GregorianCalendar(2022, 0, 1).time)

        val capturedReport = interceptedStdout.toString()
        assertThat(capturedReport).isEqualTo("""Expenses Sat Jan 01 00:00:00 CET 2022
Dinner	5000	 
Dinner	5001	X
Breakfast	1000	 
Breakfast	1001	X
Car Rental	1	 
Car Rental	10000	 
Meal expenses: 12002
Total expenses: 22003
""")
    }

    @Test
    fun `print report with number overflow for Int max value`() {
        val testee = ExpenseReport()
        val expenses = listOf(
            Expense().apply { type = ExpenseType.BREAKFAST; amount = 1 },
            Expense().apply { type = ExpenseType.BREAKFAST; amount = Int.MAX_VALUE },

            Expense().apply { type = ExpenseType.CAR_RENTAL; amount = 1000 }
        )

        testee.printReport(expenses, GregorianCalendar(2022, 0, 1).time)

        val capturedReport = interceptedStdout.toString()
        assertThat(capturedReport).isEqualTo("""Expenses Sat Jan 01 00:00:00 CET 2022
Breakfast	1	 
Breakfast	2147483647	X
Car Rental	1000	 
Meal expenses: -2147483648
Total expenses: -2147482648
""")
    }

    @Test
    fun `produce non blank report when using system clock`(){
        val testee = ExpenseReport()

        testee.printReport(emptyList())


        val capturedReport = interceptedStdout.toString()
        assertThat(capturedReport).isNotBlank()
    }

}
