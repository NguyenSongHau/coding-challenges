# Array Manipulation And Missing Number Problem By Sum Method
This code defines a class ArrayManipulationAndMissingNumberProblemBySumMethod, which identifies missing numbers in an array from 1 up to the maximum value in the array.
* findMissingNumbers(int[] arr):
    * Finds the maximum value in the array.
    * Creates a boolean array present to track which numbers from 1 to max are in arr.
    * Identifies missing numbers by checking indices in present where values are false and adds them to a missingNumbers list.
* Main method:
    * Initializes an array arr, calls findMissingNumbers to get missing numbers, and prints them. If there are none, it displays a message indicating no missing numbers.
* Note: You should use Apache Netbeans IDE 23 and use SDK 21 to run project