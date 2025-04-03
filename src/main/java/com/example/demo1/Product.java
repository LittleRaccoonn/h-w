
package com.example.demo1;
public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private double finalPrice;
    private String date;


    public Product(int id, String name, double price, int quantity, double finalPrice, String date){
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.finalPrice = finalPrice;
        this.date = date;

    }
    public int getId() {
        return id;
    }
    public String getname() {
        return name;
    }
    public double getprice() {
        return price;

    }
    public int getquantity() {
        return quantity;

    }
    public double getfinalprice() {
        return finalPrice;
    }
    public String getdate() {
        return date;
    }

}
