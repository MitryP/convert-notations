/*
 * Copyright (c) 2022. Dmytro Popov
 */

package ua.com.mitryp.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A class that provides useful methods for obtaining input from the user.
 *
 * @author Dmytro Popov
 */
public final class DataInput {
    /**
     * Код символу, який фарбує весь наступний текст в консолі в червоний колір.
     */
    private static final String ANSI_RED = "\u001B[31m";

    /**
     * Код символу, який скидає колір наступного тексту в консолі до значення за замовчуванням.
     */
    private static final String ANSI_RESET = "\u001B[0m";

    /**
     * Очікує від користувача вводу деякого числового значення у межах типу Integer.
     * Після введення числа, що задовольняє обмеження типу Integer, повертає отримане значення.
     * Виводить переданий текст перед очікуванням введення користувача.
     *
     * @param label текст, що має бути виведений перед очікуванням вводу користувача.
     * @return int
     */
    public static Integer getInt(String label) {
        int res;
        while (true) {
            String s = getString(label);
            try {
                res = Integer.parseInt(s);
                break;
            } catch (NumberFormatException ignored) {
                System.out.println(ANSI_RED + "Wrong Integer format! Please enter a correct value." + ANSI_RESET);
            }
        }
        return res;
    }

    /**
     * Очікує від користувача вводу деякої стрічки (можливо, пустої).
     * Після успішного введення стрічки повертає отримане значення.
     *
     * @return String
     */
    public static String getString() {
        return getString("");
    }

    /**
     * Очікує від користувача вводу деякої стрічки (можливо, пустої).
     * Після успішного введення стрічки повертає отримане значення.
     * Виводить переданий текст перед очікуванням введення користувача.
     *
     * @param label текст, що має бути виведений перед очікуванням вводу користувача.
     * @return String
     */
    public static String getString(String label) {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String input;
        while (true) {
            try {
                System.out.print(label + ((label.length() > 0) ? " " : ""));
                input = br.readLine();
                break;
            } catch (IOException ignored) {
                System.out.println(ANSI_RED + "An error occurred! Please repeat input." + ANSI_RESET);
            }
        }
        return input;
    }

}