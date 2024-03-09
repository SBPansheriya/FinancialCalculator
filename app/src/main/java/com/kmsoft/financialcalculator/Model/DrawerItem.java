package com.kmsoft.financialcalculator.Model;

public class DrawerItem {
    String name;
    int icon;

    public DrawerItem(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

}
