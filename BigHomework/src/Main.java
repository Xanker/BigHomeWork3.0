//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import mapper.FruitDAO;
import mapper.HerbDAO;
import mapper.WoodDAO;
import products.*;
import ui.LoginUI;


public class Main {


    int w = 0;
    private static Inventory inventory = new Inventory();

    private static Scanner scanner = new Scanner(System.in);

    // 正确的 main 方法签名
    public static void main(String[] args) throws SQLException {

        LoginUI.showLogin();

        // 测试购物车（先模拟添加商品）
        //ShoppingCart cart = new ShoppingCart();
       // Fruit apple = new Fruit("苹果", 101, "红色", 2.5, "斤",
                //"山东烟台", LocalDate.of(2001,4,2), 8.0);
       // cart.addProduct(apple, 2);
       // ShoppingCartUI.showCartUI(cart);


        updateData();
        while (true) {
            System.out.println("\n==== 农产品库存管理系统 ====");
            System.out.println("0. 添加产品");
            System.out.println("1. 查看库存产品总数");
            System.out.println("2. 出售产品");
            System.out.println("3. 查看各类产品所有产品");
            System.out.println("4. 修改数据");
            System.out.println("5. 退出");
            System.out.println("6. 删除");
            System.out.println("7. 查找");
            System.out.print("请选择操作: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 0:
                    addProduct();
                    updateData();
                    break;
                case 1:
                    System.out.println("木头有:" + inventory.getWoodStock() + '\n');
                    System.out.println("草药有:" + inventory.getHerbStock() + '\n');
                    System.out.println("水果有:" + inventory.getFruitStock() + '\n');
                    System.out.println("共有:" + inventory.getStock() + '\n');
                    break;
                case 2:
                    sellProduct();
                    updateData();
                    break;
                case 3:
                    System.out.println(inventory.getAllProducts() + "\n");
                    break;
                case 4:
                    Change();
                    break;
                case 6:
                    Deleted();
                case 7:
                    Find();

                case 5:
                    scanner.close();
                    System.out.println("感谢使用，再见！");
                    return; // 退出程序

            }
        }
    }

    private static void Find() throws SQLException {
        System.out.println("请输入产品类型（水果/树木/草药）:");
        String productType = scanner.nextLine();

        switch (productType) {
            case"wood":
                System.out.println("输入id");
                int id = Integer.parseInt(scanner.nextLine());
                if (inventory.findbyID(id,"Wood")) {
                    System.out.println(inventory.getWood(id));
                }
            case "herb":
                System.out.println("输入id");
                int id1 = Integer.parseInt(scanner.nextLine());
                if (inventory.findbyID(id1,"Herb")) {
                    System.out.println(inventory.getHerb(id1));
                }
            case"Fruit":
                System.out.println("输入id");
                int id2 = Integer.parseInt(scanner.nextLine());
                if (inventory.findbyID(id2,"Fruit")) {
                    System.out.println(inventory.getFruit(id2));
                }
        }
    }
    // 商品添加方法
    private static void addProduct() throws SQLException {
        System.out.println("请输入产品类型（水果/树木/草药）:");
        String productType = scanner.nextLine();

        switch (productType) {
            case "树木":
                addWood();
                int num = 0;

                break;
            case "水果":
                // 添加水果逻辑
                addFruit();
                break;
            case "草药":
                // 添加草药逻辑
                addHerb();
                break;
            default:
                System.out.println("不支持的产品类型");
                break;
        }

    }

    private static void addWood() throws SQLException {
        System.out.println("请输入树木名称:");
        String name = scanner.nextLine();

        System.out.println("ID:");
        int id = Integer.parseInt(scanner.nextLine()); // 避免换行符问题
        if(!inventory.findbyID(id,"Wood"))
        {
            System.out.println("ID重复，请重新输入");
            return;
        }

        System.out.println("价格：");
        int price = Integer.parseInt(scanner.nextLine());


        System.out.println("长度:");
        double length = Double.parseDouble(scanner.nextLine());

        System.out.println("宽度:");
        double width = Double.parseDouble(scanner.nextLine());

        System.out.println("厚度:");
        double thickness = Double.parseDouble(scanner.nextLine());


        System.out.println("时间");
        String input = scanner.nextLine().trim();  // trim用于去空格
        // 创建自定义格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy M d");
        // 使用格式器解析日期
        LocalDate date = LocalDate.parse(input, formatter);
        System.out.println("产地");
        String in = scanner.nextLine().trim();

        System.out.println("Stock?0");
        Integer inn = Integer.parseInt(scanner.nextLine());
        Wood wood = new Wood(name, id, price, length, width, thickness, date, in, inn);

        inventory.Add(wood, wood.getStock());
        WoodDAO woodDAO = new WoodDAO();
        woodDAO.insert(wood);

        System.out.println("添加成功！\n" + inventory.getWood(wood.getID()) + "\t" + inventory.getWoodStock());
        System.out.println(inventory.getWood(wood.getID()).getWaterness());
    }

