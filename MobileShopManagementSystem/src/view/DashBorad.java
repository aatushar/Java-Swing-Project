/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.DbCon1;

/**
 *
 * @author user
 */
public class DashBorad extends javax.swing.JFrame {

    DbCon1 con = new DbCon1();
    PreparedStatement ps;
    String sql = "";
    ResultSet rs;

    LocalDate currentDate = LocalDate.now();
    java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);

    String[] productColumns = {"Product ID", "Product Name", "Product catagory", "Product code"};
    String[] purchaseColumns = {"Purchase ID", "Product Name", "UnitPrice", "Quantity", "Total Price", "Date"};

    public void getCardTable() {

        String[] columns = {"Product Name", "Unit Price", "Quantity", "Total Price", "Discount", "Actual Price", "Due Amount","Date", "CustomerName"};

        DefaultTableModel cardAdd = new DefaultTableModel();
        cardAdd.setColumnIdentifiers(columns);
        tblSales.setModel(cardAdd);
    }

    public void todaySalesReport() {

        sql = "select sum(actualPrice) from sales where Date=?";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setDate(1, sqlDate);

            rs = ps.executeQuery();

            while (rs.next()) {
                Float totalPrice = rs.getFloat("sum(actualPrice)");
                lblTodaySales.setText(totalPrice + "");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void monthlySalesReport() {

//        System.out.println(sqlDate.toString().substring(0, 8));
        sql = "select sum(actualPrice) from sales where Date like ?";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, sqlDate.toString().substring(0, 8) + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                Float totalPrice = rs.getFloat("sum(actualPrice)");
                lblTotalSales.setText(totalPrice + "");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void todayPurchaseReport() {

        sql = "select sum(totalPrice) from purchase where date=?";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setDate(1, sqlDate);

            rs = ps.executeQuery();

            while (rs.next()) {
                Float totalPrice = rs.getFloat("sum(totalPrice)");
                lblTodayPurchase.setText(totalPrice + "");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void totalPurchaseReport() {

        sql = "select sum(totalPrice) from purchase";
        try {
            ps = con.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                Float totalPrice = rs.getFloat("sum(totalPrice)");
                lblTotalPurchase.setText(totalPrice + "");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateStockSales() {

        sql = "update stock set quantity=quantity-? where pName= ?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setFloat(1, Float.parseFloat(txtSaleQuantity.getText().trim()));
            ps.setString(2, txtSalesProductName.getSelectedItem().toString());

            ps.executeUpdate();

            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateStockPurchase() {

        sql = "update stock set quantity=quantity+? where pName= ?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setFloat(1, Float.parseFloat(txtPurchaseQuantity.getText().trim()));
            ps.setString(2, txtPurchaseProductCombo.getSelectedItem().toString());

            ps.executeUpdate();

            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addProductToStock() {
        sql = "insert into stock(pName, quantity) values(?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtProductName.getText().trim());
            ps.setFloat(2, 0.0f);

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {

            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getProductNameToCombo() {
        sql = "select name from product";
        txtPurchaseProductCombo.removeAllItems();
        txtSalesProductName.removeAllItems();

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");

                txtPurchaseProductCombo.addItem(name);
                txtSalesProductName.addItem(name);
            }
            rs.close();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getAllProduct() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(productColumns);

        tableProduct.setModel(model);
        sql = "select * from product";

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("purchaseId");
                String name = rs.getString("name");
                String catagory = rs.getString("catagory");
                String code = rs.getString("productCode");

                model.addRow(new Object[]{id, name, catagory, code});
            }
            rs.close();
            ps.close();
            con.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getAllPurchase() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(purchaseColumns);

        tblPurchase.setModel(model);
        sql = "select * from purchase";

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("purchaseId");
                String name = rs.getString("name");
                float quantity = rs.getFloat("quantity");
                float unitprice = rs.getFloat("unitPrice");
                float totalPrice = rs.getFloat("totalPrice");
                java.util.Date date = rs.getDate("date");

                model.addRow(new Object[]{id, name, unitprice, quantity, totalPrice, date});
            }
            rs.close();
            ps.close();
            con.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Creates new form DashBorad
     */
    public DashBorad() {
        initComponents();
        getAllProduct();
        getProductNameToCombo();
        getAllPurchase();
        todaySalesReport();
        monthlySalesReport();
        todayPurchaseReport();
        totalPurchaseReport();
        getCardTable();


    }

    public void reset() {
        txtProductId.setText(null);
        txtProductName.setText(null);
        txtProductCatagory.getSelectedItem().toString();
        txtProductCode.setText(null);
    }
    
  public void resetSales(){
//      txtSalesProductName.getSelectedIndex(0);
      txtSaleUnitPrice.setText(null);
      txtSaleQuantity.setText(null);
      txtSalesDiscount.setText(null);
      txtSalesActualPrice.setText(null);
      txtSalesCustomerName.setText(null);
      dateSalesDate.setDate(null);
      txtSalesTotalPrice.setText(null);
      txtSalesCashReceived.setText(null);
      txtSalesDueAmount.setText(null);
  }
//calculate total price

    public float getTotalPrice() {
        float unitPrice = Float.parseFloat(txtSaleUnitPrice.getText().trim());
        float quanitiy = Float.parseFloat(txtSaleQuantity.getText().trim());

        float totalPrice = unitPrice * quanitiy;

        return totalPrice;
    }
    public float getTotalPricePurchase() {
        float unitPrice = Float.parseFloat(txtPurchaseUnitPrice.getText().trim());
        float quanitiy = Float.parseFloat(txtPurchaseQuantity.getText().trim());

        float totalPrice = unitPrice * quanitiy;

        return totalPrice;
    }

    public float getActualPrice() {
        float discount = 0.0f;
        float totalPrice = getTotalPrice();
        String discountStr = txtSalesDiscount.getText().trim();

        discount = Float.parseFloat(discountStr);
        float discountamount = totalPrice * discount / 100;

        float actualPrice = totalPrice - discountamount;

        return actualPrice;
    }

    public java.sql.Date convertUtilDateToSqlDate(java.sql.Date utilDate) {
        if (utilDate != null) {
            return new java.sql.Date(utilDate.getTime());
        }
        return null;
    }
    
//    //sql to utill
//    public java.util.Date conversqlDateToutillDate(java.sql.Date sqlDate) {
//        if (sqlDate != null) {
//            return new java.util.Date(sqlDate.getTime());
//        }
//        return null;
//    }

    public float getDiscountAmount() {

        return getTotalPrice() - getActualPrice();
    }

    public java.sql.Date convertUtilDateToSqlDate(java.util.Date utilDate) {
        if (utilDate != null) {
            return new java.sql.Date(utilDate.getTime());
        }

        return null;
    }
////      public java.util.Date convertIntoUtilDate(String Date){
//          SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
//        
//        try {
//            return dateformat.parse(Date);
//        } catch (ParseException ex) {
//            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        btnSalse = new javax.swing.JButton();
        btnPurchase = new javax.swing.JButton();
        btnWarrenty = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        btnProduct = new javax.swing.JButton();
        btnCustomer = new javax.swing.JButton();
        btnStock = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        menu = new javax.swing.JTabbedPane();
        home = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        lblTodaySales = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        lblTodayPurchase = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        lblTodayDue = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        lblTo = new javax.swing.JLabel();
        lblTotalPurchase = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        lblTotalSales = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        lblTotalDue = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        sales = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        txtSaleUnitPrice = new javax.swing.JTextField();
        txtSaleQuantity = new javax.swing.JTextField();
        txtSalesTotalPrice = new javax.swing.JTextField();
        txtSalesDiscount = new javax.swing.JTextField();
        txtSalesCashReceived = new javax.swing.JTextField();
        txtSalesDueAmount = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        btnSalesSubmit = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        dateSalesDate = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        txtSalesActualPrice = new javax.swing.JTextField();
        txtSalesProductName = new javax.swing.JComboBox<>();
        txtSalesCustomerName = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblSales = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        card = new javax.swing.JTextArea();
        jButton6 = new javax.swing.JButton();
        purchase = new javax.swing.JTabbedPane();
        btnPurchaseDelate = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtPurchaseProductId = new javax.swing.JTextField();
        txtPurchaseUnitPrice = new javax.swing.JTextField();
        txtPurchaseQuantity = new javax.swing.JTextField();
        txtPurchaseTotalPrice = new javax.swing.JTextField();
        txtPurchaseDate = new com.toedter.calendar.JDateChooser();
        btnPurchaseSave = new javax.swing.JButton();
        btnPurchaseUpdate = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        btnPurchaseReset = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPurchase = new javax.swing.JTable();
        txtPurchaseProductCombo = new javax.swing.JComboBox<>();
        product = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtProductId = new javax.swing.JTextField();
        txtProductCode = new javax.swing.JTextField();
        btnPsave = new javax.swing.JButton();
        btnProductUpdate = new javax.swing.JButton();
        btnPdelate = new javax.swing.JButton();
        btnRreset = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableProduct = new javax.swing.JTable();
        txtProductName = new javax.swing.JTextField();
        txtProductCatagory = new javax.swing.JComboBox<>();
        report = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        radioReportPurchase = new javax.swing.JRadioButton();
        radioReportSales = new javax.swing.JRadioButton();
        radioReportStock = new javax.swing.JRadioButton();
        btnRadioView = new javax.swing.JButton();
        dateReportFrom = new com.toedter.calendar.JDateChooser();
        dateReportTo = new com.toedter.calendar.JDateChooser();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblReport = new javax.swing.JTable();
        customer = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        txtContractNo = new javax.swing.JTextField();
        txtCustomerEmail = new javax.swing.JTextField();
        btnCustomerReg = new javax.swing.JButton();
        btnCustomerReset = new javax.swing.JButton();
        radioGenderMale = new javax.swing.JRadioButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtCustomerAddress = new javax.swing.JTextArea();
        radioGenderFemale = new javax.swing.JRadioButton();
        warranty = new javax.swing.JTabbedPane();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        warentyDate = new javax.swing.JTextField();
        salesdateWarenty = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        btnWarentyFind = new javax.swing.JButton();
        txtWarentyId = new javax.swing.JTextField();
        Stock = new javax.swing.JTabbedPane();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblStock = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setBackground(java.awt.Color.pink);
        jLabel1.setFont(new java.awt.Font("Showcard Gothic", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TTM GADGETS & SERVICES");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Asset/Icon/house.png"))); // NOI18N
        btnHome.setText("Home");
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHomeMouseExited(evt);
            }
        });
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });
        jPanel1.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 140, 40));

        btnSalse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Asset/Icon/sales.png"))); // NOI18N
        btnSalse.setText("Sales");
        btnSalse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalseMouseExited(evt);
            }
        });
        btnSalse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalseActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalse, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 140, 40));

        btnPurchase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Asset/Icon/purchase.png"))); // NOI18N
        btnPurchase.setText("Purchase");
        btnPurchase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPurchaseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPurchaseMouseExited(evt);
            }
        });
        btnPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseActionPerformed(evt);
            }
        });
        jPanel1.add(btnPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 140, 40));

        btnWarrenty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Asset/Icon/in-stock.png"))); // NOI18N
        btnWarrenty.setText("Stock");
        btnWarrenty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnWarrentyMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnWarrentyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnWarrentyMouseExited(evt);
            }
        });
        btnWarrenty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWarrentyActionPerformed(evt);
            }
        });
        jPanel1.add(btnWarrenty, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 140, 40));

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Asset/Icon/report.png"))); // NOI18N
        btnReport.setText("Report");
        btnReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnReportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnReportMouseExited(evt);
            }
        });
        jPanel1.add(btnReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 140, 40));

        btnProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Asset/Icon/device.png"))); // NOI18N
        btnProduct.setText("Gadget");
        btnProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnProductMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnProductMouseExited(evt);
            }
        });
        btnProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductActionPerformed(evt);
            }
        });
        jPanel1.add(btnProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 140, 40));

        btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Asset/Icon/customer (1).png"))); // NOI18N
        btnCustomer.setText("Customers");
        btnCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCustomerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCustomerMouseExited(evt);
            }
        });
        btnCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerActionPerformed(evt);
            }
        });
        jPanel1.add(btnCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 140, 40));

        btnStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Asset/Icon/money.png"))); // NOI18N
        btnStock.setText("Warranty");
        btnStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnStockMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnStockMouseExited(evt);
            }
        });
        btnStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockActionPerformed(evt);
            }
        });
        jPanel1.add(btnStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 140, 40));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        home.setPreferredSize(new java.awt.Dimension(500, 250));

        jPanel4.setBackground(new java.awt.Color(0, 153, 153));
        jPanel4.setFont(new java.awt.Font("Goudy Stout", 0, 12)); // NOI18N
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Goudy Stout", 0, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Home");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 150));

        jPanel19.setMinimumSize(new java.awt.Dimension(800, 400));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel20.setBackground(new java.awt.Color(204, 204, 255));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Today Sales");
        jPanel20.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 70));

        lblTodaySales.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTodaySales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTodaySales.setText("0.00");
        jPanel20.add(lblTodaySales, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 80));

        jPanel19.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 230, 150));

        jPanel21.setBackground(new java.awt.Color(204, 204, 255));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTodayPurchase.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTodayPurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTodayPurchase.setText("0.00");
        jPanel21.add(lblTodayPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 80));

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Today Purchase");
        jPanel21.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 70));

        jPanel19.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 230, 150));

        jPanel22.setBackground(new java.awt.Color(204, 204, 255));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Today Due");
        jPanel22.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 70));

        lblTodayDue.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTodayDue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTodayDue.setText("0.00");
        jPanel22.add(lblTodayDue, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 80));

        jPanel19.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 60, 230, 150));

        jPanel23.setBackground(new java.awt.Color(204, 204, 255));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTo.setText("Total Purchase");
        jPanel23.add(lblTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 70));

        lblTotalPurchase.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTotalPurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalPurchase.setText("0.00");
        jPanel23.add(lblTotalPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 80));

        jPanel19.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 230, 150));

        jPanel24.setBackground(new java.awt.Color(204, 204, 255));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("Total Sales");
        jPanel24.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 70));

        lblTotalSales.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTotalSales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalSales.setText("0.00");
        jPanel24.add(lblTotalSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 80));

        jPanel19.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 250, 230, 150));

        jPanel25.setBackground(new java.awt.Color(204, 204, 255));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalDue.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTotalDue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalDue.setText("0.00");
        jPanel25.add(lblTotalDue, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 80));

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Total Due");
        jPanel25.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 70));

        jPanel19.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, 230, 150));

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeLayout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                .addContainerGap())
        );

        menu.addTab("tab1", home);

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(0, 153, 153));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Goudy Stout", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Asset/Image/discount (3).png"))); // NOI18N
        jLabel3.setText("SalES");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1220, Short.MAX_VALUE)
                .addGap(54, 54, 54))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, -1));

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 360));

        jPanel6.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 1270, 510));

        jLabel4.setText("PID");

        jLabel5.setText("Product Name");

        jLabel6.setText("Unit Price");

        jLabel7.setText("Quantity");

        jLabel8.setText("Total Price");

        jLabel9.setText("Discount");

        jLabel10.setText("Cash Recived");

        jLabel11.setText("Due Amount");

        jLabel12.setText("Date");

        txtSaleUnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSaleUnitPriceFocusLost(evt);
            }
        });

        txtSaleQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSaleQuantityFocusLost(evt);
            }
        });

        txtSalesDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSalesDiscountFocusGained(evt);
            }
        });
        txtSalesDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalesDiscountActionPerformed(evt);
            }
        });
        txtSalesDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSalesDiscountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSalesDiscountKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSalesDiscountKeyTyped(evt);
            }
        });

        txtSalesCashReceived.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesCashReceivedFocusLost(evt);
            }
        });

        jLabel13.setText("Customer Name");

        btnSalesSubmit.setBackground(new java.awt.Color(0, 153, 153));
        btnSalesSubmit.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        btnSalesSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnSalesSubmit.setText("Submit");
        btnSalesSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalesSubmitMouseClicked(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 153, 153));
        jButton2.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Reset");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jLabel14.setText("Actual Price");

        txtSalesActualPrice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSalesActualPriceMouseClicked(evt);
            }
        });

        tblSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tblSales);

        jButton1.setBackground(new java.awt.Color(0, 153, 153));
        jButton1.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add to Cart");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        card.setColumns(20);
        card.setRows(5);
        jScrollPane6.setViewportView(card);

        jButton6.setBackground(new java.awt.Color(0, 153, 153));
        jButton6.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Print");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnSalesSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(30, 30, 30)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel14)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSalesProductName, 0, 173, Short.MAX_VALUE)
                            .addComponent(jTextField1)
                            .addComponent(txtSaleUnitPrice)
                            .addComponent(txtSaleQuantity)
                            .addComponent(txtSalesTotalPrice)
                            .addComponent(txtSalesDiscount)
                            .addComponent(txtSalesActualPrice)
                            .addComponent(txtSalesCashReceived)
                            .addComponent(txtSalesDueAmount))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtSalesCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(92, 92, 92)
                                        .addComponent(dateSalesDate, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtSaleUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(txtSaleQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addGap(22, 22, 22)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(txtSalesActualPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(txtSalesCashReceived, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(txtSalesDueAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(txtSalesTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSalesDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6)
                            .addComponent(btnSalesSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2)
                            .addComponent(jButton1))
                        .addGap(23, 23, 23))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12))
                                    .addComponent(dateSalesDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(txtSalesProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel13)
                                        .addComponent(txtSalesCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(48, 48, 48)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane6))
                        .addContainerGap())))
        );

        sales.addTab("tab1", jPanel5);

        menu.addTab("tab2", sales);

        btnPurchaseDelate.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(0, 153, 153));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Goudy Stout", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Purchase");
        jPanel13.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1190, 100));

        btnPurchaseDelate.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 110));

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        btnPurchaseDelate.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 106, -1, -1));

        jLabel16.setText("Unit Price");
        btnPurchaseDelate.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        jLabel17.setText("Product ID");
        btnPurchaseDelate.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        jLabel18.setText("Quantity");
        btnPurchaseDelate.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        jLabel19.setText("Product Name");
        btnPurchaseDelate.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel20.setText("Total Price");
        btnPurchaseDelate.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        jLabel21.setText("Date");
        btnPurchaseDelate.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, -1, 20));
        btnPurchaseDelate.add(txtPurchaseProductId, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, 150, -1));

        txtPurchaseUnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseUnitPriceFocusLost(evt);
            }
        });
        btnPurchaseDelate.add(txtPurchaseUnitPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 150, -1));

        txtPurchaseQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseQuantityFocusLost(evt);
            }
        });
        btnPurchaseDelate.add(txtPurchaseQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, 150, -1));
        btnPurchaseDelate.add(txtPurchaseTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, 150, -1));
        btnPurchaseDelate.add(txtPurchaseDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 330, 150, -1));

        btnPurchaseSave.setBackground(new java.awt.Color(0, 153, 153));
        btnPurchaseSave.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnPurchaseSave.setForeground(new java.awt.Color(255, 255, 255));
        btnPurchaseSave.setText("Save");
        btnPurchaseSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseSaveMouseClicked(evt);
            }
        });
        btnPurchaseDelate.add(btnPurchaseSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, -1, -1));

        btnPurchaseUpdate.setBackground(new java.awt.Color(0, 153, 153));
        btnPurchaseUpdate.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnPurchaseUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnPurchaseUpdate.setText("Update");
        btnPurchaseUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseUpdateMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPurchaseUpdateMouseEntered(evt);
            }
        });
        btnPurchaseDelate.add(btnPurchaseUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 440, -1, -1));

        jButton5.setBackground(new java.awt.Color(0, 153, 153));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Delate");
        btnPurchaseDelate.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 440, -1, -1));

        btnPurchaseReset.setBackground(new java.awt.Color(0, 153, 153));
        btnPurchaseReset.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnPurchaseReset.setForeground(new java.awt.Color(255, 255, 255));
        btnPurchaseReset.setText("Reset");
        btnPurchaseDelate.add(btnPurchaseReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 440, -1, -1));

        jScrollPane1.setForeground(new java.awt.Color(0, 153, 153));

        tblPurchase.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblPurchase);

        btnPurchaseDelate.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 130, 480, 240));

        txtPurchaseProductCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HP", "ASOS", "Lenevo", "Dell", " " }));
        txtPurchaseProductCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPurchaseProductComboActionPerformed(evt);
            }
        });
        btnPurchaseDelate.add(txtPurchaseProductCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, 150, -1));

        purchase.addTab("tab1", btnPurchaseDelate);

        menu.addTab("tab3", purchase);

        jPanel14.setBackground(new java.awt.Color(0, 153, 153));

        jLabel22.setBackground(new java.awt.Color(204, 204, 204));
        jLabel22.setFont(new java.awt.Font("Goudy Stout", 2, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Gadget");

        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel23.setText("Product ID");

        jLabel24.setText("Product Name");

        jLabel25.setText("Catagory");

        jLabel26.setText("Product Code");

        btnPsave.setText("Save");
        btnPsave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPsaveMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPsaveMouseEntered(evt);
            }
        });

        btnProductUpdate.setText("Update");
        btnProductUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductUpdateMouseClicked(evt);
            }
        });

        btnPdelate.setText("Delate ");
        btnPdelate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPdelateMouseClicked(evt);
            }
        });

        btnRreset.setText("Reset");
        btnRreset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRresetMouseClicked(evt);
            }
        });

        tableProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProductMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableProduct);

        txtProductName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductNameActionPerformed(evt);
            }
        });

        txtProductCatagory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Smartphone", "SmartWatch", "Headphone", "Charger", "Power Bank" }));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel23)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtProductId, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(txtProductCode)
                            .addComponent(txtProductName)
                            .addComponent(txtProductCatagory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPsave)
                            .addComponent(btnPdelate))
                        .addGap(71, 71, 71)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRreset)
                            .addComponent(btnProductUpdate))))
                .addGap(54, 54, 54)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(591, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtProductCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(74, 74, 74)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPsave)
                            .addComponent(btnProductUpdate))
                        .addGap(64, 64, 64)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPdelate)
                            .addComponent(btnRreset)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 66, Short.MAX_VALUE))
        );

        product.addTab("product", jPanel11);

        menu.addTab("tab4", product);

        jPanel17.setBackground(new java.awt.Color(0, 153, 153));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setFont(new java.awt.Font("Goudy Stout", 1, 24)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Report");
        jPanel17.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 1270, 100));

        jLabel28.setText("From");

        jLabel29.setText("To");

        buttonGroup1.add(radioReportPurchase);
        radioReportPurchase.setText("Purchase");

        buttonGroup1.add(radioReportSales);
        radioReportSales.setText("Sales");

        buttonGroup1.add(radioReportStock);
        radioReportStock.setText("Stock");

        btnRadioView.setText("View");
        btnRadioView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRadioViewMouseClicked(evt);
            }
        });

        tblReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblReport);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioReportPurchase)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGap(119, 119, 119)
                                .addComponent(radioReportSales)
                                .addGap(47, 47, 47)
                                .addComponent(radioReportStock)))
                        .addGap(35, 35, 35)
                        .addComponent(btnRadioView)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(40, 40, 40)
                        .addComponent(dateReportFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(92, 92, 92)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dateReportTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 657, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel28)
                            .addComponent(dateReportFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateReportTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(94, 94, 94)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radioReportPurchase)
                            .addComponent(radioReportSales)
                            .addComponent(radioReportStock)
                            .addComponent(btnRadioView)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(81, 81, 81))
        );

        report.addTab("tab1", jPanel16);

        menu.addTab("tab5", report);

        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel26.setBackground(new java.awt.Color(0, 153, 153));

        jLabel30.setFont(new java.awt.Font("Britannic Bold", 0, 24)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Customer Regestration");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 1274, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel18.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, -1));

        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel32.setText("Name");
        jPanel27.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, -1, -1));

        jLabel34.setText("Contact No");
        jPanel27.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, -1, -1));

        jLabel36.setText("Email");
        jPanel27.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 140, -1, -1));

        jLabel38.setText("Address");
        jPanel27.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 210, -1, -1));

        jLabel40.setText("Gender");
        jPanel27.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 330, -1, -1));
        jPanel27.add(txtCustomerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 40, 220, -1));
        jPanel27.add(txtContractNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 90, 220, -1));
        jPanel27.add(txtCustomerEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 140, 220, -1));

        btnCustomerReg.setText("Save");
        btnCustomerReg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCustomerRegMouseClicked(evt);
            }
        });
        jPanel27.add(btnCustomerReg, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 400, -1, -1));

        btnCustomerReset.setText("Reset");
        jPanel27.add(btnCustomerReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 400, -1, -1));

        buttonGroup3.add(radioGenderMale);
        radioGenderMale.setText("Male");
        jPanel27.add(radioGenderMale, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 330, -1, -1));

        txtCustomerAddress.setColumns(20);
        txtCustomerAddress.setRows(5);
        jScrollPane5.setViewportView(txtCustomerAddress);

        jPanel27.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 180, -1, -1));

        buttonGroup3.add(radioGenderFemale);
        radioGenderFemale.setText("Female");
        jPanel27.add(radioGenderFemale, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 330, -1, -1));

        jPanel18.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 1280, 490));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        customer.addTab("tab1", jPanel12);

        menu.addTab("tab6", customer);

        jPanel29.setBackground(new java.awt.Color(0, 153, 153));

        jLabel43.setFont(new java.awt.Font("Showcard Gothic", 1, 24)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("Warranty Check");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel44.setText("Sales Date");

        jLabel45.setText("Warranty Available");

        jLabel47.setText("Invoice Number");

        btnWarentyFind.setBackground(new java.awt.Color(0, 153, 153));
        btnWarentyFind.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnWarentyFind.setForeground(new java.awt.Color(255, 255, 255));
        btnWarentyFind.setText("Find");
        btnWarentyFind.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnWarentyFindMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel44)
                    .addComponent(jLabel45)
                    .addComponent(jLabel47))
                .addGap(66, 66, 66)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(warentyDate, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(salesdateWarenty)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                        .addComponent(txtWarentyId)
                        .addGap(18, 18, 18)
                        .addComponent(btnWarentyFind)))
                .addGap(608, 608, 608)
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel47)
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnWarentyFind)
                        .addComponent(txtWarentyId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(54, 54, 54)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel44)
                    .addComponent(salesdateWarenty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(warentyDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        warranty.addTab("tab1", jPanel28);

        menu.addTab("tab7", warranty);

        jPanel32.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel33.setBackground(new java.awt.Color(0, 153, 153));

        jLabel42.setBackground(new java.awt.Color(255, 255, 255));
        jLabel42.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Stock");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, 1280, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jPanel32.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, -1));

        tblStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStockMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblStockMouseEntered(evt);
            }
        });
        jScrollPane7.setViewportView(tblStock);

        jPanel32.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 120, 670, -1));

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Stock.addTab("tab1", jPanel31);

        menu.addTab("tab8", Stock);

        jPanel2.add(menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(-8, -80, 1280, 670));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(0);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnSalseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalseActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(1);
    }//GEN-LAST:event_btnSalseActionPerformed

    private void btnPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(2);
    }//GEN-LAST:event_btnPurchaseActionPerformed

    private void btnWarrentyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWarrentyActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(7);
        
    }//GEN-LAST:event_btnWarrentyActionPerformed

    private void btnHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseEntered
        btnHome.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnHomeMouseEntered

    private void btnSalseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalseMouseEntered
        // TODO add your handling code here:
        btnSalse.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnSalseMouseEntered

    private void btnPurchaseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseMouseEntered
        // TODO add your handling code here:
        btnPurchase.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnPurchaseMouseEntered

    private void btnWarrentyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnWarrentyMouseEntered
        btnWarrenty.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnWarrentyMouseEntered

    private void btnHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseExited
        // TODO add your handling code here:
        btnHome.setBackground(getBackground());
    }//GEN-LAST:event_btnHomeMouseExited

    private void btnSalseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalseMouseExited
        // TODO add your handling code here:
        btnSalse.setBackground(getBackground());
    }//GEN-LAST:event_btnSalseMouseExited

    private void btnPurchaseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseMouseExited
        // TODO add your handling code here:
        btnPurchase.setBackground(getBackground());
    }//GEN-LAST:event_btnPurchaseMouseExited

    private void btnWarrentyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnWarrentyMouseExited

        btnWarrenty.setBackground(getBackground());
    }//GEN-LAST:event_btnWarrentyMouseExited

    private void txtSalesDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalesDiscountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalesDiscountActionPerformed

    private void txtSaleUnitPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSaleUnitPriceFocusLost
        // TODO add your handling code here:
        try {
            if (!txtSaleUnitPrice.getText().trim().isEmpty()) {

            } else {
                JOptionPane.showMessageDialog(rootPane, "Unit price is empty");
                txtSaleUnitPrice.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error" + e.getMessage());

        }
    }//GEN-LAST:event_txtSaleUnitPriceFocusLost

    private void txtSaleQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSaleQuantityFocusLost
        // TODO add your handling code here:
        try {
            if (txtSaleUnitPrice.getText().trim().isEmpty()) {
                txtSaleUnitPrice.requestFocus();
            } else if (!txtSaleQuantity.getText().trim().isEmpty()) {
                txtSalesTotalPrice.setText(getTotalPrice() + "");
            } else {
                JOptionPane.showMessageDialog(rootPane, "Quantity is empty");
                txtSaleQuantity.requestFocus();
            }
        } catch (Exception f) {
            JOptionPane.showMessageDialog(rootPane, "Error" + f.getMessage());
        }
    }//GEN-LAST:event_txtSaleQuantityFocusLost

    private void txtSalesDiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalesDiscountKeyPressed
        // TODO add your handling code here:
        txtSalesActualPrice.setText(getActualPrice() + "");

    }//GEN-LAST:event_txtSalesDiscountKeyPressed

    private void txtSalesDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalesDiscountKeyReleased
        // TODO add your handling code here:
