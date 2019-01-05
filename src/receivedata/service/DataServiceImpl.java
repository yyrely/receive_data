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
			// 开启事务
			DruidUtil.startTransaction();

			// 解析参数
			String boardId = Integer.parseInt(msg.substring(0, 4), 16) + "";
			String transducerType = msg.substring(8, 12);
			String analysisType = null;
			String transducerId = Integer.parseInt(msg.substring(12, 16), 16) + "";
			String transducerData = msg.substring(16, 20);
			double analysisData = 0;
			int status = 0;
			// 根据类型的不同,解析不同的数据
			switch (transducerType) {
			case "0001":
				analysisType = "温度传感器";
				// 解析温度数据
				if (Integer.parseInt(transducerData.substring(0, 1)) == 0) {
					analysisData = (double) Integer.parseInt(transducerData.substring(2, 4), 16);
				} else {
					analysisData = Integer.parseInt(transducerData.substring(2, 4), 16) * (-1);
				}
				break;

			case "0002":
				analysisType = "湿度传感器";
				// 解析湿度数据
				analysisData = (double) Integer.parseInt(transducerData, 16);
				break;
			default:
				break;
			}
			// 获得传感器配置对象
			TransducerDataConf transducerDataConf = transducerDataConfService.getTransducerDataConf(boardId,
					analysisType, transducerId);
			// 从redis中取出对应的时间
			String time = jedisClient.get("time:" + boardId + ":" + transducerType + ":" + transducerId);
			// 判断是否在容错范围内
			if ((transducerDataConf.getTransducerLevel() - transducerDataConf.getTransducerErrornum()) <= analysisData
					&& (transducerDataConf.getTransducerLevel()
							+ transducerDataConf.getTransducerErrornum()) >= analysisData) {
				// 判断时间是否为空
				if (time == null || time.equals("")) {
					// 创建一个data对象
					Data data = new Data();
					// 封装对象
					data.setBoardId(boardId);
					data.setTransducerId(transducerId);
					data.setTransducerType(analysisType);
					data.setTransducerData((analysisData));
					data.setDataTime(new Date());
					// 执行添加操作
					dataDao.addData(data);
					// 创建传感器对象
					Transducer transducer = new Transducer();
					// 封装传感器对象
					transducer.setApplicationFlag(transducerDataConf.getApplicationFlag());
					transducer.setBoardId(boardId);
					transducer.setTransducerType(analysisType);
					transducer.setTransducerId(transducerId);
					transducer.setTransducerStatus(status);
					transducer.setTransducerNowdata(analysisData);
					transducer.setTransducerTime(new Date());
					// 更新传感器
					transducerDao.updateTransducer(transducer);
					// 更新板
					boardDao.updateBoard(boardId, status,new Date());
					// 在redis中设置一个时间标识,并设置过期时间
					jedisClient.set("time:" + boardId + ":" + transducerType + ":" + transducerId, "time");
					jedisClient.expire("time:" + boardId + ":" + transducerType + ":" + transducerId,
							(transducerDataConf.getTransducerWarntime().intValue()*60));
				}

			} else {
				// 判断是否过高
				if (transducerDataConf.getTransducerMax() <= analysisData) {
					PushMsg.testSendPush(analysisType.substring(0, 2) + "过高,警报",transducerDataConf.getApplicationFlag());
					System.out.println(analysisType.substring(0, 2) + "过高,警报");
					status = 2;
				}
				// 判断是否过低
				if (transducerDataConf.getTransducerMin() >= analysisData) {
					PushMsg.testSendPush(analysisType.substring(0, 2) + "过低,警报", transducerDataConf.getApplicationFlag());
					System.out.println(analysisType.substring(0, 2) + "过低,警报");
					status = 1;
				}

				// 创建一个data对象
				Data data = new Data();
				// 封装对象
				data.setBoardId(boardId);
				data.setTransducerId(transducerId);
				data.setTransducerType(analysisType);
				data.setTransducerData((analysisData));
				data.setDataTime(new Date());
				// 执行添加操作
				dataDao.addData(data);
				// 创建传感器对象
				Transducer transducer = new Transducer();
				// 封装传感器对象
				transducer.setApplicationFlag(transducerDataConf.getApplicationFlag());
				transducer.setBoardId(boardId);
				transducer.setTransducerType(analysisType);
				transducer.setTransducerId(transducerId);
				transducer.setTransducerStatus(status);
				transducer.setTransducerNowdata(analysisData);
				transducer.setTransducerTime(new Date());
				// 更新传感器
				transducerDao.updateTransducer(transducer);
				// 更新板
				boardDao.updateBoard(boardId, status,new Date());
				// 判断时间标识是否为空
				if (time == null || time.equals("")) {
					// 空则在redis中设置时间标识,并设置过期时间
					jedisClient.set("time:" + boardId + ":" + transducerType + ":" + transducerId, "time");
					jedisClient.expire("time:" + boardId + ":" + transducerType + ":" + transducerId,
							(transducerDataConf.getTransducerWarntime().intValue()*60));
				} else {
					// 重置过期时间
					jedisClient.expire("time:" + boardId + ":" + transducerType + ":" + transducerId,
							transducerDataConf.getTransducerWarntime().intValue()*60);
				}

			}
			// 事务提交
			DruidUtil.commit();
		} catch (Exception e) {
			// 事务回滚
			DruidUtil.rollback();
		} finally {
			// 事务关闭
			DruidUtil.close();
		}

	}

}
