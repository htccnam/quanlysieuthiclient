# BÁO CÁO THẨM ĐỊNH EGM: PHÂN HỆ SỬA / CẬP NHẬT ĐƠN HÀNG (CLIENT & API)

**Mã tài liệu:** `2026-11-03_SUA_DON_HANG_REVIEW_REPORT.md`  
**Ngày thẩm định:** 03/11/2026  
**Phạm vi:** Luồng bàn giao dữ liệu sửa đơn giữa `ChiTietDonHangController` và `TaoDonController`, giao diện `TaoDonView`, kết nối `DonHangApiClient` và xử lý tại backend `DonHangController` / `DonHangServiceImpl`.

---

## 1. TỔNG QUAN LUỒNG SỬA ĐƠN HÀNG THỰC TẾ

Quy trình sửa/cập nhật đơn hàng hiện tại gồm 3 chặng:

```
[ChiTietView: Chọn đơn & Bấm Sửa]
               │
               ▼
   [ChiTietDonHangController]
       ├─ (1) Gọi getAllDonHang() và filter lấy DonHang
       ├─ (2) Gọi getChiTietByMa(maDH) lấy ds ChiTietDon
       └─ (3) Tạo JFrame("Sửa đơn hàng") chứa TaoDonView + TaoDonController
               │
               ▼
     [TaoDonController.hienThiDonHangDeSua]
       ├─ Đặt isEditMode = true, khóa txtMaDon (setEditable(false))
       ├─ Điền ngày lập, phương thức bán, phương thức thanh toán
       ├─ Chọn ComboBox nhân viên (theo mã)
       ├─ Đổ chi tiết các mặt hàng vào bảng giỏ hàng
       └─ Tính lại tổng tiền
               │
               ▼ (Người dùng sửa giỏ hàng và bấm "Lưu")
     [TaoDonController.luuDonHang]
       ├─ Kiểm tra giỏ hàng rỗng, mã đơn
       ├─ Gửi PUT /api/donhang/{maDH} với payload { donHang, chitiet }
       └─ Hiện thông báo thành công, reset form
               │
               ▼
     [Backend: DonHangServiceImpl.capNhatDon]
       ├─ Cập nhật thông tin bảng donhang
       ├─ Xóa toàn bộ chi tiết cũ trong chitietdonhang theo maDH
       └─ Lưu lại danh sách chi tiết mới
```

---

## 2. PHÂN TÍCH RỦI RO & CÁC LỖI TRỌNG YẾU (AUDIT FINDINGS)

