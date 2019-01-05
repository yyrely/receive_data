package receivedata.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;

import receivedata.pojo.TransducerDataConf;
import receivedata.utils.DruidUtil;

public class TransducerDataConfDaoImpl implements TransducerDataConfDao {

	@Override
	@Test
	public TransducerDataConf getTransducerDataConf(String boardId, String transducerType, String transducerId) throws SQLException {
		QueryRunner query = new QueryRunner();
		String sql = "select application_flag applicationFlag,transducer_max transducerMax,transducer_min transducerMin,transducer_errornum transducerErrornum,transducer_level transducerLevel,transducer_warntime transducerWarntime from transducer_data_conf where board_id = ? and transducer_type = ? and transducer_id = ?";
		TransducerDataConf transducerDataConf = query.query(DruidUtil.getConnection(), sql, new BeanHandler<>(TransducerDataConf.class),boardId,transducerType,transducerId);
		return transducerDataConf;
	}

}
