package Graph3;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.camera.Camera;
import org.graphstream.ui.view.util.InteractiveElement;


import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.*;



public class Graph3 extends JFrame implements ActionListener {
    private JTextArea txtInput, txtChoose;
    private JTextField tHead, tTail;
    private JLabel lChoose;
    JComboBox<String> comboBoxHead, comboBoxTail, comboBoxChoose;
    int count;
    private int countBack;
    private JButton find,reset,back,savePath,bNext;
    public List<String> edges = new ArrayList<String>();


    public static void main(String args[]) {
        EventQueue.invokeLater(new Graph3()::display);
    }

    public void readText(DFS g, Graph graph) {
        JFileChooser chooser = new JFileChooser();
        String currentDirectory = System.getProperty("user.dir");
        chooser.setCurrentDirectory(new File(currentDirectory));
        chooser.showSaveDialog(null);
        String path = chooser.getSelectedFile().getAbsolutePath();


        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String edge = myReader.nextLine();
                edges.add(edge);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        Set<String> nodes = new HashSet<String>();
        for(String edge : edges) {
            String [] e = edge.split(" ");
            if (!nodes.contains(e[0])) {
                graph.addNode(e[0]);
                nodes.add(e[0]);
            }
            if (!nodes.contains(e[1])) {
                graph.addNode(e[1]);
                nodes.add(e[1]);
            }
            Integer i1 = Integer.parseInt(e[0]);
            Integer i2 = Integer.parseInt(e[1]);
            g.addEd(i1, i2);
            String f = e[0] + e[1];
            graph.addEdge(f, e[0], e[1], true);
        }
    }

    private void display() {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph = new SingleGraph("Graphxyz", false, true);
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");


        graph.setStrict(false);
        graph.setAutoCreate(true);
        graph.setAttribute("ui.stylesheet",
                "graph {\n" +
                        "\tfill-color: white;\n" +
                        "}\n" +
                        "node {\n" +
                        "\tsize: 25px;\n" +
                        "\tshape: circle;\n" +
                        "\tfill-color: white;\n" +
                        "\tstroke-mode: plain;\n" +
                        "\tstroke-color: black;\n" +
                        "}\n" +
                        "node.marked {\n" +
                        "\tfill-color: yellow;\n" +
                        "}\n"+
                        "node.marked2 {\n" +
                        "\tfill-color: white;\n" +
                        "}\n"+
                        "edge {\n" +
                        "\tfill-color: black;\n" +
                        "\tshape: line;\n" +
                        "}\n" +
                        "edge.marked {\n" +
                        "\tfill-color: red;\n" +
                        "}\n" +
                        "edge.marked2 {\n" +
                        "\tfill-color: black;\n" +
                        "}\n"
        );
        DFS g = new DFS(100);

        readText(g, graph);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("TEAM4_PROJECT");

        BorderLayout layoutManager = new BorderLayout();
        layoutManager.setHgap(0);
        layoutManager.setVgap(0);

        JPanel panel = new JPanel(layoutManager) {
            public Dimension getPreferredSize() {
                return new Dimension(1600, 800);
            }
        };
        panel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        ArrayList<String> nodeNames = new ArrayList<String>();
        for (int i = 0; i < graph.getNodeCount(); i++) {
            nodeNames.add(graph.getNode(i).getId());
        }

        String[] nodeNamesArr = nodeNames.toArray(new String[nodeNames.size()]);

        JLabel lHead = new JLabel("Head");
        tHead = new JTextField(10);
        comboBoxHead = new JComboBox<String>(nodeNamesArr);

        JLabel lTail = new JLabel("Tail");
        tTail = new JTextField(10);
        comboBoxTail = new JComboBox<String>(nodeNamesArr);

        lChoose = new JLabel("Choose");
        comboBoxChoose = new JComboBox<String>(new String[] {"100"});

        find = new JButton("Find");
        savePath = new JButton("Save Path");
        back = new JButton("Back");
        reset = new JButton("Reset Graph");
        bNext = new JButton("Next");
        // cbHandler = new CalculateButtonHandler();
        // find.addActionListener(cbHandler);
        // Add the buttons to panel.

        for (Node node : graph) {
            node.setAttribute("ui.label", node.getId());
        }


        SwingViewer view = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view.enableAutoLayout();

        ViewPanel viewPanel = (ViewPanel) view.addDefaultView(false);
        viewPanel.removeMouseListener(viewPanel.getMouseListeners()[0]);
        viewPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Camera camera = view.getDefaultView().getCamera();
        camera.setAutoFitView(true);

        viewPanel.addMouseMotionListener(new MouseMotionListener() {

            private int preX = -1;
            private int preY = -1;

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                int currentX = mouseEvent.getX();
                int currentY = mouseEvent.getY();

                Point3 pointView = camera.getViewCenter();

                if (preX != -1 && preY != -1) {
                    if (preX < currentX) {
                        pointView.x -= 0.01;
                    }
                    else if (preX > currentX) {
                        pointView.x += 0.01;
                    }

                    if (preY < currentY) {
                        pointView.y += 0.01;
                    }
                    else if (preY > currentY) {
                        pointView.y -= 0.01;
                    }
                }
                camera.setViewCenter(pointView.x, pointView.y, pointView.z);

                preX = currentX;
                preY = currentY;
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                GraphicElement node = ((View) viewPanel).findGraphicElementAt(EnumSet.of(InteractiveElement.NODE), mouseEvent.getX(), mouseEvent.getY());
                if (node != null) {
                    viewPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                else {
                    viewPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
        });

        viewPanel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                if (mouseWheelEvent.getWheelRotation() < 0) {
                    camera.setViewPercent(camera.getViewPercent() * 0.95);
                }
                else {
                    camera.setViewPercent(camera.getViewPercent() * 1.05);
                }
            }
        });

        viewPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                GraphicElement node = ((View) viewPanel).findGraphicElementAt(EnumSet.of(InteractiveElement.NODE), mouseEvent.getX(), mouseEvent.getY());
                if (node != null) {
                    System.out.println(node.getId());
                    graph.getNode(node.getId()).setAttribute("ui.class", "marked");
                }
            }
            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        panel.add(viewPanel);
        frame.add(lHead);
        // frame.add(tHead);
        frame.add(new JScrollPane(comboBoxHead));
        frame.add(lTail);
        // frame.add(tTail);
        frame.add(new JScrollPane(comboBoxTail));
        frame.add(lChoose);
        frame.add(new JScrollPane(comboBoxChoose));
        frame.add(back);
        frame.add(reset);
        frame.add(savePath);
        frame.add(find);
        frame.add(bNext);

        comboBoxHead.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                Object item = event.getItem();
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    comboBoxChoose.setModel(new DefaultComboBoxModel<String>(new String[] {item.toString()}));
                }
            }

        });


        txtInput = new JTextArea(8, 16);
        txtInput.setText("All path!\n");
        JScrollPane jsp = new JScrollPane(txtInput);
        //jsp.setViewportView(find);
        txtInput.setEditable(false);
        frame.add(jsp, BorderLayout.LINE_END);


        txtChoose = new JTextArea(8, 16);
        txtChoose.setEditable(false);
        frame.add(new JScrollPane(txtChoose));



        back.addActionListener(e-> {


            String[] List=new String[countBack];


            String headName = String.valueOf(comboBoxHead.getSelectedItem());
            String tailName = String.valueOf(comboBoxTail.getSelectedItem());
            g.printAllPaths(Integer.parseInt(headName), Integer.parseInt(tailName));

            for(int i=0;i<countBack;i++){
                List[i]=g.getValue().get(i)+"";
            };





            Node node = graph.getNode( List[countBack-1]);
            node.setAttribute("ui.class", "marked2");



            Edge edge = graph.getEdge( List[countBack-2] + List[countBack-1] );
            edge.setAttribute("ui.class", "marked2");







            for(int i=0;i<countBack;i++){
                System.out.println(List[i]);
            };
            System.out.println("***");
            countBack--;





            g.clearString();
            g.clp();
        });

        find.addActionListener(e -> {
            String headName = String.valueOf(comboBoxHead.getSelectedItem());
            String tailName = String.valueOf(comboBoxTail.getSelectedItem());
            g.printAllPaths(Integer.parseInt(headName), Integer.parseInt(tailName));
            txtInput.setText(g.getString() + "\nBest\n " + g.getValue());
            String text = txtInput.getText();
            txtInput.setCaretPosition(text != null ? text.length() : 0);



            String[] List = new String[g.getValue().size()];
            for(int i = 0; i < g.getValue().size(); i++) {
                List[i] =  g.getValue().get(i)+"";
            };
            countBack=g.getValue().size();
            //     String[] edgeUpdates = {"12", "23","34"};

            Timer timer = new Timer(2000, new ActionListener() {
                int cnt = 0;
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if(cnt == List.length) {
                        ((Timer) actionEvent.getSource()).stop();
                        return;
                    }
                    graph.getNode( List[cnt] ).setAttribute("ui.class", "marked");
                    graph.getEdge( List[cnt] + List[cnt + 1] ).setAttribute("ui.class", "marked");
                    cnt++;
                }
            });
            timer.start();
            g.clp();
        });



        reset.addActionListener(ex-> {


            //  String[] edgeUpdates = {"12", "23","34"};



            // for(int i=0;i<list.size();i++){
            //    graph.getNode(list.get(i)).setAttribute("ui.class", "marked2");
            // };

            for (int i = 0; i < graph.getNodeCount(); i++) {
                Node node = graph.getNode(i);
                node.setAttribute("ui.class", "marked2");
                // Do something with node
            }
            for (int i = 0; i < graph.getEdgeCount(); i++) {
                Edge edge = graph.getEdge(i);
                edge.setAttribute("ui.class", "marked2");
                // Do something with node
            }




        });


        savePath.addActionListener(k ->  {
            count ++;




            BufferedImage bi = new BufferedImage(viewPanel.getWidth(), viewPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics x = bi.createGraphics();
            viewPanel.print(x);
            x.dispose();
            try {
                File initialImage = new File("C://SaveImg/image" + String.format("%d", count)+ ".png");
                ImageIO.write(bi, "gif", new File("C://SaveImg/image"+ String.format("%d",count)+".png"));
                JOptionPane.showMessageDialog(null,"Images were written successfully .");

            } catch (IOException e) {
                System.out.println("Exception occured :" + e.getMessage());
            }
            ;



        });

        bNext.addActionListener(k ->  {

            String selected = String.valueOf(comboBoxChoose.getSelectedItem());
            String tailName = String.valueOf(comboBoxTail.getSelectedItem());
            if(Integer.parseInt(selected) != Integer.parseInt(tailName)) {
                ArrayList<String> temp = new ArrayList<String>();
                g.printPointPaths(Integer.parseInt(selected), Integer.parseInt(tailName));
                for (int p = 0; p < g.getfirstelement().size(); p++) {
                    // int s = g.getfirstelement().get(p);
                    temp.add(String.valueOf(g.getfirstelement().get(p)));
                }
                comboBoxChoose.setModel(new DefaultComboBoxModel<String>(temp.toArray(new String[temp.size()])));
                txtChoose.setText(String.join("\n", temp));
                String text1 = txtInput.getText();
                txtInput.setCaretPosition(text1 != null ? text1.length() : 0);
            }
            Node node = graph.getNode(selected);
            node.setAttribute("ui.class", "marked");

        });


        // btnBottom.addActionListener(this);
        //  find.addActionListener(this);
        //  this.validate();

        frame.add(panel);
        frame.pack();
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(1600, 800);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    // @Override

}