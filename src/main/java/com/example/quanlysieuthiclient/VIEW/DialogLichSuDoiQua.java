package com.example.quanlysieuthiclient.VIEW;

import com.example.quanlysieuthiclient.DTO.LichSuDoiQua;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DialogLichSuDoiQua extends JDialog {

    public DialogLichSuDoiQua(JFrame parent, List<LichSuDoiQua> listLichSu, String tenKhachHang) {
        super(parent, "Lịch Sử Đổi Quà - " + tenKhachHang, true);
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Tiêu đề
        JLabel lblTitle = new JLabel("LỊCH SỬ ĐỔI QUÀ GẦN ĐÂY", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 51, 102));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"Thời Gian", "Món Quà", "Điểm Trừ"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        // Format Tiêu đề cột
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(230, 240, 255));
        header.setForeground(new Color(0, 51, 102));
        header.setFont(new Font("Arial", Font.BOLD, 15));

        // Canh giữa dữ liệu
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Đổ dữ liệu vào bảng
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (LichSuDoiQua ls : listLichSu) {
            model.addRow(new Object[]{
                    ls.getThoiGian().format(formatter),
                    ls.getTenQua(),
                    "-" + ls.getDiemTru()
            });
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        // Nút Đóng
        JButton btnDong = new JButton("Đóng");
        btnDong.setFont(new Font("Arial", Font.BOLD, 14));
        btnDong.setBackground(new Color(108, 117, 125));
        btnDong.setForeground(Color.WHITE);
        btnDong.addActionListener(e -> dispose());

        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBot.setBackground(Color.WHITE);
        pnlBot.add(btnDong);
        add(pnlBot, BorderLayout.SOUTH);
    }
}