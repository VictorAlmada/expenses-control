package com.victor.expenses_control.mapper;

import com.victor.expenses_control.dto.ExpenseDTO;
import com.victor.expenses_control.model.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

    ExpenseDTO toDTO(Expense expense);

    Expense toEntity(ExpenseDTO dto);

    List<ExpenseDTO> toDTOList(List<Expense> expenses);

    List<Expense> toEntityList(List<ExpenseDTO> expenseDTOs);

}
