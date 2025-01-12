package com.brunosong.javaexam.jsonwriterwitharray;

import com.brunosong.javaexam.jsonwriterwitharray.data.Actor;
import com.brunosong.javaexam.jsonwriterwitharray.data.Movie;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Actor actor1 = new Actor("A", new String[]{"AAA,BBB,CCC"});
        Actor actor2 = new Actor("B", new String[]{"DDD,FFF,AAAGCCC"});

        Movie movie1 = new Movie("AAA", 5.6f, new String[]{"Action", "Adventure"},
                new Actor[]{actor1, actor2});

        System.out.println(objectToJson(movie1,0));
    }

    public static String objectToJson(Object instance,int indentSize) throws IllegalAccessException {
        Field[] declaredFields = instance.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();

        sb.append(indent(indentSize));
        sb.append("{");
        sb.append("\n");

        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            field.setAccessible(true);

            if (field.isSynthetic()) continue;

            sb.append(indent(indentSize + 1));
            sb.append(formatStringValue(field.getName()));
            sb.append(":");

            if (field.getType().isPrimitive()) {
                sb.append(formatPrimitiveValue(field.get(instance), field.getType()));
            } else if (field.getType().equals(String.class)) {
                sb.append(formatStringValue(field.get(instance).toString()));
            } else if (field.getType().isArray()) {
                sb.append(arrayToJson(field.get(instance),indentSize + 1));
            } else {
                sb.append(objectToJson(field.get(instance), indentSize + 1));
            }

            if (i != (declaredFields.length - 1)) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append(indent(indentSize));
        sb.append("}");
        return sb.toString();
    }

    private static String arrayToJson(Object arrayObject, int indentSize) throws IllegalAccessException {
        int length = Array.getLength(arrayObject);
        StringBuilder stringBuilder = new StringBuilder();

        Class<?> componentType = arrayObject.getClass().getComponentType();

        stringBuilder.append("[");
        stringBuilder.append("\n");

        for (int i = 0; i < length; i++) {
            Object element = Array.get(arrayObject,i);

            if (componentType.isPrimitive()) {
                stringBuilder.append(indent(indentSize + 1));
                stringBuilder.append(formatPrimitiveValue(element, componentType));
            } else if (componentType.equals(String.class)) {
                stringBuilder.append(indent(indentSize + 1));
                stringBuilder.append(formatStringValue(element.toString()));
            } else {
                stringBuilder.append(objectToJson(element, indentSize+1));
            }

            if (i != length-1) {
                stringBuilder.append(",");
            }
            stringBuilder.append("\n");
        }

        stringBuilder.append(indent(indentSize));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private static String indent(int indentSize) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < indentSize; i++) {
            stringBuilder.append("\t");
        }
        return stringBuilder.toString();
    }

    private static String formatPrimitiveValue(Object instance, Class<?> type) {
        if (type.equals(boolean.class)
         || type.equals(int.class)
         || type.equals(long.class)
         || type.equals(short.class)) {
            return instance.toString();
        } else if (type.equals(double.class) || type.equals(float.class)) {
            return String.format("%.02f", instance);
        }

        throw new RuntimeException(String.format("Type : %s is unsupported. ", type.getName()));
    }

    private static String formatStringValue(String value) {
        return String.format("\"%s\"", value);
    }
}
