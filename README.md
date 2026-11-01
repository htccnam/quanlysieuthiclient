# 🛒 HỆ THỐNG QUẢN LÝ SIÊU THỊ (SUPERMARKET MANAGEMENT CLIENT)

[![Java](https://img.shields.io/badge/Java-21-orange.svg?style=flat&logo=openjdk)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36.svg?style=flat&logo=apache-maven)](https://maven.apache.org/)
[![UI](https://img.shields.io/badge/GUI-Java%20Swing-blue.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> **Ứng dụng Desktop Java Swing** kết nối RESTful API, cung cấp giải pháp toàn diện cho hoạt động quản lý kho hàng, nhân sự, bán hàng thu ngân và chăm sóc khách hàng thân thiết tại các chuỗi siêu thị bán lẻ.

---

## 📑 Mục lục (Table of Contents)
- [1. Giới thiệu tổng quan](#1-giới-thiệu-tổng-quan)
- [2. Các tính năng cốt lõi](#2-các-tính-năng-cốt-lõi)
- [3. Kiến trúc dự án (Architecture)](#3-kiến-trúc-dự-án-architecture)
- [4. Yêu cầu hệ thống](#4-yêu-cầu-hệ-thống)
- [5. Hướng dẫn cài đặt & Khởi chạy](#5-hướng-dẫn-cài-đặt--khởi-chạy)
- [6. Cấu hình kết nối API Backend](#6-cấu-hình-kết-nối-api-backend)
- [7. Bộ công cụ & Kỹ năng phát triển (Skills)](#7-bộ-công-cụ--kỹ-năng-phát-triển-skills)
- [8. Giấy phép bản quyền (License)](#8-giấy-phép-bản-quyền-license)

---

## 1. Giới thiệu tổng quan

`quanlysieuthiclient` là ứng dụng Client giao diện đồ họa (Desktop GUI) được phát triển trên nền tảng **Java 21** và thư viện **Swing**. Ứng dụng hoạt động theo mô hình Client-Server hiện đại:
* **Giao diện Client:** Chịu trách nhiệm tương tác người dùng, kiểm tra tính hợp lệ dữ liệu form và trực quan hóa bảng biểu.
* **Tầng Mạng (API Client):** Giao tiếp không đồng bộ với Backend Spring Boot qua chuẩn HTTP RESTful API (JSON Payload thông qua thư viện Google Gson).

---

## 2. Các tính năng cốt lõi

| Phân hệ nghiệp vụ | Các tính năng chi tiết | Màn hình tương ứng |
| :--- | :--- | :--- |
| **Bán hàng & Thu ngân** | Tạo đơn hàng mới, tra cứu mã vạch/sản phẩm, tính tổng tiền, chiết khấu hạng thành viên và in hóa đơn | `TaoDonView.java`, `ChiTietView.java` |
| **Quản lý Hàng hóa** | Quản lý danh mục sản phẩm, nhóm loại hàng, số lượng tồn kho, giá nhập và đơn giá bán | `sanphamView.java`, `loaihangView.java` |
| **Khách hàng & Tích điểm**| Quản lý thông tin khách hàng, phân hạng thành viên (Đồng, Bạc, Vàng, Kim Cương), tích lũy điểm thưởng | `KhachHangView.java`, `HangThanhVienView.java` |
| **Đổi quà tri ân** | Đổi điểm tích lũy lấy quà tặng, quản lý danh mục quà và tra cứu lịch sử đổi quà | `DoiQuaView.java`, `DialogLichSuDoiQua.java` |
| **Nhân sự & Phân quyền** | Quản lý hồ sơ nhân viên, phân công chức vụ, cấp phát tài khoản đăng nhập hệ thống | `nhanvienViews.java`, `chucvuView.java`, `LoginView.java` |
| **Nhà cung cấp & Nhập hàng** | Quản lý danh bạ đối tác cung ứng, hợp đồng phân phối hàng hóa | `nhacungcapView.java` |
| **Chương trình Khuyến mãi** | Thiết lập các chương trình giảm giá sự kiện, áp dụng voucher khuyến mãi theo đợt | `khuyenmaiView.java` |

---

## 3. Kiến trúc dự án (Architecture)

Dự án được tổ chức theo mô hình phân lớp rõ ràng (Layered / MVC Pattern):

```text
quanlysieuthiclient/
├── src/main/java/com/example/quanlysieuthiclient/
│   ├── APICLIENT/     <-- Lớp giao tiếp mạng, gửi HTTP Request (GET, POST, PUT, DELETE) tới API
│   ├── CONTROLLER/    <-- Lớp điều khiển xử lý sự kiện UI, logic trung gian và gọi ApiClient
│   ├── DTO/           <-- Data Transfer Object mô hình hóa thực thể (Sản phẩm, Hóa đơn, Khách hàng...)
│   ├── UTIL/          <-- Tiện ích chung (Đọc cấu hình động configLoader.java)
│   └── VIEW/          <-- Giao diện người dùng Java Swing (JFrame, JPanel, JDialog)
├── src/main/resources/
│   └── config.properties   <-- Cấu hình địa chỉ Base URL Backend
├── docs/skills/            <-- Cẩm nang bộ 7 Agent Skills & Hướng dẫn kỹ thuật
├── pom.xml                 <-- Cấu hình Maven & dependencies
└── LICENSE                 <-- Giấy phép mã nguồn mở MIT
```

---

## 4. Yêu cầu hệ thống

Trước khi cài đặt và chạy dự án, hãy đảm bảo máy tính đã cài đặt:
* **Java Development Kit (JDK):** Phiên bản **Java 21 LTS** trở lên.
  * Kiểm tra qua lệnh: `java -version`
* **Apache Maven:** Phiên bản **3.8.x** trở lên.
  * Kiểm tra qua lệnh: `mvn -v`
* **Môi trường Backend:** Máy chủ Spring Boot đang chạy tại cổng `http://localhost:8080/api` (hoặc sử dụng [WireMock Mock Server](docs/skills/05-wiremock-api.md)).

---

## 5. Hướng dẫn cài đặt & Khởi chạy

### Bước 1: Sao chép mã nguồn về máy
```bash
git clone https://github.com/htccnam/quanlysieuthiclient.git
cd quanlysieuthiclient
```

### Bước 2: Tải thư viện phụ thuộc bằng Maven
```bash
mvn clean install -DskipTests
```

### Bước 3: Khởi chạy ứng dụng
Chạy trực tiếp màn hình Đăng nhập thông qua plugin Maven Exec:
```bash
mvn compile exec:java -Dexec.mainClass="com.example.quanlysieuthiclient.VIEW.LoginView"
```
*(Hoặc mở dự án bằng **IntelliJ IDEA / Eclipse**, tìm đến file `LoginView.java` hoặc `manhinhchinh.java` và nhấn biểu tượng **Run ▶**).*

---

## 6. Cấu hình kết nối API Backend

Địa chỉ máy chủ API backend được khai báo tại file `src/main/resources/config.properties`:

```properties
api.baseurl=http://localhost:8080/api
```

> **Mẹo phát triển:** Nếu chưa có máy chủ backend thật, bạn có thể khởi chạy Mock Server độc lập trong thư mục `mock-server` theo hướng dẫn tại [05-wiremock-api.md](docs/skills/05-wiremock-api.md).

---

## 7. Bộ công cụ & Kỹ năng phát triển (Skills)

Dự án tích hợp đầy đủ bộ tài liệu hướng dẫn chuẩn mực dành cho Lập trình viên và AI Coding Agents trong thư mục [`docs/skills/`](docs/skills/):

* 📊 [**Graphify (grafiphy):**](docs/skills/01-graphify.md) Bản đồ tri thức kiến trúc dự án và sơ đồ mạng lưới quan hệ.
* 📦 [**Repomix:**](docs/skills/02-repomix.md) Đóng gói ngữ cảnh toàn bộ mã nguồn tối ưu cho AI Assistant.
* 🎨 [**FlatLaf:**](docs/skills/03-flatlaf.md) Hiện đại hóa giao diện Swing sang phong cách phẳng (Flat Light / Dark).
* 🧪 [**Superpowers TDD:**](docs/skills/04-superpowers-tdd.md) Quy trình kiểm thử tự động với JUnit 5 & Mockito.
* 🌐 [**WireMock:**](docs/skills/05-wiremock-api.md) Giả lập Mock API phục vụ kiểm thử offline.
* 📏 [**Checkstyle:**](docs/skills/06-checkstyle.md) Chuẩn hóa quy tắc viết code theo Google Java Style.
* 🛡️ [**Semgrep:**](docs/skills/07-semgrep.md) Quét phân tích tĩnh phát hiện lỗ hổng bảo mật và rò rỉ token.

👉 Xem tài liệu tổng hợp tại [**docs/skills/README.md**](docs/skills/README.md).

---

## 8. Giấy phép bản quyền (License)

Dự án này được phân phối dưới giấy phép **MIT License**. Bạn có toàn quyền sử dụng, sửa đổi, phân phối và tích hợp vào các dự án thương mại hoặc cá nhân.

Xem toàn văn giấy phép tại file [LICENSE](LICENSE).

```text
MIT License
Copyright (c) 2026 htccnam
```
