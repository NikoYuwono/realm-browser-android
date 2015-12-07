package com.nikoyuwono.realmbrowser.sample.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nyuwono on 12/1/15.
 */
public class User extends RealmObject {

    @PrimaryKey
    private String          name;
    private int             age;

    public String getName() { return name; }
    public void   setName(String name) { this.name = name; }
    public int    getAge() { return age; }
    public void   setAge(int age) { this.age = age; }
}