package ui;

import mapper.FruitDAO;
import mapper.HerbDAO;
import mapper.ProductDAO;
import mapper.WoodDAO;
import products.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Member;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ui.ManagerUI.*;

public class ShoppingCartUI extends JFrame {
    private Members cart;
    // 用于展示购物车商品列表
    private JTabbedPane tabbedPane;
    private JTable productTable,cartTable;
    private DefaultTableModel productModel,cartModel;
    private JList<String> cartList;
    private DefaultListModel<String> listModel;
    private static final Color HEADER_BG_COLOR = new Color(102, 51, 153);
    private Inventory inventory = new Inventory();
    public ShoppingCartUI(Members cart) throws SQLException {

        getInventory();
        setTitle("商品管理系统 - 管理员");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // 顶部标题栏（紫色）
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(HEADER_BG_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("购物车");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JLabel userLabel = new JLabel("管理员");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        userLabel.setForeground(Color.WHITE);
        titlePanel.add(userLabel, BorderLayout.EAST);

        add(titlePanel, BorderLayout.NORTH);

        // 标签页面板
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("微软雅黑", Font.BOLD, 14));
        tabbedPane.setBackground(BG_COLOR);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        try {
            // 添加标签页
            addProductTab();
            addCartTab();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "数据加载失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        add(tabbedPane, BorderLayout.CENTER);

        // 底部按钮面板
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton refreshAllBtn = createStyledButton("刷新全部", BUTTON_YELLOW, Color.BLACK);
        refreshAllBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        refreshAllBtn.addActionListener(e -> refreshTable());
        bottomPanel.add(refreshAllBtn);


        add(bottomPanel, BorderLayout.SOUTH);

        // 设置水果标签为默认选中
        tabbedPane.setSelectedIndex(0);


       // Container container = getContentPane();
        //container.setLayout(new BorderLayout());

        // 初始化列表模型


        JLabel totalPriceLabel = new JLabel("总价：" + cart.getName());
        JButton checkoutBtn = new JButton("结算");
        JButton continueShopBtn = new JButton("继续购物");

        bottomPanel.add(totalPriceLabel);
        bottomPanel.add(checkoutBtn);
        bottomPanel.add(continueShopBtn);
       // container.add(bottomPanel, BorderLayout.SOUTH);

        tabbedPane.setSelectedIndex(0);
    }

    // 启动购物车界面
    public static void showCartUI(Members cart) throws SQLException {
        ShoppingCartUI cartUI = new ShoppingCartUI(cart);
        cartUI.setVisible(true);
    }


    public void addProductTab() throws SQLException {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tabPanel.setBackground(Color.WHITE);

        // 表格标题
        JLabel tableTitle = new JLabel("商品列表");
        tableTitle.setFont(new Font("微软雅黑", Font.BOLD, 18));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        tableTitle.setForeground(new Color(70, 70, 70));
        tabPanel.add(tableTitle, BorderLayout.NORTH);

        // 创建水果表格
        String[] columns = {"ID", "名称", "产地", "收获日期", "单价", "库存","类型"};
        productModel = createTableModel(columns);

        productTable = createStyledTable(productModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        // 操作按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton addBtn = createStyledButton("添加进购物车", BUTTON_GREEN, Color.WHITE);
        addBtn.addActionListener(e -> showAddDialog(productTable));

        JButton editBtn = createStyledButton("结算", BUTTON_BLUE, Color.WHITE);
        editBtn.addActionListener(e -> {

        });


        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        //buttonPanel.add(refreshBtn);

        tabPanel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("商品", tabPanel);
    }
    public void addCartTab() throws SQLException {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tabPanel.setBackground(Color.WHITE);

        // 表格标题
        JLabel tableTitle = new JLabel("购物车列表");
        tableTitle.setFont(new Font("微软雅黑", Font.BOLD, 18));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        tableTitle.setForeground(new Color(70, 70, 70));
        tabPanel.add(tableTitle, BorderLayout.NORTH);

        // 创建表格
        String[] cartColumns = {"ID", "名称", "单价", "数量", "小计"};
        cartModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // 仅允许修改数量列
            }
        };

        cartTable = createStyledTable(cartModel);
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        // 操作按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));



        JButton editBtn = createStyledButton("结算", BUTTON_BLUE, Color.WHITE);
        editBtn.addActionListener(e -> {

        });

        JButton deleteBtn = createStyledButton("删除", BUTTON_GREEN, Color.WHITE);
        deleteBtn.addActionListener(e -> {

        });

        JLabel totalLabel = new JLabel("总价：0.00");
        totalLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));