//        txtSalesActualPrice.setText(getActualPrice() + "");
    }//GEN-LAST:event_txtSalesDiscountKeyReleased

    
   // stock method create Seles add contain to stock
    public void getAllStockToSeles() {
        sql = "update stock set quantity = quantity - ? where pName=?";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setFloat(1, Float.parseFloat(txtSaleQuantity.getText()));
            
            ps.setString(2, txtSalesProductName.getSelectedItem().toString());
            
            ps.executeUpdate();
            ps.close();
            con.getCon().close();

      //      JOptionPane.showMessageDialog(prCompanyName, "Stock Data is  Decreament");

        } catch (SQLException ex) {
      //      JOptionPane.showMessageDialog(prCompanyName, "Stock Data is Not Decreament");
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    
    
    private void btnSalesSubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesSubmitMouseClicked
//         TODO add your handling code here:
    int rowindex=tblSales.getModel().getRowCount();
        for (int i = 0; i < rowindex; i++) {
            sql = "insert into sales(name, unitPrice, quantity, totalPrice, discount,"
                + " actualPrice, cashRecived, dueAmount, date,warrent_date, customerName)"
                + "  values(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, tblSales.getModel().getValueAt(i, 0).toString());
            ps.setFloat(2, Float.parseFloat(tblSales.getModel().getValueAt(i, 1).toString()));
            ps.setFloat(3, Float.parseFloat(tblSales.getModel().getValueAt(i, 2).toString()));
            ps.setFloat(4, Float.parseFloat(tblSales.getModel().getValueAt(i, 3).toString()));
            ps.setFloat(5, Float.parseFloat(tblSales.getModel().getValueAt(i, 4).toString()));

            ps.setFloat(6, getActualPrice());
            ps.setFloat(7, Float.parseFloat(tblSales.getModel().getValueAt(i, 5).toString()));
            ps.setFloat(8, Float.parseFloat(tblSales.getModel().getValueAt(i, 6).toString()));

            ps.setDate(9, convertUtilDateToSqlDate(dateSalesDate.getDate()));
//            ps.setString(10, tblSales.getModel().getValueAt(i, 7).toString());
            
            // Start 
            Date originalDate=dateSalesDate.getDate();
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(originalDate);
            
            calendar.add(Calendar.YEAR, 2);
            Date updateDate=calendar.getTime();
            
            //End
            
            ps.setDate(10, convertUtilDateToSqlDate(updateDate));
            
            ps.setString(11, tblSales.getModel().getValueAt(i, 8).toString());

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

           
            updateStockSales();
            
            getAllStockToSeles();
       //     System.out.println("row "+ rowindex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Data not submit");
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
         JOptionPane.showMessageDialog(rootPane, "Data submited");
//        sql = "insert into sales(name, unitPrice, quantity, totalPrice, discount,"
//                + " actualPrice, cashRecived, dueAmount, date, customerName)"
//                + "  values(?,?,?,?,?,?,?,?,?,?)";
//        try {
//            ps = con.getCon().prepareStatement(sql);
//
//            ps.setString(1, txtSalesProductName.getSelectedItem().toString());
//            ps.setFloat(2, Float.parseFloat(txtSaleUnitPrice.getText().trim()));
//            ps.setFloat(3, Float.parseFloat(txtSaleQuantity.getText().trim()));
//            ps.setFloat(4, Float.parseFloat(txtSalesTotalPrice.getText().trim()));
//            ps.setFloat(5, getDiscountAmount());
//
//            ps.setFloat(6, getActualPrice());
//            ps.setInt(7, 1);
//            ps.setFloat(8, Float.parseFloat(txtSalesDueAmount.getText().trim()));
//
//            ps.setDate(9, convertUtilDateToSqlDate(dateSalesDate.getDate()));
//            ps.setString(10, txtSalesCustomerName.getText().trim());
//
//            ps.executeUpdate();
//            ps.close();
//            con.getCon().close();
//
//            JOptionPane.showMessageDialog(rootPane, "Data submited");
//            updateStockSales();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(rootPane, "Data not submit");
//            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
//        }


    }//GEN-LAST:event_btnSalesSubmitMouseClicked

    private void txtSalesDiscountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalesDiscountKeyTyped
        // TODO add your handling code here:
        txtSalesActualPrice.setText(getActualPrice() + "");
    }//GEN-LAST:event_txtSalesDiscountKeyTyped

    private void txtSalesDiscountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesDiscountFocusGained
        // TODO add your handling code here:
        txtSalesDiscount.setText(0 + "");
    }//GEN-LAST:event_txtSalesDiscountFocusGained

    private void txtSalesCashReceivedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesCashReceivedFocusLost
        // TODO add your handling code here:
        float dueAmount = getActualPrice() - Float.parseFloat(txtSalesCashReceived.getText().trim());

        txtSalesDueAmount.setText(dueAmount + "");
    }//GEN-LAST:event_txtSalesCashReceivedFocusLost

    private void btnPsaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPsaveMouseClicked
        // TODO add your handling code here:
        sql = "insert into product(name, catagory, productCode) values(?,?,?)";
        try {

            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, txtProductName.getText().trim());
            ps.setString(2, txtProductCatagory.getSelectedItem().toString());
            ps.setString(3, txtProductCode.getText().trim());

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Saved");
            getAllProduct();
            txtPurchaseProductCombo.removeAllItems();
            getProductNameToCombo();
            addProductToStock();
        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data not Saved");
        }

    }//GEN-LAST:event_btnPsaveMouseClicked

    private void btnPsaveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPsaveMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPsaveMouseEntered

    private void btnRresetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRresetMouseClicked
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_btnRresetMouseClicked

    public void updateStockProductName(int stockId) {
        sql = "update stock set pName=? where stockId=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtProductName.getText().trim());
            ps.setInt(2, stockId);

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void btnProductUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductUpdateMouseClicked

        sql = "update product set name=?, catagory=?, productCode=? where purchaseId=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtProductName.getText().trim());
            ps.setString(2, txtProductCatagory.getSelectedItem().toString());
            ps.setString(3, txtProductCode.getText().trim());
            ps.setInt(4, Integer.parseInt(txtProductId.getText()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            updateStockProductName(Integer.parseInt(txtProductId.getText()));

            JOptionPane.showMessageDialog(rootPane, "Product Updated");
            getAllProduct();

            getProductNameToCombo();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Product not update");
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnProductUpdateMouseClicked

    private void tableProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProductMouseClicked
        // TODO add your handling code here:
        int rowIndex = tableProduct.getSelectedRow();
        String id = tableProduct.getModel().getValueAt(rowIndex, 0).toString();
        String name = tableProduct.getModel().getValueAt(rowIndex, 1).toString();
        String catagory = tableProduct.getModel().getValueAt(rowIndex, 2).toString();
        String productCode = tableProduct.getModel().getValueAt(rowIndex, 3).toString();

        txtProductId.setText(id);
        txtProductName.setText(name);
        txtProductCatagory.setSelectedItem(catagory);
        txtProductCode.setText(productCode);
    }//GEN-LAST:event_tableProductMouseClicked

    private void btnPdelateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPdelateMouseClicked
        // TODO add your handling code here:
        sql = "Delete from product where purchaseID =?";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(txtProductId.getText().trim()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Deleted");
            reset();
            getAllProduct();
        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data not Delete");
        }


    }//GEN-LAST:event_btnPdelateMouseClicked

    
     // stock method create purchase add contain to stock*************
    public void getStock() {
        sql = "update stock set quantity = quantity + ? where PName=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            System.out.println("ok");
            ps.setFloat(1, Float.parseFloat(txtPurchaseQuantity.getText().trim()));
            System.out.println("ok");
            ps.setString(2, txtPurchaseProductCombo.getSelectedItem().toString());
            System.out.println("ok");

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

           

        } catch (SQLException ex) {
           
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    
    private void btnPurchaseSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseSaveMouseClicked
        // TODO add your handling code here:
        sql = "insert into purchase(name, quantity, unitprice,totalprice,date)"
                + "values(?,?,?,?,?)";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, txtPurchaseProductCombo.getSelectedItem().toString());
            ps.setFloat(2, Float.parseFloat(txtPurchaseQuantity.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtPurchaseUnitPrice.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtPurchaseTotalPrice.getText().trim()));
            ps.setDate(5, convertUtilDateToSqlDate(txtPurchaseDate.getDate()));

            ps.executeUpdate();

            ps.close();
            con.getCon().close();
            JOptionPane.showMessageDialog(rootPane, "Data Save");
            
            getStock();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Data not Save");
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateStockPurchase();
        getAllPurchase();

    }//GEN-LAST:event_btnPurchaseSaveMouseClicked

    private void txtProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductNameActionPerformed

    private void btnReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportMouseClicked
        // TODO add your handling code here:
        menu.setSelectedIndex(4);
    }//GEN-LAST:event_btnReportMouseClicked

    public void getPurchaseReportByDate(java.util.Date fromDate, java.util.Date toDate) {
        String[] columnNames = {"Product Name", "Unit Price", "Quantity", "Total Amount", "Date"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        tblReport.setModel(model);

        sql = "select * from purchase where date between ? and ?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            while (rs.next()) {

                String name = rs.getString("name");
                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
                float totalPrice = rs.getFloat("totalPrice");
                java.util.Date date = rs.getDate("date");

                model.addRow(new Object[]{name, unitPrice, quantity, totalPrice, date});
            }

            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getSalesReportByDate(java.util.Date fromDate, java.util.Date toDate) {
        String[] columnNames = {"Product Name", "Unit Price", "Quantity", "Discount", "Due Amount", "Total Amount"};

        DefaultTableModel model = new DefaultTableModel();

        model.setColumnIdentifiers(columnNames);
        tblReport.setModel(model);

        sql = "select * from sales where date between ? and ?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            while (rs.next()) {

                String name = rs.getString("name");
                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
                float discount = rs.getFloat("discount");
                float dueAmount = rs.getFloat("dueAmount");
                float actualPrice = rs.getFloat("actualPrice");

                model.addRow(new Object[]{name, unitPrice, quantity, discount, dueAmount, actualPrice});
            }

            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getStockReportByDate() {

        String[] columnNames = {"Product Name", "Quantity"};
        DefaultTableModel model = new DefaultTableModel();

        model.setColumnIdentifiers(columnNames);
        tblReport.setModel(model);

        sql = "select * from stock";

        try {
            ps = con.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                String name = rs.getString("pName");

                float quantity = rs.getFloat("quantity");

                model.addRow(new Object[]{name, quantity});
            }

            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void btnRadioViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRadioViewMouseClicked

        if (radioReportPurchase.isSelected()) {
            getPurchaseReportByDate(dateReportFrom.getDate(), dateReportTo.getDate());

        } else if (radioReportSales.isSelected()) {
            getSalesReportByDate(dateReportFrom.getDate(), dateReportTo.getDate());

        } else if (radioReportStock.isSelected()) {
            getStockReportByDate();

        }
    }//GEN-LAST:event_btnRadioViewMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        DefaultTableModel model = (DefaultTableModel) tblSales.getModel();

        String productName = txtSalesProductName.getSelectedItem().toString();
        float unitPrice = Float.parseFloat(txtSaleUnitPrice.getText());
        float quantity = Float.parseFloat(txtSaleQuantity.getText());
        float totalPrice = Float.parseFloat(txtSalesTotalPrice.getText());
        float discount = Float.parseFloat(txtSalesDiscount.getText().trim());
        float actualPrice = getActualPrice();
        float dueAmount = Float.parseFloat(txtSalesDueAmount.getText().trim());
        
        
        
    //    float cashPay = Float.parseFloat(sCashReceive.getText());

        java.util.Date date = convertUtilDateToSqlDate(dateSalesDate.getDate());
        String CustomerName = txtSalesCustomerName.getText().trim();

        List<Object> productList = new ArrayList<>();

        productList.add(new Object[]{productName, unitPrice, quantity, totalPrice, discount, actualPrice,dueAmount, date, CustomerName});

        int row = model.getRowCount();

        for (Object i : productList) {
            model.insertRow(row, (Object[]) i);
        }

    }//GEN-LAST:event_jButton1MouseClicked

    private void btnProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductMouseEntered
        btnProduct.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnProductMouseEntered

    private void btnProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductMouseExited
        btnProduct.setBackground(getBackground());
    }//GEN-LAST:event_btnProductMouseExited

    private void btnProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductActionPerformed
        menu.setSelectedIndex(3);
    }//GEN-LAST:event_btnProductActionPerformed

    private void btnCustomerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerMouseEntered
        btnCustomer.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnCustomerMouseEntered

    private void btnCustomerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerMouseExited
        btnCustomer.setBackground(getBackground());
    }//GEN-LAST:event_btnCustomerMouseExited

    private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerActionPerformed
        menu.setSelectedIndex(5);
    }//GEN-LAST:event_btnCustomerActionPerformed

    private void btnStockMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStockMouseEntered
        btnCustomer.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnStockMouseEntered

    private void btnStockMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStockMouseExited
        btnStock.setBackground(getBackground());
    }//GEN-LAST:event_btnStockMouseExited

    private void btnStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockActionPerformed
        menu.setSelectedIndex(6);
    }//GEN-LAST:event_btnStockActionPerformed

    private void btnProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnProductMouseClicked

    private void btnWarrentyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnWarrentyMouseClicked
        String [] column={"Id","Produnt name","qty"};
        DefaultTableModel dtm =new DefaultTableModel();
        dtm.setColumnIdentifiers(column);
        tblStock.setModel(dtm);
        
        
        sql="SELECT * FROM swingproject.stock";
        try {
            ps= con.getCon().prepareStatement(sql);
            rs= ps.executeQuery();
            while(rs.next()){
               int id =rs.getInt("stockId");
               String productName = rs.getString("pName");
               float quant = rs.getFloat("quantity");
               
               dtm.addColumn(new Object[]{id,productName,quant});
               
            }
            rs.close();
            ps.close();
            con.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnWarrentyMouseClicked

    private void btnReportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportMouseEntered
        btnReport.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnReportMouseEntered

    private void btnReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportMouseExited
        btnReport.setBackground(getBackground());
    }//GEN-LAST:event_btnReportMouseExited

    private void txtPurchaseProductComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPurchaseProductComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurchaseProductComboActionPerformed

    private void txtPurchaseQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseQuantityFocusLost
        try {
            if (txtPurchaseUnitPrice.getText().trim().isEmpty()) {
                txtPurchaseUnitPrice.requestFocus();
            } else if (!txtPurchaseQuantity.getText().trim().isEmpty()) {
                txtPurchaseTotalPrice.setText(getTotalPricePurchase()+ "");
            } else {
                JOptionPane.showMessageDialog(rootPane, "Quantity is empty");
                txtSaleQuantity.requestFocus();
            }
        } catch (Exception f) {
            JOptionPane.showMessageDialog(rootPane, "Error" + f.getMessage());
        }
    }//GEN-LAST:event_txtPurchaseQuantityFocusLost

    private void txtPurchaseUnitPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseUnitPriceFocusLost
       try {
            if (!txtPurchaseUnitPrice.getText().trim().isEmpty()) {

            } else {
                JOptionPane.showMessageDialog(rootPane, "Unit price is empty");
                txtPurchaseUnitPrice.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error" + e.getMessage());

        }
              
    }//GEN-LAST:event_txtPurchaseUnitPriceFocusLost

    private void btnPurchaseUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseUpdateMouseClicked
