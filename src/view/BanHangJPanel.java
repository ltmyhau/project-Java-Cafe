/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import dao.DBConnect;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.ChiTietHoaDon;
import model.SanPham;

/**
 *
 * @author Administrator
 */
public class BanHangJPanel extends javax.swing.JPanel {

    /**
     * Creates new form HoaDonJPanel
     */
    DefaultTableModel model = new DefaultTableModel();

    public BanHangJPanel() {
        initComponents();

        jtbHoaDon_SanPham.getTableHeader().setBackground(new Color(0, 113, 61));
        jtbHoaDon_SanPham.getTableHeader().setForeground(Color.white);
        jtbHoaDon_SanPham.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        jtbHoaDon_SanPham.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 17));
        jtbHoaDon_SanPham.getTableHeader().setPreferredSize(new Dimension(100, 35));
        jtbHoaDon_SanPham.setRowHeight(35);
        jtfTongTien.setText("0");
    }

    public static ImageIcon ResizeImage(String imagePath, byte[] pic) {
        ImageIcon myImage = null;

        if (imagePath != null) {
            myImage = new ImageIcon(imagePath);
        } else {
            myImage = new ImageIcon(pic);
        }

        Image img = myImage.getImage();
        Image img2 = img.getScaledInstance(jlbHinhAnh.getWidth(), jlbHinhAnh.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(img2);
        return image;
    }

    public static void hienThongTin(SanPham sanPham) {
        jtfMaSP.setText(sanPham.getMaSP());
        jtfTenSP.setText(sanPham.getTenSP());
        jtfDonGia.setText(Float.toString(sanPham.getGiaBan()));
        if (sanPham.getHinhAnh() != null) {
            jlbHinhAnh.setIcon(ResizeImage(null, sanPham.getHinhAnh()));
        } else {
            jlbHinhAnh.setIcon(null);
        }
        jsnSoLuong.setValue(1);
    }

    public void themDongTable() {
        model = (DefaultTableModel) jtbHoaDon_SanPham.getModel();
        float donGia = Float.parseFloat(jtfDonGia.getText());
        int soLuong = (int) jsnSoLuong.getValue();
        float thanhTien = donGia * soLuong;

        model.addRow(new Object[]{
            jtfMaSP.getText(),
            jtfTenSP.getText(),
            jtfDonGia.getText(),
            jsnSoLuong.getValue(),
            thanhTien,});
    }

    public void lamMoi() {
        model = (DefaultTableModel) jtbHoaDon_SanPham.getModel();
        model.setRowCount(0);

        jtfMaHD.setText("");
        jtfMaNV.setText("");
        jtfMaKH.setText("");
        jdcNgayLap.setDate(null);
        jtfTongTien.setText("0");
        jtfMaSP.setText("");
        jtfTenSP.setText("");
        jtfDonGia.setText("");
        jsnSoLuong.setValue(0);
        jlbHinhAnh.setIcon(null);
    }

    public void tinhTongTien() {
        model = (DefaultTableModel) jtbHoaDon_SanPham.getModel();
        float tongTien = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            tongTien = tongTien + Float.parseFloat(model.getValueAt(i, 4).toString());
        }
        NumberFormat df = new DecimalFormat("#,###");
        jtfTongTien.setText(df.format(tongTien));
    }

    public void insertCTHD(ChiTietHoaDon chiTietHoaDon) {
        try {
            Connection cons = DBConnect.getConnection();
            String sql = "Insert into ChiTietHoaDon(MaHD, MaSP, SLDat) Values(?,?,?)";
            PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, jtfMaHD.getText());
            ps.setString(2, chiTietHoaDon.getMaSP());
            ps.setString(3, Integer.toString(chiTietHoaDon.getSoLuongDat()));
            ps.executeUpdate();

            ps.close();
            cons.close();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    public void taoHoaDon() {
        if (jtfMaHD.getText().length() == 0 || jdcNgayLap.getDate() == null || jtfMaNV.getText().length() == 0 | jtfMaKH.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập dữ liệu bắt buộc!");
        } else if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng thêm sản phẩm vào chi tiết hóa đơn!");
        } else {
            try {
                Connection cons = DBConnect.getConnection();
                String sql = "Insert into HoaDon(MaHD, MaNV, MaKH, NgayLapHD) Values(?,?,?,convert(datetime,?))";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setString(1, jtfMaHD.getText());
                ps.setString(2, jtfMaNV.getText());
                ps.setString(3, jtfMaKH.getText());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String ngayLap = sdf.format(jdcNgayLap.getDate());
                ps.setString(4, ngayLap);
                ps.executeUpdate();

                ps.close();
                cons.close();

                for (int i = 0; i < model.getRowCount(); i++) {
                    ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
                    chiTietHoaDon.setMaHD(jtfMaHD.getText());
                    chiTietHoaDon.setMaSP(model.getValueAt(i, 0).toString());
                    chiTietHoaDon.setSoLuongDat((int) model.getValueAt(i, 3));
                    insertCTHD(chiTietHoaDon);
                }

                JOptionPane.showMessageDialog(this, "Thêm hóa đơn thành công!");
                lamMoi();

            } catch (SQLException ex) {
                System.out.println(ex.toString());
                JOptionPane.showMessageDialog(this, "Thêm hóa đơn thất bại!");
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

        jPanel1 = new javax.swing.JPanel();
        jpnThongTinHoaDon = new javax.swing.JPanel();
        jlbMaHD = new javax.swing.JLabel();
        jtfMaHD = new javax.swing.JTextField();
        jlbNgayLap = new javax.swing.JLabel();
        jdcNgayLap = new com.toedter.calendar.JDateChooser();
        jlbMaNV = new javax.swing.JLabel();
        jtfMaNV = new javax.swing.JTextField();
        jlbMaKH = new javax.swing.JLabel();
        jtfMaKH = new javax.swing.JTextField();
        jbnTaoHD = new javax.swing.JButton();
        jbnHuyHD = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbHoaDon_SanPham = new javax.swing.JTable();
        jpnThongTinSanPham = new javax.swing.JPanel();
        jlbMaSP = new javax.swing.JLabel();
        jtfMaSP = new javax.swing.JTextField();
        jbnChonSP = new javax.swing.JButton();
        jlbTenSP = new javax.swing.JLabel();
        jtfTenSP = new javax.swing.JTextField();
        jlbDonGia = new javax.swing.JLabel();
        jtfDonGia = new javax.swing.JTextField();
        jlbSoLuong = new javax.swing.JLabel();
        jsnSoLuong = new javax.swing.JSpinner();
        jbnThem = new javax.swing.JButton();
        jbnSua = new javax.swing.JButton();
        jbnXoa = new javax.swing.JButton();
        jlbTongTien = new javax.swing.JLabel();
        jtfTongTien = new javax.swing.JTextField();
        jlbHinhAnh = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jpnThongTinHoaDon.setBackground(new java.awt.Color(255, 255, 255));

        jlbMaHD.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbMaHD.setText("Mã hóa đơn");

        jtfMaHD.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbNgayLap.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbNgayLap.setText("Ngày lập");

        jdcNgayLap.setDateFormatString("dd-MM-yyyy");
        jdcNgayLap.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbMaNV.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbMaNV.setText("Mã nhân viên");

        jtfMaNV.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbMaKH.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbMaKH.setText("Mã khách hàng");

        jtfMaKH.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jbnTaoHD.setBackground(new java.awt.Color(157, 205, 239));
        jbnTaoHD.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnTaoHD.setText("Tạo hóa đơn");
        jbnTaoHD.setBorder(null);
        jbnTaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnTaoHDActionPerformed(evt);
            }
        });

        jbnHuyHD.setBackground(new java.awt.Color(204, 204, 204));
        jbnHuyHD.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnHuyHD.setText("Hủy hóa đơn");
        jbnHuyHD.setBorder(null);
        jbnHuyHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnHuyHDActionPerformed(evt);
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
                        .addComponent(jlbMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtfMaNV, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                    .addGroup(jpnThongTinHoaDonLayout.createSequentialGroup()
                        .addComponent(jlbMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtfMaHD, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlbNgayLap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlbMaKH, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfMaKH, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jdcNgayLap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37)
                .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbnTaoHD, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbnHuyHD, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        jpnThongTinHoaDonLayout.setVerticalGroup(
            jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnThongTinHoaDonLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnThongTinHoaDonLayout.createSequentialGroup()
                        .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jlbNgayLap, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(jdcNgayLap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jlbMaKH, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(jtfMaKH)))
                    .addGroup(jpnThongTinHoaDonLayout.createSequentialGroup()
                        .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbnTaoHD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jlbMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jtfMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jpnThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbnHuyHD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtfMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(16, 16, 16))
        );

        jtbHoaDon_SanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Sản Phẩm", "Tên Sản Phẩm", "Đơn Giá", "Số Lượng", "Thành Tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtbHoaDon_SanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbHoaDon_SanPhamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtbHoaDon_SanPham);

        jpnThongTinSanPham.setBackground(new java.awt.Color(255, 255, 255));

        jlbMaSP.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbMaSP.setText("Mã SP");

        jtfMaSP.setEditable(false);
        jtfMaSP.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jtfMaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfMaSPActionPerformed(evt);
            }
        });

        jbnChonSP.setBackground(new java.awt.Color(157, 205, 239));
        jbnChonSP.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jbnChonSP.setText("...");
        jbnChonSP.setBorder(null);
        jbnChonSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnChonSPActionPerformed(evt);
            }
        });

        jlbTenSP.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbTenSP.setText("Tên SP");

        jtfTenSP.setEditable(false);
        jtfTenSP.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbDonGia.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbDonGia.setText("Đơn giá");

        jtfDonGia.setEditable(false);
        jtfDonGia.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbSoLuong.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbSoLuong.setText("Số lượng");

        jsnSoLuong.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jsnSoLuong.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        javax.swing.GroupLayout jpnThongTinSanPhamLayout = new javax.swing.GroupLayout(jpnThongTinSanPham);
        jpnThongTinSanPham.setLayout(jpnThongTinSanPhamLayout);
        jpnThongTinSanPhamLayout.setHorizontalGroup(
            jpnThongTinSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnThongTinSanPhamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnThongTinSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jlbDonGia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlbTenSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlbMaSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlbSoLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jpnThongTinSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jtfTenSP, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpnThongTinSanPhamLayout.createSequentialGroup()
                        .addComponent(jtfMaSP, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbnChonSP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsnSoLuong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfDonGia))
                .addGap(8, 8, 8))
        );
        jpnThongTinSanPhamLayout.setVerticalGroup(
            jpnThongTinSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnThongTinSanPhamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnThongTinSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpnThongTinSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbnChonSP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jlbMaSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpnThongTinSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnThongTinSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnThongTinSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jsnSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(116, 116, 116))
        );

        jbnThem.setBackground(new java.awt.Color(157, 205, 239));
        jbnThem.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnThem.setText("Thêm");
        jbnThem.setBorder(null);
        jbnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnThemActionPerformed(evt);
            }
        });

        jbnSua.setBackground(new java.awt.Color(157, 205, 239));
        jbnSua.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnSua.setText("Sửa");
        jbnSua.setBorder(null);
        jbnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnSuaActionPerformed(evt);
            }
        });

        jbnXoa.setBackground(new java.awt.Color(204, 204, 204));
        jbnXoa.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnXoa.setText("Xóa sản phẩm");
        jbnXoa.setBorder(null);
        jbnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnXoaActionPerformed(evt);
            }
        });

        jlbTongTien.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbTongTien.setText("Tổng tiền");

        jtfTongTien.setEditable(false);
        jtfTongTien.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jtfTongTien.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jlbHinhAnh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbHinhAnh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jpnThongTinHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jpnThongTinSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jbnXoa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jbnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jbnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(99, 99, 99)
                                .addComponent(jlbHinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jlbTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtfTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jpnThongTinHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlbHinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jpnThongTinSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jbnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlbTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jbnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtfMaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfMaSPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfMaSPActionPerformed

    private void jbnChonSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnChonSPActionPerformed
        SanPhamJFrame sanPhamJFrame = new SanPhamJFrame();
        sanPhamJFrame.setTitle("Danh Sách Sản phẩm");
        sanPhamJFrame.setVisible(true);
        sanPhamJFrame.setResizable(false);
        sanPhamJFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_jbnChonSPActionPerformed

    private void jbnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnThemActionPerformed
        if (jtfMaSP.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!");
        } else if (jtfMaSP.getText().length() != 0 && (int) jsnSoLuong.getValue() <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!");
        } else {
            themDongTable();
            tinhTongTien();
            jtfMaSP.setText("");
            jtfTenSP.setText("");
            jtfDonGia.setText("");
            jsnSoLuong.setValue(0);
            jlbHinhAnh.setIcon(null);
        }
    }//GEN-LAST:event_jbnThemActionPerformed

    private void jtbHoaDon_SanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbHoaDon_SanPhamMouseClicked
        jbnThem.setEnabled(false);
        int row = jtbHoaDon_SanPham.getSelectedRow();
        String maSP = jtbHoaDon_SanPham.getValueAt(row, 0).toString();
        String sql = "select * from SanPham where MaSP = '" + maSP + "'";
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            SanPham sanPham = new SanPham();
            while (rs.next()) {
                sanPham.setMaSP(rs.getString("MaSP"));
                sanPham.setTenSP(rs.getString("TenSP"));
                sanPham.setMaLSP(rs.getString("MaLSP"));
                sanPham.setGiaBan(rs.getFloat("GiaBan"));
                sanPham.setHinhAnh(rs.getBytes("HinhAnh"));
            }
            ps.close();
            rs.close();
            cons.close();
            hienThongTin(sanPham);
            jsnSoLuong.setValue(jtbHoaDon_SanPham.getValueAt(row, 3));
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jtbHoaDon_SanPhamMouseClicked

    private void jbnTaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnTaoHDActionPerformed
        taoHoaDon();
    }//GEN-LAST:event_jbnTaoHDActionPerformed

    private void jbnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnXoaActionPerformed
        int row = jtbHoaDon_SanPham.getSelectedRow();
        model = (DefaultTableModel) jtbHoaDon_SanPham.getModel();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!");
        } else {
            int opt = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa sản phẩm này khỏi hóa đơn?", "Xóa sản phẩm khỏi hóa đơn", JOptionPane.YES_NO_OPTION);
            if (opt == 0) {
                model.removeRow(row);
                tinhTongTien();
            }
        }
        jtfMaSP.setText("");
        jtfTenSP.setText("");
        jtfDonGia.setText("");
        jsnSoLuong.setValue(0);
        jlbHinhAnh.setIcon(null);
        jbnThem.setEnabled(true);
    }//GEN-LAST:event_jbnXoaActionPerformed

    private void jbnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnSuaActionPerformed
        int row = jtbHoaDon_SanPham.getSelectedRow();
        model = (DefaultTableModel) jtbHoaDon_SanPham.getModel();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!");
        } else {
            model.removeRow(row);
            themDongTable();
            tinhTongTien();
            jtfMaSP.setText("");
            jtfTenSP.setText("");
            jtfDonGia.setText("");
            jsnSoLuong.setValue(0);
            jlbHinhAnh.setIcon(null);
            jbnThem.setEnabled(true);
        }
    }//GEN-LAST:event_jbnSuaActionPerformed

    private void jbnHuyHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnHuyHDActionPerformed
        int opt = JOptionPane.showConfirmDialog(null, "Bạn muốn hủy tạo hóa đơn này?", "Hủy hóa đơn", JOptionPane.YES_NO_OPTION);
        if (opt == 0) {
            lamMoi();
        }
    }//GEN-LAST:event_jbnHuyHDActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbnChonSP;
    private javax.swing.JButton jbnHuyHD;
    private javax.swing.JButton jbnSua;
    private javax.swing.JButton jbnTaoHD;
    private javax.swing.JButton jbnThem;
    private javax.swing.JButton jbnXoa;
    private com.toedter.calendar.JDateChooser jdcNgayLap;
    private javax.swing.JLabel jlbDonGia;
    private static javax.swing.JLabel jlbHinhAnh;
    private javax.swing.JLabel jlbMaHD;
    private javax.swing.JLabel jlbMaKH;
    private javax.swing.JLabel jlbMaNV;
    private javax.swing.JLabel jlbMaSP;
    private javax.swing.JLabel jlbNgayLap;
    private javax.swing.JLabel jlbSoLuong;
    private javax.swing.JLabel jlbTenSP;
    private javax.swing.JLabel jlbTongTien;
    private javax.swing.JPanel jpnThongTinHoaDon;
    private javax.swing.JPanel jpnThongTinSanPham;
    private static javax.swing.JSpinner jsnSoLuong;
    private javax.swing.JTable jtbHoaDon_SanPham;
    private static javax.swing.JTextField jtfDonGia;
    private javax.swing.JTextField jtfMaHD;
    private javax.swing.JTextField jtfMaKH;
    private javax.swing.JTextField jtfMaNV;
    private static javax.swing.JTextField jtfMaSP;
    private static javax.swing.JTextField jtfTenSP;
    private javax.swing.JTextField jtfTongTien;
    // End of variables declaration//GEN-END:variables
}
