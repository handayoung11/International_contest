import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class TopChart extends BaseFrame{
	int bx=30, by=20, ly=360, wx=700, unit=28, bw=40, su=wx/6;
	JPanel jP[]=new JPanel[2], chartJP=new JPanel();
	JButton jB=new JButton("전체");
	JRadioButton genre[]=new JRadioButton[8];
	JLabel title[]=new JLabel[6];
	
	public TopChart() {
		// TODO Auto-generated constructor stub
		super("인기차트", new BorderLayout(), 770, 560, 2);
		setDesign();
		drawChart();
		setAction();
		setVisible(true);
	}
	
	void setAction() {
		jB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				drawChart("전체");
			}
		});
		for(int i=0; i<genre.length; i++)
			genre[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					drawChart(arg0.getActionCommand());
				}
			});
	}
	
	void setDesign() {
		((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
		add(jP[0]=new JPanel(new BorderLayout(0, 10)), BorderLayout.NORTH);
		add(jP[1]=new JPanel(new BorderLayout()));
		
		JPanel south=new JPanel(new GridLayout(0, 6));
		for(int i=0; i<6; i++)
			south.add(title[i]=new JLabel("", JLabel.CENTER));
		size(south, 10, 15);
		south.setBorder(new EmptyBorder(0, 27, 0, 10));
		
		jP[1].add(south, BorderLayout.SOUTH);
		jP[0].add(jB, BorderLayout.SOUTH);
		setComp();
	}
	
	void setComp() {
		JPanel center=new JPanel(new GridLayout(0, 4));
		jP[0].add(center);
		center.setBorder(new TitledBorder("장르"));
		try {
			ResultSet rs=DB.stmt.executeQuery("select distinct genre from music");
			int cnt=0;
			ButtonGroup bG=new ButtonGroup();
			while(rs.next()){
				center.add(genre[cnt]=new JRadioButton(rs.getString(1)));
				bG.add(genre[cnt]);
				cnt++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	
	void drawChart(String genre) {
		jP[1].remove(chartJP);
		chartJP=new JPanel() {
			protected void paintComponent(Graphics g) {
				drawLine(g);
				Color colors[]=new Color[] {Color.pink, Color.red, Color.orange, Color.YELLOW, Color.BLUE, Color.green};
				String query="";
				if(!genre.equals("전체"))
					query=" where genre='"+genre+"'";
				try {
					ResultSet rs=DB.stmt.executeQuery("select album, title from music"+query+" order by album desc, music_no asc limit 6");
					int i=0;
					while(rs.next()) {
						g.setColor(colors[i]);
						g.fillRect(bx+(i+1)*su-su/2-bw/2, ly-rs.getInt(1)*unit/2, bw, rs.getInt(1)*unit/2);
						title[i].setText(rs.getString(2));
						i++;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}; 
		};
		jP[1].add(chartJP);
		repaint();
		revalidate();
	}
	
	void drawChart() {
		chartJP=new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				drawLine(g);
				Color colors[]=new Color[] {Color.pink, Color.red, Color.orange, Color.YELLOW, Color.BLUE, Color.green};
				for(int i=0; i<colors.length; i++) {
					g.setColor(colors[i]);
					g.fillRect(bx+(i+1)*su-su/2-bw/2, ly-5, bw, 5);
				}
			}
		};
		jP[1].add(chartJP);
	}
	
	void drawLine(Graphics g) {
		g.drawLine(bx, ly, wx, ly);
		g.drawLine(bx, by, bx, ly);
		for(int i=0; i<10; i++) {
			g.drawLine(bx, ly-unit*(i+1), wx, ly-unit*(i+1));
			g.drawString((i+1)*2+"", bx-15, ly-unit*(i+1));
		}
	}
}
