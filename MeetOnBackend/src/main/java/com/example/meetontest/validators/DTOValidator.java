package com.example.meetontest.validators;

import java.util.List;

public interface DTOValidator {
    List<String> validate(Object obj) throws IllegalAccessException;
    List<String> getFieldsList(Object obj) throws IllegalAccessException;
    boolean checkFieldCompliance(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException;

}
