package receivedata.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import receivedata.pojo.Data;
import receivedata.utils.DruidUtil;

public class DataDaoImpl implements DataDao{
	@Override
	public void addData(Data data) throws SQLException {
		QueryRunner qr = new QueryRunner();
		qr.update(DruidUtil.getConnection(),"insert into data(board_id,transducer_id,transducer_type,transducer_data,data_time) values(?,?,?,?,?)",data.getBoardId(),data.getTransducerId(),data.getTransducerType(),data.getTransducerData(),data.getDataTime());
	}
}
