package receivedata.dao;

import java.sql.SQLException;
import java.util.Date;

public interface BoardDao {

	void updateBoard(String boardId, Integer status, Date date) throws SQLException;

}
