/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import dao.DBConnect;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;
import javax.swing.JTextField;

/**
 *
 * @author Administrator
 */
public class DangNhapJFrame extends javax.swing.JFrame {

    /**
     * Creates new form DangNhapJFrame
     */
    Preferences preference;
    boolean nhoMatKhau;

    public DangNhapJFrame() {
        initComponents();

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo_PL.png")));
        addPlaceholderStyle(jtfUsername);
        addPlaceholderStyle(jpfPassword);

        setTitle("PHÚC LONG COFFEE & TEA");
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);

        rememberMe();
        font();
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

    public void rememberMe() {
        preference = Preferences.userNodeForPackage(this.getClass());
        nhoMatKhau = preference.getBoolean("rememberMe", Boolean.valueOf(""));
        if (nhoMatKhau) {
            jtfUsername.setText(preference.get("Username", ""));
            jpfPassword.setText(preference.get("Password", ""));
            jckbNhoMatKhau.setSelected(nhoMatKhau);
        }
    }

    public void font() {
        if (!jtfUsername.getText().equals("Username") && !jpfPassword.getText().equals("Password")) {
            Font font = jtfUsername.getFont();
            font = font.deriveFont(Font.PLAIN);
            jtfUsername.setForeground(Color.BLACK);
            jtfUsername.setFont(font);
            jpfPassword.setForeground(Color.BLACK);
            jpfPassword.setFont(font);
            jpfPassword.setEchoChar('\u2022');
        }
    }

