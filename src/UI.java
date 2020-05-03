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
    private HashMap<JButton,JButton>reqButtons=new HashMap<>();
    private JPanel left = new JPanel();
    private JPanel center = new JPanel();
    private JPanel right = new JPanel();

    private JComboBox urlType;
    private JTextField url;
    private JButton send;
    private JPanel header;
    private JPanel body;
    private JComboBox bodyType;
    private JTextArea bodyFiled;
    private JTabbedPane centerTab;
    private JButton save;

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
        mainFrame.getContentPane().setBackground(Color.darkGray);
        left.setBackground(Color.darkGray);
        center.setBackground(Color.darkGray);
        center.setVisible(false);
        mainFrame.setVisible(true);
    }

    public void showGui() {

        ImageIcon jsomniaLogo = new ImageIcon("src/Jsomnia.png");
        JLabel jsomnia = new JLabel(jsomniaLogo);
        jsomnia.setSize(200, 50);
        jsomnia.setLocation(0, 0);
        mainFrame.add(jsomnia);

        MenuBar menuBar = new MenuBar();
        Menu application = new Menu("Application");
        menuBar.add(application);
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
        save = new JButton("SAVE");
        save.setSize(75, 40);
        save.setLocation((width - 200) / 2 - 80, height - 171);
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
        //body.add(bodyType);

        bodyFiled = new JTextArea("...");
        bodyFiled.setLocation(5, 50);
        bodyFiled.setSize((width - 200) / 2 - 10, height - 300);
        body.add(bodyFiled);

        //Center tab

        centerTab = new JTabbedPane();
        centerTab.addTab("Body", body);
        centerTab.addTab("Header", header);
        centerTab.setSize((width - 200) / 2, height - 175);
        centerTab.setLocation(0, 50);
        center.add(centerTab);

        //Right


        right = new JPanel();
        right.setLayout(null);
        right.setSize((width - 200) / 2, height);
        right.setLocation(width - ((width - 200) / 2), 0);
        right.setBackground(Color.darkGray);
        mainFrame.add(right);

    }

    public void reSize() {
        left.setSize(200, height - 100);
        center.setSize((width - 200) / 2, height);
        right.setSize((width - 200) / 2, height);
        right.setLocation(width - ((width - 200) / 2), 0);
        url.setSize((width - 200) / 2 - 150, 40);
        send.setLocation((width - 200) / 2 - 80, 5);
        centerTab.setSize((width - 200) / 2, height - 175);
        bodyFiled.setSize((width - 200) / 2 - 10, height - 300);
        save.setLocation((width - 200) / 2 - 80, height - 171);
    }

    private class NewRequest {
        private JFrame newRequestGetter = new JFrame("New Request");
        private JTextField name;
        private JComboBox reqType;
        private Request request;

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
                requests.add(new Request(name.getText(), reqType.getSelectedItem().toString()));
                int j = 0;
                ImageIcon del = new ImageIcon("src/delete.png");
                delReq delReq = new delReq();
                loadReq loadReq = new loadReq();
                for(JButton button:reqButtons.keySet()){
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
                    reqButtons.put(req,delete);
                    req.setSize(150, 30);
                    req.setLocation(5, 80 + j * 35);
                    req.addActionListener(loadReq);
                    left.add(req);
                    j++;
                }
                newRequestGetter.setVisible(false);
                urlType.setSelectedItem(reqType.getSelectedItem().toString());
                center.setVisible(true);
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
            if (e.getSource().equals(new JButton())) {
                JButton button = (JButton) e.getSource();
                for (Request i : requests) {
                    if (i.getName().equals(button.getName())) {
                        urlType.setSelectedItem(i.getUrlType());
                        bodyFiled.setText(i.getBody());
                        url.setText(i.getUrl());
                        break;
                    }
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
                    requests.remove(i);
                    for(JButton button1 :reqButtons.keySet()){
                        button1.setVisible(false);
                        reqButtons.get(button1).setVisible(false);
                    }
                    reqButtons.clear();
                    int j=0;
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
                        reqButtons.put(req,delete);
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
}
