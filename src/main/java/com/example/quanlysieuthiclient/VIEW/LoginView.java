package com.example.quanlysieuthiclient.VIEW;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author VŨ HÙNG HẢI
 */
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;

public class LoginView extends JFrame {

    public JTextField taikhoanField = new JTextField();
    public JPasswordField matkhauField = new JPasswordField();
    public JButton dangnhapButton = new JButton("Đăng nhập");

    public LoginView() {
        setTitle("Đăng nhập Hệ thống");
        setSize(500, 300);
        setLayout(null);
        
        JLabel taikhoanJLabel=new JLabel("Nhập tài khoản:");
        taikhoanJLabel.setBounds(10, 10, 150, 40);
        taikhoanJLabel.setFont(new Font("Arial",Font.BOLD,17));
        add(taikhoanJLabel);
        
        taikhoanField.setBounds(200, 10, 200, 40);
        taikhoanField.setFont(new Font("Arial",Font.BOLD,17));
        add(taikhoanField);
        
        JLabel matkhauJLabel=new JLabel("Nhập mật khẩu:");
        matkhauJLabel.setBounds(10, 60, 150, 40);
        matkhauJLabel.setFont(new Font("Arial",Font.BOLD,17));
        add(matkhauJLabel);
        
        matkhauField.setBounds(200, 60, 200, 40);
        taikhoanJLabel.setFont(new Font("Arial",Font.BOLD,17));
        add(matkhauField);
        
        dangnhapButton.setBounds(20, 110, 250, 30);
        dangnhapButton.setBackground(Color.GREEN);
        dangnhapButton.setFont(new Font("Arial",Font.BOLD,17));
        add(dangnhapButton);

    }
    public void addDangNhapListener (ActionListener listener) {
        dangnhapButton.addActionListener(listener);
    };
}