    public void dangNhap() {
        if (jtfUsername.getText().equals("Username") && new String(jpfPassword.getPassword()).equals("Password")) {
            jlbMsg.setText("Vui lòng nhập dữ liệu bắt buộc!");
        } else if (jtfUsername.getText().equals("Username")) {
            jlbMsg.setText("Vui lòng nhập tên đăng nhập!");
        } else if (new String(jpfPassword.getPassword()).equals("Password")) {
            jlbMsg.setText("Vui lòng nhập mật khẩu!");
        } else {
            String username = jtfUsername.getText();
            String password = new String(jpfPassword.getPassword());
            String sql = "Select * from TaiKhoan where UserName = '" + username + "' and Password = '" + password + "'";
            try {
                Connection cons = new DBConnect().getConnection();
                PreparedStatement ps = cons.prepareCall(sql);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if (jckbNhoMatKhau.isSelected() && !nhoMatKhau) {
                        preference.put("Username", jtfUsername.getText());
                        preference.put("Password", jpfPassword.getText());
                        preference.putBoolean("rememberMe", true);
                    } else if (!jckbNhoMatKhau.isSelected() && nhoMatKhau) {
                        preference.put("Username", "");
                        preference.put("Password", "");
                        preference.putBoolean("rememberMe", false);
                    }
                    if (rs.getBoolean("TinhTrang")) {
                        if (rs.getString("Quyen").equalsIgnoreCase("Quản lý")) {
                            dispose();
                            QuyenQuanLyJFrame quyenQuanLyJFrame = new QuyenQuanLyJFrame();
                            quyenQuanLyJFrame.show();
                        } else {
                            dispose();
                            QuyenNhanVienJFrame quyenNhanVienJFrame = new QuyenNhanVienJFrame();
                            quyenNhanVienJFrame.show();
                        }
                    } else {
                        jlbMsg.setText("Tài khoản của bạn đang tạm khóa!");
                    }
                } else {
                    jlbMsg.setText("Thông tin đăng nhập không đúng!");
                }
                ps.close();
                rs.close();
                cons.close();
            } catch (Exception ex) {
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jlbUsername = new javax.swing.JLabel();
        jlbPassword = new javax.swing.JLabel();
        jckbNhoMatKhau = new javax.swing.JCheckBox();
        jbnDangNhap = new javax.swing.JButton();
        jlbMsg = new javax.swing.JLabel();
        jtfUsername = new javax.swing.JTextField();
        jpfPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo-phuc-long-company.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Showcard Gothic", 1, 29)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 113, 61));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("PHÚC LONG COFFEE & TEA");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jlbUsername.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jlbUsername.setText("Tên đăng nhập");

        jlbPassword.setFont(new java.awt.Font("Times New Roman", 1, 19)); // NOI18N
        jlbPassword.setText("Mật khẩu");

        jckbNhoMatKhau.setBackground(new java.awt.Color(255, 255, 255));
        jckbNhoMatKhau.setFont(new java.awt.Font("Times New Roman", 0, 17)); // NOI18N
        jckbNhoMatKhau.setText("Nhớ mật khẩu?");
        jckbNhoMatKhau.setBorder(null);
        jckbNhoMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jckbNhoMatKhauActionPerformed(evt);
            }
        });

        jbnDangNhap.setBackground(new java.awt.Color(0, 113, 61));
        jbnDangNhap.setFont(new java.awt.Font("Segoe UI", 1, 23)); // NOI18N
        jbnDangNhap.setForeground(new java.awt.Color(255, 255, 255));
        jbnDangNhap.setText("Đăng nhập");
        jbnDangNhap.setBorder(null);
        jbnDangNhap.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jbnDangNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnDangNhapActionPerformed(evt);
            }
        });

        jlbMsg.setFont(new java.awt.Font("Times New Roman", 3, 17)); // NOI18N
        jlbMsg.setForeground(new java.awt.Color(255, 0, 0));

        jtfUsername.setFont(new java.awt.Font("Times New Roman", 0, 19)); // NOI18N
        jtfUsername.setText("Username");
        jtfUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfUsernameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfUsernameFocusLost(evt);
            }
        });

        jpfPassword.setFont(new java.awt.Font("Times New Roman", 0, 19)); // NOI18N
        jpfPassword.setText("Password");
        jpfPassword.setEchoChar('\u0000');
        jpfPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jpfPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jpfPasswordFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlbUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtfUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jckbNhoMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jlbMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jpfPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbnDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(170, 170, 170))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlbUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(jtfUsername))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlbPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(jpfPassword))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jckbNhoMatKhau, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jlbMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbnDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jckbNhoMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jckbNhoMatKhauActionPerformed

    }//GEN-LAST:event_jckbNhoMatKhauActionPerformed

    private void jbnDangNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnDangNhapActionPerformed
        dangNhap();
    }//GEN-LAST:event_jbnDangNhapActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        this.requestFocusInWindow();
    }//GEN-LAST:event_formWindowGainedFocus

    private void jtfUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfUsernameFocusGained
        if (jtfUsername.getText().equals("Username")) {
            jtfUsername.setText(null);
            jtfUsername.requestFocus();
            removePlaceholderStyle(jtfUsername);
        }
    }//GEN-LAST:event_jtfUsernameFocusGained

    private void jtfUsernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfUsernameFocusLost
        if (jtfUsername.getText().length() == 0) {
            addPlaceholderStyle(jtfUsername);
            jtfUsername.setText("Username");
        }
    }//GEN-LAST:event_jtfUsernameFocusLost

    private void jpfPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jpfPasswordFocusLost
        if (jpfPassword.getText().length() == 0) {
            addPlaceholderStyle(jpfPassword);
            jpfPassword.setText("Password");
            jpfPassword.setEchoChar('\u0000');
        }
    }//GEN-LAST:event_jpfPasswordFocusLost

    private void jpfPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jpfPasswordFocusGained
        if (jpfPassword.getText().equals("Password")) {
            jpfPassword.setEchoChar('\u2022');
            jpfPassword.setText(null);
            jpfPassword.requestFocus();
            removePlaceholderStyle(jpfPassword);
        }
    }//GEN-LAST:event_jpfPasswordFocusGained

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbnDangNhap;
    private javax.swing.JCheckBox jckbNhoMatKhau;
    private javax.swing.JLabel jlbMsg;
    private javax.swing.JLabel jlbPassword;
    private javax.swing.JLabel jlbUsername;
    private javax.swing.JPasswordField jpfPassword;
    private javax.swing.JTextField jtfUsername;
    // End of variables declaration//GEN-END:variables
}
