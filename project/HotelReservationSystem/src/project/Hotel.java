package project;
import java.sql.*;
public class Hotel {

	public static void main(String[] args) throws SQLException {
		String url="jdbc:mysql://localhost:3306/hotel_reservation";
		String username="root";
		String password="";
		
		Connection con= DriverManager.getConnection(url, username , password);
	
		String sql = "SELECT * FROM ROOMS";
		PreparedStatement st = con.prepareStatement(sql);
		ResultSet rs=st.executeQuery(sql);
		while (rs.next()) {
	System.out.println(rs.getInt(1)+" "+rs.getInt(2)+" "+rs.getString(3));
		
		}

	}

}
