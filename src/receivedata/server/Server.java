package receivedata.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import receivedata.service.DataService;
import receivedata.service.DataServiceImpl;
import receivedata.utils.StringUtil;


public class Server {

	public static void main(String[] args) throws IOException {
		// 1��������������+ָ���˿�
		DatagramSocket server = new DatagramSocket(1234);
		while (true) {
			// 2����׼���������� �ֽ�����
			byte[] container = new byte[12];
			// 3������װ�ɰ�
			DatagramPacket packet = new DatagramPacket(container, container.length);
			// ��������
			server.receive(packet);
			// 4������������
			byte[] data = packet.getData();
			//������������תΪ�ַ���
			
			String msg = StringUtil.bytesToHexString(data);
			//��ȡ���ݶ�Ӧ������
			
			String actionNum = msg.substring(4, 6);
			String dataLength = msg.substring(6, 8);
			
			String crc = StringUtil.getCRC(StringUtil.hexStringToBytes(msg.substring(0, msg.length() - 4)));
			//�ж�crc�봫������crc�Ƿ����,�����Ƿ�Ϊ������,���ݳ����Ƿ�Ϊ6
			if ((crc.toUpperCase()).equals(StringUtil.convertCRC(msg).toUpperCase()) && "01".equals(actionNum)
					&& "06".equals(dataLength)) {
				
				//���÷���㷽��
				DataService dataService = new DataServiceImpl();
				dataService.save(msg);
				
			} else {

				System.out.println("-----"+StringUtil.bytesToHexString(data));
			}

		}

	}
}
