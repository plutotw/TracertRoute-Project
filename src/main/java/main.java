import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 主函数，程序入口
 */
public class main {
    public static void main(String[] args) {
/*        TracertRoute route=new TracertRoute();
        Thread t=new Thread(route);
        t.start();*/
        InetAddress dstIP= null;
        try {
            dstIP = InetAddress.getByName("baidu.com");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        JsonRootBean json=HttpRequest.sendGet(dstIP);
        if (json!=null)System.out.println(json.getStatus());
        else System.out.println("n");
    }
}
