package com.example.grocery_shopping_cart;

public class GroceryModel {

    String Image;
    String Name;
    String Price;
    String Quantity;
    String Id;

    public GroceryModel() {
    }

    public GroceryModel(String image, String name,String price, String quantity, String id) {
        Image = image;
        Price = price;
        Name = name;
        Quantity = quantity;
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
