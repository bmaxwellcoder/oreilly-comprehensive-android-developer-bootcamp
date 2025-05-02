package ch5oop;

public class MyClass {
    public static void main(String[] args) {

        Microphone microphone1 = new Microphone();
        microphone1.setName("Blue");
        microphone1.setName("Blue Yeti");
        microphone1.setModel(133543);

        Microphone microphone2 = new Microphone("Blue Yeti",  "Blue", 546);

        microphone2.turnOff();
        microphone2.setVolume();
        microphone2.turnOff();

        System.out.println(microphone2.showDescription());
        System.out.println(microphone2);
    }
}

