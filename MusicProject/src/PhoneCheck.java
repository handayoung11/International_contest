import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class PhoneCheck extends BaseFrame{
	String random="";
	JLabel ranJL, jL;
	public PhoneCheck() {
		// TODO Auto-generated constructor stub
		super("�޴�������", null, 355, 610, 2);
		setDesign();
		setVisible(true);
		shake();
	}
	
	void shake() {
		for(int i=0; i<10; i++) {
			setLocation(getX()+50*(i%2==0?1:-1), getY());
			printAll(getGraphics());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	boolean getCheck() {
		JTextField text=new JTextField();
		if(con_msg(inputJP("������ȣ:", text), "������ȣ Ȯ��")) {
			if(!text.getText().equals(random)) 
				err_msg("���� ����");
			else {
				info_msg("���� ����");
				dispose();
				return true;
			}			
		};	
		dispose();
		return false;
	}
	
	
	
	void setDesign() {
		String ran="�޴��� ������ȣ : ";
		for(int i=0; i<6; i++)
			random+=new Random().nextInt(10);
		add(ranJL=new JLabel(ran+random));
		ranJL.setBounds(30, 60, 300, 30);
		add(jL=new JLabel(insert_Image("./2���������ڷ�/�̹��� �����ڷ�/phone1.png", 340, 570)));
		jL.setBounds(0, 0, 340, 570);
	}
}
