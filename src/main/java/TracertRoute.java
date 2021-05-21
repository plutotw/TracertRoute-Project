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

    public TracertRoute() {
        gui=new GUI();
        gui.Button.addActionListener(this);
        gui.setNetwork(NetworkTools.getInterfaceList());
        icmp = new ICMP();
    }

    @Override
    public void run() {
        init();
        Tracert();
    }

    /**
     * 初始化
     */
    public void init(){
        devNo= gui.getNetwork();
        url=gui.getUrl();
        device=NetworkTools.getInterfaceList()[devNo];
        captor=NetworkTools.openDevice(device);
        srcIP=NetworkTools.getLocalIp(device);
        dstIP=NetworkTools.getUrlIp(url);
        srcMAC=NetworkTools.getLocalMac(device);
        dstMAC=NetworkTools.getGatewayMac(url);
        gui.setUrl("Target host:"+dstIP.getHostAddress());
    }

    public void Tracert(){
        /**
         *开始tracertroute
         */
        short ttl=0;
        boolean flag=Boolean.TRUE;
        long cur,pre,delay;
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
            if (icmpPacket1!=null){
                String city="",isp="";
                JsonRootBean json=HttpRequest.sendGet(icmpPacket1.src_ip.toString());
                if (json!=null){
                    isp=json.getIsp();
                    if (json.getCity()==null){
                        city="Local area network";
                    }else city=json.getCity();
                }
                Object[] obj={ttl,delay,icmpPacket1.src_ip.getHostAddress(),city,isp};
                gui.addRow(obj);
            }else {
                Object[] obj={ttl,"*","null","null","null"};
                gui.addRow(obj);
            }
            flag=icmp.parseICMP(icmpPacket1);
        }
        JOptionPane.showMessageDialog(null, "路由追踪完成!");
        System.out.println("success!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()== gui.Button){
            Thread thread=new Thread(this);
            thread.start();
        }
    }
}
