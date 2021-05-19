public class Util {
    /**
     * 将byte类型的mac地址转化为string类型
     * @param mac
     * @return
     */
    public static String getStringMac(byte[] mac){
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<mac.length; i++){
            if (i != 0){
                sb.append("-");
            }
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length()==1?0+s:s);
        }
        return sb.toString().toUpperCase();
    }
}
