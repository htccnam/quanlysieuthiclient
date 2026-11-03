# BÁO CÁO THẨM ĐỊNH EGM: PHÂN HỆ TẠO ĐƠN HÀNG (CLIENT & API)

**Mã tài liệu:** `2026-11-03_TAO_DON_REVIEW_REPORT.md`  
**Ngày thẩm định:** 03/11/2026  
**Phạm vi:** Luồng nghiệp vụ, giao diện Client Swing, điều khiển Controller, hợp đồng API và truyền tải dữ liệu DTO.

---

## 1. TỔNG QUAN HỆ THỐNG VÀ CÁC THÀNH PHẦN LIÊN QUAN

Phân hệ Tạo đơn hàng phụ trách toàn bộ quy trình bán hàng tại quầy (POS), gồm các thành phần:
* **Giao diện Client (VIEW):**
  * [`TaoDonView.java`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/VIEW/TaoDonView.java): Giao diện lập đơn (bảng danh sách hàng hóa, bộ chọn số lượng `JSpinner`, bảng giỏ hàng, thông tin thanh toán, khuyến mãi).
  * [`ChiTietView.java`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/VIEW/ChiTietView.java): Bảng theo dõi lịch sử đơn hàng, xem chi tiết và kích hoạt chế độ sửa đơn.
* **Tầng Điều khiển (CONTROLLER):**
  * [`TaoDonController.java`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java): Xử lý sự kiện click nút, tính toán tổng tiền, chiết khấu và gửi dữ liệu lên API.
  * [`ChiTietDonHangController.java`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/ChiTietDonHangController.java): Điều phối danh sách đơn, xóa đơn và truyền dữ liệu đơn cũ sang `TaoDonView` khi sửa.
* **Tầng Mạng & Hợp đồng API (APICLIENT):**
  * [`DonHangApiClient.java`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/APICLIENT/DonHangApiClient.java): Giao tiếp HTTP RESTful với backend qua `GET /donhang`, `GET /donhang/{id}/chitiet`, `POST /donhang`, `PUT /donhang/{id}`, `DELETE /donhang/{id}`.
  * Các client phụ thuộc: `sanphamApiClient`, `KhachHangApiClient`, `nhanvienApiClient`, `khuyenmaiApiClient`.
* **Mô hình Dữ liệu (DTO):**
  * [`DonHang.java`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/DTO/DonHang.java): Chứa mã đơn, mã nhân viên, mã khách hàng, ngày lập, phương thức bán, phương thức thanh toán, tổng tiền.
  * [`ChiTietDon.java`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/DTO/ChiTietDon.java): Chứa mã đơn, mã sản phẩm, tên sản phẩm, số lượng, đơn giá, thành tiền.

---

## 2. PHÂN TÍCH LUỒNG HOẠT ĐỘNG THỰC TẾ (OPERATIONAL FLOW)

### Luồng 1: Khởi tạo dữ liệu (Initialization)
1. Khi `TaoDonController` được khởi tạo, hàm `initData()` gọi tuần tự 4 API:
   * `nvApiClient.getAllNhanVien()`
   * `KhachHangApiClient.getInstance().getAllKhachHang()`
   * `sanphamApiClient.getInstance().getAllSanPham()`
   * `khuyenmaiApiClient.getInstance().getAllKhuyenMai()`
2. Dữ liệu trả về được nạp vào các `JComboBox` nhân viên, khách hàng, khuyến mãi và bảng sản phẩm bên trái.

### Luồng 2: Thêm và bớt hàng trong giỏ (Cart Operation)
1. Thu ngân chọn một dòng trên bảng sản phẩm (`tableSanPham`), chọn số lượng trên `JSpinner` (giới hạn từ 1 đến 100), rồi nhấn nút `btnThem`.
2. Hệ thống tính `thanhTien = gia * soLuong` và chèn trực tiếp dòng mới vào `table` (giỏ hàng bên phải).
3. Sau khi chèn, gọi hàm `tinhToanTongTien()`.
4. Nếu chọn một dòng trên bảng giỏ hàng và nhấn `btnXoa`, dòng đó bị xóa và tính lại tổng tiền.

