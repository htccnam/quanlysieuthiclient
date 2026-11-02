# HƯỚNG DẪN TRIỂN KHAI & SỬ DỤNG ELEPHANT-GOLDFISH MODEL (EGM) - SPEC-DRIVEN DEVELOPMENT

Đóng vai trò là một **Senior Tech Lead và Mentor đào tạo lập trình**, tài liệu này hướng dẫn chi tiết từ A đến Z cách ứng dụng mô hình **Elephant-Goldfish Model (EGM)** và bộ công cụ plugin Google EGM vừa được tích hợp vào dự án `quanlysieuthiclient` nhằm hiện thực hóa phương pháp phát triển phần mềm dựa trên đặc tả kỹ thuật (**Spec-Driven Development - SDD**), ngăn chặn hoàn toàn lỗi sinh mã vội vã ("AI code slop").

---

<a id="mục-lục"></a>
## 📑 Mục lục (Table of Contents)
1. [Sơ đồ luồng hoạt động tổng quan](#1-sơ-đồ-luồng-hoạt-động-tổng-quan)
2. [Giai đoạn 1: Thiết lập hạ tầng & Cấu trúc Plugin EGM trong dự án (Setup)](#2-giai-đoạn-1-thiết-lập-hạ-tầng--cấu-trúc-plugin-egm-trong-dự-án-setup)
3. [Giai đoạn 2: Quy trình 5 bước phát triển tính năng chuẩn EGM (Execution)](#3-giai-đoạn-2-quy-trình-5-bước-phát-triển-tính-năng-chuẩn-egm-execution)
4. [Giai đoạn 3: Chi tiết bộ 7 Skills và 9 Swarm Reviewer Agents](#4-giai-đoạn-3-chi-tiết-bộ-7-skills-và-9-swarm-reviewer-agents)
5. [Sổ tay lỗi kinh điển cần tránh (Lessons Learned)](#5-sổ-tay-lỗi-kinh-điển-cần-tránh-lessons-learned)
6. [Hướng dẫn kịch bản áp dụng thực tế vào quanlysieuthiclient (Practical Workflow)](#6-hướng-dẫn-kịch-bản-áp-dụng-thực-tế-vào-quanlysieuthiclient-practical-workflow)

---

### 1. Sơ đồ luồng hoạt động tổng quan

```text
               ┌─────────────────────────────────────────────────────────┐
               │    BẮT ĐẦU YÊU CẦU MỚI (TÍNH NĂNG / REFACTOR PHỨC TẠP)    │
               └─────────────────────────────────────────────────────────┘
                                            │
                                            ▼
                           [PHIÊN ELEPHANT - TRÍ NHỚ DÀI HẠN]
                     ┌──────────────────────────────────────────────┐
                     │ • Nắm toàn bộ bối cảnh dự án & nghiệp vụ     │
                     │ • Brainstorming phương án (TUYỆT ĐỐI NO-CODE)│
                     │ • Soạn thảo bản đặc tả docs/designs/<doc>.md │
                     └──────────────────────────────────────────────┘
                                            │
                                            ▼
                           [PHIÊN GOLDFISH - BỐI CẢNH ĐỘC LẬP]
                     ┌──────────────────────────────────────────────┐
                     │ • Subagents KHÔNG BIẾT thảo luận trước đó    │
                     │ • Đọc nguội (Cold Read) bản đặc tả           │
                     │ • 9 Persona Swarm phản biện & stress-test    │
                     └──────────────────────────────────────────────┘
                                            │
                                            ▼
                           [TỔNG HỢP & HOÀN THIỆN ĐẶC TẢ]
                     ┌──────────────────────────────────────────────┐
                     │ • egm-resolve: Khắc phục điểm mờ, phản biện  │
                     │ • Bản thiết kế đạt trạng thái "Ready-to-Code"│
                     └──────────────────────────────────────────────┘
                                            │
                                            ▼
                           [THỰC THI MÃ NGUỒN & KIỂM TRA LỆCH]
                     ┌──────────────────────────────────────────────┐
                     │ • Lập trình viên & AI code theo đúng đặc tả  │
                     │ • egm-drift-report: Quét lệch pha code/design│
                     └──────────────────────────────────────────────┘
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 2. Giai đoạn 1: Thiết lập hạ tầng & Cấu trúc Plugin EGM trong dự án (Setup)

#### 2.1. Nguồn gốc & Bản quyền
Plugin EGM bắt nguồn từ mã nguồn mở chính thức của Google:
* **Repository gốc:** `https://github.com/google/elephant-goldfish-model.git`
* **Trang chủ tài liệu:** `https://egm.dev`

#### 2.2. Vị trí cài đặt hai tầng
Để đảm bảo môi trường IDE nhận diện được công cụ vừa giúp dự án lưu trữ toàn bộ tài nguyên một cách độc lập:
1. **Tầng Hệ thống (Antigravity Global Plugin):** Nằm tại `~/.gemini/config/plugins/egm/` để nạp trực tiếp vào ngữ cảnh của AI Assistant khi khởi động.
2. **Tầng Dự án (Project Workspace Resource):** Đã được sao chép nguyên vẹn vào `../../.gemini/skills/egm` để bất kỳ lập trình viên nào clone dự án cũng có thể đọc, tra cứu và sử dụng.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
```text
quanlysieuthiclient/
└── docs/skills/
    ├── 01-graphify.md
    ├── 02-repomix.md
    ├── 03-flatlaf.md
    ├── 04-superpowers-tdd.md
    ├── 05-wiremock-api.md
    ├── 06-checkstyle.md
    ├── 07-semgrep.md
    ├── 08-elephant-goldfish-model.md  <-- [MỚI TẠO] Cẩm nang hướng dẫn EGM này
    └── egm/                           <-- [MỚI CHUYỂN VÀO] Trọn bộ tài nguyên Plugin EGM
        ├── 📄 plugin.json             <-- Khai báo plugin metadata chuẩn Antigravity
        ├── 📄 AGENTS.md               <-- Quy tắc ràng buộc cốt lõi No-Code & phân tách vai trò
        ├── 📁 assets/                 <-- Logo và icon biểu tượng EGM
        ├── 📁 agents/                 <-- 9 định nghĩa Subagent Goldfish độc lập
        └── 📁 skills/                 <-- 7 gói kỹ năng quy trình EGM độc lập
            ├── egm-bootstrap/
            ├── egm-brainstorm/
            ├── egm-draft/
            ├── egm-drift-report/
            ├── egm-resolve/
            ├── egm-review/
            └── egm-review-loop/
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 3. Giai đoạn 2: Quy trình 5 bước phát triển tính năng chuẩn EGM (Execution)

Quy trình EGM chia tách nghiêm ngặt giữa khâu **Tư duy thiết kế** và **Viết mã nguồn**:

```text
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                      QUY TRÌNH 5 GIAI ĐOẠN PHÁT TRIỂN SPEC-DRIVEN                      │
└────────────────────────────────────────────────────────────────────────────────────────┘

 [BƯỚC 1: KHỞI TẠO - BOOTSTRAP]
   Kích hoạt: skill `egm-bootstrap`
   Mục đích: Thiết lập bộ khung tài liệu thiết kế trong `docs/designs/`.

 [BƯỚC 2: KHẢO SÁT & ĐỘT PHÁ - BRAINSTORM]
   Kích hoạt: skill `egm-brainstorm`
   Quy tắc thép: TUYỆT ĐỐI KHÔNG VIẾT CODE TRONG BƯỚC NÀY.
   Mục đích: Khảo sát trade-offs, phân tích rủi ro, lựa chọn kiến trúc tối ưu.

 [BƯỚC 3: SOẠN THẢO BẢN ĐẶC TẢ - DRAFT]
   Kích hoạt: skill `egm-draft`
   Mục đích: Chuyển hóa toàn bộ kết quả brainstorm thành văn bản Markdown hoàn chỉnh
   theo template gồm: Mục tiêu (Goals), Phi mục tiêu (Non-goals), Chi tiết kỹ thuật,
   Test cases và Kế hoạch di trú (Migration plan).

 [BƯỚC 4: RÀ SOÁT ĐỘC LẬP TỔ ONG - REVIEW SWARM]
   Kích hoạt: skill `egm-review` hoặc `egm-review-loop`
   Mục đích: Khởi chạy 9 subagent "Goldfish" đọc bản đặc tả với tâm thế người ngoài cuộc,
   phát hiện các lỗ hổng logic, biệt ngữ chưa định nghĩa và thiếu sót kỹ thuật.
   Sau đó dùng `egm-resolve` để vá lại bản đặc tả.

 [BƯỚC 5: LẬP TRÌNH & KIỂM TRA LỆCH PHA - IMPLEMENT & DRIFT CHECK]
   Sau khi bản thiết kế được duyệt:
   Tiến hành lập trình (kết hợp Superpowers TDD).
   Chạy `egm-drift-report` để đảm bảo code sinh ra khớp 100% với bản đặc tả.
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 4. Giai đoạn 3: Chi tiết bộ 7 Skills và 9 Swarm Reviewer Agents

#### 4.1. Bộ 7 Skills quy trình trong `../../.gemini/skills/egm`

| Tên Skill | Mục đích chính | Khi nào kích hoạt? |
| :--- | :--- | :--- |
| **`egm-bootstrap`** | Khởi tạo thư mục và cấu trúc template đặc tả thiết kế | Bắt đầu khởi sự dự án hoặc thêm module lớn |
| **`egm-brainstorm`** | Thảo luận kiến trúc, đào sâu vấn đề, cấm viết code | Khi nhận yêu cầu mới mơ hồ hoặc phức tạp |
| **`egm-draft`** | Chấp bút tài liệu thiết kế Markdown `docs/designs/*.md` | Sau khi thống nhất hướng giải quyết |
| **`egm-review`** | Điều phối reviewer đọc nguội và ghi nhận phản biện | Khi bản đặc tả vừa được soạn xong |
| **`egm-review-loop`** | Vòng lặp tự động Review -> Resolve -> Re-review | Rà soát liên tục cho đến khi 0 finding |
| **`egm-resolve`** | Phân tích từng phản biện, quyết định đồng ý/bác bỏ và sửa doc | Sau khi có báo cáo từ đợt Review |
| **`egm-drift-report`** | So sánh AST code thực tế với đặc tả để tìm điểm lệch | Trước khi mở Pull Request / Release |

#### 4.2. Bộ 9 Persona Subagents (Goldfish Reviewers) trong `../../.gemini/skills/egm`

Khi chạy `egm-review`, hệ thống triệu hồi các subagent độc lập đại diện cho các góc nhìn khắt khe:

1. **`egm-design` (Hostile Design Critic):** Nhà phê bình kiến trúc khắt khe, lùng sục rủi ro mở rộng, hiệu năng mạng và chi phí vận hành.
2. **`egm-implementation-readiness` (Pragmatic Tech Lead):** Đảm bảo tài liệu rõ ràng đến mức lập trình viên có thể code ngay mà không phải đoán mò bất kỳ quyết định nào.
3. **`egm-comprehension` (Naive Reader):** Người đọc mới tinh, bắt lỗi các từ viết tắt không giải thích, biệt ngữ nội bộ hoặc giả định ngầm.
4. **`egm-codebase-details` (Pedantic Quality Auditor):** Soi kỹ từng dòng tên class, method xem có tái sử dụng đúng thư viện có sẵn trong dự án hay không.
5. **`egm-structural` (Document Architect):** Kiểm tra bố cục, thứ bậc tiêu đề H1-H4, định dạng bảng biểu và độ cân xứng.
6. **`egm-style` (Copy Editor):** Gọt giũa câu từ, xóa bỏ từ ngữ lan man sáo rỗng, đưa về giọng văn kỹ thuật khách quan, gãy gọn.
7. **`egm-zoom-out` (Skeptical Architect):** Nhìn từ độ cao 10.000m, đặt câu hỏi YAGNI ("Có thực sự cần tính năng này không? Có cách nào đơn giản hơn gấp 10 lần không?").
8. **`egm-synthesizer` (Swarm Compiler):** Trình tổng hợp trung gian, lọc trùng và nhóm các ý kiến phản biện thành một bảng báo cáo duy nhất.
9. **`egm-bootstrap-orchestrator`:** Điều phối viên tự động sinh các README và cấu trúc tài liệu đồng nhất.

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 5. Sổ tay lỗi kinh điển cần tránh (Lessons Learned)

> [!CAUTION]
> **Lỗi 1: Viết code ngay trong phiên Brainstorming**
> * **Hậu quả:** AI và lập trình viên sẽ bị cuốn vào việc gõ code và gỡ lỗi vặt, đánh mất tầm nhìn tổng thể và bỏ qua các rủi ro kiến trúc lớn.
> * **Bài học:** Tuân thủ triệt để nguyên tắc **No-Code Rule** của EGM. Khi ở phiên Elephant Brainstorm, chỉ bàn về logic, hợp đồng API, cấu trúc DTO, không gõ bất kỳ dòng code `.java` nào.

> [!WARNING]
> **Lỗi 2: Dùng chính AI vừa brainstorm để tự review**
> * **Hậu quả:** AI tự review bản thiết kế của chính mình sẽ mắc "điểm mù xác nhận" (confirmation bias) - những gì nó giả định ngầm trong đầu nó đều cho là hiển nhiên đúng.
> * **Bài học:** Bắt buộc phải spawn một **Goldfish Subagent** hoàn toàn mới (fresh context) để đọc nguội bản thiết kế. Nếu subagent mới không hiểu một câu trong doc, điều đó chứng minh bản doc chưa đủ rõ ràng.

> [!TIP]
> **Lỗi 3: Bỏ qua kiểm tra lệch pha (Drift Check) trước khi commit**
> * **Hậu quả:** Sau một tuần code, mã nguồn thực tế đã rẽ sang hướng khác nhưng bản thiết kế vẫn giữ nguyên mẫu cũ, biến tài liệu thành "xác sống" vô dụng.
> * **Bài học:** Chạy `egm-drift-report` trước mỗi đợt hợp nhất nhánh (Merge PR) để cập nhật ngược lại thiết kế hoặc sửa code cho đúng thỏa thuận ban đầu.

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 6. Hướng dẫn kịch bản áp dụng thực tế vào quanlysieuthiclient (Practical Workflow)

#### Ví dụ thực tế: Thiết kế tính năng "Tích hợp Thanh toán Quét mã QR Động"

Giả sử dự án cần bổ sung màn hình thanh toán VietQR cho giao diện `TaoDonView`:

##### Bước 1: Khởi động Elephant Brainstorm
Bạn trao đổi với AI:
```text
"Hãy kích hoạt quy trình EGM Brainstorm. Dự án đang có TaoDonView và DonHangApiClient. 
Chúng ta muốn tích hợp tạo mã QR thanh toán động hiển thị lên dialog sau khi ấn nút 'Thanh toán'. 
Yêu cầu tuân thủ No-Code, hãy phân tích rủi ro và các phương án kiến trúc."
```

##### Bước 2: Soạn thảo Design Doc
AI hỗ trợ viết file `docs/designs/001-dynamic-qr-payment.md`:
* **Goals:** Hiển thị Dialog QR, thăm dò (polling) trạng thái thanh toán từ backend.
* **Non-Goals:** Không nhúng SDK cổng thanh toán bên thứ ba vào trực tiếp client (đẩy qua backend).
* **Impacted Files:** Thêm `DialogQRPayment.java` vào `VIEW`, thêm method `checkPaymentStatus` vào `DonHangApiClient.java`.

##### Bước 3: Triệu hồi Goldfish Review
Kích hoạt lệnh review:
```text
"Chạy egm-review đối với docs/designs/001-dynamic-qr-payment.md"
```
* **`egm-design`** sẽ bắt lỗi: *"Cơ chế Polling mỗi 2 giây có thể gây nghẽn mạng siêu thị nếu nhân viên mở nhiều hóa đơn cùng lúc. Cần cơ chế timeout sau 3 phút."*
* **`egm-comprehension`** sẽ phản biện: *"Chưa định nghĩa trạng thái 'PENDING' và 'EXPIRED' xử lý ra sao trên giao diện."*

##### Bước 4: Hoàn thiện & Triển khai
* Cập nhật timeout và error handling vào bản đặc tả.
* Bắt đầu áp dụng **Skill 04 (Superpowers TDD)** và **Skill 05 (WireMock)** để viết code theo đúng hợp đồng đã duyệt.

---

[⬆️ Quay lại Mục lục](#mục-lục)

