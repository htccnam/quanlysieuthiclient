/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.quanlysieuthiclient.VIEW;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author Admin
 */
public class manhinhchinh extends JFrame {

    JMenu hangHoaVaKhoJMenu;
    JMenuItem danhSachSanPham, phanLoaiHang, nhaCungCap;
    JMenu banHangJMenu;
    JMenuItem taoDonMoi, ChiTietDonHang;
    JMenu khachHangJMenu;
    JMenuItem quanLyKhachHang;
    JMenuItem hangThanhVien;
    JMenuItem doiQuaItem; 
    JMenu khuyenmaiJMenu;
    JMenuItem quanlykhuyenmai;
    JMenu nhanSuJMenu;
    JMenuItem quanLyNhanVien;
    JMenuItem chucvuItem;
    
    JMenu thaotacJMenu;
    JMenuItem dangxuatItem;

    JPanel containerJPanel;

    public manhinhchinh() {
        setTitle("HỆ THỐNG QUẢN LÝ SIÊU THỊ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1800, 1000);
        
        hangHoaVaKhoJMenu = new JMenu("Hàng Hóa và kho");
        danhSachSanPham = new JMenuItem("danh sách sản phẩm");
        phanLoaiHang = new JMenuItem("phân loại hàng");
        nhaCungCap = new JMenuItem("Nhà cung cấp");
        hangHoaVaKhoJMenu.setFont(new Font("Arial",Font.BOLD,20));
        danhSachSanPham.setFont(new Font("Arial",Font.BOLD,17));
        phanLoaiHang.setFont(new Font("Arial",Font.BOLD,17));   
        nhaCungCap.setFont(new Font("Arial",Font.BOLD,17));
        hangHoaVaKhoJMenu.add(danhSachSanPham);
        hangHoaVaKhoJMenu.add(phanLoaiHang);
        hangHoaVaKhoJMenu.add(nhaCungCap);

        banHangJMenu = new JMenu("Bán hàng");
        taoDonMoi = new JMenuItem("tạo đơn mới");
        ChiTietDonHang = new JMenuItem("chi tiết đơn hàng");
        banHangJMenu.setFont(new Font("Arial",Font.BOLD,20));
        taoDonMoi.setFont(new Font("Arial",Font.BOLD,17));   
        ChiTietDonHang.setFont(new Font("Arial",Font.BOLD,17));
        banHangJMenu.add(taoDonMoi);
        banHangJMenu.add(ChiTietDonHang);

        //3.Menu KhachHang :
        khachHangJMenu = new JMenu("Khách hàng");
        quanLyKhachHang= new JMenuItem("Quản lý khách hàng");
        hangThanhVien = new JMenuItem("Hạng thành viên (VIP)");
        doiQuaItem = new JMenuItem("Đổi điểm nhận quà");
        
        khachHangJMenu.setFont(new Font("Arial",Font.BOLD,20));
        quanLyKhachHang.setFont(new Font("Arial",Font.BOLD,17));   
        hangThanhVien.setFont(new Font("Arial",Font.BOLD,17));
        doiQuaItem.setFont(new Font("Arial",Font.BOLD,17));
        
        khachHangJMenu.add(quanLyKhachHang);
        khachHangJMenu.add(hangThanhVien);
        khachHangJMenu.add(doiQuaItem);
        khachHangJMenu.setVisible(true);
        
        khuyenmaiJMenu = new JMenu("Khuyến mại");
        quanlykhuyenmai= new JMenuItem("Quản lý khuyến mại");
        khuyenmaiJMenu.setFont(new Font("Arial",Font.BOLD,20));
        quanlykhuyenmai.setFont(new Font("Arial",Font.BOLD,17));   
        khuyenmaiJMenu.add(quanlykhuyenmai);
        
        nhanSuJMenu = new JMenu("Nhân sự");
        quanLyNhanVien=new JMenuItem("Quản lý nhân viên");
        chucvuItem=new JMenuItem("Chức vụ");
        nhanSuJMenu.setFont(new Font("Arial",Font.BOLD,20));
        quanLyNhanVien.setFont(new Font("Arial",Font.BOLD,17));   
        chucvuItem.setFont(new Font("Arial",Font.BOLD,17));
        nhanSuJMenu.add(quanLyNhanVien);
        nhanSuJMenu.add(chucvuItem);
        
        thaotacJMenu=new JMenu("Thao tác");
        dangxuatItem=new JMenuItem("Đăng xuất");
        thaotacJMenu.setFont(new Font("Arial",Font.BOLD,20));
        dangxuatItem.setFont(new Font("Arial",Font.BOLD,17));   
        thaotacJMenu.add(dangxuatItem);

        JMenuBar bar = new JMenuBar();
        //
        bar.add(hangHoaVaKhoJMenu);
        bar.add(banHangJMenu);
        bar.add(khachHangJMenu);
        bar.add(khuyenmaiJMenu);
        bar.add(nhanSuJMenu);
        bar.add(thaotacJMenu);
        bar.setBackground(Color.GREEN);
        setJMenuBar(bar);

        containerJPanel = new JPanel(new java.awt.BorderLayout());

        containerJPanel.setPreferredSize(new Dimension(1200, 900)); // 👈 QUAN TRỌNG
        add(containerJPanel, BorderLayout.CENTER);

    }

    public void showpanel(javax.swing.JPanel jp) { 
        
        containerJPanel.removeAll();
        containerJPanel.add(jp, java.awt.BorderLayout.CENTER); 
        containerJPanel.revalidate(); 
        containerJPanel.repaint();   
    }
    

    public void addClickDanhSachSanPham(ActionListener listener) {
        danhSachSanPham.addActionListener(listener);
    }

    public void addClickPhanLoaiHang(ActionListener listener) {
        phanLoaiHang.addActionListener(listener);
    }

    public void addClickNhaCungCap(ActionListener listener) { 
            nhaCungCap.addActionListener(listener); 
        }

    public void addClickTaoDonMoi(ActionListener listener) {
        taoDonMoi.addActionListener(listener);
    }
    
    public void addClickChiTiet(ActionListener listener) {
        ChiTietDonHang.addActionListener(listener);
    }

    public void addClickQuanLyKhachHang(ActionListener listener) {
        quanLyKhachHang.addActionListener(listener);
    }

    public void addClickQuanLyKhuyenMai(ActionListener listener) {
        quanlykhuyenmai.addActionListener(listener);
    }

    public void addClickQuanLyNhanVien(ActionListener listener) {
        quanLyNhanVien.addActionListener(listener);
    }
    public void addClickQuanLyChucVu(ActionListener listener){
        chucvuItem.addActionListener(listener);
    }
    public void addClickHangThanhVien(ActionListener listener) { 
        hangThanhVien.addActionListener(listener); 
    }
    public void addClickDoiQua(ActionListener listener) { 
        doiQuaItem.addActionListener(listener); 
    }
    public void addClickDangXuat(ActionListener listener){
        dangxuatItem.addActionListener(listener);
    }
}

