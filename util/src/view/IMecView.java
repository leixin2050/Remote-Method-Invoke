package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JFrame;

public interface IMecView {
	Font topicFont = new Font("微软雅黑", Font.BOLD, 30);
	Font normalFont = new Font("宋体", Font.PLAIN, 16);
	Font buttonFont = new Font("宋体", Font.PLAIN, 12);
	
	Color topicColor = new Color(3, 3, 121);
	
	Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	
	int PADDING = 5;
	int MARGIN = 5;
	
	default void initView() {
		init();
		dealAction();
	}

	JFrame getFrame();
	
	void init();
	void dealAction();
	
	void beforeShowView();
	void afterShowView();
	
	boolean beforeCloseView();
	void afterCloseView();
	
	default void showView() {
		JFrame jfrmView = getFrame();
		
		if (jfrmView == null) {
			throw new FrameNotExistException("Frame未定义");
		}
		
		beforeShowView();
		jfrmView.setVisible(true);
		afterShowView();
	}
	
	default void closeView() {
		JFrame jfrmView = getFrame();
		
		if (jfrmView == null) {
			throw new FrameNotExistException("Frame未定义");
		}
		
		if (beforeCloseView()) {
			jfrmView.dispose();
			afterCloseView();
		}
	}
	
}
