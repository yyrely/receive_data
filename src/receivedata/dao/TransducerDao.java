package receivedata.dao;

import java.sql.SQLException;

import receivedata.pojo.Transducer;

public interface TransducerDao {
	
	void updateTransducer(Transducer transducer) throws SQLException;

}
