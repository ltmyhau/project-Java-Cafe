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
import model.ChiTietHoaDon;

/**
 *
 * @author Administrator
 */
public class ChiTietHoaDonDao {

    List<ChiTietHoaDon> list = new ArrayList<>();

    public List<ChiTietHoaDon> getList(String maHD) {
        String sql = "select MaHD, cthd.MaSP, TenSP, GiaBan, SLDat, GiaBan * SLDat as ThanhTien \n"
                + "from ChiTietHoaDon cthd inner join SanPham sp on cthd.MaSP = sp.MaSp where MaHD = '" + maHD + "'";
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
                chiTietHoaDon.setMaHD(rs.getString("MaHD"));
                chiTietHoaDon.setMaSP(rs.getString("MaSP"));
                chiTietHoaDon.setTenSP(rs.getString("TenSP"));
                chiTietHoaDon.setDonGia(rs.getFloat("GiaBan"));
                chiTietHoaDon.setSoLuongDat(rs.getInt("SLDat"));
                chiTietHoaDon.setThanhTien(rs.getFloat("ThanhTien"));
                list.add(chiTietHoaDon);
            }
        } catch (Exception e) {
        }
        return list;
    }
}
