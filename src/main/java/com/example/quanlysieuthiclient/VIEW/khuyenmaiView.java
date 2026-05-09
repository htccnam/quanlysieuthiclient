package com.example.quanlysieuthiclient.VIEW;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.demo.DateChooserPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.util.Calendar;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Admin
 */
public class khuyenmaiView extends JPanel {

    public JTextField makhuyenmaiField ;
    public JTextField tenkhuyenmaiField ;
    public JTextField motaField;
    public JTextField sotiemgiamField;
    public JDateChooser ngaytaoChooser ;

    public JButton themButton;
    public JButton suaButton;
    public JButton xoaButton ;
    public JButton resetButton ;

    public JTextField timkiemField;
    public JButton timkiemButton;

    public DefaultTableModel khuyenmaiDefaultTableModel;
    public JTable khuyenmaiJTable;

    public khuyenmaiView() {
        setLayout(null);
        setPreferredSize(new Dimension(1200,900));

        //makhuyenmai
        JLabel matintucJLabel = new JLabel("Mã khuyến mại:");
        matintucJLabel.setBounds(40, 50, 200, 40);
        matintucJLabel.setFont(new Font("Arial",Font.BOLD,24));
        add(matintucJLabel);

        makhuyenmaiField=new JTextField();
        makhuyenmaiField.setBounds(300, 50, 400, 40);
        makhuyenmaiField.setFont(new Font("Arial",Font.ITALIC,24));
        add(makhuyenmaiField);

        //tenkhuyenmai
        JLabel tenkhuyenmaiJLabel = new JLabel("Tên khuyến mại:");
        tenkhuyenmaiJLabel.setBounds(40, 100, 200, 40);
        tenkhuyenmaiJLabel.setFont(new Font("Arial",Font.BOLD,24));
        add(tenkhuyenmaiJLabel);

        tenkhuyenmaiField=new JTextField();
        tenkhuyenmaiField.setBounds(300, 100, 400, 40);
        tenkhuyenmaiField.setFont(new Font("Arial",Font.ITALIC,24));
        add(tenkhuyenmaiField);

     

        //mota
        JLabel motaJLabel = new JLabel("Mô tả:");
        motaJLabel.setBounds(40, 150, 200, 40);
        motaJLabel.setFont(new Font("Arial",Font.BOLD,24));
        add(motaJLabel);

        motaField =new JTextField();
        motaField.setBounds(300, 150, 500, 40);
        motaField.setFont(new Font("Arial",Font.ITALIC,24));
        add(motaField);

        //sotiemgiam
        JLabel sotiengiamJLabel = new JLabel("Số tiền giảm");
        sotiengiamJLabel.setBounds(850, 150, 200, 40);
        sotiengiamJLabel.setFont(new Font("Arial",Font.BOLD,24));
        add(sotiengiamJLabel);

        sotiemgiamField =new JTextField();
        sotiemgiamField.setBounds(1100, 150, 300, 40);
        sotiemgiamField.setFont(new Font("Arial",Font.ITALIC,24));
        add(sotiemgiamField);


        //ngày tạo
        JLabel ngaytaoJLabel = new JLabel("Ngày tạo:");
        ngaytaoJLabel.setBounds(40, 200, 200, 40);
        ngaytaoJLabel.setFont(new Font("Arial",Font.BOLD,24));
        add(ngaytaoJLabel);

        ngaytaoChooser=new JDateChooser();
        ngaytaoChooser.setDateFormatString("dd/MM/yyyy");
        ngaytaoChooser.setBounds(300, 200, 400, 40);
        ngaytaoChooser.setFont(new Font("Arial",Font.ITALIC,24));
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.YEAR, 0);
        ngaytaoChooser.setMaxSelectableDate(calendar.getTime());
        ngaytaoChooser.setDate(calendar.getTime());
        ((JTextField)(ngaytaoChooser.getDateEditor().getUiComponent())).setEditable(false);
        add(ngaytaoChooser);

        //button
        themButton=new JButton();
        themButton.setText("Thêm");
        themButton.setBounds(50, 250, 100, 30);
        themButton.setBackground(Color.GREEN);
        themButton.setFont(new Font("Arial",Font.BOLD,18));
        add(themButton);

        suaButton=new JButton();
        suaButton.setText("Sửa");
        suaButton.setBounds(200, 250, 100, 30);
        suaButton.setBackground(Color.BLUE);
        suaButton.setForeground(Color.white);
        suaButton.setFont(new Font("Arial",Font.BOLD,18));
        add(suaButton);

        xoaButton=new JButton();
        xoaButton.setText("Xóa");
        xoaButton.setBounds(350, 250, 100, 30);
        xoaButton.setBackground(Color.red);
        xoaButton.setFont(new Font("Arial",Font.BOLD,18));
        add(xoaButton);

        resetButton=new JButton();
        resetButton.setText("reset");
        resetButton.setBounds(500, 250, 100, 30);
        resetButton.setBackground(Color.yellow);
        resetButton.setFont(new Font("Arial",Font.BOLD,18));
        add(resetButton);

        timkiemField=new JTextField();
        timkiemField.setBounds(40, 300, 300, 40);
        timkiemField.setFont(new Font("Arial",Font.ITALIC,24));
        add(timkiemField);
        
        timkiemButton=new JButton();
        timkiemButton.setText("Tìm kiếm");
        timkiemButton.setBounds(400, 300, 200, 30);
        timkiemButton.setBackground(Color.gray);
        timkiemButton.setFont(new Font("Arial",Font.BOLD,18));
        add(timkiemButton);

        String[] colStrings = {"Mã khuyến mại", "Tên khuyến mại", "Mô tả", "Số tiền giảm", "Ngày tạo"};
        khuyenmaiDefaultTableModel = new DefaultTableModel(colStrings, 0);
        khuyenmaiJTable = new JTable(khuyenmaiDefaultTableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        khuyenmaiJTable.setBackground(Color.pink);
        khuyenmaiJTable.setFont(new Font("Arial",Font.BOLD,17));
        khuyenmaiJTable.setRowHeight(30);
        
        JTableHeader khuyenmaiHeader=khuyenmaiJTable.getTableHeader();
        khuyenmaiHeader.setFont(new Font("Arial",Font.BOLD,18));
        khuyenmaiHeader.setBackground(Color.green);
        
        JScrollPane khuyenmaiJScrollPane = new JScrollPane(khuyenmaiJTable);
        khuyenmaiJScrollPane.setBounds(50, 350, 1000, 500);
        add(khuyenmaiJScrollPane);
    }

    public void addcellClickListener(MouseListener listener) {
        khuyenmaiJTable.addMouseListener(listener);
    }

    public void addThemActionListener(ActionListener listener) {
        themButton.addActionListener(listener);
    }

    public void addSuaActionListener(ActionListener listener) {
        suaButton.addActionListener(listener);
    }
    public void addXoaActionListener(ActionListener listener){
        xoaButton.addActionListener(listener);
    }
    public void addResetActionListener(ActionListener listener){
        resetButton.addActionListener(listener);
    }
    public void addTimKiemActionListener(ActionListener listener){
        timkiemButton.addActionListener(listener);
    }
}
