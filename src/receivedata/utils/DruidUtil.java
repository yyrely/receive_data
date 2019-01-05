package receivedata.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;


public class DruidUtil {
	
	private static DruidDataSource dataSource = null;
	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
	 
	static {
		dataSource = new DruidDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql:///info_collect?useUnicode=true&characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("admin");
	}
	
	
	public static Connection getConnection() {

		try {
			Connection conn = threadLocal.get();

			if (conn == null) {
				// ������Դ�л�ȡ���ݿ�����
				conn = getDataSource().getConnection();
				// ��conn�󶨵���ǰ�߳�
				threadLocal.set(conn);
			}
			return conn;
			
		} catch (SQLException e) {
			throw new RuntimeException("������æ.....");
		}
	}
	
	public static void startTransaction() {
		try {
			
			Connection conn = threadLocal.get();
			if(conn == null) {
				conn = getConnection();
				threadLocal.set(conn);
			}
			
			conn.setAutoCommit(false);
		} catch (Exception e) {
			 throw new RuntimeException(e);
		}
		
	}
	
	public static void rollback() {
		try {

			Connection conn = threadLocal.get();
			if (conn == null) {
				conn = getConnection();
				threadLocal.set(conn);
			}

			conn.rollback();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void commit() {
		try {

			Connection conn = threadLocal.get();
			if (conn == null) {
				conn = getConnection();
				threadLocal.set(conn);
			}

			conn.commit();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void close(){
        try{
            //�ӵ�ǰ�߳��л�ȡConnection
            Connection conn = threadLocal.get();
            if(conn!=null){
                conn.close();
                 //�����ǰ�߳��ϰ�conn
                threadLocal.remove();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	public static DataSource getDataSource() {
		return dataSource;
	}
	


	
}









