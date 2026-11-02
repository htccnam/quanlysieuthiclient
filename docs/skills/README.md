# TỔNG HỢP TOÀN BỘ QUY TRÌNH & LỘ TRÌNH LÀM CHỦ 7 SKILL PHÁT TRIỂN DỰ ÁN QUẢN LÝ SIÊU THỊ CLIENT

Đóng vai trò là một **Senior Tech Lead và Mentor đào tạo lập trình**, tài liệu này tổng hợp toàn bộ lộ trình hướng dẫn chi tiết từ A đến Z để làm chủ bộ 7 công cụ và kỹ năng (skills) phát triển chuyên nghiệp áp dụng trực tiếp vào dự án **`quanlysieuthiclient`**.

---

<a id="mục-lục"></a>
## 📑 Menu Anchor Link (Mục lục - Table of Contents)
1. [Sơ đồ luồng dữ liệu tổng quan của 7 Skill trong dự án](#1-sơ-đồ-luồng-dữ-liệu-tổng-quan-của-7-skill-trong-dự-án)
2. [Giai đoạn 1: Thiết lập hạ tầng & Dự án (Setup)](#2-giai-đoạn-1-thiết-lập-hạ-tầng--dự-án-setup)
3. [Giai đoạn 2: Chi tiết 7 Skill theo 3 tầng kiến trúc (APICLIENT, CONTROLLER, VIEW)](#3-giai-đoạn-2-chi-tiết-7-skill-theo-3-tầng-kiến-trúc-apiclient-controller-view)
4. [Giai đoạn 3: Kiểm thử toàn diện & Bảng đánh giá Test Cases](#4-giai-đoạn-3-kiểm-thử-toàn-diện--bảng-đánh-giá-test-cases)
5. [Sổ tay lỗi kinh điển cần tránh (Lessons Learned)](#5-sổ-tay-lỗi-kinh-điển-cần-tránh-lessons-learned)
6. [Sổ tay quy trình làm việc hàng ngày kết hợp cả 7 Skill (Daily Developer Workflow)](#6-sổ-tay-quy-trình-làm-việc-hàng-ngày-kết-hợp-cả-7-skill-daily-developer-workflow)

---

### 1. Sơ đồ luồng dữ liệu tổng quan của bộ Skill trong dự án

```text
               ┌─────────────────────────────────────────────────────────┐
               │         DỰ ÁN JAVA DESKTOP: quanlysieuthiclient          │
               └─────────────────────────────────────────────────────────┘
                                            │
        ┌───────────────────────────────────┼───────────────────────────────────┐
        ▼                                   ▼                                   ▼
 [TẦNG KIẾN TRÚC & NGỮ CẢNH]       [TẦNG PHÁT TRIỂN & GIAO DIỆN]        [TẦNG KIỂM THỬ & BẢO MẬT]
 ├─ Skill 1: Graphify              ├─ Skill 3: FlatLaf                  ├─ Skill 4: Superpowers TDD
 │  (Đồ thị quan hệ class)         │  (Giao diện phẳng Desktop hiện đại)│  (Quy trình Test-Driven)
 └─ Skill 2: Repomix               └─ Skill 6: Checkstyle               ├─ Skill 5: WireMock API
    (Đóng gói context AI)             (Chuẩn hóa Google Code Style)     │  (Mock Server kiểm thử Client)
                                                                        └─ Skill 7: Semgrep Security
                                                                           (Quét lỗ hổng tĩnh SAST)
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 2. Giai đoạn 1: Thiết lập hạ tầng & Dự án (Setup)

#### 2.1. Cài đặt các công cụ CLI nền tảng trên hệ điều hành

Chạy lệnh cài đặt đồng thời các engine Python cần thiết:

```bash
pip install graphifyy semgrep
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `pip`: Trình quản lý gói chuẩn của Python.
* `install`: Lệnh con yêu cầu tải và cài đặt thư viện vào hệ điều hành.
* `graphifyy`: Thư viện lõi phân tích cú pháp AST và dựng đồ thị quan hệ mã nguồn (Knowledge Graph).
* `semgrep`: Engine phân tích mã nguồn bảo mật tĩnh (Static Application Security Testing).

#### 🧠 Tư duy logic: Tại sao cần chuẩn bị công cụ từ bước Setup?
Trước khi bắt tay vào sửa code hoặc viết thêm tính năng, người lập trình viên giỏi cần có sẵn bộ công cụ đo lường và bảo vệ xung quanh dự án để giảm thiểu rủi ro sinh lỗi hồi quy (regression bugs).

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Lệnh trên tạo ra các công cụ thực thi toàn cục trong hệ thống máy:

```text
C:\Users\<User>\AppData\Local\Programs\Python\PythonXX\Scripts\
├── 📄 graphify.exe          <-- [MỚI TẠO chính xác từ lệnh pip install graphifyy]
└── 📄 semgrep.exe           <-- [MỚI TẠO chính xác từ lệnh pip install semgrep]
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 3. Giai đoạn 2: Chi tiết 7 Skill theo 3 tầng kiến trúc (APICLIENT, CONTROLLER, VIEW)

Toàn bộ 7 kỹ năng đã được biên soạn thành các cẩm nang độc lập chi tiết trong thư mục `docs/skills/`:

| STT | Tên Skill | Tài liệu chi tiết | Tầng kiến trúc tác động | Giá trị mang lại |
| :---: | :--- | :--- | :--- | :--- |
| **01** | **Graphify** | [01-graphify.md](file:///D:/github/quanlyduanphanmem/quanlysieuthiclient/docs/skills/01-graphify.md) | Toàn bộ dự án | Quét AST, xuất đồ thị `graph.html` và báo cáo `GRAPH_REPORT.md` cho AI và lập trình viên |
| **02** | **Repomix** | [02-repomix.md](file:///D:/github/quanlyduanphanmem/quanlysieuthiclient/docs/skills/02-repomix.md) | Ngữ cảnh AI | Đóng gói toàn bộ code vào `repomix-output.xml`, tối ưu hóa số token nạp vào prompt |
| **03** | **FlatLaf Modern UI** | [03-flatlaf.md](file:///D:/github/quanlyduanphanmem/quanlysieuthiclient/docs/skills/03-flatlaf.md) | Tầng `VIEW` | Hiện đại hóa toàn bộ các JFrame, bo cong nút bấm, bảng JTable sắc nét trên màn hình 2K/4K |
| **04** | **Superpowers TDD** | [04-superpowers-tdd.md](file:///D:/github/quanlyduanphanmem/quanlysieuthiclient/docs/skills/04-superpowers-tdd.md) | Tầng `CONTROLLER` & Logic | Thiết lập JUnit 5 & Mockito, xây dựng thói quen viết test trước khi viết mã nghiệp vụ |
| **05** | **WireMock API Testing** | [05-wiremock-api.md](file:///D:/github/quanlyduanphanmem/quanlysieuthiclient/docs/skills/05-wiremock-api.md) | Tầng `APICLIENT` | Dựng máy chủ giả lập localhost:8080 để test màn hình Desktop mà không cần bật backend thật |
| **06** | **Java Checkstyle** | [06-checkstyle.md](file:///D:/github/quanlyduanphanmem/quanlysieuthiclient/docs/skills/06-checkstyle.md) | Chuẩn hóa Codebase | Quét quy tắc đặt tên file, class, biến theo chuẩn Google Java Style |
| **07** | **Semgrep Security** | [07-semgrep.md](file:///D:/github/quanlyduanphanmem/quanlysieuthiclient/docs/skills/07-semgrep.md) | An toàn thông tin | Quét mã độc, mật khẩu hardcoded, rò rỉ token và lỗ hổng giao tiếp HTTP |
| **08** | **Elephant-Goldfish (EGM)** | [08-elephant-goldfish-model.md](08-elephant-goldfish-model.md) | Toàn bộ dự án & Đặc tả | Phát triển dựa trên đặc tả (Spec-Driven Development), phân tách Elephant/Goldfish ngăn ngừa AI code slop |

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 4. Giai đoạn 3: Kiểm thử toàn diện & Bảng đánh giá Test Cases

#### 4.1. Bảng ma trận kiểm thử toàn diện quy trình áp dụng

| Mã Test | Hạng mục kiểm tra | Lệnh thực thi | Tiêu chí đạt (Acceptance Criteria) |
| :---: | :--- | :--- | :--- |
| **TC-01** | Trích xuất đồ thị Graphify | `graphify .` | Sinh ra thư mục `graphify-out/` chứa `graph.html` hiển thị đủ các node |
| **TC-02** | Đóng gói ngữ cảnh Repomix | `npx repomix --style xml` | Sinh ra `repomix-output.xml` không chứa rác `target/` |
| **TC-03** | Nâng cấp giao diện FlatLaf | `mvn compile` | Giao diện đăng nhập phẳng, nút bấm có hiệu ứng hover mượt mà |
| **TC-04** | Kiểm thử logic TDD | `mvn test` | 100% các Test Case trong `src/test/java` báo XANH (PASS) |
| **TC-05** | Máy chủ giả lập WireMock | `java -jar mock-server\wiremock.jar` | Client gọi API `GET /api/sanpham` nhận về JSON 200 OK |
| **TC-06** | Kiểm tra phong cách mã nguồn | `mvn checkstyle:checkstyle` | Báo cáo `target/site/checkstyle.html` được sinh ra |
| **TC-07** | Quét bảo mật tự động | `semgrep scan --config auto` | Không có lỗ hổng nghiêm trọng (Critical/High) |
| **TC-08** | Rà soát đặc tả EGM | `egm-review` / `egm-draft` | Bản thiết kế hoàn chỉnh trong `docs/designs/`, vượt qua 9 reviewer swarm |

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 5. Sổ tay lỗi kinh điển cần tránh (Lessons Learned)

> [!WARNING]
> **Lỗi 1: Áp dụng tất cả các công cụ cùng một lúc gây quá tải**
> * **Bài học:** Hãy làm theo lộ trình từng bước: Khởi đầu bằng việc trực quan hóa dự án với **Graphify** -> Làm đẹp giao diện với **FlatLaf** -> Đảm bảo chất lượng bằng **JUnit/WireMock**.

> [!CAUTION]
> **Lỗi 2: Xung đột phiên bản Java giữa Maven và công cụ ngoài**
> * **Bài học:** Dự án được cấu hình cho **Java 21** (`<maven.compiler.source>21</maven.compiler.source>`). Luôn đảm bảo lệnh `java -version` trên terminal trỏ đúng JDK 21 để tránh lỗi biên dịch bytecode không tương thích.

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 6. Sổ tay quy trình làm việc hàng ngày kết hợp cả 7 Skill (Daily Developer Workflow)

Dưới đây là một ngày làm việc mẫu của một Senior Fullstack/Client Developer khi phát triển dự án Quản lý Siêu thị:

```text
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                        QUY TRÌNH 5 BƯỚC LÀM VIỆC CHUẨN MỰC                             │
└────────────────────────────────────────────────────────────────────────────────────────┘

 [BƯỚC 1: ĐẦU NGÀY]
   git pull origin main
   graphify .                              <-- Xem nhanh sơ đồ và cập nhật Knowledge Graph

 [BƯỚC 2: PHÁT TRIỂN TÍNH NĂNG MỚI]
   java -jar mock-server/wiremock.jar      <-- Bật mock API nếu chưa có server thật
   mvn test -Dtest=...                     <-- Viết Unit Test trước (TDD)
   (Viết code Java trong APICLIENT, CONTROLLER, VIEW)

 [BƯỚC 3: LÀM ĐẸP GIAO DIỆN SWING]
   FlatLightLaf.setup()                    <-- Bo cong nút bấm, căn chỉnh bảng JTable

 [BƯỚC 4: RÀ SOÁT CHẤT LƯỢNG & BẢO MẬT]
   mvn checkstyle:check                    <-- Sửa lỗi thụt lề, tên biến theo Google Style
   semgrep scan --config auto              <-- Quét sạch mật khẩu, token lộ

 [BƯỚC 5: ĐẨY CODE VÀ CỘNG TÁC CÙNG AI]
   npx repomix --style xml                 <-- Nén code gửi AI để review lần cuối
   git add . && git commit -m "feat: ..."  <-- Đẩy mã nguồn sạch lên GitHub
```

#### 📌 Bảng ma trận tra cứu nhanh toàn bộ 7 Skill

| Tên Skill | Câu lệnh phổ biến nhất | Tác dụng tức thì |
| :--- | :--- | :--- |
| **01. Graphify** | `graphify .` | Xuất file đồ thị `graph.html` xem toàn bộ cấu trúc dự án |
| **02. Repomix** | `npx repomix --copy` | Đóng gói toàn bộ code và tự copy vào Clipboard |
| **03. FlatLaf** | `FlatLightLaf.setup();` | Biến giao diện Swing cũ thành giao diện phẳng hiện đại |
| **04. Superpowers TDD**| `mvn test` | Chạy bộ kiểm thử tự động, phát hiện bug logic tính tiền |
| **05. WireMock** | `java -jar mock-server\wiremock.jar` | Chạy Mock API giả lập server không cần kết nối mạng |
| **06. Checkstyle** | `mvn checkstyle:check` | Báo danh sách dòng code chưa chuẩn format Google |
| **07. Semgrep** | `semgrep scan --config "p/secrets"` | Quét tìm mật khẩu hoặc key nhạy cảm bị hardcode |

[⬆️ Quay lại Mục lục](#mục-lục)

