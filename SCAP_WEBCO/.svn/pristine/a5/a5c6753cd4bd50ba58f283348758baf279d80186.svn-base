package nc.scap.pub.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import nc.uap.portal.log.ScapLogger;

public class CpDbfConnection {
	
	private static final Map<String, CpDbfConnection> instanceMap = new HashMap<String, CpDbfConnection>();
	private String sourceDB;
	private Connection connection;

	private CpDbfConnection(String sourceDB) {
		this.sourceDB = sourceDB;
		buildConnection();
	}
	
	private void buildConnection() {
		connection = null;
		try {
			StringBuilder sb =new StringBuilder();
			sb.append("jdbc:odbc:driver={Microsoft Visual FoxPro Driver};SourceType=DBF;SourceDB=" + sourceDB);
			String url = sb.toString();
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Properties prop = new Properties();
			prop.put("charSet", "gb2312");
			connection = DriverManager.getConnection(url, prop);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		try {
			if(connection == null) {
				return null;
			}
			if(connection.isClosed()) {
				buildConnection();
			}
		} catch (SQLException e) {
			ScapLogger.error("===error:CpDbfConnection.getConnection 根据SourceId:" + sourceDB + "创建DBF连接失败！", e);
		}
		return connection;
	}

	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static CpDbfConnection getInstance(String sourceDB) {
		if(instanceMap.get(sourceDB) == null || instanceMap.get(sourceDB).getConnection() == null) {
			instanceMap.put(sourceDB, new CpDbfConnection(sourceDB));
		}
		return instanceMap.get(sourceDB);
	}

}
