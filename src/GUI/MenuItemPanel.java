package GUI;

import Ordering.Item;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MenuItemPanel extends JPanel implements ActionListener{
    
    private static final int width = 587;
    private static final int height = 44;
    private static final int xPos = 0;
    private static final int componentsYPos = 8;
    private static final int nameXPos = 15;
    private static final int priceXPos = 175;
    private static final String breakfastPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\breakfastIcon.png";
    private static final String lunchPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\lunchIcon.png";
    private static final String dinnerPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\dinnerIcon.png";
    private static final String drinksPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\drinkIcon.png";
    private static final String specialPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\specialIcon.gif";
    private static ImageIcon menuIcon;
    private static ImageIcon specialIcon;
    private Image menuImg;
    private Image specialImg;
    private final JLabel name;
    private final JLabel price;
    private final JLabel quantity;
    private JLabel menuIconLabel;
    private JLabel specialIconLabel;
    private final Item item;
    private final JComboBox combo;
    private final MainDashboard dash;
    private final DecimalFormat dF;
    private int previousIndex = 0;
    
    
    public MenuItemPanel(Item item, int yPos, MainDashboard dash) throws IOException{
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setLocation(xPos, yPos);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(null);
        setOpaque(false);
        menuIconLabel = new JLabel();
        specialIconLabel = new JLabel();
        this.name = new JLabel();
        this.price = new JLabel();
        this.dash = dash;
        this.item = item;
        dF = new DecimalFormat("##0.00");
        
        if(item.getMenuType().equals("Breakfast")){
            menuIcon = new ImageIcon(breakfastPath);
            menuIconLabel.setToolTipText("Breakfast");
        }
        if(item.getMenuType().equals("Lunch")){
            menuIcon = new ImageIcon(lunchPath);
            menuIconLabel.setToolTipText("Lunch");
        }
        if(item.getMenuType().equals("Dinner")){
            menuIcon = new ImageIcon(dinnerPath);
            menuIconLabel.setToolTipText("Dinner");
        }
        if(item.getMenuType().equals("Drinks")){
            menuIcon = new ImageIcon(drinksPath);
            menuIconLabel.setToolTipText("Drinks");
        }
       
        menuImg = menuIcon.getImage();
        Image newImg1 = menuImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        menuIconLabel.setIcon(new ImageIcon(newImg1));
        
        specialIcon = new ImageIcon(specialPath);
        specialIconLabel.setToolTipText("Daily Special! (Half Price)");
        specialImg = specialIcon.getImage();
        Image newImg2 = specialImg.getScaledInstance(60, 25, Image.SCALE_SMOOTH);
        specialIconLabel.setIcon(new ImageIcon(newImg2));
        
        this.setToolTipText(item.getDescription());
                quantity = new JLabel();
        combo = new JComboBox();
        this.name.setText(item.getName());
        this.price.setText("£" + dF.format(item.getPrice()));
        this.quantity.setText("Quantity: ");
        this.name.setBounds(nameXPos, componentsYPos, priceXPos - nameXPos, 30);
        this.price.setBounds(priceXPos, componentsYPos, 75, 30);
        this.quantity.setBounds(452, componentsYPos, 60, 30);
        combo.setBounds(512, componentsYPos, 60, 30);
        menuIconLabel.setBounds(225, componentsYPos-10, 50, 50);
        specialIconLabel.setBounds(325, componentsYPos+3, 60, 25);
        add(this.name);
        add(this.price);
        add(quantity);
        add(combo);
        add(menuIconLabel);
        
        if(item.getIsSpecial()){
            add(specialIconLabel);
        }
        
        populateComboList();
        combo.addActionListener(this);
    }    
    
    /**
     * Populates the combo list.
     */
    private void populateComboList(){
        for(int i = 0; i < 11; i++){
            combo.addItem(new ComboItem(Integer.toString(i), Integer.toString(i)));
        }
    }
    
    public Item getItem(){
        return item;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.drawLine(165, 0, 165, 44);
    }  

    public JComboBox getCombo() {
        return combo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateCombo();
    }
    
    /**
     * Updates the value in the combo box depending on the selection by user.
     */
    public void updateCombo(){
        if(combo.getSelectedIndex() > previousIndex){
            for(int i = 0; i < (combo.getSelectedIndex() - previousIndex); i++){
                dash.getOrder().getItemList().add(item);
                
                if(item.getMenuType().equals("Breakfast")){
                    dash.getBreakfastOrder().getItemList().add(item);
                }
                if(item.getMenuType().equals("Lunch")){
                    dash.getLunchOrder().getItemList().add(item);
                }
                if(item.getMenuType().equals("Dinner")){
                    dash.getDinnerOrder().getItemList().add(item);
                }
                if(item.getMenuType().equals("Drinks")){
                    dash.getDrinksOrder().getItemList().add(item);
                }
            }
        }
        if(combo.getSelectedIndex() < previousIndex){
            for(int i = 0; i < (previousIndex - combo.getSelectedIndex()); i++){
                dash.getOrder().getItemList().remove(item);
                
                if(item.getMenuType().equals("Breakfast")){
                    dash.getBreakfastOrder().getItemList().remove(item);
                }
                if(item.getMenuType().equals("Lunch")){
                    dash.getLunchOrder().getItemList().remove(item);
                }
                if(item.getMenuType().equals("Dinner")){
                    dash.getDinnerOrder().getItemList().remove(item);
                }
                if(item.getMenuType().equals("Drinks")){
                    dash.getDrinksOrder().getItemList().remove(item);
                }
            }
        }
        
        previousIndex = combo.getSelectedIndex();
        dash.updateBasketPrice();
    }

    public int getPreviousIndex() {
        return previousIndex;
    }

    public void setPreviousIndex(int previousIndex) {
        this.previousIndex = previousIndex;
    }
}
