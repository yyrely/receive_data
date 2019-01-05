package receivedata.dao;

import java.sql.SQLException;

import receivedata.pojo.Data;

public interface DataDao {
	
	void addData(Data data) throws SQLException;
}
