package ru.consulting.validated;

import ru.consulting.dto.DepartmentDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class MapValidator implements ConstraintValidator<MapValidConstraint, Map<String, DepartmentDto>> {
    @Override
    public boolean isValid(Map<String, DepartmentDto> stringDepartmentDtoMap, ConstraintValidatorContext constraintValidatorContext) {
        return stringDepartmentDtoMap.containsKey("higherDepartment") && stringDepartmentDtoMap.containsKey("department");
    }
}
