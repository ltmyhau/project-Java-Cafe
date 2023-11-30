/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import dao.DBConnect;
import dao.SanPhamDao;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.SanPham;

/**
 *
 * @author Administrator
 */
public class SanPhamJPanel extends javax.swing.JPanel {

    /**
     * Creates new form SanPhamJPanel
     */
    DefaultTableModel model = new DefaultTableModel();
    TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<DefaultTableModel>(model);

    public SanPhamJPanel() {
        initComponents();

        jtbSanPham.getTableHeader().setBackground(new Color(0, 113, 61));
        jtbSanPham.getTableHeader().setForeground(Color.white);
        jtbSanPham.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        jtbSanPham.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 17));
        jtbSanPham.getTableHeader().setPreferredSize(new Dimension(100, 35));
        jtbSanPham.setRowHeight(35);

        addPlaceholderStyle(jtfTimKiem);

        layDuLieuTable();
        doLoaiSPComboBox(jcbMaLoai);
        doLoaiSPComboBox(jcbLocMaLoai);
        sort();
    }

    String ImgPath = null;

    public ImageIcon ResizeImage(String imagePath, byte[] pic) {
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

    public void doLoaiSPComboBox(JComboBox jcb) {
        String sql = "select * from LoaiSanPham order by MaLSP";
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            DefaultComboBoxModel cbbModel = (DefaultComboBoxModel) jcb.getModel();

            while (rs.next()) {
                jcb.addItem(rs.getString("MaLSP"));
            }
            ps.close();
            rs.close();
            cons.close();
        } catch (Exception e) {
        }
    }

    public void layDuLieuTable() {
        List<SanPham> listSanPham = new SanPhamDao().getList();
        model = (DefaultTableModel) jtbSanPham.getModel();
        model.setRowCount(0);
        for (SanPham o : listSanPham) {
            model.addRow(new Object[]{
                o.getMaSP(),
                o.getTenSP(),
                o.getMaLSP(),
                o.getGiaBan(),
                o.getHinhAnh()});
        }
    }

    public void sort() {
        rowSorter = new TableRowSorter<DefaultTableModel>(model);
        jtbSanPham.setRowSorter(rowSorter);
    }

    public void search() {
        model = (DefaultTableModel) jtbSanPham.getModel();
        rowSorter = new TableRowSorter<>(model);
        jtbSanPham.setRowSorter(rowSorter);
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

    public void hienThongTin(SanPham sanPham) {
        jtfMaSP.setText(sanPham.getMaSP());
        jtfTenSP.setText(sanPham.getTenSP());
        jtfGiaBan.setText(Float.toString(sanPham.getGiaBan()));

        String maLoaiCbBox = sanPham.getMaLSP();
        for (int i = 0; i < jcbMaLoai.getItemCount(); i++) {
            if (jcbMaLoai.getItemAt(i).toString().equalsIgnoreCase(maLoaiCbBox)) {
                jcbMaLoai.setSelectedIndex(i);
            }
        }
        if (sanPham.getHinhAnh() != null) {
            jlbHinhAnh.setIcon(ResizeImage(null, sanPham.getHinhAnh()));
        } else {
            jlbHinhAnh.setIcon(null);
        }
    }

    public void chonAnh() {
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));

        FileNameExtensionFilter filter = new FileNameExtensionFilter(".images", ".jpg", ".png");
        file.addChoosableFileFilter(filter);
        int result = file.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = file.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            jlbHinhAnh.setIcon(ResizeImage(path, null));
            ImgPath = path;
        } else {
            System.out.println("Không có file được chọn");
        }
    }

    public boolean kiemTra() {
        if (jtfMaSP.getText().length() == 0 || jtfTenSP.getText().length() == 0
                || jcbMaLoai.getSelectedItem().toString().length() == 0 || jtfGiaBan.getText().length() == 0) {
            return false;
        } else {
            try {
                Float.parseFloat(jtfGiaBan.getText());
                return true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Giá bán phải là một số");
                return false;
            }
        }
    }

    public void insert() {
        if (kiemTra()) {
            String sql = null;
            Connection cons = null;
            PreparedStatement ps = null;
            if (ImgPath == null) {
                try {
                    cons = DBConnect.getConnection();
                    sql = "Insert into SanPham(MaSP, TenSP, MaLSP, GiaBan) values(?,?,?,?)";
                    ps = cons.prepareStatement(sql);

                    ps = cons.prepareStatement(sql);
                    ps.setString(1, jtfMaSP.getText());
                    ps.setString(2, jtfTenSP.getText());
                    ps.setString(3, jcbMaLoai.getSelectedItem().toString());
                    ps.setString(4, jtfGiaBan.getText());

                    ps.executeUpdate();

                    lamMoi();

                    JOptionPane.showMessageDialog(null, "Thêm sản phẩm thành công");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Mã sản phẩm đã tồn tại");
                    System.out.println(ex.toString());
                }
            } else {
                try {
                    cons = DBConnect.getConnection();
                    sql = "Insert into SanPham(MaSP, TenSP, MaLSP, GiaBan, HinhAnh) values(?,?,?,?,?)";
                    ps = cons.prepareStatement(sql);

                    ps.setString(1, jtfMaSP.getText());
                    ps.setString(2, jtfTenSP.getText());
                    ps.setString(3, jcbMaLoai.getSelectedItem().toString());
                    ps.setString(4, jtfGiaBan.getText());
                    InputStream img = new FileInputStream(new File(ImgPath));
                    ps.setBlob(5, img);

                    ps.executeUpdate();

                    lamMoi();

                    JOptionPane.showMessageDialog(null, "Thêm sản phẩm thành công");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Mã sản phẩm đã tồn tại");
                    System.out.println(e.toString());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập dữ liệu");
        }
    }

    public void update() {
        if (kiemTra()) {
            String sql = null;
            Connection cons = null;
            PreparedStatement ps = null;
            if (ImgPath == null) {
                try {
                    cons = DBConnect.getConnection();
                    sql = "Update SanPham set TenSP = ?, MaLSP = ?, GiaBan = ? where MaSP = ?";
                    ps = cons.prepareStatement(sql);

                    ps.setString(1, jtfTenSP.getText());
                    String maLSP = jcbMaLoai.getSelectedItem().toString();
                    ps.setString(2, maLSP);
                    ps.setString(3, jtfGiaBan.getText());
                    ps.setString(4, jtfMaSP.getText());
                    ps.executeUpdate();

                    lamMoi();

                    JOptionPane.showMessageDialog(null, "Cập nhật sản phẩm thành công");

                } catch (SQLException ex) {
                    Logger.getLogger(SanPhamJPanel.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    cons = DBConnect.getConnection();
                    sql = "Update SanPham set TenSP = ?, MaLSP = ?, GiaBan = ?, HinhAnh = ? where MaSP = ?";
                    ps = cons.prepareStatement(sql);

                    ps.setString(1, jtfTenSP.getText());
                    String maLSP = jcbMaLoai.getSelectedItem().toString();
                    ps.setString(2, maLSP);
                    ps.setString(3, jtfGiaBan.getText());
                    InputStream img = new FileInputStream(new File(ImgPath));
                    ps.setBlob(4, img);
                    ps.setString(5, jtfMaSP.getText());
                    ps.executeUpdate();

                    lamMoi();

                    JOptionPane.showMessageDialog(null, "Cập nhật sản phẩm thành công");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập dữ liệu");
        }
    }

    public void delete() {
        if (!jtfMaSP.getText().equals("")) {
            int opt = JOptionPane.showConfirmDialog(null, "Bạn muốn xóa sản phẩm này? \nNếu bạn xóa sản phẩm này, một vài hóa đơn cũng bị ảnh hưởng.", "Xóa sản phẩm", JOptionPane.YES_NO_OPTION);
            if (opt == 0) {
                try {
                    Connection cons = DBConnect.getConnection();
                    String sql = "Delete from SanPham where MaSP = ?";
                    PreparedStatement ps = cons.prepareStatement(sql);

                    ps.setString(1, jtfMaSP.getText());
                    ps.executeUpdate();

                    lamMoi();

                    JOptionPane.showMessageDialog(null, "Xóa sản phẩm thành công");
                } catch (SQLException e) {
                    System.out.println(e.toString());
                    JOptionPane.showMessageDialog(null, "Xóa sản phẩm thất bại");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Chọn sản phẩm cần xóa");
        }
    }

    public void lamMoi() {
        layDuLieuTable();
        jtfMaSP.setEditable(true);
        jtfMaSP.setText("");
        jtfTenSP.setText("");
        jtfGiaBan.setText("");
        jtfTimKiem.setText("");
        jtfGiaDau.setText("");
        jtfGiaCuoi.setText("");
        jlbHinhAnh.setIcon(null);
        jcbMaLoai.setSelectedIndex(0);
        jcbLocMaLoai.setSelectedIndex(0);
    }

    public void locTheoMaLoai(String maLSP) {
        rowSorter = new TableRowSorter<>(model);
        model = (DefaultTableModel) jtbSanPham.getModel();
        jtbSanPham.setRowSorter(rowSorter);
        if (maLSP != "Tất cả") {
            rowSorter.setRowFilter(RowFilter.regexFilter(maLSP));
        } else {
            jtbSanPham.setRowSorter(rowSorter);
        }
    }

    public void locTheoGia(String sql) {
        SanPhamDao sanPhamDao = new SanPhamDao();
        List<SanPham> listSanPham = sanPhamDao.getList(sql);
        model = (DefaultTableModel) jtbSanPham.getModel();
        model.setRowCount(0);
        for (SanPham o : listSanPham) {
            model.addRow(new Object[]{
                o.getMaSP(),
                o.getTenSP(),
                o.getMaLSP(),
                o.getGiaBan(),
                o.getHinhAnh()});
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
        jlbHinhAnh = new javax.swing.JLabel();
        jbnChonAnh = new javax.swing.JButton();
        jpnThongTin = new javax.swing.JPanel();
        jlbMaSP = new javax.swing.JLabel();
        jtfMaSP = new javax.swing.JTextField();
        jlbTenSP = new javax.swing.JLabel();
        jtfTenSP = new javax.swing.JTextField();
        jlbMaLoai = new javax.swing.JLabel();
        jcbMaLoai = new javax.swing.JComboBox<>();
        jlbGiaBan = new javax.swing.JLabel();
        jtfGiaBan = new javax.swing.JTextField();
        jbnThem = new javax.swing.JButton();
        jbnSua = new javax.swing.JButton();
        jbnXoa = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbSanPham = new javax.swing.JTable();
        jtfTimKiem = new javax.swing.JTextField();
        jcbLocMaLoai = new javax.swing.JComboBox<>();
        jlbGiaVND = new javax.swing.JLabel();
        jtfGiaDau = new javax.swing.JTextField();
        jlbGiua = new javax.swing.JLabel();
        jtfGiaCuoi = new javax.swing.JTextField();
        jbnLocGia = new javax.swing.JButton();
        jbnLamMoi = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jlbHinhAnh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jbnChonAnh.setBackground(new java.awt.Color(204, 204, 204));
        jbnChonAnh.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jbnChonAnh.setText("Chọn ảnh");
        jbnChonAnh.setBorder(null);
        jbnChonAnh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnChonAnhActionPerformed(evt);
            }
        });

        jpnThongTin.setBackground(new java.awt.Color(255, 255, 255));

        jlbMaSP.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbMaSP.setText("Mã sản phẩm");

        jtfMaSP.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbTenSP.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbTenSP.setText("Tên sản phẩm");

        jtfTenSP.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbMaLoai.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbMaLoai.setText("Mã loại");

        jcbMaLoai.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jlbGiaBan.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbGiaBan.setText("Giá bán");

        jtfGiaBan.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        javax.swing.GroupLayout jpnThongTinLayout = new javax.swing.GroupLayout(jpnThongTin);
        jpnThongTin.setLayout(jpnThongTinLayout);
        jpnThongTinLayout.setHorizontalGroup(
            jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jlbGiaBan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlbMaSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlbTenSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                    .addComponent(jlbMaLoai, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfMaSP)
                    .addComponent(jtfTenSP)
                    .addComponent(jtfGiaBan)
                    .addComponent(jcbMaLoai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpnThongTinLayout.setVerticalGroup(
            jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtfTenSP, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(jlbTenSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlbMaLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbMaLoai, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlbGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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

        jbnXoa.setBackground(new java.awt.Color(157, 205, 239));
        jbnXoa.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnXoa.setText("Xóa");
        jbnXoa.setBorder(null);
        jbnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnXoaActionPerformed(evt);
            }
        });

        jtbSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Mã loại", "Giá bán (VNĐ)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtbSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbSanPhamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtbSanPham);

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

        jcbLocMaLoai.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jcbLocMaLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        jcbLocMaLoai.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbLocMaLoaiItemStateChanged(evt);
            }
        });
        jcbLocMaLoai.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jcbLocMaLoaiAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jlbGiaVND.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbGiaVND.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlbGiaVND.setText("Giá (VNĐ)");

        jtfGiaDau.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jtfGiaDau.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jlbGiua.setFont(new java.awt.Font("Times New Roman", 1, 23)); // NOI18N
        jlbGiua.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbGiua.setText("-");

        jtfGiaCuoi.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jtfGiaCuoi.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfGiaCuoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfGiaCuoiActionPerformed(evt);
            }
        });

        jbnLocGia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon_search.png"))); // NOI18N
        jbnLocGia.setBorder(null);
        jbnLocGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnLocGiaActionPerformed(evt);
            }
        });

        jbnLamMoi.setBackground(new java.awt.Color(157, 205, 239));
        jbnLamMoi.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnLamMoi.setText("Làm mới");
        jbnLamMoi.setBorder(null);
        jbnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnLamMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jtfTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcbLocMaLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jlbGiaVND, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfGiaDau, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlbGiua, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfGiaCuoi, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbnLocGia, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbnChonAnh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlbHinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addComponent(jpnThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(94, 94, 94)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbnXoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbnSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbnThem, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(jbnLamMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(13, 13, 13)))
                .addGap(14, 14, 14))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jbnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbnLamMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jpnThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlbHinhAnh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbnChonAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbLocMaLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbnLocGia, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfGiaDau, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbGiaVND, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbGiua, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfGiaCuoi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                .addGap(16, 16, 16))
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

    private void jtfGiaCuoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfGiaCuoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfGiaCuoiActionPerformed

    private void jcbLocMaLoaiAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jcbLocMaLoaiAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbLocMaLoaiAncestorAdded

    private void jbnChonAnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnChonAnhActionPerformed
        chonAnh();
    }//GEN-LAST:event_jbnChonAnhActionPerformed

    private void jbnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnThemActionPerformed
        insert();
    }//GEN-LAST:event_jbnThemActionPerformed

    private void jbnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnSuaActionPerformed
        update();
    }//GEN-LAST:event_jbnSuaActionPerformed

    private void jbnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnXoaActionPerformed
        delete();
    }//GEN-LAST:event_jbnXoaActionPerformed

    private void jtbSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbSanPhamMouseClicked
        jtfMaSP.setEditable(false);
        int row = jtbSanPham.getSelectedRow();
        String maSP = jtbSanPham.getValueAt(row, 0).toString();
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
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jtbSanPhamMouseClicked

    private void jcbLocMaLoaiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbLocMaLoaiItemStateChanged
        String maLSP = jcbLocMaLoai.getSelectedItem().toString();
        locTheoMaLoai(maLSP);
    }//GEN-LAST:event_jcbLocMaLoaiItemStateChanged

    private void jbnLocGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnLocGiaActionPerformed
        if (jtfGiaDau.getText().equalsIgnoreCase("") && jtfGiaCuoi.getText().equalsIgnoreCase("")) {
            String sql = "Select * from SanPham";
            locTheoGia(sql);
        } else {
            float giaDau = Float.parseFloat(jtfGiaDau.getText());
            float giaCuoi = Float.parseFloat(jtfGiaCuoi.getText());
            String sql = "Select * from SanPham where GiaBan between " + giaDau + " and " + giaCuoi;
            locTheoGia(sql);
        }
    }//GEN-LAST:event_jbnLocGiaActionPerformed

    private void jtfTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfTimKiemKeyReleased
        search();
    }//GEN-LAST:event_jtfTimKiemKeyReleased

    private void jbnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnLamMoiActionPerformed
        lamMoi();
        sort();
    }//GEN-LAST:event_jbnLamMoiActionPerformed

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbnChonAnh;
    private javax.swing.JButton jbnLamMoi;
    private javax.swing.JButton jbnLocGia;
    private javax.swing.JButton jbnSua;
    private javax.swing.JButton jbnThem;
    private javax.swing.JButton jbnXoa;
    private javax.swing.JComboBox<String> jcbLocMaLoai;
    private javax.swing.JComboBox<String> jcbMaLoai;
    private javax.swing.JLabel jlbGiaBan;
    private javax.swing.JLabel jlbGiaVND;
    private javax.swing.JLabel jlbGiua;
    private javax.swing.JLabel jlbHinhAnh;
    private javax.swing.JLabel jlbMaLoai;
    private javax.swing.JLabel jlbMaSP;
    private javax.swing.JLabel jlbTenSP;
    private javax.swing.JPanel jpnThongTin;
    private javax.swing.JTable jtbSanPham;
    private javax.swing.JTextField jtfGiaBan;
    private javax.swing.JTextField jtfGiaCuoi;
    private javax.swing.JTextField jtfGiaDau;
    private javax.swing.JTextField jtfMaSP;
    private javax.swing.JTextField jtfTenSP;
    private javax.swing.JTextField jtfTimKiem;
    // End of variables declaration//GEN-END:variables
}
