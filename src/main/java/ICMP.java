import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;

import java.io.IOException;
import java.net.InetAddress;

public class ICMP {
    /**
     * 构造一个ICMP数据包
     * @param ttl
     * @param srcIP
     * @param dstIP
     * @param LocalMAC
     * @param GatewayMAC
     * @return
     */
    public ICMPPacket createICMP(short ttl, InetAddress srcIP, InetAddress dstIP, byte[] LocalMAC, byte[] GatewayMAC){
        ICMPPacket icmp = new ICMPPacket();
        icmp.type       = 8;
        icmp.code       = 0;
        icmp.seq        = 100;
        icmp.id         = 0;
        icmp.data       = "data".getBytes ();
        icmp.setIPv4Parameter(0, false, false, false, 0, false, false, false,
                0, 0, 0, IPPacket.IPPROTO_ICMP, srcIP, dstIP);

        EthernetPacket ether = new EthernetPacket();
        ether.frametype = EthernetPacket.ETHERTYPE_IP;
        ether.src_mac   = LocalMAC;
        ether.dst_mac   = GatewayMAC;
        icmp.datalink   = ether;
        icmp.hop_limit  = ttl;

        return icmp;
    }

    /**
     * 发送一个ICMP数据包
     * @param icmp
     * @param captor
     * @param dev
     */
    public void sendICMP(ICMPPacket icmp, JpcapCaptor captor, NetworkInterface dev){
        JpcapSender sender;
        sender = captor.getJpcapSenderInstance ();
        sender.sendPacket(icmp);
    }

    /**
     * 接受一个ICMP数据包
     * @param captor
     * @return
     * @throws IOException
     */
    public ICMPPacket revICMP(JpcapCaptor captor){
        ICMPPacket p = null;
        int counter=0;
        try {
            captor.setFilter("icmp",true);
            while(true){
                p = (ICMPPacket)captor.getPacket ();
                if(p==null){
                    if (counter>100)break;
                    counter++;
                    Thread.sleep(10);
                    continue;
                }else if (p.type==11){
                    break;
                }else if(p.type==0){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return p;
    }

    /**
     * 解析一个ICMP数据包
     * @param icmp
     */
    public boolean parseICMP(ICMPPacket icmp){
        if (icmp==null) {
            System.out.println("请求超时");
        }
        else if(icmp.type==11){
            System.out.println(icmp.src_ip.getHostAddress()+"->"+icmp.dst_ip.getHostAddress());
        }
        else if (icmp.type==0){
            System.out.println(icmp.src_ip.getHostAddress()+"->"+icmp.dst_ip.getHostAddress());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
