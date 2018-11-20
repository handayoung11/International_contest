import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MainFrame extends BaseFrame{
	JPanel jP[]=new JPanel[2], inJP[]=new JPanel[2], center=new JPanel(new GridLayout(0, 1)), south=new JPanel(new GridLayout(0, 1, 0, 5));
	JLabel rank[]=new JLabel[6];
	JButton jB[]=new JButton[4], lJB[]=new JButton[3];
	JTextField id=new JTextField();
	JPasswordField pw=new JPasswordField();
	static JLabel PJL=new JLabel(), GJL=new JLabel();
	static int POINT=-1;
	JFrame thisForm=this;

	public MainFrame() {
		// TODO Auto-generated constructor stub
		super("메인", new BorderLayout(), 450, 400, JFrame.DO_NOTHING_ON_CLOSE);
		setDesign();
		setAction();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				err_msg("메인 'X'버튼을 사용하실 수 없습니다.");
			}
		});
	}

	void setAction() {
		for(int i=0; i<jB.length; i++) {
			jB[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if(arg0.getSource().equals(jB[0])) 
						new SongList().addWindowListener(new Before(thisForm));
					if(arg0.getSource().equals(jB[1]))
						new TopChart().addWindowListener(new Before(thisForm));
					if(arg0.getSource().equals(jB[2])) {
						if(POINT==-1) {
							err_msg("로그인 후 이용해주세요.");
							return;
						}
						new MyPage().addWindowListener(new Before(thisForm));
					}
					if(arg0.getSource().equals(jB[3])){
						if(con_msg("종료하시겠습니까?", "종료"))
								System.exit(2);
						else
							return;
					}
					setVisible(false);
				}
			});
		}

		for(int i=0; i<lJB.length; i++) {
			lJB[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if(arg0.getSource().equals(lJB[0])) {
						if(id.getText().equals("") || pw.getText().equals("")) {
							err_msg("아이디 혹은 패스워드를 입력해주세요.");
							return;
						}
						try {
							ResultSet rs=DB.stmt.executeQuery("select * from member where id='"+id.getText()+"' and pw='"+pw.getText()+"'");
							if(rs.next()) {
								GJL.setText("등급 : "+rs.getString(8));
								PJL.setText("잔여포인트 : "+rs.getString(9));
								POINT=rs.getInt(9);
								DB.GRADE=rs.getString(8);
								DB.ID=id.getText();
								setLogin(rs.getString(4));
							}else 								
								err_msg("회원정보가 일치하지 않습니다.");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					}
					if(arg0.getSource().equals(lJB[1]))
						new Register().addWindowListener(new Before(thisForm));
					if(arg0.getSource().equals(lJB[2])) 
						new Search().addWindowListener(new Before(thisForm));
					setVisible(false);
				}
			});
		}
	}

	void setDesign() {
		add(jP[0]=new JPanel(new BorderLayout()), BorderLayout.NORTH);
		add(jP[1]=new JPanel(new GridLayout(0, 2, 0, 10)));

		jP[1].add(inJP[0]=new JPanel(new BorderLayout()));
		jP[1].add(inJP[1]=new JPanel(new BorderLayout()));

		inJP[0].setBorder(new EmptyBorder(0, 15, 15, 5));
		inJP[1].setBorder(new EmptyBorder(0, 10, 5, 5));

		String label[]= {"로그인", "회원가입", "아이디/패스워드 찾기"};
		for(int i=0; i<lJB.length; i++)
			lJB[i]=new JButton(label[i]);
		setNorth();
		setLogout();
		MyThread th=new MyThread();
		th.start();
	}

	void setLogin(String name) {
		center.removeAll();
		south.removeAll();
		JButton logout;
		size(south, 20, 120);
		center.add(new JLabel(name + " 고객님"));
		center.add(GJL);
		center.add(PJL);		
		center.add(logout=new JButton("로그아웃"));
		logout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				setLogout();
			}
		});
		repaint();
		revalidate();
	}

	void setLogout() {
		POINT=-1;
		DB.ID="";
		
		id.setText("");
		pw.setText("");
		center.removeAll();
		south.removeAll();

		inJP[1].add(center);
		inJP[1].add(south, BorderLayout.SOUTH);
		size(south, 20, 100);

		addcom(center, new JLabel("아이디"), id, new JLabel("패스워드"), pw);
		for(int i=0; i<lJB.length; i++)
			south.add(lJB[i]);
		repaint();
		revalidate();
	}

	void setNorth() {
		JPanel south=new JPanel();
		jP[0].add(fontJL("Music Market", 35));
		jP[0].setBackground(Color.yellow);
		jP[0].add(south, BorderLayout.SOUTH);
		String label[]= {"노래목록", "인기차트", "마이페이지", "시스템 종료"};
		for(int i=0; i<label.length; i++)
			south.add(jB[i]=new JButton(label[i]));
	}

	class MyThread extends Thread{
		MyThread(){
			JPanel north=new JPanel(new BorderLayout()), center=new JPanel(), east=new JPanel(new GridLayout(0, 3));
			inJP[0].add(north, BorderLayout.NORTH);
			size(east, 70, 50);
			north.add(fontJL("인기차트", "HY견고딕", 25));
			north.add(east, BorderLayout.EAST);
			for(int i=0; i<6; i++) {
				east.add(rank[i]=new JLabel(i+1+"", JLabel.CENTER));
				rank[i].setBorder(new LineBorder(Color.black, 2));
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int cnt;
			JLabel imgJL;
			inJP[0].add(imgJL=new JLabel());
			while(true) {
				cnt=0;
				try {
					Statement stmt=DB.con.createStatement();
					ResultSet rs=stmt.executeQuery("select * from music order by album desc, music_no asc limit 6");
					while(rs.next()) {
						for(int i=0; i<6; i++) {
							rank[i].setBorder(new LineBorder(Color.black, 2));
							rank[i].setForeground(Color.black);
						}
						rank[cnt].setForeground(Color.red);
						rank[cnt].setBorder(new LineBorder(Color.red, 2));
						imgJL.setIcon(insert_Image("./2과제지급자료/이미지 지급자료/앨범 사진/"+rs.getString(2)+".jpg", 200, 200));
						cnt++;
						Thread.sleep(3000);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
