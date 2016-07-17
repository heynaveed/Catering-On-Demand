package Controllers;

import Accounts.Customer;
import Accounts.Staff;
import Accounts.User;
import Database.DBConnect;
import GUI.LoginRegister;
import GUI.MainDashboard;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainController {
    
    private JFrame frame;
    private static MainController instance;

    private LoginRegister loginRegister;
    private MainDashboard dash;
    private Customer customer;
    private final DBConnect con;
    
    public static MainController getInstance(){
        
        return instance;
    }
    public MainController() throws Exception{
        this.con = new DBConnect();
        CoDStart();
    }
    public static void main(String[] args) throws SQLException, Exception{
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    boot();
                } catch (Exception ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    public static void boot() throws Exception{
        instance = new MainController();
    }

    public void CoDStart() throws Exception{
        if(frame != null){
            frame.dispose();
        }
        createLoginPage();
    }
    
    public void createLoginPage() throws Exception{
        loginRegister = new LoginRegister(this);
        frame = new JFrame("Catering On Demand");
        frame.add(loginRegister, BorderLayout.CENTER);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        initialiseFrame(frame);
    }
    
    public void dashboard(Customer customer) throws Exception{
        frame.dispose();
        dash = new MainDashboard(this, customer);
        dash.getJPanelMainDisplay().removeAll();
        dash.getJPanelMainDisplay().repaint();
        dash.getJPanelMainDisplay().revalidate();
        dash.getJPanelMainDisplay().add(dash.getJScrollPaneHome());
        dash.getJPanelMainDisplay().repaint();
        dash.getJPanelMainDisplay().revalidate(); 
        frame = new JFrame("Catering On Demand");
        frame.add(dash, BorderLayout.CENTER);
        initialiseFrame(frame);
    }
    
    public void dashboard(Staff staff) throws Exception{
        frame.dispose();
        dash = new MainDashboard(this, staff);
        dash.getJPanelMainDisplay().removeAll();
        dash.getJPanelMainDisplay().repaint();
        dash.getJPanelMainDisplay().revalidate();
        dash.getJPanelMainDisplay().add(dash.getJScrollPaneHome());
        dash.getJPanelMainDisplay().repaint();
        dash.getJPanelMainDisplay().revalidate(); 
        frame = new JFrame("Catering On Demand");
        frame.add(dash, BorderLayout.CENTER);
        initialiseFrame(frame);
    }
    
    public void initialiseFrame(JFrame frame) throws Exception{
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.repaint();
        frame.validate();
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
    
    public Customer getCustomer(){
        return customer;
    }
    
    public void setCustomer(Customer customer){
        this.customer = customer;
    }
}

