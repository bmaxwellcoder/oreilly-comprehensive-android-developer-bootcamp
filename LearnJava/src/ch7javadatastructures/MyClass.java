package ch7javadatastructures;

import java.util.ArrayList;

public class MyClass {

    public static void main(String[] args) {

        int[] numArr = {1, 33, 4, 12, 89, 98};
        for (int num: numArr) {
            System.out.println("Numbers: " + num);
        }

        String[] namesArr = {"James", "Anthony", "Rodrigo", "Nirai"};

        int[] numArr2 = new int[4];
        numArr2[0] = 12;
        numArr2[1] = 13;
        numArr2[2] = 133;
        numArr2[3] = 112;

        for (int num : numArr2) {
            System.out.println("Num " + num);
        }

        String[] namesArr2 = new String[5];

        // Array Lists
//        ArrayList<String> name = new ArrayList<>();
       // this is a relaxed declaration for an ArrayList, it's better to use the above
        ArrayList namesList = new ArrayList();
//        name.add(1);
//        name.add("Hello");
//        name.remove("Hello");
        namesList.add("Bond");
        namesList.add("James");
        namesList.add("Bonni");
        namesList.add("Arnold");


        for (int i = 0; i < namesList.size(); i++) {
            System.out.println("Names: " + namesList.get(i));
        }

        // Clever for loop
        for (Object n : namesList) {
            System.out.println("Names: " + n);
        }
        System.out.println(namesList);
    }
}
