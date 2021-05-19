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
        device=NetworkTools.openDevice(2);
        captor=NetworkTools.getCaptor();
        srcIP=NetworkTools.getLocalIp(1,device);
        dstIP=NetworkTools.getUrlIp("baidu.com");
        srcMAC=NetworkTools.getLocalMac(device);
        //dstMAC=NetworkTools.getGatewayMac();
        dstMAC=new byte[]{(byte) 0xec,(byte) 0x26,(byte) 0xca,(byte) 0x3b,(byte) 0x08,(byte) 0xc8};
        /**
         *
         */
        ICMP icmp = new ICMP();
        short ttl=0;
        boolean flag=Boolean.TRUE;
        while (flag) {
            ttl++;
            ICMPPacket icmpPacket = icmp.createICMP(ttl, srcIP, dstIP, srcMAC, dstMAC);
            icmp.sendICMP(icmpPacket, captor, device);
            ICMPPacket icmpPacket1=icmp.revICMP(captor);
            if (icmpPacket1==null)
                System.out.println("请求超时");
            else{
                System.out.println(icmpPacket1.src_ip+"->"+icmpPacket1.dst_ip);
            }
            if (icmpPacket1!=null&&icmpPacket1.type==0)flag=Boolean.FALSE;
        }
        System.out.println("success!");
    }
}
