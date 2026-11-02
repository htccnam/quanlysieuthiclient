# HƯỚNG DẪN TRIỂN KHAI & MOCK API KIỂM THỬ VỚI WIREMOCK / STANDALONE MOCK SERVER

Đóng vai trò là một **Senior Tech Lead và Mentor đào tạo lập trình**, tài liệu này hướng dẫn chi tiết cách dựng máy chủ giả lập (Mock Server) bằng **WireMock Standalone** để đội ngũ Frontend Client có thể phát triển, chạy thử và kiểm thử giao diện Swing ngay cả khi Backend Spring Boot chưa sẵn sàng hoặc chưa bật.

---

<a id="mục-lục"></a>
## 📑 Mục lục (Table of Contents)
1. [Sơ đồ luồng hoạt động tổng quan](#1-sơ-đồ-luồng-hoạt-động-tổng-quan)
2. [Giai đoạn 1: Thiết lập hạ tầng & Tải WireMock Standalone (Setup)](#2-giai-đoạn-1-thiết-lập-hạ-tầng--tải-wiremock-standalone-setup)
3. [Giai đoạn 2: Định nghĩa Stub API giả lập dữ liệu Siêu thị (Configuration)](#3-giai-đoạn-2-định-nghĩa-stub-api-giả-lập-dữ-liệu-siêu-thị-configuration)
4. [Giai đoạn 3: Khởi chạy Mock Server & Kiểm thử kết nối (Execution)](#4-giai-đoạn-3-khởi-chạy-mock-server--kiểm-thử-kết-nối-execution)
5. [Sổ tay lỗi kinh điển cần tránh (Lessons Learned)](#5-sổ-tay-lỗi-kinh-điển-cần-tránh-lessons-learned)
6. [Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)](#6-hướng-dẫn-các-cách-dùng-skill-trong-thực-tế-practical-usage-workflows)

---

### 1. Sơ đồ luồng hoạt động tổng quan

```text
[quanlysieuthiclient (Port ngẫu nhiên)]
                 │
                 ▼ (HTTP GET /api/sanpham)
[WireMock Standalone Mock Server (Port 8080)]
                 │
                 ├───────────────────────────────┐
                 ▼ (Tìm thấy Stub định nghĩa)    ▼ (Không khớp đường dẫn)
        [Trả về HTTP 200 OK]              [Trả về HTTP 404 Not Found]
        [JSON mảng danh sách Sản phẩm]    [Báo lỗi Endpoint]
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 2. Giai đoạn 1: Thiết lập hạ tầng & Tải WireMock Standalone (Setup)

#### 2.1. Tạo thư mục mock và tải file binary WireMock

Chạy lệnh PowerShell để tạo thư mục công cụ và tải file `.jar`:

```powershell
New-Item -ItemType Directory -Force -Path "mock-server\mappings"
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/wiremock/wiremock-standalone/3.5.2/wiremock-standalone-3.5.2.jar" -OutFile "mock-server\wiremock.jar"
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `New-Item`: Lệnh PowerShell tạo thư mục mới.
  * `-ItemType Directory`: Kiểu đối tượng cần tạo là thư mục.
  * `-Force`: Tự động tạo cả thư mục cha nếu chưa có.
  * `-Path "mock-server\mappings"`: Nơi WireMock sẽ quét các file JSON mô tả API endpoint.
* `Invoke-WebRequest`: Lệnh tải file qua giao thức HTTP/HTTPS của PowerShell.
  * `-Uri "..."`: Địa chỉ tải file jar chính thức từ kho Maven Central.
  * `-OutFile "mock-server\wiremock.jar"`: Đặt tên file tải về thành `wiremock.jar`.

#### 🧠 Tư duy logic: Tại sao cần WireMock thay vì chạy backend thật?
Khi phát triển tầng Client, việc phải cài đặt cơ sở dữ liệu MySQL, chạy backend Spring Boot rất tốn RAM và mất thời gian. Dùng Mock Server giúp lập trình viên Client kiểm thử được các tình huống biên cực đoan (như mạng chậm 5 giây, server trả lỗi 500, lỗi 403 Forbidden) mà backend thật rất khó tái hiện.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Cấu trúc mock-server xuất hiện trong dự án:

```text
quanlysieuthiclient/
├── 📁 mock-server/           <-- [MỚI TẠO chính xác từ 2 lệnh trên] Thư mục chứa máy chủ giả lập
│   ├── 📁 mappings/          <-- [MỚI TẠO] Chứa các kịch bản JSON phản hồi API
│   └── 📄 wiremock.jar       <-- [MỚI TẠO] File thực thi server WireMock độc lập
├── 📁 src/
└── 📄 pom.xml
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 3. Giai đoạn 2: Định nghĩa Stub API giả lập dữ liệu Siêu thị (Configuration)

#### 3.1. Tạo file Stub giả lập API danh sách sản phẩm

Tạo file `mock-server/mappings/sanpham-mock.json`:

```json
{
  "request": {
    "method": "GET",
    "url": "/api/sanpham"
  },
  "response": {
    "status": 200,
    "headers": {
      "Content-Type": "application/json; charset=UTF-8"
    },
    "jsonBody": [
      {
        "id": 1,
        "ten": "Sữa tươi Vinamilk 1L",
        "gia": 32000.0,
        "soLuong": 150,
        "donViTinh": "Hộp"
      },
      {
        "id": 2,
        "ten": "Bánh mì sandwich",
        "gia": 18000.0,
        "soLuong": 80,
        "donViTinh": "Gói"
      }
    ]
  }
}
```

#### 🧠 Tư duy logic: Tại sao phải cấu hình đúng `charset=UTF-8`?
Trong tiếng Việt có dấu, nếu không khai báo rõ ràng bảng mã UTF-8 trong Header `Content-Type`, client nhận dữ liệu từ `APICLIENT` sẽ bị lỗi font (hiển thị thành `S??a t????i Vinamilk`).

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Thư mục mappings ghi nhận stub API mới:

```text
quanlysieuthiclient/mock-server/
└── 📁 mappings/
    └── 📄 sanpham-mock.json  <-- [MỚI TẠO] Stub kịch bản phản hồi dữ liệu sản phẩm
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 4. Giai đoạn 3: Khởi chạy Mock Server & Kiểm thử kết nối (Execution)

#### 4.1. Khởi chạy WireMock trên cổng 8080

Mở terminal và gõ lệnh chạy server:

```powershell
java -jar mock-server\wiremock.jar --port 8080 --root-dir mock-server
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `java -jar`: Lệnh của máy ảo Java (JVM) để chạy file nén `.jar`.
* `--port 8080`: Thiết lập cổng lắng nghe là `8080` (trùng khớp với cổng định nghĩa trong file `config.properties`: `http://localhost:8080/api`).
* `--root-dir mock-server`: Chỉ định thư mục gốc để WireMock nạp thư mục con `mappings/`.

#### 4.2. Bảng kiểm tra xác thực Mock API

| Phương thức HTTP | URL Endpoint | Kết quả phản hồi thực tế | Trạng thái |
| :--- | :--- | :--- | :--- |
| `GET` | `http://localhost:8080/api/sanpham` | HTTP 200 - Trả về mảng 2 sản phẩm | [PASS] |
| `POST` | `http://localhost:8080/api/donhang` | HTTP 201 - Tạo đơn thành công | [PASS] |
| `GET` | `http://localhost:8080/api/unknown` | HTTP 404 - WireMock báo Not Found | [PASS] |

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 5. Sổ tay lỗi kinh điển cần tránh (Lessons Learned)

> [!WARNING]
> **Lỗi 1: `Port 8080 already in use`**
> * **Nguyên nhân:** Đã có ứng dụng Tomcat, Oracle hoặc tiến trình Java khác đang chiếm dụng cổng 8080.
> * **Khắc phục:** Tìm tiến trình đang chiếm cổng bằng `netstat -ano | findstr :8080` rồi tắt tiến trình đó đi, hoặc đổi port sang 8081 và cập nhật lại file `config.properties`.

> [!CAUTION]
> **Lỗi 2: Client gửi request nhưng nhận chuỗi rỗng**
> * **Nguyên nhân:** Đường dẫn URL trong stub bị thiếu tiền tố `/api` (ví dụ viết nhầm thành `/sanpham` thay vì `/api/sanpham`).

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 6. Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)

Dưới đây là 3 kỹ thuật nâng cao mà lập trình viên frontend/client bắt buộc phải biết khi dùng Mock Server:

#### Kịch bản 1: Giả lập độ trễ mạng (Network Latency) để kiểm tra UI có bị đơ không
Trong Java Swing, nếu bạn gọi API đồng bộ trên luồng giao diện chính (Event Dispatch Thread - EDT), khi mạng lag ứng dụng sẽ bị treo cứng (Not Responding). 
Để kiểm tra xem code của bạn đã dùng luồng ngầm (`SwingWorker` hoặc `CompletableFuture`) chuẩn hay chưa, hãy thêm thuộc tính `"fixedDelayMilliseconds"` vào file stub `mock-server/mappings/sanpham-mock.json`:

```json
{
  "request": {
    "method": "GET",
    "url": "/api/sanpham"
  },
  "response": {
    "status": 200,
    "fixedDelayMilliseconds": 3000,
    "jsonBody": [ { "id": 1, "ten": "Sản phẩm test lag" } ]
  }
}
```

##### 🧠 Tư duy logic
WireMock sẽ cố tình hoãn lại 3 giây trước khi trả về. Nếu trong 3 giây đó bạn vẫn kéo rê được cửa sổ `JFrame` và con trỏ chuột không bị quay tròn, chứng tỏ code Client của bạn đạt chuẩn Senior!

---

#### Kịch bản 2: Giả lập lỗi Server sập (HTTP 500) hoặc Hết phiên đăng nhập (HTTP 401)
Kiểm tra xem khi server backend lỗi thì giao diện có hiển thị hộp thoại `JOptionPane.showMessageDialog` báo lỗi nhẹ nhàng cho nhân viên thu ngân hay bị văng ứng dụng ra màn hình Desktop:

```json
{
  "request": {
    "method": "POST",
    "url": "/api/donhang"
  },
  "response": {
    "status": 500,
    "headers": { "Content-Type": "application/json" },
    "jsonBody": { "error": "Lỗi kết nối cơ sở dữ liệu siêu thị" }
  }
}
```

---

#### Kịch bản 3: Chế độ ghi lại tự động từ Server thật (Proxy & Record Mode)
Khi backend thật đang chạy và bạn muốn lưu lại toàn bộ dữ liệu mẫu thật về máy để sau này đi cà phê không có mạng vẫn code được:

```powershell
java -jar mock-server\wiremock.jar --proxy-all="http://192.168.1.100:8080" --record-mappings --root-dir mock-server
```

##### 🔍 Giải phẫu chi tiết lệnh Terminal
* `--proxy-all`: Mọi request từ Client gửi đến WireMock sẽ được chuyển tiếp (forward) sang server backend thật.
* `--record-mappings`: WireMock tự động bắt các cặp Request - Response từ server thật và tự động ghi ra các file `.json` trong thư mục `mappings/`.

#### 📌 Bảng tra cứu kịch bản Stubbing WireMock (Cheat Sheet)

| Nhu cầu giả lập | Thuộc tính JSON trong Response |
| :--- | :--- |
| Giả lập độ trễ mạng (lag) | `"fixedDelayMilliseconds": 2500` |
| Giả lập mã lỗi server | `"status": 500` |
| Giả lập hết quyền truy cập | `"status": 403` |
| Thêm Header tùy chỉnh | `"headers": { "X-Auth-Token": "test-jwt-token" }` |

[⬆️ Quay lại Mục lục](#mục-lục)