//        sql = "update purchase set  unitPrice=? ,quantity=?,totalPrice=?,  where purchaseId=?";
//
//        try {
//            ps = con.getCon().prepareStatement(sql);
//
//            
//            ps.setInt(1, Integer.parseInt(txtPurchaseUnitPrice.getText()));
//            ps.setInt(2, Integer.parseInt(txtPurchaseQuantity.getText()));
//            ps.setInt(3, Integer.parseInt(txtPurchaseTotalPrice.getText()));
//             ps.setInt(4, Integer.parseInt(txtPurchaseProductId.getText()));
//            
//            
//       
//            ps.executeUpdate();
//            ps.close();
//            con.getCon().close();
//
//            updateStockProductName(Integer.parseInt(txtProductId.getText()));
//
//            JOptionPane.showMessageDialog(rootPane, "Purchase Updated");
            getAllPurchase();
//
////            getProductNameToCombo();
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(rootPane, "Purchase not update");
//            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_btnPurchaseUpdateMouseClicked

    private void btnPurchaseUpdateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseUpdateMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPurchaseUpdateMouseEntered

    private void txtSalesActualPriceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSalesActualPriceMouseClicked
        getActualPrice();
    }//GEN-LAST:event_txtSalesActualPriceMouseClicked

    // Print total price from cuatomer table **************    
    public float getActualTotal() {

        float ff = 0.0f;

        DefaultTableModel model = (DefaultTableModel) tblSales.getModel();
        int row = model.getRowCount();

        for (int i = 0; i < row; i++) {
            float aPrice = Float.valueOf(tblSales.getModel().getValueAt(i, 5).toString());
            ff += aPrice;

        }
        return ff;
    }

    // cash Pay method create ****************************  
