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
import model.NhanVien;

/**
 *
 * @author Administrator
 */
public class NhanVienJFrame extends javax.swing.JFrame {

    /**
     * Creates new form NhanVienJFrame
     */
    public NhanVienJFrame() {
        initComponents();

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo_PL.png")));
    }

    public void select(NhanVien nhanVien) {

        jtfMaNhanVien.setText(nhanVien.getMaNV());
        jtfHoTen.setText(nhanVien.getHoTenNV());
        jdcNgaySinh.setDate(nhanVien.getNgaySinh());
        if (nhanVien.getGioiTinh().equalsIgnoreCase("Nam")) {
            jrdGTNam.setSelected(true);
        } else if (nhanVien.getGioiTinh().equalsIgnoreCase("Nữ")) {
            jrdGTNu.setSelected(true);
        } else {
            jrdGTKhac.setSelected(true);
        }
        jdcNgayVaoLam.setDate(nhanVien.getNgayVaoLam());
        jtfDienThoai.setText(nhanVien.getDienThoai());
        jtfEmail.setText(nhanVien.getEmail());
        jtaDiaChi.setText(nhanVien.getDiaChi());
    }

    public boolean kiemTra() {
        if (jtfMaNhanVien.getText().length() == 0 || jtfHoTen.getText().length() == 0
                || jdcNgaySinh.getDate() == null || jdcNgayVaoLam.getDate() == null || jtfDienThoai.getText().length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void insert() {
        try {
            Connection cons = DBConnect.getConnection();
            String sql = "Insert into NhanVien(MaNV, HoTenNV, NgaySinh, GioiTinh, NgayVaoLam, DienThoai, Email, DiaChi) Values(?,?,convert(date,?),?,convert(date,?),?,?,?)";
            PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, jtfMaNhanVien.getText());
            ps.setString(2, jtfHoTen.getText());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ns = sdf.format(jdcNgaySinh.getDate());
            ps.setString(3, ns);
            if (jrdGTNam.isSelected()) {
                ps.setString(4, "Nam");
            } else if (jrdGTNu.isSelected()) {
                ps.setString(4, "Nữ");
            } else {
                ps.setString(4, "Khác");
            }
            String nvl = sdf.format(jdcNgayVaoLam.getDate());
            ps.setString(5, nvl);
            ps.setString(6, jtfDienThoai.getText());
            if (jtfEmail.getText().length() == 0) {
                ps.setString(7, null);
            } else {
                ps.setString(7, jtfEmail.getText());
            }
            if (jtaDiaChi.getText().length() == 0) {
                ps.setString(8, null);
            } else {
                ps.setString(8, jtaDiaChi.getText());
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
            String maNv = jtfMaNhanVien.getText();
            String sql = "Update NhanVien set MaNV = ?, HoTenNV = ?, NgaySinh = ?, GioiTinh = ?, "
                    + "NgayVaoLam = ?, DienThoai = ?, Email = ?, DiaChi = ? where MaNV = '" + maNv + "'";
            PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, jtfMaNhanVien.getText());
            ps.setString(2, jtfHoTen.getText());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ns = sdf.format(jdcNgaySinh.getDate());
            ps.setString(3, ns);
            if (jrdGTNam.isSelected()) {
                ps.setString(4, "Nam");
            } else if (jrdGTNu.isSelected()) {
                ps.setString(4, "Nữ");
            } else {
                ps.setString(4, "Khác");
            }
            String nvl = sdf.format(jdcNgayVaoLam.getDate());
            ps.setString(5, nvl);
            ps.setString(6, jtfDienThoai.getText());
            if (jtfEmail.getText().length() == 0) {
                ps.setString(7, null);
            } else {
                ps.setString(7, jtfEmail.getText());
            }
            if (jtaDiaChi.getText().length() == 0) {
                ps.setString(8, null);
            } else {
                ps.setString(8, jtaDiaChi.getText());
            }
            ps.executeUpdate();

            ps.close();
            cons.close();

            JOptionPane.showMessageDialog(this, "Đã cập nhật thành công!");
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void delete(String maNv) {
        int opt = JOptionPane.showConfirmDialog(null, "Bạn muốn xóa nhân viên này? \nNếu bạn xóa một nhân viên thì mọi hóa đơn được nhân viên đó lập cũng sẽ bị xóa hết.", "Xóa nhân viên", JOptionPane.YES_NO_OPTION);
        if (opt == 0) {
            try {
                Connection cons = DBConnect.getConnection();
                String sql = "Delete from NhanVien where MaNV = '" + maNv + "'";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.executeUpdate();

                ps.close();
                cons.close();

                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                NhanVienJPanel.hienThiTable();
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
        jpnView = new javax.swing.JPanel();
        jlbMaNhanVien = new javax.swing.JLabel();
        jtfMaNhanVien = new javax.swing.JTextField();
        jlbDienThoai = new javax.swing.JLabel();
        jtfDienThoai = new javax.swing.JTextField();
        jlbHoTen = new javax.swing.JLabel();
        jtfHoTen = new javax.swing.JTextField();
        jlbEmail = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();
        jlbDiaChi = new javax.swing.JLabel();
        jlbNgaySinh = new javax.swing.JLabel();
        jlbGioiTinh = new javax.swing.JLabel();
        jlbNgayVaoLam = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaDiaChi = new javax.swing.JTextArea();
        jdcNgaySinh = new com.toedter.calendar.JDateChooser();
        jdcNgayVaoLam = new com.toedter.calendar.JDateChooser();
        jrdGTNam = new javax.swing.JRadioButton();
        jrdGTNu = new javax.swing.JRadioButton();
        jrdGTKhac = new javax.swing.JRadioButton();
        jbnLuu = new javax.swing.JButton();
        jbnXoa = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jpnView.setBackground(new java.awt.Color(255, 255, 255));
        jpnView.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin nhân viên", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 19))); // NOI18N

        jlbMaNhanVien.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbMaNhanVien.setText("Mã nhân viên");

        jtfMaNhanVien.setEditable(false);
        jtfMaNhanVien.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jtfMaNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfMaNhanVienActionPerformed(evt);
            }
        });

        jlbDienThoai.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbDienThoai.setText("Điện thoại");

        jtfDienThoai.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbHoTen.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbHoTen.setText("Họ và tên");

        jtfHoTen.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbEmail.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbEmail.setText("Email");

        jtfEmail.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbDiaChi.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbDiaChi.setText("Địa chỉ");

        jlbNgaySinh.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbNgaySinh.setText("Ngày sinh");

        jlbGioiTinh.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbGioiTinh.setText("Giới tính");

        jlbNgayVaoLam.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbNgayVaoLam.setText("Ngày vào làm");

        jtaDiaChi.setColumns(20);
        jtaDiaChi.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jtaDiaChi.setRows(5);
        jScrollPane1.setViewportView(jtaDiaChi);

        jdcNgaySinh.setDateFormatString("dd-MM-yyyy");
        jdcNgaySinh.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jdcNgayVaoLam.setDateFormatString("dd-MM-yyyy");
        jdcNgayVaoLam.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jrdGTNam.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jrdGTNam);
        jrdGTNam.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdGTNam.setSelected(true);
        jrdGTNam.setText("Nam");

        jrdGTNu.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jrdGTNu);
        jrdGTNu.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdGTNu.setText("Nữ");

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
                .addGap(32, 32, 32)
                .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnViewLayout.createSequentialGroup()
                        .addComponent(jlbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jrdGTNam, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jrdGTNu, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jrdGTKhac, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jpnViewLayout.createSequentialGroup()
                        .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpnViewLayout.createSequentialGroup()
                                .addComponent(jlbNgayVaoLam, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jdcNgayVaoLam, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpnViewLayout.createSequentialGroup()
                                .addComponent(jlbNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jdcNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jpnViewLayout.createSequentialGroup()
                                .addComponent(jlbMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfMaNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
                            .addGroup(jpnViewLayout.createSequentialGroup()
                                .addComponent(jlbHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfHoTen)))
                        .addGap(74, 74, 74)
                        .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbDienThoai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlbEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlbDiaChi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfDienThoai, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                    .addComponent(jtfEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
                .addGap(28, 28, 28))
        );
        jpnViewLayout.setVerticalGroup(
            jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnViewLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnViewLayout.createSequentialGroup()
                        .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlbNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlbDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jdcNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jlbGioiTinh, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(jrdGTNam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jrdGTNu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jrdGTKhac, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlbNgayVaoLam, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(jdcNgayVaoLam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(61, Short.MAX_VALUE))
        );

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jpnView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jbnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(jpnView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14))
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

    private void jtfMaNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfMaNhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfMaNhanVienActionPerformed

    private void jbnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnLuuActionPerformed
        if (!kiemTra()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập dữ liệu bắt buộc!");
        } else {
            try {
                Connection cons = DBConnect.getConnection();
                String sql = "select MaNV from NhanVien where MaNV = ?";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, jtfMaNhanVien.getText());

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
            NhanVienJPanel.hienThiTable();
        }
    }//GEN-LAST:event_jbnLuuActionPerformed

    private void jbnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnXoaActionPerformed
        if (!kiemTra()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
        } else {
            delete(jtfMaNhanVien.getText());
        }
    }//GEN-LAST:event_jbnXoaActionPerformed

    private void jrdGTKhacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdGTKhacActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jrdGTKhacActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JButton jbnLuu;
    public javax.swing.JButton jbnXoa;
    public com.toedter.calendar.JDateChooser jdcNgaySinh;
    public com.toedter.calendar.JDateChooser jdcNgayVaoLam;
    private javax.swing.JLabel jlbDiaChi;
    private javax.swing.JLabel jlbDienThoai;
    private javax.swing.JLabel jlbEmail;
    private javax.swing.JLabel jlbGioiTinh;
    private javax.swing.JLabel jlbHoTen;
    private javax.swing.JLabel jlbMaNhanVien;
    private javax.swing.JLabel jlbNgaySinh;
    private javax.swing.JLabel jlbNgayVaoLam;
    private javax.swing.JPanel jpnView;
    private javax.swing.JRadioButton jrdGTKhac;
    public javax.swing.JRadioButton jrdGTNam;
    public javax.swing.JRadioButton jrdGTNu;
    public javax.swing.JTextArea jtaDiaChi;
    public javax.swing.JTextField jtfDienThoai;
    public javax.swing.JTextField jtfEmail;
    public javax.swing.JTextField jtfHoTen;
    public javax.swing.JTextField jtfMaNhanVien;
    // End of variables declaration//GEN-END:variables
}
