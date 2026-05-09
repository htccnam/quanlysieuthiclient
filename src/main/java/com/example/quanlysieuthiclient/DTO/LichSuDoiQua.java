package com.example.quanlysieuthiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichSuDoiQua {
    private int maGiaoDich;
    private String maKH;
    private String tenQua;
    private int diemTru;
    private LocalDateTime thoiGian;
}