### 🔴 1. Quên khôi phục Khách hàng trên form sửa (Mất thông tin Khách hàng)
* **Vị trí:** [`TaoDonController.java:184-210`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java#L184-L210)
* **Thực trạng:**
  * Trong hàm `hienThiDonHangDeSua()`, Controller có vòng lặp duyệt `cboNV` để chọn lại đúng Nhân viên (`view.getCboNV().getItemAt(i).startsWith(dh.getManhanvien())`).
  * **Tuy nhiên, `cboKH` (Khách hàng) hoàn toàn bị bỏ sót**, không được chọn lại theo `dh.getMakhachhang()`.
* **Hậu quả:**
  * Khi mở form sửa, ComboBox khách hàng luôn giữ giá trị mặc định ở vị trí đầu tiên (khách hàng đầu tiên trong danh bạ).
  * Nếu thu ngân chỉ muốn sửa số lượng món hàng mà không để ý ComboBox khách hàng, khi bấm "Lưu", đơn hàng sẽ **bị đổi chủ sang khách hàng khác** trong cơ sở dữ liệu.

---

### 🔴 2. Không khôi phục Mã khuyến mãi đã áp dụng
* **Vị trí:** [`TaoDonController.java:184-210`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java#L184-L210)
* **Thực trạng:**
  * `dh.getMakhuyenmai()` không được nạp lại vào `view.getCboMaKM()`.
* **Hậu quả:**
  * `cboMaKM` bị reset về chuỗi rỗng `""`.
  * Tổng tiền của đơn hàng bị tính lại mà không có chiết khấu khuyến mãi cũ, khiến tiền thanh toán bị đội lên sai lệch so với lúc tạo đơn ban đầu.

---

### 🔴 3. Màn hình danh sách đơn hàng không tự động cập nhật và cửa sổ sửa không tự đóng
* **Vị trí:** [`ChiTietDonHangController.java:99-112`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/ChiTietDonHangController.java#L99-L112) và [`TaoDonController.java:272-277`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java#L272-L277)
* **Thực trạng:**
  * `ChiTietDonHangController` mở một cửa sổ `JFrame` độc lập mới để sửa đơn nhưng **không truyền callback hoặc listener** báo khi sửa xong.
  * Sau khi `TaoDonController` gọi API PUT thành công, nó hiển thị `JOptionPane.showMessageDialog` rồi gọi `resetForm()`, để lại một cửa sổ trắng tinh với tiêu đề "Sửa đơn hàng".
  * Màn hình danh sách `ChiTietView` bên dưới **không hề chạy `loadTable()`**, thu ngân vẫn nhìn thấy thông tin cũ chưa cập nhật trên bảng.

---

### 🔴 4. Không cập nhật và kiểm soát số lượng tồn kho (Inventory Decoupling)
* **Vị trí:** [`DonHangServiceImpl.java:64-91`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthi_api/src/main/java/com/example/quanlysieuthi_api/Service/DonHangServiceImpl.java#L64-L91)
* **Thực trạng:**
  * Cả khi tạo đơn lẫn khi cập nhật đơn, backend chỉ thao tác trên hai bảng `donhang` và `chitietdonhang`. Bảng `sanpham` (cột `soluong` tồn kho) **hoàn toàn không được cộng hoàn kho hay trừ thêm**.
* **Hậu quả:**
  * Nếu đơn hàng ban đầu có 1 sản phẩm, thu ngân sửa lại thành 20 sản phẩm, kho thực tế mất 19 sản phẩm nhưng database kho vẫn giữ nguyên số lượng cũ. Dữ liệu kho và doanh thu bị phân mảnh.

---

### 🟡 5. Tải toàn bộ cơ sở dữ liệu về Client chỉ để lọc 1 đơn hàng (Lãng phí tài nguyên mạng)
* **Vị trí:** [`ChiTietDonHangController.java:91-95`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/ChiTietDonHangController.java#L91-L95)
* **Thực trạng:**
  ```java
  List<DonHang> dsDonHang = apiClient.getAllDonHang();
  DonHang dhSelected = dsDonHang.stream()
          .filter(d -> d.getMadonhang().equals(maDH))
          .findFirst().orElse(null);
  ```
* **Hậu quả:**
  * Khi hệ thống có hàng chục nghìn đơn hàng, mỗi lần bấm sửa 1 đơn, Client phải tải toàn bộ file JSON khổng lồ của tất cả các đơn hàng về máy rồi dùng Java Stream lọc lấy 1 dòng.
  * Backend thực chất đã có sẵn endpoint `GET /api/donhang/{madonhang}` để lấy riêng một đơn nhanh chóng.

---

## 3. BẢNG ĐỐI CHIẾU TRẠNG THÁI (EXPECTED VS REALITY)

| Tiêu chí | Mong muốn chuẩn mực (Expected) | Hiện trạng mã nguồn (Reality) | Mức độ rủi ro |
| :--- | :--- | :--- | :--- |
| **Bảo toàn Khách hàng** | Giữ nguyên khách hàng của đơn cũ khi sửa | Bị bỏ quên, nhảy về khách hàng đầu tiên trong ComboBox | 🔴 Cực kỳ nghiêm trọng |
| **Bảo toàn Khuyến mãi** | Chọn lại đúng mã KM và tiền giảm cũ | Bị bỏ trống, mất khuyến mãi của đơn | 🔴 Nghiêm trọng |
| **Đồng bộ giao diện** | Tự đóng form sửa và reload bảng danh sách đơn | Cửa sổ sửa bị treo trắng trơn; bảng cũ không reload | 🔴 Nghiêm trọng |
| **Hiệu năng mạng** | Gọi trực tiếp `GET /donhang/{id}` | Tải toàn bộ bảng `getAllDonHang()` rồi lọc bằng Stream | 🟡 Trung bình |
| **Quản lý kho hàng** | Hoàn trả kho chi tiết cũ, trừ kho chi tiết mới | Không chạm đến tồn kho `sanpham` | 🔴 Nghiêm trọng (Logic hệ thống) |

---

## 4. KẾ HOẠCH KHẮC PHỤC TRIỆT ĐỂ (ACTIONABLE REMEDIATION)

1. **Khắc phục Client - `TaoDonController.java`**:
   * Bổ sung chọn lại Khách hàng trong `hienThiDonHangDeSua`:
     ```java
     for (int i = 0; i < view.getCboKH().getItemCount(); i++) {
         if (view.getCboKH().getItemAt(i).startsWith(dh.getMakhachhang())) {
             view.getCboKH().setSelectedIndex(i);
             break;
         }
     }
     ```
   * Bổ sung nạp lại Khuyến mãi: `view.getCboMaKM().setSelectedItem(dh.getMakhuyenmai() != null ? dh.getMakhuyenmai() : "");`
   * Thêm callback `Runnable onSaveSuccessCallback` để đóng cửa sổ `JFrame` sửa và kích hoạt `loadTable()` bên `ChiTietDonHangController`.

2. **Khắc phục Client - `ChiTietDonHangController.java`**:
   * Thay thế `getAllDonHang().stream().filter(...)` bằng việc lấy thông tin trực tiếp từ dòng được chọn trong bảng hoặc gọi `apiClient.getDonHangById(maDH)`.
   * Truyền callback đóng cửa sổ và tải lại bảng sau khi cập nhật thành công.