// 添加监听器动态计算总价
        cartModel.addTableModelListener(e -> {
            double total = 0;
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                total += Double.parseDouble(cartModel.getValueAt(i, 4).toString());
            }
            totalLabel.setText("总价：" + String.format("%.2f", total));
        });

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.add(totalLabel);
        tabPanel.add(infoPanel, BorderLayout.SOUTH);


        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        //buttonPanel.add(refreshBtn);

        tabPanel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("购物车", tabPanel);
    }

    private void showAddDialog(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择商品", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 获取选中商品数据
        Object id = table.getValueAt(selectedRow, 0);
        Object name = table.getValueAt(selectedRow, 1);
        Object price = table.getValueAt(selectedRow, 4);
        Object stock = table.getValueAt(selectedRow, 5);

        // 数量选择对话框
        String input = JOptionPane.showInputDialog(this, "请输入购买数量：", "添加商品", JOptionPane.PLAIN_MESSAGE);
        if (input == null || input.trim().isEmpty()) return;

        try {
            int quantity = Integer.parseInt(input);
            int availableStock = (stock != null) ? Integer.parseInt(stock.toString()) : 0;

            // 验证库存
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "数量必须大于0", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (quantity > availableStock) {
                JOptionPane.showMessageDialog(this, "库存不足！最大可用数量：" + availableStock, "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 检查是否已存在购物车中
            boolean exists = false;
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                if (cartModel.getValueAt(i, 0).equals(id)) {
                    int oldQty = (int) cartModel.getValueAt(i, 3);
                    cartModel.setValueAt(oldQty + quantity, i, 3); // 更新数量
                    double newSubtotal = (oldQty + quantity) * Double.parseDouble(price.toString());
                    cartModel.setValueAt(newSubtotal, i, 4); // 更新小计
                    exists = true;
                    break;
                }
            }

            // 新增商品条目
            if (!exists) {
                double subtotal = quantity * Double.parseDouble(price.toString());
                cartModel.addRow(new Object[]{
                        id, name, price, quantity, subtotal
                });
            }

            // 更新库存显示（可选）
            table.setValueAt(availableStock - quantity, selectedRow, 5);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效数字", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);

        // 设置表格样式
        table.setRowHeight(30);
        table.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(255, 248, 225)); // 淡黄色选中背景
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(240, 240, 240));
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 设置单元格左对齐
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }

        // 设置表头样式
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 14));
        header.setBackground(HEADER_BG_COLOR);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // 表头左对齐
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        return table;
    }

    /// 创建商品表格
    private DefaultTableModel createTableModel( String[] columns) throws SQLException {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        /// "ID", "名称", "产地", "收获日期", "单价", "库存"

                List<Fruit> fruits = new FruitDAO().getAllFruits();
                for (Fruit fruit : fruits) {
                    model.addRow(new Object[]{
                            fruit.getID(),
                            fruit.getName(),
                            fruit.getOrigin(),
                            fruit.getDate(),
                            fruit.getUnitPrice(),
                            fruit.getStock(),
                            fruit.getType()
                    });
                }


        /// "ID", "名称", "产地", "收获日期", "单价", "库存"
                // 直接读取herb表完整数据
                List<Herb> herbs = new HerbDAO().getAllHerbs();
                for (Herb herb : herbs) {
                    model.addRow(new Object[]{
                            herb.getID(),
                            herb.getName(),
                            herb.getOrigin(),
                            herb.getPurchday(),
                            herb.getUnitPrice(),
                            herb.getStock(),
                            herb.getType()
                    });
                }


        /// "ID", "名称", "产地", "收获日期", "单价", "库存"

                // 直接读取wood表完整数据
                List<Wood> woods = new WoodDAO().getAllWoods();
                for (Wood wood : woods) {
                    model.addRow(new Object[]{
                            wood.getID(),
                            wood.getName(),
                            wood.getOrigin(),
                            wood.getDate(),
                            wood.getUnitPrice(),
                            wood.getStock(),
                            wood.getType()

                    });
                }


        return model;
    }
    private DefaultTableModel addToCart(String[] columns) throws SQLException {
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        return model;
    }
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        // 添加悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }
    private void refreshTable() {
        try {

                    productModel.setRowCount(0);
                    ArrayList<?> fruitProducts = getProducts("Fruit");
                    addProductsToModel(fruitProducts, productModel, "Fruit");

                    ArrayList<?> herbProducts = getProducts("Herb");
                    addProductsToModel(herbProducts, productModel, "Herb");


                    ArrayList<?> woodProducts = getProducts("Wood");
                    addProductsToModel(woodProducts, productModel, "Wood");


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "刷新失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    private ArrayList<?> getProducts(String type) throws SQLException {
        if (type.equals("Fruit"))
        {
            return (ArrayList<?>) new FruitDAO().getAllFruits();
        }
        else if (type.equals("Herb"))
        {
            return (ArrayList<?>) new HerbDAO().getAllHerbs();
        }
        else if (type.equals("Wood"))
        {
            return (ArrayList<?>) new WoodDAO().getAllWoods();
        }
        return new ArrayList<>();
    }

    /// "ID", "名称", "产地", "收获日期", "单价", "库存"
    private void addProductsToModel(ArrayList<?> products, DefaultTableModel model, String type) {
        for (Object product : products) {
            Object[] rowData = new Object[model.getColumnCount()];
            if (type.equals("Fruit") && product instanceof Fruit) {
                Fruit fruit = (Fruit) product;
                rowData[0] = fruit.getID();
                rowData[1] = fruit.getName();
                rowData[2] = fruit.getOrigin();
                rowData[3] = fruit.getDate();
                rowData[4] = fruit.getUnitPrice();
                rowData[5] = fruit.getStock();
                rowData[6] = fruit.getType();
            } else if (type.equals("Herb") && product instanceof Herb) {
                Herb herb = (Herb) product;
                rowData[0] = herb.getID();
                rowData[1] = herb.getName();                        /// "ID", "名称", "产地", "收获日期", "单价", "库存"
                rowData[2] = herb.getOrigin();
                rowData[3] = herb.getPurchday();
                rowData[4] = herb.getUnitPrice();
                rowData[5] = herb.getStock();
                rowData[6] = herb.getType();
            } else if (type.equals("Wood") && product instanceof Wood) {
                Wood wood = (Wood) product;
                rowData[0] = wood.getID();
                rowData[1] = wood.getName();
                rowData[2] = wood.getOrigin();
                rowData[3] = wood.getDate();
                rowData[4] = wood.getUnitPrice();
                rowData[5] = wood.getStock();
                rowData[6] = wood.getType();
            }
            model.addRow(rowData);
        }
    }

    private void getInventory() throws SQLException {
        FruitDAO fruitDAO = new FruitDAO();
        HerbDAO herbDAO = new HerbDAO();
        WoodDAO woodDAO = new WoodDAO();

        inventory.setProducts(fruitDAO.loadProductTable());
        inventory.setProducts(herbDAO.loadProductTable());
        inventory.setProducts(woodDAO.loadProductTable());
    }
}
