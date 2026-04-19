/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.quanlysieuthiclient.VIEW;

import com.sun.net.httpserver.Headers;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.time.ZoneId;
import java.util.Calendar;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/**
 *
 * @author Admin
 */
public class nhanvienViews extends JPanel {
    
    private JButton themButton;
    private JButton suaButton;
    private JButton xoaButton;
    private JButton resetButton;
    private JButton timkiemButton;
    
    public JTextField manhanvienField;
    public JTextField tennhanvienField;
    public JDateChooser ngaysinhChooser;
    public JComboBox<String> gioitinhComboBox;
    public JTextField sodienthoaiField;
    public JTextField emailField;
    public JTextField diachiField;
    public JComboBox<String> chucvuBox;
    
    public JTextField timkiemField;
    
    public DefaultTableModel nhanvienDefaultTableModel;
    public JTable nhanvienJTable;
    
    public nhanvienViews() {
        setLayout(null);
        setPreferredSize(new Dimension(1200,900));

        //manhanvien
        JLabel manhanvienJLabel = new JLabel("Mã nhân viên:");
        manhanvienJLabel.setBounds(50, 30, 200, 40);
        manhanvienJLabel.setFont(new Font("Arial",Font.BOLD,23));
        add(manhanvienJLabel);
        manhanvienField=new JTextField();
        manhanvienField.setBounds(300, 30, 300, 40);
        manhanvienField.setFont(new Font("Arial",Font.ITALIC,23));
        add(manhanvienField);

        //tennhanvien
        JLabel tennhanvienJLabel = new JLabel("Tên nhân viên:");
        tennhanvienJLabel.setBounds(50, 100, 200, 40);
        tennhanvienJLabel.setFont(new Font("Arial",Font.BOLD,23));
        add(tennhanvienJLabel);
        tennhanvienField=new JTextField();
        tennhanvienField.setBounds(300, 100, 300, 40);
        tennhanvienField.setFont(new Font("Arial",Font.ITALIC,23));
        add(tennhanvienField);

        //ngaysinh
        JLabel ngaysinhJLabel = new JLabel("Ngày sinh:");
        ngaysinhJLabel.setBounds(50, 150, 200, 40);
        ngaysinhJLabel.setFont(new Font("Arial",Font.BOLD,23));
        add(ngaysinhJLabel);
        
        ngaysinhChooser=new JDateChooser();
        ngaysinhChooser.setDateFormatString("dd/MM/yyyy");
        ngaysinhChooser.setBounds(300, 150, 300, 40);
        ngaysinhChooser.setFont(new Font("Arial",Font.ITALIC,23));
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18);
        ngaysinhChooser.setMaxSelectableDate(calendar.getTime());
        ngaysinhChooser.setDate(calendar.getTime());
        ((JTextField)(ngaysinhChooser.getDateEditor().getUiComponent())).setEditable(false);
        add(ngaysinhChooser);

        //gioitinh
        JLabel gioitinhJLabel = new JLabel("Chọn giới tính:");
        gioitinhJLabel.setBounds(50, 200, 200, 40);
        gioitinhJLabel.setFont(new Font("Arial",Font.BOLD,23));
        add(gioitinhJLabel);
        
        String[] gioitinhStrings={"Nam","Nữ","Khác"};
        gioitinhComboBox=new JComboBox<>(gioitinhStrings);
        gioitinhComboBox.setBounds(300, 200, 300, 40);
        gioitinhComboBox.setFont(new Font("Arial",Font.ITALIC,23));
        add(gioitinhComboBox);

         //sodienthoai
        JLabel sodienthoaiJLabel = new JLabel("Số điện thoại:");
        sodienthoaiJLabel.setBounds(50, 250, 200, 40);
        sodienthoaiJLabel.setFont(new Font("Arial",Font.BOLD,23));
        add(sodienthoaiJLabel);
        
        sodienthoaiField=new JTextField();
        sodienthoaiField.setBounds(300, 250, 300, 40);
        sodienthoaiField.setFont(new Font("Arial",Font.ITALIC,23));
        add(sodienthoaiField);
        
        //email
        JLabel emailJLabel=new JLabel("Email:");
        emailJLabel.setBounds(50, 300, 200, 40);
        emailJLabel.setFont(new Font("Arial",Font.BOLD,23));
        add(emailJLabel);
        
