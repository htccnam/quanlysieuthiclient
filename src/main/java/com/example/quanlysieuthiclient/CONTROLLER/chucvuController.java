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
        cvView.addTimKiemClickListener(new timkiemchucvuListener());
        cvView.addClickTableListener(new clicktableListener());
        loadTable();
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

            chucvu cv=new chucvu(machucvuString,tenchucvuString);
            try{
                cvApiClient.themChucVu(cv);
                loadTable();
            }catch (Exception exception){
                JOptionPane.showMessageDialog(cvView,"lỗi thêm chức vụ"+exception.getMessage());
            }
        }
    }
    private class timkiemchucvuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            cvView.chucvuDefaultTableModel.setRowCount(0);
            try {
                List<chucvu> list=cvApiClient.timKiemChucVu(cvView.timkiemField.getText().toString());
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
}
