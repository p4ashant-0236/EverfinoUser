package com.everfino.everfinouser.Models;

public class OrderItem {
    public Menu Menu;
    public Restaurant Restaurant;

    public Menu getItem() {
        return Menu;
    }

    public void setItem(Menu item) {
        this.Menu = item;
    }

    public Restaurant getRest() {
        return Restaurant;
    }

    public void setRest(Restaurant rest) {
        this.Restaurant = rest;
    }
}
