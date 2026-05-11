package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.chucvuApiClient;
import com.example.quanlysieuthiclient.DTO.chucvu;
import com.example.quanlysieuthiclient.VIEW.chucvuView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class chucvuController {
    private chucvuView cvView;
    private chucvuApiClient cvApiClient;
    private int selectedrow=-1;

    public chucvuController(chucvuView cvView) {
        this.cvView=cvView;
        this.cvApiClient=chucvuApiClient.getInstance();

        cvView.addThemClickListener(new themchucvuListener());
        cvView.addSuaClickListener(new suachucvuListener());
        cvView.addTimKiemClickListener(new timkiemchucvuListener());
        cvView.addClickTableListener(new clicktableListener());
        cvView.addXoaClickListener(new xoachucvuListener());
        cvView.addResetClickListener(new resetListener());
        loadTable();
        cvView.xoaButton.setEnabled(false);
        cvView.suaButton.setEnabled(false);
    }
    private void loadTable (){
        try{
            cvView.chucvuDefaultTableModel.setRowCount(0);
            List<chucvu> list= cvApiClient.getAllChucVu();
            for (chucvu cv : list){
                cvView.chucvuDefaultTableModel.addRow(new Object[]{
                        cv.getMachucvu(),
                        cv.getTenchucvu()
                });
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(cvView,"Lỗi tải bảng chức vụ"+e.getMessage());
        }
    }
    private class themchucvuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String machucvuString= cvView.machucvuField.getText().trim();
            String tenchucvuString=cvView.tenchucvuField.getText().trim();

            if (machucvuString.isEmpty()) {
                JOptionPane.showMessageDialog(cvView, "mã nhân viên không được để trống");
                return;
            }
            if (tenchucvuString.isEmpty()) {
                JOptionPane.showMessageDialog(cvView, "mã nhân viên không được để trống");
                return;
            }
            chucvu cv=new chucvu(machucvuString,tenchucvuString);
            try{
                cvApiClient.themChucVu(cv);
                loadTable();
                // Reset form
                cvView.machucvuField.setText("");
                cvView.tenchucvuField.setText("");
                cvView.machucvuField.setEnabled(true);
            }catch (Exception exception){
                JOptionPane.showMessageDialog(cvView,"lỗi thêm chức vụ :"+exception.getMessage());
            }
        }
    }

    private class suachucvuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String machucvuString = cvView.machucvuField.getText();
            String tenchucvuString = cvView.tenchucvuField.getText();

            int result = JOptionPane.showConfirmDialog(cvView, "bạn có chắc chắn muốn sửa");
            if (result == JOptionPane.YES_OPTION) {
                chucvu cv = new chucvu(machucvuString, tenchucvuString);
                try {
                    cvApiClient.suaChucVu(cv);
                    loadTable();
                } catch (Exception ex) {
                    throw new RuntimeException("lỗi sửa chức vụ" + ex.getMessage());
                }
            }


        }
    }
    private class xoachucvuListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String manhanvienString=cvView.machucvuField.getText();
            int result = JOptionPane.showConfirmDialog(cvView,"bạn có chắc chắn muốn xóa");
            if(result==JOptionPane.YES_OPTION){
                try {
                    cvApiClient.xoaChucVu(manhanvienString);
                    loadTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(cvView,"lỗi xóa chức vụ: "+ex.getMessage());
                }
            }
        }
    }
    private class timkiemchucvuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            cvView.chucvuDefaultTableModel.setRowCount(0);
            try {
                List<chucvu> list=cvApiClient.timKiemChucVu(cvView.timkiemField.getText().toString().trim());
                for (chucvu cv : list){
                    cvView.chucvuDefaultTableModel.addRow(new Object[]{
                            cv.getMachucvu(),
                            cv.getTenchucvu()
                    });
                }
            }catch (Exception exception){
                JOptionPane.showMessageDialog(cvView,"lỗi tìm kiếm chức vụ:"+exception.getMessage());
            }
        }
    }
    private class clicktableListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            cvView.machucvuField.setEnabled(false);
            selectedrow=cvView.chucvuJTable.getSelectedRow();
            cvView.machucvuField.setText(cvView.chucvuDefaultTableModel.getValueAt(selectedrow,0).toString().trim());
            cvView.tenchucvuField.setText(cvView.chucvuDefaultTableModel.getValueAt(selectedrow,1).toString().trim());
            cvView.suaButton.setEnabled(true);
            cvView.xoaButton.setEnabled(true);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
    private class resetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            cvView.machucvuField.setText("");
            cvView.tenchucvuField.setText("");
            cvView.timkiemField.setText("");
            cvView.machucvuField.setEnabled(true);
            cvView.tenchucvuField.setEnabled(true);
            cvView.themButton.setEnabled(true);
            cvView.xoaButton.setEnabled(false);
            cvView.suaButton.setEnabled(false);
            loadTable();
        }
    }
}
