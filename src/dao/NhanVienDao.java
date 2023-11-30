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
import model.NhanVien;

/**
 *
 * @author Administrator
 */
public class NhanVienDao {

    public List<NhanVien> getList() {
        try {
            Connection cons = DBConnect.getConnection();
            List<NhanVien> list = new ArrayList<>();
            String sql = "Select * from NhanVien";
            PreparedStatement ps = cons.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhanVien nhanVien = new NhanVien();
                nhanVien.setMaNV(rs.getString("MaNV"));
                nhanVien.setHoTenNV(rs.getString("HoTenNV"));
                nhanVien.setNgaySinh(rs.getDate("NgaySinh"));
                nhanVien.setGioiTinh(rs.getString("GioiTinh"));
                nhanVien.setNgayVaoLam(rs.getDate("NgayVaoLam"));
                nhanVien.setDienThoai(rs.getString("DienThoai"));
                nhanVien.setEmail(rs.getString("Email"));
                nhanVien.setDiaChi(rs.getString("DiaChi"));
                list.add(nhanVien);
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
