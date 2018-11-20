import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class Search extends BaseFrame{
	JPanel jP[]=new JPanel[2];
	JButton jB[]=new JButton[2];
	JTextField id[]=new JTextField[4], pw[]=new JTextField[5];
	
	public Search() {
		// TODO Auto-generated constructor stub
		super("계정찾기", new BorderLayout(), 420, 400, 2);
		setDesign();
		setComp();
		setAction();
		setVisible(true);
	}
	
	void setAction() {
		for(int i=0; i<2; i++) {
			jB[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if(arg0.getSource().equals(jB[0])) {
						try {
							ResultSet rs=DB.stmt.executeQuery("select * from member where member_name='"+id[0].getText()+"' and phone='"+id[1].getText()+"-"+id[2].getText()+"-"+id[3].getText()+"'");
							if(rs.next()) {
								PhoneCheck phone=new PhoneCheck();
								if(phone.getCheck()) {
									info_msg(rs.getString(4)+"님의 아이디는 "+rs.getString(2)+"입니다.");
									for(int i=0; i<id.length; i++)
										id[i].setText("");
									pw[1].setText(rs.getString(2));
								}
							}else
								err_msg("회원정보가 틀렸습니다.");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else {
						try {
							ResultSet rs=DB.stmt.executeQuery("select * from member where member_name='"+pw[0].getText()+"' and phone='"+pw[2].getText()+"-"+pw[3].getText()+"-"+pw[4].getText()+"' and id='"+pw[1].getText()+"'");
							if(rs.next()) {
								PhoneCheck phone=new PhoneCheck();
								if(phone.getCheck()) {
									JTextField text=new JTextField();
									if(con_msg(inputJP("패스워드 변경 :", text), "패스워드 변경")) {
										if(text.getText().matches(".*[^a-zA-Z0-9!@\\^].*") || !text.getText().matches(".*[a-zA-Z].*") || !text.getText().matches(".*[0-9].*") || !text.getText().matches(".*[!@\\^].*") || !text.getText().matches(".{6,10}")) 
											err_msg("패스워드 형식이 다릅니다.");
										else {
											DB.execute("update member set pw='"+text.getText()+"' where id='"+pw[1].getText()+"'");
											info_msg("패스워드가 변경되었습니다.");
											dispose();
										}											
									}

								}
							}else
								err_msg("회원정보가 틀렸습니다.");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}
	}
	
	void setComp() {
		JPanel center[]=new JPanel[2];
		for(int i=0; i<2; i++) {
			jP[i].add(center[i]=new JPanel(new GridLayout(0, 1)));
			center[i].setBorder(new EmptyBorder(10, 10, 10, 10));
		}
		for(int i=0; i<2; i++) {
			JPanel inJP=new JPanel(new FlowLayout(FlowLayout.LEFT));
			String label[]= {"이름", "전화번호"};
			JLabel jL;
			center[0].add(inJP);
			inJP.add(jL=fontJL(label[i], 15));
			jL.setHorizontalAlignment(JLabel.LEFT);
			size(jL, 80, 25);
			if(i==0)
				inJP.add(id[0]);
			else 
				addcom(inJP, id[1], new JLabel("-"), id[2], new JLabel("-"), id[3]);			
		}
		
		for(int i=0; i<3; i++) {
			JPanel inJP=new JPanel(new FlowLayout(FlowLayout.LEFT));
			String label[]= {"이름", "아이디", "전화번호"};
			JLabel jL;
			center[1].add(inJP);
			inJP.add(jL=fontJL(label[i], 15));
			jL.setHorizontalAlignment(JLabel.LEFT);
			size(jL, 80, 25);
			if(i<=1)
				inJP.add(pw[i]);
			else 
				addcom(inJP, pw[2], new JLabel("-"), pw[3], new JLabel("-"), pw[4]);
		}
	}
	
	void setDesign() {
		((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
		add(jP[0]=new JPanel(new BorderLayout()), BorderLayout.NORTH);
		add(jP[1]=new JPanel(new BorderLayout()));
		
		String label[]= {"아이디 찾기", "패스워드 찾기"};
		for(int i=0; i<2; i++) {
			jP[i].setBorder(new TitledBorder(label[i]));
			jP[i].add(jB[i]=new JButton(label[i]), BorderLayout.SOUTH);
		}
		
		for(int i=0; i<4; i++) {
			id[i]=new JTextField(i==0?22:i==1?4:7);
			size(id[i], 20, 30);
		}
		for(int i=0; i<5; i++) {
			pw[i]=new JTextField(i<=1?22:i==2?4:7);
			size(pw[i], 20, 30);
		}
	}
}
