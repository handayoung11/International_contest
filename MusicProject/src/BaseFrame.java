import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class BaseFrame extends JFrame{

	public BaseFrame(String title, LayoutManager manager, int w, int h, int operation) {
		// TODO Auto-generated constructor stub
		super(title);
		setSize(w, h);
		setDefaultCloseOperation(operation);
		setLayout(manager);
		setLocationRelativeTo(null);
	}
	
	static JLabel fontJL(String label, int size) {
		JLabel jL=new JLabel(label, JLabel.CENTER);
		jL.setFont(new Font("", Font.BOLD, size));
		return jL;
	}

	JPanel inputJP(String title, JTextField text) {
		JPanel jP=new JPanel(new GridLayout(0, 2));
		jP.add(new JLabel(title));
		jP.add(text);
		return jP;
	}
	
	int toint(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return 0;
		}
	}
	
	ImageIcon insert_Image(String path, int w, int h) {
		try {
			return new ImageIcon(ImageIO.read(new File(path)).getScaledInstance(w, h, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			path=path.replace("jpg", "png");
			try {
				return new ImageIcon(ImageIO.read(new File(path)).getScaledInstance(w, h, Image.SCALE_SMOOTH));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("image null");
				return null;
			}
		}
	}
	
	void err_msg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "오류", JOptionPane.ERROR_MESSAGE);
	}
	void addcom(JPanel par, Component ...com) {
		for(Component i : com)
			par.add(i);
	}
	
	static JLabel fontJL(String label, String font, int size) {
		JLabel jL=new JLabel(label, JLabel.CENTER);
		jL.setFont(new Font(font, Font.BOLD, size));
		return jL;
	}
	
	boolean con_msg(Object msg, String title) {
		if(JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
			return true;
		return false;
	}
	
	JTable makeTable(DefaultTableModel dTM, int column) {
		JTable jT=new JTable(dTM);
		jT.getTableHeader().setReorderingAllowed(false);
		jT.getTableHeader().setResizingAllowed(false);
		DefaultTableCellRenderer dTCR=new DefaultTableCellRenderer();
		dTCR.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		for(int i=0; i<dTM.getColumnCount(); i++)
			if(i!=column)
				jT.getColumnModel().getColumn(i).setCellRenderer(dTCR);
		return jT;
	}
	
	void updateTable(DefaultTableModel dTM, JTable jT) {
		DefaultTableCellRenderer dTCR=new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3,
					int arg4, int arg5) {
				// TODO Auto-generated method stub
				Component c=super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
				setHorizontalAlignment(CENTER);				
				c.setBackground(Color.white);
				
				try {
					ResultSet rs=DB.stmt.executeQuery("select * from rating where member_r='"+DB.ID+"' and kinds=1 and music_r="+arg0.getValueAt(arg4, 1));
					if(rs.next())
						if(rs.getString(1).equals(arg0.getValueAt(arg4, 1).toString())) 
							c.setBackground(Color.yellow);				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return c;
			}
		};
		for(int i=1; i<dTM.getColumnCount(); i++)
			jT.getColumnModel().getColumn(i).setCellRenderer(dTCR);
		ImageIcon heart=insert_Image("./2과제지급자료/이미지 지급자료/image2.png", 11, 11);
		ImageIcon non_heart=insert_Image("./2과제지급자료/이미지 지급자료/image1.png", 10, 10);
		for(int i=0; i<dTM.getRowCount(); i++) {
			dTM.setValueAt(non_heart, i, 0);
			try {
				ResultSet rs=DB.stmt.executeQuery("select * from rating where member_r='"+DB.ID+"' and rating=0 and music_r="+jT.getValueAt(i, 1));
				if(rs.next())
						dTM.setValueAt(heart, i, 0);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		repaint();
		revalidate();
	}
	
	void addItem(String sql, JComboBox jCB) {
		try {
			ResultSet rs=DB.stmt.executeQuery(sql);
			while(rs.next())
				jCB.addItem(rs.getString(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	DefaultTableModel makeModel(String title[], int column) {
		DefaultTableModel dTM=new DefaultTableModel(null, title) {
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			public Class<?> getColumnClass(int arg0) {
				// TODO Auto-generated method stub
				if(arg0==column)
					return ImageIcon.class;
				return super.getColumnClass(arg0);
			}
		};
		return dTM;
	}
	
	void addRow(DefaultTableModel dTM, String sql) {
		dTM.setRowCount(0);		
		try {
			ResultSet rs=DB.stmt.executeQuery(sql);
			Object row[]=new Object[dTM.getColumnCount()];
			while(rs.next()) {
				for(int i=0; i<dTM.getColumnCount(); i++)
					row[i]=rs.getString(i+1);
				dTM.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void size(Component com, int w, int h) {
		com.setPreferredSize(new Dimension(w, h));
	}
	
	void info_msg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "확인", JOptionPane.INFORMATION_MESSAGE);
	}
	
	class Before extends WindowAdapter{
		JFrame jF;
		public Before(JFrame jF) {
			// TODO Auto-generated constructor stub
			this.jF=jF;
		}
		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			jF.setVisible(true);
		}
	}
}
