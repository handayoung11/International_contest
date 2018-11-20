import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class SongList extends BaseFrame{
	JPanel jP[]=new JPanel[3];
	JButton jB[]=new JButton[3];
	JComboBox genre=new JComboBox<>();
	DefaultTableModel dTM=makeModel(new String[] {"",  "��ȣ", "����", "����", "�帣", "�߸���"}, 0);
	JTable jT=makeTable(dTM, 0);
	JTextField text=new JTextField(25);
	JFrame thisForm=this;
	
	public SongList() {
		// TODO Auto-generated constructor stub
		super("�뷡 ���", new BorderLayout(), 760, 360, 2);
		setDesign();
		setAction();
		setVisible(true);
	}

	void setAction() {
		jT.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				setWest();
				if(arg0.getClickCount()==2 && jT.getSelectedColumn()==0) {
					if(DB.ID=="") {
						if(con_msg("���ƿ並 �Ͻ÷��� �α����� ���ּ���.", "���ƿ�")) {
							dispose();
							return;
						}
					}
					int row=jT.getSelectedRow();
					try {
						if(((ImageIcon)(jT.getValueAt(row, 0))).getIconHeight()==10) 
							DB.stmt.execute("insert into rating values("+jT.getValueAt(row, 1)+", '"+DB.ID+"', 0, '')");
						else
							DB.stmt.execute("delete from rating where member_r='"+DB.ID+"' and music_r="+jT.getValueAt(row, 1)+" and kinds=0");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					updateTable(dTM, jT);
					setWest();
				}
			}
		});
		for(int i=0; i<3; i++) {
			jB[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if(arg0.getSource().equals(jB[0])) {
						String sql="";
						if(genre.getSelectedIndex()!=0)
							sql=" and genre='"+genre.getSelectedItem()+"'";
						addRow(dTM,"select album, music_no, title, singer, genre, date from music where title like '%"+text.getText()+"%'"+sql);
						if(dTM.getRowCount()==0) {
							err_msg("�˻��� �׸��� �����ϴ�.");
							addRow(dTM, "select album, music_no, title, singer, genre, date from music");
							genre.setSelectedIndex(0);
							text.setText("");
						}
						updateTable(dTM, jT);
						setWest();
					}
					if(arg0.getSource().equals(jB[1])) {
						setVisible(false);
						int row=jT.getSelectedRow();
						new OneLine(new Object[] {jT.getValueAt(row, 2), jT.getValueAt(row, 3), jT.getValueAt(row, 1)}).addWindowListener(new Before(thisForm));
					}
					if(arg0.getSource().equals(jB[2])) {
						try {
							int row=jT.getSelectedRow();
							ResultSet rs=DB.stmt.executeQuery("select * from rating where kinds=1 and music_r="+jT.getValueAt(row, 1)+" and member_r='"+DB.ID+"'");
							if(rs.next())
								return;
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if(MainFrame.POINT==-1) {
							if(con_msg("���Ÿ� �Ͻ÷��� �α����� ���ּ���.", "����")) {
								dispose();
								return;
							};
						}
						int row=jT.getSelectedRow();
						if(con_msg("���� : "+jT.getValueAt(row, 2)+"�� �����Ͻðڽ��ϱ�?", "���� Ȯ��")) {
							if(MainFrame.POINT==0) {
								if(con_msg("����Ʈ�� �����մϴ�.\n����Ʈ�� �����Ͻðڽ��ϱ�?", "����Ʈ ����")) {
									setVisible(false);
									new Pay().addWindowListener(new Before(thisForm));
								}
								return;
							}
							try {
								DB.stmt.execute("insert into rating values("+jT.getValueAt(row, 1)+", '"+DB.ID+"', 1, '')");
								DB.stmt.execute("update music set album=album+1 where music_no="+jT.getValueAt(row, 1));
								DB.stmt.execute("update member set point=point-100 where id='"+DB.ID+"'");
								MainFrame.POINT-=100;
								MainFrame.PJL.setText("�ܿ�����Ʈ : "+MainFrame.POINT);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						updateTable(dTM, jT);
						setWest();
					}
				}
			});
		}
	}

	void setDesign() {
		add(jP[0]=new JPanel(new BorderLayout()), BorderLayout.WEST);
		add(jP[2]=new JPanel(new BorderLayout()));
		jP[2].add(jP[1]=new JPanel(), BorderLayout.NORTH);
		jP[0].setBorder(new EmptyBorder(10, 10, 10, 10));

		jB[1]=new JButton("������");
		jB[2]=new JButton("����");
		genre.addItem("��ü");
		addItem("select distinct genre from music", genre);
		setCenter();
		setWest();
	}

	void setWest() {
		jP[0].removeAll();
		if(jT.getSelectedRow()==-1)
			jT.setRowSelectionInterval(0, 0);

		int row=jT.getSelectedRow();
		JPanel north=new JPanel(new GridLayout(0, 1)), south=new JPanel(new GridLayout(0, 2));
		jP[0].add(north, BorderLayout.NORTH);
		jP[0].add(south, BorderLayout.SOUTH);
		jP[0].add(new JLabel(insert_Image("./2���������ڷ�/�̹��� �����ڷ�/�ٹ� ����/"+jT.getValueAt(row, 2)+".jpg", 200, 200)));

		north.add(new JLabel("���� : "+jT.getValueAt(row, 2)));
		north.add(new JLabel("���� :"+jT.getValueAt(row, 3)));

		try {
			ResultSet rs=DB.stmt.executeQuery("select sum(if(kinds=0, 1, 0)), sum(if(kinds=1, 1, 0)) from rating where music_r="+
					jT.getValueAt(row, 1));
			if(rs.next()) {
				south.add(new JLabel("���ƿ� : "+rs.getString(1)));
				south.add(jB[1]);
				south.add(new JLabel("���� : "+rs.getString(2)));		
				south.add(jB[2]);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateTable(dTM, jT);
		repaint();
		revalidate();
	}

	void setCenter() {
		jT.getColumnModel().getColumn(0).setMaxWidth(20);
		jT.getColumnModel().getColumn(1).setMaxWidth(40);
		jT.getColumnModel().getColumn(2).setMinWidth(120);
		jT.getColumnModel().getColumn(3).setMinWidth(120);
		jT.getColumnModel().getColumn(4).setMaxWidth(80);

		addcom(jP[1], new JLabel("������:"), text, new JLabel("�帣:"), genre, jB[0]=new JButton("�˻�"));
		jP[2].add(new JScrollPane(jT));
		addRow(dTM, "select album, music_no, title, singer, genre, date from music");
		updateTable(dTM, jT);
	}
}
