package receivedata.service;

import receivedata.pojo.TransducerDataConf;

public interface TransducerDataConfService {
	
	TransducerDataConf getTransducerDataConf(String boardId, String transducerType,
			String transducerId);
}
