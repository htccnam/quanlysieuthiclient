# HƯỚNG DẪN TRIỂN KHAI & HIỆN ĐẠI HÓA GIAO DIỆN SWING VỚI FLATLAF

Đóng vai trò là một **Senior Tech Lead và Mentor đào tạo lập trình**, tài liệu này hướng dẫn chi tiết từ A đến Z cách nâng cấp toàn bộ giao diện Java Swing của dự án `quanlysieuthiclient` sang phong cách phẳng hiện đại (FlatLaf Light/Dark Theme).

---

<a id="mục-lục"></a>
## 📑 Mục lục (Table of Contents)
1. [Sơ đồ luồng hoạt động tổng quan](#1-sơ-đồ-luồng-hoạt-động-tổng-quan)
2. [Giai đoạn 1: Thiết lập hạ tầng & Khai báo Dependency Maven (Setup)](#2-giai-đoạn-1-thiết-lập-hạ-tầng--khai-báo-dependency-maven-setup)
3. [Giai đoạn 2: Cài đặt Look & Feel trong mã nguồn Java (Implementation)](#3-giai-đoạn-2-cài-đặt-look--feel-trong-mã-nguồn-java-implementation)
4. [Giai đoạn 3: Kiểm thử hiển thị giao diện (UI Verification)](#4-giai-đoạn-3-kiểm-thử-hiển-thị-giao-diện-ui-verification)
5. [Sổ tay lỗi kinh điển cần tránh (Lessons Learned)](#5-sổ-tay-lỗi-kinh-điển-cần-tránh-lessons-learned)
6. [Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)](#6-hướng-dẫn-các-cách-dùng-skill-trong-thực-tế-practical-usage-workflows)

---

### 1. Sơ đồ luồng hoạt động tổng quan

```text
[Ứng dụng khởi động: LoginView main()]
                 │
                 ▼
     [FlatLightLaf.setup()]  <-- (Nạp cấu hình Theme FlatLaf trước khi khởi tạo Component)
                 │
                 ▼
[UIManager cập nhật Look & Feel]
                 │
                 ▼
  ┌──────────────────────────────┬──────────────────────────────┐
  ▼                              ▼                              ▼
[Bảng JTable bo góc & highlight] [Nút JButton phẳng & hover]    [JTextField hiện đại]
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 2. Giai đoạn 1: Thiết lập hạ tầng & Khai báo Dependency Maven (Setup)

#### 2.1. Thêm FlatLaf vào `pom.xml`

Thêm khối dependency sau vào file `pom.xml`:

```xml
<dependency>
    <groupId>com.formdev</groupId>
    <artifactId>flatlaf</artifactId>
    <version>3.5.4</version>
</dependency>
```

Sau đó chạy lệnh tải thư viện thông qua Maven CLI:

```bash
mvn dependency:resolve
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `mvn`: Trình thực thi công cụ quản lý dự án Apache Maven.
* `dependency:resolve`: Plugin mục tiêu (goal) yêu cầu Maven phân tích toàn bộ file `pom.xml` và tải về tất cả các gói `.jar` còn thiếu vào thư mục lưu trữ cục bộ (Local Repository `.m2`).

#### 🧠 Tư duy logic: Tại sao phải chọn FlatLaf thay vì Nimbus hay Metal có sẵn của Java?
Look and Feel mặc định của Java Swing (Metal/Windows 98) đã lỗi thời hơn 20 năm, gây cảm giác ứng dụng thô và khó nhìn. FlatLaf hỗ trợ độ phân giải cao (HiDPI scaling trên màn hình 2K/4K), bo tròn nút bấm, hỗ trợ Dark Mode và các hiệu ứng hover chuyên nghiệp mà không cần viết lại mã giao diện.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Lệnh trên làm thay đổi cấu hình dự án và nạp thư viện vào kho máy:

```text
quanlysieuthiclient/
├── 📄 pom.xml                      <-- [CẬP NHẬT] Thêm khối dependency "com.formdev:flatlaf"
C:\Users\<User>\.m2\repository\
└── 📁 com\formdev\flatlaf\3.5.4\   <-- [MỚI TẠO chính xác từ lệnh mvn dependency:resolve] File flatlaf-3.5.4.jar
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 3. Giai đoạn 2: Cài đặt Look & Feel trong mã nguồn Java (Implementation)

#### 3.1. Kích hoạt FlatLaf tại điểm vào của ứng dụng (Entry Point)

Mở file `src/main/java/com/example/quanlysieuthiclient/VIEW/LoginView.java` và bổ sung dòng thiết lập ngay đầu hàm `main`:

```java
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public static void main(String[] args) {
    // Kích hoạt giao diện phẳng FlatLaf trước khi khởi tạo UI
    try {
        FlatLightLaf.setup();
    } catch (Exception ex) {
        System.err.println("Không thể khởi tạo FlatLaf Look and Feel: " + ex.getMessage());
    }

    SwingUtilities.invokeLater(() -> {
        new LoginView().setVisible(true);
    });
}
```

#### 🧠 Tư duy logic: Tại sao `FlatLightLaf.setup()` phải đặt TRƯỚC `new LoginView()`?
Trong mô hình đồ họa Java Swing (AWT/Swing Toolkit), một khi cửa sổ `JFrame` hoặc các component đã được tạo vào bộ nhớ, việc đổi theme sau đó sẽ phải gọi `SwingUtilities.updateComponentTreeUI()` rất tốn hiệu năng và dễ bị giật màn hình hoặc vỡ layout.

#### 📂 MINH HỌA SỰ TIẾN HÓA CỦA THƯ MỤC
Sự thay đổi tập trung vào tầng VIEW:

```text
quanlysieuthiclient/src/main/java/com/example/quanlysieuthiclient/
└── 📁 VIEW/
    └── 📄 LoginView.java           <-- [CẬP NHẬT] Thêm import FlatLightLaf và gọi FlatLightLaf.setup()
```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 4. Giai đoạn 3: Kiểm thử hiển thị giao diện (UI Verification)

#### 4.1. Chạy biên dịch và khởi động ứng dụng

Biên dịch và chạy thử bằng lệnh:

```bash
mvn compile exec:java -Dexec.mainClass="com.example.quanlysieuthiclient.VIEW.LoginView"
```

#### 🔍 Giải phẫu chi tiết lệnh Terminal
* `mvn compile`: Yêu cầu Maven biên dịch tất cả file `.java` trong `src/main/java` thành file bytecode `.class` trong thư mục `target/classes`.
* `exec:java`: Plugin thực thi ứng dụng Java trực tiếp từ Maven.
* `-Dexec.mainClass="..."`: Tham số định danh class chứa hàm `main()` khởi chạy chương trình.

#### 4.2. Bảng kiểm tra chất lượng hiển thị UI

| Thành phần UI | Giao diện cũ (Metal/Windows Classic) | Giao diện mới (FlatLaf) |
| :--- | :--- | :--- |
| Nút Đăng nhập (`JButton`) | Khối chữ nhật cứng, viền xám | Viền bo cong mềm mại, hiệu ứng hover đổi màu |
| Ô nhập tài khoản (`JTextField`) | Chìm (beveled 3D) kiểu cũ | Phẳng hiện đại, viền sáng xanh khi focus |
| Bảng dữ liệu (`JTable`) | Kẻ ô lưới đơn điệu, font chữ mờ | Hàng xen kẽ màu, header đổ bóng nhẹ, font sắc nét trên HiDPI |

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 5. Sổ tay lỗi kinh điển cần tránh (Lessons Learned)

> [!WARNING]
> **Lỗi 1: `ClassNotFoundException: com.formdev.flatlaf.FlatLightLaf` khi chạy**
> * **Nguyên nhân:** Đã sửa `pom.xml` nhưng chưa bấm Reload Project / Sync Maven trong IntelliJ IDEA.
> * **Khắc phục:** Click chuột phải vào `pom.xml` -> Chọn **Maven** -> **Reload Project**.

> [!CAUTION]
> **Lỗi 2: Font chữ tiếng Việt hiển thị ô vuông lỗi mã hóa trên Windows**
> * **Nguyên nhân:** FlatLaf dùng font hệ thống cũ của Windows chưa hỗ trợ đầy đủ Unicode tiếng Việt.
> * **Khắc phục:** Cấu hình font Segoe UI hoặc Tahoma cho toàn bộ UIManager:
>   ```java
>   UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
>   ```

[⬆️ Quay lại Mục lục](#mục-lục)

---

### 6. Hướng dẫn các cách dùng Skill trong thực tế (Practical Usage Workflows)

Để biến ứng dụng quản lý siêu thị từ giao diện cũ thành phần mềm chuyên nghiệp, đây là các cách áp dụng thực tế:

#### Kịch bản 1: Chuyển đổi Dark Mode (Nền tối) tức thì
Nếu siêu thị hoạt động ca đêm hoặc thu ngân muốn đỡ mỏi mắt, bạn có thể dễ dàng đổi toàn bộ ứng dụng sang chế độ tối bằng cách đổi lệnh setup:

```java
import com.formdev.flatlaf.FlatDarkLaf;

// Thay vì FlatLightLaf, gọi FlatDarkLaf:
FlatDarkLaf.setup();
FlatLaf.updateUI(); // Cập nhật lại toàn bộ cây giao diện đang hiển thị
```

##### 🧠 Tư duy logic
FlatLaf xử lý tự động độ tương phản màu chữ và nền, đảm bảo chữ trắng trên nền xám than rõ ràng mà không làm mất thông tin hiển thị trên `JTable`.

---

#### Kịch bản 2: Bo tròn toàn bộ nút bấm và ô nhập liệu chỉ trong 1 dòng code
Đặt các thuộc tính sau vào hàm `main` ngay sau khi gọi `setup()`:

```java
// Bo góc tròn cho tất cả nút bấm (JButton) trong toàn bộ ứng dụng
UIManager.put("Button.arc", 16);

// Bo góc cho ô nhập văn bản (JTextField, JPasswordField)
UIManager.put("Component.arc", 12);

// Tăng chiều cao của các dòng trong bảng JTable để dễ đọc và dễ click chuột
UIManager.put("Table.rowHeight", 28);
```

##### 🧠 Tư duy logic
Thay vì phải can thiệp vào từng file View (`sanphamView`, `KhachHangView`, `TaoDonView`) để set Border hoặc set Size thủ công, cơ chế `UIManager` áp dụng toàn cục cho toàn bộ hệ thống ngay lập tức.

---

#### Kịch bản 3: Tùy chỉnh màu nhận diện thương hiệu siêu thị (Brand Accent Color)
Nếu siêu thị muốn màu xanh lá mát mắt (phong cách Bách Hóa Xanh / Co.opmart):

```java
// Đặt màu chủ đạo cho viền focus và nút mặc định
UIManager.put("Component.focusColor", new Color(46, 204, 113));
UIManager.put("Button.default.background", new Color(39, 174, 96));
```

#### 📌 Bảng tra cứu nhanh thuộc tính FlatLaf (Cheat Sheet)

| Nhu cầu thẩm mỹ | Thuộc tính UIManager cần gọi |
| :--- | :--- |
| Đổi theme sáng hiện đại | `FlatLightLaf.setup();` |
| Đổi theme tối | `FlatDarkLaf.setup();` |
| Bo tròn nút | `UIManager.put("Button.arc", 16);` |
| Tăng khoảng cách dòng trong bảng | `UIManager.put("Table.rowHeight", 30);` |
| Hiển thị đường kẻ ô trong JTable | `UIManager.put("Table.showHorizontalLines", true);` |

[⬆️ Quay lại Mục lục](#mục-lục)

