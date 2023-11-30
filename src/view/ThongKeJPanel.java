/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import dao.DBConnect;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Administrator
 */
public class ThongKeJPanel extends javax.swing.JPanel {

    /**
     * Creates new form ThongKeJPanel
     */
    DefaultTableModel model = new DefaultTableModel();

    public ThongKeJPanel() {
        initComponents();

        dinhDangTable(jtbDT_Bang);
        String sqlDT = "select concat(convert(varchar, format(month(NgayLapHD), '0#')),'/',year(NgayLapHD)) [Tháng Năm], count (distinct hd.MaHD) [Tổng số hóa đơn], sum(cthd.SLDat * GiaBan) [Tổng doanh thu]\n"
                + "from HoaDon hd inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                + "group by year(NgayLapHD), month(NgayLapHD) order by year(NgayLapHD), month(NgayLapHD)";
        tongDoanhThu(sqlDT);
        bieuDoDuong(jpnDT_BieuDo);
        tinhTong(jtbDT_Bang, "Tổng số hóa đơn", jtfDT_TongHoaDon);
        tinhTong(jtbDT_Bang, "Tổng doanh thu", jtfDT_TongDoanhThu);

        dinhDangTable(jtbSP_Bang);
        doLoaiSPComboBox(jcbSP_MaLSP);
        String sqlSP = "select sp.MaSP [Mã SP], TenSP [Tên SP], MaLSP [Mã LSP], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                + "from SanPham sp left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP\n"
                + "group by  MaLSP, sp.MaSP, TenSP";
        doanhThuSanPham(sqlSP);
        tinhTong(jtbSP_Bang, "Số lượng đã bán", jtfSP_TongSoLuong);
        tinhTong(jtbSP_Bang, "Tổng doanh thu", jtfSP_TongDoanhThu);

        dinhDangTable(jtbCN_Bang);
        doChiNhanhComboBox(jcbCN_MaChiNhanh, "MaCN");
        doChiNhanhComboBox(jcbCN_TenChiNhanh, "TenCN");
        String sqlCN = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                + "group by cn.MaCN, TenCN";
        tongDoanhThuChiNhanh(sqlCN);
        bieuDoCot(jtbCN_Bang, jpnCN_BieuDo, "Thống kê doanh số bán hàng", "Tổng doanh thu");
        tinhTong(jtbCN_Bang, "Tổng doanh thu", jtfCN_TongDoanhThu);
    }

