/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.quanlysieuthiclient.APICLIENT;


import com.example.quanlysieuthiclient.DTO.nhanvien;
import com.example.quanlysieuthiclient.UTIL.configLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Reader;import java.lang.reflect.Type;import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 *
 * @author Admin
 */
@AllArgsConstructor
public class nhanvienApiClient {
    @Getter
    private static final nhanvienApiClient instance=new nhanvienApiClient();
    private final String apiurl= configLoader.getbaseapiurl()+"/nhanvien";
    private final Gson gson=new Gson();
    private final HttpClient httpClient= HttpClient.newBuilder().build();

    public boolean themNhanVien(nhanvien nhanvien) throws Exception {
        String jsonbody=gson.toJson(nhanvien);
        HttpRequest request= HttpRequest.newBuilder()
                .uri(URI.create(apiurl))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonbody))
                .build();
        HttpResponse response=httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode()==200) return true;
        else {
            throw new Exception("loi khi goi api them nhan vien");
        }
    }

//    public boolean suaNhanVien(nhanvien nhanvien) throws Exception {
//
//    }
//
//    public boolean xoaNhanVien(String manhanvienString) throws Exception {
//
//    }
//
//    public List<nhanvien> timKiemNhanVien(String manhanvienString) throws Exception {
//
//    }
//
    public List<nhanvien> getAllNhanVien() throws Exception {
        HttpRequest request= HttpRequest.newBuilder()
                .uri(URI.create(apiurl))
                .GET()
                .build();
        HttpResponse response=httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        System.out.println(response.body().toString());
        if(response.statusCode()==200) {
            Type reponsetype=new TypeToken<List<nhanvien>>(){}.getType();
            return gson.fromJson(response.body().toString(),reponsetype);
        }
        else {
            throw new Exception("loi khi goi api them nhan vien");
        }

    }

}
