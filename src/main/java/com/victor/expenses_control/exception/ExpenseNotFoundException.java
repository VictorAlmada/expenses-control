package com.victor.expenses_control.exception;


public class ExpenseNotFoundException extends RuntimeException{

    public ExpenseNotFoundException(Long id) {
        super("Despesa com ID " + id + " não encontrada.");
    }

    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
