# HƯỚNG DẪN TRIỂN KHAI & QUẢN LÝ CHẤT LƯỢNG MÃ NGUỒN VỚI JAVA CHECKSTYLE

Đóng vai trò là một **Senior Tech Lead và Mentor đào tạo lập trình**, tài liệu này hướng dẫn chi tiết cách tích hợp công cụ phân tích tĩnh **Checkstyle** (chuẩn Google Java Style) vào dự án `quanlysieuthiclient` để chuẩn hóa quy tắc đặt tên biến, định dạng mã nguồn và phát hiện các lỗi lập trình tiềm ẩn.

---

<a id="mục-lục"></a>
## 📑 Mục lục (Table of Contents)
1. [Sơ đồ luồng hoạt động tổng quan](#1-sơ-đồ-luồng-hoạt-động-tổng-quan)
2. [Giai đoạn 1: Thiết lập hạ tầng & Tích hợp Plugin Maven Checkstyle (Setup)](#2-giai-đoạn-1-thiết-lập-hạ-tầng--tích-hợp-plugin-maven-checkstyle-setup)
3. [Giai đoạn 2: Cấu hình bộ luật quy chuẩn mã nguồn (Ruleset Configuration)](#3-giai-đoạn-2-cấu-hình-bộ-luật-quy-chuẩn-mã-nguồn-ruleset-configuration)
4. [Giai đoạn 3: Thực thi quét & Phân tích báo cáo vi phạm (Code Audit)](#4-giai-đoạn-3-thực-thi-quét--phân-tích-báo-cáo-vi-phạm-code-audit)
5. [Sổ tay lỗi kinh điển cần tránh (Lessons Learned)](#5-sổ-tay-lỗi-kinh-điển-cần-tránh-lessons-learned)
6. [Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)](#6-hướng-dẫn-các-cách-dùng-skill-trong-thực-tế-practical-usage-workflows)

---

### 1. Sơ đồ luồng hoạt động tổng quan

```text
[Mã nguồn Java (APICLIENT, DTO, CONTROLLER, VIEW)]
                        │
                        ▼ (Lệnh mvn checkstyle:check)
          [Maven Checkstyle Plugin 3.3.1]
                        │
                        ▼ (Đối chiếu với Google Java Style)
    ┌───────────────────┴───────────────────┐
    ▼                                       ▼
[Không vi phạm]                       [Phát hiện vi phạm]
(Build SUCCESS)                       (Liệt kê: Tên file, Dòng số, Lỗi cụ thể)
                                            │
                                            ▼
                               [target/checkstyle-result.xml]
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 2. Giai đoạn 1: Thiết lập hạ tầng & Tích hợp Plugin Maven Checkstyle (Setup)

#### 2.1. Bổ sung `maven-checkstyle-plugin` vào `pom.xml`

Thêm phần `<build>` và `<plugins>` vào file `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-checkstyle-plugin</artifactId>
            <version>3.3.1</version>
            <configuration>
                <configLocation>google_checks.xml</configLocation>
                <consoleOutput>true</consoleOutput>
                <failsOnError>false</failsOnError>
                <includeTestSourceDirectory>false</includeTestSourceDirectory>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Nạp plugin về máy bằng lệnh:

```bash
mvn checkstyle:help
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `mvn`: Trình quản lý Apache Maven.
* `checkstyle:help`: Yêu cầu Maven tải về plugin `maven-checkstyle-plugin` phiên bản 3.3.1 và in ra hướng dẫn trợ giúp của plugin.

#### 🧠 Tư duy logic: Tại sao phải đặt `failsOnError` là `false` trong giai đoạn đầu?
Dự án hiện tại có nhiều file đặt tên chưa theo chuẩn CamelCase (ví dụ: `sanphamApiClient`, `chucvuController` thay vì `SanphamApiClient`). Nếu đặt `failsOnError` là `true`, Maven sẽ dừng toàn bộ quá trình build ngay lập tức. Để `false` giúp ta xuất báo cáo phân tích trước rồi sửa dần dần.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Plugin được khai báo vào file cấu hình gốc:

```text
quanlysieuthiclient/
├── 📄 pom.xml                      <-- [CẬP NHẬT] Thêm plugin maven-checkstyle-plugin cấu hình google_checks
└── 📁 src/
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 3. Giai đoạn 2: Cấu hình bộ luật quy chuẩn mã nguồn (Ruleset Configuration)

#### 3.1. Ý nghĩa của bộ luật `google_checks.xml`

Bộ luật Google Java Style quy định:
1. **Quy tắc đặt tên (Naming Conventions):**
   * Tên Class / Interface: Phải viết hoa chữ cái đầu (PascalCase), ví dụ: `NhanVienController.java` thay vì `nhanvienController.java`.
   * Tên Biến / Method: Viết thường chữ cái đầu (camelCase), ví dụ: `danhSachDonHang`.
   * Tên Package: Toàn bộ chữ thường không dấu gạch ngang (lowercase), ví dụ: `com.example.quanlysieuthiclient.controller` thay vì chữ HOA `CONTROLLER`.
2. **Quy tắc dòng mã:** Độ dài dòng không vượt quá 120 ký tự, thụt lề 2 hoặc 4 dấu cách (spaces), không dùng ký tự Tab cứng.

#### 🧠 Tư duy logic: Tại sao dự án cần chuẩn hóa đặt tên?
Khi làm việc nhóm nhiều thành viên, mỗi người đặt tên một kiểu (ví dụ bạn Nam đặt `CONTROLLER`, bạn Hoàng đặt `VIEW`, bạn Khang đặt chữ thường `dto`) làm mã nguồn thiếu tính nhất quán, khó bảo trì và dễ sinh lỗi khi chuyển giao dự án.

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 4. Giai đoạn 3: Thực thi quét & Phân tích báo cáo vi phạm (Code Audit)

#### 4.1. Chạy lệnh kiểm tra mã nguồn

Thực hiện lệnh quét toàn diện dự án:

```bash
mvn checkstyle:checkstyle
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `mvn checkstyle:checkstyle`: Quét toàn bộ mã nguồn theo ruleset và sinh ra báo cáo HTML trực quan bên trong thư mục `target/site/`.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Thư mục báo cáo tự động được sinh ra trong `target/`:

```text
quanlysieuthiclient/
└── 📁 target/
    ├── 📄 checkstyle-result.xml   <-- [MỚI TẠO chính xác từ lệnh mvn checkstyle:checkstyle] Dữ liệu vi phạm
    └── 📁 site/
        └── 📄 checkstyle.html     <-- [MỚI TẠO] Trang web báo cáo chi tiết từng dòng mã vi phạm
```

#### 4.2. Bảng phân loại các lỗi thường gặp trong dự án

| Vị trí | Lỗi Checkstyle | Hướng khắc phục chuẩn |
| :--- | :--- | :--- |
| `com.example...CONTROLLER` | Package name must be lowercase | Đổi tên package thành `com.example.quanlysieuthiclient.controller` |
| `nhanvienViews.java` | Class name should start with uppercase | Đổi tên file thành `NhanVienViews.java` |
| `import java.util.*;` | Avoid wildcard imports | Khai báo rõ ràng từng class cần import (ví dụ `java.util.List`) |

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 5. Sổ tay lỗi kinh điển cần tránh (Lessons Learned)

> [!WARNING]
> **Lỗi 1: `Unable to find configuration file at location: google_checks.xml`**
> * **Nguyên nhân:** Phiên bản plugin cũ không tìm thấy file cấu hình tích hợp sẵn.
> * **Khắc phục:** Sử dụng phiên bản plugin mới nhất `3.3.1` hoặc chỉ định `sun_checks.xml` (chuẩn của Sun Microsystems).

> [!CAUTION]
> **Lỗi 2: Đổi tên file Java trên Windows nhưng Git không nhận diện chữ HOA/thường**
> * **Nguyên nhân:** Hệ thống tệp NTFS của Windows mặc định không phân biệt chữ hoa chữ thường (Case-insensitive).
> * **Khắc phục:** Dùng lệnh: `git mv -f nhanvien.java NhanVien.java` để ép Git ghi nhận việc đổi tên.

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 6. Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)

Dưới đây là các kỹ thuật thực chiến giúp cả nhóm giữ mã nguồn luôn sạch đẹp, chuẩn chỉ như dự án của các công ty công nghệ lớn:

#### Kịch bản 1: Kiểm tra nhanh vi phạm trước khi tạo Pull Request
Trước khi bạn gõ `git push origin your-branch`, hãy chạy lệnh kiểm tra nhanh để đảm bảo không bị Tech Lead từ chối code:

```bash
mvn checkstyle:check
```

##### 🔍 Giải phẫu chi tiết lệnh Terminal
* `checkstyle:check`: Khác với mục tiêu `checkstyle:checkstyle` (chỉ xuất báo cáo), mục tiêu `check` sẽ trực tiếp in danh sách dòng lỗi lên màn hình Console để bạn bấm vào sửa ngay.

---

#### Kịch bản 2: Tự động format code trong IntelliJ IDEA chuẩn Google Style
Thay vì phải dùng phím cách (Space) căn chỉnh thủ công:
1. Vào **Settings / Preferences** trong IntelliJ (Ctrl + Alt + S).
2. Tìm đến mục **Plugins** -> Tìm và cài plugin **Checkstyle-IDEA**.
3. Vào **Tools** -> **Checkstyle** -> Chọn quy tắc **Google Checks**.
4. Mỗi khi viết xong một class (ví dụ `DonHangApiClient.java`), chỉ cần nhấn tổ hợp phím:
   ```text
   Ctrl + Alt + L  (Reformat Code)
   Ctrl + Alt + O  (Optimize Imports - Tự dọn sạch các import thừa)
   ```

##### 🧠 Tư duy logic
Tự động hóa định dạng bằng phím tắt giúp lập trình viên tập trung 100% trí tuệ vào logic nghiệp vụ siêu thị thay vì tốn thời gian căn chỉnh thụt lề hay tranh cãi về dấu mở ngoặc nhọn `{`.

---

#### Kịch bản 3: Tự động chặn Commit code lỗi bằng Git Pre-Commit Hook
Để ngăn chặn thành viên trong nhóm lỡ tay commit code viết ẩu vào nhánh chung, hãy tạo file `.git/hooks/pre-commit` với nội dung:

```bash
#!/bin/sh
echo "Đang kiểm tra chất lượng mã nguồn với Checkstyle..."
mvn checkstyle:check
if [ $? -ne 0 ]; then
    echo "Checkstyle phát hiện lỗi vi phạm quy chuẩn! Vui lòng sửa lỗi trước khi commit."
    exit 1
fi
```

#### 📌 Bảng tra cứu nhanh lệnh Checkstyle (Cheat Sheet)

| Nhu cầu kiểm tra | Câu lệnh thực thi |
| :--- | :--- |
| In trực tiếp dòng lỗi ra Console | `mvn checkstyle:check` |
| Xuất trang web báo cáo chi tiết | `mvn checkstyle:checkstyle` |
| Mở xem báo cáo HTML trên Windows | `Start-Process target\site\checkstyle.html` |

[⬆️ Quay lại Mục lục](#mục-lục)

