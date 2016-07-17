package Accounts;

import java.sql.SQLException;

public class Staff extends User {

    private final String staffID;
    private final String staffType;
    
    public Staff(String username, String password, String firstName, 
            String lastName, String userType, String userID, String staffType, String staffID ) throws SQLException {
        super(username, password, firstName, lastName, userType, userID);
        this.staffID = staffID;
        this.staffType = staffType;
    }

    public String getStaffID() {
        return staffID;
    }
    
    public String getStaffType() {
        return staffType;
    }
}
