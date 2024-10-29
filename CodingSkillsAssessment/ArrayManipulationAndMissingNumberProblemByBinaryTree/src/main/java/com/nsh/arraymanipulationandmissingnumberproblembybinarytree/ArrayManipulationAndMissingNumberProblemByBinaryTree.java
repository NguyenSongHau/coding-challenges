package com.nsh.arraymanipulationandmissingnumberproblembybinarytree;

import java.util.ArrayList;
import java.util.List;

public class ArrayManipulationAndMissingNumberProblemByBinaryTree {
    public static void main(String[] args) {
        int[] array = {1, 2, 4, 6, 7, 9};
        BinarySearchTree bst = new BinarySearchTree();

        for (int num : array) {
            bst.insert(num);
        }

        List<Integer> missingNumbers = bst.findMissingNumbers(1, 9);

        if (missingNumbers.isEmpty()) {
            System.out.println("There are no missing numbers.");
        } else {
            System.out.println("The missing numbers are: " + missingNumbers);
        }
    }
}
