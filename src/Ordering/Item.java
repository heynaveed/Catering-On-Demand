package Ordering;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Item {
    
    private String ID;
    private String name;
    private double price;
    private String description;
    private boolean availability; 
    private int preparationTime;
    private String menuType;
    private String courseType;
    private boolean isSpecial;
    private ArrayList<Stock> stockList;
    
    public Item(String ID, String name, double price, String description, 
            boolean availability, int preparationTime, String menuType,
            String courseType, boolean isSpecial){
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.description = description;
        this.availability = availability;
        this.preparationTime = preparationTime;
        this.menuType = menuType;
        this.courseType = courseType;
        this.isSpecial = isSpecial;
    }

    public String getID(){
        return ID;
    }
    
    public String getName() {
        return name;
    }

    public double getPrice() {
        if(isSpecial){
            return price/2;
        }
        else{
            return price;
        }
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailability() {
        return availability;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public String getMenuType() {
        return menuType;
    }

    public String getCourseType() {
        return courseType;
    }

    public boolean getIsSpecial() {
        return isSpecial;
    }
    
    public void setID(String ID){
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public void setIsSpecial(boolean isSpecial) {
        this.isSpecial = isSpecial;
    }
}
