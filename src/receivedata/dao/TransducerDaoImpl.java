package receivedata.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import receivedata.pojo.Transducer;
import receivedata.utils.DruidUtil;

public class TransducerDaoImpl implements TransducerDao {

	@Override
	public void updateTransducer(Transducer transducer) throws SQLException {
		QueryRunner qr = new QueryRunner();
		String sql = "update transducer "
				   + "set transducer_status = ? ,transducer_nowdata = ? ,transducer_time = ?"
				   + "where application_flag = ? and board_id = ? and transducer_type = ? and transducer_id = ?";
		qr.update(DruidUtil.getConnection(), sql, transducer.getTransducerStatus(),transducer.getTransducerNowdata(),transducer.getTransducerTime(),transducer.getApplicationFlag(),transducer.getBoardId(),transducer.getTransducerType(),transducer.getTransducerId());
		
	}

}
