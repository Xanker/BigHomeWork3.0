package products;

import java.sql.Date;
import java.time.LocalDate;

public class Fruit extends Product {
    private String color;
    private double weight;
    private String unit;  // "斤" 或 "个"
    private String origin;
    private String maturity;
    private String name;
    private LocalDate date;
    private String type;
    private double unitPrice;  // 每斤/每个的价格
    private double totalPrice;  // 总价

    // 构造函数
    public Fruit(String name, int ID, String color, double weight, String unit,
                 String origin, LocalDate date, double unitPrice) {
        this.name = name;
        super.setID(ID);
        this.color = color;
        this.weight = weight;
        setUnit(unit);  // 使用setter方法设置单位
        this.origin = origin;
        this.date = date;
        this.unitPrice = unitPrice;
        super.setUnitprice(unitPrice);
        setType();
        calculateTotalPrice();  // 计算总价

        super.setDate(this.date);
        super.setStock((int) weight);
        totalPrice = weight * unitPrice;
        glow();
    }
    public Fruit()
    {

    }

    // 计算总价方法
    public Object calculateTotalPrice() {
        if ("斤".equals(unit)) {
            totalPrice = weight * unitPrice;  // 按斤计算
        } else if ("个".equals(unit)) {
            totalPrice = weight * unitPrice;  // 按个计算
        }
        return null;
    }

    // ===== 原有getter/setter方法 =====
    public String getType() {
        return "Fruit";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        calculateTotalPrice();
        super.setStock((int)weight);
        totalPrice = unitPrice * weight;// 重量变更时重新计算价格
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        if (unit == null || (!unit.equals("斤") && !unit.equals("个"))) {
            this.unit = "斤"; // 设置默认单位
            System.out.println("警告：单位不合法，已设为默认值'斤'，ID: " + getID());
        } else {
            this.unit = unit;
        }
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getMaturity() {
        return maturity;
    }

    public void setType()
    {
        type = getClass().getSimpleName();
    }



    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        glow();  // 日期变更时更新成熟度
    }

    // ===== 新添加的价格相关方法 =====
    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice();  // 价格变更时重新计算总价
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // ===== 成熟度计算方法 =====
    public void glow() {
        LocalDate now = LocalDate.now();
        int distance = 365 * (now.getYear() - date.getYear()) + (now.getDayOfYear() - date.getDayOfYear());

        if (distance <= 10) {
            maturity = "未完全成熟";
        } else if (distance <= 20) {
            maturity = "成熟";
        } else if (distance <= 30) {
            maturity = "即将变质";
        } else {
            maturity = "腐烂";
        }
    }

    // ===== toString方法增强 =====

        public String toString() {
            return String.format("""
            === 水果信息 ===
            名称: %s
            ID: %d
            类型: %s
            颜色: %s
            重量: %.2f %s
            单价: ￥%.2f/%s
            总价: ￥%.2f
            产地: %s
            收获日期: %s
            成熟度: %s
            ==============
            """,
                    name, getID(), getClass().getSimpleName(),
                    color, weight, unit,
                    unitPrice, unit, totalPrice,
                    origin, date, maturity);
        }

    // 模拟计数方法（根据需求补充逻辑）
    public void count() {
        // 这里可以添加库存计数逻辑
    }
}