import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MyPage extends BaseFrame{
	JTabbedPane tab=new JTabbedPane();
	JPanel jP[]=new JPanel[2];
	JTextField text[]=new JTextField[9], title=new JTextField(25);
	JComboBox month=new JComboBox<>();
	JRadioButton gender[]=new JRadioButton[2];
	JButton jB=new JButton("����"), muJB=new JButton("���");
	String phone;
	DefaultTableModel dTM[]=new DefaultTableModel[2];
	JTable jT[]=new JTable[2];
	
	public MyPage() {
		// TODO Auto-generated constructor stub
		super("����������", new GridLayout(), 450, 420, 2);
		setDesign();
		setInfo();
		setMyMusic();
		setAction();
		setVisible(true);
	}

	String err="";

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
						if(rs.next() && !phone.equals(text[6].getText()+"-"+text[7].getText()+"-"+text[8].getText())) {
							err="�ߺ� ��ȭ��ȣ�� �����մϴ�.";
							for(int i=6; i<=8; i++)
								text[i].setForeground(Color.red);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(!Register.isdate(text[4].getText()+"-"+month.getSelectedItem()+"-"+text[5].getText())) {
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
						if(rs.next() && !DB.ID.equals(text[0].getText())) {
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
		
		jT[0].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getClickCount()==2 && jT[0].getSelectedColumn()==0) {
					int row=jT[0].getSelectedRow();
					DB.execute("delete from rating where member_r='"+DB.ID+"' and music_r="+jT[0].getValueAt(row, 1)+" and kinds=0");
					dTM[0].removeRow(row);
				}
			}
		});
		
		muJB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int row=jT[1].getSelectedRow();
				if(row==-1) {
					err_msg("������ ���ټҰ��� Ŭ�����ּ���.");
					return;
				}
				
				DB.execute("update rating set rating='"+title.getText()+"' where member_r='"+DB.ID+"' and music_r="+jT[1].getValueAt(row, 0));
				info_msg("�����Ǿ����ϴ�.");
				jT[1].setValueAt(title.getText(), row, 2);
				jT[1].clearSelection();
				title.setText("");
			}
		});
		
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
					info_msg("�����Ǿ����ϴ�.");						
					DB.execute("update member set id='"+text[0].getText()+"', pw='"+text[1].getText()+"', member_name='"+text[3].getText()+"', birthday='"+text[4].getText()+"-"+month.getSelectedItem()+"-"+text[5].getText()+"', gender='"+gen+"', phone='"+text[6].getText()+"-"+text[7].getText()+"-"+text[8].getText()+"' where id='"+DB.ID+"'");
					DB.execute("update rating set member_r='"+text[0].getText()+"' where member_r='"+DB.ID+"'");
					dispose();
				}
			}
		});
	}

	void setInfo() {
		JPanel big=new JPanel(new BorderLayout());
		tab.addTab("������������", big);
		
		big.add(jP[0]=new JPanel(new GridLayout(0, 1)));
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
		big.add(jP[1]=new JPanel(new GridLayout(0, 1)), BorderLayout.SOUTH);
		jP[1].add(jB);
		
		try {
			ResultSet rs=DB.stmt.executeQuery("select * from member where id='"+DB.ID+"'");
			while(rs.next()) {
				text[0].setText(DB.ID);
				text[1].setText(rs.getString("pw"));
				text[2].setText(rs.getString("pw"));
				text[3].setText(rs.getString("member_name"));
				phone=rs.getString("phone");
				String birth[]=rs.getString("birthday").split("-"), phone[]=rs.getString("phone").split("-");
				text[4].setText(birth[0]);
				month.setSelectedItem(birth[1]);
				text[5].setText(birth[2]);
				if(rs.getString("gender").equals("����"))
					gender[0].setSelected(true);
				else
					gender[1].setSelected(true);
				text[6].setText(phone[0]);
				text[7].setText(phone[1]);
				text[8].setText(phone[2]);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		big.setBorder(new EmptyBorder(10, 10, 10, 10));
	}

	void setMyMusic() {
		JPanel big=new JPanel(new BorderLayout()), center=new JPanel(new GridLayout(0, 1)), jP[]=new JPanel[3];
		big.setBorder(new EmptyBorder(10, 10, 10, 10));
		tab.addTab("�� ����", big);
		big.add(center);
		big.add(jP[2]=new JPanel(new GridLayout(0, 1, 0, 10)), BorderLayout.SOUTH);
		
		center.add(jP[0]=new JPanel(new BorderLayout()));
		center.add(jP[1]=new JPanel(new BorderLayout()));
		
		dTM[0]=makeModel(new String[] {"",  "��ȣ", "����", "����", "�帣"}, 0);
		dTM[1]=makeModel(new String[] {"��ȣ",  "����", "���� ��"}, -1);
		
		jT[0]=makeTable(dTM[0], 0);
		jT[1]=makeTable(dTM[1], -1);
		
		jP[0].add(new JLabel("���ƿ� ����Ʈ"), BorderLayout.NORTH);
		jP[0].add(new JScrollPane(jT[0]));		
		
		jP[1].add(new JLabel("���� ����Ʈ"), BorderLayout.NORTH);
		jP[1].add(new JScrollPane(jT[1]));
		
		jP[2].add(new JLabel("������:"));
		jP[2].add(title);
		jP[2].add(muJB);
		
		addRow(dTM[0], "select m.album, r.music_r, m.title, m.singer, m.genre from rating r inner join music m on m.music_no=r.music_r where kinds=0 and member_r='"+DB.ID+"'");
		addRow(dTM[1], "select r.music_r, m.title, r.rating from rating r inner join music m on m.music_no=r.music_r where kinds=1 and member_r='"+DB.ID+"'");
		
		ImageIcon heart=insert_Image("./2���������ڷ�/�̹��� �����ڷ�/image2.png", 10, 10);
		for(int i=0; i<jT[0].getRowCount(); i++)
			jT[0].setValueAt(heart, i, 0);
		
		jT[0].getColumnModel().getColumn(0).setMaxWidth(15);
		jT[0].getColumnModel().getColumn(1).setMaxWidth(45);
		jT[0].getColumnModel().getColumn(4).setMaxWidth(70);
		
		jT[1].getColumnModel().getColumn(0).setMaxWidth(45);
		jT[1].getColumnModel().getColumn(2).setMinWidth(140);
		
	}

	void setDesign() {
		add(tab);
	}
}
