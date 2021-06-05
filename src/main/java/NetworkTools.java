import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;

import javax.swing.*;
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
     * 关闭设备
     * @param captor
     */
    public static void closeDevice(JpcapCaptor captor){
        captor.close();
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
            JOptionPane.showMessageDialog(null, "非法域名，请重新输入!");
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
    public static byte[] getGatewayMac(String url,NetworkInterface dev){
        byte[] Mac=null;
        JpcapCaptor captor=NetworkTools.openDevice(dev);
        try {
            int counter=0;
            String dstIP=InetAddress.getByName(url).getHostAddress();
            captor.setFilter("tcp",true);
            Socket socket=new Socket(url,80);
            socket.close();
            while (true){
                IPPacket ip=(IPPacket) captor.getPacket();
                if (ip==null){
                    if (counter>20){
                        JOptionPane.showMessageDialog(null, "获取网关MAC地址超时!");
                        System.out.println("获取网关MAC地址超时");
                        break;
                    }
                    Thread.sleep(100);
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
