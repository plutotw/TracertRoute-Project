import jpcap.NetworkInterface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;


public class GUI extends Frame {
    Panel Panel;
    JTable table;
    DefaultTableModel tableModel;
    Button Button,Button2;
    TextField textField;
    JComboBox cmb;
    Label label;
    String url;

    public GUI(){
        init();
        setProperties();
    }

    public void init(){
        Panel=new Panel();
        Panel.setLayout(null);
        Panel.setSize(450,450);
        add(Panel);

        textField=new TextField("github.com");
        textField.setBounds(5,0,150,30);
        textField.setFont(new Font("微软雅黑",0,15));
        Panel.add(textField);

        Button=new Button("tracert");
        Button.setBounds(155,0,80,30);
        Panel.add(Button);

        Button2=new Button("reset");
        Button2.setBounds(240,0,80,30);
        Panel.add(Button2);

        cmb=new JComboBox();
        cmb.setBounds(330,0,270,30);
        Panel.add(cmb);

        label=new Label("Target host:");
        label.setBounds(5,30,200,30);
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

        String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
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
