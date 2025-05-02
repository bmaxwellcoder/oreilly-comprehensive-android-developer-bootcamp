package ch6inheritance;

public class Manager extends Employee{

    @Override
    public double getAnnualSalary() {
        return super.getAnnualSalary() + 10_000;
    }

    @Override
    public int getId() {
        return super.getId() + 100;
    }

    @Override
    public String toString() {
        return this.getFirstName() + ", "
                + this.getId() + ", "
                + this.getAnnualSalary();
    }
}
