package com.koschel.revenue.mobile.model;

public class ActionModel {
    public final String name;
    public final int drawable;

    public final Class<?> target;

    public ActionModel(String name, int drawable, Class<?> target) {
        this.name = name;
        this.drawable = drawable;
        this.target = target;
    }
}
