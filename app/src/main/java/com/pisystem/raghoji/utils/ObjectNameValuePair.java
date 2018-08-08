package com.pisystem.raghoji.utils;

import java.io.Serializable;


public class ObjectNameValuePair implements  Cloneable, Serializable {

    private static final long serialVersionUID = -6437800749411518984L;

    private final String name;
    private final Object value;

    /**
     * Default Constructor taking a name and a value. The value may be null.
     *
     * @param name  The name.
     * @param value The value.
     */
    public ObjectNameValuePair(final String name, final Object value) {
        super();
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }


    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}