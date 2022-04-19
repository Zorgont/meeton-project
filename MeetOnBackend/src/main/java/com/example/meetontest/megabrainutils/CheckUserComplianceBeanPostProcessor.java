package com.example.meetontest.megabrainutils;

import com.example.meetontest.dto.DTO;
import com.example.meetontest.dto.MessageResponse;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.services.impl.UserDetailsImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class CheckUserComplianceBeanPostProcessor implements BeanPostProcessor {
    private Map<String, Class> controllers = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(RestController.class)) {
            if(Arrays.stream(beanClass.getMethods()).anyMatch(method -> method.isAnnotationPresent(CheckUserCompliance.class)))
                controllers.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = controllers.get(beanName);
        if (beanClass != null) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(bean.getClass());
            enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
                if (method.isAnnotationPresent(CheckUserCompliance.class)) {
                    for(int i = 0; i < method.getParameters().length; i++) {
                        Parameter param = method.getParameters()[i];
                        if (param.isAnnotationPresent(DTO.class)) {
                            Field userIdField = args[i].getClass().getDeclaredField(param.getAnnotation(DTO.class).name());
                            userIdField.setAccessible(true);
                            if (!((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().equals((Long) userIdField.get(args[i]))) {
                                if(method.getReturnType().equals(Boolean.class))
                                    return false;
                                if(method.getReturnType().equals(ResponseEntity.class))
                                    return ResponseEntity.badRequest().body("User id doesn't match to current user!");
                                throw new ValidatorException("User id doesn't match to current user!");
                            }
                        }
                    }
                }

                return method.invoke(bean, args);
            });

            try {
                Class[] parameterTypes = beanClass.getConstructors()[0].getParameterTypes();
                Object[] args = Arrays.stream(beanClass.getDeclaredFields())
                        .filter(field ->  Modifier.isPrivate(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
                        .map((field) -> {
                            try {
                                field.setAccessible(true);
                                return field.get(bean);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            throw new RuntimeException("Something went wrong!");
                        }).toArray();
                return enhancer.create(parameterTypes, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bean;
    }
}
