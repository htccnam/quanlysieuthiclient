package com.example.quanlysieuthiclient.VIEW;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ChiTietView extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JButton btnSua, btnXoa, btnXemChiTiet;

    private JLabel lblTongDoanhThu, lblTongDon, lblDoanhThuTB;

    public ChiTietView() {
        setLayout(new BorderLayout(0, 10));
        setBackground(new Color(245, 247, 249));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(CongCu(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(ThongKe(), BorderLayout.NORTH);
        centerPanel.add(Table(), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel CongCu() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        left.setOpaque(false);

        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        lblTimKiem.setFont(new Font("Arial", Font.BOLD, 14));

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(250, 35));

        left.add(lblTimKiem);
        left.add(txtSearch);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        btnXemChiTiet = new JButton("Xem CT");
        styleButton(btnXemChiTiet, new Color(59, 130, 246));

        btnSua = new JButton("Sửa");
        styleButton(btnSua, new Color(245, 158, 11));

        btnXoa = new JButton("Xóa");
        styleButton(btnXoa, new Color(239, 68, 68));

        right.add(btnXemChiTiet);
        right.add(btnSua);
        right.add(btnXoa);

        topBar.add(left, BorderLayout.WEST);
        topBar.add(right, BorderLayout.EAST);

        return topBar;
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private JPanel ThongKe() {
        JPanel pnlCard = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCard.setOpaque(false);

        lblTongDoanhThu = new JLabel("0 đ");
        pnlCard.add(Card("Tổng doanh thu", lblTongDoanhThu, new Color(34, 197, 94)));

        lblTongDon = new JLabel("0");
        pnlCard.add(Card("Tổng số đơn hàng", lblTongDon, new Color(59, 130, 246)));

        lblDoanhThuTB = new JLabel("0 đ/đơn");
        pnlCard.add(Card("Doanh thu TB/Đơn", lblDoanhThuTB, new Color(245, 158, 11)));

        return pnlCard;
    }

    private JPanel Card(String title, JLabel lblVal, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.GRAY);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));

        lblVal.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblVal.setForeground(Color.DARK_GRAY);

        JPanel icon = new JPanel();
        icon.setPreferredSize(new Dimension(5, 40));
        icon.setBackground(accentColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblVal, BorderLayout.CENTER);
        card.add(icon, BorderLayout.EAST);

        return card;
    }

    private JPanel Table() {
        JPanel pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(Color.WHITE);
        pnlContent.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        String[] cols = {
                "MÃ ĐƠN HÀNG",
                "NGÀY GIAO DỊCH",
                "NHÂN SỰ",
                "PHƯƠNG THỨC BÁN",
                "THANH TOÁN",
                "TỔNG TIỀN"
        };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(245, 245, 245));
        table.setSelectionBackground(new Color(240, 247, 255));

        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(250, 250, 250));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        pnlContent.add(scroll, BorderLayout.CENTER);
        return pnlContent;
    }

    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
    public JTextField getTxtSearch() { return txtSearch; }

    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnXemChiTiet() { return btnXemChiTiet; }

    public JLabel getLblTongDoanhThu() { return lblTongDoanhThu; }
    public JLabel getLblTongDon() { return lblTongDon; }
    public JLabel getLblDoanhThuTB() { return lblDoanhThuTB; }
}
