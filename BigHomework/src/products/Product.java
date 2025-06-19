package products;

import java.sql.Date;
import java.time.LocalDate;

public abstract class Product {
    private String name;
    private String type;
    private String description;
    private int ID;
    private LocalDate date;
    private int stock;
    private double unitprice;
    // 构造函数、getter和setter方法、toString()方法
    public int getID()
    {
        return ID;
    }
    public void setID(int ID)
    {
        this.ID = ID;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getType()
    {
        return type;
    }
    public void setType()
    {
        type = getClass().getSimpleName();
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public int getStock() {
        return stock;
    }
    public double getunitprice() {
        return unitprice;
    }

    public void setUnitprice(double unitprice) {
        this.unitprice = unitprice;
    }
}

