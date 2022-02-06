/*
 * Copyright (c) 2022. Dmytro Popov
 */

package ua.com.mitryp;

import static ua.com.mitryp.tools.ConsoleTools.*;

/**
 * A class that implements methods for converting numbers between different notations.
 *
 * @author Dmytro Popov
 */
public class Bases {
    /**
     * A difference between the char with number 0 and a start of numerical chars.
     */
    private static final byte NUMBER_DIFFERENCE = '0';

    /**
     * A difference between the char with number 0 and a start of alphabetic chars.
     */
    private static final byte LETTER_DIFFERENCE = 'a' - ('9' - '0' + 1);

    /**
     * A quantity of signs after the dot.
     */
    private static final byte ROUND_SIGNS = 10;

    /**
     * A correction constant. This value is being added to the result of rounding.
     */
    private static final double CORRECTION = 0.003;

    /**
     * A method that is responsible for processing command line arguments.
     * Takes arguments in format: "base_of_the_number   number   base_to_convert_the_number_to".
     * If arguments quantity != 3, obtains correct input from the user.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length == 3)
                println(translate(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2])));
            else {
                do {
                    String input = ua.com.mitryp.tools.DataInput.getString("Please enter a number:");
                    int startBase = ua.com.mitryp.tools.DataInput.getInt("Please enter a notation base of the number:");
                    int endBase = ua.com.mitryp.tools.DataInput.getInt("Please enter a notation base that number is being converted to:");
                    println(translate(startBase, input, endBase));
                } while (ua.com.mitryp.tools.DataInput.getInt("Continue? (1/0):") == 1);
            }
        } catch (IllegalArgumentException e) {
            println(e.getMessage());
        }
    }

    /**
     * Returns an integer representation of the given character (must be in bounds from '0' to 'z').
     * Also, checks whether the char can be used in notation with the base `base` (must be in bounds from 2 to 35)
     *
     * @param chr  a character that is being translated to integer value
     * @param base a notation base in which the number is
     * @return int
     */
    public static int getValue(char chr, int base) {
        chr = Character.toLowerCase(chr);
        if (!Character.isLetterOrDigit(chr))
            throw new IllegalArgumentException("Error! '" + chr + "' must be in bounds form '0' to 'z'");
        if (base < 2) throw new IllegalArgumentException("Error! A notation base must be in bounds [2, 36]");

        byte diff;
        if (chr >= 'a' && chr <= 'z')
            diff = LETTER_DIFFERENCE;
        else diff = NUMBER_DIFFERENCE;

        byte res = (byte) (chr - diff);
        if (res >= base)
            throw new IllegalArgumentException("Error! Character '" + chr + "' cannot be used in a notation with base of " + base + ".");

        return chr - diff;
    }

    /**
     * Returns an alphabetic-numerical representation of the given number (must be in bounds from 0 to 35).
     * For example, with number=10 it will return "a" char.
     *
     * @param number byte
     * @return char
     * @throws IllegalArgumentException if number not in [0, 35]
     */
    public static char getCharForNumber(byte number) throws IllegalArgumentException {
        if (number < getValue('0', 36) || number > getValue('z', 36))
            throw new IllegalArgumentException("Error! Number '" + number + "' doesn't have an alphabetic representation in English alphabet!");

        if (number >= 0 && number < 10)
            return (char) (number + NUMBER_DIFFERENCE);
        return (char) (number + LETTER_DIFFERENCE);
    }

    /**
     * Translates the string representation of the natural number `natural` in notation `base`
     * to the decimal number system.
     *
     * @param natural a number that is being translated
     * @param base    a notation of the `natural` number
     * @return int dec(natural, base)
     */
    private static int getDecFromNaturalInBase(String natural, int base) {
        int res = 0;
        for (int i = natural.length() - 1, power = 0; i >= 0; i--, power++)
            res += getValue(natural.charAt(i), base) * Math.pow(base, power);
        return res;
    }

