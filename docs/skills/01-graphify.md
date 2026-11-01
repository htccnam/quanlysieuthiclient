# HƯỚNG DẪN TRIỂN KHAI & SỬ DỤNG SKILL GRAPHIFY (KNOWLEDGE GRAPH FOR CODEBASE)

Đóng vai trò là một **Senior Tech Lead và Mentor đào tạo lập trình**, tài liệu này hướng dẫn chi tiết từ A đến Z cách cài đặt, cấu hình, vận hành và phân tích dự án `quanlysieuthiclient` bằng công cụ **Graphify**.

---

<a id="mục-lục"></a>
## 📑 Mục lục (Table of Contents)
1. [Sơ đồ luồng hoạt động tổng quan](#1-sơ-đồ-luồng-hoạt-động-tổng-quan)
2. [Giai đoạn 1: Thiết lập hạ tầng & Cài đặt môi trường (Setup)](#2-giai-đoạn-1-thiết-lập-hạ-tầng--cài-đặt-môi-trường-setup)
3. [Giai đoạn 2: Quét mã nguồn & Trích xuất Knowledge Graph](#3-giai-đoạn-2-quét-mã-nguồn--trích-xuất-knowledge-graph)
4. [Giai đoạn 3: Phân tích & Khai thác kết quả (Visual & Report)](#4-giai-đoạn-3-phân-tích--khai-thác-kết-quả-visual--report)
5. [Sổ tay lỗi kinh điển cần tránh (Lessons Learned)](#5-sổ-tay-lỗi-kinh-điển-cần-tránh-lessons-learned)
6. [Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)](#6-hướng-dẫn-các-cách-dùng-skill-trong-thực-tế-practical-usage-workflows)

---

### 1. Sơ đồ luồng hoạt động tổng quan

```text
[quanlysieuthiclient Source Code]
      │
      ▼ (Quét AST qua Tree-sitter)
[Graphify Engine CLI] 
      │
      ├───────────────────────┬────────────────────────┐
      ▼                       ▼                        ▼
[GRAPH_REPORT.md]        [graph.html]            [graph.json]
(Kiến trúc DTO-View)    (Đồ thị tương tác UI)   (Graph Data cho AI)
      │                       │                        │
      ▼                       ▼                        ▼
[AI Agent đọc nhanh]   [Lập trình viên xem]    [Truy vấn quan hệ class]
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 2. Giai đoạn 1: Thiết lập hạ tầng & Cài đặt môi trường (Setup)

#### 2.1. Cài đặt Python package `graphifyy`

Chạy lệnh cài đặt công cụ thông qua trình quản lý gói `pip` của Python:

```bash
pip install graphifyy
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `pip`: Trình quản lý gói tiêu chuẩn của ngôn ngữ lập trình Python (Package Installer for Python).
* `install`: Lệnh con (subcommand) chỉ định hành động tải về và cài đặt gói thư viện.
* `graphifyy`: Tên định danh chính xác của gói Graphify trên kho lưu trữ PyPI (Python Package Index). Có chữ `y` kép ở cuối để phân biệt với các gói graph cơ bản khác.

#### 🧠 Tư duy logic: Tại sao phải dùng Graphify?
Trong dự án `quanlysieuthiclient`, chúng ta có 11 cặp View, Controller, ApiClient và DTO. Khi một lập trình viên mới hoặc AI cần sửa tính năng (ví dụ `TaoDonController`), thay vì phải mở lần lượt từng file để xem nó gọi `DonHangApiClient` hay `KhachHangApiClient`, Graphify sẽ quét cây cú pháp trừu tượng (AST - Abstract Syntax Tree) và tạo bản đồ phụ thuộc tức thì.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Lệnh trên cài đặt vào môi trường Python toàn cục của hệ thống (Global Scripts), chuẩn bị sẵn lệnh `graphify` thực thi trong terminal:

```text
C:\Users\<User>\AppData\Local\Programs\Python\PythonXX\
└── 📁 Scripts/
    └── 📄 graphify.exe      <-- [MỚI TẠO chính xác từ lệnh pip install graphifyy] File thực thi CLI
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 3. Giai đoạn 2: Quét mã nguồn & Trích xuất Knowledge Graph

#### 3.1. Chạy lệnh phân tích dự án

Đứng tại thư mục gốc của dự án `quanlysieuthiclient` (`D:\github\quanlyduanphanmem\quanlysieuthiclient`), gõ lệnh:

```bash
graphify .
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `graphify`: Lệnh binary vừa được cài đặt ở Giai đoạn 1.
* `.`: Tham số đường dẫn đại diện cho thư mục hiện tại (Current Directory), báo cho công cụ biết cần phân tích toàn bộ cây thư mục từ gốc dự án trở vào.

#### 🧠 Tư duy logic: Tại sao không đọc file thủ công?
Parser của Graphify dùng engine Tree-sitter của ngôn ngữ Java để tìm kiếm chính xác các cấu trúc `import`, `class inheritance` (kế thừa), `method calls` (gọi hàm) thay vì chỉ tìm chuỗi ký tự bằng grep/findstr vốn hay bị lẫn comment hoặc trùng tên biến.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Ngay sau khi lệnh kết thúc, dự án xuất hiện thêm thư mục đầu ra `graphify-out/`:

```text
quanlysieuthiclient/
├── 📁 graphify-out/          <-- [MỚI TẠO chính xác từ lệnh graphify .] Chứa kết quả đồ thị tri thức
│   ├── 📄 GRAPH_REPORT.md    <-- [MỚI TẠO] Báo cáo tóm tắt kiến trúc dành cho Lập trình viên và AI
│   ├── 📄 graph.html         <-- [MỚI TẠO] Giao diện trực quan đồ thị mạng lưới để mở bằng trình duyệt
│   └── 📄 graph.json         <-- [MỚI TẠO] Dữ liệu cấu trúc Node & Edge cho các công cụ tích hợp
├── 📁 src/
└── 📄 pom.xml
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 4. Giai đoạn 3: Phân tích & Khai thác kết quả (Visual & Report)

#### 4.1. Mở xem giao diện tương tác trực quan

Mở file giao diện bằng lệnh:

```powershell
Start-Process "graphify-out\graph.html"
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `Start-Process`: Lệnh của PowerShell dùng để khởi chạy một tiến trình hoặc mở một file bằng phần mềm mặc định trong Windows.
* `"graphify-out\graph.html"`: Đường dẫn đến file HTML vừa sinh ra. Trình duyệt mặc định (Chrome/Edge) sẽ được mở lên.

#### 4.2. Bảng kiểm tra đánh giá chất lượng phân tích (Test Cases)

| STT | Thành phần kiểm tra | Kết quả mong đợi | Ý nghĩa đối với dự án |
| :--- | :--- | :--- | :--- |
| 1 | Node `TaoDonController` | Liên kết đến `TaoDonView`, `DonHangApiClient`, `ChiTietDon` | Xác nhận luồng tạo đơn hàng khép kín |
| 2 | Cụm `DTO` | Đứng độc lập hoặc được trỏ từ `APICLIENT` | Đảm bảo mô hình dữ liệu không phụ thuộc ngược vào View |
| 3 | God Nodes (Nút trung tâm) | `configLoader`, `manhinhchinh` hiển thị kích thước lớn | Nhận diện các class có mức độ ảnh hưởng rộng |

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 5. Sổ tay lỗi kinh điển cần tránh (Lessons Learned)

> [!WARNING]
> **Lỗi 1: `graphify: command not found` hoặc không nhận diện lệnh trên Windows**
> * **Nguyên nhân:** Thư mục `Scripts` của Python chưa được thêm vào biến môi trường `PATH`.
> * **Khắc phục:** Chạy bằng cú pháp module Python: `python -m graphify .`

> [!CAUTION]
> **Lỗi 2: Commit nhầm thư mục `graphify-out/` lên GitHub**
> * **Nguyên nhân:** File output đồ thị nặng và không cần thiết đẩy lên Git remote.
> * **Khắc phục:** Mở file `.gitignore` và thêm dòng:
>   ```text
>   graphify-out/
>   ```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 6. Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)

Để tận dụng tối đa sức mạnh của **Graphify** trong công việc hàng ngày của dự án `quanlysieuthiclient`, dưới đây là 3 kịch bản sử dụng cốt lõi:

#### Kịch bản 1: Phân tích mức độ ảnh hưởng (Impact Analysis) trước khi sửa một Class
Khi bạn chuẩn bị sửa đổi hoặc xóa một hàm trong `sanphamApiClient.java`, bạn cần biết chính xác những Controller hay View nào đang phụ thuộc vào nó:

```bash
graphify query "sanphamApiClient"
```

##### 🔍 Giải phẫu chi tiết lệnh Terminal
* `graphify`: Công cụ CLI.
* `query`: Lệnh con truy vấn đồ thị quan hệ đã được lập chỉ mục trong file `graph.json`.
* `"sanphamApiClient"`: Tên node/class cần tra cứu phụ thuộc ngược (inbound/outbound references).

##### 🧠 Tư duy logic
Thay vì dùng chức năng Find in Files của IDE dễ bị sót các lệnh gọi gián tiếp hoặc chuỗi ký tự phản chiếu, Graphify trả về chính xác danh sách các component gọi hàm này (`sanphamController`, `TaoDonController`, `nhanvienController`).

---

#### Kịch bản 2: Cập nhật lại đồ thị kiến trúc sau mỗi đợt Merge tính năng mới
Mỗi khi bạn vừa kéo code mới từ nhánh của đồng đội (ví dụ `git pull origin Hoag` hoặc `nam`), hãy chạy lệnh cập nhật gia tăng (Incremental Update):

```bash
graphify . --watch
```

##### 🔍 Giải phẫu chi tiết lệnh Terminal
* `--watch`: Cờ lệnh chạy chế độ quan sát ngầm (Daemon mode). Khi bạn tạo thêm bất kỳ file Java nào trong `src/main/java`, Graphify sẽ tự động vẽ thêm node mới vào `graph.html` mà không cần chạy lại toàn bộ từ đầu.

---

#### Kịch bản 3: Sử dụng làm tài liệu định hướng cho AI Assistant (Prompt Engineering)
Khi bạn làm việc với trợ lý AI (như Antigravity, Claude Code, Cursor), bạn chỉ cần nhắc AI tham chiếu trực tiếp vào file báo cáo kiến trúc để tiết kiệm 80% thời gian giải thích:

```text
Prompt mẫu gửi AI:
"Hãy đọc file graphify-out/GRAPH_REPORT.md để nắm bắt luồng dữ liệu của dự án. Sau đó, hãy thêm tính năng xuất hóa đơn PDF vào luồng kết thúc đơn hàng trong TaoDonController và TaoDonView."
```

#### 📌 Bảng tra cứu nhanh lệnh Graphify (Cheat Sheet)

| Mục đích công việc | Câu lệnh thực thi |
| :--- | :--- |
| Quét toàn bộ dự án lần đầu | `graphify .` |
| Mở xem giao diện tương tác mạng lưới | `Start-Process "graphify-out\graph.html"` |
| Tìm kiếm quan hệ của một Controller | `graphify query "TaoDonController"` |
| Bật chế độ tự động cập nhật khi gõ code | `graphify . --watch` |

[⬆️ Quay lại Mục lục](#mục-lục)

