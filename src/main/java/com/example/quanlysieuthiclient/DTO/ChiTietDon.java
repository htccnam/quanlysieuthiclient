package com.example.quanlysieuthiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ChiTietDon {
    private String madonhang;
    private String masanpham;
    private String tensanpham;
    private int soluong;
    private double dongia;
    private double thanhtien;
}
