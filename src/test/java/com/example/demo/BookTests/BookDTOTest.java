package com.example.demo.BookTests;

import com.example.demo.dto.BookDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookDTOTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void whenTitleIsValid_thenNoViolations() {
        BookDTO bookDTO = new BookDTO(null, "Good Book", "John Doe", 10, false);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenTitleIsTooShort_thenViolations() {
        BookDTO bookDTO = new BookDTO(null, "Go", "John Doe", 10, false);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenTitleDoesNotStartWithCapitalLetter_thenViolations() {
        BookDTO bookDTO = new BookDTO(null, "bad book", "John Doe", 10, false);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenTitleIsEmpty_thenViolations() {
        BookDTO bookDTO = new BookDTO(null, "", "John Doe", 10, false);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenAuthorIsValid_thenNoViolations() {
        BookDTO bookDTO = new BookDTO(null, "Good Book", "Paulo Coelho", 10, false);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenAuthorHasNoSpace_thenViolations() {
        BookDTO bookDTO = new BookDTO(null, "Good Book", "JohnSmith", 10, false);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenAuthorIsLowerCase_thenViolations() {
        BookDTO bookDTO = new BookDTO(null, "Good Book", "john smith", 10, false);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenAuthorHasOnlyOneWord_thenViolations() {
        BookDTO bookDTO = new BookDTO(null, "Good Book", "John", 10, false);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenAuthorHasExtraWords_thenViolations() {
        BookDTO bookDTO = new BookDTO(null, "Good Book", "John Smith Junior", 10, false);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenAuthorIsEmpty_thenViolations() {
        BookDTO bookDTO = new BookDTO(null, "Good Book", "", 10, false);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenAuthorIsNull_thenViolations() {
        BookDTO bookDTO = new BookDTO(null, "Good Book", null, 10, false);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertFalse(violations.isEmpty());
    }
}
