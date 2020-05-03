import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class UI {
    private JFrame mainFrame = new JFrame("Jsomnia");
    private int width = 1000;
    private int height = 600;
    private ArrayList<Request> requests = new ArrayList<>();
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
        mainFrame.setVisible(true);
    }

    public void showGui() {
        //Left

        left.setLayout(null);
        left.setSize(200, height - 100);
        left.setLocation(0, 50);
        mainFrame.add(left);

        //New Request Button

        JButton newRequest = new JButton("New Request");
        newRequest.setSize(150, 50);
        newRequest.setBackground(Color.magenta);
        newRequest.setLocation(25, 5);
        newRequest.addActionListener(new newRequestHandler());
        left.add(newRequest);

        //Requests list

        int j = 0;
        ImageIcon del = new ImageIcon("src/delete.png");
        for (Request i : requests) {
            JButton delete = new JButton(del);
            delete.setSize(30, 30);
            delete.setLocation(160, 130 + j * 35);
            left.add(delete);
            JButton req = new JButton(i.getName());
            req.setSize(150, 30);
            req.setLocation(5, 80 * j * 35);
            left.add(req);
            j++;
        }

        //Center

        center.setLayout(null);
        center.setSize((width - 200) / 2, height - 100);
        center.setLocation(200, 50);
        mainFrame.add(center);

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
        body.add(bodyType);

        bodyFiled = new JTextArea("...");
        bodyFiled.setLocation(5, 50);
        bodyFiled.setSize((width-200)/2-10,height-250);
        body.add(bodyFiled);

        //Center tab

        centerTab = new JTabbedPane();
        centerTab.addTab("Body", body);
        centerTab.addTab("Header", header);
        centerTab.setSize((width - 200) / 2, height -150);
        centerTab.setLocation(0, 50);
        center.add(centerTab);
    }

    public void reSize() {
        left.setSize(200,height-100);

        center.setSize((width - 200) / 2, height - 100);
        url.setSize((width - 200) / 2 - 150, 40);
        send.setLocation((width - 200) / 2 - 80, 5);
        centerTab.setSize((width - 200) / 2, height - 50);
        bodyFiled.setSize((width-200)/2-10,height-250);

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
                for (Request i : requests) {
                    JButton delete = new JButton(del);
                    delete.setSize(30, 30);
                    delete.setLocation(160, 80 + j * 35);
                    mainFrame.add(delete);
                    JButton req = new JButton(i.getName());
                    req.setSize(150, 30);
                    req.setLocation(5, 80 + j * 35);
                    mainFrame.add(req);
                    j++;
                }
                newRequestGetter.setVisible(false);
            }
        }
    }

    private class newRequestHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            NewRequest newRequest = new NewRequest();
        }
    }


}
