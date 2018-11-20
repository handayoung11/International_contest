import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	static Statement stmt;
	static Connection con;
	static String ID="";
	static String GRADE="";
	DB(){
		try {
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306", "user", "1234");
			stmt=con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		DB db=new DB();
		db.createDB();
		new MainFrame().setVisible(true);
	}
	
	void createDB() {
		execute("use musicmarket");
	}
	
	static void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