    /**
     * Translates the string natural representation of the float `mantis` in notation `base` to the decimal number system.
     * (For `0.131415` takes `131415`).
     *
     * @param mantis natural representation of the float part of the number
     * @param base   a notation of the mantis
     * @return double
     */
    private static double getDecFromMantisInBase(String mantis, int base) {
        double res = 0;
        for (int i = 0; i < mantis.length(); i++)
            res += getValue(mantis.charAt(i), base) * Math.pow(base, -(i + 1));
        return res;
    }

    /**
     * Translates real number from notation `base` to the decimal number system.
     *
     * @param num  число, яке переводиться a number that is being translated
     * @param base a notation of the num
     * @return double dec(num, base)
     */
    public static String getDec(String num, int base) {
        if (base < 2) throw new IllegalArgumentException("Error! A notation base must be in bounds [2, 36]");
        char sep = getSeparator(num);
        if (num.indexOf(sep) < 0) num += ".0";
        String[] parts = num.split("\\" + sep);
        return getDecFromNaturalInBase(parts[0], base) +
                ("" + getDecFromMantisInBase(parts[1], base)).substring(1);
    }

    /**
     * Returns a string representation of the decimal natural number translated to the `base` notation.
     *
     * @param number a decimal number that is to be translated
     * @param base   a notation to that number must be converted
     * @return String
     */
    private static String translateDecIntToBase(int number, int base) {
        StringBuilder resB = new StringBuilder();
        while (number != 0) {
            resB.append(getCharForNumber((byte) (number % base)));
            number = number / base;
        }
        resB.reverse();
        return resB.toString();
    }

    /**
     * Returns a string representation of the decimal float number in bounds (0, 1) translated to the `base` notation.
     *
     * @param mantis a decimal float number that is to be converted
     * @param base   a notation to translate the mantis to
     * @return String
     */
    private static String translateDecMantisToBase(double mantis, int base) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < ROUND_SIGNS; i++) {
            res.append(getCharForNumber((byte) (mantis * base)));
            mantis = (mantis * base) % 1;
            if (round(mantis) == 0) break;
        }
        return res.toString();
    }

    /**
     * Returns a string representation of the decimal real number translated to the `base` notation.
     *
     * @param number a number that is being translated
     * @param base   a notation that number to be converted to
     * @return String
     */
    private static String translateDecToBase(String number, int base) {
        if (number.indexOf(getSeparator(number)) == -1) number += ".0";
        String[] parts = number.split("\\" + getSeparator(number));
        return translateDecIntToBase(Integer.parseInt(parts[0]), base) +
                "." +
                translateDecMantisToBase(Double.parseDouble("0." + parts[1]), base);
    }

    /**
     * Returns a string representation of the real number in notation `startBase` in the `endBase` notation.
     * Uses the decimal number system as an intermediate system.
     *
     * @param startBase a notation of the number `input`
     * @param input     a string representation of the number that is to be translated
     * @param endBase   a notation of the number that `input` is to be translated to
     * @return String representation of the result of converting
     */
    public static String translate(int startBase, String input, int endBase) {
        if (startBase < 2 || endBase < 2)
            throw new IllegalArgumentException("Error! A notation base must be in bounds [2, 36]");
        String dec = getDec(input, startBase);
        return translateDecToBase(dec, endBase);
    }

    /**
     * Повертає роздільник дробу (',' або '.') зі строкового представлення числа.
     * Якщо в строці немає роздільника, повертає '.'.
     * <p>
     * Returns a separator that has been found in numberRepresentation.
     * If no separator found, returns '.'.
     *
     * @param numberRepresentation a string representation of the floating-point number
     * @return char '.' || ','
     */
    private static char getSeparator(String numberRepresentation) {
        return ((numberRepresentation.indexOf(',') >= 0) ? ',' : '.');
    }

    /**
     * Rounds the given number to the ROUND_SIGNS number of signs after the point.
     * Also, adds the CORRECTION constant to the result.
     *
     * @param number a number that is being rounded
     * @return double
     */
    private static double round(double number) {
        return Math.floor(number * Math.pow(10, ROUND_SIGNS) + CORRECTION) / Math.pow(10, ROUND_SIGNS);
    }

}
