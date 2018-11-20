import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	static Statement stmt;
	static Connection con;
	
	DB(){
		try {
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "1234");
			stmt=con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		DB db=new DB();
		db.createDB();
		db.createTable("member", "member_no int primary key not null auto_increment, id varchar(10), pw varchar(10), member_name varchar(10), birthday date, gender varchar(4), phone varchar(20), membership varchar(8), point int, payment int");
		db.createTable("music", "music_no int primary key not null auto_increment, title varchar(30), singer varchar(20), genre varchar(10), date date, album int");
		db.createTable("rating", "music_r int not null, member_r varchar(10), kinds int, rating varchar(30)");
	}
	
	void createTable(String table, String column) {
		execute("create table "+table+"("+column+")");
		execute("load data local infile './2과제지급자료/텍스트 지급자료/"+table+".txt' into table "+table+" ignore 1 lines");
	}
	
	void createDB() {
		execute("drop database if exists musicmarket");
		execute("create database musicmarket");
		execute("use musicmarket");
		execute("drop user if exists user@localhost");
		execute("create user user@localhost identified by '1234'");
		execute("grant select, insert, delete, update on musicmarket.* to user@localhost");
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
