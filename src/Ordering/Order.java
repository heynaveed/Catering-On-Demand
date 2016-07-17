package Ordering;

import Accounts.Staff;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Order {
    
    private String iD;
    private double totalPrice = 0;
    private boolean isAdvanceOrder = false;
    private ArrayList<Item> itemList;
    private int totalPrepTime = 0;
    private boolean isFlagged = false;
    private boolean isPriority = false;
    private String status = "Incomplete";
    private String drinkMenu = "";
    private String menuType;
    private String date;
    private Staff staff;
    private String time;
    private String customerID;
    private final DecimalFormat dF;
            
    public Order(String iD, String customerID, String menuType){
        this.iD = iD;
        this.customerID = customerID;
        this.menuType = menuType;
        itemList = new ArrayList<>();
        dF = new DecimalFormat("###.00");
    }

    public String getID() {
        return iD;
    }

    public double getTotalPrice() {
        totalPrice = 0;
        for (Item itemList1 : itemList) {
            totalPrice += itemList1.getPrice();
        }
        
        if(isPriority){
            totalPrice *= 1.05;
        }

        return Double.parseDouble(dF.format(totalPrice));
    }

    public boolean getIsAdvanceOrder() {
        return isAdvanceOrder;
    }

    public int getTotalPrepTime() {
        totalPrepTime = 0;
        for (Item itemList1 : itemList) {
            totalPrepTime += itemList1.getPreparationTime();
        }
        return totalPrepTime;
    }
    
    public boolean getIsFlagged() {
        return isFlagged;
    }

    public String getMenuType() {
        return menuType;
    }

    public String getDate() {
        return date;
    }

    public Staff getStaff() {
        return staff;
    }

    public String getTime() {
        return time;
    }

    public String getCustomerID() {
        return customerID;
    }
    
    public ArrayList<Item> getItemList(){
        return itemList;
    }

    public boolean getIsPriority() {
        return isPriority;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setIsAdvanceOrder(boolean isAdvanceOrder) {
        this.isAdvanceOrder = isAdvanceOrder;
    }

    public void setTotalPrepTime(int totalPrepTime) {
        this.totalPrepTime = totalPrepTime;
    }

    public void setIsFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCustomer(String customerID) {
        this.customerID = customerID;
    }
    
    public void toggleIsAdvanceOrder(){
        isAdvanceOrder = !isAdvanceOrder;
    }
    
    public void toggleFlagStatus(){
        isFlagged = !isFlagged;
    }

    public void togglePriorityStatus(){
        isPriority = !isPriority;
    }

    public void setIsPriority(boolean isPriority) {
        this.isPriority = isPriority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDrinkMenu() {
        return drinkMenu;
    }

    public void setDrinkMenu(String drinkMenu) {
        this.drinkMenu = drinkMenu;
    }
}
