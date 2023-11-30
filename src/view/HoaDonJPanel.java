/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import dao.ChiTietHoaDonDao;
import dao.DBConnect;
import dao.HoaDonDao;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.ChiTietHoaDon;
import model.HoaDon;
import model.SanPham;

/**
 *
 * @author Administrator
 */
public class HoaDonJPanel extends javax.swing.JPanel {

    /**
     * Creates new form HoaDonJPanel
     */
    DefaultTableModel model = new DefaultTableModel();
    TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<DefaultTableModel>(model);

    public HoaDonJPanel() {
        initComponents();

        jtbHoaDon.getTableHeader().setBackground(new Color(0, 113, 61));
        jtbHoaDon.getTableHeader().setForeground(Color.white);
        jtbHoaDon.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        jtbHoaDon.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 17));
        jtbHoaDon.getTableHeader().setPreferredSize(new Dimension(100, 35));
        jtbHoaDon.setRowHeight(35);

        jtbChiTietHoaDon.getTableHeader().setBackground(new Color(0, 113, 61));
        jtbChiTietHoaDon.getTableHeader().setForeground(Color.white);
        jtbChiTietHoaDon.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        jtbChiTietHoaDon.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 17));
        jtbChiTietHoaDon.getTableHeader().setPreferredSize(new Dimension(100, 35));
        jtbChiTietHoaDon.setRowHeight(35);

        addPlaceholderStyle(jtfTimKiem);

        layDuLieuTable();
        sort();
    }

    public void layDuLieuTable() {
        HoaDonDao hoaDonDao = new HoaDonDao();
        List<HoaDon> listHoaDon = hoaDonDao.getList();
        model = (DefaultTableModel) jtbHoaDon.getModel();
        model.setRowCount(0);
        for (HoaDon o : listHoaDon) {
            model.addRow(new Object[]{
                o.getMaHD(),
                o.getMaNV(),
                o.getMaKH(),
                o.getNgayLapHD(),
                o.getTongTien()});
        }
    }

    public void sort() {
        rowSorter = new TableRowSorter<DefaultTableModel>(model);
        jtbHoaDon.setRowSorter(rowSorter);
    }

    public void search() {
        model = (DefaultTableModel) jtbHoaDon.getModel();
        rowSorter = new TableRowSorter<>(model);
        jtbHoaDon.setRowSorter(rowSorter);
        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + jtfTimKiem.getText()));
    }

    public void addPlaceholderStyle(JTextField jTextField) {
        Font font = jTextField.getFont();
        font = font.deriveFont(Font.ITALIC);
        jTextField.setFont(font);
        jTextField.setForeground(Color.GRAY);
    }

    public void removePlaceholderStyle(JTextField jTextField) {
        Font font = jTextField.getFont();
        font = font.deriveFont(Font.PLAIN);
        jTextField.setFont(font);
        jTextField.setForeground(Color.BLACK);
    }

    public void layDuLieuChiTietHoaDon(String maHD) {
        ChiTietHoaDonDao chiTietHoaDonDao = new ChiTietHoaDonDao();
        List<ChiTietHoaDon> listChiTietHoaDon = chiTietHoaDonDao.getList(maHD);
        model = (DefaultTableModel) jtbChiTietHoaDon.getModel();
        model.setRowCount(0);
        for (ChiTietHoaDon i : listChiTietHoaDon) {
            model.addRow(new Object[]{
                i.getMaHD(),
                i.getMaSP(),
                i.getTenSP(),
                i.getDonGia(),
                i.getSoLuongDat(),
                i.getThanhTien()
            });
        }
    }

    public void updateHoaDon() {
        if (jtfMaHD.getText().length() == 0 || jtfMaNV.getText().length() == 0 || jtfMaKH.getText().length() == 0 || jdcNgayLap.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập dữ liệu bắt buộc!");
        } else {
            try {
                Connection cons = DBConnect.getConnection();
                String sql = "Update HoaDon set MaNV = ?, MaKH = ?, NgayLapHD = ? where MaHD = ?";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setString(1, jtfMaNV.getText());
                ps.setString(2, jtfMaKH.getText());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String ngayLap = sdf.format(jdcNgayLap.getDate());
                ps.setString(3, ngayLap);
                ps.setString(4, jtfMaHD.getText());
                ps.executeUpdate();

                ps.close();
                cons.close();

                JOptionPane.showMessageDialog(this, "Đã cập nhật thành công!");

                layDuLieuTable();
                jtfMaHD.setText("");
                jtfMaNV.setText("");
                jtfMaKH.setText("");
                jdcNgayLap.setDate(null);
                jtfTongTien.setText("");
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public void deleteHoaDon() {
        int opt = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa hóa đơn này?", "Xóa hóa đơn", JOptionPane.YES_NO_OPTION);
        if (opt == 0) {
            try {
                Connection cons = DBConnect.getConnection();
                String sql = "Delete from HoaDon where MaHD = '" + jtfMaHD.getText() + "'";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.executeUpdate();

                ps.close();
                cons.close();

                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");

                layDuLieuTable();
                model = (DefaultTableModel) jtbChiTietHoaDon.getModel();
                model.setRowCount(0);
                jtfMaHD.setText("");
                jtfMaNV.setText("");
                jtfMaKH.setText("");
                jdcNgayLap.setDate(null);
                jtfTongTien.setText("");
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public static void hienThongTinSanPham(SanPham sanPham) {
        jtfCTHD_MaSP.setText(sanPham.getMaSP());
        jtfCTHD_DonGia.setText(Float.toString(sanPham.getGiaBan()));
    }

    public void tinhTongTien() {
        model = (DefaultTableModel) jtbChiTietHoaDon.getModel();
        float tongTien = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            tongTien = tongTien + Float.parseFloat(model.getValueAt(i, 5).toString());
        }
        jtfTongTien.setText(Float.toString(tongTien));
    }

    public void insertCTHD() {
        if (jtfMaHD.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần thêm sản phẩm!");
        } else if (jtfCTHD_MaSP.getText().length() == 0 && (int) jsnCTHD_SoLuong.getValue() > 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!");
        } else if (jtfCTHD_MaSP.getText().length() != 0 && (int) jsnCTHD_SoLuong.getValue() <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!");
        } else {
            try {
                Connection cons = DBConnect.getConnection();
                String sql = "Insert into ChiTietHoaDon(MaHD, MaSP, SLDat) Values(?,?,?)";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setString(1, jtfMaHD.getText());
                ps.setString(2, jtfCTHD_MaSP.getText());
                ps.setString(3, jsnCTHD_SoLuong.getValue().toString());
                ps.executeUpdate();

                ps.close();
                cons.close();

                JOptionPane.showMessageDialog(this, "Thêm sản phẩm vào hóa đơn thành công");

                layDuLieuChiTietHoaDon(jtfMaHD.getText());
                jtfCTHD_MaHD.setText("");
                jtfCTHD_MaSP.setText("");
                jtfCTHD_DonGia.setText("");
                jsnCTHD_SoLuong.setValue(0);
                layDuLieuTable();
                tinhTongTien();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
                JOptionPane.showMessageDialog(this, "Thất bại! Sản phẩm đã có trong hóa đơn");
            }
        }
    }

    public void updateCTHD() {
        if (jtfMaHD.getText().length() == 0 || jtfCTHD_MaSP.getText().length() == 0 || (int) jsnCTHD_SoLuong.getValue() <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần cập nhật!");
        } else if (jtfCTHD_MaSP.getText().length() == 0 && (int) jsnCTHD_SoLuong.getValue() > 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!");
        } else if (jtfCTHD_MaSP.getText().length() != 0 && (int) jsnCTHD_SoLuong.getValue() <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!");
        } else {
            try {
                Connection cons = DBConnect.getConnection();
                String sql = "Update ChiTietHoaDon set SLDat = ? where MaHD = ? and  MaSP = ?";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setString(1, jsnCTHD_SoLuong.getValue().toString());
                ps.setString(2, jtfMaHD.getText());
                ps.setString(3, jtfCTHD_MaSP.getText());
                ps.executeUpdate();

                ps.close();
                cons.close();

                JOptionPane.showMessageDialog(this, "Cập nhật số lượng đặt thành công");

                layDuLieuChiTietHoaDon(jtfMaHD.getText());
                jtfCTHD_MaHD.setText("");
                jtfCTHD_MaSP.setText("");
                jtfCTHD_DonGia.setText("");
                jsnCTHD_SoLuong.setValue(0);
                layDuLieuTable();
                tinhTongTien();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public void deleteCTHD() {
        int opt = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa sản phẩm này khỏi hóa đơn?", "Xóa sản phẩm khỏi hóa đơn", JOptionPane.YES_NO_OPTION);
        if (opt == 0) {
            try {
                Connection cons = DBConnect.getConnection();
                String sql = "Delete from ChiTietHoaDon where MaHD = '" + jtfCTHD_MaHD.getText() + "' and MaSP = '" + jtfCTHD_MaSP.getText() + "'";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.executeUpdate();

                ps.close();
                cons.close();

                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");

                layDuLieuChiTietHoaDon(jtfMaHD.getText());
                jtfCTHD_MaHD.setText("");
                jtfCTHD_MaSP.setText("");
                jtfCTHD_DonGia.setText("");
                jsnCTHD_SoLuong.setValue(0);
                layDuLieuTable();
                tinhTongTien();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
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

        jLabel5 = new javax.swing.JLabel();
        jlbHoaDon = new javax.swing.JLabel();
        jpnViewHoaDon = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbHoaDon = new javax.swing.JTable();
        jtfTimKiem = new javax.swing.JTextField();
        jpnThongTinHoaDon = new javax.swing.JPanel();
        jlbMaHD = new javax.swing.JLabel();
        jtfMaHD = new javax.swing.JTextField();
        jlbMaNV = new javax.swing.JLabel();
        jtfMaNV = new javax.swing.JTextField();
        jlbMaKH = new javax.swing.JLabel();
        jtfMaKH = new javax.swing.JTextField();
        jlbNgayLap = new javax.swing.JLabel();
        jdcNgayLap = new com.toedter.calendar.JDateChooser();
        jlbTongTien = new javax.swing.JLabel();
        jtfTongTien = new javax.swing.JTextField();
        jbnSuaHD = new javax.swing.JButton();
        jbnXoaHD = new javax.swing.JButton();
        jlbChiTietHoaDon = new javax.swing.JLabel();
        jpnViewChiTietHoaDon = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtbChiTietHoaDon = new javax.swing.JTable();
        jpnThongTinCTHD = new javax.swing.JPanel();
        jlbCTHD_MaHD = new javax.swing.JLabel();
        jtfCTHD_MaHD = new javax.swing.JTextField();
        jlbCTHD_MaSP = new javax.swing.JLabel();
        jtfCTHD_MaSP = new javax.swing.JTextField();
        jbnCTHD_ChonSP = new javax.swing.JButton();
        jlbCTHD_DonGia = new javax.swing.JLabel();
        jtfCTHD_DonGia = new javax.swing.JTextField();
        jlbCTHD_SoLuong = new javax.swing.JLabel();
        jsnCTHD_SoLuong = new javax.swing.JSpinner();
        jbnCTHD_Them = new javax.swing.JButton();
        jbnCTHD_Sua = new javax.swing.JButton();
        jbnCTHD_Xoa = new javax.swing.JButton();

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel5.setText("jLabel1");

        setBackground(new java.awt.Color(255, 255, 255));

        jlbHoaDon.setBackground(new java.awt.Color(255, 255, 255));
        jlbHoaDon.setFont(new java.awt.Font("Sitka Text", 1, 29)); // NOI18N
        jlbHoaDon.setForeground(new java.awt.Color(0, 113, 61));
        jlbHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbHoaDon.setText("QUẢN LÝ HÓA ĐƠN");
        jlbHoaDon.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jpnViewHoaDon.setBackground(new java.awt.Color(255, 255, 255));

        jtbHoaDon.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jtbHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Hóa Đơn", "Mã Nhân Viên", "Mã Khách Hàng", "Ngày Lập", "Tổng Tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtbHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtbHoaDon);

        javax.swing.GroupLayout jpnViewHoaDonLayout = new javax.swing.GroupLayout(jpnViewHoaDon);
        jpnViewHoaDon.setLayout(jpnViewHoaDonLayout);
        jpnViewHoaDonLayout.setHorizontalGroup(
            jpnViewHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jpnViewHoaDonLayout.setVerticalGroup(
            jpnViewHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jtfTimKiem.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jtfTimKiem.setText("Tìm kiếm");
        jtfTimKiem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfTimKiemFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfTimKiemFocusLost(evt);
            }
        });
        jtfTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfTimKiemKeyReleased(evt);
            }
        });

        jpnThongTinHoaDon.setBackground(new java.awt.Color(255, 255, 255));

        jlbMaHD.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbMaHD.setText("Mã HĐ");

        jtfMaHD.setEditable(false);
        jtfMaHD.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbMaNV.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbMaNV.setText("Mã NV");

        jtfMaNV.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbMaKH.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbMaKH.setText("Mã KH");

        jtfMaKH.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbNgayLap.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbNgayLap.setText("Ngày Lập");

        jdcNgayLap.setBackground(new java.awt.Color(255, 255, 255));
        jdcNgayLap.setDateFormatString("dd-MM-yyyy");
        jdcNgayLap.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbTongTien.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbTongTien.setText("Tổng Tiền");

        jtfTongTien.setEditable(false);
        jtfTongTien.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jbnSuaHD.setBackground(new java.awt.Color(157, 205, 239));
        jbnSuaHD.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnSuaHD.setText("Sửa");
        jbnSuaHD.setBorder(null);
        jbnSuaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnSuaHDActionPerformed(evt);
            }
        });

        jbnXoaHD.setBackground(new java.awt.Color(157, 205, 239));
        jbnXoaHD.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnXoaHD.setText("Xóa");
        jbnXoaHD.setBorder(null);
        jbnXoaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnXoaHDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnThongTinHoaDonLayout = new javax.swing.GroupLayout(jpnThongTinHoaDon);
        jpnThongTinHoaDon.setLayout(jpnThongTinHoaDonLayout);
        jpnThongTinHoaDonLayout.setHorizontalGroup(
            jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnThongTinHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnThongTinHoaDonLayout.createSequentialGroup()
                        .addComponent(jlbMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfMaHD))
                    .addGroup(jpnThongTinHoaDonLayout.createSequentialGroup()
                        .addComponent(jlbMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfMaNV, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnThongTinHoaDonLayout.createSequentialGroup()
                        .addComponent(jlbMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfMaKH, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))
                    .addGroup(jpnThongTinHoaDonLayout.createSequentialGroup()
                        .addComponent(jbnSuaHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbnXoaHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpnThongTinHoaDonLayout.createSequentialGroup()
                        .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlbTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfTongTien)
                            .addComponent(jdcNgayLap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jpnThongTinHoaDonLayout.setVerticalGroup(
            jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnThongTinHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlbNgayLap, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(jdcNgayLap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbnSuaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbnXoaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jlbChiTietHoaDon.setFont(new java.awt.Font("Sitka Text", 1, 23)); // NOI18N
        jlbChiTietHoaDon.setForeground(new java.awt.Color(0, 113, 61));
        jlbChiTietHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbChiTietHoaDon.setText("CHI TIẾT HÓA ĐƠN");
        jlbChiTietHoaDon.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jpnViewChiTietHoaDon.setBackground(new java.awt.Color(255, 255, 255));

        jtbChiTietHoaDon.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jtbChiTietHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Hóa Đơn", "Mã Sản Phẩm", "Tên Sản Phẩm", "Đơn Giá", "Số Lượng", "Thành Tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtbChiTietHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbChiTietHoaDonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jtbChiTietHoaDon);

        javax.swing.GroupLayout jpnViewChiTietHoaDonLayout = new javax.swing.GroupLayout(jpnViewChiTietHoaDon);
        jpnViewChiTietHoaDon.setLayout(jpnViewChiTietHoaDonLayout);
        jpnViewChiTietHoaDonLayout.setHorizontalGroup(
            jpnViewChiTietHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );
        jpnViewChiTietHoaDonLayout.setVerticalGroup(
            jpnViewChiTietHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jpnThongTinCTHD.setBackground(new java.awt.Color(255, 255, 255));

        jlbCTHD_MaHD.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbCTHD_MaHD.setText("Mã HĐ");

        jtfCTHD_MaHD.setEditable(false);
        jtfCTHD_MaHD.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbCTHD_MaSP.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbCTHD_MaSP.setText("Mã SP");

        jtfCTHD_MaSP.setEditable(false);
        jtfCTHD_MaSP.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jbnCTHD_ChonSP.setBackground(new java.awt.Color(157, 205, 239));
        jbnCTHD_ChonSP.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnCTHD_ChonSP.setText("...");
        jbnCTHD_ChonSP.setBorder(null);
        jbnCTHD_ChonSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnCTHD_ChonSPActionPerformed(evt);
            }
        });

        jlbCTHD_DonGia.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbCTHD_DonGia.setText("Đơn Giá");

        jtfCTHD_DonGia.setEditable(false);
        jtfCTHD_DonGia.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbCTHD_SoLuong.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbCTHD_SoLuong.setText("Số Lượng");

        jsnCTHD_SoLuong.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jsnCTHD_SoLuong.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jbnCTHD_Them.setBackground(new java.awt.Color(157, 205, 239));
        jbnCTHD_Them.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnCTHD_Them.setText("Thêm");
        jbnCTHD_Them.setBorder(null);
        jbnCTHD_Them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnCTHD_ThemActionPerformed(evt);
            }
        });

        jbnCTHD_Sua.setBackground(new java.awt.Color(157, 205, 239));
        jbnCTHD_Sua.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnCTHD_Sua.setText("Sửa");
        jbnCTHD_Sua.setBorder(null);
        jbnCTHD_Sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnCTHD_SuaActionPerformed(evt);
            }
        });

        jbnCTHD_Xoa.setBackground(new java.awt.Color(157, 205, 239));
        jbnCTHD_Xoa.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnCTHD_Xoa.setText("Xóa");
        jbnCTHD_Xoa.setBorder(null);
        jbnCTHD_Xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnCTHD_XoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnThongTinCTHDLayout = new javax.swing.GroupLayout(jpnThongTinCTHD);
        jpnThongTinCTHD.setLayout(jpnThongTinCTHDLayout);
        jpnThongTinCTHDLayout.setHorizontalGroup(
            jpnThongTinCTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnThongTinCTHDLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnThongTinCTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnThongTinCTHDLayout.createSequentialGroup()
                        .addComponent(jlbCTHD_MaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfCTHD_MaHD))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnThongTinCTHDLayout.createSequentialGroup()
                        .addComponent(jlbCTHD_DonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfCTHD_DonGia))
                    .addGroup(jpnThongTinCTHDLayout.createSequentialGroup()
                        .addGroup(jpnThongTinCTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpnThongTinCTHDLayout.createSequentialGroup()
                                .addComponent(jlbCTHD_SoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jsnCTHD_SoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jpnThongTinCTHDLayout.createSequentialGroup()
                                .addComponent(jlbCTHD_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfCTHD_MaSP, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbnCTHD_ChonSP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnThongTinCTHDLayout.createSequentialGroup()
                        .addComponent(jbnCTHD_Them, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbnCTHD_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbnCTHD_Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jpnThongTinCTHDLayout.setVerticalGroup(
            jpnThongTinCTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnThongTinCTHDLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnThongTinCTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbCTHD_MaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfCTHD_MaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnThongTinCTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpnThongTinCTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlbCTHD_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfCTHD_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jbnCTHD_ChonSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnThongTinCTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbCTHD_DonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfCTHD_DonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnThongTinCTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlbCTHD_SoLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(jsnCTHD_SoLuong))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnThongTinCTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbnCTHD_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbnCTHD_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbnCTHD_Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbChiTietHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpnViewHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtfTimKiem)
                            .addComponent(jpnThongTinHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpnViewChiTietHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpnThongTinCTHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jlbHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jtfTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpnThongTinHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpnViewHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbChiTietHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpnViewChiTietHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnThongTinCTHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addComponent(jlbHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(648, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtbHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbHoaDonMouseClicked
        int row = jtbHoaDon.getSelectedRow();
        jtfMaHD.setText(jtbHoaDon.getValueAt(row, 0).toString());
        jtfMaNV.setText(jtbHoaDon.getValueAt(row, 1).toString());
        jtfMaKH.setText(jtbHoaDon.getValueAt(row, 2).toString());
        jdcNgayLap.setDate((Date) jtbHoaDon.getValueAt(row, 3));
        jtfTongTien.setText(jtbHoaDon.getValueAt(row, 4).toString());

        layDuLieuChiTietHoaDon(jtbHoaDon.getValueAt(row, 0).toString());
    }//GEN-LAST:event_jtbHoaDonMouseClicked

    private void jtfTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfTimKiemKeyReleased
        search();
    }//GEN-LAST:event_jtfTimKiemKeyReleased

    private void jbnSuaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnSuaHDActionPerformed
        updateHoaDon();
    }//GEN-LAST:event_jbnSuaHDActionPerformed

    private void jbnXoaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnXoaHDActionPerformed
        if (jtfMaHD.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa!");
        } else {
            deleteHoaDon();
        }
    }//GEN-LAST:event_jbnXoaHDActionPerformed

    private void jbnCTHD_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnCTHD_ThemActionPerformed
        insertCTHD();
    }//GEN-LAST:event_jbnCTHD_ThemActionPerformed

    private void jbnCTHD_SuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnCTHD_SuaActionPerformed
        updateCTHD();
    }//GEN-LAST:event_jbnCTHD_SuaActionPerformed

    private void jtbChiTietHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbChiTietHoaDonMouseClicked
        int row = jtbChiTietHoaDon.getSelectedRow();
        jtfCTHD_MaHD.setText(jtbChiTietHoaDon.getValueAt(row, 0).toString());
        jtfCTHD_MaSP.setText(jtbChiTietHoaDon.getValueAt(row, 1).toString());
        jtfCTHD_DonGia.setText(jtbChiTietHoaDon.getValueAt(row, 3).toString());
        jsnCTHD_SoLuong.setValue((int) jtbChiTietHoaDon.getValueAt(row, 4));
    }//GEN-LAST:event_jtbChiTietHoaDonMouseClicked

    private void jtfTimKiemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfTimKiemFocusGained
        if (jtfTimKiem.getText().equals("Tìm kiếm")) {
            jtfTimKiem.setText(null);
            jtfTimKiem.requestFocus();
            removePlaceholderStyle(jtfTimKiem);
        }
    }//GEN-LAST:event_jtfTimKiemFocusGained

    private void jtfTimKiemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfTimKiemFocusLost
        if (jtfTimKiem.getText().length() == 0) {
            addPlaceholderStyle(jtfTimKiem);
            jtfTimKiem.setText("Tìm kiếm");
        }
    }//GEN-LAST:event_jtfTimKiemFocusLost

    private void jbnCTHD_XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnCTHD_XoaActionPerformed
        int row = jtbChiTietHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!");
        } else {
            deleteCTHD();
        }
    }//GEN-LAST:event_jbnCTHD_XoaActionPerformed

    private void jbnCTHD_ChonSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnCTHD_ChonSPActionPerformed
        SanPhamJFrame sanPhamJFrame = new SanPhamJFrame();
        sanPhamJFrame.setTitle("Danh Sách Sản phẩm");
        sanPhamJFrame.setVisible(true);
        sanPhamJFrame.setResizable(false);
        sanPhamJFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_jbnCTHD_ChonSPActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbnCTHD_ChonSP;
    private javax.swing.JButton jbnCTHD_Sua;
    private javax.swing.JButton jbnCTHD_Them;
    private javax.swing.JButton jbnCTHD_Xoa;
    private javax.swing.JButton jbnSuaHD;
    private javax.swing.JButton jbnXoaHD;
    private com.toedter.calendar.JDateChooser jdcNgayLap;
    private javax.swing.JLabel jlbCTHD_DonGia;
    private javax.swing.JLabel jlbCTHD_MaHD;
    private javax.swing.JLabel jlbCTHD_MaSP;
    private javax.swing.JLabel jlbCTHD_SoLuong;
    private javax.swing.JLabel jlbChiTietHoaDon;
    private javax.swing.JLabel jlbHoaDon;
    private javax.swing.JLabel jlbMaHD;
    private javax.swing.JLabel jlbMaKH;
    private javax.swing.JLabel jlbMaNV;
    private javax.swing.JLabel jlbNgayLap;
    private javax.swing.JLabel jlbTongTien;
    private javax.swing.JPanel jpnThongTinCTHD;
    private javax.swing.JPanel jpnThongTinHoaDon;
    private javax.swing.JPanel jpnViewChiTietHoaDon;
    private javax.swing.JPanel jpnViewHoaDon;
    private javax.swing.JSpinner jsnCTHD_SoLuong;
    private javax.swing.JTable jtbChiTietHoaDon;
    private javax.swing.JTable jtbHoaDon;
    private static javax.swing.JTextField jtfCTHD_DonGia;
    private javax.swing.JTextField jtfCTHD_MaHD;
    private static javax.swing.JTextField jtfCTHD_MaSP;
    private javax.swing.JTextField jtfMaHD;
    private javax.swing.JTextField jtfMaKH;
    private javax.swing.JTextField jtfMaNV;
    private javax.swing.JTextField jtfTimKiem;
    private javax.swing.JTextField jtfTongTien;
    // End of variables declaration//GEN-END:variables
}
