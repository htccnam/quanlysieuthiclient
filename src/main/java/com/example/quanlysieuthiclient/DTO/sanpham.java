package com.example.quanlysieuthiclient.DTO;

import lombok.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class sanpham {
    private String masanpham;
    private String tensanpham;
    private String maloai;
    private String manhacungcap;
    private String xuatxu;
    private Integer soluong;
    private Date ngaysanxuat;
    private Date hansudung;
    private String tinhtrang;
    private Double gianhap;
    private Double giaban;
    private String donvitinh;
}