package com.victor.expenses_control.controller;

import com.victor.expenses_control.dto.ExpenseDTO;
import com.victor.expenses_control.service.ExpenseServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseServiceImpl expenseService;

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> findAll() {
        List<ExpenseDTO> lista = expenseService.findAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> findById(@PathVariable Long id) {
        ExpenseDTO dto = expenseService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ExpenseDTO> create(@RequestBody @Valid ExpenseDTO dto) {
        ExpenseDTO created = expenseService.create(dto);
        URI uri = URI.create("/api/expenses/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> update(@PathVariable Long id, @RequestBody @Valid ExpenseDTO dto) {
        ExpenseDTO updated = expenseService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Long id) {
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
