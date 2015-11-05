package gitProject;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

import javax.swing.*;

class textGUI extends JFrame {
	TextArea 	textContents;			// 내용입력창
	JPanel 		jp_North, jp_South;		// 특정 단어 입력 구역과 옵션 버튼 구역
	JLabel 		jl_select, jl_modify;	// 특정 단어 입력 구역에 쓰일 글자
	TextField   tf_select, tf_modify;	// 특정 단어 입력칸
	
	// 옵션 버튼 이름
	String[] btnName = {
			"Undo",
			"Delete_EvenLine",
			"Delete_Letter",
			"Delete_EmptyLine",
	};
	
	// 옵션 버튼 객체
	Button[] optionBtn = new Button[btnName.length];
	
	// 개행 문자
	private final String CR_LF = System.getProperty("line.separator");
	private String prevText = "";
	
	private void registerEventHandler() {
		int n = 0;
		
		optionBtn[n++].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String curText = textContents.getText();
				
				if ( !prevText.equals("") ) {
					textContents.setText(prevText);
				}
				
				prevText = curText;
			}
		});

		optionBtn[n++].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// 현재 textContents에 적혀있는 문자열을 받아온다.
				String curText = textContents.getText();
				StringBuffer sb = new StringBuffer(curText.length());
				
				prevText = curText;
				
				Scanner sc = new Scanner(curText);
				
				for ( int i = 0; sc.hasNextLine(); i++ ) {
					String line = sc.nextLine();
					
					if ( i % 2 == 0 ) {
						sb.append(line).append(CR_LF);
					}
				}
				
				textContents.setText(sb.toString());
			}
		});
		
		optionBtn[n++].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String curText = textContents.getText();
				StringBuffer sb = new StringBuffer(curText.length());
				
				prevText = curText;
				
				String deleteText = tf_select.getText();
				
				if ( "".equals(deleteText)) return;
				
				for ( int i = 0; i < curText.length(); i++ ) {
					char ch = curText.charAt(i);
					
					if ( deleteText.indexOf(ch) == -1 )
						sb.append(ch);
				}
				
				textContents.setText(sb.toString());
			}
		});
		
		optionBtn[n++].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String curText = textContents.getText();
				StringBuffer sb = new StringBuffer(curText.length());
				
				prevText = curText;
				
				Scanner sc = new Scanner(curText);
				
				for ( int i = 0; sc.hasNextLine(); i++ ) {
					String line = sc.nextLine();
					
					if ( line.trim().length() > 0 ) {
						sb.append(line).append(CR_LF);
					}
				}
				
				textContents.setText(sb.toString());
			}
		});
	}
	
	textGUI(String title) {
		super(title);
		jl_select = new JLabel("Select : ", JLabel.RIGHT);
		tf_select = new TextField(15);
		jl_modify = new JLabel("modify : ", JLabel.RIGHT);
		tf_modify = new TextField(15);
		textContents = new TextArea();
		jp_North = new JPanel();
		jp_South = new JPanel();
		
		for ( int i = 0; i < optionBtn.length; i++ ) {
			optionBtn[i] = new Button(btnName[i]);
		}
		
		jp_North.setLayout(new FlowLayout());
		jp_North.add(jl_select);
		jp_North.add(tf_select);
		jp_North.add(jl_modify);
		jp_North.add(tf_modify);
		
		jp_South.setLayout(new GridLayout(2, 10));
		
		for ( int i = 0; i < optionBtn.length; i++ ) {
			jp_South.add(optionBtn[i]);
		}
		
		add(jp_North, "North");
		add(textContents, "Center");
		add(jp_South, "South");
		
		setBounds(100, 100, 600, 300);
		textContents.requestFocus();
		registerEventHandler();
		setVisible(true);
	}
}

public class TextEx {
	public static void main(String[] args) {
		new textGUI("My Own Text Editor");
	}
}
