import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.ICMPPacket;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class TracertRoute implements Runnable, ActionListener {
    JpcapCaptor captor;
    NetworkInterface device;
    byte[] srcMAC,dstMAC;
    InetAddress srcIP,dstIP;
    String url;
    GUI gui;
    ICMP icmp;
    int devNo;
    short ttl;
    boolean flag;
    long cur,pre,delay;

    /**
     * 构造函数
     */
    public TracertRoute() {
        gui=new GUI();
        gui.Button.addActionListener(this);
        gui.Button2.addActionListener(this);
        gui.setNetwork(NetworkTools.getInterfaceList());
        icmp = new ICMP();
    }

    /**
     * 线程，用于启动路由跟踪程序
     */
    @Override
    public void run() {
        init();
        Tracert();
    }

    /**
     * 初始化
     */
    public void init(){
        ttl=0;
        flag=Boolean.TRUE;
        devNo= gui.getNetwork();
        url=gui.getUrl();
        device=NetworkTools.getInterfaceList()[devNo];
        captor=NetworkTools.openDevice(device);
        srcIP=NetworkTools.getLocalIp(device);
        dstIP=NetworkTools.getUrlIp(url);
        srcMAC=NetworkTools.getLocalMac(device);
        dstMAC=NetworkTools.getGatewayMac(url,device);
        gui.setUrl("Target host:"+dstIP.getHostAddress());
    }

    /**
     *开始tracertroute
     */
    public void Tracert(){
        while (flag) {
            ttl++;
            System.out.println("----------");
            System.out.println("ttl:"+ttl);
            ICMPPacket icmpPacket = icmp.createICMP(ttl, srcIP, dstIP, srcMAC, dstMAC);
            cur=System.nanoTime();
            icmp.sendICMP(icmpPacket, captor, device);
            ICMPPacket icmpPacket1=icmp.revICMP(captor);
            pre=System.nanoTime();
            delay=(pre-cur)/1000000;
            setTable(icmpPacket1);
            flag=icmp.parseICMP(icmpPacket1);
        }
        JOptionPane.showMessageDialog(null, "路由追踪完成!");
        System.out.println("success!");
        NetworkTools.closeDevice(captor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()== gui.Button){
            Thread thread=new Thread(this);
            thread.start();
        }
        if (e.getSource()==gui.Button2){
            gui.delRow();
        }
    }

    /**
     * 将数据展示到界面上
     * @param icmpPacket
     */
    public void setTable(ICMPPacket icmpPacket){
        if (icmpPacket!=null){
            String city="",isp="",country="",regionName="";
            JsonRootBean json=HttpRequest.sendGet(icmpPacket.src_ip.toString());
            if (json!=null){
                isp=json.getIsp();
                if (json.getCity()==null)
                    city="局域网";
                else{
                    regionName=json.getRegionName();
                    city=json.getCity();
                    country=json.getCountry();
                }

            }
            Object[] obj={ttl,delay,icmpPacket.src_ip.getHostAddress(),
                    country+" "+regionName+" "+city,isp};
            gui.addRow(obj);
        }else {
            Object[] obj={ttl,"*","*","*",""};
            gui.addRow(obj);
        }
    }
}
