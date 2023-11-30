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
import model.HoaDon;

/**
 *
 * @author Administrator
 */
public class HoaDonDao {

    List<HoaDon> list = new ArrayList<>();

    public List<HoaDon> getList() {
        String sql = "select hd.MaHD, MaNV, MaKH, NgayLapHD, sum(SLDat * GiaBan) as TongTien\n"
                + "from HoaDon hd inner join ChiTietHoaDon cthd on hd.MaHD = cthd.MaHD\n"
                + "inner join SanPham sp on cthd.MaSP = sp.MaSP\n"
                + "group by hd.MaHD, MaNV, MaKH, NgayLapHD";
        try {
            Connection cons = new DBConnect().getConnection();
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hoaDon = new HoaDon();
                hoaDon.setMaHD(rs.getString("MaHD"));
                hoaDon.setMaNV(rs.getString("MaNV"));
                hoaDon.setMaKH(rs.getString("MaKH"));
                hoaDon.setNgayLapHD(rs.getDate("NgayLapHD"));
                hoaDon.setTongTien(rs.getFloat("TongTien"));
                list.add(hoaDon);
            }
        } catch (Exception e) {
        }
        return list;
    }
}
