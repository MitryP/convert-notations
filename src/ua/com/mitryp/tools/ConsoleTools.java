/*
 * Copyright (c) 2022. Dmytro Popov
 */

package ua.com.mitryp.tools;

/**
 * Клас, що реалізує деякі зручні методи для виводу даних в консоль.
 *
 * @author Dmytro Popov
 */
public class ConsoleTools {
    /**
     * Виводить у консоль передану стрічку.
     *
     * @param text {@link String}
     */
    public static void println(String text) {
        System.out.println(text);
    }

    /**
     * Виводить у консоль усі перераховані через кому стрічки, використовуючи {@link StringBuilder рекомендований спосіб
     * конкатенації стрічок}.
     *
     * @param arr перераховані через кому стрічки
     */
    public static void println(Object... arr) {
        StringBuilder sb = new StringBuilder();
        for (Object text : arr) {
            sb.append(text.toString());
            sb.append(" ");
        }
        System.out.println(sb);
    }
}
