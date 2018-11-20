import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Pay extends BaseFrame{
	JPanel jP[]=new JPanel[2];
	JComboBox month=new JComboBox<>();
	JTextField text[]=new JTextField[6];
	JPasswordField pw=new JPasswordField(18);
	BufferedImage buffer=new BufferedImage(150, 150, Image.SCALE_SMOOTH);
	JButton jB=new JButton("결제");
	Brush brush=new Brush();
	
	public Pay() {
		// TODO Auto-generated constructor stub
		super("결제", new BorderLayout(), 450, 210, 2);
		setDesign();
		setAction();
		setVisible(true);
	}
	
	String rise_membership(String grade) {
		if(grade.equals("일반"))
			return "VIP";
		else
			return "VVIP";
	}
	
	void setAction() {
		jB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					ResultSet rs=DB.stmt.executeQuery("select * from member where id='"+text[0].getText()+"' and pw='"+pw.getText()+"' and birthday='"+text[1].getText()+"-"+month.getSelectedItem()+"-"+text[2].getText()+"' and phone='"+text[3].getText()+"-"+text[4].getText()+"-"+text[5].getText()+"'");
					if(rs.next()) {
						ImageIO.write(buffer, "JPG", new File("./2과제지급자료/이미지 지급자료/결제 사인/"+DB.ID+" "+LocalDate.now()+".jpg"));
						
						DB.GRADE=rise_membership(DB.GRADE);
						int point=getPoint(DB.GRADE);
						DB.execute("update member set point="+point+", payment=payment+1000, membership='"+DB.GRADE+"' where id='"+text[0].getText()+"'");
						MainFrame.GJL.setText("등급 : "+DB.GRADE);
						MainFrame.PJL.setText("잔여포인트 : "+point);
						info_msg("결제되었습니다.");
						dispose();
					}else
						err_msg("회원님의 정보가 일치하지 않습니다.");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	private int getPoint(String grade) {
		// TODO Auto-generated method stub
		if(grade.equals("일반"))
			return 1000;
		else if(grade.equals("VIP"))
			return 1200;
		else
			return 1400;
	}
	
	void setDesign() {
		add(jP[0]=new JPanel(new GridLayout(0, 1)));
		add(jP[1]=new JPanel(new BorderLayout()), BorderLayout.EAST);
		
		jP[1].add(jB, BorderLayout.SOUTH);
		
		for(int i=1; i<=12; i++)
			month.addItem(i);
		for(int i=0; i<text.length; i++) {
			text[i]=new JTextField(i==0?18:i==3?3:i==2?3:5);
			size(text[i], 20, 25);
		}
		setUIJP();
		setSignJP();
	}
	
	void setUIJP() {
		String label[]= {"아이디", "패스워드", "생년월일", "전화번호"};
		for(int i=0; i<label.length; i++) {
			JPanel tempJP=new JPanel();
			jP[0].add(tempJP);
			JLabel jL;
			tempJP.add(jL=fontJL(label[i], 15));
			size(jL, 60, 25);
			jL.setHorizontalAlignment(JLabel.LEFT);
			if(i==0)
				tempJP.add(text[0]);
			if(i==1)
				tempJP.add(pw);
			if(i==2) 
				addcom(tempJP, text[1], new JLabel("년"), month, new JLabel("월"), text[2], new JLabel("일"));
			if(i==3)
				addcom(tempJP, text[3], new JLabel("-"), text[4], new JLabel("-"), text[5]);
		}
	}
	
	void setSignJP() {
		size(jP[1], 150, 150);
		JLabel label=new JLabel(new ImageIcon(buffer));
		
		label.setBounds(0, 0, 150, 150);
		JPanel signJP=new JPanel(null);
		jP[1].add(signJP);
		signJP.add(brush);
		signJP.add(label);
		brush.setBounds(0, 0, 150, 150);
		
		brush.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				brush.x=arg0.getX();
				brush.y=arg0.getY();
				brush.repaint();
				brush.printAll(buffer.getGraphics());
				repaint();
				revalidate();
			}
		});
	}
	
	class Brush extends JLabel{
		int x, y;
		@Override
		protected void paintComponent(java.awt.Graphics arg0) {
			arg0.setColor(Color.WHITE);
			arg0.fillOval(x-5, y-5, 15, 15);
		};
	}
}