    public void dinhDangTable(JTable jTable) {
        jTable.getTableHeader().setBackground(new Color(0, 113, 61));
        jTable.getTableHeader().setForeground(Color.white);
        jTable.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        jTable.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 17));
        jTable.getTableHeader().setPreferredSize(new Dimension(100, 35));
        jTable.setRowHeight(35);
        jTable.setEnabled(false);
    }

    public void tongDoanhThu(String sql) {
        String[] listColumn = {"Tháng/Năm", "Tổng số hóa đơn", "Tổng doanh thu"};
        DefaultTableModel model = new DefaultTableModel(listColumn, 0);
        jtbDT_Bang.setModel(model);
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] item = new Object[3];
                item[0] = rs.getString("Tháng Năm");
                item[1] = rs.getString("Tổng số hóa đơn");
                item[2] = rs.getString("Tổng doanh thu");
                model.addRow(item);
            }
        } catch (Exception e) {
        }
    }

    public void tongDoanhThuNhanVien(String sql, JTable jTable) {
        String[] listColumn = {"Mã NV", "Họ tên", "Tổng số hóa đơn", "Tổng doanh thu"};
        model = new DefaultTableModel(listColumn, 0);
        jTable.setModel(model);
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] item = new Object[4];
                item[0] = rs.getString("Mã NV");
                item[1] = rs.getString("Họ tên");
                item[2] = rs.getString("Tổng số hóa đơn");
                item[3] = rs.getString("Tổng doanh thu");
                model.addRow(item);
            }
        } catch (Exception e) {
        }
    }

    public void tongDoanhThuKhachHang(String sql) {
        String[] listColumn = {"Mã KH", "Họ tên", "Tổng số hóa đơn", "Tổng doanh thu"};
        model = new DefaultTableModel(listColumn, 0);
        jtbDT_Bang.setModel(model);
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] item = new Object[4];
                item[0] = rs.getString("Mã KH");
                item[1] = rs.getString("Họ tên");
                item[2] = rs.getString("Tổng số hóa đơn");
                item[3] = rs.getString("Tổng doanh thu");
                model.addRow(item);
            }
        } catch (Exception e) {
        }
    }

    public void tinhTong(JTable jTable, String tenCot, JTextField jTextField) {
        model = (DefaultTableModel) jTable.getModel();
        double tong = 0;
        int column = 0;
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (model.getColumnName(i).toString().equalsIgnoreCase(tenCot)) {
                column = i;
            }
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            tong = tong + Float.parseFloat(model.getValueAt(i, column).toString());
        }

        NumberFormat df = new DecimalFormat("#,###");
        jTextField.setText(df.format(tong));
    }

    public void bieuDoDuong(JPanel jpnBieuDo) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < jtbDT_Bang.getRowCount(); i++) {
            dataset.setValue(Float.parseFloat(jtbDT_Bang.getValueAt(i, 2).toString()), "Doanh Thu", (Comparable) jtbDT_Bang.getValueAt(i, 0));
        }

        JFreeChart linechart = ChartFactory.createLineChart("THỐNG KÊ DOANH THU BÁN HÀNG", null, null,
                dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot lineCategoryPlot = linechart.getCategoryPlot();
        lineCategoryPlot.setBackgroundPaint(Color.white);

        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();
        Color lineChartColor = new Color(204, 0, 51);
        lineRenderer.setSeriesPaint(0, lineChartColor);

        ChartPanel lineChartPanel = new ChartPanel(linechart);
        jpnBieuDo.removeAll();
        jpnBieuDo.add(lineChartPanel, BorderLayout.CENTER);
        jpnBieuDo.validate();
    }

    public void bieuDoCot(JTable jTable, JPanel jpnBieuDo, String tenBieuDo, String tenCot) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        int column = 0;
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (model.getColumnName(i).toString().equalsIgnoreCase(tenCot)) {
                column = i;
            }
        }
        for (int i = 0; i < jTable.getRowCount(); i++) {
            dataset.setValue(Float.parseFloat(jTable.getValueAt(i, column).toString()), "Doanh Thu", (Comparable) jTable.getValueAt(i, 0));
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                tenBieuDo.toUpperCase(),
                null, null,
                dataset, PlotOrientation.VERTICAL, false, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(jpnBieuDo.getWidth(), 321));

        jpnBieuDo.removeAll();
        jpnBieuDo.setLayout(new CardLayout());
        jpnBieuDo.add(chartPanel);
        jpnBieuDo.validate();
        jpnBieuDo.repaint();
    }

    public void tatCaDoanhThu() {
        String sql = "select concat(convert(varchar, format(month(NgayLapHD), '0#')),'/',year(NgayLapHD)) [Tháng Năm], count (distinct hd.MaHD) [Tổng số hóa đơn], sum(cthd.SLDat * GiaBan) [Tổng doanh thu]\n"
                + "from HoaDon hd inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                + "group by year(NgayLapHD), month(NgayLapHD) order by year(NgayLapHD), month(NgayLapHD)";
        tongDoanhThu(sql);
        bieuDoDuong(jpnDT_BieuDo);
        tinhTong(jtbDT_Bang, "Tổng số hóa đơn", jtfDT_TongHoaDon);
        tinhTong(jtbDT_Bang, "Tổng doanh thu", jtfDT_TongDoanhThu);
    }

    public void doanhThuKhoangThoiGian(String ngayBD, String ngayKT) {
        String sql = "select concat(convert(varchar, format(month(NgayLapHD), '0#')),'/',year(NgayLapHD)) [Tháng Năm], count (distinct hd.MaHD) [Tổng số hóa đơn], sum(cthd.SLDat * GiaBan) [Tổng doanh thu]\n"
                + "from HoaDon hd inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "')\n"
                + "group by year(NgayLapHD), month(NgayLapHD) order by year(NgayLapHD), month(NgayLapHD)";

        tongDoanhThu(sql);
        bieuDoDuong(jpnDT_BieuDo);
        tinhTong(jtbDT_Bang, "Tổng số hóa đơn", jtfDT_TongHoaDon);
        tinhTong(jtbDT_Bang, "Tổng doanh thu", jtfDT_TongDoanhThu);
    }

    public void doanhSoBanHangNhanVien(String ngayBD, String ngayKT) {
        String sql = "";
        if (ngayBD == null || ngayKT == null) {
            sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                    + "from NhanVien nv left join HoaDon hd on hd.MaNV = nv.MaNV left join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD left join SanPham sp on cthd.MaSP = sp.MaSP\n"
                    + "group by nv.MaNV, HoTenNV";
        } else {
            sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                    + "from NhanVien nv left join HoaDon hd on hd.MaNV = nv.MaNV left join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD left join SanPham sp on cthd.MaSP = sp.MaSP\n"
                    + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "')\n"
                    + "group by nv.MaNV, HoTenNV";
        }
        tongDoanhThuNhanVien(sql, jtbDT_Bang);
        bieuDoCot(jtbDT_Bang, jpnDT_BieuDo, "Thống kê doanh số bán hàng của nhân viên", "Tổng doanh thu");
        tinhTong(jtbDT_Bang, "Tổng số hóa đơn", jtfDT_TongHoaDon);
        tinhTong(jtbDT_Bang, "Tổng doanh thu", jtfDT_TongDoanhThu);
    }

    public void doanhThuBanHangTheoKhachHang(String ngayBD, String ngayKT) {
        String sql = "";
        if (ngayBD == null || ngayKT == null) {
            sql = "select kh.MaKH [Mã KH], HoTenKH [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                    + "from KhachHang kh left join HoaDon hd on kh.MaKH = hd.MaKh left join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD left join SanPham sp on cthd.MaSP = sp.MaSP\n"
                    + "group by kh.MaKH, HoTenKH";
        } else {
            sql = "select kh.MaKH [Mã KH], HoTenKH [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                    + "from KhachHang kh left join HoaDon hd on kh.MaKH = hd.MaKh left join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD left join SanPham sp on cthd.MaSP = sp.MaSP\n"
                    + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "')\n"
                    + "group by kh.MaKH, HoTenKH";
        }
        tongDoanhThuKhachHang(sql);
        bieuDoCot(jtbDT_Bang, jpnDT_BieuDo, "Thống kê doanh thu bán hàng theo khách hàng", "Tổng doanh thu");
        tinhTong(jtbDT_Bang, "Tổng số hóa đơn", jtfDT_TongHoaDon);
        tinhTong(jtbDT_Bang, "Tổng doanh thu", jtfDT_TongDoanhThu);
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

    public void doanhThuSanPham(String sql) {
        String[] listColumn = {"Mã SP", "Tên SP", "Mã LSP", "Số lượng đã bán", "Tổng doanh thu"};
        model = new DefaultTableModel(listColumn, 0);
        jtbSP_Bang.setModel(model);
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] item = new Object[5];
                item[0] = rs.getString("Mã SP");
                item[1] = rs.getString("Tên SP");
                item[2] = rs.getString("Mã LSP");
                item[3] = rs.getString("Số lượng đã bán");
                item[4] = rs.getString("Tổng doanh thu");
                model.addRow(item);
            }
        } catch (Exception e) {
        }
    }

    public void doanhThuLoaiSanPham(String sql) {
        String[] listColumn = {"Mã LSP", "Tên LSP", "Số lượng sản phẩm", "Số lượng đã bán", "Tổng doanh thu"};
        model = new DefaultTableModel(listColumn, 0);
        jtbSP_Bang.setModel(model);
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] item = new Object[5];
                item[0] = rs.getString("Mã LSP");
                item[1] = rs.getString("Tên LSP");
                item[2] = rs.getString("Số lượng sản phẩm");
                item[3] = rs.getString("Số lượng đã bán");
                item[4] = rs.getString("Tổng doanh thu");
                model.addRow(item);
            }
        } catch (Exception e) {
        }
    }

    public void doanhThuSanPhamTheoLoaiSanPham(String sql) {
        String[] listColumn = {"Mã SP", "Tên SP", "Số lượng đã bán", "Tổng doanh thu"};
        model = new DefaultTableModel(listColumn, 0);
        jtbSP_Bang.setModel(model);
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] item = new Object[4];
                item[0] = rs.getString("Mã SP");
                item[1] = rs.getString("Tên SP");
                item[2] = rs.getString("Số lượng đã bán");
                item[3] = rs.getString("Tổng doanh thu");
                model.addRow(item);
            }
        } catch (Exception e) {
        }
    }

    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    public void xuatBaoCao(JTable jTable) {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(this);
            File saveFile = jFileChooser.getSelectedFile();

            if (saveFile != null) {
                saveFile = new File(saveFile.toString() + ".xlsx");
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("Thống kê");

                Row rowCol = sheet.createRow(0);
                for (int i = 0; i < jTable.getColumnCount(); i++) {
                    Cell cell = rowCol.createCell(i);
                    cell.setCellValue(jTable.getColumnName(i));
                }

                for (int j = 0; j < jTable.getRowCount(); j++) {
                    Row row = sheet.createRow(j + 1);
                    for (int k = 0; k < jTable.getColumnCount(); k++) {
                        Cell cell = row.createCell(k);
                        if (jTable.getValueAt(j, k) != null) {
                            cell.setCellValue(jTable.getValueAt(j, k).toString());
                        }
                    }
                }
                FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                wb.write(out);
                wb.close();
                out.close();
                openFile(saveFile.toString());
            } else {
                JOptionPane.showMessageDialog(null, "Không thể tạo file");
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException io) {
            System.out.println(io);
        }
    }

    public void doChiNhanhComboBox(JComboBox jcb, String str) {
        String sql = "select * from ChiNhanh order by MaCN";
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            DefaultComboBoxModel cbbModel = (DefaultComboBoxModel) jcb.getModel();

            while (rs.next()) {
                jcb.addItem(rs.getString(str));
            }
            ps.close();
            rs.close();
            cons.close();
        } catch (Exception e) {
        }
    }

    public void tongDoanhThuChiNhanh(String sql) {
        String[] listColumn = {"Mã CN", "Tên CN", "Tổng số hóa đơn", "Tổng doanh thu"};
        model = new DefaultTableModel(listColumn, 0);
        jtbCN_Bang.setModel(model);
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] item = new Object[4];
                item[0] = rs.getString("Mã CN");
                item[1] = rs.getString("Tên CN");
                item[2] = rs.getString("Tổng số hóa đơn");
                item[3] = rs.getString("Tổng doanh thu");
                model.addRow(item);
            }
        } catch (Exception e) {
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpnDoanhThu = new javax.swing.JPanel();
        jpnDT_ThongKe = new javax.swing.JPanel();
        jpnDT_ThoiGian = new javax.swing.JPanel();
        jrdDT_TatCa = new javax.swing.JRadioButton();
        jrdDT_KhoangThoiGian = new javax.swing.JRadioButton();
        jdcDT_NgayBD = new com.toedter.calendar.JDateChooser();
        jdcDT_NgayKT = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jpnDT_PhanLoai = new javax.swing.JPanel();
        jrdDT_NhanVien = new javax.swing.JRadioButton();
        jrdDT_KhachHang = new javax.swing.JRadioButton();
        jpnDT_TongHoaDon = new javax.swing.JPanel();
        jtfDT_TongHoaDon = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jpnDT_TongDoanhThu = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jtfDT_TongDoanhThu = new javax.swing.JTextField();
        jpnDT_Bang = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbDT_Bang = new javax.swing.JTable();
        jbnDT_XuatBaoCao = new javax.swing.JButton();
        jbnDT_XemBieuDo = new javax.swing.JButton();
        jpnDT_BieuDo = new javax.swing.JPanel();
        jpnSanPham = new javax.swing.JPanel();
        jpnSP_ThongKe = new javax.swing.JPanel();
        jpnSP_ThoiGian = new javax.swing.JPanel();
        jrdSP_TatCa = new javax.swing.JRadioButton();
        jrdSP_KhoangThoiGian = new javax.swing.JRadioButton();
        jdcSP_NgayBD = new com.toedter.calendar.JDateChooser();
        jdcSP_NgayKT = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jpnSP_PhanLoai = new javax.swing.JPanel();
        jrdSP_SanPham = new javax.swing.JRadioButton();
        jrdSP_LoaiSanPham = new javax.swing.JRadioButton();
        jcbSP_MaLSP = new javax.swing.JComboBox<>();
        jpnSP_TongHoaDon = new javax.swing.JPanel();
        jtfSP_TongSoLuong = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jpnSP_TongDoanhThu = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jtfSP_TongDoanhThu = new javax.swing.JTextField();
        jpnSP_Bang = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtbSP_Bang = new javax.swing.JTable();
        jbnSP_XuatBaoCao = new javax.swing.JButton();
        jbnSP_XemBieuDo = new javax.swing.JButton();
        jpnChiNhanh = new javax.swing.JPanel();
        jpnCN_ThongKe = new javax.swing.JPanel();
        jpnCN_ThoiGian = new javax.swing.JPanel();
        jrdCN_TatCa = new javax.swing.JRadioButton();
        jrdCN_KhoangThoiGian = new javax.swing.JRadioButton();
        jdcCN_NgayBD = new com.toedter.calendar.JDateChooser();
        jdcCN_NgayKT = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jpnCN_PhanLoai = new javax.swing.JPanel();
        jrdCN_MaChiNhanh = new javax.swing.JRadioButton();
        jcbCN_MaChiNhanh = new javax.swing.JComboBox<>();
        jrdCN_TenChiNhanh = new javax.swing.JRadioButton();
        jcbCN_TenChiNhanh = new javax.swing.JComboBox<>();
        jpnCN_TongDoanhThu = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jtfCN_TongDoanhThu = new javax.swing.JTextField();
        jpnCN_Bang = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtbCN_Bang = new javax.swing.JTable();
        jbnCN_XuatBaoCao = new javax.swing.JButton();
        jbnCN_XemBieuDo = new javax.swing.JButton();
        jpnCN_BieuDo = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N

        jpnDoanhThu.setBackground(new java.awt.Color(255, 255, 255));

        jpnDT_ThongKe.setBackground(new java.awt.Color(255, 255, 255));

        jpnDT_ThoiGian.setBackground(new java.awt.Color(255, 255, 255));
        jpnDT_ThoiGian.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thời gian", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 17))); // NOI18N

        jrdDT_TatCa.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jrdDT_TatCa);
        jrdDT_TatCa.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdDT_TatCa.setSelected(true);
        jrdDT_TatCa.setText("Từ trước đến nay");
        jrdDT_TatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdDT_TatCaActionPerformed(evt);
            }
        });

        jrdDT_KhoangThoiGian.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jrdDT_KhoangThoiGian);
        jrdDT_KhoangThoiGian.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdDT_KhoangThoiGian.setText("Khoảng thời gian");
        jrdDT_KhoangThoiGian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdDT_KhoangThoiGianActionPerformed(evt);
            }
        });

        jdcDT_NgayBD.setBackground(new java.awt.Color(255, 255, 255));
        jdcDT_NgayBD.setDateFormatString("dd-MM-yyyy");
        jdcDT_NgayBD.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jdcDT_NgayKT.setBackground(new java.awt.Color(255, 255, 255));
        jdcDT_NgayKT.setDateFormatString("dd-MM-yyyy");
        jdcDT_NgayKT.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Từ");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("đến");

        javax.swing.GroupLayout jpnDT_ThoiGianLayout = new javax.swing.GroupLayout(jpnDT_ThoiGian);
        jpnDT_ThoiGian.setLayout(jpnDT_ThoiGianLayout);
        jpnDT_ThoiGianLayout.setHorizontalGroup(
            jpnDT_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDT_ThoiGianLayout.createSequentialGroup()
                .addGroup(jpnDT_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnDT_ThoiGianLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdcDT_NgayBD, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdcDT_NgayKT, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                    .addGroup(jpnDT_ThoiGianLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jpnDT_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jrdDT_TatCa)
                            .addComponent(jrdDT_KhoangThoiGian))))
                .addContainerGap())
        );
        jpnDT_ThoiGianLayout.setVerticalGroup(
            jpnDT_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDT_ThoiGianLayout.createSequentialGroup()
                .addComponent(jrdDT_TatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jrdDT_KhoangThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpnDT_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnDT_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jdcDT_NgayKT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnDT_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jdcDT_NgayBD, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8))
        );

        jpnDT_PhanLoai.setBackground(new java.awt.Color(255, 255, 255));
        jpnDT_PhanLoai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phân loại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 17))); // NOI18N

        jrdDT_NhanVien.setBackground(new java.awt.Color(255, 255, 255));
        jrdDT_NhanVien.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdDT_NhanVien.setText("Nhân Viên");
        jrdDT_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdDT_NhanVienActionPerformed(evt);
            }
        });

        jrdDT_KhachHang.setBackground(new java.awt.Color(255, 255, 255));
        jrdDT_KhachHang.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdDT_KhachHang.setText("Khách Hàng");
        jrdDT_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdDT_KhachHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnDT_PhanLoaiLayout = new javax.swing.GroupLayout(jpnDT_PhanLoai);
        jpnDT_PhanLoai.setLayout(jpnDT_PhanLoaiLayout);
        jpnDT_PhanLoaiLayout.setHorizontalGroup(
            jpnDT_PhanLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDT_PhanLoaiLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jpnDT_PhanLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jrdDT_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jrdDT_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jpnDT_PhanLoaiLayout.setVerticalGroup(
            jpnDT_PhanLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDT_PhanLoaiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jrdDT_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jrdDT_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnDT_TongHoaDon.setBackground(new java.awt.Color(157, 205, 239));

        jtfDT_TongHoaDon.setEditable(false);
        jtfDT_TongHoaDon.setBackground(new java.awt.Color(255, 255, 255));
        jtfDT_TongHoaDon.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jtfDT_TongHoaDon.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Tổng Số Hóa Đơn");

        javax.swing.GroupLayout jpnDT_TongHoaDonLayout = new javax.swing.GroupLayout(jpnDT_TongHoaDon);
        jpnDT_TongHoaDon.setLayout(jpnDT_TongHoaDonLayout);
        jpnDT_TongHoaDonLayout.setHorizontalGroup(
            jpnDT_TongHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDT_TongHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnDT_TongHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfDT_TongHoaDon)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpnDT_TongHoaDonLayout.setVerticalGroup(
            jpnDT_TongHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnDT_TongHoaDonLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfDT_TongHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jpnDT_TongDoanhThu.setBackground(new java.awt.Color(157, 205, 239));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Tổng Doanh Thu");

        jtfDT_TongDoanhThu.setEditable(false);
        jtfDT_TongDoanhThu.setBackground(new java.awt.Color(255, 255, 255));
        jtfDT_TongDoanhThu.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jtfDT_TongDoanhThu.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jpnDT_TongDoanhThuLayout = new javax.swing.GroupLayout(jpnDT_TongDoanhThu);
        jpnDT_TongDoanhThu.setLayout(jpnDT_TongDoanhThuLayout);
        jpnDT_TongDoanhThuLayout.setHorizontalGroup(
            jpnDT_TongDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDT_TongDoanhThuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnDT_TongDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfDT_TongDoanhThu)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpnDT_TongDoanhThuLayout.setVerticalGroup(
            jpnDT_TongDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDT_TongDoanhThuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfDT_TongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jpnDT_ThongKeLayout = new javax.swing.GroupLayout(jpnDT_ThongKe);
        jpnDT_ThongKe.setLayout(jpnDT_ThongKeLayout);
        jpnDT_ThongKeLayout.setHorizontalGroup(
            jpnDT_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDT_ThongKeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpnDT_ThoiGian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnDT_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jpnDT_TongHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpnDT_TongDoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnDT_ThongKeLayout.setVerticalGroup(
            jpnDT_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDT_ThongKeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnDT_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpnDT_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jpnDT_TongDoanhThu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpnDT_TongHoaDon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnDT_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jpnDT_ThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jpnDT_PhanLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnDT_Bang.setBackground(new java.awt.Color(255, 255, 255));

        jtbDT_Bang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jtbDT_Bang);

        jbnDT_XuatBaoCao.setBackground(new java.awt.Color(157, 205, 239));
        jbnDT_XuatBaoCao.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnDT_XuatBaoCao.setText("Xuất Báo Cáo");
        jbnDT_XuatBaoCao.setBorder(null);
        jbnDT_XuatBaoCao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnDT_XuatBaoCaoActionPerformed(evt);
            }
        });

        jbnDT_XemBieuDo.setBackground(new java.awt.Color(157, 205, 239));
        jbnDT_XemBieuDo.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnDT_XemBieuDo.setText("Xem Biểu Đồ");
        jbnDT_XemBieuDo.setBorder(null);
        jbnDT_XemBieuDo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnDT_XemBieuDoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnDT_BangLayout = new javax.swing.GroupLayout(jpnDT_Bang);
        jpnDT_Bang.setLayout(jpnDT_BangLayout);
        jpnDT_BangLayout.setHorizontalGroup(
            jpnDT_BangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnDT_BangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbnDT_XuatBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbnDT_XemBieuDo, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpnDT_BangLayout.setVerticalGroup(
            jpnDT_BangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDT_BangLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnDT_BangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbnDT_XemBieuDo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbnDT_XuatBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jpnDT_BieuDo.setBackground(new java.awt.Color(255, 255, 255));
        jpnDT_BieuDo.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jpnDoanhThuLayout = new javax.swing.GroupLayout(jpnDoanhThu);
        jpnDoanhThu.setLayout(jpnDoanhThuLayout);
        jpnDoanhThuLayout.setHorizontalGroup(
            jpnDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnDoanhThuLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jpnDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpnDoanhThuLayout.createSequentialGroup()
                        .addComponent(jpnDT_Bang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpnDT_BieuDo, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE))
                    .addComponent(jpnDT_ThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );
        jpnDoanhThuLayout.setVerticalGroup(
            jpnDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDoanhThuLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jpnDT_ThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpnDT_BieuDo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnDT_Bang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Doanh Thu", jpnDoanhThu);

        jpnSanPham.setBackground(new java.awt.Color(255, 255, 255));

        jpnSP_ThongKe.setBackground(new java.awt.Color(255, 255, 255));

        jpnSP_ThoiGian.setBackground(new java.awt.Color(255, 255, 255));
        jpnSP_ThoiGian.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thời gian", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 17))); // NOI18N

        jrdSP_TatCa.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(jrdSP_TatCa);
        jrdSP_TatCa.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdSP_TatCa.setSelected(true);
        jrdSP_TatCa.setText("Từ trước đến nay");
        jrdSP_TatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdSP_TatCaActionPerformed(evt);
            }
        });

        jrdSP_KhoangThoiGian.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(jrdSP_KhoangThoiGian);
        jrdSP_KhoangThoiGian.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdSP_KhoangThoiGian.setText("Khoảng thời gian");
        jrdSP_KhoangThoiGian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdSP_KhoangThoiGianActionPerformed(evt);
            }
        });

        jdcSP_NgayBD.setBackground(new java.awt.Color(255, 255, 255));
        jdcSP_NgayBD.setDateFormatString("dd-MM-yyyy");
        jdcSP_NgayBD.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jdcSP_NgayKT.setBackground(new java.awt.Color(255, 255, 255));
        jdcSP_NgayKT.setDateFormatString("dd-MM-yyyy");
        jdcSP_NgayKT.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Từ");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("đến");

        javax.swing.GroupLayout jpnSP_ThoiGianLayout = new javax.swing.GroupLayout(jpnSP_ThoiGian);
        jpnSP_ThoiGian.setLayout(jpnSP_ThoiGianLayout);
        jpnSP_ThoiGianLayout.setHorizontalGroup(
            jpnSP_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSP_ThoiGianLayout.createSequentialGroup()
                .addGroup(jpnSP_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnSP_ThoiGianLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdcSP_NgayBD, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdcSP_NgayKT, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                    .addGroup(jpnSP_ThoiGianLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jpnSP_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jrdSP_TatCa)
                            .addComponent(jrdSP_KhoangThoiGian))))
                .addContainerGap())
        );
        jpnSP_ThoiGianLayout.setVerticalGroup(
            jpnSP_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSP_ThoiGianLayout.createSequentialGroup()
                .addComponent(jrdSP_TatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jrdSP_KhoangThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpnSP_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnSP_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jdcSP_NgayKT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnSP_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jdcSP_NgayBD, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8))
        );

        jpnSP_PhanLoai.setBackground(new java.awt.Color(255, 255, 255));
        jpnSP_PhanLoai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phân loại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 17))); // NOI18N

        jrdSP_SanPham.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup3.add(jrdSP_SanPham);
        jrdSP_SanPham.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdSP_SanPham.setSelected(true);
        jrdSP_SanPham.setText("Sản Phẩm");
        jrdSP_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdSP_SanPhamActionPerformed(evt);
            }
        });

        jrdSP_LoaiSanPham.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup3.add(jrdSP_LoaiSanPham);
        jrdSP_LoaiSanPham.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdSP_LoaiSanPham.setText("Loại Sản Phẩm");
        jrdSP_LoaiSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdSP_LoaiSanPhamActionPerformed(evt);
            }
        });

        jcbSP_MaLSP.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jcbSP_MaLSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        jcbSP_MaLSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbSP_MaLSPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnSP_PhanLoaiLayout = new javax.swing.GroupLayout(jpnSP_PhanLoai);
        jpnSP_PhanLoai.setLayout(jpnSP_PhanLoaiLayout);
        jpnSP_PhanLoaiLayout.setHorizontalGroup(
            jpnSP_PhanLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSP_PhanLoaiLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jpnSP_PhanLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jrdSP_LoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jrdSP_SanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnSP_PhanLoaiLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jcbSP_MaLSP, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpnSP_PhanLoaiLayout.setVerticalGroup(
            jpnSP_PhanLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSP_PhanLoaiLayout.createSequentialGroup()
                .addComponent(jrdSP_SanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jrdSP_LoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jcbSP_MaLSP, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jpnSP_TongHoaDon.setBackground(new java.awt.Color(157, 205, 239));

        jtfSP_TongSoLuong.setEditable(false);
        jtfSP_TongSoLuong.setBackground(new java.awt.Color(255, 255, 255));
        jtfSP_TongSoLuong.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jtfSP_TongSoLuong.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Số Lượng Đã Bán");

        javax.swing.GroupLayout jpnSP_TongHoaDonLayout = new javax.swing.GroupLayout(jpnSP_TongHoaDon);
        jpnSP_TongHoaDon.setLayout(jpnSP_TongHoaDonLayout);
        jpnSP_TongHoaDonLayout.setHorizontalGroup(
            jpnSP_TongHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSP_TongHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnSP_TongHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfSP_TongSoLuong)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpnSP_TongHoaDonLayout.setVerticalGroup(
            jpnSP_TongHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnSP_TongHoaDonLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfSP_TongSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jpnSP_TongDoanhThu.setBackground(new java.awt.Color(157, 205, 239));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Tổng Doanh Thu");

        jtfSP_TongDoanhThu.setEditable(false);
        jtfSP_TongDoanhThu.setBackground(new java.awt.Color(255, 255, 255));
        jtfSP_TongDoanhThu.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jtfSP_TongDoanhThu.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jpnSP_TongDoanhThuLayout = new javax.swing.GroupLayout(jpnSP_TongDoanhThu);
        jpnSP_TongDoanhThu.setLayout(jpnSP_TongDoanhThuLayout);
        jpnSP_TongDoanhThuLayout.setHorizontalGroup(
            jpnSP_TongDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSP_TongDoanhThuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnSP_TongDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfSP_TongDoanhThu)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpnSP_TongDoanhThuLayout.setVerticalGroup(
            jpnSP_TongDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSP_TongDoanhThuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfSP_TongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jpnSP_ThongKeLayout = new javax.swing.GroupLayout(jpnSP_ThongKe);
        jpnSP_ThongKe.setLayout(jpnSP_ThongKeLayout);
        jpnSP_ThongKeLayout.setHorizontalGroup(
            jpnSP_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSP_ThongKeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpnSP_ThoiGian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnSP_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jpnSP_TongHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpnSP_TongDoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnSP_ThongKeLayout.setVerticalGroup(
            jpnSP_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSP_ThongKeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnSP_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpnSP_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jpnSP_TongDoanhThu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpnSP_TongHoaDon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnSP_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jpnSP_ThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jpnSP_PhanLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnSP_Bang.setBackground(new java.awt.Color(255, 255, 255));

        jtbSP_Bang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jtbSP_Bang);

        jbnSP_XuatBaoCao.setBackground(new java.awt.Color(157, 205, 239));
        jbnSP_XuatBaoCao.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnSP_XuatBaoCao.setText("Xuất Báo Cáo");
        jbnSP_XuatBaoCao.setBorder(null);
        jbnSP_XuatBaoCao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnSP_XuatBaoCaoActionPerformed(evt);
            }
        });

        jbnSP_XemBieuDo.setBackground(new java.awt.Color(157, 205, 239));
        jbnSP_XemBieuDo.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnSP_XemBieuDo.setText("Xem Biểu Đồ");
        jbnSP_XemBieuDo.setBorder(null);
        jbnSP_XemBieuDo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnSP_XemBieuDoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnSP_BangLayout = new javax.swing.GroupLayout(jpnSP_Bang);
        jpnSP_Bang.setLayout(jpnSP_BangLayout);
        jpnSP_BangLayout.setHorizontalGroup(
            jpnSP_BangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnSP_BangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbnSP_XuatBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbnSP_XemBieuDo, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpnSP_BangLayout.setVerticalGroup(
            jpnSP_BangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSP_BangLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnSP_BangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbnSP_XemBieuDo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbnSP_XuatBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jpnSanPhamLayout = new javax.swing.GroupLayout(jpnSanPham);
        jpnSanPham.setLayout(jpnSanPhamLayout);
        jpnSanPhamLayout.setHorizontalGroup(
            jpnSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnSanPhamLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jpnSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpnSP_ThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnSP_Bang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );
        jpnSanPhamLayout.setVerticalGroup(
            jpnSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSanPhamLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jpnSP_ThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnSP_Bang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Sản Phẩm", jpnSanPham);

        jpnChiNhanh.setBackground(new java.awt.Color(255, 255, 255));

        jpnCN_ThongKe.setBackground(new java.awt.Color(255, 255, 255));

        jpnCN_ThoiGian.setBackground(new java.awt.Color(255, 255, 255));
        jpnCN_ThoiGian.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thời gian", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 17))); // NOI18N

        jrdCN_TatCa.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup4.add(jrdCN_TatCa);
        jrdCN_TatCa.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdCN_TatCa.setSelected(true);
        jrdCN_TatCa.setText("Từ trước đến nay");
        jrdCN_TatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdCN_TatCaActionPerformed(evt);
            }
        });

        jrdCN_KhoangThoiGian.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup4.add(jrdCN_KhoangThoiGian);
        jrdCN_KhoangThoiGian.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdCN_KhoangThoiGian.setText("Khoảng thời gian");
        jrdCN_KhoangThoiGian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdCN_KhoangThoiGianActionPerformed(evt);
            }
        });

        jdcCN_NgayBD.setBackground(new java.awt.Color(255, 255, 255));
        jdcCN_NgayBD.setDateFormatString("dd-MM-yyyy");
        jdcCN_NgayBD.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jdcCN_NgayKT.setBackground(new java.awt.Color(255, 255, 255));
        jdcCN_NgayKT.setDateFormatString("dd-MM-yyyy");
        jdcCN_NgayKT.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Từ");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("đến");

        javax.swing.GroupLayout jpnCN_ThoiGianLayout = new javax.swing.GroupLayout(jpnCN_ThoiGian);
        jpnCN_ThoiGian.setLayout(jpnCN_ThoiGianLayout);
        jpnCN_ThoiGianLayout.setHorizontalGroup(
            jpnCN_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCN_ThoiGianLayout.createSequentialGroup()
                .addGroup(jpnCN_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnCN_ThoiGianLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdcCN_NgayBD, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdcCN_NgayKT, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                    .addGroup(jpnCN_ThoiGianLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jpnCN_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jrdCN_TatCa)
                            .addComponent(jrdCN_KhoangThoiGian))))
                .addContainerGap())
        );
        jpnCN_ThoiGianLayout.setVerticalGroup(
            jpnCN_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCN_ThoiGianLayout.createSequentialGroup()
                .addComponent(jrdCN_TatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jrdCN_KhoangThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpnCN_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnCN_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jdcCN_NgayKT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnCN_ThoiGianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jdcCN_NgayBD, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8))
        );

        jpnCN_PhanLoai.setBackground(new java.awt.Color(255, 255, 255));
        jpnCN_PhanLoai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phân loại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 17))); // NOI18N

        jrdCN_MaChiNhanh.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup5.add(jrdCN_MaChiNhanh);
        jrdCN_MaChiNhanh.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdCN_MaChiNhanh.setSelected(true);
        jrdCN_MaChiNhanh.setText("Mã Chi Nhánh");
        jrdCN_MaChiNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdCN_MaChiNhanhActionPerformed(evt);
            }
        });

        jcbCN_MaChiNhanh.setFont(new java.awt.Font("Times New Roman", 0, 15)); // NOI18N
        jcbCN_MaChiNhanh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        jcbCN_MaChiNhanh.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbCN_MaChiNhanhItemStateChanged(evt);
            }
        });
        jcbCN_MaChiNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbCN_MaChiNhanhActionPerformed(evt);
            }
        });

        jrdCN_TenChiNhanh.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup5.add(jrdCN_TenChiNhanh);
        jrdCN_TenChiNhanh.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jrdCN_TenChiNhanh.setText("Tên Chi Nhánh");
        jrdCN_TenChiNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdCN_TenChiNhanhActionPerformed(evt);
            }
        });

        jcbCN_TenChiNhanh.setFont(new java.awt.Font("Times New Roman", 0, 15)); // NOI18N
        jcbCN_TenChiNhanh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        jcbCN_TenChiNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbCN_TenChiNhanhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnCN_PhanLoaiLayout = new javax.swing.GroupLayout(jpnCN_PhanLoai);
        jpnCN_PhanLoai.setLayout(jpnCN_PhanLoaiLayout);
        jpnCN_PhanLoaiLayout.setHorizontalGroup(
            jpnCN_PhanLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCN_PhanLoaiLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jpnCN_PhanLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jrdCN_TenChiNhanh, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(jrdCN_MaChiNhanh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnCN_PhanLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbCN_MaChiNhanh, 0, 167, Short.MAX_VALUE)
                    .addComponent(jcbCN_TenChiNhanh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpnCN_PhanLoaiLayout.setVerticalGroup(
            jpnCN_PhanLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCN_PhanLoaiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnCN_PhanLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jcbCN_MaChiNhanh)
                    .addComponent(jrdCN_MaChiNhanh, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jpnCN_PhanLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jrdCN_TenChiNhanh, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(jcbCN_TenChiNhanh))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnCN_TongDoanhThu.setBackground(new java.awt.Color(157, 205, 239));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Tổng Doanh Thu");

        jtfCN_TongDoanhThu.setEditable(false);
        jtfCN_TongDoanhThu.setBackground(new java.awt.Color(255, 255, 255));
        jtfCN_TongDoanhThu.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jtfCN_TongDoanhThu.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jpnCN_TongDoanhThuLayout = new javax.swing.GroupLayout(jpnCN_TongDoanhThu);
        jpnCN_TongDoanhThu.setLayout(jpnCN_TongDoanhThuLayout);
        jpnCN_TongDoanhThuLayout.setHorizontalGroup(
            jpnCN_TongDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCN_TongDoanhThuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnCN_TongDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfCN_TongDoanhThu)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpnCN_TongDoanhThuLayout.setVerticalGroup(
            jpnCN_TongDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCN_TongDoanhThuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfCN_TongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jpnCN_ThongKeLayout = new javax.swing.GroupLayout(jpnCN_ThongKe);
        jpnCN_ThongKe.setLayout(jpnCN_ThongKeLayout);
        jpnCN_ThongKeLayout.setHorizontalGroup(
            jpnCN_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCN_ThongKeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpnCN_ThoiGian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnCN_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jpnCN_TongDoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnCN_ThongKeLayout.setVerticalGroup(
            jpnCN_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCN_ThongKeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnCN_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpnCN_TongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpnCN_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jpnCN_ThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jpnCN_PhanLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnCN_Bang.setBackground(new java.awt.Color(255, 255, 255));

        jtbCN_Bang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(jtbCN_Bang);

        jbnCN_XuatBaoCao.setBackground(new java.awt.Color(157, 205, 239));
        jbnCN_XuatBaoCao.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnCN_XuatBaoCao.setText("Xuất Báo Cáo");
        jbnCN_XuatBaoCao.setBorder(null);
        jbnCN_XuatBaoCao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnCN_XuatBaoCaoActionPerformed(evt);
            }
        });

        jbnCN_XemBieuDo.setBackground(new java.awt.Color(157, 205, 239));
        jbnCN_XemBieuDo.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jbnCN_XemBieuDo.setText("Xem Biểu Đồ");
        jbnCN_XemBieuDo.setBorder(null);
        jbnCN_XemBieuDo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnCN_XemBieuDoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnCN_BangLayout = new javax.swing.GroupLayout(jpnCN_Bang);
        jpnCN_Bang.setLayout(jpnCN_BangLayout);
        jpnCN_BangLayout.setHorizontalGroup(
            jpnCN_BangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnCN_BangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbnCN_XuatBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbnCN_XemBieuDo, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpnCN_BangLayout.setVerticalGroup(
            jpnCN_BangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCN_BangLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnCN_BangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbnCN_XemBieuDo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbnCN_XuatBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jpnCN_BieuDo.setBackground(new java.awt.Color(255, 255, 255));
        jpnCN_BieuDo.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jpnChiNhanhLayout = new javax.swing.GroupLayout(jpnChiNhanh);
        jpnChiNhanh.setLayout(jpnChiNhanhLayout);
        jpnChiNhanhLayout.setHorizontalGroup(
            jpnChiNhanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnChiNhanhLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jpnChiNhanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpnChiNhanhLayout.createSequentialGroup()
                        .addComponent(jpnCN_Bang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpnCN_BieuDo, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE))
                    .addComponent(jpnCN_ThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );
        jpnChiNhanhLayout.setVerticalGroup(
            jpnChiNhanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnChiNhanhLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jpnCN_ThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnChiNhanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpnCN_BieuDo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnCN_Bang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Chi Nhánh", jpnChiNhanh);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbnDT_XuatBaoCaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnDT_XuatBaoCaoActionPerformed
        xuatBaoCao(jtbDT_Bang);
    }//GEN-LAST:event_jbnDT_XuatBaoCaoActionPerformed

    private void jbnDT_XemBieuDoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnDT_XemBieuDoActionPerformed
        JFrame bieuDoJFrame = new JFrame();
        JPanel jpnView = new JPanel();
        jpnView.setLayout(new java.awt.BorderLayout());

        if (!jrdDT_NhanVien.isSelected() && !jrdDT_KhachHang.isSelected()) {
            bieuDoDuong(jpnView);
            bieuDoJFrame.setTitle("Thống kê doanh thu bán hàng");
        } else {
            String tenBieuDo = "";
            if (jrdDT_NhanVien.isSelected() && !jrdDT_KhachHang.isSelected()) {
                bieuDoJFrame.setTitle("Thống kê doanh số bán hàng của nhân viên");
                tenBieuDo = "Thống kê doanh số bán hàng của nhân viên";
            } else {
                bieuDoJFrame.setTitle("Thống kê doanh thu bán hàng theo khách hàng");
                tenBieuDo = "Thống kê doanh thu bán hàng theo khách hàng";
            }
            bieuDoCot(jtbDT_Bang, jpnView, tenBieuDo, "Tổng doanh thu");
        }

        bieuDoJFrame.add(jpnView, BorderLayout.CENTER);
        bieuDoJFrame.setSize(1100, 700);
        bieuDoJFrame.setVisible(true);
        bieuDoJFrame.setLocationRelativeTo(null);
        bieuDoJFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_jbnDT_XemBieuDoActionPerformed

    private void jrdDT_TatCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdDT_TatCaActionPerformed
        if (!jrdDT_NhanVien.isSelected() && !jrdDT_KhachHang.isSelected()) {
            tatCaDoanhThu();
        } else {
            if (jrdDT_NhanVien.isSelected()) {
                doanhSoBanHangNhanVien(null, null);
            } else {
                doanhThuBanHangTheoKhachHang(null, null);
            }
        }
    }//GEN-LAST:event_jrdDT_TatCaActionPerformed

    private void jrdDT_KhoangThoiGianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdDT_KhoangThoiGianActionPerformed
        if (jdcDT_NgayBD.getDate() == null || jdcDT_NgayKT.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khoảng thời gian");
            jrdDT_TatCa.setSelected(true);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngayBD = sdf.format(jdcDT_NgayBD.getDate());
            String ngayKT = sdf.format(jdcDT_NgayKT.getDate());
            if (!jrdDT_NhanVien.isSelected() && !jrdDT_KhachHang.isSelected()) {
                doanhThuKhoangThoiGian(ngayBD, ngayKT);
            } else {
                if (jrdDT_NhanVien.isSelected()) {
                    doanhSoBanHangNhanVien(ngayBD, ngayKT);
                } else {
                    doanhThuBanHangTheoKhachHang(ngayBD, ngayKT);
                }
            }
        }
    }//GEN-LAST:event_jrdDT_KhoangThoiGianActionPerformed

    private void jrdDT_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdDT_NhanVienActionPerformed
        jrdDT_KhachHang.setSelected(false);

        if (jrdDT_NhanVien.isSelected()) {
            if (jrdDT_TatCa.isSelected()) {
                doanhSoBanHangNhanVien(null, null);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String ngayBD = sdf.format(jdcDT_NgayBD.getDate());
                String ngayKT = sdf.format(jdcDT_NgayKT.getDate());
                doanhSoBanHangNhanVien(ngayBD, ngayKT);
            }
        } else {
            if (jrdDT_TatCa.isSelected()) {
                tatCaDoanhThu();
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String ngayBD = sdf.format(jdcDT_NgayBD.getDate());
                String ngayKT = sdf.format(jdcDT_NgayKT.getDate());
                doanhThuKhoangThoiGian(ngayBD, ngayKT);
            }
        }
    }//GEN-LAST:event_jrdDT_NhanVienActionPerformed

    private void jrdDT_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdDT_KhachHangActionPerformed
        jrdDT_NhanVien.setSelected(false);

        if (jrdDT_KhachHang.isSelected()) {
            if (jrdDT_TatCa.isSelected()) {
                doanhThuBanHangTheoKhachHang(null, null);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String ngayBD = sdf.format(jdcDT_NgayBD.getDate());
                String ngayKT = sdf.format(jdcDT_NgayKT.getDate());
                doanhThuBanHangTheoKhachHang(ngayBD, ngayKT);
            }
        } else {
            if (jrdDT_TatCa.isSelected()) {
                tatCaDoanhThu();
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String ngayBD = sdf.format(jdcDT_NgayBD.getDate());
                String ngayKT = sdf.format(jdcDT_NgayKT.getDate());
                doanhThuKhoangThoiGian(ngayBD, ngayKT);
            }
        }
    }//GEN-LAST:event_jrdDT_KhachHangActionPerformed

    private void jrdSP_TatCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdSP_TatCaActionPerformed
        if (jrdSP_SanPham.isSelected()) {
            String sql = "select sp.MaSP [Mã SP], TenSP [Tên SP], MaLSP [Mã LSP], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                    + "from SanPham sp left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP\n"
                    + "group by  MaLSP, sp.MaSP, TenSP";
            doanhThuSanPham(sql);
        } else {
            if (jcbSP_MaLSP.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                String sql = "select lsp.MaLSP [Mã LSP], TenLSP [Tên LSP], count(distinct sp.MaSP) [Số lượng sản phẩm], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from LoaiSanPham lsp left join SanPham sp on lsp.MaLSP = sp.MaLSP left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP\n"
                        + "group by lsp.MaLSP, TenLSP order by lsp.MaLSP, TenLSP";
                doanhThuLoaiSanPham(sql);
            } else {
                String maLSP = jcbSP_MaLSP.getSelectedItem().toString();
                String sql = "select sp.MaSP [Mã SP], TenSP [Tên SP], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from SanPham sp left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP\n"
                        + "where MaLSP = '" + maLSP + "' group by sp.MaSP, TenSP";
                doanhThuSanPhamTheoLoaiSanPham(sql);
            }
        }
        tinhTong(jtbSP_Bang, "Số lượng đã bán", jtfSP_TongSoLuong);
        tinhTong(jtbSP_Bang, "Tổng doanh thu", jtfSP_TongDoanhThu);
    }//GEN-LAST:event_jrdSP_TatCaActionPerformed

    private void jrdSP_KhoangThoiGianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdSP_KhoangThoiGianActionPerformed
        if (jdcSP_NgayBD.getDate() == null || jdcSP_NgayKT.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khoảng thời gian");
            jrdSP_TatCa.setSelected(true);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngayBD = sdf.format(jdcSP_NgayBD.getDate());
            String ngayKT = sdf.format(jdcSP_NgayKT.getDate());
            if (jrdSP_SanPham.isSelected()) {
                String sql = "select sp.MaSP [Mã SP], TenSP [Tên SP], MaLSP [Mã LSP], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from SanPham sp left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP left join HoaDon hd on cthd.MaHD = hd.MaHD\n"
                        + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by  MaLSP, sp.MaSP, TenSP";
                doanhThuSanPham(sql);
            } else {
                if (jcbSP_MaLSP.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                    String sql = "select lsp.MaLSP [Mã LSP], TenLSP [Tên LSP], count(distinct sp.MaSP) [Số lượng sản phẩm], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                            + "from LoaiSanPham lsp left join SanPham sp on lsp.MaLSP = sp.MaLSP left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP left join HoaDon hd on cthd.MaHD = hd.MaHD\n"
                            + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by lsp.MaLSP, TenLSP order by lsp.MaLSP, TenLSP";
                    doanhThuLoaiSanPham(sql);
                } else {
                    String maLSP = jcbSP_MaLSP.getSelectedItem().toString();
                    String sql = "select sp.MaSP [Mã SP], TenSP [Tên SP], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                            + "from SanPham sp left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP left join HoaDon hd on cthd.MaHD = hd.MaHD\n"
                            + "where MaLSP = '" + maLSP + "' and NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by sp.MaSP, TenSP";
                    doanhThuSanPhamTheoLoaiSanPham(sql);
                }
            }
            tinhTong(jtbSP_Bang, "Số lượng đã bán", jtfSP_TongSoLuong);
            tinhTong(jtbSP_Bang, "Tổng doanh thu", jtfSP_TongDoanhThu);
        }
    }//GEN-LAST:event_jrdSP_KhoangThoiGianActionPerformed

    private void jrdSP_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdSP_SanPhamActionPerformed
        if (jrdSP_TatCa.isSelected()) {
            String sql = "select sp.MaSP [Mã SP], TenSP [Tên SP], MaLSP [Mã LSP], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                    + "from SanPham sp left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP\n"
                    + "group by  MaLSP, sp.MaSP, TenSP";
            doanhThuSanPham(sql);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngayBD = sdf.format(jdcSP_NgayBD.getDate());
            String ngayKT = sdf.format(jdcSP_NgayKT.getDate());
            String sql = "select sp.MaSP [Mã SP], TenSP [Tên SP], MaLSP [Mã LSP], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                    + "from SanPham sp left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP left join HoaDon hd on cthd.MaHD = hd.MaHD\n"
                    + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by  MaLSP, sp.MaSP, TenSP";
            doanhThuSanPham(sql);
        }
        tinhTong(jtbSP_Bang, "Số lượng đã bán", jtfSP_TongSoLuong);
        tinhTong(jtbSP_Bang, "Tổng doanh thu", jtfSP_TongDoanhThu);
    }//GEN-LAST:event_jrdSP_SanPhamActionPerformed

    private void jrdSP_LoaiSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdSP_LoaiSanPhamActionPerformed
        if (jrdSP_TatCa.isSelected()) {
            if (jcbSP_MaLSP.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                String sql = "select lsp.MaLSP [Mã LSP], TenLSP [Tên LSP], count(distinct sp.MaSP) [Số lượng sản phẩm], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from LoaiSanPham lsp left join SanPham sp on lsp.MaLSP = sp.MaLSP left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP\n"
                        + "group by lsp.MaLSP, TenLSP order by lsp.MaLSP, TenLSP";
                doanhThuLoaiSanPham(sql);
            } else {
                String maLSP = jcbSP_MaLSP.getSelectedItem().toString();
                String sql = "select sp.MaSP [Mã SP], TenSP [Tên SP], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from SanPham sp left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP\n"
                        + "where MaLSP = '" + maLSP + "' group by sp.MaSP, TenSP";
                doanhThuSanPhamTheoLoaiSanPham(sql);
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngayBD = sdf.format(jdcSP_NgayBD.getDate());
            String ngayKT = sdf.format(jdcSP_NgayKT.getDate());
            if (jcbSP_MaLSP.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                String sql = "select lsp.MaLSP [Mã LSP], TenLSP [Tên LSP], count(distinct sp.MaSP) [Số lượng sản phẩm], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from LoaiSanPham lsp left join SanPham sp on lsp.MaLSP = sp.MaLSP left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP left join HoaDon hd on cthd.MaHD = hd.MaHD\n"
                        + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by lsp.MaLSP, TenLSP order by lsp.MaLSP, TenLSP";
                doanhThuLoaiSanPham(sql);
            } else {
                String maLSP = jcbSP_MaLSP.getSelectedItem().toString();
                String sql = "select sp.MaSP [Mã SP], TenSP [Tên SP], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from SanPham sp left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP left join HoaDon hd on cthd.MaHD = hd.MaHD\n"
                        + "where MaLSP = '" + maLSP + "' and NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by sp.MaSP, TenSP";
                doanhThuSanPhamTheoLoaiSanPham(sql);
            }
        }
        tinhTong(jtbSP_Bang, "Số lượng đã bán", jtfSP_TongSoLuong);
        tinhTong(jtbSP_Bang, "Tổng doanh thu", jtfSP_TongDoanhThu);
    }//GEN-LAST:event_jrdSP_LoaiSanPhamActionPerformed

    private void jbnSP_XuatBaoCaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnSP_XuatBaoCaoActionPerformed
        xuatBaoCao(jtbSP_Bang);
    }//GEN-LAST:event_jbnSP_XuatBaoCaoActionPerformed

    private void jbnSP_XemBieuDoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnSP_XemBieuDoActionPerformed
        JFrame bieuDoJFrame = new JFrame();
        JPanel jpnView = new JPanel();
        jpnView.setLayout(new java.awt.BorderLayout());

        String tenBieuDo = "";
        if (jrdSP_SanPham.isSelected()) {
            bieuDoJFrame.setTitle("Thống kê doanh thu sản phẩm");
            tenBieuDo = "Thống kê doanh thu sản phẩm";
        } else {
            bieuDoJFrame.setTitle("Thống kê doanh thu loại sản phẩm");
            tenBieuDo = "Thống kê doanh thu loại sản phẩm";
        }
        bieuDoCot(jtbSP_Bang, jpnView, tenBieuDo, "Tổng doanh thu");

        bieuDoJFrame.add(jpnView, BorderLayout.CENTER);
        bieuDoJFrame.setSize(1100, 700);
        bieuDoJFrame.setVisible(true);
        bieuDoJFrame.setLocationRelativeTo(null);
        bieuDoJFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_jbnSP_XemBieuDoActionPerformed

    private void jcbSP_MaLSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbSP_MaLSPActionPerformed
        if (jrdSP_LoaiSanPham.isSelected()) {
            if (jrdSP_TatCa.isSelected()) {
                if (jcbSP_MaLSP.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                    String sql = "select lsp.MaLSP [Mã LSP], TenLSP [Tên LSP], count(distinct sp.MaSP) [Số lượng sản phẩm], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                            + "from LoaiSanPham lsp left join SanPham sp on lsp.MaLSP = sp.MaLSP left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP\n"
                            + "group by lsp.MaLSP, TenLSP order by lsp.MaLSP, TenLSP";
                    doanhThuLoaiSanPham(sql);
                } else {
                    String maLSP = jcbSP_MaLSP.getSelectedItem().toString();
                    String sql = "select sp.MaSP [Mã SP], TenSP [Tên SP], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                            + "from SanPham sp left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP\n"
                            + "where MaLSP = '" + maLSP + "' group by sp.MaSP, TenSP";
                    doanhThuSanPhamTheoLoaiSanPham(sql);
                }
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String ngayBD = sdf.format(jdcSP_NgayBD.getDate());
                String ngayKT = sdf.format(jdcSP_NgayKT.getDate());
                if (jcbSP_MaLSP.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                    String sql = "select lsp.MaLSP [Mã LSP], TenLSP [Tên LSP], count(distinct sp.MaSP) [Số lượng sản phẩm], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                            + "from LoaiSanPham lsp left join SanPham sp on lsp.MaLSP = sp.MaLSP left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP left join HoaDon hd on cthd.MaHD = hd.MaHD\n"
                            + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by lsp.MaLSP, TenLSP order by lsp.MaLSP, TenLSP";
                    doanhThuLoaiSanPham(sql);
                } else {
                    String maLSP = jcbSP_MaLSP.getSelectedItem().toString();
                    String sql = "select sp.MaSP [Mã SP], TenSP [Tên SP], isnull(sum(SLDat),0) [Số lượng đã bán], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                            + "from SanPham sp left join ChiTietHoaDon cthd on sp.MaSP = cthd.MaSP left join HoaDon hd on cthd.MaHD = hd.MaHD\n"
                            + "where MaLSP = '" + maLSP + "' and NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by sp.MaSP, TenSP";
                    doanhThuSanPhamTheoLoaiSanPham(sql);
                }
            }
            tinhTong(jtbSP_Bang, "Số lượng đã bán", jtfSP_TongSoLuong);
            tinhTong(jtbSP_Bang, "Tổng doanh thu", jtfSP_TongDoanhThu);
        }
    }//GEN-LAST:event_jcbSP_MaLSPActionPerformed

    private void jrdCN_TatCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdCN_TatCaActionPerformed
        if (jrdCN_MaChiNhanh.isSelected()) {
            if (jcbCN_MaChiNhanh.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                String sql = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                        + "group by cn.MaCN, TenCN";
                tongDoanhThuChiNhanh(sql);
            } else {
                String maCN = jcbCN_MaChiNhanh.getSelectedItem().toString();
                String sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                        + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                        + "where cn.MaCN = N'" + maCN + "' group by nv.MaNV, HoTenNV";
                tongDoanhThuNhanVien(sql, jtbCN_Bang);
            }
        } else {
            if (jcbCN_TenChiNhanh.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                String sql = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                        + "group by cn.MaCN, TenCN";
                tongDoanhThuChiNhanh(sql);
            } else {
                String tenCN = jcbCN_TenChiNhanh.getSelectedItem().toString();
                String sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                        + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                        + "where cn.TenCN = N'" + tenCN + "' group by nv.MaNV, HoTenNV";
                tongDoanhThuNhanVien(sql, jtbCN_Bang);
            }
        }
        bieuDoCot(jtbCN_Bang, jpnCN_BieuDo, "Thống kê doanh số bán hàng", "Tổng doanh thu");
        tinhTong(jtbCN_Bang, "Tổng doanh thu", jtfCN_TongDoanhThu);
    }//GEN-LAST:event_jrdCN_TatCaActionPerformed

    private void jrdCN_KhoangThoiGianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdCN_KhoangThoiGianActionPerformed
        if (jdcCN_NgayBD.getDate() == null || jdcCN_NgayKT.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khoảng thời gian");
            jrdCN_TatCa.setSelected(true);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngayBD = sdf.format(jdcCN_NgayBD.getDate());
            String ngayKT = sdf.format(jdcCN_NgayKT.getDate());
            if (jrdCN_MaChiNhanh.isSelected()) {
                if (jcbCN_MaChiNhanh.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                    String sql = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                            + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                            + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by cn.MaCN, TenCN";
                    tongDoanhThuChiNhanh(sql);
                } else {
                    String maCN = jcbCN_MaChiNhanh.getSelectedItem().toString();
                    String sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                            + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                            + "where cn.MaCN = N'" + maCN + "' and NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by nv.MaNV, HoTenNV";
                    tongDoanhThuNhanVien(sql, jtbCN_Bang);
                }
            } else {
                if (jcbCN_TenChiNhanh.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                    String sql = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                            + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                            + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by cn.MaCN, TenCN";
                    tongDoanhThuChiNhanh(sql);
                } else {
                    String tenCN = jcbCN_TenChiNhanh.getSelectedItem().toString();
                    String sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                            + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                            + "where cn.TenCN = N'" + tenCN + "' and NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by nv.MaNV, HoTenNV";
                    tongDoanhThuNhanVien(sql, jtbCN_Bang);
                }
            }
            bieuDoCot(jtbCN_Bang, jpnCN_BieuDo, "Thống kê doanh số bán hàng", "Tổng doanh thu");
            tinhTong(jtbCN_Bang, "Tổng doanh thu", jtfCN_TongDoanhThu);
        }
    }//GEN-LAST:event_jrdCN_KhoangThoiGianActionPerformed

    private void jrdCN_MaChiNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdCN_MaChiNhanhActionPerformed
        if (jrdCN_TatCa.isSelected()) {
            if (jcbCN_MaChiNhanh.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                String sql = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                        + "group by cn.MaCN, TenCN";
                tongDoanhThuChiNhanh(sql);
            } else {
                String maCN = jcbCN_MaChiNhanh.getSelectedItem().toString();
                String sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                        + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                        + "where cn.MaCN = N'" + maCN + "' group by nv.MaNV, HoTenNV";
                tongDoanhThuNhanVien(sql, jtbCN_Bang);
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngayBD = sdf.format(jdcCN_NgayBD.getDate());
            String ngayKT = sdf.format(jdcCN_NgayKT.getDate());
            if (jcbCN_MaChiNhanh.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                String sql = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                        + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by cn.MaCN, TenCN";
                tongDoanhThuChiNhanh(sql);
            } else {
                String maCN = jcbCN_MaChiNhanh.getSelectedItem().toString();
                String sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                        + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                        + "where cn.MaCN = N'" + maCN + "' and NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by nv.MaNV, HoTenNV";
                tongDoanhThuNhanVien(sql, jtbCN_Bang);
            }
        }
        bieuDoCot(jtbCN_Bang, jpnCN_BieuDo, "Thống kê doanh số bán hàng", "Tổng doanh thu");
        tinhTong(jtbCN_Bang, "Tổng doanh thu", jtfCN_TongDoanhThu);
    }//GEN-LAST:event_jrdCN_MaChiNhanhActionPerformed

    private void jrdCN_TenChiNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdCN_TenChiNhanhActionPerformed
        if (jrdCN_TatCa.isSelected()) {
            if (jcbCN_TenChiNhanh.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                String sql = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                        + "group by cn.MaCN, TenCN";
                tongDoanhThuChiNhanh(sql);
            } else {
                String tenCN = jcbCN_TenChiNhanh.getSelectedItem().toString();
                String sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                        + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                        + "where cn.TenCN = N'" + tenCN + "' group by nv.MaNV, HoTenNV";
                tongDoanhThuNhanVien(sql, jtbCN_Bang);
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngayBD = sdf.format(jdcCN_NgayBD.getDate());
            String ngayKT = sdf.format(jdcCN_NgayKT.getDate());
            if (jcbCN_TenChiNhanh.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                String sql = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                        + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                        + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by cn.MaCN, TenCN";
                tongDoanhThuChiNhanh(sql);
            } else {
                String tenCN = jcbCN_TenChiNhanh.getSelectedItem().toString();
                String sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                        + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                        + "where cn.TenCN = N'" + tenCN + "' and NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by nv.MaNV, HoTenNV";
                tongDoanhThuNhanVien(sql, jtbCN_Bang);
            }
        }
        bieuDoCot(jtbCN_Bang, jpnCN_BieuDo, "Thống kê doanh số bán hàng", "Tổng doanh thu");
        tinhTong(jtbCN_Bang, "Tổng doanh thu", jtfCN_TongDoanhThu);
    }//GEN-LAST:event_jrdCN_TenChiNhanhActionPerformed

    private void jbnCN_XuatBaoCaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnCN_XuatBaoCaoActionPerformed
        xuatBaoCao(jtbCN_Bang);
    }//GEN-LAST:event_jbnCN_XuatBaoCaoActionPerformed

    private void jbnCN_XemBieuDoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnCN_XemBieuDoActionPerformed
        JFrame bieuDoJFrame = new JFrame();
        JPanel jpnView = new JPanel();
        jpnView.setLayout(new java.awt.BorderLayout());

        bieuDoCot(jtbCN_Bang, jpnView, "Thống kê doanh số bán hàng", "Tổng doanh thu");

        bieuDoJFrame.add(jpnView, BorderLayout.CENTER);
        bieuDoJFrame.setSize(1100, 700);
        bieuDoJFrame.setVisible(true);
        bieuDoJFrame.setLocationRelativeTo(null);
        bieuDoJFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_jbnCN_XemBieuDoActionPerformed

    private void jcbCN_MaChiNhanhItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbCN_MaChiNhanhItemStateChanged
    }//GEN-LAST:event_jcbCN_MaChiNhanhItemStateChanged

    private void jcbCN_MaChiNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbCN_MaChiNhanhActionPerformed
        if (jrdCN_MaChiNhanh.isSelected()) {
            if (jrdCN_TatCa.isSelected()) {
                if (jcbCN_MaChiNhanh.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                    String sql = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                            + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                            + "group by cn.MaCN, TenCN";
                    tongDoanhThuChiNhanh(sql);
                } else {
                    String maCN = jcbCN_MaChiNhanh.getSelectedItem().toString();
                    String sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                            + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                            + "where cn.MaCN = N'" + maCN + "' group by nv.MaNV, HoTenNV";
                    tongDoanhThuNhanVien(sql, jtbCN_Bang);
                }
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String ngayBD = sdf.format(jdcCN_NgayBD.getDate());
                String ngayKT = sdf.format(jdcCN_NgayKT.getDate());
                if (jcbCN_MaChiNhanh.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                    String sql = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                            + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                            + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by cn.MaCN, TenCN";
                    tongDoanhThuChiNhanh(sql);
                } else {
                    String maCN = jcbCN_MaChiNhanh.getSelectedItem().toString();
                    String sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                            + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                            + "where cn.MaCN = N'" + maCN + "' and NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by nv.MaNV, HoTenNV";
                    tongDoanhThuNhanVien(sql, jtbCN_Bang);
                }
            }
            bieuDoCot(jtbCN_Bang, jpnCN_BieuDo, "Thống kê doanh số bán hàng", "Tổng doanh thu");
            tinhTong(jtbCN_Bang, "Tổng doanh thu", jtfCN_TongDoanhThu);
        }
    }//GEN-LAST:event_jcbCN_MaChiNhanhActionPerformed

    private void jcbCN_TenChiNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbCN_TenChiNhanhActionPerformed
        if (jrdCN_TenChiNhanh.isSelected()) {
            if (jrdCN_TatCa.isSelected()) {
                if (jcbCN_TenChiNhanh.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                    String sql = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                            + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                            + "group by cn.MaCN, TenCN";
                    tongDoanhThuChiNhanh(sql);
                } else {
                    String tenCN = jcbCN_TenChiNhanh.getSelectedItem().toString();
                    String sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                            + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                            + "where cn.TenCN = N'" + tenCN + "' group by nv.MaNV, HoTenNV";
                    tongDoanhThuNhanVien(sql, jtbCN_Bang);
                }
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String ngayBD = sdf.format(jdcCN_NgayBD.getDate());
                String ngayKT = sdf.format(jdcCN_NgayKT.getDate());
                if (jcbCN_TenChiNhanh.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
                    String sql = "select cn.MaCN [Mã CN], TenCN [Tên CN], count (distinct hd.MaHD) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan),0) [Tổng doanh thu]\n"
                            + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                            + "where NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by cn.MaCN, TenCN";
                    tongDoanhThuChiNhanh(sql);
                } else {
                    String tenCN = jcbCN_TenChiNhanh.getSelectedItem().toString();
                    String sql = "select nv.MaNV [Mã NV], HoTenNV [Họ tên], isnull(count(distinct hd.MaHD),0) [Tổng số hóa đơn], isnull(sum(cthd.SLDat * GiaBan), 0) [Tổng doanh thu]\n"
                            + "from ChiNhanh cn inner join NhanVien nv on cn.MaCN = nv.MaCN inner join HoaDon hd on nv.MaNV= hd.MaNV inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                            + "where cn.TenCN = N'" + tenCN + "' and NgayLapHD between convert(date,'" + ngayBD + "') and convert(date,'" + ngayKT + "') group by nv.MaNV, HoTenNV";
                    tongDoanhThuNhanVien(sql, jtbCN_Bang);
                }
            }
            bieuDoCot(jtbCN_Bang, jpnCN_BieuDo, "Thống kê doanh số bán hàng", "Tổng doanh thu");
            tinhTong(jtbCN_Bang, "Tổng doanh thu", jtfCN_TongDoanhThu);
        }
    }//GEN-LAST:event_jcbCN_TenChiNhanhActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbnCN_XemBieuDo;
    private javax.swing.JButton jbnCN_XuatBaoCao;
    private javax.swing.JButton jbnDT_XemBieuDo;
    private javax.swing.JButton jbnDT_XuatBaoCao;
    private javax.swing.JButton jbnSP_XemBieuDo;
    private javax.swing.JButton jbnSP_XuatBaoCao;
    private javax.swing.JComboBox<String> jcbCN_MaChiNhanh;
    private javax.swing.JComboBox<String> jcbCN_TenChiNhanh;
    private javax.swing.JComboBox<String> jcbSP_MaLSP;
    private com.toedter.calendar.JDateChooser jdcCN_NgayBD;
    private com.toedter.calendar.JDateChooser jdcCN_NgayKT;
    private com.toedter.calendar.JDateChooser jdcDT_NgayBD;
    private com.toedter.calendar.JDateChooser jdcDT_NgayKT;
    private com.toedter.calendar.JDateChooser jdcSP_NgayBD;
    private com.toedter.calendar.JDateChooser jdcSP_NgayKT;
    private javax.swing.JPanel jpnCN_Bang;
    private javax.swing.JPanel jpnCN_BieuDo;
    private javax.swing.JPanel jpnCN_PhanLoai;
    private javax.swing.JPanel jpnCN_ThoiGian;
    private javax.swing.JPanel jpnCN_ThongKe;
    private javax.swing.JPanel jpnCN_TongDoanhThu;
    private javax.swing.JPanel jpnChiNhanh;
    private javax.swing.JPanel jpnDT_Bang;
    private javax.swing.JPanel jpnDT_BieuDo;
    private javax.swing.JPanel jpnDT_PhanLoai;
    private javax.swing.JPanel jpnDT_ThoiGian;
    private javax.swing.JPanel jpnDT_ThongKe;
    private javax.swing.JPanel jpnDT_TongDoanhThu;
    private javax.swing.JPanel jpnDT_TongHoaDon;
    private javax.swing.JPanel jpnDoanhThu;
    private javax.swing.JPanel jpnSP_Bang;
    private javax.swing.JPanel jpnSP_PhanLoai;
    private javax.swing.JPanel jpnSP_ThoiGian;
    private javax.swing.JPanel jpnSP_ThongKe;
    private javax.swing.JPanel jpnSP_TongDoanhThu;
    private javax.swing.JPanel jpnSP_TongHoaDon;
    private javax.swing.JPanel jpnSanPham;
    private javax.swing.JRadioButton jrdCN_KhoangThoiGian;
    private javax.swing.JRadioButton jrdCN_MaChiNhanh;
    private javax.swing.JRadioButton jrdCN_TatCa;
    private javax.swing.JRadioButton jrdCN_TenChiNhanh;
    private javax.swing.JRadioButton jrdDT_KhachHang;
    private javax.swing.JRadioButton jrdDT_KhoangThoiGian;
    private javax.swing.JRadioButton jrdDT_NhanVien;
    private javax.swing.JRadioButton jrdDT_TatCa;
    private javax.swing.JRadioButton jrdSP_KhoangThoiGian;
    private javax.swing.JRadioButton jrdSP_LoaiSanPham;
    private javax.swing.JRadioButton jrdSP_SanPham;
    private javax.swing.JRadioButton jrdSP_TatCa;
    private javax.swing.JTable jtbCN_Bang;
    private javax.swing.JTable jtbDT_Bang;
    private javax.swing.JTable jtbSP_Bang;
    private javax.swing.JTextField jtfCN_TongDoanhThu;
    private javax.swing.JTextField jtfDT_TongDoanhThu;
    private javax.swing.JTextField jtfDT_TongHoaDon;
    private javax.swing.JTextField jtfSP_TongDoanhThu;
    private javax.swing.JTextField jtfSP_TongSoLuong;
    // End of variables declaration//GEN-END:variables
}
