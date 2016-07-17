package GUI;

import Database.DBConnect;
import Ordering.Order;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class QueueItemPanel extends JPanel implements ActionListener{
    
    private static final int width = 623;
    private static final int height = 40;
    private static final int xPos = 0;
    private static final String flagPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\flagIcon.png";
    private static final String priorityPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\priorityIcon.png";
    private static final String latePath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\exclamationIcon.png";
    private static ImageIcon flagImageIcon;
    private static ImageIcon priorityImageIcon;
    private static ImageIcon lateImageIcon;
    private Image flagImg;
    private Image priorityImg;
    private Image lateImg;
    private final JLabel flagIconLabel;
    private final JLabel priorityIconLabel;
    private final JLabel timeOfOrder;
    private final JLabel timeLabel;
    private final JLabel lateIconLabel;
    private final JButton viewOrder;
    private final Order order;
    private final MainDashboard dash;
    private final DecimalFormat dF;
    private final DBConnect con;
    private final String[][] customer;
    private final double totalPrice;
    
    
    public QueueItemPanel(Order order, int yPos, MainDashboard dash, double totalPrice) throws IOException, SQLException{
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setLocation(xPos, yPos);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(null);
        setOpaque(false);
        dF = new DecimalFormat("##0.00");
        flagIconLabel = new JLabel();
        priorityIconLabel = new JLabel();
        timeOfOrder = new JLabel();
        timeLabel = new JLabel();
        lateIconLabel = new JLabel();
        viewOrder = new JButton();
        this.order = order;
        this.dash = dash;
        this.totalPrice = totalPrice;
        con = new DBConnect();
        customer = con.selectCustomer("CustomerID", order.getCustomerID(), "", false);
        
        if(order.getIsFlagged()){
            flagImageIcon = new ImageIcon(flagPath);
            flagIconLabel.setToolTipText("Order is flagged!");
            flagImg = flagImageIcon.getImage();
            Image newImg1 = flagImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            flagIconLabel.setIcon(new ImageIcon(newImg1));
        }
        
        if(order.getIsPriority()){
            priorityImageIcon = new ImageIcon(priorityPath);
            priorityIconLabel.setToolTipText("Order is priority!");
            priorityImg = priorityImageIcon.getImage();
            Image newImg2 = priorityImg.getScaledInstance(65, 33, Image.SCALE_SMOOTH);
            priorityIconLabel.setIcon(new ImageIcon(newImg2));
        }
        
        if(order.getStatus().equals("Late")){
            lateImageIcon = new ImageIcon(latePath);
            lateIconLabel.setToolTipText("Order is late!");
            lateImg = lateImageIcon.getImage();
            Image newImg3 = lateImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            lateIconLabel.setIcon(new ImageIcon(newImg3));
        }
        
        viewOrder.setText("View Order");
        timeLabel.setText("Preparation Time: " + order.getTotalPrepTime() + " minutes");
        timeOfOrder.setText("Placed at: " + order.getTime());
        viewOrder.setBounds(505, 9, 100, 23);
        priorityIconLabel.setBounds(425, 5, 65, 33);
        timeLabel.setBounds(10, 5, 200, 35);
        lateIconLabel.setBounds(330, 5, 35, 35);
        timeOfOrder.setBounds(200, 5, 150, 35);
        flagIconLabel.setBounds(380, 5, 35, 35);
        viewOrder.addActionListener(this);
        
        add(viewOrder);
        add(priorityIconLabel);
        add(flagIconLabel);
        add(timeOfOrder);
        add(timeLabel);
        add(lateIconLabel);
    }    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.drawLine(190, 0, 190, 40);
    }  
    
    /**
     * Changes the state of the order in the queue.
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        
        try {
            String[][] orderArray = con.selectOrder("OrderID", order.getID(), "", false);
            
            if(orderArray[0][7].equals("Incomplete")){
                dash.getJButtonFlagOrder().setEnabled(true);
                dash.getJLabelStatusOrder().setText("Status: " + order.getStatus());
            }
            if(orderArray[0][7].equals("Flagged")){
                dash.getJButtonFlagOrder().setEnabled(false);
                dash.getJLabelStatusOrder().setText("Status: Flagged");
            }
            if(orderArray[0][12].equals("Late")){
                dash.getJLabelStatusOrder().setText("Status: " + order.getStatus());
            }
            
            if(dash.getJButtonFlagOrder().isEnabled()){
                dash.getJButtonSendNotification().setEnabled(false);
            }
            if(!dash.getJButtonFlagOrder().isEnabled()){
                dash.getJButtonSendNotification().setEnabled(true);
            }
            
            dash.switchMainDisplayTo(dash.getJScrollPaneViewOrder());
            dash.getJLabelOrderNoOrder().setText("Order Type: " + order.getMenuType());
            dash.getJLabelTotalPriceOrder().setText("Total Price: £" + dF.format(totalPrice));
            dash.getJLabelCustomerIDOrder().setText("Customer Name: " + customer[0][2] + " " + customer[0][3]);
            dash.getJLabelPreparationTimeOrder().setText("Preparation Time: " + order.getTotalPrepTime() + " minutes");
            dash.getJLabelNoOfItems().setText("No. of Items: " + order.getItemList().size());
            dash.setOrderToFlag(order);
            dash.getJTextAreaItemsInOrder().setText("");
            
            for(int i = 0; i < order.getItemList().size(); i++){
                dash.getJTextAreaItemsInOrder().append(order.getItemList().get(i).getName() + "\n");
            }
        } catch (ParseException ex) {
            Logger.getLogger(QueueItemPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(QueueItemPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
