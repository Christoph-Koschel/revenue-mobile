package com.koschel.revenue.mobile.model;

import java.io.Serializable;

public class TagModel implements Serializable {
    public final int id;
    public final String name;
    public final boolean income;

    public TagModel(int id, String name, boolean income) {
        this.id = id;
        this.name = name;
        this.income = income;
    }
}
