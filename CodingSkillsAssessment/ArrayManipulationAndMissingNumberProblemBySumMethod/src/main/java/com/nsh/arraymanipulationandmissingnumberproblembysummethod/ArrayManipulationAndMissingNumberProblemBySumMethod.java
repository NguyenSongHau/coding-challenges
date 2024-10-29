package com.nsh.arraymanipulationandmissingnumberproblembysummethod;

import java.util.ArrayList;
import java.util.List;

public class ArrayManipulationAndMissingNumberProblemBySumMethod {

    public static List<Integer> findMissingNumbers(int[] arr) {
        int max = arr[0];
        for (int number : arr) {
            if (number > max) {
                max = number;
            }
        }

        boolean[] present = new boolean[max + 1];

        for (int number : arr) {
            if (number >= 1 && number <= max) {
                present[number] = true;
            }
        }

        List<Integer> missingNumbers = new ArrayList<>();
        for (int i = 1; i <= max; i++) {
            if (!present[i]) {
                missingNumbers.add(i);
            }
        }
        
        return missingNumbers;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 7, 10};
        List<Integer> missingNumbers = findMissingNumbers(arr);

        if (missingNumbers.isEmpty()) {
            System.out.println("There are no missing numbers.");
        } else {
            System.out.println("The missing numbers are: " + missingNumbers);
        }
    }
}