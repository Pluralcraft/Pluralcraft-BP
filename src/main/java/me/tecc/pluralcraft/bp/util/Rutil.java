/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.util;

import org.jetbrains.annotations.Contract;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public class Rutil {
    @Contract("null, _, _, -> fail; _, null, _ -> fail")
    public static <T> T call(Object target, String method, Object... params) {
        if (target == null) throw new IllegalArgumentException("'target' may not be null!");
        return call(target.getClass(), target, method, params);
    }

    /**
     * Calls a method target an object.
     * @param clz The class
     * @param method The method to call
     * @param target The object to call
     * @param params The parameters to pass to the method
     * @param <T> The return type
     * @return The result of the method.
     * @see Class#getMethod
     * @see java.lang.reflect.Method#invoke(Object, Object...)
     */
    @SuppressWarnings("unchecked")
    public static <T> T call(Class<?> clz, Object target, String method, Object... params) {
        if (clz == null) throw new IllegalArgumentException("'clz' may not be null!");
        try {
            return (T) clz.getMethod(method).invoke(target, params);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void set(Object target, String fieldName, T value) {
        if (target == null) throw new IllegalArgumentException("'target' may not be null!");
        set(target.getClass(), target, fieldName, value);
    }
    public static <T> void set(Class<?> clz, Object target, String fieldName, T value) {
        try {
            Field field = clz.getField(fieldName);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> T get(Object target, String fieldName) {
        if (target == null) throw new IllegalArgumentException("'target' may not be null!");
        return get(target.getClass(), target, fieldName);
    }
    public static <T> T get(Class<?> clz, Object target, String fieldName) {
        try {
            Field field = clz.getField(fieldName);
            return (T) field.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