    public static void addFruit() throws SQLException {
        System.out.println("请输入水果名称:");
        String name = scanner.nextLine();

        System.out.println("ID:");
        int ID = Integer.parseInt(scanner.nextLine()); // 避免换行符问题

        if(!inventory.findbyID(ID,"Fruit"))
        {
            System.out.println("ID重复，请重新输入");
            return;
        }

        System.out.println("颜色:");
        String color = scanner.nextLine();

        System.out.println("重量:");
        double weight = Double.parseDouble(scanner.nextLine());

        System.out.println("单位:");
        String unit = scanner.nextLine();

        System.out.println("原产地:");
        String origin = scanner.nextLine();

        System.out.println("收获时间");

        String date = scanner.nextLine().trim();


        LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy M d"));

        System.out.println("单位价格");
        double unitprice = Double.parseDouble(scanner.nextLine());


        Fruit fruit = new Fruit(name, ID, color, weight, unit, origin, date1, unitprice);
        inventory.Add(fruit, fruit.getStock());
        System.out.println("添加成功！\n" + inventory.getFruit(fruit.getID()) + "\t" + inventory.getFruitStock());
        System.out.println("新鲜度为:" + inventory.getFruit(fruit.getID()).getMaturity());
        FruitDAO fruitDAO = new FruitDAO();
        fruitDAO.insert(fruit);

    }

    public static void addHerb() throws SQLException {
        System.out.println("请输入草药名称:");
        String name = scanner.nextLine();

        System.out.println("ID:");
        int ID = Integer.parseInt(scanner.nextLine()); // 避免换行符问题

        if(!inventory.findbyID(ID,"Herb"))
        {
            System.out.println("ID重复，请重新输入");
            return;
        }

        System.out.println("采摘季节:");
        String pSeason = scanner.nextLine();


        System.out.println("原产地:");
        String origin = scanner.nextLine();

        System.out.println("保质期");
        int pMonth = Integer.parseInt(scanner.nextLine());

        System.out.println("性质");
        String property = scanner.nextLine();

        System.out.println("收获时间");

        String date = scanner.nextLine().trim();
        LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy M d"));

        System.out.println("价格");
        Double price = Double.parseDouble(scanner.nextLine());

        System.out.println("数量");
        int stock = Integer.parseInt(scanner.nextLine());
        Herb herb = new Herb(name, ID, origin, pSeason, property, date1, price, stock);

        inventory.Add(herb, herb.getStock());
        System.out.println("添加成功！\n" + inventory.getHerb(herb.getID()) + "\t" + inventory.getHerbStock());
        HerbDAO herbDAO = new HerbDAO();
        herbDAO.insert(herb);
    }

    //出售（删除）
    public static void sellProduct() throws SQLException {
        System.out.println("请输入产品类型（水果/树木/草药）:");
        String productType = scanner.nextLine();

        switch (productType) {
            case "树木":
                System.out.println("请输入ID");
                int ID = Integer.parseInt(scanner.nextLine());
                sellWood(ID);
                break;
            case "水果":
                System.out.println("请输入ID");
                int ID1 = Integer.parseInt(scanner.nextLine());
                sellFruit(ID1);
                break;
            case "草药":
                System.out.println("请输入ID");
                int ID2 = Integer.parseInt(scanner.nextLine());
                sellHerb(ID2);
                break;
            default:
                System.out.println("不支持的产品类型");
                break;

        }
    }

    private static void sellWood(int ID) throws SQLException {
        inventory.sell(ID, 1,"Wood");//需要对getWood进行修改？？
    }

    private static void sellFruit(int id) throws SQLException {
        inventory.sell(id, 1,"Fruit");
    }

    private static void sellHerb(int id) throws SQLException {
        inventory.sell(id, 1,"Herb");
    }


