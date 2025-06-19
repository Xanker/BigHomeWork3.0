package ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mapper.FruitDAO;
import mapper.HerbDAO;
import mapper.WoodDAO;
import products.Fruit;
import products.Herb;
import products.Wood;
import products.Inventory;
import java.time.LocalDate;
public class ManagerUI extends JFrame {
    // 颜色定义
    protected static final Color HEADER_BG_COLOR = new Color(102, 51, 153); // 紫色表头
    protected static Color BUTTON_GREEN = new Color(76, 175, 80); // 新增按钮 - 绿色
    protected static Color BUTTON_BLUE = new Color(33, 150, 243); // 修改按钮 - 蓝色
    protected static final Color BUTTON_RED = new Color(244, 67, 54); // 删除按钮 - 红色
    protected static final Color BUTTON_YELLOW = new Color(255, 193, 7); // 刷新按钮 - 黄色
    protected static final Color BUTTON_BROWN = new Color(121, 85, 72); // 退出按钮 - 棕色
    protected static final Color BG_COLOR = new Color(245, 245, 245); // 背景色

    private JTabbedPane tabbedPane;
    private JTable fruitTable, herbTable, woodTable;
    private DefaultTableModel fruitModel, herbModel, woodModel;

    public ManagerUI() {
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

        JLabel titleLabel = new JLabel("商品管理系统");
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
            addFruitTab();
            addHerbTab();
            addWoodTab();
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
        refreshAllBtn.addActionListener(e -> refreshAllTables());
        bottomPanel.add(refreshAllBtn);

        JButton logoutBtn = createStyledButton("退出系统", BUTTON_BROWN, Color.WHITE);
        logoutBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        logoutBtn.addActionListener(e -> System.exit(0));
        bottomPanel.add(logoutBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // 设置水果标签为默认选中
        tabbedPane.setSelectedIndex(0);
    }

    private void addFruitTab() throws SQLException {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tabPanel.setBackground(Color.WHITE);

        // 表格标题
        JLabel tableTitle = new JLabel("水果列表");
        tableTitle.setFont(new Font("微软雅黑", Font.BOLD, 18));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        tableTitle.setForeground(new Color(70, 70, 70));
        tabPanel.add(tableTitle, BorderLayout.NORTH);

        // 创建水果表格
        String[] columns = {"ID", "名称", "颜色", "重量", "单位", "产地", "收获日期", "单价", "库存"};
        fruitModel = createTableModel("Fruit", columns);

        fruitTable = createStyledTable(fruitModel);
        JScrollPane scrollPane = new JScrollPane(fruitTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        // 操作按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton addBtn = createStyledButton("新增", BUTTON_GREEN, Color.WHITE);
        addBtn.addActionListener(e -> showAddDialog("水果"));

        JButton editBtn = createStyledButton("修改", BUTTON_BLUE, Color.WHITE);
        editBtn.addActionListener(e -> {
            try {
                showEditDialog("水果", fruitTable);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton deleteBtn = createStyledButton("删除", BUTTON_RED, Color.WHITE);
        deleteBtn.addActionListener(e -> deleteSelected("水果", fruitTable));

        JButton changeBtn = createStyledButton("修改",BUTTON_RED, Color.WHITE);
       // deleteBtn.addActionListener(e -> changeSelected("水果", fruitTable));
       // JButton refreshBtn = createStyledButton("刷新", BUTTON_YELLOW, Color.BLACK);
       // refreshBtn.addActionListener(e -> refreshTable("水果"));

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        //buttonPanel.add(refreshBtn);

        tabPanel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("水果", tabPanel);
    }

    private void addHerbTab() throws SQLException {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tabPanel.setBackground(Color.WHITE);

        // 表格标题
        JLabel tableTitle = new JLabel("草药列表");
        tableTitle.setFont(new Font("微软雅黑", Font.BOLD, 18));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        tableTitle.setForeground(new Color(70, 70, 70));
        tabPanel.add(tableTitle, BorderLayout.NORTH);

        // 创建草药表格
        String[] columns = {"ID", "名称", "产地", "采摘季节", "采摘月份", "性质", "收获日期", "单价", "库存"};
        herbModel = createTableModel("Herb", columns);

        herbTable = createStyledTable(herbModel);
        JScrollPane scrollPane = new JScrollPane(herbTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        // 操作按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton addBtn = createStyledButton("新增", BUTTON_GREEN, Color.WHITE);
        addBtn.addActionListener(e -> showAddDialog("草药"));

        JButton editBtn = createStyledButton("修改", BUTTON_BLUE, Color.WHITE);
        editBtn.addActionListener(e -> {
            try {
                showEditDialog("草药", herbTable);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton deleteBtn = createStyledButton("删除", BUTTON_RED, Color.WHITE);
        deleteBtn.addActionListener(e -> deleteSelected("草药", herbTable));

        JButton refreshBtn = createStyledButton("刷新", BUTTON_YELLOW, Color.BLACK);
        refreshBtn.addActionListener(e -> refreshTable("草药"));

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
       // buttonPanel.add(refreshBtn);

        tabPanel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("草药", tabPanel);
    }

    private void addWoodTab() throws SQLException {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tabPanel.setBackground(Color.WHITE);

        // 表格标题
        JLabel tableTitle = new JLabel("木材列表");
        tableTitle.setFont(new Font("微软雅黑", Font.BOLD, 18));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        tableTitle.setForeground(new Color(70, 70, 70));
        tabPanel.add(tableTitle, BorderLayout.NORTH);

        // 创建木材表格
        String[] columns = {"ID", "名称", "产地", "湿度", "长度", "宽度", "厚度", "日期", "单价", "库存"};
        woodModel = createTableModel("Wood", columns);

        woodTable = createStyledTable(woodModel);
        JScrollPane scrollPane = new JScrollPane(woodTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        // 操作按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton addBtn = createStyledButton("新增", BUTTON_GREEN, Color.WHITE);
        addBtn.addActionListener(e -> showAddDialog("木材"));

        JButton editBtn = createStyledButton("修改", BUTTON_BLUE, Color.WHITE);
        editBtn.addActionListener(e -> {
            try {
                showEditDialog("木材", woodTable);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton deleteBtn = createStyledButton("删除", BUTTON_RED, Color.WHITE);
        deleteBtn.addActionListener(e -> deleteSelected("木材", woodTable));

        JButton refreshBtn = createStyledButton("刷新", BUTTON_YELLOW, Color.BLACK);
        refreshBtn.addActionListener(e -> refreshTable("木材"));

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
       // buttonPanel.add(refreshBtn);

        tabPanel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("木材", tabPanel);
    }

    private DefaultTableModel createTableModel(String type, String[] columns) throws SQLException {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 根据商品类型直接访问对应DAO的专属方法
        switch (type) {
            case "Fruit":
                // 直接读取fruit表完整数据
                List<Fruit> fruits = new FruitDAO().getAllFruits();
                for (Fruit fruit : fruits) {
                    model.addRow(new Object[]{
                            fruit.getID(),
                            fruit.getName(),
                            fruit.getColor(),
                            fruit.getWeight(),
                            fruit.getUnit(),
                            fruit.getOrigin(),
                            fruit.getDate(),
                            fruit.getUnitPrice(),
                            fruit.getStock()
                    });
                }
                break;

            case "Herb":
                // 直接读取herb表完整数据
                List<Herb> herbs = new HerbDAO().getAllHerbs();
                for (Herb herb : herbs) {
                    model.addRow(new Object[]{
                            herb.getID(),
                            herb.getName(),
                            herb.getOrigin(),
                            herb.getSeason(),
                            herb.getMonth(),
                            herb.getProperty(),
                            herb.getPurchday(),
                            herb.getUnitPrice(),
                            herb.getStock()
                    });
                }
                break;

            case "Wood":
                // 直接读取wood表完整数据
                List<Wood> woods = new WoodDAO().getAllWoods();
                for (Wood wood : woods) {
                    model.addRow(new Object[]{
                            wood.getID(),
                            wood.getName(),
                            wood.getOrigin(),
                            wood.getWaterness(),
                            wood.getLength(),
                            wood.getWidth(),
                            wood.getThickness(),
                            wood.getDate(),
                            wood.getUnitPrice(),
                            wood.getStock()

                    });
                }
                break;
        }
        return model;
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

    // 从数据库获取产品数据
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

    // 刷新单个表格
    private void refreshTable(String type) {
        try {
            switch (type) {
                case "Fruit":
                    fruitModel.setRowCount(0);
                    ArrayList<?> fruitProducts = getProducts("Fruit");
                    addProductsToModel(fruitProducts, fruitModel, "Fruit");
                    break;
                case "Herb":
                    herbModel.setRowCount(0);
                    ArrayList<?> herbProducts = getProducts("Herb");
                    addProductsToModel(herbProducts, herbModel, "Herb");
                    break;
                case "Wood":
                    woodModel.setRowCount(0);
                    ArrayList<?> woodProducts = getProducts("Wood");
                    addProductsToModel(woodProducts, woodModel, "Wood");
                    break;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "刷新失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 添加产品到表格模型
    private void addProductsToModel(ArrayList<?> products, DefaultTableModel model, String type) {
        for (Object product : products) {
            Object[] rowData = new Object[model.getColumnCount()];
            if (type.equals("Fruit") && product instanceof Fruit) {
                Fruit fruit = (Fruit) product;
                rowData[0] = fruit.getID();
                rowData[1] = fruit.getName();
                rowData[2] = fruit.getColor();
                rowData[3] = fruit.getWeight();
                rowData[4] = fruit.getUnit();
                rowData[5] = fruit.getOrigin();
                rowData[6] = fruit.getDate();
                rowData[7] = fruit.getUnitPrice();
                rowData[8] = fruit.getStock();
            } else if (type.equals("Herb") && product instanceof Herb) {
                Herb herb = (Herb) product;
                rowData[0] = herb.getID();
                rowData[1] = herb.getName();
                rowData[2] = herb.getOrigin();
                rowData[3] = herb.getSeason();
                rowData[4] = herb.getMonth();
                rowData[5] = herb.getProperty();
                rowData[6] = herb.getPurchday();
                rowData[7] = herb.getUnitPrice();
                rowData[8] = herb.getStock();
            } else if (type.equals("Wood") && product instanceof Wood) {
                Wood wood = (Wood) product;
                rowData[0] = wood.getID();
                rowData[1] = wood.getName();
                rowData[2] = wood.getOrigin();
                rowData[3] = wood.getWaterness();
                rowData[4] = wood.getLength();
                rowData[5] = wood.getWidth();
                rowData[6] = wood.getThickness();
                rowData[7] = wood.getDate();
                rowData[8] = wood.getUnitPrice();
                rowData[9] = wood.getStock();
            }
            model.addRow(rowData);
        }
    }

    // 刷新所有表格
    private void refreshAllTables() {
        refreshTable("Fruit");
        refreshTable("Herb");
        refreshTable("Wood");
        JOptionPane.showMessageDialog(this, "所有数据已刷新!", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    // 以下是占位方法 - 在实际应用中需要实现这些功能
    private void showAddDialog(String type) {
        // 创建表单对话框
        JDialog dialog = new JDialog(this, "添加新" + type, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        // 主面板
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 公共字段（所有商品类型共有）
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField originField = new JTextField();
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField unitPriceField = new JTextField();
        JTextField stockField = new JTextField();

        // 添加公共字段
        formPanel.add(new JLabel("ID:")); ///0
        formPanel.add(idField);
        formPanel.add(new JLabel("名称:")); ///1
        formPanel.add(nameField);
        formPanel.add(new JLabel("产地:")); ///2
        formPanel.add(originField);
        formPanel.add(new JLabel("日期(YYYY-MM-DD):"));  ///3
        formPanel.add(dateField);
        formPanel.add(new JLabel("单价:")); ///4
        formPanel.add(unitPriceField);
        if(!type.equals("水果")) {
            formPanel.add(new JLabel("库存:"));
            formPanel.add(stockField);
        }


        // 特定类型字段
        switch (type) {
            case "水果":
                addFruitFields(formPanel);
                break;
            case "草药":
                addHerbFields(formPanel);
                break;
            case "木材":
                addWoodFields(formPanel);
                break;
        }

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveBtn = new JButton("保存");
        JButton cancelBtn = new JButton("取消");

        saveBtn.setBackground(BUTTON_GREEN);
        saveBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(BUTTON_RED);
        cancelBtn.setForeground(Color.WHITE);

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        // 添加事件处理
        saveBtn.addActionListener(e -> {
            try {
                saveNewProduct(type, formPanel);
                dialog.dispose();
                refreshTable(type);
                JOptionPane.showMessageDialog(this, type + "添加成功!", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "添加失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        // 添加到对话框
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void addFruitFields(JPanel panel) {
        panel.add(new JLabel("颜色:"));
        JTextField colorField = new JTextField();
        panel.add(colorField);

        panel.add(new JLabel("重量:"));
        JTextField weightField = new JTextField();
        panel.add(weightField);

        panel.add(new JLabel("单位:"));
        JTextField unitField = new JTextField();
        panel.add(unitField);

    }

    private void addHerbFields(JPanel panel) {
        panel.add(new JLabel("采摘季节:"));
        JTextField seasonField = new JTextField();
        panel.add(seasonField);

        //panel.add(new JLabel("采摘月份:"));
        //JTextField monthField = new JTextField();
        //panel.add(monthField);

        panel.add(new JLabel("性质:"));
        JTextField propertyField = new JTextField();
        panel.add(propertyField);
    }

    private void addWoodFields(JPanel panel) {
       // panel.add(new JLabel("木材类型:"));
       // JTextField woodTypeField = new JTextField();
        //panel.add(woodTypeField);

        //panel.add(new JLabel("湿度:"));
       // JTextField moistureField = new JTextField();
        //panel.add(moistureField);

        panel.add(new JLabel("长度:"));
        JTextField lengthField = new JTextField();
        panel.add(lengthField);

        panel.add(new JLabel("宽度:"));
        JTextField widthField = new JTextField();
        panel.add(widthField);

        panel.add(new JLabel("厚度:"));
        JTextField thicknessField = new JTextField();
        panel.add(thicknessField);
    }

    private void saveNewProduct(String type, JPanel formPanel) throws SQLException, NumberFormatException {
        // 收集表单数据
        Component[] components = formPanel.getComponents();
        java.util.List<JTextField> fields = new ArrayList<>();

        for (Component comp : components) {
            if (comp instanceof JTextField) {
                fields.add((JTextField) comp);
            }
        }

        // 创建商品对象
        switch (type) {
            case "水果":
                saveFruit(fields);
                break;
            case "草药":
                saveHerb(fields);
                break;
            case "木材":
                saveWood(fields);
                break;
        }
    }

    private void saveFruit(java.util.List<JTextField> fields) throws SQLException {
        // 字段顺序:
        // 0:ID, 1:名称, 2:产地, 3:日期, 4:单价,5
        // 6:颜色, 7:重量, 8:单位

        Fruit fruit = new Fruit(
                fields.get(1).getText(), // name
                Integer.parseInt(fields.get(0).getText()), // id
                fields.get(5).getText(), // color
                Double.parseDouble(fields.get(6).getText()), // weight
                fields.get(7).getText(), // unit
                fields.get(2).getText(), // origin
                LocalDate.parse(fields.get(3).getText()), // date
                Double.parseDouble(fields.get(4).getText()) // unitPrice
        );

        fruit.setType("Fruit");

        new FruitDAO().insert(fruit);
    }

    private void saveHerb(java.util.List<JTextField> fields) throws SQLException {
        // 字段顺序:
        // 0:ID, 1:名称, 2:产地, 3:日期, 4:单价, 5:库存,
        // 6:季节, 7:月份, 8:性质

        Herb herb = new Herb(
                fields.get(1).getText(), // name
                Integer.parseInt(fields.get(0).getText()), // id
                fields.get(2).getText(), // origin
                fields.get(6).getText(), // season
                fields.get(8).getText(), // property
                LocalDate.parse(fields.get(3).getText()), // date
                Double.parseDouble(fields.get(4).getText())  , // price
                Integer.parseInt(fields.get(5).getText())
        );

        herb.setType("Herb");

        new HerbDAO().insert(herb);
    }

    private void saveWood(java.util.List<JTextField> fields) throws SQLException {
        // 字段顺序:
        // 0:ID, 1:名称, 2:产地, 3:日期, 4:单价, 5:库存,
        //   6:长度, 7:宽度, 8:厚度

        Wood wood = new Wood(
                fields.get(1).getText(), // name
                Integer.parseInt(fields.get(0).getText()), // id
                Double.parseDouble(fields.get(4).getText()), // unitprice
                Double.parseDouble(fields.get(6).getText()) , // length
                Double.parseDouble(fields.get(7).getText()), // width
                Double.parseDouble(fields.get(8).getText()), // thickness
                LocalDate.parse(fields.get(3).getText()), // date
                fields.get(2).getText(), // origin
                Integer.parseInt(fields.get(5).getText()) // stock
        );
        wood.setType("Wood");

        new WoodDAO().insert(wood);
    }

    private void showEditDialog(String type, JTable table) throws SQLException {
        JDialog dialog = new JDialog(this, "添加新" + type, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要编辑的记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object id = table.getValueAt(selectedRow, 0);
        JOptionPane.showMessageDialog(this, "编辑" + type + " (ID: " + id + ")", "提示", JOptionPane.INFORMATION_MESSAGE);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


        JTextField nameField = new JTextField();
        JTextField originField = new JTextField();
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField unitPriceField = new JTextField();
        JTextField stockField = new JTextField();

        // 添加公共字段

        formPanel.add(new JLabel("名称:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("产地:"));
        formPanel.add(originField);
        formPanel.add(new JLabel("日期(YYYY-MM-DD):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("单价:"));
        formPanel.add(unitPriceField);
        if(!type.equals("水果")) {
            formPanel.add(new JLabel("库存:"));
            formPanel.add(stockField);
        }


        // 特定类型字段
        switch (type) {
            case "水果":
                addFruitFields(formPanel);
                break;
            case "草药":
                addHerbFields(formPanel);
                break;
            case "木材":
                addWoodFields(formPanel);
                break;
        }

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveBtn = new JButton("修改");
        JButton cancelBtn = new JButton("取消");

        saveBtn.setBackground(BUTTON_GREEN);
        saveBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(BUTTON_RED);
        cancelBtn.setForeground(Color.WHITE);

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        // 添加事件处理
        saveBtn.addActionListener(e -> {
            try {
                changeProduct(type, formPanel, table);
                dialog.dispose();
                refreshTable(type);
                JOptionPane.showMessageDialog(this, type + "修改成功!", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "添加失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        // 添加到对话框
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    private void changeProduct(String type,JPanel formPanel,JTable table) throws SQLException {
        Component[] components = formPanel.getComponents();
        java.util.List<JTextField> fields = new ArrayList<>();

        for (Component comp : components) {
            if (comp instanceof JTextField) {
                fields.add((JTextField) comp);
            }
        }

        // 创建商品对象
        switch (type) {
            case "水果":
                changeFruit(table,fields);
                break;
            case "草药":
                changeHerb(table,fields);
                break;
            case "木材":
                changeWood(table,fields);
                break;
        }
    }
    private void changeFruit(JTable a,java.util.List<JTextField> fields) throws SQLException {

       Inventory inventory = new Inventory();
       FruitDAO fruitDAO = new FruitDAO();
       inventory.setProducts(fruitDAO.loadProductTable());
       Fruit fruit = new Fruit();
       Object id = a.getValueAt(a.getSelectedRow(),0);
        int num = Integer.parseInt(id.toString());
       fruit = inventory.getFruit(num);

       fruit.setName(fields.get(0).getText());
       fruit.setOrigin(fields.get(1).getText());
       fruit.setDate(LocalDate.parse(fields.get(2).getText()));
       fruit.setUnitPrice(Double.parseDouble(fields.get(3).getText()));
       fruit.setColor(fields.get(4).getText());
       fruit.setWeight(Double.parseDouble(fields.get(5).getText()));
        inventory.UpdateCount( fruit, (int)fruit.getWeight());

       fruit.setUnit(fields.get(6).getText());
       fruitDAO.update(fruit);


    }
    private void changeHerb(JTable b,java.util.List<JTextField> fields) throws SQLException {
        Inventory inventory = new Inventory();
        HerbDAO herbDAO = new HerbDAO();
        inventory.setProducts(herbDAO.loadProductTable());
        Herb herb = new Herb();
        Object id = b.getValueAt(b.getSelectedRow(),0);
        int num = Integer.parseInt(id.toString());
        herb = inventory.getHerb(num);

        herb.setName(fields.get(0).getText());
        herb.setOrigin(fields.get(1).getText());
        herb.setDate(LocalDate.parse(fields.get(2).getText()));
        herb.setUnitPrice(Double.parseDouble(fields.get(3).getText()));
        herb.setStock(Integer.parseInt(fields.get(4).getText()));
        herb.setSeason(fields.get(5).getText());
        herb.setProperty(fields.get(6).getText());

        inventory.UpdateCount( herb, herb.getStock());


        herbDAO.update(herb);

    }
    private void changeWood(JTable c,java.util.List<JTextField> fields) throws SQLException {
        Inventory inventory = new Inventory();
        WoodDAO woodDAO = new WoodDAO();
        inventory.setProducts(woodDAO.loadProductTable());
        Wood wood = new Wood();
        Object id = c.getValueAt(c.getSelectedRow(),0);
        int num = Integer.parseInt(id.toString());
        wood = inventory.getWood(num);

        wood.setName(fields.get(0).getText());
        wood.setOrigin(fields.get(1).getText());
        wood.setDate(LocalDate.parse(fields.get(2).getText()));
        wood.setunitprice(Double.parseDouble(fields.get(3).getText()));
        wood.setUnitprice(Double.parseDouble(fields.get(3).getText()));
        wood.setStock(Integer.parseInt(fields.get(4).getText()));
        wood.setLength(Integer.parseInt(fields.get(5).getText()));
        wood.setWidth(Integer.parseInt(fields.get(6).getText()));
        wood.setThickness(Integer.parseInt(fields.get(7).getText()));


        inventory.UpdateCount( wood, wood.getStock());


        woodDAO.update(wood);
    }

    private void deleteSelected(String type, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object id1 = table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要删除选中的" + " ( " + type + ")" + "记录吗?",
                "确认删除",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            Object id = table.getValueAt(selectedRow, 0);
            try {
                boolean success = false;
                switch (type) {
                    case "水果":
                        success = new FruitDAO().delete(Integer.parseInt(id.toString()));
                        break;
                    case "草药":
                        success = new HerbDAO().delete(Integer.parseInt(id.toString()));
                        break;
                    case "木材":
                        success = new WoodDAO().delete(Integer.parseInt(id.toString()));
                        break;
                }

                if (success) {
                    refreshTable(type);
                    JOptionPane.showMessageDialog(this, type + "记录删除成功!", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "删除失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 启动管理员界面
    public static void showManagerUI() {
        SwingUtilities.invokeLater(() -> {
            ManagerUI managerUI = new ManagerUI();
            managerUI.setVisible(true);
        });
    }

    public static void main(String[] args) {
        showManagerUI();
    }
}