package GUI;

import Ordering.Item;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class BasketItemPanel extends JPanel implements ActionListener{
    
    private static final int width = 350;
    private static final int height = 30;
    private static final int xPos = 17;
    private static final int componentsYPos = 8;
    private static final int nameXPos = 56;
    private static final int priceXPos = 170;
    private static final String breakfastPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\breakfastIcon.png";
    private static final String lunchPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\lunchIcon.png";
    private static final String dinnerPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\dinnerIcon.png";
    private static final String drinksPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\drinkIcon.png";
    private static ImageIcon imageIcon;
    private Image img;
    private final JLabel iconLabel;
    private final int yPos;
    private final JLabel name;
    private final JLabel price;
    private final JButton removeButton;
    private final MainDashboard dash;
    private final Item item;
    private final DecimalFormat dF;
    
    public BasketItemPanel(Item item, int yPos, MainDashboard dash){
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setLocation(xPos, yPos);
        setLayout(null);
        setOpaque(false);
        this.name = new JLabel();
        this.price = new JLabel();
        this.dash = dash;
        this.item = item;
        this.yPos = yPos;
        dF = new DecimalFormat("##0.00");
        
        iconLabel = new JLabel();
        if(item.getMenuType().equals("Breakfast")){
            imageIcon = new ImageIcon(breakfastPath);
            iconLabel.setToolTipText("Breakfast");
        }
        if(item.getMenuType().equals("Lunch")){
            imageIcon = new ImageIcon(lunchPath);
            iconLabel.setToolTipText("Lunch");
        }
        if(item.getMenuType().equals("Dinner")){
            imageIcon = new ImageIcon(dinnerPath);
            iconLabel.setToolTipText("Dinner");
        }
        if(item.getMenuType().equals("Drinks")){
            imageIcon = new ImageIcon(drinksPath);
            iconLabel.setToolTipText("Drinks");
        }
        
        img = imageIcon.getImage();
        Image newImg = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        iconLabel.setIcon(new ImageIcon(newImg));
        
        removeButton = new JButton();
        this.setToolTipText(item.getDescription());
        this.name.setText(item.getName());
        this.price.setText(dF.format(item.getPrice()));
        this.name.setBounds(nameXPos, componentsYPos, 150, 15);
        this.price.setBounds(priceXPos + 50, componentsYPos, 75, 15);
        removeButton.setBounds(265, 5, 80, 20);
        iconLabel.setBounds(11, 2, 25, 25);
        removeButton.setText("Remove");
        removeButton.addActionListener(this);
        add(this.name);
        add(this.price);
        add(removeButton);
        add(iconLabel);
    }    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.drawLine(215, 0, 215, 44);
        g.drawRect(xPos + 35, 0, width - 53, height - 2);
    }  

    /**
     * Removes item from basket.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i = 0; i < dash.getMenuPanel().size(); i++){
            if(dash.getMenuPanel().get(i).getItem().getName().equals(item.getName())){
                dash.getMenuPanel().get(i).getCombo().setSelectedIndex(
                dash.getMenuPanel().get(i).getCombo().getSelectedIndex() - 1);
                break;
            }
        }
        
        dash.displayBasket();
        dash.updateBasketPrice();
    }
}
