package com.example.quanlysieuthiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {
    private String maKH;
    private String hoTen;
    private String sdt;
    private String gioiTinh;
    private String email;
    private LocalDate ngaySinh;
    private String diachi;
    private int diemtichluy;
}