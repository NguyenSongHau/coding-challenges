/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nsh.arraymanipulationandmissingnumberproblembybinarytree;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author songh
 */
public class BinarySearchTree {

    Node root;

    void insert(int data) {
        root = insertRec(root, data);
    }

    Node insertRec(Node root, int data) {
        if (root == null) {
            root = new Node(data);
            return root;
        }
        if (data < root.data) {
            root.left = insertRec(root.left, data);
        } else if (data > root.data) {
            root.right = insertRec(root.right, data);
        }
        return root;
    }

    boolean search(Node root, int key) {
        if (root == null) {
            return false;
        }
        if (root.data == key) {
            return true;
        }
        return key < root.data ? search(root.left, key) : search(root.right, key);
    }

    List<Integer> findMissingNumbers(int start, int end) {
        List<Integer> missingNumbers = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            if (!search(root, i)) {
                missingNumbers.add(i);
            }
        }
        return missingNumbers;
    }
}
