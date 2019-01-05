package receivedata.dao;

import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.dbutils.QueryRunner;

import receivedata.utils.DruidUtil;

public class BoardDaoImpl implements BoardDao {

	@Override
	public void updateBoard(String boardId, Integer status,Date date) throws SQLException {
		QueryRunner query = new QueryRunner();
		String sql = "update board set board_status = ?,board_time = ?  where board_id = ?";
		query.update(DruidUtil.getConnection(),sql,status,date,boardId);
	}

}
