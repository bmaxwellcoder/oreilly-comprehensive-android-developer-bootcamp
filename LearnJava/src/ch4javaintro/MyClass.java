package ch4javaintro;

public class MyClass {

    public static void main(String[] args) {

        // Declaring and initializing variables of different data types
        byte bMin = -128; // Min value for a byte
        byte bMax = 127; // Max value for a byte
        short sMin =  -32_768; // Min value for short
        short sMax = 32_767; // Max value for short
        int iMin = -2_147_483_648; // Min value for an integer
        int iMax = 2_147_483_647; // Max value for an integer
       long lMin = -9_223_372_036_854_775_808L; // Min value for a long
       long lMax = 9_223_372_036_854_775_807L; // Max value for a long

        // Declaring and initializing floating-point numbers
        float fNegMin = -3.4028234663852886E+38f;
        float fPosMin = -1.40129846432481707E-45f;
        float fNegMax = -1.40129846432481707E-45f;
        float fPosMax = 3.4028234663852886E+38f;
        double dNegMin = -1.7976931348623157E+308;
        double dPosMin = 4.94065645841246544E-324;
        double dNegMax = -4.94065645841246544E-324;
        double dPosMax = 1.7976931348623157E+308;

        // Declaring and initializing character and string variables
        char middleInitial = 'm'; // Example value for char
        String name = "Georgina"; // Example value for String
        int age = 23;
        System.out.println("Hello World!");
        System.out.println("My name is " + name + " and I'm " + age + " years old");
        System.out.println("Hey, I am a programmer.");
        System.out.println("My middle initial is: " + middleInitial);

        // Declaring and initializing a boolean variable
        boolean isTrue = true; // Example value for boolean
        System.out.println("My name is Bryan: " + isTrue);

        int a = 12;
        int b = 5;

        double total = (double) b / a; // casting
        total += total;
        System.out.println(total);

        double remainder = a % b;
        System.out.println("Remainder of " + a  + "/" +   b + " is " + remainder);

        // Relational Operators
        // == --> comparison
        // !- --> NOT
        // != --> Negation
        // > --> Greater than
        // < --> Less than
        // >= --> Greater than or equal to
        // <= --> Less than or equal to

        // Control flow
        double val1 = 10;
        double val2 = 12;
        boolean isAged;

        if (val2 > val1) {
            isAged = true;
        } else {
            isAged = false;
        }

        if (isAged) {
            System.out.println("Indeed:");
        } else {
            System.out.println("Not true");
        }

        // Logical Operators
        // AND --> &&
        // OR --> ||

    }

    // Methods
    public static void addNumbers(int a, int b) {
        System.out.println(a + b);
    }

}