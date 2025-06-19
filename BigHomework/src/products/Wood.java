package products;

import java.sql.Date;
import java.time.LocalDate;

public class Wood extends Product {
    private String name;
    private String type;
    private double length;
    private double width;
    private double thickness;
    private double Waterness;
    private String origin;
    private LocalDate date;
    private double unitprice;
    private double totalprice;
    private int stock;

    // 构造函数、getter和setter方法、glow()方法
    public Wood(String name,int ID,double unitprice, double length, double width, double thickness,LocalDate date,String origin,int stock) {
        this.name = name;
        super.setID(ID);
        this.unitprice = unitprice;
        super.setUnitprice(unitprice);
        this.length = length;
        this.width = width;
        this.thickness = thickness;
        this.date = date;
        this.origin = origin;
        setType();
        dry();
        super.setDate(this.date);
        this.stock = stock;
        super.setUnitprice(unitprice);
        super.setStock(stock);
        totalprice = unitprice * stock;
    }

    public Wood() {

    }

    /// get方法
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
        setWaterness();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return "Wood";
    }
    public double getLength() {
        return length;
    }
    public double getWidth() {
        return width;
    }
    public double getThickness() {
        return thickness;
    }
    public double getWaterness() {
        return Waterness;
    }

    ///set方法
    public void setType() {
        type = getClass().getSimpleName();
    }
    public void setLength(double length) {
        this.length = length;
    }
    public void setWidth(double width) {
        this.width = width;
    }
    public void setThickness(double thickness) {
        this.thickness = thickness;
    }
    public void setWaterness() {
        dry();
    }
    public void setWaterness(double waterness) {
        Waterness = waterness;
    }

    public String toString() {
        return "这是一棵ID为：" + super.getID() + '\t' + "的" + name  + '\t' + "属性：" + type + '\t'+"长为:" + length + '\t' + "宽为：" + width;

    }
    public void dry()
    {
          Waterness = 100;
          LocalDate now = LocalDate.now();
          int distance = 365 * (now.getYear() - date.getYear()) + (now.getDayOfYear() - date.getDayOfYear());
          Waterness = Waterness-((double)distance*0.001);
    }

    public String getOrigin() {

        return origin;
    }

    public void setOrigin(String origin)
    {
     this.origin = origin;
    }


    public double getUnitPrice() {
        return unitprice;
    }

    public int getTotalPrice() {

        totalprice = (int)unitprice * stock;
        return (int)totalprice;
    }
    public void setunitprice(double unitprice) {
        this.unitprice = unitprice;
    }

    @Override
    public void setStock(int stock) {
        super.setStock(stock);
        this.stock = stock;
    }


}
