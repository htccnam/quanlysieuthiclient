package com.example.quanlysieuthiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonHang {
    private String madonhang;
    private String makhachhang;
    private String manhanvien;
    private String makhuyenmai;
    private Date ngaylap;
    private String phuongthucban;
    private String thanhtoan;
    private double tongtien;
}
