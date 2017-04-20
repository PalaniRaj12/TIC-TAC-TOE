package DBUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil 
{
	public static Connection getDBConnection()
	{ 
		Connection cn=null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","system","hr");
			 //System.out.println("connected");
			}
		catch (ClassNotFoundException | SQLException e) {
		
			e.printStackTrace();
				//System.out.println("not");
		}
	  
		return cn;
}
}