/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import dao.DBConnect;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.KhachHang;

/**
 *
 * @author Administrator
 */
public class KhachHangJFrame extends javax.swing.JFrame {

    /**
     * Creates new form KhachHangJFrame
     */
    public KhachHangJFrame() {
        initComponents();
        
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo_PL.png")));

    }

    public void select(KhachHang khachHang) {

        jtfMaKhachHang.setText(khachHang.getMaKhachHang());
        jtfHoTen.setText(khachHang.getHoTen());
        jdcNgaySinh.setDate(khachHang.getNgaySinh());
        if (khachHang.getGioiTinh().equalsIgnoreCase("Nam")) {
            jrdGTNam.setSelected(true);
        } else if (khachHang.getGioiTinh().equalsIgnoreCase("Nữ")) {
            jrdGTNu.setSelected(true);
        } else {
            jrdGTKhac.setSelected(true);
        }
        jtfDienThoai.setText(khachHang.getDienThoai());
        jtaDiaChi.setText(khachHang.getDiaChi());
    }

    public boolean kiemTra() {
        if (jtfMaKhachHang.getText().length() == 0 || jtfHoTen.getText().length() == 0
                || jdcNgaySinh.getDate() == null || jtfDienThoai.getText().length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void insert() {
        try {
            Connection cons = DBConnect.getConnection();
            String sql = "Insert into KhachHang(MaKH, HoTenKH, NgaySinh, GioiTinh, DienThoai, DiaChi) Values(?,?,convert(date,?),?,?,?)";
            PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, jtfMaKhachHang.getText());
            ps.setString(2, jtfHoTen.getText());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(jdcNgaySinh.getDate());
            ps.setString(3, date);
            if (jrdGTNam.isSelected()) {
                ps.setString(4, "Nam");
            } else if (jrdGTNu.isSelected()) {
                ps.setString(4, "Nữ");
            } else {
                ps.setString(4, "Khác");
            }
            ps.setString(5, jtfDienThoai.getText());
            if (jtaDiaChi.getText() == null) {
                ps.setString(6, null);
            } else {
                ps.setString(6, jtaDiaChi.getText());
            }
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            int generatrdKey = 0;
            if (rs.next()) {
                generatrdKey = rs.getInt(1);
            }
            ps.close();
            rs.close();
            cons.close();

            JOptionPane.showMessageDialog(this, "Đã thêm thành công!");
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update() {
        try {
            Connection cons = DBConnect.getConnection();
            String maKh = jtfMaKhachHang.getText();
            String sql = "Update KhachHang set HoTenKH = ?, NgaySinh = ?, GioiTinh = ?, DienThoai = ?, DiaChi = ? where MaKH = '" + maKh + "'";
            PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, jtfHoTen.getText());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(jdcNgaySinh.getDate());
            ps.setString(2, date);
            if (jrdGTNam.isSelected()) {
                ps.setString(3, "Nam");
            } else if (jrdGTNu.isSelected()) {
                ps.setString(3, "Nữ");
            } else {
                ps.setString(3, "Khác");
            }
            ps.setString(4, jtfDienThoai.getText());
            if (jtaDiaChi.getText() == null) {
                ps.setString(5, null);
            } else {
                ps.setString(5, jtaDiaChi.getText());
            }
            ps.executeUpdate();

            ps.close();
            cons.close();

            JOptionPane.showMessageDialog(this, "Đã cập nhật thành công!");
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(String maKh) {
        int opt = JOptionPane.showConfirmDialog(null, "Bạn muốn xóa khách hàng này? \nNếu bạn xóa một khách hàng thì mọi hóa đơn của khách hàng đó cũng sẽ bị xóa hết.", "Xóa khách hàng", JOptionPane.YES_NO_OPTION);
        if (opt == 0) {
            try {
                Connection cons = DBConnect.getConnection();
                String sql = "Delete from KhachHang where MaKH = '" + maKh + "'";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.executeUpdate();

                ps.close();
                cons.close();

                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                KhachHangJPanel.hienThiTable();
            } catch (SQLException ex) {
                Logger.getLogger(KhachHangJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            dispose();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jbnLuu = new javax.swing.JButton();
        jbnXoa = new javax.swing.JButton();
        jpnView = new javax.swing.JPanel();
        jlbMaKhachHang = new javax.swing.JLabel();
        jtfMaKhachHang = new javax.swing.JTextField();
        jlbHoTen = new javax.swing.JLabel();
        jtfHoTen = new javax.swing.JTextField();
        jlbNgaySinh = new javax.swing.JLabel();
        jdcNgaySinh = new com.toedter.calendar.JDateChooser();
        jlbGioiTinh = new javax.swing.JLabel();
        jrdGTNam = new javax.swing.JRadioButton();
        jrdGTNu = new javax.swing.JRadioButton();
        jlbDienThoai = new javax.swing.JLabel();
        jtfDienThoai = new javax.swing.JTextField();
        jlbDiaChi = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaDiaChi = new javax.swing.JTextArea();
        jrdGTKhac = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jbnLuu.setBackground(new java.awt.Color(157, 205, 239));
        jbnLuu.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jbnLuu.setText("Lưu dữ liệu");
        jbnLuu.setToolTipText("");
        jbnLuu.setBorder(null);
        jbnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnLuuActionPerformed(evt);
            }
        });

        jbnXoa.setBackground(new java.awt.Color(157, 205, 239));
        jbnXoa.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jbnXoa.setText("Xoá");
        jbnXoa.setToolTipText("");
        jbnXoa.setBorder(null);
        jbnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnXoaActionPerformed(evt);
            }
        });

        jpnView.setBackground(new java.awt.Color(255, 255, 255));
        jpnView.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 19))); // NOI18N

        jlbMaKhachHang.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbMaKhachHang.setText("Mã khách hàng");

        jtfMaKhachHang.setEditable(false);
        jtfMaKhachHang.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbHoTen.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbHoTen.setText("Họ và tên");

        jtfHoTen.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbNgaySinh.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbNgaySinh.setText("Ngày sinh");

        jdcNgaySinh.setDateFormatString("dd/MM/yyyy");
        jdcNgaySinh.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbGioiTinh.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbGioiTinh.setText("Giới tính");

        jrdGTNam.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jrdGTNam);
        jrdGTNam.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdGTNam.setSelected(true);
        jrdGTNam.setText("Nam");

        jrdGTNu.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jrdGTNu);
        jrdGTNu.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdGTNu.setText("Nữ");

        jlbDienThoai.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbDienThoai.setText("Điện thoại");

        jtfDienThoai.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbDiaChi.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbDiaChi.setText("Địa chỉ");

        jtaDiaChi.setColumns(20);
        jtaDiaChi.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jtaDiaChi.setRows(5);
        jScrollPane1.setViewportView(jtaDiaChi);

        jrdGTKhac.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jrdGTKhac);
        jrdGTKhac.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdGTKhac.setText("Khác");
        jrdGTKhac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdGTKhacActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnViewLayout = new javax.swing.GroupLayout(jpnView);
        jpnView.setLayout(jpnViewLayout);
        jpnViewLayout.setHorizontalGroup(
            jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnViewLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlbNgaySinh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbHoTen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbMaKhachHang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnViewLayout.createSequentialGroup()
                        .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfMaKhachHang)
                            .addComponent(jtfHoTen)
                            .addComponent(jdcNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
                        .addGap(61, 61, 61)
                        .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlbDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                            .addComponent(jtfDienThoai))
                        .addGap(32, 32, 32))
                    .addGroup(jpnViewLayout.createSequentialGroup()
                        .addComponent(jrdGTNam, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jrdGTNu, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jrdGTKhac, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jpnViewLayout.setVerticalGroup(
            jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnViewLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnViewLayout.createSequentialGroup()
                        .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpnViewLayout.createSequentialGroup()
                                .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlbMaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtfMaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33, 33, 33)
                                .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlbHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtfHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(34, 34, 34)
                                .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jlbNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jdcNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)))
                            .addGroup(jpnViewLayout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(35, 35, 35)
                        .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jrdGTNam, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jrdGTNu, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jrdGTKhac, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpnViewLayout.createSequentialGroup()
                        .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlbDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtfDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addComponent(jlbDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jbnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jpnView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(jpnView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnLuuActionPerformed
        if (!kiemTra()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập dữ liệu bắt buộc!");
        } else {
            try {
                Connection cons = DBConnect.getConnection();
                String sql = "select MaKh from KhachHang where MaKh = ?";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, jtfMaKhachHang.getText());

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    update();
                    dispose();
                } else {
                    insert();
                    dispose();
                }

                cons.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            KhachHangJPanel.hienThiTable();
        }
    }//GEN-LAST:event_jbnLuuActionPerformed

    private void jbnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnXoaActionPerformed
        if (!kiemTra()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
        } else {
            delete(jtfMaKhachHang.getText());
        }
    }//GEN-LAST:event_jbnXoaActionPerformed

    private void jrdGTKhacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdGTKhacActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jrdGTKhacActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JButton jbnLuu;
    public javax.swing.JButton jbnXoa;
    private com.toedter.calendar.JDateChooser jdcNgaySinh;
    private javax.swing.JLabel jlbDiaChi;
    private javax.swing.JLabel jlbDienThoai;
    private javax.swing.JLabel jlbGioiTinh;
    private javax.swing.JLabel jlbHoTen;
    private javax.swing.JLabel jlbMaKhachHang;
    private javax.swing.JLabel jlbNgaySinh;
    private javax.swing.JPanel jpnView;
    private javax.swing.JRadioButton jrdGTKhac;
    private javax.swing.JRadioButton jrdGTNam;
    private javax.swing.JRadioButton jrdGTNu;
    private javax.swing.JTextArea jtaDiaChi;
    private javax.swing.JTextField jtfDienThoai;
    private javax.swing.JTextField jtfHoTen;
    public javax.swing.JTextField jtfMaKhachHang;
    // End of variables declaration//GEN-END:variables
}
