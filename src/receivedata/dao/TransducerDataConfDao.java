package receivedata.dao;

import java.sql.SQLException;

import receivedata.pojo.TransducerDataConf;

public interface TransducerDataConfDao {

	TransducerDataConf getTransducerDataConf(String boardId, String transducerType,
			String transducerId) throws SQLException;

}