    /// 修改功能
    public static void Change() throws SQLException {
        System.out.println("请输入产品类型（水果/树木/草药）:");
        String productType = scanner.nextLine();

        switch (productType) {
            case "树木":
                System.out.println("请输入ID");
                int ID = Integer.parseInt(scanner.nextLine());
                changeWood(ID);
                break;
            case "水果":
                System.out.println("请输入ID");
                int ID1 = Integer.parseInt(scanner.nextLine());
                chageFruit(ID1);
                break;
            case "草药":
                System.out.println("请输入ID");
                int ID2 = Integer.parseInt(scanner.nextLine());
                chageHerb(ID2);
                break;
            default:
                System.out.println("不支持的产品类型");
                break;
        }
    }

    public static void changeWood(int id) throws SQLException {
        WoodDAO woodDAO = new WoodDAO();
        Wood wood = inventory.getWood(id);

        if (wood != null) {
            System.out.println("请输入新的树木名称（不修改请直接回车）:");
            String newName = scanner.nextLine();
            if (!newName.isEmpty())
            {
                wood.setName(newName);
            }

            System.out.println("请输入新的长度（不修改请直接回车）:");
            String newLengthStr = scanner.nextLine();
            if (!newLengthStr.isEmpty()) {
                double newLength = Double.parseDouble(newLengthStr);
                wood.setLength(newLength);
            }

            System.out.println("请输入新的宽度（不修改请直接回车）:");
            String newWidthStr = scanner.nextLine();
            if (!newWidthStr.isEmpty()) {
                double newWidth = Double.parseDouble(newWidthStr);
                wood.setWidth(newWidth);
            }

            System.out.println("请输入新的厚度（不修改请直接回车）:");
            String newThicknessStr = scanner.nextLine();
            if (!newThicknessStr.isEmpty()) {

                wood.setThickness(Double.parseDouble(newThicknessStr));
            }
            System.out.println("请输入新的数量（不修改请直接回车）:");
            int stock = Integer.parseInt(scanner.nextLine());
            if (!newThicknessStr.isEmpty()) {

                wood.setStock(stock);
                inventory.UpdateCount(wood, stock);
            }
            System.out.println("请输入新的产地（不修改请直接回车）:");
            String newOrigin = scanner.nextLine();
            if (!newOrigin.isEmpty()) {
                wood.setOrigin(newOrigin);
            }
            System.out.println("请输入新的价格:");
           String newPrice = scanner.nextLine();
            if (!newPrice.isEmpty()) {
                Double newPrice1 = Double.parseDouble(newPrice);
                wood.setunitprice(newPrice1);
            }

            if (woodDAO.update(wood)) {
                System.out.println("树木数据修改成功！");
            } else {
                System.out.println("树木数据修改失败！");
            }
        } else {
            System.out.println("未找到对应的树木产品！");
        }
    }

