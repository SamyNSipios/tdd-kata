package com.example.helloworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello there");
    }


    /**
     * @param number Number as string we need to cast to int
     * @return The value of the number as int if valid, 0 otherwise
     */
    public static int castStringToInt(String number) {
        if (number.length() < 1) {
            return 0;
        }
        else {
            try {
                return Integer.parseInt(number);
            }
            catch (NumberFormatException exception) {
                //System.err.println("Invalid format");
                return 0;
            }
        }
    }


    /**
     * Generate the regex pattern to match our new list of separators
     * @param currentSeparators Current string of separators (usually : , or \n)
     * @param newSeparator New separator we want to add in our list of separator
     * @return New regex which match all the separators
     */
    public static String updateSeparators(String currentSeparators, String newSeparator) {
        StringBuilder newSeparators = new StringBuilder(currentSeparators);
        // We remove the end parenthesis
        newSeparators.deleteCharAt(newSeparators.length() - 1);
        // We add our new separator and close the parenthesis
        newSeparators.append(newSeparator).append(']');
        return newSeparators.toString();
    }

    public static boolean isNumeric(String number) {
        try {
            Integer.parseInt(number);
            return true;
        }
        catch (NumberFormatException exception) {
            //System.err.println("Invalid format");
            return false;
        }
    }

    public static String castStringToValidRegex(String regexNotFormatted) {
        var listOfSpecialChar = List.of('$', '^', '*', '.', '?', '|');
        StringBuilder formattedRegex = new StringBuilder();
        for (int i = 0; i < regexNotFormatted.length(); i++) {
            char currentChar = regexNotFormatted.charAt(i);
            if (listOfSpecialChar.contains(currentChar)) {
                formattedRegex.append("\\").append(currentChar);
            }
            else {
                formattedRegex.append(currentChar);
            }
        }
        return formattedRegex.toString();
    }


    /**
     * A valid string is a string just with the valid separators and not two separators a row
     * @param listOfNumbers List of numbers we want to validate to be valid
     * @return A boolean to say if the string is valid
     */
    public static boolean isStringValid(List<String> listOfNumbers, List<String> delimiters) {
        boolean isPrevDelimiter = false;
        for (String currentChar : listOfNumbers) {
            if (isPrevDelimiter && delimiters.contains(currentChar)) {
                return false;
            }
            if (delimiters.contains(currentChar)) {
                isPrevDelimiter = true;
            }
            if (isPrevDelimiter && isNumeric(currentChar)) {
                isPrevDelimiter = false;
            }
            if (!isNumeric(currentChar) && !delimiters.contains(currentChar)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Function to sum a list of number with various separators
     * @param numbers A list of a numbers as a string with various separators
     * @return The result of the sum
     */
    public static int add(String numbers) throws NegativeValueException {

        //Initialize data we need
        List<String> listOfNumbers;
        List<Integer> negativeValues = new ArrayList<>(List.of());
        List<String> delimiters = new ArrayList<>(List.of(",", "\n"));
        int res = 0;

        // Stripe white spaces
        numbers = numbers.replaceAll(" ", "");

        // Extracting delimiters and get the accurate list of numbers
        if (numbers.startsWith("//")) {
            if (numbers.charAt(2) == '[') {
                // Init the length of the delimiters definition
                int startOfDelimiterDefinition = 2;
                int endOfDelimiterDefinition = numbers.indexOf('\n');

                if (endOfDelimiterDefinition < 0) {
                    return 0;
                }

                // Extract the part where we define the delimiters
                String delimitersDefinition = numbers.substring(startOfDelimiterDefinition, endOfDelimiterDefinition);

                // Extract the rest of the string
                numbers = numbers.substring(endOfDelimiterDefinition + 1);

                char firstElementOfAnyLengthDelimiter = '[';
                char lastElementOfAnyLengthDelimiter = ']';

                for (int index = 0; index < delimitersDefinition.length(); index++) {
                    char currentCharacter = delimitersDefinition.charAt(index);
                    if (currentCharacter == firstElementOfAnyLengthDelimiter) {
                        int endOfSeparatorIndex = delimitersDefinition.indexOf(lastElementOfAnyLengthDelimiter, index);
                        if (endOfSeparatorIndex < 0) {
                            return 0;
                        }
                        int startOfSeparatorIndex = index + 1;
                        String newSeparator = delimitersDefinition.substring(startOfSeparatorIndex, endOfSeparatorIndex);
                        delimiters.add(castStringToValidRegex(newSeparator));
                        index = endOfSeparatorIndex;
                    } else {
                        System.out.println(delimitersDefinition.charAt(index));
                    }
                }
            }
            else if (numbers.charAt(3) == '\n') {
                // Define the new separator
                String newSeparator = Character.toString(numbers.charAt(2));
                // Adding our new separator in our string
                delimiters.add(castStringToValidRegex(newSeparator));
                // Remove all the whitespace and remove the definition of the seperator
                numbers = numbers.substring(4);
            }
            else {
                return 0;
            }
        }

        // Create our list of number we want to sum
        String separators = String.join("|", delimiters);
        listOfNumbers = List.of(numbers.split(separators));

        // Is string is not valid we return 0
        if ( !isStringValid(listOfNumbers, delimiters) ) {
            return 0;
        }

        // Iterate over our value to sum them (we ignore more than 1000 and add in an Array if negative values)
        for (String listOfNumber : listOfNumbers) {
            int newElt = castStringToInt(listOfNumber);
            if (newElt < 0) {
                negativeValues.add(newElt);
            }
            if (newElt > 1000) {
                continue;
            }
            res += newElt;
        }

        // Throw an Exception when negative values are detected
        if (negativeValues.size() > 0) {
            throw new NegativeValueException("Negative value : " + negativeValues);
        }

        return res;
    }
}
