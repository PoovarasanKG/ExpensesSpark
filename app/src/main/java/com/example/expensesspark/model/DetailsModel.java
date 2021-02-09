package com.example.expensesspark.model;

public class DetailsModel<name, value>
{
    private final name nameStr;
    private final value valueStr;

    public DetailsModel(name nameStr, value valueStr) {
        this.nameStr = nameStr;
        this.valueStr = valueStr;
    }

    public name getName() { return nameStr; }
    public value getValue() { return valueStr; }
}