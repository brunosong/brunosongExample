package com.brunosong.javaexam.arrays;

import java.lang.reflect.Array;

public class Main {
    public static void main(String[] args) {
        int[] oneDimensionalArray = {1,2};
        double[][] twpDimensionalArray = { {3.4,5.2}, {3.4,5.2}  };
        inspectArrayValue(twpDimensionalArray);
        inspectArrayObject(twpDimensionalArray);
    }

    public static void inspectArrayValue(Object arrayObject) {
        int length = Array.getLength(arrayObject);

        System.out.print("[");
        for (int i = 0; i < length; i++) {
            Object element = Array.get(arrayObject,i);

            if (element.getClass().isArray()) {
                inspectArrayValue(element);
            } else {
                System.out.print(element);
            }

            if (i != length-1) {
                System.out.print(" , ");
            }
        }
        System.out.print("]");
    }

    public static void inspectArrayObject(Object arrayObject) {
        Class<?> aClass = arrayObject.getClass();

        System.out.println(String.format("Is array : %s", aClass.isArray()));

        Class<?> componentType = aClass.getComponentType();

        System.out.println(String.format("This is an array of type : %s", componentType.getTypeName()));
    }
}