### Luồng 3: Tính toán chiết khấu và tổng thanh toán
1. Hàm `tinhToanTongTien()` duyệt qua tất cả các dòng trong giỏ hàng, cộng dồn cột thành tiền thành `tamTinh`.
2. Đọc mã khuyến mãi đang chọn từ `cboMaKM`.
3. Áp dụng tỷ lệ giảm giá:
   * Nếu chọn `KM01` -> giảm 10%
   * Nếu chọn `KM02` -> giảm 20%
   * Nếu chọn `KM03` -> giảm 30%
4. Tính `tienGiam = tamTinh * phanTramGiam` và `tongCuoi = tamTinh - tienGiam`.
5. Cập nhật nhãn `lblTamTinh`, `lblKM`, `lblTongTien`.

### Luồng 4: Lưu đơn hàng mới lên API
1. Khi nhấn `btnLuu`, hàm `luuDonHang()` thu thập thông tin:
   * Mã đơn hàng: lấy từ `txtMaDon`.
   * Mã nhân viên: tách chuỗi ComboBox theo ký tự `" - "` (`split(" - ")[0]`).
   * Mã khách hàng: tách chuỗi ComboBox tương tự.
   * Ngày giao dịch: lấy từ `ngayGD` hoặc `new Date()`.
   * Phương thức bán, thanh toán, tổng tiền.
2. Quét toàn bộ dòng trong bảng giỏ hàng để tạo danh sách `List<ChiTietDon>`.
3. Gọi `DonHangApiClient.getInstance().saveDonHang(dh, dsChiTiet)`:
   * Đóng gói payload: `Map.of("donHang", dh, "chitiet", dsChiTiet)`.
   * Gửi HTTP POST với định dạng JSON (ngày định dạng `yyyy-MM-dd`).
   * Nếu backend phản hồi mã khác `200`, ném Exception thông báo lỗi.
4. Xóa trắng form (`resetForm()`) nếu thành công.

### Luồng 5: Sửa đơn hàng cũ
1. Từ màn hình `ChiTietView`, thu ngân chọn đơn hàng và nhấn `btnSua`.
2. `ChiTietDonHangController` lấy đơn hàng `dhSelected` và chi tiết `dsChiTiet` từ API qua `getChiTietByMa(maDH)`.
3. Mở cửa sổ `TaoDonView` mới, gọi `taoDonController.hienThiDonHangDeSua(dhSelected, dsChiTiet)`.
4. `isEditMode` chuyển thành `true`, khóa không cho sửa `txtMaDon`.
5. Khi người dùng nhấn `btnLuu`, controller gọi `donHangApiClient.updateDonHang(...)` (HTTP PUT `/donhang/{maDH}`).

---

## 3. CÁC ĐIỂM YẾU, LỖI LOGIC VÀ RỦI RO HỆ THỐNG (STRESS-TEST & AUDIT FINDINGS)

