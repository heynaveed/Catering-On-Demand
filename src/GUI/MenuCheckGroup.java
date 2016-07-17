package GUI;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JPanel;

public class MenuCheckGroup extends JPanel implements ItemListener{
    
    private static final int width = 230;
    private static final int height = 85;
    private final CheckboxGroup cbg;
    private final Checkbox breakfast;
    private final Checkbox lunch;
    private final Checkbox dinner;
    private final MainDashboard dash;
    
    MenuCheckGroup(int xPos, int yPos, MainDashboard dash){
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setLocation(xPos, yPos);
        setLayout(null);
        setOpaque(false);
        setToolTipText("Select an order to add the drinks order to.");
        this.dash = dash;
        cbg = new CheckboxGroup();
        breakfast = new Checkbox("Breakfast", cbg, false);
        lunch = new Checkbox("Lunch", cbg, false);
        dinner = new Checkbox("Dinner", cbg, false);
        breakfast.setBounds(5, 5, 75, 25);
        lunch.setBounds(5, 30, 75, 25);
        dinner.setBounds(5, 55, 75, 25);
        breakfast.addItemListener(this);
        lunch.addItemListener(this);
        dinner.addItemListener(this);
        add(breakfast);
        add(lunch);
        add(dinner);
    }

    /**
     * Changes the state of the menu check boxes.
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(breakfast.getState()){
            dash.getDrinksOrder().setDrinkMenu("Breakfast");
        }
        if(lunch.getState()){
            dash.getDrinksOrder().setDrinkMenu("Lunch");
        }
        if(dinner.getState()){
            dash.getDrinksOrder().setDrinkMenu("Dinner");
        }
    }
}
