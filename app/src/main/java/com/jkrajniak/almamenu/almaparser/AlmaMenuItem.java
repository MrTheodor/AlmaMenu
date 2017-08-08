package com.jkrajniak.almamenu.almaparser;

public class AlmaMenuItem {
    public String name;
    public boolean isVeggie;
    public String price;

    public String toString() {
        return name + ", veggie: " + isVeggie + ", price: " + price;
    }
}
