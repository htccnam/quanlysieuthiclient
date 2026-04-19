package com.example.quanlysieuthiclient.APICLIENT;

import com.example.quanlysieuthiclient.DTO.chucvu;
import com.example.quanlysieuthiclient.UTIL.configLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class chucvuApiClient {
    @Getter
    private static final chucvuApiClient instance=new chucvuApiClient();

    private final String apiUrl = configLoader.getbaseapiurl()+"/chucvu";
    private final Gson gson=new Gson();
    public HttpClient httpClient= HttpClient.newBuilder().build();

    public List<chucvu> getAllChucVu() throws Exception {
        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();
        HttpResponse<String> response=httpClient.send(request,HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<chucvu>>(){}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi lấy danh sách chức vụ: " + response.statusCode());
    }

    public void themChucVu(chucvu cv) throws Exception {
        String json = gson.toJson(cv);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception(response.body()); // ném exception với nội dung lỗi từ server
        }
    }

    public void suaChucVu(chucvu cv) throws Exception {
        String json = gson.toJson(cv);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + cv.getMachucvu()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response=httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }
    public void xoaChucVu(String machucvu) throws Exception {
        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create(apiUrl+"/"+machucvu))
                .DELETE()
                .build();

        HttpResponse<String> response= httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() !=200 ){
            throw new Exception(response.body());
        }
    }
    public List<chucvu> timKiemChucVu(String keyword) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/search?keyword=" + keyword))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode()==200){
            Type type=new TypeToken<List<chucvu>>(){}.getType();
            return gson.fromJson(response.body(),type);
        }
        throw new Exception("lỗi tìm kiếm chức vụ:"+response.statusCode());

    }
}
