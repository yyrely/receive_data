package receivedata.utils;

public class StringUtil {

	/**
	 * 字节数组转16进制
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src){     
		   StringBuilder stringBuilder = new StringBuilder("");     
		   if (src == null || src.length <= 0) {     
		       return null;     
		   }     
		   for (int i = 0; i < src.length; i++) {     
		       int v = src[i] & 0xFF;     
		       String hv = Integer.toHexString(v);     
		       if (hv.length() < 2) {     
		           stringBuilder.append(0);     
		       }     
		       stringBuilder.append(hv);     
		   }     
		   return stringBuilder.toString();
	}
	
	/**
	 * 16进制数转字节数组
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {     
	    if (hexString == null || hexString.equals("")) {     
	        return null;     
	    }     
	    hexString = hexString.toUpperCase();
	    int length = hexString.length() / 2;
	    char[] hexChars = hexString.toCharArray(); 
	    byte[] d = new byte[length];     
	    for (int i = 0; i < length; i++) {     
	        int pos = i * 2;     
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1])); 
	    }     
	    return d;     
	}
	
	
	/**
	 * 字符转字节
	 * @param c
	 * @return
	 */
	public static byte charToByte(char c) {     
		return (byte) "0123456789ABCDEF".indexOf(c);     
	}
	
	/**
	 * crc检验
	 * @param bytes
	 * @return
	 */
	public static String getCRC(byte[] bytes) {
		int CRC = 0x0000ffff;
		int POLYNOMIAL = 0x0000a001;
		int i, j;
		for (i = 0; i < bytes.length; i++) {
			CRC ^= ((int) bytes[i] & 0x000000ff);
			for (j = 0; j < 8; j++) {
				if ((CRC & 0x00000001) != 0) {
					CRC >>= 1;
					CRC ^= POLYNOMIAL;
				} else {
					CRC >>= 1;
				}
			}
		}
		return Integer.toHexString(CRC);
	}

	/**
	 * 转换crc
	 * @param msg
	 * @return
	 */
	public static String convertCRC(String msg){
		String reverseCrc = msg.substring(msg.length()-4, msg.length());
		String crc = reverseCrc.substring(2, 4)+reverseCrc.substring(0, 2);
		return crc;
	}

}














