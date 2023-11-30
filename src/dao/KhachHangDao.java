/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.KhachHang;

/**
 *
 * @author Administrator
 */
public class KhachHangDao {
     public List<KhachHang> getList() {
        try {
            Connection cons = DBConnect.getConnection();
            List<KhachHang> list = new ArrayList<>();
            String sql = "Select * from KhachHang";
            PreparedStatement ps = cons.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhachHang khachHang = new KhachHang();
                khachHang.setMaKhachHang(rs.getString("MaKH"));
                khachHang.setHoTen(rs.getString("HoTenKH"));
                khachHang.setNgaySinh(rs.getDate("NgaySinh"));
                khachHang.setGioiTinh(rs.getString("GioiTinh"));
                khachHang.setDienThoai(rs.getString("DienThoai"));
                khachHang.setDiaChi(rs.getString("DiaChi"));
                list.add(khachHang);
            }
            ps.close();
            rs.close();
            cons.close();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
