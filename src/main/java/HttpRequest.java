import com.alibaba.fastjson.JSON;
import com.ejlchina.okhttps.OkHttps;

import java.net.InetAddress;

public class HttpRequest {
    /**
     * 将ip地址使用接口进行解析，返回IP信息
     * @param ip
     * @return
     */
    public static JsonRootBean sendGet(InetAddress ip){
        String ipInformation="";
        try{
            ipInformation= OkHttps.sync("http://ip-api.com/json/"+ip.getHostAddress()).get().getBody().toString();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return JSON.parseObject(ipInformation,JsonRootBean.class);
    }
}
