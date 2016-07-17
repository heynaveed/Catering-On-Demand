package Controllers;

import Accounts.Customer;
import Accounts.Staff;
import Accounts.User;
import Database.DBConnect;
import GUI.LoginRegister;
import Ordering.Order;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountController {
    
    private final Random random;
    private final DBConnect con;
    private Customer customer;
    private Staff staff;
    
    public AccountController() throws SQLException{
        random = new Random();
        con = new DBConnect();
    }
    
    public void login(String username, String password, MainController system, LoginRegister lg){
        try {
            String[][] a;
            a = con.selectUser("Username", username);
            if(a[0][2].equals(password)){ 
                switch (a[0][3]) {
                    case "Staff":
                        staff = createStaffObject(a[0][0]);
                        system.dashboard(staff);
                        con.changeUser("Is_Logged_In", staff.getUsername(), "1");
                        break;
                    case "Customer":           
                        customer = createCustomerObject(a[0][0]);
                        system.dashboard(customer); 
                        con.changeUser("Is_Logged_In", customer.getUsername(), "1");
                        break;
                }
            }
            else {
                lg.getJLabelLoginError().setText("Incorrect username or password!");
            }
        } catch (Exception ex) {
            lg.getJLabelLoginError().setText("Incorrect username or password!");
        }   
    }
    
    public void registerStaff(String name, String surname, String type, String username, String password){
        try {
            con.insertStaffRecord(name, surname, username, password, type);
        } catch (SQLException ex) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void registerCustomer(String firstName, String surname, String email, String username, String password, String phoneNumber) throws SQLException{
        con.insertCustomerRecord(firstName, surname, email, username, password, phoneNumber);
    }
    
    public String generateID(){
        int intID = 100000 + random.nextInt(899999);
        
        /* Code to check whether generated int already exists in database, if
        so then generate another one until unique ID is found.
        */
    
        return Integer.toString(intID);
    }
    
    public void promoteToManager(User u){
        // Code here to manipulate database.
    }
    /*
    public User searchUser(String usernameOrFirstName){
        // Code here to manipulate database.
    }
    */
    public void changeUsername(){
        // Code here to manipulate database.
    }
    
    public void changePassword(){
        // Code here to manipulate database.
    }
    
    public void changeEmail(){
        // Code here to manipulate database.
    }
    
    public void changePhoneNumber(){
        // Code here to manipulate database.
    }
    
    public Staff createStaffObject(String UserID) throws SQLException
    {
        String[][] a = con.selectUser("UserID", UserID);
        String[][] b = con.selectStaff("UserID", UserID, "", false);
        
        Staff staff = new Staff(a[0][1], a[0][2], b[0][2], b[0][3], a[0][3], b[0][1], b[0][4], b[0][0]);
        return staff;
    }
    
    public Customer createCustomerObject(String UserID) throws SQLException{

        String[][] a = con.selectUser("UserID", UserID);
        String[][] b = con.selectCustomer("UserID", UserID, "", false);

        double amountSpent = Double.parseDouble(b[0][6]);
        customer = new Customer(a[0][1], a[0][2], b[0][2], b[0][3], a[0][3], b[0][1], b[0][0], b[0][7], amountSpent, b[0][4], b[0][5], b[0][8]);
        return customer;
    }
    
    public Staff getStaff(){
        return staff;
    }
}
