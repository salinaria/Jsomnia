import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class UI {
    private JFrame mainFrame=new JFrame("Jsomnia");
    private int width=1000;
    private int height=600;
    private ArrayList<Request> requests=new ArrayList<>();
    private JFrame newRequestGetter=new JFrame("New Request");
    public UI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        mainFrame.setLayout(null);
        mainFrame.setSize(width,height);
        mainFrame.getContentPane().setBackground(Color.darkGray);
        //Left
        JButton newRequest=new JButton("New Request");
        newRequest.setSize(150,50);
        newRequest.setBackground(Color.magenta);
        newRequest.setLocation(25,25);
        newRequest.addActionListener(new newRequestHandler());
        mainFrame.add(newRequest);

        //Center

        //URL
        JTextField url=new JTextField();
        url.setSize(265,40);
        url.setLocation(265,5);
        mainFrame.add(url);

        JButton send=new JButton("SEND");
        send.setSize(75,40);
        send.setLocation(525,5);
        mainFrame.add(send);

        JComboBox urlType=new JComboBox();
        urlType.addItem("GET");
        urlType.addItem("POST");
        urlType.addItem("PUT");
        urlType.addItem("WATCH");
        urlType.setSize(65,40);
        urlType.setLocation(200,5);
        mainFrame.add(urlType);


        //Body panel
        JPanel body=new JPanel();
        body.setLayout(null);

        JComboBox bodyType=new JComboBox();
        bodyType.addItem("JSON");
        bodyType.addItem("XML");
        bodyType.addItem("YAML");
        bodyType.addItem("EDN");
        bodyType.setSize(65,40);
        bodyType.setLocation(5,5);
        body.add(bodyType);

        JTextArea bodyFiled=new JTextArea("...");
        bodyFiled.setLocation(0,50);
        bodyFiled.setSize(400,400);
        body.add(bodyFiled);
        mainFrame.add(body);

        //Center tab
        JTabbedPane center=new JTabbedPane();
        center.addTab("Body",body);
        center.addTab("Header",new JPanel());
        center.setSize(400,500);
        center.setLocation(200,50);
        mainFrame.add(center);



        mainFrame.setVisible(true);

    }
    private class newRequestHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            newRequestGetter.setLayout(null);
            newRequestGetter.setSize(400,150);

            JLabel namePrint=new JLabel("Name");
            namePrint.setVisible(true);
            namePrint.setLocation(5,5);
            namePrint.setSize(40,40);
            newRequestGetter.add(namePrint);


            JTextField name=new JTextField();
            name.setSize(250,40);
            name.setLocation(50,5);
            newRequestGetter.add(name);

            JButton create=new JButton("CREATE");
            create.addActionListener(new createReqHandler());
            create.setSize(100,40);
            create.setLocation(125,50);
            newRequestGetter.add(create);

            JComboBox reqType=new JComboBox();
            reqType.addItem("GET");
            reqType.addItem("POST");
            reqType.addItem("PUT");
            reqType.addItem("WATCH");
            reqType.setSize(65,40);
            reqType.setLocation(300,5);
            newRequestGetter.add(reqType);

            newRequestGetter.setVisible(true);
        }
    }
    private class createReqHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            newRequestGetter.setVisible(false);
        }
    }
}
