/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Administrator
 */
public class SanPham {

    private String maSP;
    private String tenSP;
    private String maLSP;
    private float giaBan;
    private byte[] hinhAnh;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, String maLSP, float giaBan, byte[] hinhAnh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.maLSP = maLSP;
        this.giaBan = giaBan;
        this.hinhAnh = hinhAnh;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getMaLSP() {
        return maLSP;
    }

    public void setMaLSP(String maLSP) {
        this.maLSP = maLSP;
    }

    public float getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(float giaBan) {
        this.giaBan = giaBan;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Override
    public String toString() {
        return "SanPham{" + "maSP=" + maSP + ", tenSP=" + tenSP + ", maLSP=" + maLSP + ", giaBan=" + giaBan + ", hinhAnh=" + hinhAnh + '}';
    }    
}
