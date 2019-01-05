package receivedata.service;

import java.util.Date;

import receivedata.dao.BoardDao;
import receivedata.dao.BoardDaoImpl;
import receivedata.dao.DataDao;
import receivedata.dao.DataDaoImpl;
import receivedata.dao.TransducerDao;
import receivedata.dao.TransducerDaoImpl;
import receivedata.pojo.Data;
import receivedata.pojo.Transducer;
import receivedata.pojo.TransducerDataConf;
import receivedata.utils.DruidUtil;
import receivedata.utils.PushMsg;
import receivedata.utils.jedis.JedisClient;
import receivedata.utils.jedis.JedisClientPool;

public class DataServiceImpl implements DataService {

	private DataDao dataDao = new DataDaoImpl();
	
	private TransducerDao transducerDao = new TransducerDaoImpl();
	
	private BoardDao boardDao = new BoardDaoImpl();
	
	private JedisClient jedisClient = new JedisClientPool();
	
	private TransducerDataConfService transducerDataConfService = new TransducerDataConfServiceImpl();

	@Override
	public void save(String msg) {

		try {
			// ��������
			DruidUtil.startTransaction();

			// ��������
			String boardId = Integer.parseInt(msg.substring(0, 4), 16) + "";
			String transducerType = msg.substring(8, 12);
			String analysisType = null;
			String transducerId = Integer.parseInt(msg.substring(12, 16), 16) + "";
			String transducerData = msg.substring(16, 20);
			double analysisData = 0;
			int status = 0;
			// �������͵Ĳ�ͬ,������ͬ������
			switch (transducerType) {
			case "0001":
				analysisType = "�¶ȴ�����";
				// �����¶�����
				if (Integer.parseInt(transducerData.substring(0, 1)) == 0) {
					analysisData = (double) Integer.parseInt(transducerData.substring(2, 4), 16);
				} else {
					analysisData = Integer.parseInt(transducerData.substring(2, 4), 16) * (-1);
				}
				break;

			case "0002":
				analysisType = "ʪ�ȴ�����";
				// ����ʪ������
				analysisData = (double) Integer.parseInt(transducerData, 16);
				break;
			default:
				break;
			}
			// ��ô��������ö���
			TransducerDataConf transducerDataConf = transducerDataConfService.getTransducerDataConf(boardId,
					analysisType, transducerId);
			// ��redis��ȡ����Ӧ��ʱ��
			String time = jedisClient.get("time:" + boardId + ":" + transducerType + ":" + transducerId);
			// �ж��Ƿ����ݴ�Χ��
			if ((transducerDataConf.getTransducerLevel() - transducerDataConf.getTransducerErrornum()) <= analysisData
					&& (transducerDataConf.getTransducerLevel()
							+ transducerDataConf.getTransducerErrornum()) >= analysisData) {
				// �ж�ʱ���Ƿ�Ϊ��
				if (time == null || time.equals("")) {
					// ����һ��data����
					Data data = new Data();
					// ��װ����
					data.setBoardId(boardId);
					data.setTransducerId(transducerId);
					data.setTransducerType(analysisType);
					data.setTransducerData((analysisData));
					data.setDataTime(new Date());
					// ִ����Ӳ���
					dataDao.addData(data);
					// ��������������
					Transducer transducer = new Transducer();
					// ��װ����������
					transducer.setApplicationFlag(transducerDataConf.getApplicationFlag());
					transducer.setBoardId(boardId);
					transducer.setTransducerType(analysisType);
					transducer.setTransducerId(transducerId);
					transducer.setTransducerStatus(status);
					transducer.setTransducerNowdata(analysisData);
					transducer.setTransducerTime(new Date());
					// ���´�����
					transducerDao.updateTransducer(transducer);
					// ���°�
					boardDao.updateBoard(boardId, status,new Date());
					// ��redis������һ��ʱ���ʶ,�����ù���ʱ��
					jedisClient.set("time:" + boardId + ":" + transducerType + ":" + transducerId, "time");
					jedisClient.expire("time:" + boardId + ":" + transducerType + ":" + transducerId,
							(transducerDataConf.getTransducerWarntime().intValue()*60));
				}

			} else {
				// �ж��Ƿ����
				if (transducerDataConf.getTransducerMax() <= analysisData) {
					PushMsg.testSendPush(analysisType.substring(0, 2) + "����,����",transducerDataConf.getApplicationFlag());
					System.out.println(analysisType.substring(0, 2) + "����,����");
					status = 2;
				}
				// �ж��Ƿ����
				if (transducerDataConf.getTransducerMin() >= analysisData) {
					PushMsg.testSendPush(analysisType.substring(0, 2) + "����,����", transducerDataConf.getApplicationFlag());
					System.out.println(analysisType.substring(0, 2) + "����,����");
					status = 1;
				}

				// ����һ��data����
				Data data = new Data();
				// ��װ����
				data.setBoardId(boardId);
				data.setTransducerId(transducerId);
				data.setTransducerType(analysisType);
				data.setTransducerData((analysisData));
				data.setDataTime(new Date());
				// ִ����Ӳ���
				dataDao.addData(data);
				// ��������������
				Transducer transducer = new Transducer();
				// ��װ����������
				transducer.setApplicationFlag(transducerDataConf.getApplicationFlag());
				transducer.setBoardId(boardId);
				transducer.setTransducerType(analysisType);
				transducer.setTransducerId(transducerId);
				transducer.setTransducerStatus(status);
				transducer.setTransducerNowdata(analysisData);
				transducer.setTransducerTime(new Date());
				// ���´�����
				transducerDao.updateTransducer(transducer);
				// ���°�
				boardDao.updateBoard(boardId, status,new Date());
				// �ж�ʱ���ʶ�Ƿ�Ϊ��
				if (time == null || time.equals("")) {
					// ������redis������ʱ���ʶ,�����ù���ʱ��
					jedisClient.set("time:" + boardId + ":" + transducerType + ":" + transducerId, "time");
					jedisClient.expire("time:" + boardId + ":" + transducerType + ":" + transducerId,
							(transducerDataConf.getTransducerWarntime().intValue()*60));
				} else {
					// ���ù���ʱ��
					jedisClient.expire("time:" + boardId + ":" + transducerType + ":" + transducerId,
							transducerDataConf.getTransducerWarntime().intValue()*60);
				}

			}
			// �����ύ
			DruidUtil.commit();
		} catch (Exception e) {
			// ����ع�
			DruidUtil.rollback();
		} finally {
			// ����ر�
			DruidUtil.close();
		}

	}

}
