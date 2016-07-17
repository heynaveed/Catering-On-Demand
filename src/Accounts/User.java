package Accounts;

import java.sql.SQLException;

public class User {
    
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String userType;
    private String ID;
    private boolean isLoggedIn = true;
    
    public User(String username, String password, String firstName, 
            String lastName, String userType, String userID) throws SQLException{
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.ID = userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserType() {
        return userType;
    }
    
    public boolean getIsLoggedIn(){
        return isLoggedIn;
    }
    
    public String getID(){
        return ID;
    }
    
    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public void setIsLoggedIn(boolean isLoggedIn){
        this.isLoggedIn = isLoggedIn;
    }
    
    public void setID(String ID){
        this.ID = ID;
    }
    
    public void toggleLoginStatus(){
        isLoggedIn = !isLoggedIn;
        // Code to change information in Database goes here.
    }
    
    public void viewMenu(){
        // Code to access information in menu database goes here, depending on userType.
    }
}
