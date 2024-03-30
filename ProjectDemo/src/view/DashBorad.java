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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    public void todaySalesReport() {

        sql = "select sum(actualPrice) from sales where Date=?";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setDate(1, sqlDate);

            rs = ps.executeQuery();
            
            while(rs.next()){
                Float totalPrice=rs.getFloat("sum(actualPrice)");
                lblTodaySales.setText(totalPrice+ "");                
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
            ps.setString(1, sqlDate.toString().substring(0, 8)+"%");

            rs = ps.executeQuery();
            
            while(rs.next()){
                Float totalPrice=rs.getFloat("sum(actualPrice)");
                lblTotalSales.setText(totalPrice+ "");                
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
            
            while(rs.next()){
                Float totalPrice=rs.getFloat("sum(totalPrice)");
                lblTodayPurchase.setText(totalPrice+ "");                
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
            
            while(rs.next()){
                Float totalPrice=rs.getFloat("sum(totalPrice)");
                lblTotalPurchase.setText(totalPrice+ "");                
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
    }

    public void reset() {
        txtProductId.setText(null);
        txtProductName.setText(null);
        txtProductCatagory.getSelectedItem().toString();
        txtProductCode.setText(null);
    }
//calculate total price

    public float getTotalPrice() {
        float unitPrice = Float.parseFloat(txtSaleUnitPrice.getText().trim());
        float quanitiy = Float.parseFloat(txtSaleQuantity.getText().trim());

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
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        btnSalse = new javax.swing.JButton();
        btnPurchase = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
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
        jLabel33 = new javax.swing.JLabel();
        lblTodayPurchase = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        lblTodayDue = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        TotalPurchase = new javax.swing.JLabel();
        lblTotalPurchase = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        lblTotalSales = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        lblTotalDue = new javax.swing.JLabel();
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
        jComboBox1 = new javax.swing.JComboBox<>();
        btnSalesSubmit = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtSalesActualPrice = new javax.swing.JTextField();
        txtSalesProductName = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
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
        jPanel18 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        radioReportPurchase = new javax.swing.JRadioButton();
        radioReportSales = new javax.swing.JRadioButton();
        radioReportStock = new javax.swing.JRadioButton();
        btnRadioView = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblReport = new javax.swing.JTable();
        dateReportFrom = new com.toedter.calendar.JDateChooser();
        dateReportTo = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Dash Borad");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel1.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 140, 40));

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
        jPanel1.add(btnSalse, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 140, 40));

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
        jPanel1.add(btnPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 140, 40));

        btn4.setText("Product");
        btn4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn4MouseExited(evt);
            }
        });
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
            }
        });
        jPanel1.add(btn4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 140, 40));

        btnReport.setText("Report");
        btnReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportMouseClicked(evt);
            }
        });
        jPanel1.add(btnReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 140, 40));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        home.setPreferredSize(new java.awt.Dimension(500, 250));

        jPanel4.setBackground(new java.awt.Color(0, 153, 153));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Home");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1030, 130));

        jPanel19.setMinimumSize(new java.awt.Dimension(800, 400));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Today Sales");
        jPanel20.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 70));

        lblTodaySales.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTodaySales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTodaySales.setText("0.00");
        jPanel20.add(lblTodaySales, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 80));

        jPanel19.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 230, 150));

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Today Purchase");
        jPanel21.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 4, 230, 70));

        lblTodayPurchase.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTodayPurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTodayPurchase.setText("0.00");
        jPanel21.add(lblTodayPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 80));

        jPanel19.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 230, 150));

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Today Due");
        jPanel22.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 70));

        lblTodayDue.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTodayDue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTodayDue.setText("0.00");
        jPanel22.add(lblTodayDue, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 80));

        jPanel19.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 80, 230, 150));

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TotalPurchase.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        TotalPurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalPurchase.setText("Total Purchase");
        jPanel23.add(TotalPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 70));

        lblTotalPurchase.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTotalPurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalPurchase.setText("0.00");
        jPanel23.add(lblTotalPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 80));

        jPanel19.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 230, 150));

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("Total Sales");
        jPanel24.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 70));

        lblTotalSales.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTotalSales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalSales.setText("0.00");
        jPanel24.add(lblTotalSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 80));

        jPanel19.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 280, 230, 150));

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Total Due");
        jPanel25.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 70));

        lblTotalDue.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTotalDue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalDue.setText("0.00");
        jPanel25.add(lblTotalDue, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 80));

        jPanel19.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 280, 230, 150));

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1280, Short.MAX_VALUE)
            .addGroup(homeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        menu.addTab("tab1", home);

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(0, 153, 153));

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Salse");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
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

        jPanel6.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 940, 440));

        jLabel4.setText("SID");

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

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnSalesSubmit.setText("Submit");
        btnSalesSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalesSubmitMouseClicked(evt);
            }
        });

        jButton2.setText("Reset");

        jLabel14.setText("Actual Price");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addGap(85, 85, 85))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(35, 35, 35)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(57, 57, 57)))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addGap(61, 61, 61)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel14))
                        .addGap(46, 46, 46)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSaleQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSalesDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addComponent(jTextField1)
                            .addComponent(txtSaleUnitPrice)
                            .addComponent(txtSalesTotalPrice)
                            .addComponent(txtSalesActualPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addComponent(txtSalesProductName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(68, 68, 68)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(btnSalesSubmit))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtSalesDueAmount)
                                .addComponent(txtSalesCashReceived, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel10)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSalesCashReceived, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtSalesDueAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtSalesProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(jLabel6)
                        .addComponent(txtSaleUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtSaleQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtSalesTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtSalesDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtSalesActualPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalesSubmit)
                    .addComponent(jButton2))
                .addContainerGap(117, Short.MAX_VALUE))
        );

        sales.addTab("tab1", jPanel5);

        menu.addTab("tab2", sales);

        btnPurchaseDelate.setForeground(new java.awt.Color(0, 255, 255));
        btnPurchaseDelate.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(0, 153, 153));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Purchase");
        jPanel13.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 100));

        btnPurchaseDelate.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, -1));

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        btnPurchaseDelate.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 106, -1, -1));

        jLabel16.setText("Unit Price");
        btnPurchaseDelate.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        jLabel17.setText("Product ID");
        btnPurchaseDelate.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        jLabel18.setText("Quantity");
        btnPurchaseDelate.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        jLabel19.setText("Product Name");
        btnPurchaseDelate.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, -1));

        jLabel20.setText("Total Price");
        btnPurchaseDelate.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        jLabel21.setText("Date");
        btnPurchaseDelate.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, 20));
        btnPurchaseDelate.add(txtPurchaseProductId, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 150, -1));
        btnPurchaseDelate.add(txtPurchaseUnitPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 150, -1));
        btnPurchaseDelate.add(txtPurchaseQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 150, -1));
        btnPurchaseDelate.add(txtPurchaseTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 270, 150, -1));

        btnPurchaseSave.setText("Save");
        btnPurchaseSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseSaveMouseClicked(evt);
            }
        });
        btnPurchaseDelate.add(btnPurchaseSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, 60, -1));

        btnPurchaseUpdate.setText("Update");
        btnPurchaseDelate.add(btnPurchaseUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, -1, -1));

        jButton5.setText("Delate");
        btnPurchaseDelate.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 410, -1, -1));

        btnPurchaseReset.setText("Reset");
        btnPurchaseDelate.add(btnPurchaseReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 410, -1, -1));

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

        btnPurchaseDelate.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 120, -1, 240));

        txtPurchaseProductCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HP", "ASOS", "Lenevo", "Dell", " " }));
        btnPurchaseDelate.add(txtPurchaseProductCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 150, 150, -1));

        purchase.addTab("tab1", btnPurchaseDelate);

        menu.addTab("tab3", purchase);

        jPanel14.setBackground(new java.awt.Color(0, 153, 153));

        jLabel22.setBackground(new java.awt.Color(204, 204, 204));
        jLabel22.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Product");

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

        txtProductCatagory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mouse", "keybord", "Monitor", "Mother Board", "Ram", "Rom" }));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel23)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtProductId, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(txtProductCode)
                            .addComponent(txtProductName)
                            .addComponent(txtProductCatagory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnPdelate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPsave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(71, 71, 71)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnProductUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnRreset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(54, 54, 54)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(468, Short.MAX_VALUE))
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
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtProductCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(62, 62, 62)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPsave)
                            .addComponent(btnProductUpdate))
                        .addGap(64, 64, 64)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPdelate)
                            .addComponent(btnRreset)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 51, Short.MAX_VALUE))
        );

        product.addTab("product", jPanel11);

        menu.addTab("tab4", product);

        jPanel17.setBackground(new java.awt.Color(0, 153, 153));
        jPanel17.setForeground(new java.awt.Color(255, 255, 255));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setBackground(new java.awt.Color(0, 153, 153));
        jLabel27.setFont(new java.awt.Font("Arial Black", 0, 15)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Report");
        jPanel17.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 100));

        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        btnRadioView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRadioViewActionPerformed(evt);
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
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addComponent(btnRadioView))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateReportFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel29)
                        .addGap(37, 37, 37)
                        .addComponent(dateReportTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(41, 41, 41)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 657, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel28)
                            .addComponent(dateReportFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateReportTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(90, 90, 90)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radioReportPurchase)
                            .addComponent(radioReportSales)
                            .addComponent(radioReportStock)
                            .addComponent(btnRadioView)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(81, 81, 81))
        );

        report.addTab("tab1", jPanel16);

        menu.addTab("tab5", report);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
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

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(3);
    }//GEN-LAST:event_btn4ActionPerformed

    private void btnHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseEntered
        // TODO add your handling code here:
        btnHome.setBackground(Color.green);
    }//GEN-LAST:event_btnHomeMouseEntered

    private void btnSalseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalseMouseEntered
        // TODO add your handling code here:
        btnSalse.setBackground(Color.green);
    }//GEN-LAST:event_btnSalseMouseEntered

    private void btnPurchaseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseMouseEntered
        // TODO add your handling code here:
        btnPurchase.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnPurchaseMouseEntered

    private void btn4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn4MouseEntered
        // TODO add your handling code here:
        btn4.setBackground(Color.green);
    }//GEN-LAST:event_btn4MouseEntered

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

    private void btn4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn4MouseExited
        // TODO add your handling code here:
        btn4.setBackground(getBackground());
    }//GEN-LAST:event_btn4MouseExited

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
        txtSalesActualPrice.setText(getActualPrice() + "");
    }//GEN-LAST:event_txtSalesDiscountKeyReleased

    private void btnSalesSubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesSubmitMouseClicked
