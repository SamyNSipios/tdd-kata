package com.example.helloworld;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloWorldTest {
    @Test
    void addWithNullValue() {
        try {
            assertEquals(0, HelloWorld.add(""), "Should return 0 if no value is input");
        }
        catch (NegativeValueException exception) {
            assertTrue(exception.getMessage().isEmpty());
        }
    }

    @Test
    void addWithOneValue() {
        try {
            assertEquals(1, HelloWorld.add("1"), "Should return 1 if 1 is input");
            assertEquals(5, HelloWorld.add("5"), "Should return 5 if 5 is input");
            assertEquals(0, HelloWorld.add("a"), "Should return 0 if input is incorrect");
        }
        catch (NegativeValueException exception) {
            assertTrue(exception.getMessage().isEmpty());
        }
    }

    @Test
    void addWithTwoOrMoreValue() {
        try {
            assertEquals(6, HelloWorld.add("1     ,2     ,3"),"Should return 6 if 1,2,3 is input");
            assertEquals(10, HelloWorld.add("5,5"),"Should return 10 if 5,5 is input");
            assertEquals(0, HelloWorld.add("a,6"),"Should return 6 if input is 6 and incorrect");
        }
        catch (NegativeValueException exception) {
            assertTrue(exception.getMessage().isEmpty());
        }
    }

    @Test
    void addWithCornerCase() {
        try {
            assertEquals(6, HelloWorld.add("1  \n  2     ,3"),"Should return 6 if 1\\n2,3 is input");
            assertEquals(10, HelloWorld.add("5,5"),"Should return 10 if 5,5 is input");
            assertEquals(0, HelloWorld.add("a,6"),"Should return 6 if input is 6 and incorrect");
            //assertEquals(0, HelloWorld.add("2, 2"), "Should return -2");
        }
        catch (NegativeValueException exception) {
            assertTrue(exception.getMessage().isEmpty());
        }
    }

    @Test
    void addWithSeparatorAtTheBeginning() {
        try {
            assertEquals(3, HelloWorld.add("//|\n1|2"));
            assertEquals(3, HelloWorld.add("//:\n1:2"));
            assertEquals(8, HelloWorld.add("//;\n2;2,2\n2"));
            assertEquals(0, HelloWorld.add("//;\n2;2,2|2"));
        }
        catch (NegativeValueException exception) {
            assertTrue(exception.getMessage().isEmpty());
        }
    }

    @Test
    void addShouldThrowOnNegativeValues() {
        try {
            Exception exception = assertThrows(Exception.class, () -> HelloWorld.add("-2, -1"));
            String expectedMessage = "-2";
            String actualMessage = exception.getMessage();
            System.out.println(actualMessage);
            assertTrue(actualMessage.contains(expectedMessage));
        }
        catch (Exception exception) {
            System.err.println("Should not be printed as an error");
            assertTrue(exception.getMessage().isEmpty());
        }
    }

    @Test
    void addShouldIgnoreValueSuperiorAt1000 () {
        try {
            assertEquals(2, HelloWorld.add("2, 1001"), "Should ignore the 1001");
        }
        catch (NegativeValueException exception) {
            assertTrue(exception.getMessage().isEmpty(), "In superior at 1000");
        }
    }

    @Test
    void addShouldUseDelimitersWithUnknownLength () {
        try {
            assertEquals(3, HelloWorld.add("//[;;]\n2;;1"), "Should accept a new separator : ;;");
            assertEquals(0, HelloWorld.add("//[;;]\n2;1"), "Should not work with just one (point virgule)");
            assertEquals(10, HelloWorld.add("//[***]\n2***5***3"), "Should get ten with 3 stars separators");
        }
        catch (NegativeValueException exception) {
            System.err.println(exception.getMessage());
            assertTrue(exception.getMessage().isEmpty(), "In not known length delimiters");
        }
    }

    @Test
    void addShouldAcceptMultiDelimiterWithUnknownLength () {
        try {
            assertEquals(4, HelloWorld.add("//[;;][%]\n2;;1%1"), "Should accept a new separator : ;;");
            assertEquals(0, HelloWorld.add("//[;;\n2;;1"), "Should return 0 because the parameter definition is not closed");
        }
        catch (NegativeValueException exception) {
            System.err.println(exception.getMessage());
            assertTrue(exception.getMessage().isEmpty(), "In not known length delimiters");
        }
    }

}