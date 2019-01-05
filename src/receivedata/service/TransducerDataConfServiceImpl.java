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
			//从redis中取传感器配置
			String json = jedisClient.hget("TransducerDataConf",boardId+":"+transducerType+":"+transducerId);
			//将取出的json转为传感器配置对象
			//判读是否为空
			if(json == null || json.equals("")) {
				//从数据库中取传感器配置对象
				transducerDataConf = transducerDataConfDao.getTransducerDataConf(boardId,transducerType,transducerId);
				//写入到redis中去
				jedisClient.hset("TransducerDataConf",boardId+":"+transducerType+":"+transducerId, JsonUtils.objectToJson(transducerDataConf));
			}else {
				transducerDataConf = JsonUtils.jsonToPojo(json, TransducerDataConf.class);
			}
			
		} catch (Exception e) {
			
		}
		//返回结果
		return transducerDataConf;
		
	}

}
