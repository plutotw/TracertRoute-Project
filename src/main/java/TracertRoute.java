import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.ICMPPacket;

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

    public TracertRoute() {
        gui=new GUI();
        gui.Button.addActionListener(this);
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
        url=gui.getUrl();
        device=NetworkTools.getInterfaceList()[2];
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
                String city;
                JsonRootBean json=HttpRequest.sendGet(icmpPacket1.src_ip.toString());
                if (json.getCity()==null){
                    city="LAN Network";
                }else city=json.getCity();
                Object[] obj={ttl,delay,icmpPacket1.src_ip.getHostAddress(),city};
                gui.addRow(obj);
            }else {
                Object[] obj={ttl,"*","null","null"};
                gui.addRow(obj);
            }
            flag=icmp.parseICMP(icmpPacket1);
        }
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
