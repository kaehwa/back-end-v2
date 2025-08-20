package com.example.gaehwa2.utils;

import java.util.List;

public class ColorUtils {
    public static float[] flattenRgb(List<List<Integer>> rgbSelected) {
        float[] flat = new float[rgbSelected.size() * 3]; // 5*3=15
        int index = 0;
        for (List<Integer> rgb : rgbSelected) {
            for (Integer value : rgb) {
                flat[index++] = value.floatValue();
            }
        }
        return flat;
    }
}


