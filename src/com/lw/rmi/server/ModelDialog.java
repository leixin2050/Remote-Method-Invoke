package com.lw.rmi.server;

import com.lw.rmi.client.RmiClientActioner;
import view.IMecView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author leiWei
 * 添加模态框，目的是为了在远程方法调用时不可进行其他操作
 */
public class ModelDialog extends JDialog {
    private static final long serialVersionUID = -2329909880558481906L;
    private int size = IMecView.normalFont.getSize();
    private int hight = 6 * size;

    //用来内部发送Rmi请求
    private RmiClientActioner rmiClientActioner;

    public ModelDialog(Dialog owner, String title) {
        super(owner, true);
        init(title);
    }

    public void setRmiClientActioner(RmiClientActioner rmiClientActioner) {
        this.rmiClientActioner = rmiClientActioner;
    }

    public ModelDialog(Frame owner, String title) {
        super(owner, true);
        init(title);
    }


    public void init(String title) {
        int weight = (title.length() + 4) * size;
        setSize(weight, hight);
        //显示在父界面的中心
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JLabel jlblTopic = new JLabel(title);
        jlblTopic.setFont(IMecView.normalFont);
        add(jlblTopic);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                try {
                    if (rmiClientActioner == null) {
                        throw new RuntimeException("rmiClientActioner未设置，无法进行RMI请求！");
                    }
                    rmiClientActioner.doRmi();
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    //执行完毕后关闭模态框
                    dispose();
                }
            }
        });
    }

    //展示模态框
    public void showDialog() {
        setVisible(true);
    }
}
