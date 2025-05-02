package ch6inheritance;

public class MyClass {

    public static void main(String[] args) {

//        Person james = new Person();
        Person james = new Person("James", "Bond", 47);

        System.out.println(james.getAge());

        Employee employee = new Employee();
        employee.setFirstName("James");
        employee.setLastName("Bond");
        employee.setAge(45);
        employee.setId(12345);
        employee.setAnnualSalary(100_000);

        System.out.println(employee.getAnnualSalary());

        Manager manager = new Manager();
        manager.setFirstName("Manager George");
        manager.setLastName("Kilos");
        manager.setAge(35);
        manager.setId(456);
        manager.setAnnualSalary(45_000);

        System.out.println(manager.getAnnualSalary() + " , " +
                manager.getFirstName()  + " , id: " + manager.getId());


        System.out.println(manager);

        // String is an object
        int a = 12;
        String myString = "Hello there";
        boolean isEmpty = myString.isEmpty();

        if (!isEmpty) {
            System.out.println(myString);
        } else {
            System.out.println("Empty");
        }
    }
}
