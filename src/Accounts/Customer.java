package Accounts;

import java.sql.SQLException;

public class Customer extends User{
    
    private String vipStatus;
    private String phoneNumber;
    private String email;
    private double totalSpent;
    private String discountType;
    private String customerID;
    
    public Customer(String username, String password, String firstName, 
            String lastName, String userType, String userID, String customerID, String vipStatus, 
            double totalSpent, String email, String phoneNumber, String discountType) throws SQLException{
        super(username, password, firstName, lastName, userType, userID);
        this.email = email;
        this.vipStatus = vipStatus;
        this.totalSpent = totalSpent;
        this.phoneNumber = phoneNumber;
        this.discountType = discountType;
        this.customerID = customerID;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    } 
    
    public String getVIPStatus(){
        return vipStatus;
    }
    
    public double getTotalSpent() {
        return totalSpent;
    }
    
    public String getPercentageDiscount(){
        return discountType;
    } 
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void setVIPStatus(String vipStatus){
        this.vipStatus = vipStatus;
    }
    
    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }
    
    public void setPercentageDiscount(String percentageDiscount){
        this.discountType = percentageDiscount;
    }
    
    public String getCustomerID(){
        return customerID;
    }
    
    public void viewOrderHistory(){
        // Code to access customer order database here.
    }
}
