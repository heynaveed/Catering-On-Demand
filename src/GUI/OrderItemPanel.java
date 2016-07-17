package GUI;

import Ordering.Order;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class OrderItemPanel extends JPanel implements ActionListener{
    
    private static final int width = 300;
    private static final int height = 155;
    private static final int xPos = 387;
    private static final String breakfastPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\breakfastIcon.png";
    private static final String lunchPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\lunchIcon.png";
    private static final String dinnerPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\dinnerIcon.png";
    private static final String drinksPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\drinkIcon.png";
    private static ImageIcon imageIcon;
    private final Image menuImg;
    private final JLabel orderPrice;
    private final JLabel priorityStatus;
    private final JLabel prepTime;
    private final JLabel menuType;
    private final JLabel menuTypeIconLabel;
    private final MainDashboard dash;
    private final DecimalFormat dF;
    private final Order order;
    private final JButton addPriority;
    private final MenuCheckGroup mcg;
    
    public OrderItemPanel(Order order, int yPos, MainDashboard dash){
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setLocation(xPos, yPos);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(null);
        setOpaque(false);
        setToolTipText(order.getMenuType());
        menuTypeIconLabel = new JLabel();
        orderPrice = new JLabel();
        priorityStatus = new JLabel();
        menuType = new JLabel();
        prepTime = new JLabel();
        addPriority = new JButton();
        mcg = new MenuCheckGroup(190, 55, dash);
        this.order = order;
        this.dash = dash;
        
        if(order.getMenuType().equals("Breakfast")){
            imageIcon = new ImageIcon(breakfastPath);
            menuTypeIconLabel.setToolTipText("Breakfast");
        }
        if(order.getMenuType().equals("Lunch")){
            imageIcon = new ImageIcon(lunchPath);
            menuTypeIconLabel.setToolTipText("Lunch");
        }
        if(order.getMenuType().equals("Dinner")){
            imageIcon = new ImageIcon(dinnerPath);
            menuTypeIconLabel.setToolTipText("Dinner");
        }
        if(order.getMenuType().equals("Drinks")){
            imageIcon = new ImageIcon(drinksPath);
            menuTypeIconLabel.setToolTipText("Drinks");
        }
        
        menuImg = imageIcon.getImage();
        Image newImg = menuImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        menuTypeIconLabel.setIcon(new ImageIcon(newImg));
        
        dF = new DecimalFormat("##0.00");
        menuTypeIconLabel.setBounds(5, 5, 50, 50);
        orderPrice.setBounds(10, 70, 175, 10);
        priorityStatus.setBounds(10, 85, 175, 13);
        prepTime.setBounds(10, 100, 175, 13);
        menuType.setBounds(65, 5, 300, 50);
        addPriority.setBounds(10, 120, 280, 25);
        
        if(!order.getIsPriority()){
            addPriority.setText("Add Priority Fee (+5%)");
        }
        else{
            addPriority.setText("Remove Priority Fee (-5%)");
        }
        
        menuType.setFont(new Font("Tahoma", Font.PLAIN, 40));
        orderPrice.setText("Total Price: £" + dF.format(order.getTotalPrice()));
        priorityStatus.setText("Priority Status: " + Boolean.toString(order.getIsPriority()));
        prepTime.setText("Preparation Time (mins): " + order.getTotalPrepTime());
        menuType.setText(order.getMenuType());
        addPriority.addActionListener(this);
        add(menuTypeIconLabel);
        add(orderPrice);
        add(priorityStatus);
        add(prepTime);
        add(menuType);
        
        if(order.getMenuType().equals("Drinks")){
            add(mcg);
        }
        if(!order.getMenuType().equals("Drinks")){
            add(addPriority);
        }
    }    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }  

    /**
     * Changes the priority status of the selected order.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(order.getMenuType().equals("Breakfast")){
            dash.getBreakfastOrder().togglePriorityStatus();
        }
        if(order.getMenuType().equals("Lunch")){
            dash.getLunchOrder().togglePriorityStatus();
        }
        if(order.getMenuType().equals("Dinner")){
            dash.getDinnerOrder().togglePriorityStatus();
        }
        
        dash.displayBasket();
    }
}
