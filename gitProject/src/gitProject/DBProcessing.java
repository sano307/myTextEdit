package gitProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBProcessing {
	String driver 	 = "org.mariadb.jdbc.Driver";
	String url	  	 = "jdbc:mariadb://localhost:3306/test";
	String user 	 = "root";
	String password  = "1234";
	String tableName = "member";
	boolean resultValue;
	
	Connection db_conn;
	PreparedStatement pstmp;
	ResultSet rs;
	
	public DBProcessing() {
		createMemberTable();
	}
	
	public void DBConnection() {
		try {
			Class.forName(driver);
			db_conn = DriverManager.getConnection(url, user, password);
			
			if ( db_conn != null ) System.out.println("DB 연결 성공!");
			
		} catch (ClassNotFoundException CNFe) {
			System.out.println("드라이버 로드 실패!");
		} catch (SQLException SQLe) {
			System.out.println("DB 연결 실패!");
		}
	}
	
	public void DBDisConnection() {
		try {
			db_conn.close();
			System.out.println("DB 연결 해제 성공!");
		} catch (SQLException SQLe) {
			System.out.println("DB 연결 해제 실패!");
		}
	}
	
	public void createMemberTable() {
		DBConnection();
		
		String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
		sql += "m_num int(10) auto_increment primary key, ";
		sql += "m_id char(30) unique not null, ";
		sql += "m_pass varchar(30) not null";
		sql += ")";
		
		try {
			pstmp = db_conn.prepareStatement(sql);
			rs    = pstmp.executeQuery();

			if ( rs.isAfterLast() ) {
				System.out.println(tableName + " Table 생성 성공!");
			}
		} catch (SQLException e) {
			System.out.println("쿼리 수행 실패!");
		}
		
		DBDisConnection();
	}
	
	public boolean isMemberCheck(String argID) {
		DBConnection();
		
		String sql = "SELECT * FROM member WHERE m_id = ?";
		
		try {
			pstmp = db_conn.prepareStatement(sql);
			pstmp.setString(1, argID);
			rs    = pstmp.executeQuery();
			resultValue = rs.next();
		} catch (SQLException e) {
			System.out.println("쿼리 수행 실패!");
		}
		
		DBDisConnection();
		return resultValue;
	}
	
	public boolean isMemberCheck(String argID, String argPass) {
		DBConnection();
		
		String sql = "SELECT * FROM member WHERE m_id = ? and m_pass = ?";

		try {
			pstmp = db_conn.prepareStatement(sql);
			pstmp.setString(1, argID);
			pstmp.setString(2, argPass);
			rs    = pstmp.executeQuery();
			resultValue = rs.next();
		} catch (SQLException e) {
			System.out.println("쿼리 수행 실패!");
		}
		
		DBDisConnection();
		return resultValue;
	}
	
	public boolean insertMemberInfo(String argID, String argPass) {
		DBConnection();

		String sql = "INSERT INTO member (m_id, m_pass) VALUES (?, ?)";
		
		try {
			pstmp = db_conn.prepareStatement(sql);
			pstmp.setString(1, argID);
			pstmp.setString(2, argPass);
			pstmp.executeUpdate();
			resultValue = true;
		} catch (SQLException e) {
			resultValue = false;
		}
		
		DBDisConnection();
		return resultValue;
	}
}
