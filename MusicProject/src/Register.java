import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Register extends BaseFrame{
	JPanel jP[]=new JPanel[2];
	JTextField[] text=new JTextField[9];
	JComboBox month=new JComboBox<>();
	JRadioButton gender[]=new JRadioButton[2];
	JButton jB=new JButton("����");
	String err="";
	public Register() {
		// TODO Auto-generated constructor stub
		super("ȸ������", new BorderLayout(), 440, 420, 2);
		((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
		setComp();
		setAction();
		setVisible(true);
	}
	
	void setAction() {
		for(int i=0; i<text.length; i++) {
			text[i].addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					err="";
					for(int i=0; i<text.length; i++) {
						text[i].setForeground(Color.blue);
						if(text[i].getText().length()==0) 
							err="���� ���� �Է����ּ���.";
					}
					if(text[8].getText().length()!=4) {
						err="��ȭ��ȣ �߰�, ���ڸ��� 4�ڸ� �����Դϴ�.";
						text[8].setForeground(Color.red);
					}
					if(text[7].getText().length()!=4) {
						err="��ȭ��ȣ �߰�, ���ڸ��� 4�ڸ� �����Դϴ�.";
						text[7].setForeground(Color.red);
					}
					if(text[6].getText().length()!=3) {
						err="��ȭ��ȣ ���ڸ��� 3�ڸ� �����Դϴ�.";
						text[6].setForeground(Color.red);
					}
					try {
						ResultSet rs=DB.stmt.executeQuery("select * from member where phone='"+text[6].getText()+"-"+text[7].getText()+"-"+text[8].getText()+"'");
						if(rs.next()) {
							err="�ߺ� ��ȭ��ȣ�� �����մϴ�.";
							for(int i=6; i<=8; i++)
								text[i].setForeground(Color.red);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(!isdate(text[4].getText()+"-"+month.getSelectedItem()+"-"+text[5].getText())) {
						err="��¥�� Ȯ�����ּ���.";
						text[4].setForeground(Color.red);
						text[5].setForeground(Color.red);
					}
					if(toint(text[4].getText())<1900 || toint(text[4].getText())>LocalDate.now().getYear()-15) {
						text[4].setForeground(Color.red);
						err="������ 1900~(����⵵ - 15)�� �Է����ּ���.";
					}
					if(text[3].getText().length()>10) {
						err="�̸��� 10�ڸ� ���Ϸ� �Է����ּ���.";
						text[3].setForeground(Color.red);
					}
					if(!text[2].getText().equals(text[1].getText())) {
						text[2].setForeground(Color.red);
						err="�н������ �����ϰ� �Է����ּ���.";
					}
					if(text[1].getText().matches(".*[^a-zA-Z0-9!@\\^].*") || !text[1].getText().matches(".*[a-zA-Z].*") || !text[1].getText().matches(".*[0-9].*") || !text[1].getText().matches(".*[!@\\^].*") || !text[1].getText().matches(".{6,10}")) {
						text[1].setForeground(Color.red);
						err="�н������ ����, ����, Ư������(!,@,^)�� �����ϴ� 6~10�ڸ��� �Է����ּ���.";
					}
					if(!text[0].getText().matches(".{3,10}")) {
						err="���̵�� 3~10�ڸ��� �Է����ּ���.";
						text[0].setForeground(Color.red);
					}
					try {
						ResultSet rs=DB.stmt.executeQuery("select * from member where id='"+text[0].getText()+"'");
						if(rs.next()) {
							text[0].setForeground(Color.red);
							err="���̵� �ߺ��Ǿ����ϴ�.";
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		
		jB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(err!="") {
					err_msg(err);
					return;
				}
				String gen="";
				if(gender[0].isSelected())
					gen="����";
				else
					gen="����";
				if(gen=="") {
					err_msg("������ �������ּ���.");
					return;
				}
				PhoneCheck phone=new PhoneCheck();
				if(phone.getCheck()) {
					info_msg("���ԵǾ����ϴ�.");
					DB.execute("insert into member(id, pw, member_name, birthday, gender, phone, membership, point, payment) values('"+text[0].getText()+"', '"+text[1].getText()+"', '"+text[3].getText()+"', '"+text[4].getText()+"-"+month.getSelectedItem()+"-"+text[5].getText()+"', '"+gen+"', '"+text[6].getText()+"-"+text[7].getText()+"-"+text[8].getText()+"', '�Ϲ�', 0, 0)");
					dispose();
				}
			}
		});
	}
	
	static boolean isdate(String date) {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		df.setLenient(false);
		try {
			df.parse(date);
			return true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	void setComp() {
		add(jP[0]=new JPanel(new GridLayout(0, 1)));
		
		String label[]= {"���̵�", "�н�����", "�н����� Ȯ��", "�̸�", "�������", "����", "��ȭ��ȣ"};
		int w=23;
		for(int i=1; i<=12; i++)
			month.addItem(i);
		for(int i=0; i<label.length; i++) {
			JPanel tempJP=new JPanel(), inJP=new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel jL=fontJL(label[i], 15);
			
			jP[0].add(tempJP);
			tempJP.add(jL);
			size(jL, 120, 25);
			tempJP.add(inJP);
			size(inJP, 270, 40);
			jL.setHorizontalAlignment(JLabel.LEFT);
			
			if(i<=3)
				inJP.add(text[i]=new JTextField(w));
			if(i==4) 
				addcom(inJP, text[4]=new JTextField(5), new JLabel("��"), month, new JLabel("��"), text[5]=new JTextField(3), new JLabel("��"));

			if(i==5) {
				addcom(inJP, gender[0]=new JRadioButton("��"), gender[1]=new JRadioButton("��"));
				inJP.setLayout(new FlowLayout());
			}
			if(i==6) 
				addcom(inJP, text[6]=new JTextField(5), new JLabel("-"), text[7]=new JTextField(7), new JLabel("-"), text[8]=new JTextField(7));	
		}
		for(int i=0; i<text.length; i++)
			size(text[i], 20, 30);
		add(jP[1]=new JPanel(new GridLayout(0, 1)), BorderLayout.SOUTH);
		jP[1].add(jB=new JButton("����"));
	}
}
