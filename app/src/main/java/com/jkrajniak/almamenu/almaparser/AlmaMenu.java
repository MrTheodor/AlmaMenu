package com.jkrajniak.almamenu.almaparser;

import java.util.ArrayList;
import java.util.Date;

public class AlmaMenu {
    public Date date;
    public int almaCode;
    public ArrayList<AlmaMenuItem> menuItems;
    public AlmaMenu(int almaCode, Date date) {
        menuItems = new ArrayList<>();
        this.almaCode = almaCode;
        this.date = date;
    }

    public String toString() {
        String s =  "Menu Alma-" + this.almaCode + " date: " + date.toString() + "\nMenu:";
        for (AlmaMenuItem item : menuItems) {
            s += "\t" + item.toString() + "\n";
        }
        return s;
    }


}
