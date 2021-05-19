import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkTools {
    static JpcapCaptor captor;

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
     * @param devNo
     * @return
     */
    public static NetworkInterface openDevice(int devNo){
        NetworkInterface device=getInterfaceList()[devNo];
        try {
            captor = JpcapCaptor.openDevice( device, 2000,  false,  1 );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return device;
    }

    /**
     * 返回指定网卡的ip地址
     * @param devNO
     * @return
     */
    public static InetAddress getLocalIp(int devNO,NetworkInterface device){
        if(device==null)return null;
        InetAddress localIP=null;
        localIP=device.addresses[devNO].address;
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
    public static byte[] getGatewayMac(){
        byte[] Gateway=new byte[]{(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff};
        return Gateway;
    }

    public static JpcapCaptor getCaptor() {
        return captor;
    }
}