    public static void chageFruit(int id) throws SQLException {
        FruitDAO fruitDAO = new FruitDAO();
        Fruit fruit = inventory.getFruit(id);

        if (fruit != null) {
            System.out.println("请输入新的水果名称（不修改请直接回车）:");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                fruit.setName(newName);
            }

            System.out.println("请输入新的颜色（不修改请直接回车）:");
            String newColor = scanner.nextLine();
            if (!newColor.isEmpty()) {
                fruit.setColor(newColor);
            }

            System.out.println("请输入新的重量（不修改请直接回车）:");
            String newWeightStr = scanner.nextLine();
            if (!newWeightStr.isEmpty()) {
                double newWeight = Double.parseDouble(newWeightStr);
                fruit.setWeight(newWeight);
                inventory.UpdateCount( inventory.getFruit(id), (int) newWeight);
            }

            System.out.println("请输入新的单位（不修改请直接回车）:");
            String newUnit = scanner.nextLine();
            if (!newUnit.isEmpty()) {
                fruit.setUnit(newUnit);
            }

            System.out.println("请输入新的产地（不修改请直接回车）:");
            String newOrigin = scanner.nextLine();
            if (!newOrigin.isEmpty()) {
                fruit.setOrigin(newOrigin);
            }

            System.out.println("请输入新的收获时间（格式：yyyy M d，不修改请直接回车）:");
            String newDateStr = scanner.nextLine();
            if (!newDateStr.isEmpty()) {
                LocalDate newDate = LocalDate.parse(newDateStr, DateTimeFormatter.ofPattern("yyyy M d"));
                fruit.setDate(newDate);
            }

            System.out.println("请输入新的单位价格（不修改请直接回车）:");
            String newUnitPriceStr = scanner.nextLine();
            if (!newUnitPriceStr.isEmpty()) {
                double newUnitPrice = Double.parseDouble(newUnitPriceStr);
                fruit.setUnitPrice(newUnitPrice);

            }

            if (fruitDAO.update(inventory.getFruit(id))) {
                System.out.println("水果数据修改成功！");

            } else {
                System.out.println("水果数据修改失败！");
            }
        } else {
            System.out.println("未找到对应的水果产品！");
        }
    }

    public static void chageHerb(int id) throws SQLException {
        HerbDAO herbDAO = new HerbDAO();
        Herb herb = inventory.getHerb(id);

        if (herb != null) {
            System.out.println("请输入新的草药名称（不修改请直接回车）:");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                herb.setHerbname(newName);
                herb.setName(newName);
            }

            System.out.println("请输入新的产地（不修改请直接回车）:");
            String newOrigin = scanner.nextLine();
            if (!newOrigin.isEmpty()) { // 仅当输入非空字符串时更新
                herb.setOrigin(newOrigin);
            }

            System.out.println("请输入新的采摘季节（不修改请直接回车）:");
            String newpSeason = scanner.nextLine();
            if (!newpSeason.isEmpty()) {
                herb.setSeason(newpSeason);
            }

            System.out.println("请输入新的性质（不修改请直接回车）:");
            String newProperty = scanner.nextLine();
            if (!newProperty.isEmpty()) {
                herb.setProperty(newProperty);
            }

            System.out.println("请输入新的收获时间（格式：yyyy M d，不修改请直接回车）:");
            String newDateStr = scanner.nextLine();
            if (!newDateStr.isEmpty()) {
                LocalDate newDate = LocalDate.parse(newDateStr, DateTimeFormatter.ofPattern("yyyy M d"));
                herb.setPurchday(newDate);
            }

            System.out.println("请输入新的价格（不修改请直接回车）:");
            String newPriceStr = scanner.nextLine();
            if (!newPriceStr.isEmpty()) {
                double newPrice = Double.parseDouble(newPriceStr);
                herb.setUnitPrice(newPrice);
            }

            System.out.println("请输入新的数量（不修改请直接回车）:");
            String newStockStr = scanner.nextLine();
            if (!newStockStr.isEmpty()) {
                int newStock = Integer.parseInt(newStockStr);
                herb.setStock(newStock);
                inventory.UpdateCount( inventory.getHerb(id), newStock);
            }

            if (herbDAO.update(inventory.getHerb(id))) {
                System.out.println("草药数据修改成功！");


            } else {
                System.out.println("草药数据修改失败！");
            }
        } else {
            System.out.println("未找到对应的草药产品！");
        }
    }

    /// 删除功能
    public static void deleteHerb(int id, String str) throws SQLException {
        switch (str)
        {
            case "Fruit":
                inventory.delete(id,str);
                FruitDAO fruitDAO = new FruitDAO();
                fruitDAO.delete(id);
            case "Herb":
                inventory.delete(id,str);
                HerbDAO herbDAO = new HerbDAO();
                herbDAO.delete(id);
            case "Wood":
                inventory.delete(id,str);
                WoodDAO woodDAO = new WoodDAO();
                woodDAO.delete(id);
        }

    }
    public static void updateData() throws SQLException
    {
        FruitDAO fruitDAO = new FruitDAO();
        WoodDAO woodDAO = new WoodDAO();
        HerbDAO herbDAO = new HerbDAO();
        //fruitDAO.insert(fruit);
        inventory.setProducts(fruitDAO.loadProductTable());
        inventory.setProducts(woodDAO.loadProductTable());
        inventory.setProducts(herbDAO.loadProductTable());
    }
    private static void Deleted() throws SQLException {
        System.out.println("请输入你要删除的数据类型");
        System.out.println("请输入产品类型（水果/树木/草药）:");
        String productType = scanner.nextLine();

        switch (productType) {
            case "树木":
                System.out.println("输入id \n");
                int id = Integer.parseInt(scanner.nextLine());
                deleteHerb(id,"Wood");
                int num = 0;

                break;
            case "水果":
                // 添加水果逻辑
                System.out.println("输入id \n");
                int id1 = Integer.parseInt(scanner.nextLine());
                deleteHerb(id1,"Fruit");

                break;
            case "草药":
                // 添加草药逻辑
                System.out.println("输入id \n");
                int id2 = Integer.parseInt(scanner.nextLine());
                deleteHerb(id2,"Herb");
                break;
            default:
                System.out.println("不支持的产品类型");
                break;
        }



    }
}