//    public float cashPay() {
//
//        float ff = 0.0f;
//
//        DefaultTableModel model = (DefaultTableModel) tblSales.getModel();
//        int row = model.getRowCount();
//
//        for (int i = 0; i < row; i++) {
//            float aPrice = Float.valueOf(tblSales.getModel().getValueAt(i, 7).toString());
//            ff += aPrice;
//        }
//        return ff;
//    }

    // Customer k Tk back method create ****************************  
//    public float balance() {
//        Float balance = 0.0f;
//
//        if (getActualTotal() < cashPay()) {
//            balance = cashPay() - getActualTotal();
//        } else {
//            float bala = getActualTotal() - cashPay();
//            balance = bala * (-1);
//        }
//        return balance;
//    }

    
    
    
    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked
        try {

            card.setText(card.getText() + "\t\t\t THE TECH MAN \t\n");
            card.setText(card.getText() + "\t\t\t Mirpur1, Dhaka-1207, Bangladesh \n");
            card.setText(card.getText() + "----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            card.setText(card.getText() + "P Name" + "\tUnit Price" + "\tQuantity" + "\tTotal Price" + "\tDiscount" + "\tActual Price" +"\tdue"+ "\tDate" +"\tCust Name " + "\n");
            card.setText(card.getText() + "----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

            DefaultTableModel model = (DefaultTableModel) tblSales.getModel();  //  Data from customer table ********

            // get table product details
            for (int i = 0; i < model.getRowCount(); i++) {

                String customerName = model.getValueAt(i, 0).toString();
                String name = model.getValueAt(i, 1).toString();
                String unitPrice = model.getValueAt(i, 2).toString();
                String quantity = model.getValueAt(i, 3).toString();
                String totalPrice = model.getValueAt(i, 4).toString();
                String discount = model.getValueAt(i, 5).toString();
                String actualPrice = model.getValueAt(i, 6).toString();
                String due = model.getValueAt(i, 7).toString();
//                String cashPay = model.getValueAt(i, 7).toString();
                
                String date = model.getValueAt(i, 8).toString();

                card.setText(card.getText() + customerName + "\t"+ name + "\t" + unitPrice + "\t" + quantity +"\t" + totalPrice + "\t" + discount + "\t" + actualPrice +"\t"+due+ "\t" + date + "\n");

            }
            card.setText(card.getText() + "----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            
            card.setText(card.getText() + "\tTotal Price\t        :\t"  +getActualTotal()+ "\n");
                
//            card.setText(card.getText() + "\tTotal Price\t       :\t" + getActualTotal() + "\n");
//            card.setText(card.getText() + "\tCash Pay\t       :\t" + cashPay() + "\n");
//            card.setText(card.getText() + "\tBalance Back      :\t" + balance() + "\n");
//
//            card.setText(card.getText() + "----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
//
            card.setText(card.getText() + "\t\t\t\t Thank you so much \n\n");
           
//            card.setText(card.getText()+"\tTotal price\t      :\t" + getActualTotal() + "\n");
            
            

            card.print();

        } catch (Exception e) {
           
        }
	

    }//GEN-LAST:event_jButton6MouseClicked

    
    
    
      // purchase barar jonno method*************
//    public void addProductToStock2() {
//        sql = " insert into stock (stockName, stockQuantity) values (?,?)";
//
//        try {
//            ps = con.getCon().prepareStatement(sql);
//            ps.setString(1, txtPurchaseProductCombo.getSelectedItem().toString());
//            ps.setFloat(2, 0.0f);
//            
//
//            ps.executeUpdate();
//            ps.close();
//            con.getCon().close();
//
//        } catch (SQLException ex) {
//            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//     // colponick stock table create
//    String[] stockColumns = {"stockID", "stockName", "stockQuantity", "stockCode"};

//    public void getAllStockTable() {
//        sql = "select * from stock";
//        DefaultTableModel model = new DefaultTableModel();
//        model.setColumnIdentifiers(stockColumns);
//        tblStock.setModel(model);
//        try {
//            ps = con.getCon().prepareStatement(sql);
//            rs = ps.executeQuery();
//
//            while (rs.next()) {
//                int ID = rs.getInt("stockID");
//                String name = rs.getString("stockName");
//                Float quantity = rs.getFloat("stockQuantity");
//                String code = rs.getString("stockCode");
//                model.addRow(new Object[]{ID, name, quantity, code});
//            }
//            ps.executeQuery();
//            ps.close();
//            con.getCon().close();
//        } catch (SQLException ex) {
//            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    
    
    
    private void tblStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStockMouseClicked
       
    }//GEN-LAST:event_tblStockMouseClicked

    private void btnCustomerRegMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerRegMouseClicked
        
        
        
    }//GEN-LAST:event_btnCustomerRegMouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
      resetSales();
    }//GEN-LAST:event_jButton2MouseClicked

    private void btnWarentyFindMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnWarentyFindMouseClicked
        // TODO add your handling code here:
        String sql1="select date, warrent_date from sales where sid=?";
        
        try {
            ps=con.getCon().prepareStatement(sql1);
            ps.setInt(1, Integer.parseInt(txtWarentyId.getText().trim()));
            
            ResultSet rs1=ps.executeQuery();
            while(rs1.next()){
            Date war=rs1.getDate("warrent_date");
            Date date=rs1.getDate("date");
            
            warentyDate.setText(war+"");
            salesdateWarenty.setText(date+"");
            
            }
            rs1.close();
            ps.close();
            con.getCon().close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }//GEN-LAST:event_btnWarentyFindMouseClicked

    private void tblStockMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStockMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblStockMouseEntered

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DashBorad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashBorad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashBorad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashBorad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashBorad().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Stock;
    private javax.swing.JButton btnCustomer;
    private javax.swing.JButton btnCustomerReg;
    private javax.swing.JButton btnCustomerReset;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnPdelate;
    private javax.swing.JButton btnProduct;
    private javax.swing.JButton btnProductUpdate;
    private javax.swing.JButton btnPsave;
    private javax.swing.JButton btnPurchase;
    private javax.swing.JPanel btnPurchaseDelate;
    private javax.swing.JButton btnPurchaseReset;
    private javax.swing.JButton btnPurchaseSave;
    private javax.swing.JButton btnPurchaseUpdate;
    private javax.swing.JButton btnRadioView;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnRreset;
    private javax.swing.JButton btnSalesSubmit;
    private javax.swing.JButton btnSalse;
    private javax.swing.JButton btnStock;
    private javax.swing.JButton btnWarentyFind;
    private javax.swing.JButton btnWarrenty;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JTextArea card;
    private javax.swing.JTabbedPane customer;
    private com.toedter.calendar.JDateChooser dateReportFrom;
    private com.toedter.calendar.JDateChooser dateReportTo;
    private com.toedter.calendar.JDateChooser dateSalesDate;
    private javax.swing.JPanel home;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblTo;
    private javax.swing.JLabel lblTodayDue;
    private javax.swing.JLabel lblTodayPurchase;
    private javax.swing.JLabel lblTodaySales;
    private javax.swing.JLabel lblTotalDue;
    private javax.swing.JLabel lblTotalPurchase;
    private javax.swing.JLabel lblTotalSales;
    private javax.swing.JTabbedPane menu;
    private javax.swing.JTabbedPane product;
    private javax.swing.JTabbedPane purchase;
    private javax.swing.JRadioButton radioGenderFemale;
    private javax.swing.JRadioButton radioGenderMale;
    private javax.swing.JRadioButton radioReportPurchase;
    private javax.swing.JRadioButton radioReportSales;
    private javax.swing.JRadioButton radioReportStock;
    private javax.swing.JTabbedPane report;
    private javax.swing.JTabbedPane sales;
    private javax.swing.JTextField salesdateWarenty;
    private javax.swing.JTable tableProduct;
    private javax.swing.JTable tblPurchase;
    private javax.swing.JTable tblReport;
    private javax.swing.JTable tblSales;
    private javax.swing.JTable tblStock;
    private javax.swing.JTextField txtContractNo;
    private javax.swing.JTextArea txtCustomerAddress;
    private javax.swing.JTextField txtCustomerEmail;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JComboBox<String> txtProductCatagory;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtProductId;
    private javax.swing.JTextField txtProductName;
    private com.toedter.calendar.JDateChooser txtPurchaseDate;
    private javax.swing.JComboBox<String> txtPurchaseProductCombo;
    private javax.swing.JTextField txtPurchaseProductId;
    private javax.swing.JTextField txtPurchaseQuantity;
    private javax.swing.JTextField txtPurchaseTotalPrice;
    private javax.swing.JTextField txtPurchaseUnitPrice;
    private javax.swing.JTextField txtSaleQuantity;
    private javax.swing.JTextField txtSaleUnitPrice;
    private javax.swing.JTextField txtSalesActualPrice;
    private javax.swing.JTextField txtSalesCashReceived;
    private javax.swing.JTextField txtSalesCustomerName;
    private javax.swing.JTextField txtSalesDiscount;
    private javax.swing.JTextField txtSalesDueAmount;
    private javax.swing.JComboBox<String> txtSalesProductName;
    private javax.swing.JTextField txtSalesTotalPrice;
    private javax.swing.JTextField txtWarentyId;
    private javax.swing.JTextField warentyDate;
    private javax.swing.JTabbedPane warranty;
    // End of variables declaration//GEN-END:variables
}
