/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import GUI.BackUpPopUp;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author daniil
 */
public class DBConnect {
    
    String uName = "root";
    String uPass = "";
    String host = "jdbc:mysql://localhost:3306/chickcafedb"; 
    Connection con;
    SimpleDateFormat dF1;
    

    public DBConnect() throws SQLException {
        //this.con = DriverManager.getConnection( host, uName, uPass );
        dF1 = new SimpleDateFormat("dd-MM-yyyy");
    }
    
    public String[][] selectCustomer(String field, String value, String value2, boolean range) throws SQLException{    
        con = DriverManager.getConnection( host, uName, uPass ); 
        ArrayList<String[]> listOfLists = new ArrayList<String[]>();
        
        String selection = "";
        String query = "";
        if(field.equals("1")){
            selection = " WHERE `UserID` = ANY (SELECT `UserID` FROM `user` WHERE `Is_Logged_In` = '1' AND `User_Type` = 'Customer') ";
        }
        else if(field.equals("")){
            selection =  " where '" + field + "' = '" + value + "'";
        }
        else if(range){
            selection = " where ( " + field + " >= '" + value + "' AND " + field + " <= '" + value2 + "' )";
        } else {
            selection =  " where " + field + " = '" + value + "'";
        }
            
        query = "SELECT `CustomerID`, `UserID`, `First_Name`, `Surname`, `Email`, `Phone_Number`, `Total_Spent`, `VIP_Status`, `Discount_Type`, `Card_Number`, `Account_Number`, `Sort_Code`, `Name_Holder`, `Is_Credit`, `Card_Type`, `Security_Number`, `Expiration_Date`, `Notification` FROM `customer` " + selection;
        
        try (Statement stmt = con.createStatement()) {

            ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String[] a = new String[18];
                    
            a[0] = rs.getString("CustomerID");
            a[1] = rs.getString("UserID");
            a[2] = rs.getString("First_Name");
            a[3] = rs.getString("Surname");
            a[4] = rs.getString("Email");
            a[5] = rs.getString("Phone_Number");
            a[6] = rs.getString("Total_Spent");
            a[7] = rs.getString("VIP_Status");
            a[8] = rs.getString("Discount_Type");
            a[9] = rs.getString("Card_Number");
            a[10] = rs.getString("Account_Number");
            a[11] = rs.getString("Sort_Code");
            a[12] = rs.getString("Name_Holder");
            a[13] = rs.getString("Is_Credit");
            a[14] = rs.getString("Card_Type");
            a[15] = rs.getString("Security_Number");
            a[16] = rs.getString("Expiration_Date");
            a[17] = rs.getString("Notification");
            listOfLists.add(a);
        }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
        return (String[][]) listOfLists.toArray(new String[listOfLists.size()][18]);
        
    }
    
    public void insertCustomerRecord (String firstName, String surname, 
            String email, String username, String password, String phoneNumber) throws SQLException{ 
        con = DriverManager.getConnection( host, uName, uPass ); 
        String userQuery = "insert into user ( Username, Password, User_Type, Is_Logged_In ) values ( " + "'" + 
                username + "', '" + password + "', 'Customer', '0')";
        String customerQuery = "insert into customer ( UserID, First_Name, Surname, Email, "
                + "Phone_Number, Total_Spent, Discount_Type, Card_Number, Name_Holder, "
                + "Security_Number, Notification ) values ( (select UserID from user where "
                + "Username = '" + username + "'), '" + firstName + "', '" + 
                surname + "', '" + email + "', '" + phoneNumber + "', '0', "
                + "'None', 'None', 'None', '000', 'None')";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(userQuery);
            stmt.executeUpdate(customerQuery);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    }
    
