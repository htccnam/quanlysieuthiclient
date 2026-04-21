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

    public void themNhanVien(nhanvien nhanvien) throws Exception {
        String json=gson.toJson(nhanvien);
        HttpRequest request= HttpRequest.newBuilder()
                .uri(URI.create(apiurl))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse response=httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode()!=200) {
            throw new Exception(response.body().toString());
        }
    }

    public void suaNhanVien(nhanvien nhanvien) throws Exception {
        String json=gson.toJson(nhanvien);
        HttpRequest request= HttpRequest.newBuilder()
                .uri(URI.create(apiurl+"/"+nhanvien.getManhanvien()))
                .header("Content-Type","application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response=httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        if(response.statusCode()!=200) {
            throw new Exception(response.body().toString());
        }
    }

    public void xoaNhanVien(String manhanvienString) throws Exception {
            HttpRequest request=HttpRequest.newBuilder()
                    .uri(URI.create(apiurl+"/"+manhanvienString))
                    .DELETE()
                    .build();

            HttpResponse<String> response=httpClient.send(request,HttpResponse.BodyHandlers.ofString());
            if(response.statusCode()!=200){
                throw new Exception(response.body());
            }
    }

    public List<nhanvien> timKiemNhanVien(String keyword) throws Exception {
            HttpRequest request=HttpRequest.newBuilder()
                    .uri(URI.create(apiurl+"/search?keyword="+keyword))
                    .GET()
                    .build();
            HttpResponse<String> response=httpClient.send(request,HttpResponse.BodyHandlers.ofString());

            if(response.statusCode()==200){
                    Type type=new TypeToken<List<nhanvien>>(){}.getType();
                    return gson.fromJson(response.body(),type);
            }else {
                    throw new Exception("lỗi tìm kiếm nhân viên:"+response.statusCode());
            }
    }

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
            throw new Exception("loi khi goi api them nhan vien"+response.statusCode());
        }

    }

}
