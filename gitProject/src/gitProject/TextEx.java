package gitProject;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.*;

class textGUI extends JFrame {
    TextArea 	textContents;			// 내용입력창
    JPanel 		jp_North, jp_South;		// 특정 단어 입력 구역과 옵션 버튼 구역
    JLabel 		jl_select, jl_modify;	// 특정 단어 입력 구역에 쓰일 글자
    TextField   tf_select, tf_modify;	// 특정 단어 입력칸
    JMenuBar    jmb;                    // 메뉴바
    JMenu       jm_file;                // 파일 메뉴
    JMenuItem
            jmi_fileOpen,       // 파일 열기
            jmi_fileSave,       // 파일 저장
            jmi_fileSaveAs,
            jmi_programExit;    // 프로그램 종료
    JFileChooser defaultPath = new JFileChooser("c:/");
    FileFilter selectExpansion = new FileNameExtensionFilter("텍스트 문서(*.txt)", "txt");

    // 옵션 버튼 이름
    String[] btnName = {
            "Undo",
            "Delete_EvenLine",
            "Delete_Letter",
            "Delete_EmptyLine",
            "Delete_EmptySpace",
            "Insert_prefix",
            "Delete_prefix",
    };

    // 옵션 버튼 객체
    Button[] optionBtn = new Button[btnName.length];

    // 개행 문자. 한 줄 띄워주는 변수.
    private final String CR_LF = System.getProperty("line.separator");

    // 이전으로 가기버튼 등에 필요
    private String prevText = "";

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

        // 라인마다 앞, 뒤에 있는 공간 없애기
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

        // 라인마다 앞, 뒤에 입력한 문자 붙이기
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

                Scanner sc = new Scanner(curText);
                String param1 = tf_select.getText();
                String param2 = tf_modify.getText();

                for ( int i = 0; sc.hasNextLine(); i++ ) {
                    String line = sc.nextLine();
                    System.out.print("line : " + line);
                    String temp = " ";
                    String tempGapSpace = "";
                    int selectNum = 0;

                    while ( true ) {
                        char ch = line.charAt(selectNum);
                        if ( temp.indexOf(ch) == -1 ) {
                            break;
                        } else {
                            selectNum++;
                        }
                    }

                    line = line.trim();

                    String beforeParam = line.substring(0, param1.length());
                    System.out.print("beforeParam : " + beforeParam);
                    String afterParam = line.substring(line.length() - param2.length());
                    System.out.print("afterParam : " + afterParam);

                    for ( int j = 0; j < selectNum; j++ ) {
                        tempGapSpace += " ";
                    }
                    
                    System.out.print("tempGapSpace : " + tempGapSpace);

                    if ( beforeParam.equals(param1) && afterParam.equals(param2) ) {
                    	System.out.print("line : " + line);
                    	line = line.substring(param1.length(), line.length() - param2.length());
                    	System.out.print("line : " + line);
                        sb.append(tempGapSpace).append(line).append(CR_LF);
                        System.out.println(sb);
                    } else {
                        sb.append(line).append(CR_LF);
                        System.out.println("NO");
                    }
                }
                textContents.setText(sb.toString());
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
        jm_file.add(jmi_fileOpen);
        jmi_fileOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuffer sb = new StringBuffer();
                defaultPath.addChoosableFileFilter(selectExpansion);
                defaultPath.showOpenDialog(textGUI.this);

                try {
                    BufferedReader transientStorage = new BufferedReader(new FileReader(defaultPath.getSelectedFile()));

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
        jmi_fileSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        jm_file.add(jmi_fileSaveAs);
        jmi_fileSaveAs.addActionListener(new ActionListener() {
            @Override
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
        jmi_programExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
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