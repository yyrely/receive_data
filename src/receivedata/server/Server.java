package receivedata.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import receivedata.service.DataService;
import receivedata.service.DataServiceImpl;
import receivedata.utils.StringUtil;


public class Server {

	public static void main(String[] args) throws IOException {
		// 1）、创建服务器+指定端口
		DatagramSocket server = new DatagramSocket(1234);
		while (true) {
			// 2）、准备接受容器 字节数组
			byte[] container = new byte[12];
			// 3）、封装成包
			DatagramPacket packet = new DatagramPacket(container, container.length);
			// 接受数据
			server.receive(packet);
			// 4）、分析数据
			byte[] data = packet.getData();
			//将传来的数据转为字符串
			
			String msg = StringUtil.bytesToHexString(data);
			//截取数据对应的数据
			
			String actionNum = msg.substring(4, 6);
			String dataLength = msg.substring(6, 8);
			
			String crc = StringUtil.getCRC(StringUtil.hexStringToBytes(msg.substring(0, msg.length() - 4)));
			//判断crc与传递来的crc是否相等,数据是否为发数据,数据长度是否为6
			if ((crc.toUpperCase()).equals(StringUtil.convertCRC(msg).toUpperCase()) && "01".equals(actionNum)
					&& "06".equals(dataLength)) {
				
				//调用服务层方法
				DataService dataService = new DataServiceImpl();
				dataService.save(msg);
				
			} else {

				System.out.println("-----"+StringUtil.bytesToHexString(data));
			}

		}

	}
}
