# HƯỚNG DẪN TRIỂN KHAI & QUÉT LỖ HỔNG BẢO MẬT VỚI SEMGREP SECURITY AUDIT

Đóng vai trò là một **Senior Tech Lead và Mentor đào tạo lập trình**, tài liệu này hướng dẫn chi tiết cách cài đặt và sử dụng công cụ phân tích tĩnh bảo mật **Semgrep** (Static Application Security Testing - SAST) để quét lỗ hổng rò rỉ mã bí mật (Hardcoded Secrets), lỗi tiêm mã và giao tiếp HTTP không an toàn trong dự án `quanlysieuthiclient`.

---

<a id="mục-lục"></a>
## 📑 Mục lục (Table of Contents)
1. [Sơ đồ luồng hoạt động tổng quan](#1-sơ-đồ-luồng-hoạt-động-tổng-quan)
2. [Giai đoạn 1: Thiết lập hạ tầng & Cài đặt Semgrep CLI (Setup)](#2-giai-đoạn-1-thiết-lập-hạ-tầng--cài-đặt-semgrep-cli-setup)
3. [Giai đoạn 2: Quét mã nguồn bằng Ruleset Bảo mật Java & OWASP (Execution)](#3-giai-đoạn-2-quét-mã-nguồn-bằng-ruleset-bảo-mật-java--owasp-execution)
4. [Giai đoạn 3: Phân tích kết quả & Kế hoạch vá lỗ hổng (Remediation)](#4-giai-đoạn-3-phân-tích-kết-quả--kế-hoạch-vá-lỗ-hổng-remediation)
5. [Sổ tay lỗi kinh điển cần tránh (Lessons Learned)](#5-sổ-tay-lỗi-kinh-điển-cần-tránh-lessons-learned)
6. [Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)](#6-hướng-dẫn-các-cách-dùng-skill-trong-thực-tế-practical-usage-workflows)

---

### 1. Sơ đồ luồng hoạt động tổng quan

```text
[Mã nguồn Java & Cấu hình config.properties]
                       │
                       ▼ (Lệnh semgrep scan)
        [Semgrep Engine (Tree-sitter SAST)]
                       │
         (Đối chiếu Ruleset OWASP Top 10)
                       │
       ┌───────────────┴───────────────┐
       ▼                               ▼
[Lỗ hổng nghiêm trọng]           [Cảnh báo phong cách lập trình]
(Hardcoded Tokens, HTTP Clear)  (Bỏ sót catch ngoại lệ rỗng)
       │                               │
       └───────────────┬───────────────┘
                       ▼
             [semgrep-report.json]
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 2. Giai đoạn 1: Thiết lập hạ tầng & Cài đặt Semgrep CLI (Setup)

#### 2.1. Cài đặt Semgrep qua `pip`

Chạy lệnh cài đặt công cụ phân tích tĩnh:

```bash
pip install semgrep
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `pip`: Trình quản lý gói Python.
* `install`: Yêu cầu cài đặt gói phần mềm.
* `semgrep`: Công cụ phân tích mã nguồn tĩnh đa ngôn ngữ nhanh chóng, hoạt động cục bộ (offline) trên máy tính.

#### 🧠 Tư duy logic: Tại sao cần quét bảo mật ngay tại tầng Client?
Nhiều lập trình viên lầm tưởng chỉ cần bảo mật server. Nhưng ở tầng Client Desktop:
1. Nếu hardcode tài khoản quản trị (admin/password) trong file `.java`, kẻ xấu dịch ngược (decompile bằng JD-GUI) sẽ lấy được ngay.
2. Nếu dùng HTTP không mã hóa thay vì HTTPS, thông tin đăng nhập gửi qua mạng nội bộ siêu thị sẽ bị nghe lén (Man-in-the-Middle).

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Công cụ Semgrep được cài vào danh bạ lệnh của hệ điều hành:

```text
C:\Users\<User>\AppData\Local\Programs\Python\PythonXX\
└── 📁 Scripts/
    └── 📄 semgrep.exe       <-- [MỚI TẠO chính xác từ lệnh pip install semgrep] Trình thực thi quét bảo mật
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 3. Giai đoạn 2: Quét mã nguồn bằng Ruleset Bảo mật Java & OWASP (Execution)

#### 3.1. Chạy lệnh quét lỗ hổng và xuất báo cáo

Tại thư mục gốc `quanlysieuthiclient`, gõ lệnh:

```bash
semgrep scan --config auto --json --output semgrep-report.json
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `semgrep scan`: Lệnh chính kích hoạt chế độ phân tích bảo mật.
* `--config auto`: Tự động nhận diện công nghệ của dự án (Java + Maven) để áp dụng các bộ luật OWASP Java phù hợp nhất.
* `--json`: Định dạng kết quả xuất ra dạng JSON có cấu trúc.
* `--output semgrep-report.json`: Đường dẫn ghi kết quả báo cáo.

#### 🧠 Tư duy logic: Tại sao dùng `--config auto`?
Semgrep tự động phát hiện mã Java và tải bộ luật cộng đồng đã kiểm chứng (như quy tắc kiểm tra SQL Injection, Hardcoded Secret, Insecure Deserialization với Gson) mà lập trình viên không phải tự viết luật thủ công.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
File báo cáo kết quả quét xuất hiện tại thư mục dự án:

```text
quanlysieuthiclient/
├── 📄 semgrep-report.json    <-- [MỚI TẠO chính xác từ lệnh semgrep scan...] Báo cáo kết quả quét bảo mật
├── 📁 src/
└── 📄 pom.xml
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 4. Giai đoạn 3: Phân tích kết quả & Kế hoạch vá lỗ hổng (Remediation)

#### 4.1. Bảng đánh giá các điểm rủi ro phổ biến trong dự án Client

| Vị trí | Lỗ hổng tiềm ẩn | Mức độ nghiêm trọng | Giải pháp khắc phục |
| :--- | :--- | :--- | :--- |
| `config.properties` | Sử dụng giao thức HTTP thô (`http://localhost:8080`) | Trung bình (Medium) | Chuyển sang HTTPS (`https://`) khi triển khai môi trường thực tế |
| `taikhoanApiClient.java` | Bắt ngoại lệ và nuốt lỗi (`catch (Exception e) {}` rỗng) | Thấp (Low) | Ghi log `e.printStackTrace()` hoặc hiển thị thông báo lỗi cho người dùng |
| `LoginView.java` | Lưu mật khẩu dưới dạng chuỗi `String` thay vì `char[]` | Thấp (Low) | Dùng `JPasswordField.getPassword()` trả về `char[]` để xóa khỏi bộ nhớ RAM ngay sau khi gửi |

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 5. Sổ tay lỗi kinh điển cần tránh (Lessons Learned)

> [!WARNING]
> **Lỗi 1: Semgrep quét cả thư mục `target/` làm treo máy hoặc báo lỗi trùng lặp**
> * **Nguyên nhân:** Không loại trừ thư mục build của Maven.
> * **Khắc phục:** Thêm cờ `--exclude target` khi chạy:
>   `semgrep scan --config auto --exclude target`

> [!CAUTION]
> **Lỗi 2: Commit file `semgrep-report.json` chứa thông tin nhạy cảm**
> * **Nguyên nhân:** File báo cáo có thể trích đoạn mã nguồn có chứa đường dẫn nhạy cảm.
> * **Khắc phục:** Thêm `semgrep-report.json` vào `.gitignore`.

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 6. Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)

Để đảm bảo phần mềm siêu thị không bị tấn công mạng hoặc lộ dữ liệu khách hàng, dưới đây là các cách sử dụng Semgrep định kỳ:

#### Kịch bản 1: Quét phát hiện mật khẩu và khóa bí mật bị ghi cứng (Secret Detection)
Khi lập trình viên test thử tính năng thường có thói quen gán cứng token hay password vào mã nguồn rồi quên xóa:

```bash
semgrep scan --config "p/secrets"
```

##### 🔍 Giải phẫu chi tiết lệnh Terminal
* `--config "p/secrets"`: Nạp bộ luật chuyên biệt của Semgrep Registry nhằm phát hiện các mẫu chuỗi nhạy cảm (Private Keys, API Keys, DB Passwords, JWT).

##### 🧠 Tư duy logic
Lệnh này quét toàn bộ các file `.java` và `.properties` chỉ trong vài giây. Nếu phát hiện dòng nào có dạng `String token = "eyJhbGciOi..."`, Semgrep sẽ chặn lại ngay.

---

#### Kịch bản 2: Quét nhanh chỉ các file vừa thay đổi trong nhánh Git (Delta Scan)
Thay vì quét lại cả 50 file mã nguồn tốn thời gian, bạn có thể chỉ yêu cầu Semgrep quét những thay đổi so với commit trước:

```bash
semgrep scan --config auto --baseline-commit HEAD~1
```

##### 🔍 Giải phẫu chi tiết lệnh Terminal
* `--baseline-commit HEAD~1`: Chỉ định điểm mốc so sánh là commit liền trước (HEAD~1). Semgrep sẽ chỉ cảnh báo các lỗ hổng mới phát sinh trong commit hiện tại.

---

#### Kịch bản 3: Tích hợp vào quy trình CI/CD GitHub Actions
Để tự động quét bảo mật mỗi khi có thành viên tạo Pull Request lên GitHub, tạo file `.github/workflows/security.yml`:

```yaml
name: Security Audit
on: [push, pull_request]
jobs:
  semgrep:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: returntocorp/semgrep-action@v1
        with:
          config: >-
            p/owasp-top-ten
            p/secrets
```

#### 📌 Bảng tra cứu nhanh lệnh Semgrep (Cheat Sheet)

| Nhu cầu quét bảo mật | Câu lệnh thực thi |
| :--- | :--- |
| Quét toàn diện theo chuẩn OWASP | `semgrep scan --config "p/owasp-top-ten"` |
| Quét tìm mật khẩu lộ | `semgrep scan --config "p/secrets"` |
| Bỏ qua thư mục target và idea | `semgrep scan --config auto --exclude target --exclude .idea` |
| Xuất kết quả dạng bảng tóm tắt | `semgrep scan --config auto --text` |

[⬆️ Quay lại Mục lục](#mục-lục)

