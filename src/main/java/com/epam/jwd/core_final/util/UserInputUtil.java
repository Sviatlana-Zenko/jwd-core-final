package com.epam.jwd.core_final.util;

import java.util.Scanner;

public final class UserInputUtil {

    private UserInputUtil(){
    }

    public static int getUserNumberInput(String message) {
        Scanner inputScanner = new Scanner(System.in);
        int enteredNumber;

        System.out.print(message);
        while (!inputScanner.hasNextInt()) {
            inputScanner.nextLine();
            System.out.println("Wrong input type. Try again -> ");
        }
        enteredNumber = inputScanner.nextInt();

        return enteredNumber;
    }

    public static String getUserStringInput(String message) {
        Scanner inputScanner = new Scanner(System.in);
        String enteredString;

        System.out.print(message);
        enteredString = inputScanner.nextLine();

        return enteredString;
    }
}
