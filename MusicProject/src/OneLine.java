import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class OneLine extends BaseFrame{
	JPanel jP[]=new JPanel[2];
	DefaultTableModel dTM=makeModel(new String[] {"평가자",  "한줄평가"}, -1);
	JTable jT=makeTable(dTM, 1);
	Object info[];
	public OneLine(Object info[]) {
		// TODO Auto-generated constructor stub
		super("한줄평가", new BorderLayout(), 450, 250, 2);
		this.info=info;
		setDesign();
		setVisible(true);
	}
	
	void setDesign() {
		add(jP[0]=new JPanel(new GridLayout(0, 1)), BorderLayout.NORTH);
		jP[0].add(new JLabel("제목 : "+info[0]));
		jP[0].add(new JLabel("가수 : "+info[1]));
		
		jT.getColumnModel().getColumn(1).setMinWidth(240);
		addRow(dTM, "select member_r, rating from rating where kinds=1 and music_r="+info[2]);
		add(new JScrollPane(jT));
	}
}
