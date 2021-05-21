import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;


public class GUI extends Frame {
    Panel Panel;
    JTable table;
    DefaultTableModel tableModel;
    Button Button;
    TextField textField;
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

        textField=new TextField("baidu.com");
        textField.setBounds(5,0,200,30);
        Panel.add(textField);

        Button=new Button("tracert");
        Button.setBounds(205,0,100,30);
        Panel.add(Button);

        label=new Label("Target host:");
        label.setBounds(5,30,200,30);
        Panel.add(label);

        table=new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableModel=(DefaultTableModel)table.getModel();
        tableModel.addColumn("ttl");
        tableModel.addColumn("time");
        tableModel.addColumn("ip");
        tableModel.addColumn("city");

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(5,70,450,400);
        Panel.add(scroll);


        this.setTitle("Tracert-Route");
        this.setVisible(true);
        this.setSize(475,500);
    }


    public void addRow(Object[] obj){
        tableModel.addRow(obj);
    }

    public void setProperties(){
        for (int i=0;i<tableModel.getColumnCount();i++){
            TableColumn column=table.getColumnModel().getColumn(i);
            column.setPreferredWidth(112);
        }
    }

    public String getUrl() {
        url=textField.getText();
        return url;
    }

    public void setUrl(String s) {
        label.setText(s);
    }
}
