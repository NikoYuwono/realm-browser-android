package com.nikoyuwono.realmbrowser.sample.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nyuwono on 12/1/15.
 */
public class Product extends RealmObject {

    @PrimaryKey
    private String          name;
    private int             price;

    public String getName() { return name; }
    public void   setName(String name) { this.name = name; }
    public int    getPrice() { return price; }
    public void   setPrice(int price) { this.price = price; }
}
