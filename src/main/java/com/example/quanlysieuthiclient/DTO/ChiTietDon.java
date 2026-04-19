package com.example.quanlysieuthiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ChiTietDon {
    private String maDH;
    private String maSP;
    private String tenSP;
    private int soluong;
    private double gia;
    private double thanhtien;
}
