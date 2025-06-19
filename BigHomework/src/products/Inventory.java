package products;
import mapper.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.SQLException;

public class Inventory <T extends Product>{
    private ArrayList<T> products = new ArrayList<>();//商品列表
    private final HashMap<T, Integer> productsCount = new HashMap<>();//库存数量

    //add（进货）
    public void Add(T product, int count) throws SQLException {
        products.add(product);
        productsCount.put(product, productsCount.getOrDefault(product, 0) + count);
        // 更新数据库中的库存数量
        updateStockInDB(product, productsCount.get(product));
    }

    //sell(销售)
    public void sell(int id, int count,String type) throws SQLException {

        switch (type) {
            case "Fruit": {
                Fruit wood = getFruit(id);
                if (wood == null) {
                    break;
                }
                if (productsCount.getOrDefault(wood, 0) >= count) {
                    productsCount.put((T) wood, productsCount.get(wood) - count);
                    if (productsCount.get(wood) == 0) {
                        products.remove(wood);
                        productsCount.remove(wood);
                    }
                    // 更新数据库中的库存数量
                    updateStockInDB((T) wood, productsCount.getOrDefault(wood, 0));

                    SalesRecord salesRecord = new SalesRecord(LocalDate.now(), 123, wood, count, wood.getUnitPrice());
                    SalesRecordDAO salesRecordDAO = new SalesRecordDAO();
                    salesRecordDAO.insertSalesRecord(salesRecord);
                } else {
                    System.out.println("产品数量不足");
                }
            }
                case "Herb":{
                    Herb herb = getHerb(id);
                    if(herb == null){
                        break;
                    }

                    if (productsCount.getOrDefault(herb, 0) >= count)
                    {
                        productsCount.put((T) herb, productsCount.get(herb) - count);
                        if (productsCount.get(herb) == 0) {
                            products.remove(herb);
                            productsCount.remove(herb);
                        }
                        // 更新数据库中的库存数量
                        updateStockInDB((T) herb, productsCount.getOrDefault(herb, 0));

                        SalesRecord salesRecord = new SalesRecord(LocalDate.now(), 123, herb, count, herb.getUnitPrice());
                        SalesRecordDAO salesRecordDAO = new SalesRecordDAO();
                        salesRecordDAO.insertSalesRecord(salesRecord);
                    }
                    else
                    {
                        System.out.println("产品数量不足");
                    }

                }
                case "Wood":{
                    Wood wood = getWood(id);
                    if (wood == null) {
                        System.out.println("查无此产品");
                        break;
                    }
                    if (productsCount.getOrDefault(wood, 0) >= count)
                    {
                        productsCount.put((T) wood, productsCount.get(wood) - count);
                        if (productsCount.get(wood) == 0) {
                            products.remove(wood);
                            productsCount.remove(wood);
                        }
                        // 更新数据库中的库存数量
                        updateStockInDB((T) wood, productsCount.getOrDefault(wood, 0));

                        SalesRecord salesRecord = new SalesRecord(LocalDate.now(), 123, wood, count, wood.getUnitPrice());
                        SalesRecordDAO salesRecordDAO = new SalesRecordDAO();
                        salesRecordDAO.insertSalesRecord(salesRecord);
                    }
                    else
                    {
                        System.out.println("产品数量不足");
                    }
                }


        }

    }

    //Check（库存查询）
    public int getStock(){
        int i = getWoodStock() + getHerbStock() + getFruitStock();
        return i;
    }

    public int getWoodStock( ){
        int num = 0;
        for(T product : products)
        {
            if(product.getType().equals("Wood"))
                num++;
        }
        return num;
    }
    public int getHerbStock( ){
        int num = 0;
        for(T product : products)
        {
            if(product.getType().equals("Herb"))
                num++;
        }
        return num;
    }
    public int getFruitStock( ){
        int num = 0;
        for(T product : products)
        {
            if(product.getType().equals("Fruit"))
                num++;
        }
        return num;
    }
    //Update(库存更新)
    public void UpdateCount(T product, int count) throws SQLException {
        productsCount.put(product, count);
        // 更新数据库中的库存数量
        updateStockInDB(product, count);
    }

    public T getWOod(int Id){
        for(T product: products) {
            String type = product.getType();
            if(type.equals("Wood") && product.getID() == Id) {
                return product;
            }
        }
        return null;
    }

    public Wood getWood(int ID) {
        for(T product : products) {
            if(product.getID() == ID && product.getType().equals("Wood")) {
                return (Wood) product;
            }
        }
        return null;
    }

    public Fruit getFruit(int ID) {
        for(T product : products ) {
            if(product.getID() == ID && product.getType().equals("Fruit")) {
                return (Fruit) product;
            }
        }
        return null;
    }

    public Herb getHerb(int ID) {
        for(T product : products) {
            if(product.getID() ==  ID && product.getType().equals("Herb")) {
                return (Herb) product;
            }
        }
        return null;
    }

    public ArrayList<T> getAllProducts() {
        return new ArrayList<>(products);
    }

    public void setProducts(ArrayList<T> products) {
        this.products = products;
        for(T product : products) {
            productsCount.put(product, product.getStock());
            System.out.println(productsCount.get(product));
        }
    }

    private void updateStockInDB(T product, int count) throws SQLException {
        ProductDAO<T> dao = getDAO(product);
        dao.updateStock(product, count);
    }

    @SuppressWarnings("unchecked")
    public ProductDAO<T> getDAO(T product) throws SQLException {
        if (product instanceof Fruit) {
            return (ProductDAO<T>) new FruitDAO();
        } else if (product instanceof Wood) {
            return (ProductDAO<T>) new WoodDAO();
        } else if (product instanceof Herb) {
            return (ProductDAO<T>) new HerbDAO();
        }
        return null;
    }
    public boolean findbyID(int id,String str) throws SQLException
    {
        switch (str) {
            case "Wood":
                if(getWood(id) != null);
                return true;
                case "Herb":
                    if(getHerb(id) != null);
                    return true;
                    case "Fruit":
                        if(getFruit(id) != null);
                        return true;
        }
        return false;
    }
    public void delete(int id ,String str) throws SQLException {
        switch (str) {
            case "Wood":
                productsCount.remove(findbyID(id, str));
            case "Herb":
                productsCount.remove(findbyID(id, str));
            case "Fruit":
                products.remove(findbyID(id, str));
        }

    }

}