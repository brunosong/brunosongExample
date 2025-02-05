package com.brunosong.javaexam.jsonwriter;

import com.brunosong.javaexam.jsonwriter.data.Address;
import com.brunosong.javaexam.jsonwriter.data.Person;

import java.lang.reflect.Field;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Address address = new Address("seoul", (short) 1);
        Person person = new Person("brunosong", 30, true, 33.11f, address);

        String s = objectToJson(person,0);

        System.out.println(s);
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
                sb.append(formatPrimitiveValue(field, instance));
            } else if (field.getType().equals(String.class)){
                sb.append(formatStringValue(field.get(instance).toString()));
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

    private static String indent(int indentSize) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < indentSize; i++) {
            stringBuilder.append("\t");
        }
        return stringBuilder.toString();
    }

    private static String formatPrimitiveValue(Field field, Object parentInstance) throws IllegalAccessException {
        if (field.getType().equals(boolean.class)
         || field.getType().equals(int.class)
         || field.getType().equals(long.class)
         || field.getType().equals(short.class)) {
            return field.get(parentInstance).toString();
        } else if (field.getType().equals(double.class) || field.getType().equals(float.class)) {
            return String.format("%.02f", field.get(parentInstance));
        }

        throw new RuntimeException(String.format("Type : %s is unsupported. ", field.getType().getName()));
    }

    private static String formatStringValue(String value) {
        return String.format("\"%s\"", value);
    }
}
