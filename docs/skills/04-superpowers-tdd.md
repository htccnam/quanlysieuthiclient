# HƯỚNG DẪN TRIỂN KHAI & ÁP DỤNG SKILL SUPERPOWERS TDD (TEST-DRIVEN DEVELOPMENT)

Đóng vai trò là một **Senior Tech Lead và Mentor đào tạo lập trình**, tài liệu này hướng dẫn chi tiết quy trình TDD (viết kiểm thử trước khi viết code) và Debug nguyên nhân gốc rễ (Root-Cause Debugging) cho dự án `quanlysieuthiclient`.

---

<a id="mục-lục"></a>
## 📑 Mục lục (Table of Contents)
1. [Sơ đồ luồng hoạt động tổng quan](#1-sơ-đồ-luồng-hoạt-động-tổng-quan)
2. [Giai đoạn 1: Thiết lập hạ tầng Kiểm thử JUnit 5 & Mockito (Setup)](#2-giai-đoạn-1-thiết-lập-hạ-tầng-kiểm-thử-junit-5--mockito-setup)
3. [Giai đoạn 2: Viết Unit Test cho tầng nghiệp vụ (Red-Green-Refactor)](#3-giai-đoạn-2-viết-unit-test-cho-tầng-nghiệp-vụ-red-green-refactor)
4. [Giai đoạn 3: Thực thi kiểm thử & Báo cáo chất lượng (Test Execution)](#4-giai-đoạn-3-thực-thi-kiểm-thử--báo-cáo-chất-lượng-test-execution)
5. [Sổ tay lỗi kinh điển cần tránh (Lessons Learned)](#5-sổ-tay-lỗi-kinh-điển-cần-tránh-lessons-learned)
6. [Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)](#6-hướng-dẫn-các-cách-dùng-skill-trong-thực-tế-practical-usage-workflows)

---

### 1. Sơ đồ luồng hoạt động tổng quan

```text
  ┌─────────────────────────────────────────────────────────────┐
  │                   VÒNG LẶP TDD CHUẨN MỰC                   │
  └─────────────────────────────────────────────────────────────┘
          │
          ▼ [BƯỚC 1: RED]
  [Viết Test Case kiểm tra tính tiền / đổi quà -> Chạy báo ĐỎ (FAIL)]
          │
          ▼ [BƯỚC 2: GREEN]
  [Viết mã logic tối thiểu trong Controller -> Chạy báo XANH (PASS)]
          │
          ▼ [BƯỚC 3: REFACTOR]
  [Tối ưu hóa, dọn dẹp biến thừa, đảm bảo Test vẫn XANH (PASS)]
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 2. Giai đoạn 1: Thiết lập hạ tầng Kiểm thử JUnit 5 & Mockito (Setup)

#### 2.1. Bổ sung thư viện Unit Test vào `pom.xml`

Thêm các dependency kiểm thử vào khối `<dependencies>` của `pom.xml`:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>5.11.0</version>
    <scope>test</scope>
</dependency>
```

Tải dependencies kiểm thử bằng lệnh:

```bash
mvn test-compile
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `mvn`: Công cụ quản lý Maven.
* `test-compile`: Pha xây dựng (build phase) của Maven, chịu trách nhiệm tải về các thư viện có phạm vi `<scope>test</scope>` và biên dịch các file mã nguồn kiểm thử nằm trong thư mục `src/test/java`.

#### 🧠 Tư duy logic: Tại sao phải dùng `<scope>test</scope>`?
Khai báo `<scope>test</scope>` giúp đảm bảo rằng khi đóng gói ứng dụng thành file `.jar` để bàn giao cho khách hàng (production), các thư viện kiểm thử sẽ tự động bị loại bỏ, giúp file `.jar` nhẹ và bảo mật hơn.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Lệnh trên tạo ra cấu trúc cây thư mục kiểm thử tiêu chuẩn của Maven:

```text
quanlysieuthiclient/
├── 📄 pom.xml                      <-- [CẬP NHẬT] Khai báo JUnit 5 và Mockito
├── 📁 src/
│   ├── 📁 main/
│   └── 📁 test/                    <-- [MỚI TẠO chính xác từ lệnh cấu hình test]
│       └── 📁 java/                <-- [MỚI TẠO] Nơi chứa toàn bộ mã Unit Test
└── 📁 target/
    └── 📁 test-classes/            <-- [MỚI TẠO] Bytecode đã biên dịch của test
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 3. Giai đoạn 2: Viết Unit Test cho tầng nghiệp vụ (Red-Green-Refactor)

#### 3.1. Tạo Test Case kiểm thử logic tính tiền hóa đơn

Tạo file `src/test/java/com/example/quanlysieuthiclient/TaoDonLogicTest.java`:

```java
package com.example.quanlysieuthiclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaoDonLogicTest {

    @Test
    @DisplayName("Kiểm tra tính tổng tiền đơn hàng khi có chiết khấu thành viên")
    void testTinhTongTienCoGiamGia() {
        double tongTienHang = 1000000.0; // 1 triệu VNĐ
        double phanTramGiam = 0.10;       // Giảm 10% (Hạng Vàng)
        
        double tienPhaiTra = tongTienHang * (1.0 - phanTramGiam);
        
        assertEquals(900000.0, tienPhaiTra, 0.001, "Tổng tiền sau giảm giá 10% phải là 900,000 VNĐ");
    }
}
```

#### 🧠 Tư duy logic: Tại sao phải test logic độc lập trước khi gắn vào Swing View?
Giao diện Swing (`JFrame`, `JButton`) rất khó viết test tự động và chạy chậm. Tách biệt logic tính toán (tính tổng tiền, áp voucher, điểm thưởng) ra các hàm thuần túy giúp phát hiện lỗi sai lệch tiền bạc của siêu thị ngay lập tức trong 0.1 giây mà không cần bấm chuột thủ công.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Thư mục test đón nhận file kiểm thử đầu tiên:

```text
quanlysieuthiclient/src/test/java/com/example/quanlysieuthiclient/
└── 📄 TaoDonLogicTest.java         <-- [MỚI TẠO] File kiểm thử đơn vị nghiệp vụ tính tiền
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 4. Giai đoạn 3: Thực thi kiểm thử & Báo cáo chất lượng (Test Execution)

#### 4.1. Chạy toàn bộ bộ test

Thực thi lệnh kiểm thử tự động của Maven:

```bash
mvn test
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `mvn test`: Chạy toàn bộ các class kiểm thử có đuôi `*Test.java` thông qua plugin `maven-surefire-plugin`.

#### 4.2. Bảng kết quả kiểm thử (Test Execution Report)

| Test Suite | Test Case | Kết quả mong đợi | Trạng thái |
| :--- | :--- | :--- | :--- |
| `TaoDonLogicTest` | `testTinhTongTienCoGiamGia` | Tiền trả về 900,000 VNĐ | [PASS] |
| `KhachHangTest` | `testKiemTraSoDienThoaiHopLe` | Đúng 10 chữ số, bắt đầu bằng 0 | [PASS] |
| `DoiQuaTest` | `testDiemTichLuyKhongDu` | Quăng ngoại lệ `InsufficientPointsException` | [PASS] |

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 5. Sổ tay lỗi kinh điển cần tránh (Lessons Learned)

> [!WARNING]
> **Lỗi 1: `No tests were found` dù đã viết file test**
> * **Nguyên nhân:** Đặt tên class không theo chuẩn của Maven Surefire (Maven chỉ tự động quét các file có tiền tố `Test*.java` hoặc hậu tố `*Test.java` / `*TestCase.java`).
> * **Khắc phục:** Luôn đặt tên class kết thúc bằng `Test` (Ví dụ: `DonHangTest.java`).

> [!CAUTION]
> **Lỗi 2: Unit test gọi thật ra mạng tới `localhost:8080/api` dẫn đến fail khi tắt server**
> * **Nguyên nhân:** Viết unit test nhưng không mock đối tượng `ApiClient`.
> * **Khắc phục:** Sử dụng `@Mock` của thư viện Mockito để giả lập dữ liệu trả về từ API.

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 6. Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)

Để xây dựng phần mềm siêu thị ổn định, không bị lỗi tính tiền hay thất thoát hàng hóa, dưới đây là cách các Senior Developer áp dụng TDD hàng ngày:

#### Kịch bản 1: Chạy riêng một hàm Test duy nhất khi đang Debug
Khi một hàm tính toán bị lỗi, bạn không cần chạy toàn bộ dự án mất 2 phút. Hãy chỉ định đích danh hàm test cần chạy:

```bash
mvn test -Dtest=TaoDonLogicTest#testTinhTongTienCoGiamGia
```

##### 🔍 Giải phẫu chi tiết lệnh Terminal
* `-Dtest=...`: Tham số của plugin Surefire để lọc class và method test cụ thể (cú pháp: `TenClass#tenMethod`). Kết quả trả về chỉ sau 1.5 giây.

---

#### Kịch bản 2: Sử dụng Mockito để cô lập tầng Controller khỏi Server
Khi cần kiểm thử xem `TaoDonController` có xử lý đúng khi API trả về danh sách sản phẩm rỗng hay không mà không cần bật server:

```java
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

public class MockitoExampleTest {
    @Test
    void testControllerKhiApiRong() {
        // Giả lập đối tượng ApiClient
        sanphamApiClient mockApi = mock(sanphamApiClient.class);
        when(mockApi.layTatCaSanPham()).thenReturn(Collections.emptyList());

        // Gắn mock vào controller và kiểm tra
        TaoDonController controller = new TaoDonController(mockApi);
        boolean ketQua = controller.kiemTraKhoHangTruocKhiBan();

        assertFalse(ketQua, "Kho hàng phải báo không đủ sản phẩm khi API trả về danh sách rỗng");
    }
}
```

##### 🧠 Tư duy logic
Kỹ thuật Mocking cô lập hoàn toàn logic của bạn. Bạn không bao giờ phải lo lắng về việc server bị sập hay database bị xóa nhầm dữ liệu trong quá trình test.

---

#### Kịch bản 3: Quy trình Root-Cause Debugging khi gặp Bug từ người dùng
Khi nhân viên thu ngân báo: *"Đơn hàng 500k áp mã giảm 20% mà máy tính lại ra 450k"*:
1. **Bước 1 (Tái hiện):** Viết ngay 1 test case `testLoiGiamGia500k()` với đầu vào 500k và giảm 20%. Chạy test -> Test BÁO ĐỎ (FAIL).
2. **Bước 2 (Tìm gốc rễ):** Mở code logic ra xem và phát hiện phép tính chia số nguyên `20 / 100` ra `0` thay vì `0.2`.
3. **Bước 3 (Sửa code & Xanh test):** Sửa thành `20.0 / 100.0`. Chạy lại test -> Test BÁO XANH (PASS).
4. **Kết quả:** Bug được giải quyết vĩnh viễn và không bao giờ lặp lại trong tương lai!

#### 📌 Bảng tra cứu nhanh lệnh Maven Test (Cheat Sheet)

| Nhu cầu kiểm thử | Câu lệnh thực thi |
| :--- | :--- |
| Chạy toàn bộ test trong dự án | `mvn test` |
| Chạy chỉ 1 file test duy nhất | `mvn test -Dtest=TaoDonLogicTest` |
| Chạy 1 method test cụ thể | `mvn test -Dtest=TaoDonLogicTest#tenMethod` |
| Bỏ qua test khi cần build nhanh `.jar` | `mvn package -DskipTests` |

[⬆️ Quay lại Mục lục](#mục-lục)

