package gitProject;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.filechooser.*;

class fontSettingFrame extends JPanel implements ActionListener {
	textGUI mainFrame = new textGUI();
	
	public fontSettingFrame(){
		this.setLayout(new FlowLayout());
		this.setSize(250, 100);
		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel fontSize = new JLabel("Font Size : ");
		JButton fontSizeSeleteBtn = new JButton("확인");
		fontSizeSeleteBtn.addActionListener(this);

		this.add(fontSize);
		this.add(fontSizeSeleteBtn);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent ae){
		mainFrame.getTextContentsFont();
		this.setVisible(false);
		//this.dispose();
	}
}

class textGUI extends JFrame {
    TextArea 	textContents;			// 내용입력창
    JPanel 		jp_North, jp_South;		// 특정 단어 입력 구역과 옵션 버튼 구역
    JLabel 		jl_select, jl_modify;	// 특정 단어 입력 구역에 쓰일 글자
    TextField   tf_select, tf_modify;	// 특정 단어 입력칸
    JMenuBar    jmb;                    // 메뉴바
    JMenu       jm_file, jm_option;     // 파일 메뉴, 옵션 메뉴
    JMenuItem
            jmi_fileOpen,       	// 파일 열기
            jmi_fileSave,       	// 파일 저장
            jmi_fileSaveAs,			// 즉시 저장
            jmi_programExit,    	// 프로그램 종료
    		jmi_fontSizeUp,			// 글자 크기 크게
    		jmi_fontSizeDown,		// 글자 크기 작게
    		jmi_fontSizeOriginal;	// 글자 크기 원래대로
    JFileChooser defaultPath = new JFileChooser("c:/");
    FileFilter selectExpansion = new FileNameExtensionFilter("텍스트 문서(*.txt)", "txt");

    void getTextContentsFont() {
    	try {
    		System.out.println("Good");
    		System.out.println(textContents.getFont());
    		Font nowFontobj = textContents.getFont();
    		textContents.setFont(new Font(nowFontobj.getFontName(), nowFontobj.getStyle(), 20));
    	} catch (NullPointerException ne) {
    		System.out.println("Cyka");
    		ne.printStackTrace();
    	}
    }
    
    // 옵션 버튼 이름
    String[] btnName = {
            "Undo",
            "D_EvenLine",
            "D_Letter",
            "D_EmptyLine",
            "D_EmptySpace",
            "I_prefix",
            "D_prefix",
            "D_Specific_Prefix",
            "D_OverlapLine",
            "D_OverlapLine(num)",
            "I_Specific_Prefix",
            "D_Specific_Prefix",
    };

    // 옵션 버튼 객체
    Button[] optionBtn = new Button[btnName.length];

    // 개행 문자. 한 줄 띄워주는 변수.
    private final String CR_LF = System.getProperty("line.separator");

    // 이전으로 가기버튼 등에 필요
    private String prevText = "";

    textGUI() {
    	
    }
    