    public void changeCustomer(String field, String value, String userID) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "update customer set " + field + " = '" + value + "' where UserID = '" + userID + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    }
    
    public void deleteCustomer(String UserID) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String userQuery = "delete from user where UserID = '" + UserID + "'"; 
        String customerQuery = "delete from customer where UserID = '" + UserID + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(userQuery);
            stmt.executeUpdate(customerQuery);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    }
    
    public String[][] selectStaff(String field, String value, String value2, boolean range) throws SQLException{   
        con = DriverManager.getConnection( host, uName, uPass ); 
        ArrayList<String[]> listOfLists = new ArrayList<String[]>();
        
        String selection = "";
        String query = "";
        if(range){
            selection = " where ( " + field + " >= '" + value + "' AND " + field + " <= '" + value2 + "' )";
            query = "SELECT `StaffID`, `UserID`, `First_Name`, `Surname`, `Staff_Type`, `Notification` FROM `staff`" + selection;
        } else if (field.equals("") && value.equals("")) {
            selection =  " WHERE " + field + " = '" + value + "'";
            query = "SELECT `StaffID`, `UserID`, `First_Name`, `Surname`, `Staff_Type`, `Notification` FROM `staff`" + selection;
        }
        else if(field.equals("4")){
            query = "SELECT * FROM `staff`";
        }
        else{
            selection =  " WHERE " + field + " = '" + value + "'";
            query = "SELECT `StaffID`, `UserID`, `First_Name`, `Surname`, `Staff_Type`, `Notification` FROM `staff`" + selection;
        }
                
        try (Statement stmt = con.createStatement()) {

            ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String[] a = new String[6];
            a[0] = rs.getString("StaffID");
            a[1] = rs.getString("UserID");
            a[2] = rs.getString("First_Name");
            a[3] = rs.getString("Surname");
            a[4] = rs.getString("Staff_Type");
            a[5] = rs.getString("Notification");
            listOfLists.add(a);
        }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
        return (String[][]) listOfLists.toArray(new String[listOfLists.size()][6]);
    }
    
    public void insertStaffRecord (String firstName, String surname, 
            String staffType, String username, String password) throws SQLException{ 
        con = DriverManager.getConnection( host, uName, uPass ); 
        String userQuery = "insert into user ( Username, Password, User_Type ) values ( " + "'" + username + "', '" + password + "', 'Staff' )";
        String staffQuery = "insert into staff ( UserID, First_Name, Surname, Staff_Type, Notification ) values ( "
                + "(select UserID from user where Username = '" + username + "'), '" + firstName + "', '" + surname + "', '" + staffType + "', 'None' )";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(userQuery);
            stmt.executeUpdate(staffQuery);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    }
    
    public void changeStaff(String field, String userID, String value) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "update staff set " + field + " = '" + value + "' where UserID = '" + userID + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    }
    
    public void deleteStaff(String UserID) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String userQuery = "delete from user where UserID = '" + UserID + "'";
        String staffQuery = "delete from staff where UserID = '" + UserID + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(userQuery);
            stmt.executeUpdate(staffQuery);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    }
    
    public String[][] selectUser(String field, String value) throws SQLException{    
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "select UserID, Username, Password, User_Type from user where " + field + " = '" + value + "'";
        ArrayList<String[]> listOfLists = new ArrayList<String[]>();
        
        try (Statement stmt = con.createStatement()) {

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String[] a = new String[4];
                a[0] = rs.getString("UserID");
                a[1] = rs.getString("Username");
                a[2] = rs.getString("Password");
                a[3] = rs.getString("User_Type");
                listOfLists.add(a);
            }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
        return (String[][]) listOfLists.toArray(new String[listOfLists.size()][4]);
    }
    
    public void changeUser(String field, String username, String value) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "update user set " + field + " = '" + value + "' where Username = '" + username + "'";
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    }
    
    public String[][] selectItem(String field, String value) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        ArrayList<String[]> listOfLists = new ArrayList<String[]>();
        //String query = "select Name, Availability, Menu_Type, Prep_Time, Course_Type, Is_Special, Price, Description from item where " + field + " = '" + value + "'";
        String query = null;
        if(!value.equals("")){
            query = "select ItemID, Name, Availability, Menu_Type, Prep_Time, Course_Type, Is_Special, Price, Description from item where " + field + " = '" + value + "'";
        }
        else{
            query = "select ItemID, Name, Availability, Menu_Type, Prep_Time, Course_Type, Is_Special, Price, Description from item";
        }
        
        try (Statement stmt = con.createStatement()) {

        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String[] a = new String[9];
            a[0] = rs.getString("ItemID");
            a[1] = rs.getString("Name");
            a[2] = rs.getString("Availability");
            a[3] = rs.getString("Menu_Type");
            a[4] = rs.getString("Prep_Time");
            a[5] = rs.getString("Course_Type");
            a[6] = rs.getString("Is_Special");
            a[7] = rs.getString("Price");
            a[8] = rs.getString("Description");
            listOfLists.add(a);
        }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
        return (String[][]) listOfLists.toArray(new String[listOfLists.size()][9]);
    }
    
    public String[] selectItemHeaders(){
        String[] a = new String[9];
        a[0] = "ItemID";
        a[1] = "Name";
        a[2] = "Availability";
        a[3] = "Menu_Type";
        a[4] = "Prep_Time";
        a[5] = "Course_Type";
        a[6] = "Is_Special";
        a[7] = "Price";
        a[8] = "Description";
       
        return a;
    }
    
    public String[] selectStockHeaders(){
        String[] a = new String[5];
        a[0] = "StockID";
        a[1] = "Name";
        a[2] = "Quantity";
        a[3] = "Minimum_Quantity";
        a[4] = "Maximum_Quantity";
       
        return a;
    }
    
    
    public String[] selectStaffHeaders(){
        String[] a = new String[5];
        a[0] = "StaffID";
        a[1] = "UserID";
        a[2] = "First_Name";
        a[3] = "Surname";
        a[4] = "Staff_Type";
        
        return a;
    }
    
    public String[] selectPendingOrderHeaders(){
        String[] a = new String[6];
        a[0] = "OrderID";
        a[1] = "Order_Date";
        a[2] = "Order_Time";
        a[3] = "Order_Status";
        a[4] = "Is_Priority";
        a[5] = "Is_Late";
       
        return a;
    }
    
    public String[] selectStaffPerformanceHeaders(){
        String[] a = new String[3];
        a[0] = "Menu_Type";
        a[1] = "Projected_Time";
        a[2] = "Actual_Time";
        
        return a;
    }
    
    public String[] selectLoggedInCustomerHeaders(){
        String[] a = new String[3];
        a[0] = "CustomerID";
        a[1] = "First_Name";
        a[2] = "Surname";
       
        return a;
    }
    
    public String[] selectCustomerHeaders(){
        String[] a = new String[16];
            a[0] = "CustomerID";
            a[1] = "UserID";
            a[2] = "First_Name";
            a[3] = "Surname";
            a[4] = "Email";
            a[5] = "Phone_Number";
            a[6] = "Total_Spent";
            a[7] = "VIP_Status";
            a[8] = "Discount_Type";
            a[9] = "Card_Number";
            a[10] = "Account_Number";
            a[11] = "Sort_Code";
            a[12] = "Name_Holder";
            a[13] = "Card_Type";
            a[14] = "Security_Number";
            a[15] = "Expiration_Date";
       
        return a;
    }
    
    public String[] customerSpendingHeaders(){
        String[] a = new String[4];
        a[0] = "Customer ID";
        a[1] = "First Name";
        a[2] = "Surname";
        a[3] = "Total Spent";
        
        return a;
    }
    
    public String[] refundReportHeaders(){
        String[] a = new String[10];
        a[0] = "Customer ID";
        a[1] = "First Name";
        a[2] = "Surname";
        a[3] = "Order ID";
        a[4] = "Order Date";
        a[5] = "Order Time";
        a[6] = "Order Status";
        a[7] = "Is Priority";
        a[8] = "Is Late";
        a[9] = "StaffID";
        
        return a;
    }
    
    public String[] ordersReportTable(){
        String[] a = new String[10];
        a[0] = "Customer ID";
        a[1] = "First Name";
        a[2] = "Surname";
        a[3] = "Order ID";
        a[4] = "Order Date";
        a[5] = "Order Time";
        a[6] = "Order Status";
        a[7] = "Is Priority";
        a[8] = "Is Late";
        a[9] = "StaffID";
        
        return a;
    }
    
    public void insertItem(String name, int availability, 
            String menuType, String prepTime, String courseType, int isSpecial, String price, String description) throws SQLException{ 
        con = DriverManager.getConnection( host, uName, uPass ); 
            String query = "insert into item ( Name, Availability, Menu_Type, Prep_Time, Course_Type, Is_Special, Price, Description ) values ( " + "'" + name + "', '" + availability + "', '" + menuType + "', '" + prepTime + "', '" + courseType + "', '" + isSpecial + "', '" + price + "', '" + description + "' )";
            try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    }
    
    public void changeItem(String field, String name, String value) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "update item set " + field + " = '" + value + "' where Name = '" + name + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    }
    
    public void deleteItem(String itemName) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "delete from item where Name = '" + itemName + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    }
    
    public String[][] selectOrder(String field, String value, String value2, boolean range) throws ParseException, SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        ArrayList<String[]> listOfLists = new ArrayList<String[]>();
        String selection = "";
        if(field.equals("Order_Date")){
            java.util.Date dateStr = dF1.parse(value);
            java.sql.Date Date1 = new java.sql.Date(dateStr.getTime());
            selection =  field + " = '" + Date1 + "'";
            if(!value2.equals("")){
                java.util.Date dateStr2 = dF1.parse(value2);
                java.sql.Date Date2 = new java.sql.Date(dateStr2.getTime());
                selection = "( " + field + " >= '" + Date1 + "' AND " + field + " <= '" + Date2 + "' )";
            }
        } else {
            if(range){
                selection = "( " + field + " >= '" + value + "' AND " + field + " <= '" + value2 + "' )";
            } else {
                selection =  field + " = '" + value + "'";
            }
        }       
        
        String query = "SELECT `OrderID`, `StaffID`, `CustomerID`, `Order_Date`, `Order_Time`, `Total_Price`, `Is_Refund`, `Order_Status`, `Is_Priority`, `Payment_Type`, `Menu_Type`, `Total_Preparation_Time`, `Is_Late`, `Actual_Time`, `Date_Flagged` FROM `order` WHERE " + selection;
                                                      
        try (Statement stmt = con.createStatement()) {

        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String[] a = new String[15];
            a[0] = rs.getString("OrderID");
            a[1] = rs.getString("StaffID");
            a[2] = rs.getString("CustomerID");
            a[3] = rs.getString("Order_Date");
            a[4] = rs.getString("Order_Time");
            a[5] = rs.getString("Total_Price");
            a[6] = rs.getString("Is_Refund");
            a[7] = rs.getString("Order_Status");
            a[8] = rs.getString("Is_Priority");
            a[9] = rs.getString("Payment_Type");
            a[10] = rs.getString("Menu_Type");
            a[11] = rs.getString("Total_Preparation_Time");
            a[12] = rs.getString("Is_Late");
            a[13] = rs.getString("Actual_Time");
            a[14] = rs.getString("Date_Flagged");
            listOfLists.add(a);
        }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
        return (String[][]) listOfLists.toArray(new String[listOfLists.size()][15]);
    };
    
    public void insertOrder(String customerID, String date, String time, String totalPrice, 
            String isRefund, String status, String isPriority, String menuType, 
            String totalPrepTime, String paymentType, String[][] items) throws ParseException, SQLException{ 
        con = DriverManager.getConnection( host, uName, uPass ); 
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date d = df.parse(date);
        java.sql.Date dateDB = new java.sql.Date(d.getTime());
        String query = "INSERT INTO `order` (`CustomerID`, `Order_Date`, `Order_Time`, `Total_Price`, `Is_Refund`, `Order_Status`, `Is_Priority`, `Payment_Type`, `Menu_Type`, `Total_Preparation_Time` ) VALUES ( " + customerID + ", '" + dateDB + "', '" + time + "', '" + totalPrice + "', '" + isRefund + "', '" + status + "', '" + isPriority + "', '" + paymentType + "', '" + menuType + "', '" + totalPrepTime + "' )";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        for(int i = 0; i<items.length; i++){
            String itemID = items[i][0];
            String quantity = items[i][1];
            
            String order_item_query = "INSERT INTO `order_item` ( `OrderID`, `ItemID`, `Quantity` ) VALUES ( (SELECT `OrderID` FROM `order` ORDER BY `OrderID` DESC LIMIT 1), " + itemID + ", " + quantity + " )";

            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(order_item_query);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        con.close();
    }
    
    public void changeOrder(String field, String orderID, String value) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "UPDATE `order` SET " + field + " = '" + value + "' WHERE `OrderID` = '" + orderID + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
    
    public void deleteOrder(int OrderID) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "delete from order where OrderID = '" + OrderID + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
    
    public String[][] selectStock(String field, String value) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
    ArrayList<String[]> listOfLists = new ArrayList<String[]>();
        
        String query = null;
        
        if(!value.equals("")){
            query = "select StockID, Name, Quantity, Minimum_Quantity, Maximum_Quantity from stock where " + field + " = '" + value + "'";
        }
        else{
            query = "select StockID, Name, Quantity, Minimum_Quantity, Maximum_Quantity from stock";
        }
        
        try (Statement stmt = con.createStatement()) {
        

        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String[] a = new String[5];
            a[0] = rs.getString("StockID");
            a[1] = rs.getString("Name");
            a[2] = rs.getString("Quantity");
            a[3] = rs.getString("Minimum_Quantity");
            a[4] = rs.getString("Maximum_Quantity");
            listOfLists.add(a);
        }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
        return (String[][]) listOfLists.toArray(new String[listOfLists.size()][5]);
    };
    
    public void insertStock(String name, String quantity, String min, String max) throws SQLException{ 
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "insert into stock ( Name, Quantity, Minimum_Quantity, Maximum_Quantity ) values ( " + "'" + name + "', '" + 
                quantity + "', '" + min + "', '" + max + "' )";
       
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    }
    
    public void changeStock(String field, String stockID, String value) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "update stock set " + field + " = '" + value + "' where StockID = '" + stockID + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
    
    public void deleteStock(String name) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "delete from stock where StockID = '" + name + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
    
    public String[][] selectItemStock(String field, String value) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
    ArrayList<String[]> listOfLists = new ArrayList<String[]>();
        String query = "select Item_StockID, ItemID, StockID, Quantity from Item_Stock where " + field + " = '" + value + "'";
        
        try (Statement stmt = con.createStatement()) {

        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String[] a = new String[4];
            a[0] = rs.getString("Item_StockID");
            a[1] = rs.getString("ItemID");
            a[2] = rs.getString("StockID");
            a[3] = rs.getString("Quantity");
            listOfLists.add(a);
        }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
        return (String[][]) listOfLists.toArray(new String[listOfLists.size()][4]);
    };
    
    public void insertItemStock(String ItemID, String StockID, int quantity) throws SQLException{ 
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "insert into item_stock ( ItemID, StockID, Quantity ) values (" + ItemID + ", '" + StockID + "', '" + quantity + "' )";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
    
    public String selectAutoUpdate() throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
    String date = "";
        String query = "SELECT `date` FROM `auto_update` WHERE 1";
        
        try (Statement stmt = con.createStatement()) {

        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            date = rs.getString("date");
        }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
        return date;
    }
    
    public void changeAutoUpdate(String newDate) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "UPDATE `auto_update` SET `date` = '" + newDate + "' WHERE 1 ";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
    
    public void changeItemStock(String field, String setField, String value, String setValue) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "update Item_Stock set " + field + " = '" + value + "' where " + setField + " = '" + setValue + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
    
    public void deleteItemStock(String field, String value ) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "delete from Item_Stock where " + field + " = '" + value + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
    
    public String[][] selectOrderItem(String field, String value) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
    ArrayList<String[]> listOfLists = new ArrayList<String[]>();
        String query = "select OrderID, ItemID, Quantity from Order_Item where " + field + " = '" + value + "'";
        
        try (Statement stmt = con.createStatement()) {

        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String[] a = new String[3];
            a[0] = rs.getString("OrderID");
            a[1] = rs.getString("ItemID");
            a[2] = rs.getString("Quantity");
            listOfLists.add(a);
        }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
        return (String[][]) listOfLists.toArray(new String[listOfLists.size()][3]);
    };
    
    public void insertOrderItem(String OrderID, String ItemID, int quantity) throws SQLException{ 
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "insert into order_item ( OrderID, ItemID, Quantity ) values ( " + OrderID + ", " + ItemID + ", '" + quantity + "' )";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
    
    public void changeOrderItem(String field, String setField, String value, String setValue) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "update order_item set " + field + " = '" + value + "' where " + setField + " = '" + setValue + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
    
    public void deleteOrderItem(String field, String value ) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "delete from Order_Item where " + field + " = '" + value + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
    
    public String[][] selectDiscount(String field, String value) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
    ArrayList<String[]> listOfLists = new ArrayList<String[]>();
        String query = "SELECT `VIP_Status`, `Fixed_Percentage`, `Flexible_Percentage`, `Threshold` FROM `discount` WHERE '" + field + "' = '" + value + "'";
        
        try (Statement stmt = con.createStatement()) {

        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String[] a = new String[4];
            a[0] = rs.getString("VIP_Status");
            a[1] = rs.getString("Fixed_Percentage");
            a[2] = rs.getString("Flexible_Percentage");
            a[3] = rs.getString("Threshold");
            listOfLists.add(a);
        }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
        return (String[][]) listOfLists.toArray(new String[listOfLists.size()][4]);
    }
    
    public void changeDiscount(String status, String field, String value) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "UPDATE `discount` set " + field + " = '" + value + "' WHERE `VIP_Status` = '" + status + "'";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
    
    public boolean createBackup() throws IOException, InterruptedException{
        String command;
        BackUpPopUp backPop = null;
        command = "C:\\wamp\\bin\\mysql\\mysql5.6.17\\bin\\mysqldump -u Neko -pNeko chickcafedb -r C:\\chickCafeBackUp\\backup.sql";
        Process runtimeProcess =Runtime.getRuntime().exec(command);
        int processComplete = runtimeProcess.waitFor();
        if(processComplete == 0){
            backPop = new BackUpPopUp("Backup taken successfully!");
            backPop.setVisible(true);
            backPop.setResizable(false);
            backPop.setFocusable(true);
            return true;
        } else {
            backPop = new BackUpPopUp("Could not take mysql backup!");
            backPop.setVisible(true);
            backPop.setResizable(false);
            backPop.setFocusable(true);
            return false;
        }
    }
    public boolean restoreDatabase() throws IOException, InterruptedException{
        String[] command = new String[]{"cmd.exe", "/c", "C:\\wamp\\bin\\mysql\\mysql5.6.17\\bin\\mysql.exe -u Neko -pNeko chickcafedb < C:\\chickCafeBackUp\\backup.sql"};
        Process runtimeProcess = Runtime.getRuntime().exec(command);
        BackUpPopUp backPop = null;
        int processComplete = runtimeProcess.waitFor();
        if (processComplete == 0) {
            backPop = new BackUpPopUp("Backup restored successfully!");
            backPop.setVisible(true);
            backPop.setResizable(false);
            backPop.setFocusable(true);
            return true;
        } else {
            backPop = new BackUpPopUp("Could not restore backup!");
            backPop.setVisible(true);
            backPop.setResizable(false);
            backPop.setFocusable(true);
            return false;
        }
    }
    
    public String[][] selectPayment(String field, String value) throws SQLException{
        con = DriverManager.getConnection( host, uName, uPass ); 
    ArrayList<String[]> listOfLists = new ArrayList<String[]>();
        String query = "SELECT `PaymentID`, `CustomerID`, `Payment_Type`, `Card_Type`, `Total_Amount`, `Number_Of_Orders` FROM payment WHERE " + field + " = '" + value + "'";
        
        try (Statement stmt = con.createStatement()) {

        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String[] a = new String[6];
            a[0] = rs.getString("PaymentID");
            a[1] = rs.getString("CustomerID");
            a[2] = rs.getString("Payment_Type");
            a[3] = rs.getString("Card_Type");
            a[3] = rs.getString("Total_Amount");
            a[3] = rs.getString("Number_Of_Orders");
            listOfLists.add(a);
        }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
        return (String[][]) listOfLists.toArray(new String[listOfLists.size()][6]);
    }
    
    public void insertPayment(String customerID, String paymentType, String cardType, String totalAmount, String numberOfOrders) throws SQLException{ 
        con = DriverManager.getConnection( host, uName, uPass ); 
        String query = "INSERT INTO `payment` ( `CustomerID`, `Payment_Type`, `Card_Type`, `Total_Amount`, `Number_Of_Orders` ) values ( " + customerID + ", '" + paymentType + "', '" + cardType + "', '" + totalAmount + "', '" + numberOfOrders + "' )";
        
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.close();
    };
}

