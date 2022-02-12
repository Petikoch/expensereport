package com.nelkinda.training

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class ExpenseReportCharacterizationTest {

    @Test
    fun `test createReport with header and 0 sums for empty expenses list`() {
        val testee = ExpenseReport()

        val report = testee.createReport(emptyList(), GregorianCalendar(2022, 0, 1).time)

        assertThat(report).isEqualTo(
            """Expenses Sat Jan 01 00:00:00 CET 2022
Meal expenses: 0
Total expenses: 0
"""
        )
    }

    @Test
    fun `test createReport creates report using all types of expenses with corner cases for meal limits`() {
        val testee = ExpenseReport()
        val expenses = listOf(
            Expense().apply { type = ExpenseType.DINNER; amount = 5000 },
            Expense().apply { type = ExpenseType.DINNER; amount = 5001 },

            Expense().apply { type = ExpenseType.BREAKFAST; amount = 1000 },
            Expense().apply { type = ExpenseType.BREAKFAST; amount = 1001 },

            Expense().apply { type = ExpenseType.CAR_RENTAL; amount = 1 },
            Expense().apply { type = ExpenseType.CAR_RENTAL; amount = 10000 },
        )

        val report = testee.createReport(expenses, GregorianCalendar(2022, 0, 1).time)

        assertThat(report).isEqualTo(
            """Expenses Sat Jan 01 00:00:00 CET 2022
Dinner	5000	 
Dinner	5001	X
Breakfast	1000	 
Breakfast	1001	X
Car Rental	1	 
Car Rental	10000	 
Meal expenses: 12002
Total expenses: 22003
"""
        )
    }

    @Test
    fun `test createReport creates report with number overflow for Int max value in one of the expenses`() {
        val testee = ExpenseReport()
        val expenses = listOf(
            Expense().apply { type = ExpenseType.BREAKFAST; amount = 1 },
            Expense().apply { type = ExpenseType.BREAKFAST; amount = Int.MAX_VALUE },

            Expense().apply { type = ExpenseType.CAR_RENTAL; amount = 1000 }
        )

        val report = testee.createReport(expenses, GregorianCalendar(2022, 0, 1).time)

        assertThat(report).isEqualTo(
            """Expenses Sat Jan 01 00:00:00 CET 2022
Breakfast	1	 
Breakfast	2147483647	X
Car Rental	1000	 
Meal expenses: -2147483648
Total expenses: -2147482648
"""
        )
    }

    @Test
    fun `test createReport creates non blank report when using system clock and does not throw`() {
        val testee = ExpenseReport()

        val report = testee.createReport(emptyList())

        assertThat(report).isNotBlank
    }

}
