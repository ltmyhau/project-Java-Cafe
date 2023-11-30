/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import dao.DBConnect;
import dao.LoaiSanPhamDao;
import dao.SanPhamDao;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.LoaiSanPham;
import model.SanPham;

/**
 *
 * @author Administrator
 */
public class LoaiSanPhamJPanel extends javax.swing.JPanel {

    /**
     * Creates new form KhachHangJPanel
     */
    DefaultTableModel model = new DefaultTableModel();
    TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<DefaultTableModel>(model);

    public LoaiSanPhamJPanel() {
        initComponents();

        jtbLoaiSanPham.getTableHeader().setBackground(new Color(0, 113, 61));
        jtbLoaiSanPham.getTableHeader().setForeground(Color.white);
        jtbLoaiSanPham.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        jtbLoaiSanPham.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 17));
        jtbLoaiSanPham.getTableHeader().setPreferredSize(new Dimension(100, 35));
        jtbLoaiSanPham.setRowHeight(35);

        jtbSanPham.getTableHeader().setBackground(new Color(0, 113, 61));
        jtbSanPham.getTableHeader().setForeground(Color.white);
        jtbSanPham.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        jtbSanPham.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 17));
        jtbSanPham.getTableHeader().setPreferredSize(new Dimension(100, 35));
        jtbSanPham.setRowHeight(35);

        addPlaceholderStyle(jtfTimKiem);

        layDuLieuTable();
        sort();

    }

    public void layDuLieuTable() {
        LoaiSanPhamDao loaiSanPhamDao = new LoaiSanPhamDao();
        String sql = "Select * from LoaiSanPham";
        List<LoaiSanPham> listLoaiSP = loaiSanPhamDao.getList(sql);
        model = (DefaultTableModel) jtbLoaiSanPham.getModel();
        model.setRowCount(0);
        for (LoaiSanPham i : listLoaiSP) {
            model.addRow(new Object[]{i.getMaLSP(), i.getTenLSP()});
        }
    }

    public void sort() {
        rowSorter = new TableRowSorter<DefaultTableModel>(model);
        jtbLoaiSanPham.setRowSorter(rowSorter);
    }

    public void search() {
        model = (DefaultTableModel) jtbLoaiSanPham.getModel();
        rowSorter = new TableRowSorter<>(model);
        jtbLoaiSanPham.setRowSorter(rowSorter);
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

    public void layDuLieuSanPham(String maLSP) {
        SanPhamDao sanPhamDao = new SanPhamDao();
        String sql = "Select * from SanPham where MaLSP = '" + maLSP + "'";
        List<SanPham> listSanPham = sanPhamDao.getList(sql);
        model = (DefaultTableModel) jtbSanPham.getModel();
        model.setRowCount(0);
        for (SanPham i : listSanPham) {
            model.addRow(new Object[]{
                i.getMaSP(),
                i.getTenSP(),
                i.getMaLSP(),
                i.getGiaBan()
            });
        }
    }

    public void insert() {
        if (jtfMaLoai.getText().length() == 0 || jtfTenLoai.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập dữ liệu bắt buộc!");
        } else {
            try {
                Connection cons = DBConnect.getConnection();
                String sql = "Insert into LoaiSanPham(MaLSP, TenLSP) Values(?,?)";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setString(1, jtfMaLoai.getText());
                ps.setString(2, jtfTenLoai.getText());
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

                layDuLieuTable();
                jtfMaLoai.setText("");
                jtfTenLoai.setText("");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Mã loại sản phẩm đã tồn tại");
                System.out.println(ex.toString());
            }
        }
    }
   
    public void update() {
        if (jtfMaLoai.getText().length() == 0 || jtfTenLoai.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập dữ liệu bắt buộc!");
        } else {
            try {
                Connection cons = DBConnect.getConnection();
                String maLSP = jtfMaLoai.getText();
                String sql = "Update LoaiSanPham set TenLSP = ? where MaLSP = '" + maLSP + "'";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setString(1, jtfTenLoai.getText());
                ps.executeUpdate();

                ps.close();
                cons.close();

                JOptionPane.showMessageDialog(this, "Đã cập nhật thành công!");

                layDuLieuTable();
                jtfMaLoai.setText("");
                jtfTenLoai.setText("");
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public void delete() {
        int opt = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa loại sản phẩm này? \nĐiều này sẽ làm ảnh hướng đến hầu hết tất cả các hóa đơn bán hàng.", "Xóa loại sản phẩm", JOptionPane.YES_NO_OPTION);
        if (opt == 0) {
            try {
                Connection cons = DBConnect.getConnection();
                String maLSP = jtfMaLoai.getText();
                String sql = "Delete from LoaiSanPham where MaLSP = '" + maLSP + "'";
                PreparedStatement ps = cons.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.executeUpdate();

                ps.close();
                cons.close();

                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");

                layDuLieuTable();
                jtfMaLoai.setText("");
                jtfTenLoai.setText("");
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

        jlbLoaiSanPham = new javax.swing.JLabel();
        jpnViewLSP = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbLoaiSanPham = new javax.swing.JTable();
        jpnThongTin = new javax.swing.JPanel();
        jbnXoa = new javax.swing.JButton();
        jbnThem = new javax.swing.JButton();
        jbnSua = new javax.swing.JButton();
        jpnMaLoai = new javax.swing.JPanel();
        jlbMaLoai = new javax.swing.JLabel();
        jtfMaLoai = new javax.swing.JTextField();
        jpnTenLoai = new javax.swing.JPanel();
        jlbTenLoai = new javax.swing.JLabel();
        jtfTenLoai = new javax.swing.JTextField();
        jtfTimKiem = new javax.swing.JTextField();
        jpnViewSP = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtbSanPham = new javax.swing.JTable();
        jlbSanPham = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(900, 700));

        jlbLoaiSanPham.setBackground(new java.awt.Color(255, 255, 255));
        jlbLoaiSanPham.setFont(new java.awt.Font("Sitka Text", 1, 29)); // NOI18N
        jlbLoaiSanPham.setForeground(new java.awt.Color(0, 113, 61));
        jlbLoaiSanPham.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbLoaiSanPham.setText("LOẠI SẢN PHẨM");
        jlbLoaiSanPham.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jpnViewLSP.setBackground(new java.awt.Color(255, 255, 255));

        jtbLoaiSanPham.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jtbLoaiSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Loại", "Tên Loại"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtbLoaiSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbLoaiSanPhamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtbLoaiSanPham);

        javax.swing.GroupLayout jpnViewLSPLayout = new javax.swing.GroupLayout(jpnViewLSP);
        jpnViewLSP.setLayout(jpnViewLSPLayout);
        jpnViewLSPLayout.setHorizontalGroup(
            jpnViewLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jpnViewLSPLayout.setVerticalGroup(
            jpnViewLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jpnThongTin.setBackground(new java.awt.Color(255, 255, 255));

        jbnXoa.setBackground(new java.awt.Color(157, 205, 239));
        jbnXoa.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jbnXoa.setText("Xóa");
        jbnXoa.setToolTipText("");
        jbnXoa.setBorder(null);
        jbnXoa.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jbnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnXoaActionPerformed(evt);
            }
        });

        jbnThem.setBackground(new java.awt.Color(157, 205, 239));
        jbnThem.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jbnThem.setText("Thêm");
        jbnThem.setToolTipText("");
        jbnThem.setBorder(null);
        jbnThem.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jbnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnThemActionPerformed(evt);
            }
        });

        jbnSua.setBackground(new java.awt.Color(157, 205, 239));
        jbnSua.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jbnSua.setText("Sửa");
        jbnSua.setToolTipText("");
        jbnSua.setBorder(null);
        jbnSua.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jbnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnSuaActionPerformed(evt);
            }
        });

        jpnMaLoai.setBackground(new java.awt.Color(255, 255, 255));

        jlbMaLoai.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbMaLoai.setText("Mã Loại");

        jtfMaLoai.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        javax.swing.GroupLayout jpnMaLoaiLayout = new javax.swing.GroupLayout(jpnMaLoai);
        jpnMaLoai.setLayout(jpnMaLoaiLayout);
        jpnMaLoaiLayout.setHorizontalGroup(
            jpnMaLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnMaLoaiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbMaLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfMaLoai, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnMaLoaiLayout.setVerticalGroup(
            jpnMaLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnMaLoaiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnMaLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfMaLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbMaLoai))
                .addContainerGap())
        );

        jpnTenLoai.setBackground(new java.awt.Color(255, 255, 255));

        jlbTenLoai.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jlbTenLoai.setText("Tên loại");

        jtfTenLoai.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        javax.swing.GroupLayout jpnTenLoaiLayout = new javax.swing.GroupLayout(jpnTenLoai);
        jpnTenLoai.setLayout(jpnTenLoaiLayout);
        jpnTenLoaiLayout.setHorizontalGroup(
            jpnTenLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnTenLoaiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbTenLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfTenLoai)
                .addContainerGap())
        );
        jpnTenLoaiLayout.setVerticalGroup(
            jpnTenLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnTenLoaiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnTenLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbTenLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtfTenLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jpnThongTinLayout = new javax.swing.GroupLayout(jpnThongTin);
        jpnThongTin.setLayout(jpnThongTinLayout);
        jpnThongTinLayout.setHorizontalGroup(
            jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnThongTinLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpnThongTinLayout.createSequentialGroup()
                        .addComponent(jbnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jpnMaLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpnTenLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
        );
        jpnThongTinLayout.setVerticalGroup(
            jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnThongTinLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jpnMaLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jpnTenLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(26, 26, 26)
                .addGroup(jpnThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        jtfTimKiem.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jtfTimKiem.setText("Tìm kiếm");
        jtfTimKiem.setToolTipText("");
        jtfTimKiem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfTimKiemFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfTimKiemFocusLost(evt);
            }
        });
        jtfTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfTimKiemActionPerformed(evt);
            }
        });
        jtfTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfTimKiemKeyReleased(evt);
            }
        });

        jpnViewSP.setBackground(new java.awt.Color(255, 255, 255));

        jtbSanPham.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jtbSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Sản Phẩm", "Tên Sản Phẩm", "Mã Loại Sản Phẩm", "Giá Bán"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jtbSanPham);

        javax.swing.GroupLayout jpnViewSPLayout = new javax.swing.GroupLayout(jpnViewSP);
        jpnViewSP.setLayout(jpnViewSPLayout);
        jpnViewSPLayout.setHorizontalGroup(
            jpnViewSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jpnViewSPLayout.setVerticalGroup(
            jpnViewSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
        );

        jlbSanPham.setFont(new java.awt.Font("Sitka Text", 1, 23)); // NOI18N
        jlbSanPham.setForeground(new java.awt.Color(0, 113, 61));
        jlbSanPham.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbSanPham.setText("SẢN PHẨM");
        jlbSanPham.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlbSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlbLoaiSanPham, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jpnViewLSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jpnThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtfTimKiem)))
                    .addComponent(jpnViewSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jlbLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jtfTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpnThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jpnViewLSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlbSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnViewSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnThemActionPerformed
        jtfMaLoai.setEditable(true);
        insert();
    }//GEN-LAST:event_jbnThemActionPerformed

    private void jtfTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfTimKiemActionPerformed

    private void jbnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnSuaActionPerformed
        update();
    }//GEN-LAST:event_jbnSuaActionPerformed

    private void jbnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnXoaActionPerformed
        if (jtfMaLoai.getText().length() == 0 || jtfTenLoai.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm cần xóa!");
        } else {
            delete();
        }
    }//GEN-LAST:event_jbnXoaActionPerformed

    private void jtbLoaiSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbLoaiSanPhamMouseClicked
        jtfMaLoai.setEditable(false);
        int row = jtbLoaiSanPham.getSelectedRow();
        jtfMaLoai.setText(jtbLoaiSanPham.getValueAt(row, 0).toString());
        jtfTenLoai.setText(jtbLoaiSanPham.getValueAt(row, 1).toString());
        layDuLieuSanPham(jtbLoaiSanPham.getValueAt(row, 0).toString());
    }//GEN-LAST:event_jtbLoaiSanPhamMouseClicked

    private void jtfTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfTimKiemKeyReleased
        search();
    }//GEN-LAST:event_jtfTimKiemKeyReleased

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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbnSua;
    private javax.swing.JButton jbnThem;
    private javax.swing.JButton jbnXoa;
    private javax.swing.JLabel jlbLoaiSanPham;
    private javax.swing.JLabel jlbMaLoai;
    private javax.swing.JLabel jlbSanPham;
    private javax.swing.JLabel jlbTenLoai;
    private javax.swing.JPanel jpnMaLoai;
    private javax.swing.JPanel jpnTenLoai;
    private javax.swing.JPanel jpnThongTin;
    private javax.swing.JPanel jpnViewLSP;
    private javax.swing.JPanel jpnViewSP;
    private javax.swing.JTable jtbLoaiSanPham;
    private javax.swing.JTable jtbSanPham;
    private javax.swing.JTextField jtfMaLoai;
    private javax.swing.JTextField jtfTenLoai;
    private javax.swing.JTextField jtfTimKiem;
    // End of variables declaration//GEN-END:variables
}
