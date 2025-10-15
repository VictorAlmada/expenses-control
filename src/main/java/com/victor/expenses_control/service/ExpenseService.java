package com.victor.expenses_control.service;

import com.victor.expenses_control.dto.ExpenseDTO;

import java.util.List;

public interface ExpenseService {

    List<ExpenseDTO> findAll();

    ExpenseDTO findById(Long id);

    ExpenseDTO create(ExpenseDTO dto);

    ExpenseDTO update(Long id, ExpenseDTO dto);

    void delete(Long id);
}
