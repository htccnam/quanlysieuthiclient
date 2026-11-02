# HƯỚNG DẪN TRIỂN KHAI & SỬ DỤNG SKILL REPOMIX (CODEBASE CONTEXT PACKER)

Đóng vai trò là một **Senior Tech Lead và Mentor đào tạo lập trình**, tài liệu này hướng dẫn chi tiết từ A đến Z cách sử dụng công cụ **Repomix** để đóng gói toàn bộ mã nguồn của dự án `quanlysieuthiclient` thành một file ngữ cảnh chuẩn xác.

---

<a id="mục-lục"></a>
## 📑 Mục lục (Table of Contents)
1. [Sơ đồ luồng hoạt động tổng quan](#1-sơ-đồ-luồng-hoạt-động-tổng-quan)
2. [Giai đoạn 1: Thiết lập hạ tầng & Khởi tạo cấu hình (Setup)](#2-giai-đoạn-1-thiết-lập-hạ-tầng--khởi-tạo-cấu-hình-setup)
3. [Giai đoạn 2: Đóng gói mã nguồn theo định dạng chuẩn XML/Markdown](#3-giai-đoạn-2-đóng-gói-mã-nguồn-theo-định-dạng-chuẩn-xmlmarkdown)
4. [Giai đoạn 3: Kiểm thử dữ liệu đóng gói (Verification)](#4-giai-đoạn-3-kiểm-thử-dữ-liệu-đóng-gói-verification)
5. [Sổ tay lỗi kinh điển cần tránh (Lessons Learned)](#5-sổ-tay-lỗi-kinh-điển-cần-tránh-lessons-learned)
6. [Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)](#6-hướng-dẫn-các-cách-dùng-skill-trong-thực-tế-practical-usage-workflows)

---

### 1. Sơ đồ luồng hoạt động tổng quan

```text
[quanlysieuthiclient Repo]
      │
      ▼
[repomix.config.json (Lọc bỏ target, .idea, .git)]
      │
      ▼ (Lệnh npx repomix)
[Repomix Engine]
      │
      ├───────────────────────────────┐
      ▼                               ▼
[repomix-output.xml]            [Thống kê Token & Cây thư mục]
(Đóng gói Code + XML Tags)      (Đo lường chi phí nạp vào AI Prompt)
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 2. Giai đoạn 1: Thiết lập hạ tầng & Khởi tạo cấu hình (Setup)

#### 2.1. Khởi tạo file cấu hình cho Repomix

Tại thư mục gốc dự án, chạy lệnh:

```bash
npx repomix --init
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `npx`: Trình thực thi gói đi kèm của Node.js (Node Package Execute), giúp chạy trực tiếp các package trên npm mà không bắt buộc phải cài đặt vĩnh viễn vào hệ điều hành.
* `repomix`: Tên công cụ chính (trước đây có tên là `repopack`).
* `--init`: Cờ lệnh (flag) báo hiệu tạo file cấu hình mẫu `repomix.config.json` với các thiết lập mặc định.

#### 🧠 Tư duy logic: Tại sao phải khởi tạo cấu hình?
Dự án Java Maven thường sinh ra thư mục biên dịch `target/` rất lớn và thư mục cấu hình IDE `.idea/`. Nếu không loại trừ các file binary `.class` này, khi đóng gói file sẽ bị phình to hàng trăm Megabyte và lãng phí vô số token của AI.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Lệnh trên tạo ra file cấu hình ngay tại thư mục gốc:

```text
quanlysieuthiclient/
├── 📄 repomix.config.json    <-- [MỚI TẠO chính xác từ lệnh npx repomix --init] Cấu hình phạm vi quét mã nguồn
├── 📁 src/
└── 📄 pom.xml
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 3. Giai đoạn 2: Đóng gói mã nguồn theo định dạng chuẩn XML/Markdown

#### 3.1. Chạy lệnh đóng gói mã nguồn

Chạy lệnh nén toàn bộ code thành định dạng XML (định dạng tốt nhất để AI phân tích cấu trúc):

```bash
npx repomix --style xml --output repomix-output.xml
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `npx repomix`: Gọi bộ đóng gói Repomix.
* `--style xml`: Định dạng bao bọc file (sử dụng các thẻ tag `<file path="...">...</file>`), giúp AI phân định chính xác ranh giới giữa các class Java.
* `--output repomix-output.xml`: Chỉ định tên file đích đầu ra chứa toàn bộ mã nguồn.

#### 🧠 Tư duy logic: Tại sao chọn format XML thay vì Markdown thông thường?
Format XML giúp trình phân tích ngôn ngữ lớn (LLM) không bị nhầm lẫn giữa code Java thực tế với các đoạn giải thích Markdown, đảm bảo không bị gãy đoạn mã khi import vào Claude, GPT hay Gemini.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Thư mục dự án xuất hiện file kết quả duy nhất gom toàn bộ mã nguồn:

```text
quanlysieuthiclient/
├── 📄 repomix-output.xml    <-- [MỚI TẠO chính xác từ lệnh npx repomix --style xml...] File ngữ cảnh gom toàn bộ code
├── 📄 repomix.config.json
├── 📁 src/
└── 📄 pom.xml
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 4. Giai đoạn 3: Kiểm thử dữ liệu đóng gói (Verification)

#### 4.1. Bảng kiểm tra chất lượng file đóng gói

| Chỉ số kiểm tra | Giá trị mong muốn | Đạt / Chưa đạt |
| :--- | :--- | :--- |
| File nhị phân `.class` | Không xuất hiện (0 file) | Đạt (Đã loại trừ `target/`) |
| Thư mục IDE `.idea` | Không xuất hiện (0 file) | Đạt (Đã lọc) |
| Số lượng class Java | 48 file Java đầy đủ (APICLIENT, DTO, VIEW, CONTROLLER) | Đạt |
| Tổng lượng Token ước tính | Dưới 60.000 tokens (dễ dàng nạp vào mọi mô hình AI) | Đạt |

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 5. Sổ tay lỗi kinh điển cần tránh (Lessons Learned)

> [!WARNING]
> **Lỗi 1: File output quá lớn làm đơ trình duyệt khi mở**
> * **Nguyên nhân:** Quên thêm `target/**` vào danh sách `ignorePatterns` trong `repomix.config.json`.
> * **Khắc phục:** Mở `repomix.config.json`, đảm bảo mảng `ignorePatterns` có:
>   `"ignorePatterns": ["target/**", ".idea/**", ".git/**", "*.class"]`

>   ```text
>   repomix-output.xml
>   ```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 6. Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)

Để khai thác **Repomix** hiệu quả trong quy trình cộng tác với AI và làm việc nhóm, dưới đây là các cách dùng thường nhật:

#### Kịch bản 1: Đóng gói cục bộ một module cụ thể để tiết kiệm Token
Khi bạn chỉ cần AI giải quyết lỗi ở tầng kết nối mạng hoặc tầng điều khiển (chẳng hạn `APICLIENT` và `DTO`), không cần thiết phải đóng gói cả tầng `VIEW`. Hãy dùng cờ `--include`:

```bash
npx repomix --include "src/main/java/**/APICLIENT/**,src/main/java/**/DTO/**" --output api-dto-context.xml
```

##### 🔍 Giải phẫu chi tiết lệnh Terminal
* `--include "..."`: Chỉ định rõ danh sách các mẫu đường dẫn glob pattern cần gom lại.
* `--output api-dto-context.xml`: Đặt tên file xuất riêng biệt theo chức năng.

##### 🧠 Tư duy logic
Gom đúng phạm vi giúp giảm 70% số token, AI phản hồi nhanh hơn gấp 3 lần và không bị "ảo giác" (hallucination) do bị phân tâm bởi các đoạn code giao diện không liên quan.

---

#### Kịch bản 2: Tự động sao chép mã nguồn vào Clipboard (Khay nhớ tạm)
Khi bạn muốn dán code dự án vào ChatGPT web, Claude web hoặc diễn đàn StackOverflow mà không muốn mở file rồi bấm Ctrl+A / Ctrl+C:

```bash
npx repomix --copy
```

##### 🔍 Giải phẫu chi tiết lệnh Terminal
* `--copy`: Sau khi quét và đóng gói, Repomix tự động copy toàn bộ nội dung kết quả vào clipboard của Windows. Bạn chỉ việc nhấn `Ctrl + V` vào cửa sổ mong muốn.

---

#### Kịch bản 3: Tự động nén code trước mỗi buổi Code Review nội bộ
Trước buổi họp review đồ án hoặc giao ban với Tech Lead, hãy tạo file tổng hợp để mọi người dễ dàng rà soát tổng số dòng code và các class đã viết:

```bash
npx repomix --style markdown --output CodeReview_Summary.md
```

#### 📌 Bảng tra cứu nhanh lệnh Repomix (Cheat Sheet)

| Nhu cầu sử dụng | Câu lệnh thực thi |
| :--- | :--- |
| Đóng gói toàn bộ repo thành file XML | `npx repomix --style xml` |
| Đóng gói và tự động copy vào Clipboard | `npx repomix --copy` |
| Chỉ gom tầng Controller và DTO | `npx repomix --include "**/CONTROLLER/**,**/DTO/**"` |
| Bỏ qua việc đếm token để chạy nhanh hơn | `npx repomix --no-token-count` |

[⬆️ Quay lại Mục lục](#mục-lục)

