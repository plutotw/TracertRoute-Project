import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 主函数，程序入口
 */
public class main {
    public static void main(String[] args) {
        TracertRoute route=new TracertRoute();
        Thread t=new Thread(route);
        t.start();
    }
}
