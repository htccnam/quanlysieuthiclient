package com.example.quanlysieuthiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuaTang {
    private String maQua;
    private String tenQua;
    private int diemYeuCau;
    private String moTa;
}