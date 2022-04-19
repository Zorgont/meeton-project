package com.example.meetontest.validators.impl;

import com.example.meetontest.validators.DTOValidator;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class DTOValidatorImpl implements DTOValidator {

    @Override
    public List<String> validate(Object obj) throws IllegalAccessException {
        List<String> list = new ArrayList<>();
        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if (f.get(obj) == null || ((f.get(obj) instanceof String) && ((String) f.get(obj)).isEmpty())
                    || ((f.get(obj) instanceof Collection) && ((Collection) f.get(obj)).isEmpty()))
                list.add(f.getName());

        }
        return list;
    }

    @Override
    public List<String> getFieldsList(Object obj) throws IllegalAccessException {
        List<String> list = new ArrayList<>();
        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            list.add(f.getName());

        }
        return list;
    }
    @Override
    public boolean checkFieldCompliance(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj).equals(value);
         }

}
