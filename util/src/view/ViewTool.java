package view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ViewTool {
	public static final String TIP_MESS = "温馨提示";
	
	public ViewTool() {
	}

	public static void showInformation(JFrame parent, String message) {
		JOptionPane.showMessageDialog(parent, message, 
				TIP_MESS, JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showWarning(JFrame parent, String message) {
		JOptionPane.showMessageDialog(parent, message, 
				TIP_MESS, JOptionPane.WARNING_MESSAGE);
	}

	public static void showError(JFrame parent, String message) {
		JOptionPane.showMessageDialog(parent, message, 
				TIP_MESS, JOptionPane.ERROR_MESSAGE);
	}

	//选择功能
	public static boolean selectYesNo(JFrame parent, String message) {
		int choice = JOptionPane.showConfirmDialog(parent, message, 
				TIP_MESS, JOptionPane.YES_NO_OPTION);
		return choice == JOptionPane.YES_OPTION;
	}
	
}
