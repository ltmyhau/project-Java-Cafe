/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import java.awt.Desktop;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class TrangChuJPanel extends javax.swing.JPanel implements Runnable {

    /**
     * Creates new form TrangChuJPanel
     */
    int hour, minute, sencond;

    public TrangChuJPanel() {
        initComponents();

        Thread t = new Thread(this);
        t.start();
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
        jpnDau = new javax.swing.JPanel();
        jlbWebsite = new javax.swing.JLabel();
        jlbDongHo = new javax.swing.JLabel();
        jlbPhuongCham = new javax.swing.JLabel();
        jlbCotMocDangNho = new javax.swing.JLabel();
        jpnCuoi = new javax.swing.JPanel();
        jpnCongTy = new javax.swing.JPanel();
        jlbTenCongTy = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jlbBoCongThuong = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(900, 650));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jpnDau.setBackground(new java.awt.Color(255, 255, 255));

        jlbWebsite.setFont(new java.awt.Font("Showcard Gothic", 1, 35)); // NOI18N
        jlbWebsite.setForeground(new java.awt.Color(0, 113, 61));
        jlbWebsite.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbWebsite.setText("PHÚC LONG COFFEE & TEA");
        jlbWebsite.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jlbWebsite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlbWebsiteMouseClicked(evt);
            }
        });

        jlbDongHo.setFont(new java.awt.Font("Times New Roman", 1, 23)); // NOI18N
        jlbDongHo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbDongHo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon_clock.png"))); // NOI18N

        javax.swing.GroupLayout jpnDauLayout = new javax.swing.GroupLayout(jpnDau);
        jpnDau.setLayout(jpnDauLayout);
        jpnDauLayout.setHorizontalGroup(
            jpnDauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDauLayout.createSequentialGroup()
                .addGap(192, 192, 192)
                .addComponent(jlbWebsite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jlbDongHo, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        jpnDauLayout.setVerticalGroup(
            jpnDauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDauLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jlbDongHo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jpnDauLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbWebsite, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                .addContainerGap())
        );

        jlbPhuongCham.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbPhuongCham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/PhuongCham.png"))); // NOI18N

        jlbCotMocDangNho.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbCotMocDangNho.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CotMocDangNho.png"))); // NOI18N
        jlbCotMocDangNho.setPreferredSize(new java.awt.Dimension(1600, 456));

        jpnCuoi.setBackground(new java.awt.Color(255, 255, 255));

        jpnCongTy.setBackground(new java.awt.Color(255, 255, 255));

        jlbTenCongTy.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jlbTenCongTy.setForeground(new java.awt.Color(0, 113, 61));
        jlbTenCongTy.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlbTenCongTy.setText("CÔNG TY CỔ PHẦN PHÚC LONG HERITAGE");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("ĐKKD: 0316 871719 do sở KHĐT TPHCM cấp lần đầu ngày 21/05/2021");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Điện thoại: 1900234518 (Ext.9100/ 9102)");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Fax: (028) 6263 0379");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Email: sales@phuclong.masangroup.com, info2@phuclong.masangroup.com");

        javax.swing.GroupLayout jpnCongTyLayout = new javax.swing.GroupLayout(jpnCongTy);
        jpnCongTy.setLayout(jpnCongTyLayout);
        jpnCongTyLayout.setHorizontalGroup(
            jpnCongTyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCongTyLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jpnCongTyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jlbTenCongTy)
                    .addGroup(jpnCongTyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4)))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jpnCongTyLayout.setVerticalGroup(
            jpnCongTyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCongTyLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jlbTenCongTy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jlbBoCongThuong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bocongthuong.png"))); // NOI18N
        jlbBoCongThuong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlbBoCongThuongMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jpnCuoiLayout = new javax.swing.GroupLayout(jpnCuoi);
        jpnCuoi.setLayout(jpnCuoiLayout);
        jpnCuoiLayout.setHorizontalGroup(
            jpnCuoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCuoiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpnCongTy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlbBoCongThuong, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jpnCuoiLayout.setVerticalGroup(
            jpnCuoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnCuoiLayout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jpnCongTy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jpnCuoiLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jlbBoCongThuong, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnCuoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jlbCotMocDangNho, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jlbPhuongCham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpnDau, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jpnDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlbPhuongCham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlbCotMocDangNho, javax.swing.GroupLayout.PREFERRED_SIZE, 253, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnCuoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jlbBoCongThuongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlbBoCongThuongMouseClicked
        int opt = JOptionPane.showConfirmDialog(null, "http://online.gov.vn/Home/WebDetails/96621?AspxAutoDetectCookieSupport=1", "Bạn muốn mở liên kết này?", JOptionPane.YES_NO_OPTION);
        if (opt == 0) {
            try {
                Desktop browser = Desktop.getDesktop();
                try {
                    browser.browse(new URI("http://online.gov.vn/Home/WebDetails/96621?AspxAutoDetectCookieSupport=1"));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(TrangChuJPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(TrangChuJPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jlbBoCongThuongMouseClicked

    private void jlbWebsiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlbWebsiteMouseClicked
        int opt = JOptionPane.showConfirmDialog(null, "https://phuclong.com.vn/", "Bạn muốn mở liên kết này?", JOptionPane.YES_NO_OPTION);
        if (opt == 0) {
            try {
                Desktop browser = Desktop.getDesktop();
                try {
                    browser.browse(new URI("https://phuclong.com.vn/"));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(TrangChuJPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(TrangChuJPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jlbWebsiteMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jlbBoCongThuong;
    private javax.swing.JLabel jlbCotMocDangNho;
    private javax.swing.JLabel jlbDongHo;
    private javax.swing.JLabel jlbPhuongCham;
    private javax.swing.JLabel jlbTenCongTy;
    private javax.swing.JLabel jlbWebsite;
    private javax.swing.JPanel jpnCongTy;
    private javax.swing.JPanel jpnCuoi;
    private javax.swing.JPanel jpnDau;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        while (true) {
            Calendar cal = Calendar.getInstance();
            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);
            sencond = cal.get(Calendar.SECOND);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = cal.getTime();
            String time = sdf.format(date);

            jlbDongHo.setText(time);
        }
    }
}
