package com.victor.expenses_control.service;

import com.victor.expenses_control.dto.ExpenseDTO;
import com.victor.expenses_control.mapper.ExpenseMapper;
import com.victor.expenses_control.model.Expense;
import com.victor.expenses_control.repository.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public List<ExpenseDTO> findAll() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenseMapper.toDTOList(expenses);
    }

    @Override
    public ExpenseDTO findById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada com ID: " + id));
        return expenseMapper.toDTO(expense);
    }

    @Override
    public ExpenseDTO create(ExpenseDTO dto) {
        Expense expense = expenseMapper.toEntity(dto);
        Expense saved = expenseRepository.save(expense);
        return expenseMapper.toDTO(saved);
    }

    @Override
    public ExpenseDTO update(Long id, ExpenseDTO dto) {
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada com ID: " + id));

        existingExpense.setDescription(dto.getDescription());
        existingExpense.setAmount(dto.getAmount());
        existingExpense.setDate(dto.getDate());
        existingExpense.setCategory(dto.getCategory());

        Expense updatedExpense = expenseRepository.save(existingExpense);

        return expenseMapper.toDTO(updatedExpense);
    }

    @Override
    public void delete(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new EntityNotFoundException("Despesa não encontrada com ID: " + id);
        }
        expenseRepository.deleteById(id);
    }
}
