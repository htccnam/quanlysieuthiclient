package com.example.quanlysieuthiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class nhanvien {
    private String manhanvien;
    private String tennhanvien;
    private String ngaysinh;
    private String gioitinh;
    private String sodienthoai;
    private String email;
    private String diachi;
    private String machucvu;

}
