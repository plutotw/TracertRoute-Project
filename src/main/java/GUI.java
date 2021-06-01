import jpcap.NetworkInterface;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;


public class GUI extends Frame {
    JPanel Panel;
    JTable table;
    DefaultTableModel tableModel;
    JButton Button,Button2;
    JTextField textField;
    JComboBox cmb;
    JLabel label;
    String url;

    public GUI(){
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        init();
        setProperties();
    }

    public void init(){
        Panel= new JPanel();
        Panel.setLayout(null);
        Panel.setSize(450,450);
        add(Panel);

        textField= new JTextField("github.com");
        textField.setBounds(5,0,150,30);
        textField.setFont(new Font("微软雅黑",0,15));
        Panel.add(textField);

        Button= new JButton("tracert");
        Button.setBounds(155,0,80,30);
        Panel.add(Button);

        Button2= new JButton("reset");
        Button2.setBounds(240,0,80,30);
        Panel.add(Button2);

        cmb=new JComboBox();
        cmb.setBounds(330,0,270,30);
        Panel.add(cmb);

        label= new JLabel("Target host:");
        label.setBounds(10,30,200,30);
        label.setFont(new Font("微软雅黑",0,15));
        Panel.add(label);

        table=new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableModel=(DefaultTableModel)table.getModel();
        tableModel.addColumn("ttl");
        tableModel.addColumn("time(ms)");
        tableModel.addColumn("ip");
        tableModel.addColumn("position");
        tableModel.addColumn("isp");

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(5,70,600,400);
        Panel.add(scroll);

        this.setTitle("Tracert-Route");
        this.setVisible(true);
        this.setSize(625,510);
    }


    public void addRow(Object[] obj){
        tableModel.addRow(obj);
    }

    public void delRow(){
        tableModel.setRowCount(0);
    }

    public void setProperties(){
        for (int i=0;i<tableModel.getColumnCount();i++){
            TableColumn column=table.getColumnModel().getColumn(i);
            column.setPreferredWidth(120);
        }
    }

    public String getUrl() {
        url=textField.getText();
        return url;
    }

    public void setUrl(String s) {
        label.setText(s);
    }

    public void setNetwork(NetworkInterface[] list){
        for (int i=0;i<list.length;i++){
            cmb.addItem(list[i].description+list[i].addresses[1].address);
        }
    }

    public int getNetwork(){
        return cmb.getSelectedIndex();
    }
}
