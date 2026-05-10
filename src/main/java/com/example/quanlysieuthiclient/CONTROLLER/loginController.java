package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.taikhoanApiClient;
import com.example.quanlysieuthiclient.VIEW.LoginView;
import com.example.quanlysieuthiclient.VIEW.manhinhchinh;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class loginController {
    LoginView view;
    taikhoanApiClient tkApi = taikhoanApiClient.getInstance();

    public loginController(LoginView view) {
        this.view = view;
        view.addDangNhapListener(new dangnhap());
    }

    public class dangnhap implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String taikhoan = view.taikhoanField.getText().trim();
            String matkhau = new String(view.matkhauField.getPassword());
            if (taikhoan.isEmpty() || matkhau.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ tài khoản và mật khẩu");
                return;
            }
            try {
                if (tkApi.kiemTraDangNhap(taikhoan, matkhau)) {
                    manhinhchinh menu = new manhinhchinh();
                    new manhinhchinhController(menu);
                    menu.setVisible(true);
                    view.dispose();
                    JOptionPane.showMessageDialog(view, "Đăng nhập thành công");
                } else {
                    JOptionPane.showMessageDialog(view, "Tài khoản hoặc mật khẩu không chính xác");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi kết nối: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        LoginView loginView = new LoginView();
        new loginController(loginView);
        loginView.setVisible(true);
    }
}