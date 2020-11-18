package edu.spr19cs210.drill03;

public class Triangle extends Shape {
    private double base;
    private double height;

    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    public double area() {
        return height * base * .5;
    }

    public String toString() {
        return "Triangle(base = " + base + ", height = " + height + ")";
    }
}
