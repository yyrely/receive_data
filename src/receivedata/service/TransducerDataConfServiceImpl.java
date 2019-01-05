package receivedata.service;

import receivedata.dao.TransducerDataConfDao;
import receivedata.dao.TransducerDataConfDaoImpl;
import receivedata.pojo.TransducerDataConf;
import receivedata.utils.JsonUtils;
import receivedata.utils.jedis.JedisClient;
import receivedata.utils.jedis.JedisClientPool;

public class TransducerDataConfServiceImpl implements TransducerDataConfService{

	private JedisClient jedisClient = new JedisClientPool();
	
	private TransducerDataConfDao transducerDataConfDao = new TransducerDataConfDaoImpl();
	
	@Override
	public TransducerDataConf getTransducerDataConf(String boardId, String transducerType, String transducerId) {
		
		TransducerDataConf transducerDataConf = null;
		
		try {
			//��redis��ȡ����������
			String json = jedisClient.hget("TransducerDataConf",boardId+":"+transducerType+":"+transducerId);
			//��ȡ����jsonתΪ���������ö���
			//�ж��Ƿ�Ϊ��
			if(json == null || json.equals("")) {
				//�����ݿ���ȡ���������ö���
				transducerDataConf = transducerDataConfDao.getTransducerDataConf(boardId,transducerType,transducerId);
				//д�뵽redis��ȥ
				jedisClient.hset("TransducerDataConf",boardId+":"+transducerType+":"+transducerId, JsonUtils.objectToJson(transducerDataConf));
			}else {
				transducerDataConf = JsonUtils.jsonToPojo(json, TransducerDataConf.class);
			}
			
		} catch (Exception e) {
			
		}
		//���ؽ��
		return transducerDataConf;
		
	}

}