//         TODO add your handling code here:
        sql = "insert into sales(name, unitPrice, quantity, totalPrice, discount,"
                + " actualPrice, cashRecived, dueAmount, date)"
                + "  values(?,?,?,?,?,?,?,?,?)";
        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtSalesProductName.getSelectedItem().toString());
            ps.setFloat(2, Float.parseFloat(txtSaleUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtSaleQuantity.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtSalesTotalPrice.getText().trim()));

            ps.setFloat(5, getActualPrice());
            ps.setFloat(6, getDiscountAmount());
            ps.setFloat(7, Float.parseFloat(txtSalesDueAmount.getText().trim()));
            ps.setInt(8, 1);

            ps.setDate(9, convertUtilDateToSqlDate(dateSalesDate.getDate()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Data submited");
            updateStockSales();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Data not submit");
            Logger.getLogger(DashBorad.class.getName()).log(Level.SEVERE, null, ex);
        }


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

    private void btnRadioViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRadioViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRadioViewActionPerformed

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
    private javax.swing.JLabel TotalPurchase;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnPdelate;
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
    private javax.swing.ButtonGroup buttonGroup1;
    private com.toedter.calendar.JDateChooser dateReportFrom;
    private com.toedter.calendar.JDateChooser dateReportTo;
    private javax.swing.JPanel home;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
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
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblTodayDue;
    private javax.swing.JLabel lblTodayPurchase;
    private javax.swing.JLabel lblTodaySales;
    private javax.swing.JLabel lblTotalDue;
    private javax.swing.JLabel lblTotalPurchase;
    private javax.swing.JLabel lblTotalSales;
    private javax.swing.JTabbedPane menu;
    private javax.swing.JTabbedPane product;
    private javax.swing.JTabbedPane purchase;
    private javax.swing.JRadioButton radioReportPurchase;
    private javax.swing.JRadioButton radioReportSales;
    private javax.swing.JRadioButton radioReportStock;
    private javax.swing.JTabbedPane report;
    private javax.swing.JTabbedPane sales;
    private javax.swing.JTable tableProduct;
    private javax.swing.JTable tblPurchase;
    private javax.swing.JTable tblReport;
    private javax.swing.JComboBox<String> txtProductCatagory;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtProductId;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JComboBox<String> txtPurchaseProductCombo;
    private javax.swing.JTextField txtPurchaseProductId;
    private javax.swing.JTextField txtPurchaseQuantity;
    private javax.swing.JTextField txtPurchaseTotalPrice;
    private javax.swing.JTextField txtPurchaseUnitPrice;
    private javax.swing.JTextField txtSaleQuantity;
    private javax.swing.JTextField txtSaleUnitPrice;
    private javax.swing.JTextField txtSalesActualPrice;
    private javax.swing.JTextField txtSalesCashReceived;
    private javax.swing.JTextField txtSalesDiscount;
    private javax.swing.JTextField txtSalesDueAmount;
    private javax.swing.JComboBox<String> txtSalesProductName;
    private javax.swing.JTextField txtSalesTotalPrice;
    // End of variables declaration//GEN-END:variables
}
