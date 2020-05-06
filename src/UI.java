import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class UI {
    private JFrame mainFrame = new JFrame("Jsomnia");
    private int width = 1000;
    private int height = 600;
    private ArrayList<Request> requests = new ArrayList<>();
    private HashMap<JButton, JButton> reqButtons = new HashMap<>();
    private JPanel left = new JPanel();
    private JPanel center = new JPanel();
    private JPanel right = new JPanel();
    private JPanel leftBorder = new JPanel();
    private JPanel rightBorder = new JPanel();
    private JComboBox urlType;
    private JTextField url;
    private JButton send;
    private JPanel header;
    private JPanel body;
    private JComboBox bodyType;
    private JTextArea bodyFiled;
    private JTabbedPane centerTab;
    private JButton save;
    private HashMap<JLabel, JLabel> headers = new HashMap<>();
    private ArrayList<JButton> headerButtons = new ArrayList<>();
    private String onRequest;

    public UI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        mainFrame.setLayout(null);
        mainFrame.setSize(width, height);
        showGui();
        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                width = mainFrame.getWidth();
                height = mainFrame.getHeight();
                reSize();
            }
        });

        mainFrame.setMinimumSize(new Dimension(600, 400));
        float[] colors = new float[3];
        colors = Color.RGBtoHSB(40, 41, 37, colors);
        mainFrame.getContentPane().setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));
        center.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));
        bodyFiled.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));
        bodyFiled.setForeground(Color.WHITE);
        centerTab.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));
        colors = Color.RGBtoHSB(46, 47, 43, colors);
        left.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));
        body.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));
        centerTab.setForeground(Color.getHSBColor(colors[0], colors[1], colors[2]));
        center.setVisible(false);
        mainFrame.setVisible(true);
    }

    public void showGui() {

        ImageIcon jsomniaLogo = new ImageIcon("src/Jsomnia.png");
        JLabel jsomnia = new JLabel(jsomniaLogo);
        jsomnia.setSize(200, 50);
        jsomnia.setLocation(0, 0);
        mainFrame.add(jsomnia);

        leftBorder.setSize(2, height);
        leftBorder.setLocation(200, 0);
        mainFrame.add(leftBorder);

        leftBorder.setSize(2, height);
        leftBorder.setLocation(200 + (width - 200) / 2, 0);
        mainFrame.add(rightBorder);


        MenuBar menuBar = new MenuBar();

        Menu application = new Menu("Application");
        menuBar.add(application);

        Menu edit = new Menu("Edit");
        menuBar.add(edit);

        Menu view = new Menu("View");
        menuBar.add(view);

        Menu window = new Menu("Window");
        menuBar.add(window);

        Menu tools = new Menu("Tools");
        menuBar.add(tools);

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        help.add(about);
        menuBar.add(help);

        mainFrame.setMenuBar(menuBar);


        //Left

        left.setLayout(null);
        left.setSize(200, height - 100);
        left.setLocation(0, 50);
        mainFrame.add(left);

        //New Request Button

        JButton newRequest = new JButton("New Request");
        newRequest.setSize(150, 50);
        newRequest.setBackground(Color.green);
        newRequest.setLocation(25, 25);
        newRequest.addActionListener(new newRequestHandler());
        left.add(newRequest);

        //Requests list

        int j = 0;
        ImageIcon del = new ImageIcon("src/delete.png");
        for (Request i : requests) {
            JButton delete = new JButton(del);
            delete.setSize(30, 30);
            delete.setLocation(160, 155 + j * 35);
            left.add(delete);
            JButton req = new JButton(i.getName());
            req.setSize(150, 30);
            req.setLocation(5, 135 + j * 35);
            left.add(req);
            j++;
        }

        //Center

        center.setLayout(null);
        center.setSize((width - 200) / 2, height);
        center.setLocation(200, 0);
        mainFrame.add(center);

        //Save Button
        saveReq saveReq = new saveReq();
        save = new JButton("SAVE");
        save.setSize(75, 40);
        save.setLocation((width - 200) / 2 - 80, height - 130);
        save.addActionListener(saveReq);
        center.add(save);


        //URL

        urlType = new JComboBox();
        urlType.addItem("GET");
        urlType.addItem("POST");
        urlType.addItem("PUT");
        urlType.addItem("PATCH");
        urlType.addItem("DELETE");
        urlType.setSize(65, 40);
        urlType.setLocation(0, 5);
        center.add(urlType);

        url = new JTextField("...");
        url.setSize((width - 200) / 2 - 150, 40);
        url.setLocation(65, 5);
        center.add(url);

        send = new JButton("SEND");
        send.setSize(75, 40);
        send.setLocation((width - 200) / 2 - 80, 5);
        center.add(send);

        //Header panel

        header = new JPanel();
        header.setLayout(null);

        newHead newHead = new newHead();
        JButton newHeader = new JButton("New Header");
        newHeader.setSize(100, 50);
        newHeader.setLocation(100, 300);
        newHeader.addActionListener(newHead);
        header.add(newHeader);


        //Body panel

        body = new JPanel();
        body.setLayout(null);

        bodyType = new JComboBox();
        bodyType.addItem("JSON");
        bodyType.addItem("XML");
        bodyType.addItem("YAML");
        bodyType.addItem("EDN");
        bodyType.setSize(65, 40);
        bodyType.setLocation(5, 5);
        body.add(bodyType);

        bodyFiled = new JTextArea("...");
        bodyFiled.setLocation(5, 50);
        bodyFiled.setSize((width - 200) / 2 - 10, height - 270);
        body.add(bodyFiled);

        //Center tab

        centerTab = new JTabbedPane();
        centerTab.addTab("Body", body);
        centerTab.addTab("Header", header);
        centerTab.setSize((width - 200) / 2 - 1, height - 122);
        centerTab.setLocation(2, 50);
        center.add(centerTab);

        //Right


        right = new JPanel();
        right.setLayout(null);
        right.setSize((width - 200) / 2, height);
        right.setLocation(width - ((width - 200) / 2), 0);
        mainFrame.add(right);

    }

    public void reSize() {

        url.setSize((width - 200) / 2 - 150, 40);
        send.setLocation((width - 200) / 2 - 80, 5);
        centerTab.setSize((width - 200) / 2 - 1, height - 122);
        bodyFiled.setSize((width - 200) / 2 - 10, height - 270);
        save.setLocation((width - 200) / 2 - 80, height - 130);
        leftBorder.setLocation(200, 0);
        leftBorder.setSize(2, height);
        rightBorder.setLocation(200 + (width - 200) / 2, 0);
        rightBorder.setSize(2, height);
        left.setSize(200, height - 100);
        center.setSize((width - 200) / 2, height);
        right.setSize((width - 200) / 2, height);
        right.setLocation(width - ((width - 200) / 2), 0);

    }

    private class NewRequest {
        private JFrame newRequestGetter = new JFrame("New Request");
        private JTextField name;
        private JComboBox reqType;

        public NewRequest() {
            newRequestGetter.setLayout(null);
            newRequestGetter.setSize(400, 150);

            JLabel namePrint = new JLabel("Name");
            namePrint.setVisible(true);
            namePrint.setLocation(5, 5);
            namePrint.setSize(40, 40);
            newRequestGetter.add(namePrint);


            name = new JTextField();
            name.setSize(250, 40);
            name.setLocation(50, 5);
            newRequestGetter.add(name);

            reqType = new JComboBox();
            reqType.addItem("GET");
            reqType.addItem("POST");
            reqType.addItem("PUT");
            reqType.addItem("PATCH");
            reqType.addItem("DELETE");
            reqType.setSize(65, 40);
            reqType.setLocation(300, 5);
            newRequestGetter.add(reqType);
            newRequestGetter.setVisible(true);

            JButton create = new JButton("CREATE");
            create.addActionListener(new createReqHandler());
            create.setSize(100, 40);
            create.setLocation(125, 50);
            newRequestGetter.add(create);

        }

        private class createReqHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                Request newRequest = new Request(name.getText(), reqType.getSelectedItem().toString());
                requests.add(newRequest);
                int j = 0;
                ImageIcon del = new ImageIcon("src/delete.png");
                delReq delReq = new delReq();
                loadReq loadReq = new loadReq();
                for (JButton button : reqButtons.keySet()) {
                    button.setVisible(false);
                    reqButtons.get(button).setVisible(false);
                }
                reqButtons.clear();
                for (Request i : requests) {
                    JButton delete = new JButton(del);
                    delete.setName(i.getName());
                    delete.setSize(30, 30);
                    delete.setLocation(160, 80 + j * 35);
                    delete.addActionListener(delReq);
                    left.add(delete);
                    JButton req = new JButton(i.getName());
                    reqButtons.put(req, delete);
                    req.setSize(150, 30);
                    req.setLocation(5, 80 + j * 35);
                    req.addActionListener(loadReq);
                    left.add(req);
                    j++;
                }
                newRequestGetter.setVisible(false);
                urlType.setSelectedItem(reqType.getSelectedItem().toString());
                center.setVisible(true);
                onRequest = name.getText();
            }
        }
    }

    private class newRequestHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            NewRequest newRequest = new NewRequest();
        }
    }

    private class loadReq implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            ImageIcon del = new ImageIcon("src/delete.png");
            for (Request i : requests) {
                if (i.getName().equals(button.getText())) {
                    urlType.setSelectedItem(i.getUrlType().toString());
                    bodyFiled.setText(i.getBody());
                    url.setText(i.getUrl());
                    for(JLabel label:headers.keySet()){
                        label.setVisible(false);
                        headers.get(label).setVisible(false);
                    }
                    for(JButton button1:headerButtons){
                        button1.setVisible(false);
                    }
                    headerButtons.clear();
                    headers.clear();
                    for (String head : i.getHeaders().keySet()) {
                        headers.put(new JLabel(head), new JLabel(i.getHeaders().get(head)));
                    }
                    int k = 0;
                    JLabel value;
                    delHeader delHeader=new delHeader();
                    for (JLabel label : headers.keySet()) {
                        label.setSize(100, 30);
                        label.setLocation(10, 20 + k * 40);
                        value = headers.get(label);
                        value.setSize(100, 30);
                        value.setLocation(115, 20 + k * 40);
                        header.add(label);
                        header.add(value);
                        label.setVisible(true);
                        value.setVisible(true);
                        JButton headerButton = new JButton(del);
                        headerButton.addActionListener(delHeader);
                        headerButton.setName(label.getText());
                        headerButton.setSize(30, 30);
                        headerButton.setLocation(220, 20 + k * 40);
                        headerButtons.add(headerButton);
                        header.add(headerButton);
                        k++;
                    }
                    onRequest = i.getName();
                    center.setVisible(true);
                    break;
                }
            }
        }
    }

    private class saveReq implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (Request request : requests) {
                if (request.getName().equals(onRequest)) {
                    request.setBody(bodyFiled.getText());
                    request.setUrl(url.getText());
                    request.setUrlType(urlType.getSelectedItem().toString());
                }
            }
        }
    }

    private class delReq implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();

            for (Request i : requests) {
                if (i.getName().equals(button.getName())) {
                    if (onRequest.equals(i.getName())) {
                        bodyFiled.setText("");
                        url.setText("");
                        urlType.setSelectedItem("GET");
                        bodyType.setSelectedItem("JSON");
                        headers.clear();
                        center.setVisible(false);
                    }
                    requests.remove(i);
                    for (JButton button1 : reqButtons.keySet()) {
                        button1.setVisible(false);
                        reqButtons.get(button1).setVisible(false);
                    }
                    headers.clear();
                    reqButtons.clear();
                    int j = 0;
                    delReq delReq = new delReq();
                    loadReq loadReq = new loadReq();
                    ImageIcon del = new ImageIcon("src/delete.png");
                    for (Request k : requests) {
                        JButton delete = new JButton(del);
                        delete.setName(k.getName());
                        delete.setSize(30, 30);
                        delete.setLocation(160, 80 + j * 35);
                        delete.addActionListener(delReq);
                        left.add(delete);
                        JButton req = new JButton(k.getName());
                        reqButtons.put(req, delete);
                        req.setSize(150, 30);
                        req.setLocation(5, 80 + j * 35);
                        req.addActionListener(loadReq);
                        left.add(req);
                        j++;
                    }
                    break;
                }
            }
        }
    }

    private class newHead implements ActionListener {
        private JTextField headerFiled = new JTextField();
        private JTextField valueFiled = new JTextField();
        private JFrame newHead = new JFrame("New Header");

        @Override
        public void actionPerformed(ActionEvent e) {
            newHead.setLayout(null);
            newHead.setSize(400, 150);

            JLabel header = new JLabel("Header:");
            header.setLocation(5, 20);
            header.setSize(45, 40);
            newHead.add(header);

            headerFiled.setSize(135, 40);
            headerFiled.setLocation(55, 20);
            newHead.add(headerFiled);

            JLabel value = new JLabel("Value:");
            value.setLocation(200, 20);
            value.setSize(45, 40);
            newHead.add(value);

            valueFiled.setSize(135, 40);
            valueFiled.setLocation(245, 20);
            newHead.add(valueFiled);

            addHeader addHeader = new addHeader();
            JButton add = new JButton("ADD");
            add.setSize(70, 35);
            add.setLocation(160, 65);
            add.addActionListener(addHeader);
            newHead.add(add);

            newHead.setVisible(true);

        }

        private class addHeader implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JLabel label : headers.keySet()) {
                    label.setVisible(false);
                    headers.get(label).setVisible(false);
                }
                for (JButton button : headerButtons) {
                    button.setVisible(false);
                }
                headerButtons.clear();
                for (Request i : requests) {
                    if (i.getName().equals(onRequest)) {
                        i.addHeader(headerFiled.getText(), valueFiled.getText());
                        JLabel head = new JLabel(headerFiled.getText());
                        JLabel value = new JLabel(valueFiled.getText());
                        headers.put(head, value);
                        int k = 0;
                        delHeader delHeader=new delHeader();
                        ImageIcon del = new ImageIcon("src/delete.png");
                        for (JLabel label : headers.keySet()) {
                            label.setSize(100, 30);
                            label.setLocation(10, 20 + k * 40);
                            value = headers.get(label);
                            value.setSize(100, 30);
                            value.setLocation(115, 20 + k * 40);
                            header.add(label);
                            header.add(value);
                            label.setVisible(true);
                            value.setVisible(true);
                            JButton headerButton = new JButton(del);
                            headerButton.addActionListener(delHeader);
                            headerButton.setName(label.getText());
                            headerButton.setSize(30, 30);
                            headerButton.setLocation(220, 20 + k * 40);
                            headerButtons.add(headerButton);
                            header.add(headerButton);
                            k++;
                        }
                        valueFiled.setText("");
                        headerFiled.setText("");
                        newHead.setVisible(false);
                        break;
                    }
                }
            }
        }
    }
    private class delHeader implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton delete = (JButton) e.getSource();
            for (Request i : requests) {
                if (i.getName().equals(onRequest)) {
                    i.getHeaders().remove(delete.getName());
                    break;
                }
            }
            for (JLabel label : headers.keySet()) {
                label.setVisible(false);
                headers.get(label).setVisible(false);
            }
            for(JLabel label:headers.keySet()){
                if(label.getText().equals(delete.getName())){
                    headers.remove(label);
                    break;
                }
            }
            for (JButton button : headerButtons) {
                button.setVisible(false);
            }
            headerButtons.clear();
            int k = 0;
            JLabel value;
            delHeader delHeader=new delHeader();
            ImageIcon del = new ImageIcon("src/delete.png");
            for (JLabel label : headers.keySet()) {
                label.setSize(100, 30);
                label.setLocation(10, 20 + k * 40);
                value = headers.get(label);
                value.setSize(100, 30);
                value.setLocation(115, 20 + k * 40);
                header.add(label);
                header.add(value);
                label.setVisible(true);
                value.setVisible(true);
                JButton headerButton = new JButton(del);
                headerButton.addActionListener(delHeader);
                headerButton.setName(label.getText());
                headerButton.setSize(30, 30);
                headerButton.setLocation(220, 20 + k * 40);
                headerButtons.add(headerButton);
                header.add(headerButton);
                k++;
            }
        }
    }
}
