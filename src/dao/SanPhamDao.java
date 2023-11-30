/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.SanPham;

/**
 *
 * @author Administrator
 */
public class SanPhamDao {

    List<SanPham> list = new ArrayList<>();

    public List<SanPham> getList() {
        String sql = "Select * from SanPham";
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sanPham = new SanPham();
                sanPham.setMaSP(rs.getString("MaSP"));
                sanPham.setTenSP(rs.getString("TenSP"));
                sanPham.setMaLSP(rs.getString("MaLSP"));
                sanPham.setGiaBan(rs.getFloat("GiaBan"));
                sanPham.setHinhAnh(rs.getBytes("HinhAnh"));
                list.add(sanPham);
            }
        } catch (Exception e) {
        }
        return list;
    }

    public List<SanPham> getList(String sql) {
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sanPham = new SanPham();
                sanPham.setMaSP(rs.getString("MaSP"));
                sanPham.setTenSP(rs.getString("TenSP"));
                sanPham.setMaLSP(rs.getString("MaLSP"));
                sanPham.setGiaBan(rs.getFloat("GiaBan"));
                sanPham.setHinhAnh(rs.getBytes("HinhAnh"));
                list.add(sanPham);
            }
        } catch (Exception e) {
        }
        return list;
    }

}
