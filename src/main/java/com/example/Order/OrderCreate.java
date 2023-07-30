package com.example.Order;

import java.util.List;

public class OrderCreate {

    private List<String > ingredients;

    public OrderCreate(List<String > ingredients) {
        this.ingredients = ingredients;
    }
    public OrderCreate() {}

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
