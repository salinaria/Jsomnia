import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;
import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UI {

    private JFrame mainFrame = new JFrame("Jsomnia");
    private ArrayList<Request> requests = new ArrayList<>();
    private int width = 1000;
    private int height = 600;
    private Top top = new Top();
    private Left left = new Left();
    private Center center = new Center();
    private Right right = new Right();
    private boolean followRedirect = false;
    private boolean systemTray = false;
    private JPanel topCenter;
    private JPanel topRight;
    private JPanel rightBorder;

    public UI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        FileReader reader = null;
        try {
            reader = new FileReader("src/Options.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        try {
            followRedirect = (char) reader.read() == '1';
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            systemTray = (char) reader.read() == '1';
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        mainFrame.setLayout(null);
        mainFrame.setSize(width, height);
        mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                width = mainFrame.getWidth();
                height = mainFrame.getHeight();
                reSize();
            }
        });
        close();

        //Set Color
        float[] colors = new float[3];
        colors = Color.RGBtoHSB(40, 41, 37, colors);
        mainFrame.getContentPane().setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));

        topCenter = new JPanel();
        topCenter.setBackground(Color.WHITE);
        topCenter.setSize((width - 200) / 2 - 1, 50);
        topCenter.setLocation(200, 0);
        topCenter.setOpaque(true);
        mainFrame.add(topCenter);

        topRight = new JPanel();
        topRight.setBackground(Color.WHITE);
        topRight.setSize((width - 200) / 2, 50);
        topRight.setLocation(200 + (width - 200) / 2, 0);
        topRight.setOpaque(true);
        mainFrame.add(topRight);

        rightBorder = new JPanel();
        rightBorder.setBackground(Color.WHITE);
        rightBorder.setSize(1, height);
        rightBorder.setLocation(200 + (width - 200) / 2, 0);
        rightBorder.setOpaque(true);
        mainFrame.add(rightBorder);


        mainFrame.setJMenuBar(top);
        mainFrame.add(left);
        mainFrame.add(center);
        mainFrame.add(right);
        mainFrame.setVisible(true);

    }

    public void reSize() {
        topCenter.setSize((width - 200) / 2, 50);
        topCenter.setLocation(200, 0);
        topRight.setSize((width - 200) / 2, 50);
        topRight.setLocation(200 + (width - 200) / 2, 0);
        rightBorder.setSize(1, height);
        rightBorder.setLocation(200 + (width - 200) / 2, 0);

        left.reSize();
        center.reSize();
        right.reSize();
    }

    private class fullScreenHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            reSize();
        }
    }

    private class Top extends JMenuBar {
        private JMenu application = new JMenu("Application");
        private JMenuItem options = new JMenuItem("Options");
        private JMenuItem exit = new JMenuItem("Exit");

        private JMenu view = new JMenu("View");
        private JMenuItem fullScreen = new JMenuItem("Toggle Full Screen");
        private JMenuItem sideBar = new JMenuItem("Toggle Side Bar");

        private JMenu help = new JMenu("Help");
        private JMenuItem about = new JMenuItem("About");
        private JMenuItem helpItem = new JMenuItem("Help");

        public Top() {
            this.add(application);
            application.add(options);
            options.addActionListener(new optionHandler());
            options.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
            application.add(exit);

            this.add(view);
            view.add(fullScreen);
            fullScreen.addActionListener(new UI.fullScreenHandler());
            fullScreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
            view.add(sideBar);

            this.add(help);
            help.add(about);
            about.addActionListener(new aboutHandler());
            about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));
            help.add(helpItem);
            helpItem.addActionListener(new helpHandler());
            helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK));
        }

        private class aboutHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame aboutPage = new JFrame("About");
                aboutPage.getContentPane().setBackground(Color.magenta);
                aboutPage.setLayout(null);
                aboutPage.setSize(600, 300);
                JLabel label = new JLabel("Hello!");
                JLabel label1 = new JLabel("I'm Ali Ansari study CE at Amirkabir university and my StudentNumber is 9831136");
                JLabel label3 = new JLabel("I'm developer @GaboorStudio");
                JLabel label4 = new JLabel("Email: a.ansari3103@gmail.com  Phone:09350774076");
                label.setSize(500, 40);
                label1.setSize(500, 40);
                label3.setSize(500, 40);
                label4.setSize(500, 40);
                label.setLocation(10, 10);
                label1.setLocation(10, 60);
                label3.setLocation(10, 110);
                label4.setLocation(10, 160);
                aboutPage.add(label);
                aboutPage.add(label1);
                aboutPage.add(label3);
                aboutPage.add(label4);

                aboutPage.setVisible(true);
            }
        }

        private class helpHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame helpPage = new JFrame("Help");
                helpPage.setVisible(true);

            }
        }

        private class optionHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame optionPage = new JFrame("Options");
                optionPage.setLayout(null);
                optionPage.setSize(300, 150);
                JCheckBox followRedirectBox = new JCheckBox("Follow Redirect");
                followRedirectBox.addActionListener(new followHandler());
                followRedirectBox.setSize(500, 40);
                followRedirectBox.setLocation(10, 10);
                if (followRedirect)
                    followRedirectBox.setSelected(true);
                else
                    followRedirectBox.setSelected(false);
                optionPage.add(followRedirectBox);
                JCheckBox systemTrayBox = new JCheckBox("System Tray");
                if (systemTray)
                    systemTrayBox.setSelected(true);
                else
                    systemTrayBox.setSelected(false);
                systemTrayBox.setSize(500, 40);
                systemTrayBox.setLocation(10, 50);
                systemTrayBox.addActionListener(new trayHandler());
                optionPage.add(systemTrayBox);
                optionPage.setVisible(true);
            }

            private class trayHandler implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    systemTray = ((JCheckBox) e.getSource()).isSelected();
                    try {
                        FileWriter writer = new FileWriter("src/Options.txt");
                        if (followRedirect) writer.write('1');
                        else writer.write('0');
                        if (systemTray) writer.write('1');
                        else writer.write('0');
                        writer.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            private class followHandler implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    followRedirect = ((JCheckBox) e.getSource()).isSelected();
                    try {
                        FileWriter writer = new FileWriter("src/Options.txt");
                        if (followRedirect) writer.write('1');
                        else writer.write('0');
                        if (systemTray) writer.write('1');
                        else writer.write('0');
                        writer.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public void close() {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (systemTray) {
                    mainFrame.setVisible(false);
                    SystemTray tray = SystemTray.getSystemTray();
                    PopupMenu popupMenu = new PopupMenu();
                    MenuItem open = new MenuItem("Open");
                    open.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            mainFrame.setVisible(true);
                        }
                    });
                    MenuItem exit = new MenuItem("Exit");
                    exit.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                    });
                    popupMenu.add(open);
                    popupMenu.add(exit);
                    Image image = Toolkit.getDefaultToolkit().getImage("src/trayIcon.png");
                    TrayIcon trayIcon = new TrayIcon(image, "Jsomnia", popupMenu);
                    try {
                        tray.add(trayIcon);
                    } catch (AWTException awtException) {
                        awtException.printStackTrace();
                    }
                } else {
                    System.exit(0);
                }
            }
        });
    }

    private class Left extends JPanel {
        private JButton newRequest = new JButton("New Request");
        private ArrayList<JButton> loadRequests = new ArrayList<>();
        private ArrayList<JButton> delRequests = new ArrayList<>();

        public void reSize() {
            this.setSize(200, height - 70);
        }

        public Left() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            this.setLayout(null);
            this.setSize(200, height - 70);
            this.setLocation(0, 0);
            this.setVisible(true);

            //Set Color
            float[] colors = new float[3];
            colors = Color.RGBtoHSB(46, 47, 43, colors);
            this.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));

            //App logo
            ImageIcon jsomnia = new ImageIcon("src/Jsomnia.png");
            JLabel jsomniaLabel = new JLabel(jsomnia);
            jsomniaLabel.setSize(200, 50);
            jsomniaLabel.setLocation(0, 0);
            this.add(jsomniaLabel);

            //New Request Button
            JButton newRequest = new JButton("New Request");
            newRequest.setSize(150, 50);
            newRequest.setBackground(Color.green);
            newRequest.setLocation(25, 75);
            newRequest.addActionListener(new newRequestHandler());
            newRequest.setMnemonic('N');
            this.add(newRequest);
            ShowRequests();
        }

        public void ShowRequests() {
            for (JButton button : loadRequests) {
                button.setVisible(false);
            }
            loadRequests.clear();
            for (JButton button : delRequests) {
                button.setVisible(false);
            }
            delRequests.clear();
            int j = 0;
            for (Request request : requests) {

                //Load Request Button
                JButton loadRequestButton = new JButton(request.getName());
                loadRequestButton.setSize(150, 30);
                loadRequestButton.setLocation(5, 150 + j * 40);
                loadRequestButton.setName(request.getName());
                loadRequestButton.addActionListener(new loadRequestHandler());
                this.add(loadRequestButton);
                loadRequests.add(loadRequestButton);

                //Delete Request Button
                JButton deleteRequestButton = new JButton(new ImageIcon("src/delete.png"));
                deleteRequestButton.setBackground(Color.RED);
                deleteRequestButton.setSize(30, 30);
                deleteRequestButton.setLocation(160, 150 + j * 40);
                deleteRequestButton.setName(request.getName());
                deleteRequestButton.addActionListener(new delRequestHandler());
                this.add(deleteRequestButton);
                delRequests.add(deleteRequestButton);
                j++;
            }
            updateUI();
        }

        private class newRequestHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewRequest newRequest = new NewRequest();
            }
        }

        private class loadRequestHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                Center.Header header = center.getHeader();
                center.setOnRequest(button.getName());
                for (JLabel headerLabel : header.getHeaders().keySet()) {
                    headerLabel.setVisible(false);
                    header.getHeaders().get(headerLabel).setVisible(false);
                }
                header.getHeaders().clear();
                for (JButton button1 : header.getDelHeader()) {
                    button1.setVisible(false);
                }
                header.getDelHeader().clear();
                for (JCheckBoxMenuItem checkBoxMenuItem : header.getCheckHeaders()) {
                    checkBoxMenuItem.setVisible(false);
                }
                header.getCheckHeaders().clear();
                for (Request request : requests) {
                    if (button.getName().equals(request.getName())) {
                        center.setTopCenter(request);
                        center.setBody(request);
                        center.getHeader().ShowHeader();
                    }
                }
            }
        }

        private class delRequestHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                for (Request request : requests) {
                    if (request.getName().equals(button.getName())) {
                        if (request.getName().equals(center.getOnRequest())) {
                            Request request1 = new Request("", "GET");
                            center.setTopCenter(request1);
                            center.setBody(request1);
                            center.setVisible(false);
                        }
                        requests.remove(request);
                        break;
                    }
                }
                ShowRequests();
            }
        }
    }

    private class NewRequest {
        private JFrame newRequestPage = new JFrame("New Request");
        private JTextField name = new JTextField();
        private JComboBox urlType = new JComboBox();
        private JButton create = new JButton("CREATE");

        public NewRequest() {
            newRequestPage.setLayout(null);
            newRequestPage.setSize(400, 150);

            JLabel namePrint = new JLabel("Name");
            namePrint.setSize(40, 40);
            namePrint.setLocation(5, 5);
            namePrint.setVisible(true);
            newRequestPage.add(namePrint);

            name.setSize(250, 40);
            name.setLocation(50, 5);
            newRequestPage.add(name);

            urlType.addItem("GET");
            urlType.addItem("POST");
            urlType.addItem("PUT");
            urlType.addItem("PATCH");
            urlType.addItem("DELETE");
            urlType.setSize(65, 40);
            urlType.setLocation(300, 5);
            newRequestPage.add(urlType);

            create.addActionListener(new createHandler());
            create.setSize(100, 40);
            create.setLocation(125, 50);
            newRequestPage.add(create);

            newRequestPage.setVisible(true);

        }

        private class createHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                Request request = new Request(name.getText(), urlType.getSelectedItem().toString());
                requests.add(request);
                left.ShowRequests();
                center.setOnRequest(name.getText());
                center.setTopCenter(request);
                center.setBody(request);
                center.getHeader().ShowHeader();
                newRequestPage.setVisible(false);
                center.setVisible(true);
                topCenter.setVisible(false);
            }
        }
    }

    private class Center extends JPanel {
        private JTabbedPane type = new JTabbedPane();
        private String onRequest;
        private Header header = new Header();
        private Body body = new Body();
        private TopCenter topCenter = new TopCenter();


        public void reSize() {
            this.setSize((width - 200) / 2, height - 75);
            type.setSize((width - 200) / 2 - 10, height - 85);
            header.reSize();
            body.reSize();
            topCenter.reSize();

        }

        public String getOnRequest() {
            return onRequest;
        }

        public void setOnRequest(String onRequest) {
            this.onRequest = onRequest;
        }

        public void setTopCenter(Request request) {
            topCenter.setUrl(request.getUrl());
            topCenter.setUrlType(request.getUrlType());
        }

        public TopCenter getTopCenter() {
            return topCenter;
        }

        public void setBody(Request request) {
            body.setBodyType(request.getBodyType());
            body.setJson(request.getJson());
        }

        public Header getHeader() {
            return header;
        }

        public Center() {
            this.setVisible(false);
            this.setSize((width - 200) / 2, height - 75);
            this.setLocation(200, 0);
            this.setLayout(null);

            //Set Color
            float[] colors = new float[3];
            colors = Color.RGBtoHSB(40, 41, 37, colors);
            this.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));


            this.add(topCenter);

            type.addTab("Body", body);
            type.addTab("Headers", header);
            type.setSize((width - 200) / 2, height - 85);
            type.setLocation(0, 55);
            this.add(type);
            updateUI();
        }

        private class Header extends JPanel {
            private HashMap<JLabel, JLabel> headers = new HashMap<>();
            private ArrayList<JButton> delHeader = new ArrayList<>();
            private ArrayList<JCheckBoxMenuItem> checkHeaders = new ArrayList<>();
            private JButton newHeader = new JButton("New Header");

            public void reSize() {
                newHeader.setLocation((width - 200) / 2 - 120, height - 210);
                int k = 0;
                for (JLabel header : headers.keySet()) {
                    header.setSize((width - 200) / 6, 30);
                    header.setLocation(10, 20 + k * 40);
                    JLabel value = headers.get(header);
                    value.setSize((width - 200) / 6, 30);
                    value.setLocation((width - 200) / 6 + 20, 20 + k * 40);
                    k++;
                }
                k = 0;
                for (JCheckBoxMenuItem checkBoxMenuItem : checkHeaders) {
                    checkBoxMenuItem.setLocation((width - 200) / 3 + 30, 23 + k * 40);
                    k++;
                }
                k = 0;
                for (JButton button : delHeader) {
                    button.setLocation((width - 200) / 3 + 60, 20 + k * 40);
                    k++;
                }

            }

            public HashMap<JLabel, JLabel> getHeaders() {
                return headers;
            }

            public ArrayList<JButton> getDelHeader() {
                return delHeader;
            }

            public ArrayList<JCheckBoxMenuItem> getCheckHeaders() {
                return checkHeaders;
            }

            public Header() {
                this.setLayout(null);
                this.setSize((width - 200) / 2 - 20, height - 165);
                this.setLocation(5, 5);
                //Set Color
                float[] colors = new float[3];
                colors = Color.RGBtoHSB(40, 41, 37, colors);
                this.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));

                //New Header Button
                newHeader.setSize(100, 50);
                newHeader.setLocation((width - 200) / 2 - 120, height - 210);
                newHeader.addActionListener(new newHeaderHandler());
                ShowHeader();
                this.add(newHeader);
            }

            public void ShowHeader() {
                for (Request request : requests) {
                    if (onRequest.equals(request.getName())) {
                        for (JLabel header : headers.keySet()) {
                            header.setVisible(false);
                            headers.get(header).setVisible(false);
                        }
                        headers.clear();
                        for (JButton button : delHeader) {
                            button.setVisible(false);
                        }
                        delHeader.clear();
                        for (JCheckBoxMenuItem checkBoxMenuItem : checkHeaders) {
                            checkBoxMenuItem.setVisible(false);
                        }
                        checkHeaders.clear();
                        int k = 0;
                        for (String header : request.getHeaders().keySet()) {
                            JLabel headerLabel = new JLabel(header);
                            headerLabel.setBackground(Color.WHITE);
                            headerLabel.setOpaque(true);
                            headerLabel.setSize((width - 200) / 6, 30);
                            headerLabel.setLocation(10, 20 + k * 40);
                            this.add(headerLabel);

                            JLabel valueLabel = new JLabel(request.getHeaders().get(header));
                            valueLabel.setBackground(Color.WHITE);
                            valueLabel.setOpaque(true);
                            valueLabel.setSize((width - 200) / 6, 30);
                            valueLabel.setLocation((width - 200) / 6 + 20, 20 + k * 40);
                            this.add(valueLabel);

                            headers.put(headerLabel, valueLabel);

                            JCheckBoxMenuItem headerCheck = new JCheckBoxMenuItem();
                            headerCheck.setSize(25, 25);
                            headerCheck.setLocation((width - 200) / 3 + 30, 23 + k * 40);
                            headerCheck.setBackground(Color.WHITE);
                            headerCheck.setOpaque(true);
                            headerCheck.setName(header);
                            checkHeaders.add(headerCheck);
                            this.add(headerCheck);

                            JButton delButton = new JButton(new ImageIcon("src/delete.png"));
                            delButton.setBackground(Color.RED);
                            delButton.setSize(30, 30);
                            delButton.setLocation((width - 200) / 3 + 60, 20 + k * 40);
                            delButton.addActionListener(new delHeaderHandler());
                            delButton.setName(header);
                            delHeader.add(delButton);
                            this.add(delButton);

                            k++;
                        }
                    }
                }
                updateUI();
            }

            private class delHeaderHandler implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton del = (JButton) e.getSource();
                    for (Request request : requests) {
                        if (request.getName().equals(onRequest)) {
                            for (String header : request.getHeaders().keySet()) {
                                if (del.getName().equals(header)) {
                                    request.removeHeader(header);
                                    break;
                                }
                            }
                        }
                    }
                    ShowHeader();
                }
            }

            private class newHeaderHandler implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    newHeaderPage newHeaderPage = new newHeaderPage();
                }
            }

            private class newHeaderPage {
                JFrame newHeader = new JFrame("New Header");
                JTextField header = new JTextField();
                JTextField value = new JTextField();
                JButton add = new JButton("ADD");

                public newHeaderPage() {
                    newHeader.setLayout(null);
                    newHeader.setSize(400, 150);

                    JLabel headerPrint = new JLabel("Header:");
                    headerPrint.setLocation(5, 20);
                    headerPrint.setSize(45, 40);
                    newHeader.add(headerPrint);

                    header.setSize(135, 40);
                    header.setLocation(55, 20);
                    newHeader.add(header);

                    JLabel valuePrint = new JLabel("Value:");
                    valuePrint.setLocation(200, 20);
                    valuePrint.setSize(45, 40);
                    newHeader.add(valuePrint);

                    value.setSize(135, 40);
                    value.setLocation(245, 20);
                    newHeader.add(value);

                    add.setSize(70, 35);
                    add.setLocation(160, 65);
                    add.addActionListener(new addHandler());
                    newHeader.add(add);

                    newHeader.setVisible(true);
                }

                private class addHandler implements ActionListener {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        newHeader.setVisible(false);
                        for (Request request : requests) {
                            if (request.getName().equals(onRequest)) {
                                request.addHeader(header.getText(), value.getText());
                                ShowHeader();
                            }
                        }
                    }
                }
            }
        }

        private class Body extends JPanel {
            private JComboBox bodyType = new JComboBox();
            private FormData formData = new FormData();
            private BinaryFile binaryFile = new BinaryFile();
            private Json json = new Json();

            public String getBodyType() {
                return (String) bodyType.getSelectedItem();
            }

            public void setBodyType(String bodyType) {
                this.bodyType.setSelectedItem(bodyType);
            }

            public void setJson(String text) {
                json.setJsonBody(text);
            }

            public String getJson() {
                return json.getJsonBody();
            }

            public Body() {
                this.setSize((width - 200) / 2 - 20, height - 165);
                this.setLocation(5, 5);
                this.setLayout(null);

                //Set Color
                float[] colors = new float[3];
                colors = Color.RGBtoHSB(40, 41, 37, colors);
                this.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));

                bodyType.addItem("Form Data");
                bodyType.addItem("Binary File");
                bodyType.addItem("JSON");
                bodyType.addActionListener(new bodyTypeHandler());
                bodyType.setSize(100, 40);
                bodyType.setLocation(5, 5);
                this.add(bodyType);

                this.add(json);
                this.add(binaryFile);
                this.add(formData);
                updateUI();
            }

            public void reSize() {
                this.setSize((width - 200) / 2 - 20, height - 165);
                json.reSize();
                binaryFile.reSize();
            }

            private class bodyTypeHandler implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String bdType = (String) bodyType.getSelectedItem();
                    if (bdType.equals("Form Data")) {
                        binaryFile.setVisible(false);
                        json.setVisible(false);
                        formData.setVisible(true);
                    } else if (bdType.equals("Binary File")) {
                        formData.setVisible(false);
                        json.setVisible(false);
                        binaryFile.setVisible(true);
                    } else {
                        binaryFile.setVisible(false);
                        json.setVisible(false);
                        json.setVisible(true);
                    }
                }
            }

            private class FormData extends JPanel {
                public FormData() {
                    this.setLayout(null);
                    this.setSize((width - 200) / 2 - 20, height - 215);
                    this.setLocation(5, 50);
                    this.setVisible(false);
                    this.setVisible(false);
                    //Set Color
                    float[] colors = new float[3];
                    colors = Color.RGBtoHSB(40, 41, 37, colors);
                    this.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));
                }

            }

            private class BinaryFile extends JPanel {
                private JFileChooser binChooser = new JFileChooser("C:\\");
                File binaryFile;

                public BinaryFile() {
                    this.setLayout(null);
                    this.setSize((width - 200) / 2 - 20, height - 215);
                    this.setLocation(5, 50);
                    this.setVisible(false);
                    this.setVisible(false);
                    //Set Color
                    float[] colors = new float[3];
                    colors = Color.RGBtoHSB(40, 41, 37, colors);
                    this.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));

                    this.add(binChooser);
                    binChooser.setSize((width - 200) / 2 - 20, height - 215);
                    binChooser.setLocation(0, 0);
                    binaryFile = binChooser.getSelectedFile();
                }

                public void reSize() {
                    binChooser.setSize((width - 200) / 2 - 20, height - 215);
                    this.setSize((width - 200) / 2 - 20, height - 215);
                    this.setLocation(5, 50);

                }
            }

            private class Json extends JPanel {
                private JTextArea jsonBody = new JTextArea();

                public String getJsonBody() {
                    return jsonBody.getText();
                }

                public void setJsonBody(String jsonBody) {
                    this.jsonBody.setText(jsonBody);
                }

                public void reSize() {
                    this.setSize((width - 200) / 2 - 20, height - 215);
                    jsonBody.setSize((width - 200) / 2 - 30, height - 220);

                }

                public Json() {
                    this.setLayout(null);
                    this.setSize((width - 200) / 2 - 20, height - 215);
                    this.setLocation(5, 50);
                    this.setVisible(false);
                    //Set Color
                    float[] colors = new float[3];
                    colors = Color.RGBtoHSB(40, 41, 37, colors);
                    this.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));

                    jsonBody.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));
                    jsonBody.setForeground(Color.WHITE);
                    jsonBody.setSize((width - 200) / 2 - 30, height - 220);
                    jsonBody.setLocation(5, 5);
                    this.add(jsonBody);
                }
            }
        }


        private class TopCenter extends JPanel {
            private JTextField url = new JTextField();
            private JComboBox urlType = new JComboBox();
            private JButton send = new JButton("SEND");
            private JButton save = new JButton("SAVE");

            public void setUrlType(String urlType) {
                this.urlType.setSelectedItem(urlType);
            }

            public void setUrl(String url) {
                this.url.setText(url);
            }

            public String getUrlType() {
                return (String) urlType.getSelectedItem();
            }

            public String getUrl() {
                return url.getText();
            }

            public void reSize() {
                this.setSize((width - 200) / 2, 50);
                url.setSize((width - 200) / 2 - 200, 40);
                send.setLocation((width - 200) / 2 - 140, 5);
                save.setLocation((width - 200) / 2 - 75, 5);
            }

            public TopCenter() {
                this.setLayout(null);
                this.setBackground(Color.WHITE);
                this.setOpaque(true);
                this.setSize((width - 200) / 2, 50);
                this.setLocation(0, 0);

                urlType = new JComboBox();
                urlType.addItem("GET");
                urlType.addItem("POST");
                urlType.addItem("PUT");
                urlType.addItem("PATCH");
                urlType.addItem("DELETE");
                urlType.setSize(65, 40);
                urlType.setLocation(0, 5);
                this.add(urlType);

                url = new JTextField("...");
                url.setSize((width - 200) / 2 - 200, 40);
                url.setLocation(65, 5);
                this.add(url);

                send = new JButton("SEND");
                send.addActionListener(new sendHandler());
                send.setSize(65, 40);
                send.setLocation((width - 200) / 2 - 140, 5);
                this.add(send);

                save.setSize(65, 40);
                save.setLocation((width - 200) / 2 - 75, 5);
                save.addActionListener(new saveHandler());
                this.add(save);
                updateUI();
            }

            private class sendHandler implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    right.setVisible(true);
                    topRight.setVisible(false);
                    right.getShowHeader().printHeader();
                }
            }

            private class saveHandler implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (Request request : requests) {
                        if (request.getName().equals(onRequest)) {
                            request.setUrlType((String) urlType.getSelectedItem());
                            request.setUrl(url.getText());
                            request.setBodyType(body.getBodyType());
                            request.setJson(body.getJson());
                        }
                    }
                }
            }
        }
    }

    private class Right extends JPanel {

        private TopRight topRight = new TopRight();
        private JTabbedPane type = new JTabbedPane();
        private ShowHeader showHeader = new ShowHeader();
        private Preview preview = new Preview();

        public ShowHeader getShowHeader() {
            return showHeader;
        }

        public Right() {
            this.setVisible(false);
            this.setSize((width - 200) / 2, height - 75);
            this.setLocation(200 + (width - 200) / 2, 0);
            this.setLayout(null);

            //Set Color
            float[] colors = new float[3];
            colors = Color.RGBtoHSB(40, 41, 37, colors);
            this.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));

            this.add(topRight);

            this.add(type);
            type.addTab("Headers", showHeader);
            type.addTab("Preview", preview);
            type.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));
            type.setSize((width - 200) / 2 - 5, height - 80);
            type.setLocation(5, 55);

        }

        public void reSize() {
            this.setSize((width - 200) / 2, height - 75);
            this.setLocation(200 + (width - 200) / 2, 0);
            topRight.reSize();
            showHeader.reSize();
            type.setSize((width - 200) / 2 - 5, height - 80);
            type.setLocation(5, 55);

        }

        private class TopRight extends JPanel {
            private double responseTime;
            private String codeStatus;

            public TopRight() {
                this.setLayout(null);
                this.setBackground(Color.WHITE);
                this.setSize((width - 200) / 2, 50);
                this.setLocation(0, 0);

                codeStatus = "200OK";
                JLabel codeStatusLabel = new JLabel(codeStatus);
                codeStatusLabel.setSize(70, 40);
                codeStatusLabel.setLocation(15, 5);
                codeStatusLabel.setBackground(Color.green);
                codeStatusLabel.setHorizontalAlignment(JLabel.CENTER);
                codeStatusLabel.setOpaque(true);
                this.add(codeStatusLabel);

                responseTime = 2.69;
                JLabel responseTimeLabel = new JLabel(String.valueOf(responseTime) + 's');
                responseTimeLabel.setSize(70, 40);
                responseTimeLabel.setLocation(120, 5);
                responseTimeLabel.setBackground(Color.darkGray);
                responseTimeLabel.setForeground(Color.WHITE);
                responseTimeLabel.setHorizontalAlignment(JLabel.CENTER);
                responseTimeLabel.setOpaque(true);
                this.add(responseTimeLabel);
            }

            public void reSize() {
                this.setSize((width - 200) / 2, 50);
                this.setSize((width - 200) / 2, 50);
            }
        }

        private class ShowHeader extends JPanel {
            private HashMap<JLabel, JLabel> headers = new HashMap<>();

            public HashMap<JLabel, JLabel> getHeaders() {
                return headers;
            }

            public void reSize() {
                this.setSize((width - 200) / 2, height - 105);
                this.setLocation(5, 5);
                int k = 0;
                for (JLabel label : headers.keySet()) {
                    label.setSize((width - 200) / 5, 30);
                    label.setLocation(10, 20 + k * 40);
                    label.setSize((width - 200) / 5, 30);
                    label.setLocation((width - 200) / 5 + 20, 20 + k * 40);
                    k++;
                }
            }

            public ShowHeader() {

                this.setSize((width - 200) / 2, height - 105);
                this.setLocation(5, 5);
                this.setOpaque(true);
                this.setLayout(null);

                //Set Color
                float[] colors = new float[3];
                colors = Color.RGBtoHSB(40, 41, 37, colors);
                this.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));

            }

            public void printHeader() {
                for (JLabel label : headers.keySet()) {
                    label.setVisible(false);
                    headers.get(label).setVisible(false);
                }
                headers.clear();
                int k = 0;
                for (JCheckBoxMenuItem checkBoxMenuItem : center.getHeader().checkHeaders) {
                    if (checkBoxMenuItem.isSelected()) {
                        for (JLabel header : center.getHeader().headers.keySet()) {
                            if (header.getText().equals(checkBoxMenuItem.getName())) {
                                JLabel headLabel = new JLabel(header.getText());
                                headLabel.setOpaque(true);
                                headLabel.setBackground(Color.WHITE);
                                headLabel.setSize((width - 200) / 5, 30);
                                headLabel.setLocation(10, 20 + k * 40);

                                JLabel valueLabel = new JLabel(center.getHeader().headers.get(header).getText());
                                valueLabel.setSize((width - 200) / 5, 30);
                                valueLabel.setBackground(Color.WHITE);
                                valueLabel.setLocation((width - 200) / 5 + 20, 20 + k * 40);
                                valueLabel.setOpaque(true);
                                headers.put(headLabel, valueLabel);
                                headLabel.setVisible(true);
                                valueLabel.setVisible(true);
                                this.add(headLabel);
                                this.add(valueLabel);
                                k++;
                            }
                            updateUI();
                        }
                    }
                }
            }
        }

        private class Preview extends JPanel {
            private JTabbedPane type = new JTabbedPane();
            RawData rawData = new RawData();
            VisualPreview visualPreview = new VisualPreview();

            public Preview() {

                this.setSize((width - 200) / 2, height - 125);
                this.setLocation(5, 5);
                this.setOpaque(true);
                this.setLayout(null);

                //Set Color
                float[] colors = new float[3];
                colors = Color.RGBtoHSB(40, 41, 37, colors);
                this.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));


                this.add(type);
                type.setSize((width - 200) / 2, height - 105);
                type.setLocation(0, 0);
                type.addTab("Raw Data", rawData);
                type.addTab("VisualPreview", visualPreview);
            }

            private class RawData extends JPanel {
                public RawData() {
                    this.setSize((width - 200) / 2 - 10, height - 145);
                    this.setLocation(5, 5);
                    this.setOpaque(true);
                    this.setLayout(null);

                    //Set Color
                    float[] colors = new float[3];
                    colors = Color.RGBtoHSB(40, 41, 37, colors);
                    this.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));

                }
            }

            private class VisualPreview extends JPanel {
                public VisualPreview() {
                    this.setSize((width - 200) / 2 - 10, height - 145);
                    this.setLocation(5, 5);
                    this.setOpaque(true);
                    this.setLayout(null);

                    //Set Color
                    float[] colors = new float[3];
                    colors = Color.RGBtoHSB(40, 41, 37, colors);
                    this.setBackground(Color.getHSBColor(colors[0], colors[1], colors[2]));

                }
            }
        }
    }
}
