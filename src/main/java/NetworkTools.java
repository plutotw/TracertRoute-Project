import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkTools {

    /**
     * 获取网卡列表
     * @return
     */
    public static NetworkInterface[] getInterfaceList(){
        NetworkInterface[] devList= JpcapCaptor.getDeviceList();
        return devList;
    }

    /**
     * 打开设备接收数据包
     * @param device
     * @return
     */
    public static JpcapCaptor openDevice(NetworkInterface device){
        JpcapCaptor captor = null;
        try {
            captor = JpcapCaptor.openDevice( device, 2000,  false,  1 );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return captor;
    }

    /**
     * 返回指定网卡的ip地址
     * @param device
     * @return
     */
    public static InetAddress getLocalIp(NetworkInterface device){
        if(device==null)return null;
        InetAddress localIP=null;
        localIP=device.addresses[1].address; //1为IPV4地址
        return localIP;
    }

    /**
     * 获取指定URL的ip地址
     * @param url
     * @return
     */
    public static InetAddress getUrlIp(String url){
        InetAddress ip=null;
        try {
            ip=InetAddress.getByName(url);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取本地mac地址
     * @return
     */
    public static byte[] getLocalMac(NetworkInterface device){
        if (device==null)return null;
        return device.mac_address;
    }

    /**
     * 获取网关MAC地址
     * @return
     */
    public static byte[] getGatewayMac(String url){
        byte[] Mac=null;
        NetworkInterface dev=NetworkTools.getInterfaceList()[2];
        JpcapCaptor captor=NetworkTools.openDevice(dev);
        try {
            int counter=0;
            String dstIP=InetAddress.getByName(url).getHostAddress().toString();
            captor.setFilter("tcp",true);
            Socket socket=new Socket(url,80);
            socket.close();
            while (true){
                IPPacket ip=(IPPacket) captor.getPacket();
                if (ip==null){
                    if (counter>3){
                        System.out.println("获取网关MAC地址超时");
                        break;
                    }
                    Thread.sleep(1000);
                    counter++;
                }
                else if (!dstIP.equals(ip.src_ip.getHostAddress())){
                    Mac=((EthernetPacket)ip.datalink).dst_mac;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Mac;
    }

}
