package com.example.quanlysieuthiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonHang {
    private String maDonHang;
    private String maKH; // Thay cho maKH
    private String maNV;  // Thay cho maNV
    private Date NgayGD; // Chuyển sang String để hiển thị đẹp (dd/MM/yyyy)
    private String PTban;
    private String PTgiaodich;
    private double tongTien;
    private String maKM; // Thay cho maKM
}
