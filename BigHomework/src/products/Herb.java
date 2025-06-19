package products;

import java.time.LocalDate;

public class Herb extends Product {
    private String Herbname;
    private String Origin;
    private String pSeason;
    private String type;
    private int pMonth;
    private String  property;
    private LocalDate date;
    private int stock;
    private double totalPrice;
    private double unitPrice;
    // 构造函数、getter和setter方法、glow()方法
    public Herb(String Herbname, int id, String Origin, String pSeason,  String property,LocalDate date,double price,int stock) {
        this.Herbname = Herbname;
        super.setID(id);
        this.Origin = Origin;
        this.pSeason = pSeason;
        pMonth = date.getMonthValue();
        this.property = property;
        this.date = date;
        this.stock = stock;
        unitPrice = price;
        super.setUnitprice(unitPrice);
        super.setDate(this.date);
        super.setName(this.Herbname);
        setType();
        super.setStock(stock);
        calculate();


    }

    public Herb() {

    }

    private void calculate() {
        totalPrice = stock * unitPrice;
    }

    //  getter方法
    public String getHerbname(){
        return Herbname;
    }
    public String getOrigin(){
        return Origin;
    }
    public String getSeason(){
        return pSeason;
    }
    public int getMonth(){
        return pMonth;
    }
    public String getProperty(){
        return property;
    }

    //setter方法
    public void setType(){
        type = getClass().getSimpleName();
    }
    public void setHerbname(String Herbname){
        this.Herbname = Herbname;
    }
    public void setOrigin(String Origin){
        this.Origin = Origin;
    }
    public void setSeason(String pSeason){
        this.pSeason = pSeason;
    }
    public void setProperty(String property){
        this.property = property;
    }
    public String getType(){
        return "Herb";
    }
    public String preserve()
    {
         switch(property)
         {
             case "寒性":

                 String str1 = "冷藏";
                 super.setDescription(str1);
                 return str1;
             case "凉性":
                 String str2 =  "冷藏";
                 super.setDescription(str2);
                 return str2;
             case "热性":
                 String str3 =  "常温无光";
                 super.setDescription(str3);
                 return str3;
             case "温性":
                 String str4 =  "常温无光";
                 super.setDescription(str4);
                 return str4;

             default:
                 String str5 = "未知方法";
                 super.setDescription(str5);
                 return str5;

         }
    }

    public LocalDate getPurchday() {
        return date;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
        super.setStock(stock);
        calculate();
    }

    public void setPurchday(LocalDate newDate) {
        this.date = newDate;
        pMonth = newDate.getMonthValue();
    }


    public double getUnitPrice() {
        return unitPrice;
    }
    public double getTotalPrice() {
        calculate();
        return totalPrice;
    }

    public void setUnitPrice(double newPrice) {
        this.unitPrice = newPrice;
    }
    public void setDate(LocalDate newDate) {
        this.date = newDate;
        pMonth = newDate.getMonthValue();
    }

    public void setMonth(int pMonth) {
        this.pMonth = pMonth;
    }
}