    // 버튼에 액션리스너 장착 함수
    private void registerEventHandler() {
        int n = 0;

        // 이전 상태로 돌아가기
        optionBtn[n++].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // TextArea에 있는 문자열을 curText로 받는다.
                String curText = textContents.getText();

                System.out.println("curText : " + curText);
                // 만약, prevText가 빈 칸이 아니라면
                // TextArea에 prevText에 들어있는 문자열을 입력
                if ( !prevText.equals("") ) {
                    textContents.setText(prevText);
                    System.out.println("prevText : " + prevText);
                }

                // undo를 누르기 전에 적힌 문자열은 prevText에 대입
                prevText = curText;
            }
        });

        // 홀수번째 라인 삭제
        optionBtn[n++].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // TextArea에 있는 문자열을 curText로 받는다.
                String curText = textContents.getText();

                // 현재 적은 문자열만큼의 크기를 가진 StringBuffer객체를 생성
                StringBuffer sb = new StringBuffer(curText.length());

                // 일단, 현재 적은 문자열을 prevText에 대입
                prevText = curText;

                // curText문자열을 사용하는 Scanner 객체 생성
                Scanner sc = new Scanner(curText);

                // 다음 라인에 문자열이 존재하면 실행되는 반복문
                for ( int i = 0; sc.hasNextLine(); i++ ) {
                    // 현재 라인의 문자열을 받아서
                    String line = sc.nextLine();

                    // 만약, 현재 라인이 짝수번째라인이라면
                    // StringBuffer객체에 문자열을 적은 후 개행문자를 넣는다.
                    if ( i % 2 == 0 ) {
                        sb.append(line).append(CR_LF);
                    }
                }

                // 짝수번째라인만 적혀있는 StringBuffer의 문자열들을 textArea에 적용시킨다.
                textContents.setText(sb.toString());
            }
        });

        // 특정 문자를 라인마다 앞, 뒤에 붙이기
        optionBtn[n++].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // TextArea에 있는 문자열을 curText로 받는다.
                String curText = textContents.getText();

                // 현재 입력한 문자열만큼의 크기를 가진 StringBuffer객체를 생성
                StringBuffer sb = new StringBuffer(curText.length());

                // 일단, 현재 입력한 문자열을 prevText에 대입
                prevText = curText;

                // tf_select에 입력된 문자열을 deleteText에 대입
                String deleteText = tf_select.getText();
                StringBuffer temp = new StringBuffer();

                // 만약, deleteText에 값이 없다면 액션이벤트 종료
                if ( "".equals(deleteText)) return;

                // 현재 입력한 문자열의 길이만큼 반복하는 for문
                for ( int i = 0; i < curText.length(); i++ ) {
                    // 첫번째 문자부터 마지막문자까지
                    // 반복문이 돌아감에따라 뽑아내어 변수에 담아준다.
                    char ch = curText.charAt(i);

                    // 삭제하고 싶은 문자열에 속해있는 문자가 있다면
                    if ( deleteText.indexOf(ch) != -1 ) {
                        // 일단, 임시공간에 문자열로 저장
                        temp.append(ch);

                        // 삭제하고 싶은 문자열의 마지막에 위치한 수가 반환하는 정수와
                        // 삭제하고 싶은 문자열의 전체 길이에서 -1한 값이 같다면,
                        if ( deleteText.indexOf(ch) == (deleteText.length() - 1) ) {
                            // 위의 조건에 단지 삭제하고 싶은 단어의 마지막 문자와 일치하여
                            // 해당 조건문에 들어왔을 경우에는
                            if ( temp.length() == 1 ) {
                                // 일치한 문자만 문자열에 붙이고,
                                sb.append(temp.toString());
                            }
                            // 임시공간을 비워준다.
                            temp.setLength(0);
                        }
                    } else {    // 삭제하고 싶은 문자열에 속해있는 문자가 없다면
                        sb.append(temp.toString()); // 지금까지 임시공간에 입력된 문자열을 붙이고,
                        sb.append(ch);  // 현재 입력된 문자까지 붙인뒤에
                        temp.setLength(0);  // 임시공간을 비워준다.
                    }
                }

                // 반복문에서 StringBuffer에 입력된 모든 문자열을 textArea에 뿌려준다.
                textContents.setText(sb.toString());
            }
        });

        // 문자열이 없는 빈 라인이 있을시에 해당 라인 삭제
        optionBtn[n++].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // TextArea에 있는 문자열을 curText로 받는다.
                String curText = textContents.getText();

                // 현재 입력한 문자열만큼의 크기를 가진 StringBuffer객체를 생성
                StringBuffer sb = new StringBuffer(curText.length());

                // 일단, 현재 입력한 문자열을 prevText에 대입
                prevText = curText;

                // 현재 입력된 문자열을 사용하는 Scanner 객체 생성
                Scanner sc = new Scanner(curText);

                // 다음 라인에 문자열이 존재하면 실행되는 반복문
                for ( int i = 0; sc.hasNextLine(); i++ ) {
                    // 현재 라인의 문자열을 받아서
                    String line = sc.nextLine();

                    // 만약, 현재 라인에 문자 하나라도 존재한다면
                    if ( line.trim().length() > 0 ) {
                        // 개행 문자로 한 줄 띄워주어라.
                        sb.append(line).append(CR_LF);
                    }
                }

                // 반복문에서 StringBuffer에 입력된 모든 문자열을 textArea에 뿌려준다.
                textContents.setText(sb.toString());
            }
        });

        // 라인마다 앞, 뒤에 있는 공간 삭제
        optionBtn[n++].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String curText = textContents.getText();
                StringBuffer sb = new StringBuffer(curText.length());
                prevText = curText;
                Scanner sc = new Scanner(curText);

                for ( int i = 0; sc.hasNextLine(); i++ ) {
                    String line = sc.nextLine();

                    sb.append(line.trim()).append(CR_LF);
                }
                textContents.setText(sb.toString());
            }
        });

        // 라인마다 앞, 뒤에 입력한 문자 삽입
        optionBtn[n++].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String curText = textContents.getText();
                StringBuffer sb = new StringBuffer(curText.length());
                prevText = curText;

                String param1 = tf_select.getText();
                String param2 = tf_modify.getText();

                Scanner sc = new Scanner(curText);

                for ( int i = 0; sc.hasNextLine(); i++ ) {
                    String line = sc.nextLine();

                    if ( line.trim().length() != 0 ) {
                        sb.append(param1).append(line).append(param2).append(CR_LF);
                    }
                }

                textContents.setText(sb.toString());
            }
        });

        // 라인마다 앞, 뒤에 입력한 문자가 있다면 삭제
        optionBtn[n++].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	String curText = textContents.getText();
            	StringBuffer sb = new StringBuffer(curText.length());
            	prevText = curText;
            	
                // param1과 param2의 값을 가져온다.(getText()사용)
            	String stringOfFront = tf_select.getText();
            	String stringOfBack  = tf_modify.getText();
            	
                // Scanner클래스와 반복문을 이용해서 curText를 라인단위로 읽는다.
            	Scanner sc = new Scanner(curText);
            	
            	for ( int i = 0; sc.hasNextLine(); i++ ) {
            		String line = sc.nextLine();
            		line = line.replaceAll("\t", "");
            		
            		// 읽어온 라인을 substring으로 자른다. - param1과 param2의 내용에 관계없이 길이만큼 자른다.
            		line = line.substring(stringOfFront.length(), line.length() - stringOfBack.length());
            		sb.append(line).append(CR_LF);
            	}

            	// 작업이 끝난 후에 sb에 담긴 내용을 ta에 보여준다.(setText()사용)
            	textContents.setText(sb.toString());
            }
        });
        
        // 특정 문자열 안에 속한 문자열 뽑아오기
        optionBtn[n++].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ae) {
        		String curText = textContents.getText();
        		StringBuffer sb = new StringBuffer(curText.length());
        		prevText = curText;

        		// param1과 param2의 값을 가져온다.(getText()사용)
        		String specificStringOfFront = tf_select.getText();
        		String specificStringOfBack  = tf_modify.getText();
        		
        		// Scanner클래스와 반복문을 이용해서 curText를 라인단위로 읽는다.
        		Scanner sc = new Scanner(curText);
        		
        		for ( int i = 0; sc.hasNextLine(); i++ ) {
        			String line = sc.nextLine();
        			
        			// 각 라인에서 param1, param2과 일치하는 문자열의 위치를 찾는다.
        			int from = line.indexOf(specificStringOfFront);
        			int to   = line.lastIndexOf(specificStringOfBack);
        			
        			// (param1은 라인의 왼쪽끝부터, param2는 라인의 오른쪽끝부터 찾기 시작한다.)
        			// 만약, 첫번째로 입력된 인자가 만족하는 문자가 발견되지 않는다면 0을 반환해주고,
        			// 만족하는 문자가 발견된다면 해당 인자가 처음 발견된 문자의 위치에 그 인자의 길이를 더해준 값을 반환해준다.
        			from = ( from == -1 ) ? 0 : from + specificStringOfFront.length();
        			
        			// 만약, 두번째로 입력된 인자가 만족하는 문자가 발견되지 않는다면 현재 라인 문자열의 자리수를 반환해주고,
        			// 만족하는 문자가 발견된다면 해당 인자가 처음 발견된 문자 위치의 값을 반환해준다.
        			to   = ( to == -1 ) ? line.length() : to;
        			
        			if ( from > to ) return;
        			
        			// param1과 param2로 둘러쌓인 부분을 sb에 저장한다.
        			sb.append(line.substring(from, to)).append(CR_LF);
        		}

                // sb의 내용을 TextArea에 보여준다.
        		textContents.setText(sb.toString());
        	}
        });
        
        // 중복 라인 제거
        optionBtn[n++].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ae) {
        		String curText = textContents.getText();
        		StringBuffer sb = new StringBuffer(curText.length());
        		prevText = curText;

                // Scanner클래스와 반복문을 이용해서 curText를 라인단위로 읽어서 HashSet에 담는다.
                Scanner sc = new Scanner(curText);
                
                HashSet lineList = new HashSet();
                
                for ( int i = 0; sc.hasNextLine(); i++ ) {
                	String line = sc.nextLine();
                	lineList.add(line);
                }

        		// HashSet의 내용을 ArrayList로 옮긴다음 정렬한다.(Collections의 sort()사용)
                ArrayList distinctLineList = new ArrayList(lineList);
                Collections.sort(distinctLineList);
                
                // 정렬된 ArrayList의 내용을 sb에 저장한다.
                int listSize = distinctLineList.size();
                
                for ( int i = 0; i < listSize; i++ ) {
                	sb.append(distinctLineList.get(i)).append(CR_LF);
                }
                
                // sb에 저장된 내용을 TextArea에 보여준다.
                textContents.setText(sb.toString());
            } 
        });
        
        // 중복 라인 제거 후 뒤에 중복된 수를 표시
        optionBtn[n++].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ae) {
        		String curText = textContents.getText();
        		StringBuffer sb = new StringBuffer(curText.length());
        		prevText = curText;
        		
                // Scanner클래스와 반복문을 이용해서 curText를 라인단위로 읽어서 TreeMap에 담는다.
                Scanner sc = new Scanner(curText);
                
                TreeMap lineList = new TreeMap();
                String delimiter = tf_select.getText();
                
                // 첫번째 입력란에 어떤 문자열을 입력했으면
                // 그 문자열을 키와 값을 구분하는 구분자로 사용하고,
                // 첫번째 입력란에 아무것도 입력하지 않았으면 콤마(,)를 구분자로 설정한다.
                if ( delimiter.length() == 0 ) delimiter = ",";
                
                for ( int i = 0; sc.hasNextLine(); i++ ) {
                	String line = sc.nextLine();
                	
                	// TreeMap에 담을 때, 각 라인을 키로 저장하고 값으로는 중복회수를 저장한다.
                    // TreeMap에 담을 때, 이미 같은 내용의 값이 저장되어 있는지 확인하고
                	if ( lineList.containsKey(line) ) {
                		// 이미 같은 내용이 저장되어 있으면
                		// 해당 키의 값을 읽어서
                		Integer keyValueOfnowLine = (Integer)lineList.get(line);
                		
                		// 1을 증가시키고,
                		lineList.put(line, new Integer(keyValueOfnowLine.intValue() + 1));
                	} else {
                		// 새로운 키값이면 1을 값으로 저장한다.
                		lineList.put(line, new Integer(1));
                	}
                	System.out.println(lineList);
                }
                
        		// Iterator를 이용해서 TreeMap에 저장된 키와 값을 구분자와 함께 sb에 저장한다.
                // (TreeMap을 사용했기 때문에, 자동적으로 키값을 기준으로 오름차순 정렬된다.)
                
                // lineList에 저장되어 있는 모든 key-value쌍을
                // Map.Entry Type의 객체로 반환
                Set formOfSet = lineList.entrySet();
                
                // 컬렉션에 저장된 요소들을 읽어오는 클래스인
                // Iterator Type의 객체로 반환
                Iterator formOfIterator = formOfSet.iterator(); 

                while( formOfIterator.hasNext() ) { 
                	// formOfIterator에 저장되어 있는 요소들을 차례대로 읽어와서
                	// Map.Entry Type의 객체의 참조변수로 넣어주고,
                	Map.Entry me_obj = (Map.Entry)formOfIterator.next(); 

                	// key와 value중에 value값을 불러와서 value라는 int형 변수에 넣어준다.
                    int value = ((Integer)me_obj.getValue()).intValue(); 

                    sb.append(me_obj.getKey());	// StringBuffer에 key에 들어있는 문자,
                    sb.append(delimiter); 		// 구분자,
                    sb.append(value);			// key에 들어있는 문자가 반복된 수,
                    sb.append(CR_LF); 			// 마지막으로 개행문자를 넣어주어 enter키를 친 효과를 준다.
                } 

        		// sb에 저장된 내용을 TextArea에 보여준다.
                textContents.setText(sb.toString());
        	}
        });
        
        // 구분자로 구분된 문자열들을 특정 패턴에 맞게 출력
        optionBtn[n++].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) { 
            	String curText = textContents.getText(); 
            	StringBuffer sb = new StringBuffer(curText.length()); 
            	prevText = curText; 
                  
            	String pattern = tf_select.getText(); 
            	String delimiter = tf_modify.getText(); 

            	if( delimiter.length() == 0 ) delimiter = ","; 

            	// Scanner클래스와 반복문을 이용해서 curText를 라인단위로 읽는다.
            	Scanner sc = new Scanner(curText);
                  
            	for ( int i = 0; sc.hasNextLine(); i++ ) {
            		String line = sc.nextLine();
                	  
            		// 라인을 구분자(delimiter)로 나누어 문자열 배열에 저장한다.
            		String[] nowLineStr = line.split(delimiter);
                	  
                	// 첫번째 입력란에 입력받은 pattern을 각 라인에 적용해서 sb에 저장한다.
            		// 데이터가 들어갈 자리를 마련해 놓은 양식을 미리 작성하여
            		// 데이터를 정해진 양식에 맞게 바꿔서 출력해주는 메서드인 MessageFormat클래스의 format메서드를 사용
                	sb.append(MessageFormat.format(pattern, nowLineStr)).append(CR_LF);
            	}
            	
                // sb의 내용을 TextArea에 보여준다.
            	textContents.setText(sb.toString());
            }
        });
        
        // 
        optionBtn[n++].addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent ae) { 
                  String curText = textContents.getText(); 
                  StringBuffer sb = new StringBuffer(curText.length()); 
                  prevText = curText; 

                  String pattern = tf_select.getText(); 
                  String delimiter = tf_modify.getText(); 

                  Pattern p = Pattern.compile(pattern); 

                  if(delimiter.length()==0) delimiter = ","; 
                  
                  // Scanner클래스와 반복문을 이용해서 curText를 라인단위로 읽는다.
                  Scanner sc = new Scanner(curText);
                  
                  for ( int i = 0; sc.hasNextLine(); i++ ) {
                	  String line = sc.nextLine();
                	  
                	  Matcher m = p.matcher(line);
                	  
                  }
                  // 각 라인을 pattern에 맞게 매칭시킨다.(Pattern클래스의 matcher()사용)
                  // pattern에 매칭되는 데이터를 구분자와 함께 sb에 저장한다.
                  // sb의 내용을 TextArea에 보여준다.
            } 
        });
    }

    textGUI(String title) {
        super(title);
        jmb = new JMenuBar();
        jm_file = new JMenu("File");
        jmi_fileOpen = new JMenuItem("Open");
        jmi_fileSave = new JMenuItem("Save");
        jmi_fileSaveAs = new JMenuItem("Save as...");
        jmi_programExit = new JMenuItem("Exit");
        jm_option = new JMenu("Option");
        jmi_fontSizeUp = new JMenuItem("Font Size Up");
        jmi_fontSizeDown = new JMenuItem("Font Size Down");
        jmi_fontSizeOriginal = new JMenuItem("Font Size Original");
        
        textContents = new TextArea();
        
        jp_North = new JPanel();
        jl_select = new JLabel("Select : ", JLabel.RIGHT);
        tf_select = new TextField(15);
        jl_modify = new JLabel("modify : ", JLabel.RIGHT);
        tf_modify = new TextField(15);
        jp_South = new JPanel();

        for ( int i = 0; i < optionBtn.length; i++ ) {
            optionBtn[i] = new Button(btnName[i]);
        }

        setJMenuBar(jmb);
        jmb.add(jm_file);
        jmb.add(jm_option);
        jm_file.add(jmi_fileOpen);
        jmi_fileOpen.setAccelerator(KeyStroke.getKeyStroke('O', Event.CTRL_MASK));
        jmi_fileOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuffer sb = new StringBuffer();
                defaultPath.addChoosableFileFilter(selectExpansion);
                defaultPath.showOpenDialog(textGUI.this);

                try {
                    BufferedReade
 transientStorage = new BufferedReader(new FileReader(defaultPath.getSelectedFile()));

                    setTitle(defaultPath.getSelectedFile().getName());
                    
                    while ( true ) {
                        String line = transientStorage.readLine();

                        if ( line == null ) break;

                        sb.append(line).append(CR_LF);
                    }

                    textContents.setText(sb.toString());
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        });

        jm_file.add(jmi_fileSave);
        jmi_fileSave.setAccelerator(KeyStroke.getKeyStroke('S', Event.CTRL_MASK));
        jmi_fileSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        jm_file.add(jmi_fileSaveAs);
        jmi_fileSaveAs.setAccelerator(KeyStroke.getKeyStroke('A', Event.CTRL_MASK));
        jmi_fileSaveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                defaultPath.addChoosableFileFilter(selectExpansion);
                defaultPath.showSaveDialog(textGUI.this);
                
                try {
                    String curText = textContents.getText();
                    StringBuffer sb = new StringBuffer(curText.length());
                    PrintWriter pw = new PrintWriter(defaultPath.getSelectedFile());
                    
                    Scanner sc = new Scanner(curText);

                    for ( int i = 0; sc.hasNextLine(); i++ ) {
                        String line = sc.nextLine();
                        pw.println(line);
                    }

                    pw.close();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        });
        
        jm_file.addSeparator();

        jm_file.add(jmi_programExit);
        jmi_programExit.setAccelerator(KeyStroke.getKeyStroke('E', Event.CTRL_MASK));
        jmi_programExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        jm_option.add(jmi_fontSizeUp);
        jmi_fontSizeUp.setAccelerator(KeyStroke.getKeyStroke('Q', Event.ALT_MASK));
        jmi_fontSizeUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Font nowFontInfo = textContents.getFont();
				textContents.setFont(new Font(nowFontInfo.getFontName(), nowFontInfo.getStyle(), nowFontInfo.getSize() + 1));
				revalidate();
				repaint();
			}
        });
        
        jm_option.add(jmi_fontSizeDown);
        jmi_fontSizeDown.setAccelerator(KeyStroke.getKeyStroke('W', Event.ALT_MASK));
        jmi_fontSizeDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Font nowFontInfo = textContents.getFont();
				textContents.setFont(new Font(nowFontInfo.getFontName(), nowFontInfo.getStyle(), nowFontInfo.getSize() - 1));
				revalidate();
				repaint();
			}
        });
        
        jm_option.add(jmi_fontSizeOriginal);
        jmi_fontSizeOriginal.setAccelerator(KeyStroke.getKeyStroke('E', Event.ALT_MASK));
        jmi_fontSizeOriginal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Font nowFontInfo = textContents.getFont();
				textContents.setFont(new Font(nowFontInfo.getFontName(), nowFontInfo.getStyle(), 12));
				revalidate();
				repaint();
			}
        });

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

        // frame이 모니터 중앙에 위치하게 하기
        Toolkit tk = Toolkit.getDefaultToolkit();	// 구현된 Toolkit객체를 얻는다. 
        Dimension screenSize = tk.getScreenSize();	// 화면의 크기를 구한다.
        
        setSize(600, 300);
        int x = screenSize.width / 2 - this.getWidth() / 2;  
        int y = screenSize.height / 2 - this.getHeight() / 2;  
                 
        setLocation(x, y);
        textContents.requestFocus();
        registerEventHandler();
        setVisible(true);
    }
}

public class TextEx {
    public static void main(String[] args) {
        new textGUI("제목 없음.txt");
    }
}