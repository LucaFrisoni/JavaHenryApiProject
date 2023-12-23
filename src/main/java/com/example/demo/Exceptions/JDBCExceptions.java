package com.example.demo.Exceptions;

import org.springframework.stereotype.Component;

@Component
public class JDBCExceptions extends RuntimeException {

    public JDBCExceptions() {
    }

    public JDBCExceptions(String message) {
        super(message);
    }

    public JDBCExceptions expenseIDNotFound() {
        return new JDBCExceptions("Expense doesn't exist with that ID");
    }

    public JDBCExceptions incomeIDNotFound() {
        return new JDBCExceptions("Income doesn't exist with that ID");
    }

    public JDBCExceptions expenseCategoryFilterNotFound() {
        return new JDBCExceptions("Expenses with such category don`t exist");
    }

    public JDBCExceptions incomeCategoryFilterNotFound() {
        return new JDBCExceptions("Incomes with such category don`t exist");
    }

    public JDBCExceptions expenseMountFilterNotFound() {
        return new JDBCExceptions("Expenses with such range of amount don`t exist");
    }

    public JDBCExceptions expenseDayFilterNotFound() {
        return new JDBCExceptions("Expenses in such day don`t exist");
    }

    public JDBCExceptions expenseWeekFilterNotFound() {
        return new JDBCExceptions("Expenses in such week don`t exist");
    }

    public JDBCExceptions expenseMonthFilterNotFound() {
        return new JDBCExceptions("Expenses in such month don`t exist");
    }

    public JDBCExceptions incomeMonthFilterNotFound() {
        return new JDBCExceptions("Incomes in such month don`t exist");

    }

    public JDBCExceptions emptyArrayIncome() {
        return new JDBCExceptions("No income charged");
    }
}
