# Goldfish Annotated Review Report: Sửa / Cập Nhật Đơn Hàng

**Target:** [`docs/designs/2026-11-03_SUA_DON_HANG_REVIEW_REPORT.md`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/docs/designs/2026-11-03_SUA_DON_HANG_REVIEW_REPORT.md)  
**Status:** ALL ISSUES RESOLVED  
**Build Status:** `BUILD SUCCESS` (Java 21)

---

## Danh Sách Các Lỗi Đã Được Khắc Phục (Resolved Items)

### 1. [PROPOSE FIX] Khôi phục chính xác Khách hàng khi sửa đơn
* **Mức độ:** Khẩn cấp (High)
* **File ảnh hưởng:** [`TaoDonController.java`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java)
* **Giải pháp đã áp dụng:** Trong `hienThiDonHangDeSua()`, bổ sung vòng lặp quét qua `cboKH` để tự động chọn đúng khách hàng của đơn hàng cũ theo tiền tố mã `dh.getMakhachhang()`. Đảm bảo đơn hàng không bị vô tình chuyển sang tên khách hàng khác.

### 2. [PROPOSE FIX] Khôi phục và lưu trữ Mã khuyến mãi (`makhuyenmai`)
* **Mức độ:** Cao (Medium)
* **File ảnh hưởng:** [`TaoDonController.java`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java)
* **Giải pháp đã áp dụng:**
  - Trong `hienThiDonHangDeSua()`: Chọn lại mã khuyến mãi cũ trên `cboMaKM` nếu đơn hàng có khuyến mãi.
  - Trong `luuDonHang()`: Đọc giá trị từ `cboMaKM` và gán vào `dh.setMakhuyenmai(...)` trước khi gửi lên API (khắc phục lỗi trước đó luôn để `null`).

### 3. [PROPOSE FIX] Tự động đóng cửa sổ sửa và làm mới bảng danh sách đơn hàng
* **Mức độ:** Cao (Medium)
* **File ảnh hưởng:** [`TaoDonController.java`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java) và [`ChiTietDonHangController.java`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/ChiTietDonHangController.java)
* **Giải pháp đã áp dụng:**
  - Thêm callback `onSaveSuccessCallback` trong `TaoDonController`.
  - Trong `ChiTietDonHangController.java`, khi khởi tạo cửa sổ sửa, gắn callback:
    ```java
    taoDonController.setOnSaveSuccessCallback(() -> {
        frame.dispose(); // Đóng cửa sổ sửa
        loadTable();     // Tải lại bảng danh sách đơn hàng bên dưới
    });
    ```
  - Khi lưu cập nhật thành công, cửa sổ sửa sẽ tự động đóng và bảng danh sách đơn hàng ngoài màn hình chính được làm mới tức thì với số liệu mới nhất.

