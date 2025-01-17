package com.nelkinda.training

import java.util.Date

enum class ExpenseType {
    DINNER, BREAKFAST, CAR_RENTAL
}

class Expense {
    lateinit var type: ExpenseType
    var amount: Int = 0
}

class ExpenseReport {

    fun printReport(expenses: List<Expense>) {
        val report = createReport(expenses)
        print(report)
    }

    internal fun createReport(expenses: List<Expense>, date: Date = Date()): String {
        var total = 0
        var mealExpenses = 0
        var result = ""

        result += "Expenses $date\n"

        for (expense in expenses) {
            if (expense.type == ExpenseType.DINNER || expense.type == ExpenseType.BREAKFAST) {
                mealExpenses += expense.amount
            }

            var expenseName = ""
            when (expense.type) {
                ExpenseType.DINNER -> expenseName = "Dinner"
                ExpenseType.BREAKFAST -> expenseName = "Breakfast"
                ExpenseType.CAR_RENTAL -> expenseName = "Car Rental"
            }

            val mealOverExpensesMarker =
                if (expense.type == ExpenseType.DINNER && expense.amount > 5000 || expense.type == ExpenseType.BREAKFAST && expense.amount > 1000) "X" else " "

            result += expenseName + "\t" + expense.amount + "\t" + mealOverExpensesMarker + "\n"

            total += expense.amount
        }

        result += "Meal expenses: $mealExpenses\n"
        result += "Total expenses: $total\n"

        return result
    }
}
