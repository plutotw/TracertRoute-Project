import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.ICMPPacket;

import java.net.InetAddress;

public class TracertRoute implements Runnable{
    JpcapCaptor captor;
    NetworkInterface device;
    byte[] srcMAC,dstMAC;
    InetAddress srcIP,dstIP;
    @Override
    public void run() {
        /**
         * 初始化
         */
        device=NetworkTools.getInterfaceList()[2];
        captor=NetworkTools.openDevice(device);
        srcIP=NetworkTools.getLocalIp(device);
        dstIP=NetworkTools.getUrlIp("baidu.com");
        srcMAC=NetworkTools.getLocalMac(device);
        dstMAC=NetworkTools.getGatewayMac("baidu.com");
        /**
         *
         */
        ICMP icmp = new ICMP();
        short ttl=0;
        boolean flag=Boolean.TRUE;
        long cur,pre;
        while (flag) {
            ttl++;
            System.out.println("----------");
            System.out.println("ttl:"+ttl);
            ICMPPacket icmpPacket = icmp.createICMP(ttl, srcIP, dstIP, srcMAC, dstMAC);
            cur=System.nanoTime();
            icmp.sendICMP(icmpPacket, captor, device);
            ICMPPacket icmpPacket1=icmp.revICMP(captor);
            pre=System.nanoTime();
            System.out.println((pre-cur)/1000000+"ms");
            flag=icmp.parseICMP(icmpPacket1);
        }
        System.out.println("success!");
    }
}