### 🔴 1. Khuyến mãi bị gán cứng (Hardcoded Promotion Rate)
* **Vị trí:** [`TaoDonController.java:121-125`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java#L121-L125)
* **Thực trạng:**
  ```java
  if (maKM.equals("KM01")) phanTramGiam = 0.1;      // 10%
  else if (maKM.equals("KM02")) phanTramGiam = 0.2; // 20%
  else if (maKM.equals("KM03")) phanTramGiam = 0.3; // 30%
  ```
* **Rủi ro:**
  * Mặc dù controller gọi `khuyenmaiApiClient.getAllKhuyenMai()` để lấy danh sách từ cơ sở dữ liệu, việc tính chiết khấu lại cố định bằng chuỗi mã `"KM01"`, `"KM02"`, `"KM03"`.
  * Nếu quản trị viên thêm mã mới trên hệ thống (ví dụ `KM04`, `SALE50`) hoặc thay đổi giá trị giảm giá trong bảng khuyến mãi, ứng dụng Client hoàn toàn bỏ qua và áp mức giảm `0%`.
  * DTO `khuyenmai` có trường `sotiengiam` (số tiền giảm), nhưng controller lại tự quy đổi thành % giảm giá gán cứng.

### 🔴 2. Cho phép lưu đơn hàng rỗng (Zero-item Order Creation)
* **Vị trí:** [`TaoDonController.java:166-204`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java#L166-L204)
* **Thực trạng:**
  * Trong hàm `luuDonHang()`, không có câu lệnh kiểm tra số lượng dòng trong giỏ: `view.getTable().getRowCount() > 0`.
* **Hậu quả:**
  * Thu ngân có thể nhấn nút "Lưu" khi chưa chọn bất kỳ sản phẩm nào.
  * Đơn hàng rỗng với tổng tiền 0đ và danh sách `chitiet: []` vẫn được gửi lên server qua `POST /donhang`, gây ô nhiễm dữ liệu và sai lệch báo cáo thống kê doanh thu.

### 🔴 3. Thiếu cơ chế gộp sản phẩm trùng lặp trong giỏ hàng
* **Vị trí:** [`TaoDonController.java:77-99`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java#L77-L99)
* **Thực trạng:**
  * Mỗi lần nhấn "Thêm", hàm luôn thực hiện `modelGioHang.addRow(...)`.
* **Hậu quả:**
  * Nếu thu ngân quét hoặc chọn lại một sản phẩm đã có trong giỏ (ví dụ muốn thêm 1 lon sữa), bảng giỏ hàng sẽ xuất hiện 2 dòng cùng mã sản phẩm `SP01`.
  * Vi phạm tính duy nhất của khóa phụ `(madonhang, masanpham)` ở tầng cơ sở dữ liệu backend, dễ dẫn đến lỗi duplicate primary key / unique constraint khi lưu `ChiTietDon`.

### 🔴 4. Không kiểm tra số lượng tồn kho (Inventory Overselling)
* **Vị trí:** [`TaoDonController.java:87-92`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java#L87-L92)
* **Thực trạng:**
  * Hàm chỉ kiểm tra `if (soLuong <= 0)`, không đối chiếu với số lượng tồn kho `sanpham.getSoluong()` trong danh sách sản phẩm.
* **Hậu quả:**
  * Mặt hàng chỉ còn 2 sản phẩm trong kho nhưng thu ngân có thể nhập số lượng 50 vào giỏ và xuất hóa đơn thành công, gây âm kho và sai lệch thực tế.

### 🔴 5. Toàn bộ lệnh gọi mạng HTTP thực thi đồng bộ trên luồng giao diện (UI Freeze on EDT)
* **Vị trí:** [`TaoDonController.java:32-58`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java#L32-L58) và [`DonHangApiClient.java:29, 57`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/APICLIENT/DonHangApiClient.java#L29)
* **Thực trạng:**
  * `DonHangApiClient` dùng `httpClient.send(...)` (đồng bộ blocking).
  * Trong `TaoDonController`, lệnh gọi `initData()` và `luuDonHang()` nằm trực tiếp trên Event Dispatch Thread (EDT) của Swing.
* **Hậu quả:**
  * Khi mạng chập chờn hoặc server xử lý chậm, toàn bộ cửa sổ ứng dụng bị đơ (Not Responding). Nếu có lỗi timeout, ứng dụng dừng đột ngột mà không có spinner/loading indicator.

### 🟡 6. Tính năng tìm kiếm sản phẩm chưa được lập trình
* **Vị trí:** [`TaoDonController.java:69-74`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java#L69-L74)
* **Thực trạng:**
  ```java
  view.getTxtTimKiem().addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
      }
  });
  ```
* **Hậu quả:**
  * Ô nhập từ khóa tìm kiếm sản phẩm hoàn toàn không hoạt động. Thu ngân trong siêu thị có hàng nghìn sản phẩm phải cuộn chuột thủ công trên bảng để tìm mặt hàng.

### 🟡 7. Rủi ro phân tích chuỗi tiền tệ bằng biểu thức chính quy (Regex String Parsing)
* **Vị trí:** [`TaoDonController.java:181-182`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/CONTROLLER/TaoDonController.java#L181-L182)
* **Thực trạng:**
  ```java
  String tongTienStr = view.getLblTongTien().getText().replaceAll("[^0-9]", "");
  dh.setTongtien(Double.parseDouble(tongTienStr));
  ```
* **Hậu quả:**
  * Thay vì lưu trữ giá trị số thực `double tongCuoi` trong Controller, mã nguồn lại định dạng ra nhãn UI (`String.format("%,.0f đ", tongCuoi)`), sau đó khi lưu lại lấy text từ nhãn UI và dùng regex gạt bỏ ký tự để parse ngược lại thành số.
  * Nếu định dạng hiển thị có phần thập phân (ví dụ tiền tệ quốc tế hoặc giảm giá lẻ), regex `[^0-9]` sẽ ghép số nguyên và thập phân lại làm một (ví dụ `100.50` thành `10050`), gây sai lệch dữ liệu tài chính nghiêm trọng.

### 🟡 8. Hợp đồng API (API Contract) chưa có quản lý trạng thái đơn và giao dịch (Transaction)
* **Vị trí:** [`DonHangApiClient.java:49`](file:///c:/Users/HP/Documents/GitHub/quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/APICLIENT/DonHangApiClient.java#L49)
* **Thực trạng:**
  * Payload POST lên server: `Map.of("donHang", dh, "chitiet", dsChiTiet)`.
  * Mô hình `DonHang` không có trường trạng thái (`trangthai` ví dụ: `CHUA_THANH_TOAN`, `DA_THANH_TOAN`, `HUY`).
  * Không có xác thực idempotency (khóa chống trùng lặp request khi bấm nút Lưu nhiều lần liên tục).

---

## 4. KẾ HOẠCH KHẮC PHỤC CHI TIẾT (ACTIONABLE REMEDIATION)

| Ưu tiên | Hạng mục cần sửa | Giải pháp kỹ thuật |
| :--- | :--- | :--- |
| **P0 (Cực kỳ khẩn cấp)** | Khắc phục hardcode khuyến mãi | Đọc trực tiếp trường `sotiengiam` từ đối tượng `khuyenmai` được lưu trong map dữ liệu, thay vì so sánh chuỗi `"KM01"`, `"KM02"`. |
| **P0 (Cực kỳ khẩn cấp)** | Chặn lưu đơn rỗng | Thêm điều kiện kiểm tra: `if (view.getTable().getRowCount() == 0) { JOptionPane.showMessageDialog(view, "Giỏ hàng rỗng!"); return; }`. |
| **P1 (Quan trọng)** | Gộp dòng khi trùng mã SP | Trong `themSanPhamVaoGio()`, duyệt qua `tableGioHang`. Nếu `masp` đã tồn tại, tăng `soluong` của dòng đó lên thay vì thêm dòng mới. |
| **P1 (Quan trọng)** | Kiểm tra tồn kho | So sánh `(soLuongHienCoTrongGio + soLuongChonMoi) <= sp.getSoluong()`. Nếu vượt quá, hiện cảnh báo không cho thêm. |
| **P1 (Quan trọng)** | Triển khai tìm kiếm SP | Sử dụng `RowFilter` trên `TableRowSorter<DefaultTableModel>` của bảng sản phẩm khi người dùng gõ vào `txtTimKiem`. |
| **P2 (Cải tiến chất lượng)** | Lưu trữ biến số tổng tiền | Lưu `double currentTongTien` trong Controller; không đọc ngược từ nhãn hiển thị UI bằng regex. |
| **P2 (Cải tiến chất lượng)** | Chạy tác vụ mạng ngầm | Đưa các lệnh gọi `DonHangApiClient` vào `SwingWorker` để không chặn luồng giao diện người dùng. |

