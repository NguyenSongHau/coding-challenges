package com.nsh.productinventorymanagement;

import com.nsh.productinventorymanagement.models.Product;
import com.nsh.productinventorymanagement.models.ProductList;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Scanner;

public class ProductInventoryManagement {

    private static final String PRODUCTS_JSON_PATH = "src/main/resources/products.json";

    public static void main(String[] args) {
        ProductList productList = readProductsFromJson(PRODUCTS_JSON_PATH);
        if (productList != null) {
            printProductList(productList);
            String totalValue = calculateTotalInventoryValue(productList);
            System.out.printf("Total Inventory Value: %s%n", totalValue);
            boolean hasHeadphones = checkProductExists(productList, "Headphones");
            System.out.println("Product 'Headphones' exists in inventory: " + hasHeadphones);
            
            
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose sorting option:");
            System.out.println("1. Sort by price (ascending)");
            System.out.println("2. Sort by price (descending)");
            System.out.println("3. Sort by quantity (ascending)");
            System.out.println("4. Sort by quantity (descending)");
            System.out.print("Enter your choice (1-4): ");
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    sortProducts(productList, "price", true);
                    break;
                case 2:
                    sortProducts(productList, "price", false);
                    break;
                case 3:
                    sortProducts(productList, "quantity", true);
                    break;
                case 4:
                    sortProducts(productList, "quantity", false);
                    break;
                default:
                    System.out.println("Invalid choice. No sorting applied.");
            }

            printProductList(productList);
        }
    }

    private static ProductList readProductsFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductList productList = null;

        try {
            productList = objectMapper.readValue(new File(filePath), ProductList.class);
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }

        return productList;
    }

    private static void printProductList(ProductList productList) {
        System.out.printf("%-20s %-10s %-10s%n", "Name", "Price", "Quantity");
        System.out.println("----------------------------------------------------");

        for (Product product : productList.getProducts()) {
            System.out.printf("%-20s %-10.2f %-10d%n", product.getName(), product.getPrice(), product.getQuantity());
        }
    }

    private static String calculateTotalInventoryValue(ProductList productList) {
        double totalValue = 0;

        for (Product product : productList.getProducts()) {
            totalValue += product.getPrice() * product.getQuantity();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String formattedValue = decimalFormat.format(totalValue);
        return formattedValue.replace(',', '#')
                             .replace('.', ',')
                             .replace('#', '.');
    }

    private static boolean checkProductExists(ProductList productList, String productName) {
        for (Product product : productList.getProducts()) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }

    private static void sortProducts(ProductList productList, String sortBy, boolean ascending) {
        Comparator<Product> comparator;

        if ("price".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparingDouble(Product::getPrice);
        } else {
            comparator = Comparator.comparingInt(Product::getQuantity);
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        productList.getProducts().sort(comparator);
    }
}