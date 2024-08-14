package com.example.demo.MemberTests;

import com.example.demo.dto.MemberDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemberDTOTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void whenNameIsValid_thenNoViolations() {
        MemberDTO memberDTO = new MemberDTO(null, "John Doe", LocalDate.now(), false);
        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(memberDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenNameIsBlank_thenViolations() {
        MemberDTO memberDTO = new MemberDTO(null, "", LocalDate.now(), false);
        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(memberDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenNameIsNull_thenViolations() {
        MemberDTO memberDTO = new MemberDTO(null, null, LocalDate.now(), false);
        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(memberDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenMembershipDateIsNull_thenViolations() {
        MemberDTO memberDTO = new MemberDTO(null, "John Doe", null, false);
        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(memberDTO);
        assertFalse(violations.isEmpty());
    }
}