        emailField=new JTextField();
        emailField.setBounds(300, 300, 300, 40);
        emailField.setFont(new Font("Arial",Font.ITALIC,23));
        add(emailField);
        
        //diachi
        JLabel diachiJLabel = new JLabel("Địa chỉ");
        diachiJLabel.setBounds(50, 350, 200, 40);
        diachiJLabel.setFont(new Font("Arial",Font.BOLD,23));
        add(diachiJLabel);
        
        diachiField=new JTextField();
        diachiField.setBounds(300, 350, 300, 40);
        diachiField.setFont(new Font("Arial",Font.ITALIC,23));
        add(diachiField);
        
        //machucvu
        JLabel machucvuJLabel=new JLabel("Chức vụ:");
        machucvuJLabel.setBounds(50, 400, 200, 40);
        machucvuJLabel.setFont(new Font("Arial",Font.BOLD,23));
        add(machucvuJLabel);
        
        chucvuBox=new JComboBox<>();
        chucvuBox.setBounds(300, 400, 300, 40);
        chucvuBox.setFont(new Font("Arial",Font.ITALIC,23));
        add(chucvuBox);

        //button
        themButton=new JButton("Thêm");
        themButton.setBounds(50, 450, 100, 30);
        themButton.setBackground(Color.GREEN);
        themButton.setFont(new Font("Arial",Font.ITALIC,23));
        add(themButton);
        
        suaButton=new JButton("Sửa");
        suaButton.setBounds(200, 450, 100, 30);
        suaButton.setForeground(Color.white);
        suaButton.setBackground(Color.BLUE);
        suaButton.setFont(new Font("Arial",Font.ITALIC,23));
        add(suaButton);
        
        xoaButton=new JButton("Xóa");
        xoaButton.setBounds(350, 450, 100, 30);
        xoaButton.setBackground(Color.red);
        xoaButton.setFont(new Font("Arial",Font.ITALIC,23));
        add(xoaButton);
        
        resetButton=new JButton("Reset");
        resetButton.setBounds(500, 450, 100, 30);
        resetButton.setBackground(Color.yellow);
        resetButton.setFont(new Font("Arial",Font.ITALIC,23));
        add(resetButton);
        
        timkiemField=new JTextField();
        timkiemField.setBounds(50, 500, 300, 40);
        timkiemField.setFont(new Font("Arial",Font.ITALIC,23));
        add(timkiemField);
        
        timkiemButton=new JButton("Tìm kiếm");
        timkiemButton.setBounds(400, 500, 200, 30);
        timkiemButton.setBackground(Color.gray);
        timkiemButton.setFont(new Font("Arial",Font.ITALIC,23));
        add(timkiemButton);

        //table
        String[] nhanvienStrings = {"Mã NV", "Tên NV", "Ngày sinh", "Giới tính","Số điện thoại","Email", "Địa chỉ", "Mã chức vụ"};
        nhanvienDefaultTableModel = new DefaultTableModel(nhanvienStrings, 0);
        nhanvienJTable = new JTable(nhanvienDefaultTableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        
        };
        nhanvienJTable.setBackground(Color.pink);
        nhanvienJTable.setFont(new Font("Arial",Font.BOLD,19));
        nhanvienJTable.setRowHeight(30);
        
        JTableHeader tieudeHeaders=nhanvienJTable.getTableHeader();
        tieudeHeaders.setFont(new Font("Arial",Font.BOLD,20));
        tieudeHeaders.setBackground(Color.GREEN);
        tieudeHeaders.setForeground(Color.black);
        
        JScrollPane nhanvienJScrollPane = new JScrollPane(nhanvienJTable);
        nhanvienJScrollPane.setBounds(30, 550, 1400, 250);
        add(nhanvienJScrollPane);
    }

    //helo
    public void addThemListener(ActionListener listener) {
        themButton.addActionListener(listener);
    }

    public void addSuaListener(ActionListener listener) {
        suaButton.addActionListener(listener);
    }
    public void addXoaListener(ActionListener listener){
        xoaButton.addActionListener(listener);
    }
    public void addResetListener(ActionListener listener){
        resetButton.addActionListener(listener);
    }
    public void addTimKiemListener (ActionListener listener){
        timkiemButton.addActionListener(listener);
    }
    public void addCellClicktable(MouseListener listener) {
        nhanvienJTable.addMouseListener(listener);
    }
    
    
}
