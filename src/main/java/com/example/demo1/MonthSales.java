package com.example.demo1;

public class MonthSales {
    private String month; // Теперь String для названий месяцев
    private double sum;

    public MonthSales(String month, double sum) {
        this.month = month;
        this.sum = sum;
    }

    public String getMonths() {
        return month;
    }

    public double getSum() {
        return sum;
    }
}
