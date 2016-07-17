/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Accounts.Customer;
import Accounts.Staff;
import Controllers.MainController;
import Database.DBConnect;
import Ordering.Item;
import Ordering.Order;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import static java.lang.Math.pow;
import static java.lang.Thread.sleep;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Holds on the main GUI surface for the system.
 */
public class MainDashboard extends JPanel {

    private static final int panelYPos = 100;
    private static final int basketPanelYDistance = 31;
    private static final int orderPanelYDistance = 156;
    private final MainController system;
    private final DBConnect con;
    private final Order totalOrder;
    private final Order breakfastOrder;
    private final Order lunchOrder;
    private final Order dinnerOrder;
    private final Order drinksOrder;
    private Order orderToFlag;
    private static final String breakfastPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\breakfastIcon.png";
    private static final String lunchPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\lunchIcon.png";
    private static final String dinnerPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\dinnerIcon.png";
    private static final String drinksPath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\drinkIcon.png";
    private static final String cafePath = "C:\\Users\\Naveed PC\\Documents\\NetBeansProjects\\CoD\\src\\Images\\cafeIcon.jpeg";
    private static ImageIcon cafeIcon;
    private Image cafeImg;
    private final Customer customer;
    private final Staff staff;
    private static ImageIcon breakfastIcon;
    private Image breakfastImg;
    private static ImageIcon lunchIcon;
    private Image lunchImg;
    private static ImageIcon dinnerIcon;
    private Image dinnerImg;
    private static ImageIcon drinksIcon;
    private Image drinksImg;
    private final ArrayList<Item> currentMenuItems;
    private final ArrayList<Order> currentOrderItems;
    private final ArrayList<MenuItemPanel> menuPanel;
    private final ArrayList<QueueItemPanel> queuePanel;
    private final DecimalFormat dF;
    private boolean isBreakfastMenuPainted = false;
    private boolean isLunchMenuPainted = false;
    private boolean isDinnerMenuPainted = false;
    private boolean isDrinksMenuPainted = false;
    private boolean isBreakfastQueuePainted = false;
    private boolean isLunchQueuePainted = false;
    private boolean isDinnerQueuePainted = false;
    private int orderPanelCount = 0;
    private boolean isBreakfastOrderUp = false;
    private boolean isLunchOrderUp = false;
    private boolean isDinnerOrderUp = false;
    private boolean isDrinksOrderUp = false;
    private final DateFormat dF1;
    private final DateFormat dF2;
    private final String breakfastStartTime;
    private final String lunchStartTime;
    private final String dinnerStartTime;
    private final String globalUserType;
    private final String globalStaffType;
    private final DateFormat DF;
    private String staffNameDisplay = "";
    private String staffIDDisplay = "";
    private static final Color buttonColor = new Color(117, 59, 18);
    private static final Color buttonBorderColor = new Color(15, 6, 6);
    private static final Color buttonTextColor = new Color(220, 142, 0);
    private static final Color panelColor = new Color(199, 159, 79);
    private static final Color mainBackground = new Color(199, 159, 108);
    private boolean logoutPressed = false;
    
    /**
     * Constructor for customer.
     */
    public MainDashboard(MainController system, Customer customer) throws SQLException, ParseException, IOException, InterruptedException{
        
        this.system = system;
        con = new DBConnect();
        this.customer = customer;
        totalOrder = new Order(null, customer.getCustomerID(), "Total");
        breakfastOrder = new Order(null, customer.getCustomerID(), "Breakfast");
        lunchOrder = new Order(null, customer.getCustomerID(), "Lunch");
        dinnerOrder = new Order(null, customer.getCustomerID(), "Dinner");
        drinksOrder = new Order(null, customer.getCustomerID(), "Drinks");
        this.staff = null;
        currentMenuItems = new ArrayList<>();
        currentOrderItems = new ArrayList<>();
        DF = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dF1 = new SimpleDateFormat("dd-MM-yyyy");
        dF2 = new SimpleDateFormat("HH:mm:ss");        
        globalUserType = "Customer";
        globalStaffType = "";
        breakfastStartTime = "09:00:00";
        lunchStartTime = "12:00:00";
        dinnerStartTime = "17:00:00";

        
        
        
        menuPanel = new ArrayList<>();
        queuePanel = new ArrayList<>();
        dF = new DecimalFormat("##0.00");
        initComponents();
        currentDateTime();
        jLabelLoggedInAs.setText("Logged in as: " + customer.getUsername());
        jScrollPaneBasket.getVerticalScrollBar().setUnitIncrement(16);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        queueUpdater();
        AUTOupdate();
        jScrollPane2.getViewport().setOpaque(false);
        jTextArea1.setOpaque(false);
        setColours();
        jButtonStock.setVisible(false);
        jButtonQueue.setVisible(false);
        jButtonSettings.setVisible(false);
        
        jButton6.setVisible(false);
        jButton9.setVisible(false);
        jButton10.setVisible(false);
        jButtonStaffReport.setVisible(false);
        customerNotificationUpdater(customer);
    }
    
    /**
     * Constructor for Staff
     */
    public MainDashboard(MainController system, Staff staff) throws SQLException, ParseException, IOException, InterruptedException{
        this.system = system;
        con = new DBConnect();
        this.staff = staff;
        totalOrder = new Order(null, "", "Total");
        breakfastOrder = new Order(null, "", "Breakfast");
        lunchOrder = new Order(null, "", "Lunch");
        dinnerOrder = new Order(null, "", "Dinner");
        drinksOrder = new Order(null, "", "Drinks");
        currentMenuItems = new ArrayList<>();
        currentOrderItems = new ArrayList<>();
        DF = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dF1 = new SimpleDateFormat("dd-MM-yyyy");
        dF2 = new SimpleDateFormat("HH:mm:ss");        
        this.customer = null;
        globalUserType = "Staff";
        globalStaffType = staff.getStaffType();
        breakfastStartTime = "09:00:00";
        lunchStartTime = "12:00:00";
        dinnerStartTime = "17:00:00";
        
        
        menuPanel = new ArrayList<>();
        queuePanel = new ArrayList<>();
        dF = new DecimalFormat("##0.00");
        initComponents();
        currentDateTime();
        jLabelLoggedInAs.setText("Logged in as: " + staff.getUsername());
        jScrollPaneBasket.getVerticalScrollBar().setUnitIncrement(16);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        queueUpdater();
        AUTOupdate();
        jScrollPane2.getViewport().setOpaque(false);
        jTextArea1.setOpaque(false);
        setColours();
        staffNotificationUpdater(staff);
        
        System.out.println(globalStaffType);
        
        if(globalStaffType.equals("Manager")){
            jButtonBackUpSystem.setEnabled(false);
            jButtonRestoreSystem.setEnabled(false);
        }
        
        if(globalStaffType.equals("Kitchen") || globalStaffType.equals("Waiting")){
            jButtonStock.setVisible(false);
            jButtonSettings.setVisible(false);
            jButton6.setVisible(false);
            jButton8.setVisible(false);
            jButton9.setVisible(false);
            jButton10.setVisible(false);
            jButtonStaffReport.setVisible(true);
        }
        
        jButtonProceedToPayment.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jPanelMainFrame = new javax.swing.JPanel();
        jPanelSideBar = new javax.swing.JPanel();
        jButtonMenu = new javax.swing.JButton();
        jButtonHistory = new javax.swing.JButton();
        jButtonStock = new javax.swing.JButton();
        jButtonQueue = new javax.swing.JButton();
        jButtonSettings = new javax.swing.JButton();
        jPanelUpperBar = new javax.swing.JPanel();
        jButtonHome = new javax.swing.JButton();
        jButtonBasket = new javax.swing.JButton();
        jButtonLogout = new javax.swing.JButton();
        jLabelChickCafeLogo = new javax.swing.JLabel();
        jPanelInformationBar = new javax.swing.JPanel();
        jLabelFenixSystems = new javax.swing.JLabel();
        jLabelDate = new javax.swing.JLabel();
        jLabelTime = new javax.swing.JLabel();
        jLabelLoggedInAs = new javax.swing.JLabel();
        jPanelMainDisplay = new javax.swing.JPanel();
        jScrollPaneHome = new javax.swing.JScrollPane();
        jPanelHome = new javax.swing.JPanel();
        jLabelHomeTitle = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPaneHistory = new javax.swing.JScrollPane();
        jPanelHistory = new javax.swing.JPanel();
        jLabelHistoryTitle = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButtonStaffReport = new javax.swing.JButton();
        jScrollPaneQueue = new javax.swing.JScrollPane();
        jPanelQueue = new javax.swing.JPanel();
        jPanelQueueSelection = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanelIntegratedQueue = new javax.swing.JPanel();
        jScrollPaneBreakfastQueue = new javax.swing.JScrollPane();
        jPanelBreakfastQueue = new javax.swing.JPanel();
        jScrollPaneLunchQueue = new javax.swing.JScrollPane();
        jPanelLunchQueue = new javax.swing.JPanel();
        jScrollPaneDinnerQueue = new javax.swing.JScrollPane();
        jPanelDinnerQueue = new javax.swing.JPanel();
        jLabelQueueTitle = new javax.swing.JLabel();
        jScrollPaneSettings = new javax.swing.JScrollPane();
        jPanelSettings = new javax.swing.JPanel();
        jLabelSettingsTitle = new javax.swing.JLabel();
        jButtonItems = new javax.swing.JButton();
        jButtonBackUpSystem = new javax.swing.JButton();
        jButtonRestoreSystem = new javax.swing.JButton();
        jButtonAccountManagement = new javax.swing.JButton();
        jScrollPaneBasket = new javax.swing.JScrollPane();
        jPanelBasket = new javax.swing.JPanel();
        jLabelBasketTitle = new javax.swing.JLabel();
        jButtonProceedToPayment = new javax.swing.JButton();
        jButtonClearBasket = new javax.swing.JButton();
        jScrollPaneMenu = new javax.swing.JScrollPane();
        jPanelMenu = new javax.swing.JPanel();
        jLabelMenuTitle = new javax.swing.JLabel();
        jButtonBreakfast = new javax.swing.JButton();
        jButtonLunch = new javax.swing.JButton();
        jButtonDinner = new javax.swing.JButton();
        jButtonDrinks = new javax.swing.JButton();
        jPanelIntegratedMenu = new javax.swing.JPanel();
        jScrollPaneBreakfastMenu = new javax.swing.JScrollPane();
        jPanelBreakfastMenu = new javax.swing.JPanel();
        jScrollPaneLunchMenu = new javax.swing.JScrollPane();
        jPanelLunchMenu = new javax.swing.JPanel();
        jScrollPaneDinnerMenu = new javax.swing.JScrollPane();
        jPanelDinnerMenu = new javax.swing.JPanel();
        jScrollPaneDrinksMenu = new javax.swing.JScrollPane();
        jPanelDrinksMenu = new javax.swing.JPanel();
        jButtonCheckout = new javax.swing.JButton();
        jScrollPaneAddItem = new javax.swing.JScrollPane();
        jPanelAddItem = new javax.swing.JPanel();
        jLabelAddItemTitle = new javax.swing.JLabel();
        jLabelName = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jLabelPreparationTime = new javax.swing.JLabel();
        jTextFieldPreparationTime = new javax.swing.JTextField();
        jLabelPrice = new javax.swing.JLabel();
        jTextFieldPrice = new javax.swing.JTextField();
        jLabelDescription = new javax.swing.JLabel();
        jTextFieldDescription = new javax.swing.JTextField();
        jButtonAddToDatabase = new javax.swing.JButton();
        jButtonBackToItems1 = new javax.swing.JButton();
        jCheckBoxBreakfast = new javax.swing.JCheckBox();
        jCheckBoxLunch = new javax.swing.JCheckBox();
        jCheckBoxDinner = new javax.swing.JCheckBox();
        jCheckBoxDrinks = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jCheckBoxStarter = new javax.swing.JCheckBox();
        jLabelChooseCourse = new javax.swing.JLabel();
        jCheckBoxMain = new javax.swing.JCheckBox();
        jCheckBoxDessert = new javax.swing.JCheckBox();
        jCheckBoxDailySpecial = new javax.swing.JCheckBox();
        jScrollPaneAddStock = new javax.swing.JScrollPane();
        jPanelAddStock = new javax.swing.JPanel();
        jLabelAddStockTitle = new javax.swing.JLabel();
        jLabelName1 = new javax.swing.JLabel();
        jTextFieldName1 = new javax.swing.JTextField();
        jLabelPreparationTime1 = new javax.swing.JLabel();
        jTextFieldQuantity = new javax.swing.JTextField();
        jButtonAddStockToDatabase = new javax.swing.JButton();
        jButtonBackToStock1 = new javax.swing.JButton();
        jLabelFillDetails = new javax.swing.JLabel();
        jTextFieldMin = new javax.swing.JTextField();
        jTextFieldMax = new javax.swing.JTextField();
        jLabelName2 = new javax.swing.JLabel();
        jLabelName3 = new javax.swing.JLabel();
        jScrollPaneUpdateStockQuantity = new javax.swing.JScrollPane();
        jPanelDeleteStock1 = new javax.swing.JPanel();
        jLabelUpdateQuantityTitle = new javax.swing.JLabel();
        jButtonUpdateStockInDatabase = new javax.swing.JButton();
        jButtonBackToStock3 = new javax.swing.JButton();
        jLabelChooseStock1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaStockDisplay2 = new javax.swing.JTextArea();
        jTextFieldStockName = new javax.swing.JTextField();
        jTextFieldStockQuantityToAdd = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPanePayment = new javax.swing.JScrollPane();
        jPanelPayment = new javax.swing.JPanel();
        jLabelPayment = new javax.swing.JLabel();
        jButtonBackToBasket = new javax.swing.JButton();
        jButtonAddNewCard = new javax.swing.JButton();
        jButtonUseExistingCard = new javax.swing.JButton();
        jButtonBankTransfer = new javax.swing.JButton();
        jLabelSelectPaymentMethod = new javax.swing.JLabel();
        jPanelIntegratedPayment = new javax.swing.JPanel();
        jScrollPaneCard = new javax.swing.JScrollPane();
        jPanelCard = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabelCardNameHolder = new javax.swing.JLabel();
        jLabelCardNumber = new javax.swing.JLabel();
        jLabelExpirationDate = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPasswordSecurityNumber = new javax.swing.JPasswordField();
        jButtonConfirmPayment = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabelDiscountsApplied = new javax.swing.JLabel();
        jLabelTotalPrice = new javax.swing.JLabel();
        jLabelBreakfast = new javax.swing.JLabel();
        jLabelLunch = new javax.swing.JLabel();
        jLabelDinner = new javax.swing.JLabel();
        jLabelDrinks = new javax.swing.JLabel();
        jLabelIsCredit = new javax.swing.JLabel();
        jLabelCardType = new javax.swing.JLabel();
        jScrollPaneBankTransfer = new javax.swing.JScrollPane();
        jPanelBankTransfer = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldBankName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldAccountNumber = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldSortCode = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldNameHolder = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButtonConfirmPayment1 = new javax.swing.JButton();
        jLabelTotalPrice1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabelBreakfast2 = new javax.swing.JLabel();
        jLabelLunch2 = new javax.swing.JLabel();
        jLabelDinner2 = new javax.swing.JLabel();
        jLabelDrinks2 = new javax.swing.JLabel();
        jLabelDiscountsApplied1 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jScrollPaneViewOrder = new javax.swing.JScrollPane();
        jPanelViewOrder = new javax.swing.JPanel();
        jLabelViewOrderTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaItemsInOrder = new javax.swing.JTextArea();
        jButtonBackToQueue = new javax.swing.JButton();
        jLabelOrderNoOrder = new javax.swing.JLabel();
        jLabelTotalPriceOrder = new javax.swing.JLabel();
        jLabelCustomerIDOrder = new javax.swing.JLabel();
        jLabelPreparationTimeOrder = new javax.swing.JLabel();
        jLabelNoOfItemsOrder = new javax.swing.JLabel();
        jLabelStatusOrder = new javax.swing.JLabel();
        jButtonFlagOrder = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButtonSendNotification = new javax.swing.JButton();
        jScrollPaneEditItems = new javax.swing.JScrollPane();
        jPanelEditItems = new javax.swing.JPanel();
        jLabelEditItemsTitle = new javax.swing.JLabel();
        jButtonBack2 = new javax.swing.JButton();
        jScrollPaneIntegratedEditItems = new javax.swing.JScrollPane();
        jButton1 = new javax.swing.JButton();
        jButtonDeleteSelectedItem = new javax.swing.JButton();
        jButtonAddStockToItem = new javax.swing.JButton();
        jScrollPaneEditStock = new javax.swing.JScrollPane();
        jPanelEditStock = new javax.swing.JPanel();
        jLabelEditItemsTitle1 = new javax.swing.JLabel();
        jScrollPaneIntegratedEditStock = new javax.swing.JScrollPane();
        jButtonAddStock = new javax.swing.JButton();
        jButtonMakeOrder = new javax.swing.JButton();
        jButtonDeleteSelectedStock = new javax.swing.JButton();
        jScrollPaneAccountManagement = new javax.swing.JScrollPane();
        jPanelAccountManagement = new javax.swing.JPanel();
        jLabelAccountManagementTitle = new javax.swing.JLabel();
        jButtonStaff = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButtonBackToSettings = new javax.swing.JButton();
        jScrollPaneEditStaffAccount = new javax.swing.JScrollPane();
        jPanelEditStaffAccount = new javax.swing.JPanel();
        jLabelEditStaffAccount = new javax.swing.JLabel();
        jButtonBackToAccountManagement = new javax.swing.JButton();
        jScrollPaneIntegratedEditStaffAccount = new javax.swing.JScrollPane();
        jButton5 = new javax.swing.JButton();
        jButtonDeleteSelectedStaffAccount = new javax.swing.JButton();
        jScrollPaneAddStaff = new javax.swing.JScrollPane();
        jPanelAddStaff = new javax.swing.JPanel();
        jLabelAddStaffTitle = new javax.swing.JLabel();
        jLabelName4 = new javax.swing.JLabel();
        jTextFieldUsername = new javax.swing.JTextField();
        jLabelPreparationTime2 = new javax.swing.JLabel();
        jLabelPrice1 = new javax.swing.JLabel();
        jLabelDescription1 = new javax.swing.JLabel();
        jTextFieldFirstName = new javax.swing.JTextField();
        jButtonAddToDatabase1 = new javax.swing.JButton();
        jButtonBackToItems2 = new javax.swing.JButton();
        jCheckBoxKitchen = new javax.swing.JCheckBox();
        jCheckBoxWaiting = new javax.swing.JCheckBox();
        jCheckBoxManager = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        jPasswordPassword = new javax.swing.JPasswordField();
        jPasswordConfirmPassword = new javax.swing.JPasswordField();
        jLabelName5 = new javax.swing.JLabel();
        jTextFieldSurname = new javax.swing.JTextField();
        jScrollPaneEditCustomerAccount = new javax.swing.JScrollPane();
        jPanelEditCustomerAccount = new javax.swing.JPanel();
        jLabelEditCustomerAccount = new javax.swing.JLabel();
        jButtonBackToAccountManagement1 = new javax.swing.JButton();
        jScrollPaneIntegratedEditCustomerAccount = new javax.swing.JScrollPane();
        jButtonDeleteSelectedCustomerAccount = new javax.swing.JButton();
        jScrollPaneCurrentPending = new javax.swing.JScrollPane();
        jPanelCurrentPending = new javax.swing.JPanel();
        jLabelCurrentPending1 = new javax.swing.JLabel();
        jButtonBackToHistory1 = new javax.swing.JButton();
        jScrollPaneIntegratedCurrentPending = new javax.swing.JScrollPane();
        jButtonRefresh = new javax.swing.JButton();
        jLabelCurrentPending2 = new javax.swing.JLabel();
        jButtonSelectCustomer = new javax.swing.JButton();
        jScrollPanePendingOrders = new javax.swing.JScrollPane();
        jPanelPendingOrders = new javax.swing.JPanel();
        jLabelPendingOrders = new javax.swing.JLabel();
        jButtonBackToHistory2 = new javax.swing.JButton();
        jScrollPaneIntegratedPendingOrders = new javax.swing.JScrollPane();
        jButtonRefresh2 = new javax.swing.JButton();
        jLabelCustomerNameID = new javax.swing.JLabel();
        jButtonPrintPending = new javax.swing.JButton();
        jScrollPaneCustomerSpending = new javax.swing.JScrollPane();
        jPanelCustomerSpending = new javax.swing.JPanel();
        jLabelCustomerSpending = new javax.swing.JLabel();
        jButtonBackToHistory3 = new javax.swing.JButton();
        jScrollPaneIntegratedCustomerSpending = new javax.swing.JScrollPane();
        jButtonGenerateCustomerSpending = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldDate1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldDate2 = new javax.swing.JTextField();
        jButtonPrintCustomerSpendingReport = new javax.swing.JButton();
        jScrollPaneRefundReport = new javax.swing.JScrollPane();
        jPanelRefundReport = new javax.swing.JPanel();
        jLabelRefundReport = new javax.swing.JLabel();
        jButtonBackToHistory4 = new javax.swing.JButton();
        jScrollPaneIntegratedRefundReport = new javax.swing.JScrollPane();
        jButtonGenerateRefundReport = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldDate3 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jTextFieldDate4 = new javax.swing.JTextField();
        jButtonPrintRefundReport = new javax.swing.JButton();
        jScrollPaneOrdersReport = new javax.swing.JScrollPane();
        jPanelOrdersReport = new javax.swing.JPanel();
        jLabelOrdersReport = new javax.swing.JLabel();
        jButtonBackToHistory5 = new javax.swing.JButton();
        jScrollPaneIntegratedOrdersReport = new javax.swing.JScrollPane();
        jButtonGenerateOrdersReport = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jTextFieldDate5 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jTextFieldDate6 = new javax.swing.JTextField();
        jButtonPrintOrdersReport = new javax.swing.JButton();
        jScrollPaneAddItemStock = new javax.swing.JScrollPane();
        jPanelAddItemStock = new javax.swing.JPanel();
        jLabelAddItemStock = new javax.swing.JLabel();
        jButtonBack3 = new javax.swing.JButton();
        jScrollPaneIntegratedAddItemStock = new javax.swing.JScrollPane();
        jButtonAddStockItem2 = new javax.swing.JButton();
        jLabelEditingFor = new javax.swing.JLabel();
        jScrollPaneStaffSelection = new javax.swing.JScrollPane();
        jPanelStaffSelection = new javax.swing.JPanel();
        jLabelStaffSelection = new javax.swing.JLabel();
        jButtonBackToHistory6 = new javax.swing.JButton();
        jScrollPaneIntegratedStaffSelection = new javax.swing.JScrollPane();
        jButtonRefresh1 = new javax.swing.JButton();
        jButtonSelectStaff = new javax.swing.JButton();
        jScrollPaneStaffPerformance = new javax.swing.JScrollPane();
        jPanelStaffPerformance = new javax.swing.JPanel();
        jLabelPendingOrders1 = new javax.swing.JLabel();
        jButtonBackToHistory7 = new javax.swing.JButton();
        jScrollPaneIntegratedStaffPerformance = new javax.swing.JScrollPane();
        jButtonRefresh3 = new javax.swing.JButton();
        jButtonPrintStaffPerformanceReport = new javax.swing.JButton();
        jLabelViewingFor = new javax.swing.JLabel();
        jTextFieldDate7 = new javax.swing.JTextField();
        jTextFieldDate8 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(900, 550));

        jPanelMainFrame.setPreferredSize(new java.awt.Dimension(900, 550));

        jPanelSideBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonMenu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonMenu.setText("MENU");
        jButtonMenu.setToolTipText("");
        jButtonMenu.setName(""); // NOI18N
        jButtonMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMenuActionPerformed(evt);
            }
        });

        jButtonHistory.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonHistory.setText("HISTORY");
        jButtonHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHistoryActionPerformed(evt);
            }
        });

        jButtonStock.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonStock.setText("STOCK");
        jButtonStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStockActionPerformed(evt);
            }
        });

        jButtonQueue.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonQueue.setText("QUEUE");
        jButtonQueue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQueueActionPerformed(evt);
            }
        });

        jButtonSettings.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonSettings.setText("SETTINGS");
        jButtonSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSettingsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSideBarLayout = new javax.swing.GroupLayout(jPanelSideBar);
        jPanelSideBar.setLayout(jPanelSideBarLayout);
        jPanelSideBarLayout.setHorizontalGroup(
            jPanelSideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSideBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonQueue, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonStock, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonHistory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelSideBarLayout.setVerticalGroup(
            jPanelSideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSideBarLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jButtonMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonStock, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonQueue, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelUpperBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonHome.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonHome.setText("Home");
        jButtonHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHomeActionPerformed(evt);
            }
        });

        jButtonBasket.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonBasket.setText("Basket: £0.00");
        jButtonBasket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBasketActionPerformed(evt);
            }
        });

        jButtonLogout.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonLogout.setText("Logout");
        jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelUpperBarLayout = new javax.swing.GroupLayout(jPanelUpperBar);
        jPanelUpperBar.setLayout(jPanelUpperBarLayout);
        jPanelUpperBarLayout.setHorizontalGroup(
            jPanelUpperBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelUpperBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonHome, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonBasket, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLogout, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelUpperBarLayout.setVerticalGroup(
            jPanelUpperBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelUpperBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelUpperBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonLogout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonBasket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabelChickCafeLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/oie_transparent.png"))); // NOI18N

        jPanelInformationBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelFenixSystems.setFont(new java.awt.Font("Lucida Calligraphy", 1, 12)); // NOI18N
        jLabelFenixSystems.setText("Fenix Systems");

        jLabelDate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelDate.setText("Date");

        jLabelTime.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelTime.setText("Time");

        javax.swing.GroupLayout jPanelInformationBarLayout = new javax.swing.GroupLayout(jPanelInformationBar);
        jPanelInformationBar.setLayout(jPanelInformationBarLayout);
        jPanelInformationBarLayout.setHorizontalGroup(
            jPanelInformationBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInformationBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelFenixSystems)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelLoggedInAs, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(142, 142, 142)
                .addComponent(jLabelTime)
                .addGap(18, 18, 18)
                .addComponent(jLabelDate)
                .addGap(19, 19, 19))
        );
        jPanelInformationBarLayout.setVerticalGroup(
            jPanelInformationBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInformationBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelInformationBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelLoggedInAs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelInformationBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelFenixSystems)
                        .addComponent(jLabelDate)
                        .addComponent(jLabelTime)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanelMainDisplay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelMainDisplay.setLayout(new java.awt.CardLayout());

        jLabelHomeTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelHomeTitle.setText("Home");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Located in the heart of Islington, Chick Café is a dedicated to excellence \ncafeteria that offers delicious food and drinks. Serving fabulous delicacies, \nChick Cafe enjoys its legacy as one of London’s most popular made to order \nspots. Boasting a long-held Michelin star, the tranquil, leafy surroundings \nare offset by a bustling open kitchen.\n\nShowcasing crisp presentation and moving with the seasons, Chick Cafe menu \nchanges regularly for optimum freshness and variety of choice. Homemade \nsandwiches and succulent antipasti make for wonderfully natural flavours \nat Chick Cafe, whilst dishes finished in the impressive wood-fired oven \ninclude roasted turbot, or slow-cooked veal with saffron risotto. A rich \nchocolate nemesis heads a decadent dessert list. A library of freshly squeezed, \nhot and soft drinks at Chick Café compiles a meticulously sourced stock. \nThe serves up refreshing blends,like prosecco with fresh blood orange.\n\nChick Café is open on Monday – Saturday, \n7am – 10pm, and on Sunday, 8am – 9pm. \nIt has 4 menus for different times of the day: \n•\t  Breakfast menu: from opening - 11am  \n•\t  Lunch menu: 12pm - 5pm  \n•\t  Dinner menu: 6pm – until closing  \n•\t  Drinks menu: Available all day  \n");
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 664, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelHomeLayout = new javax.swing.GroupLayout(jPanelHome);
        jPanelHome.setLayout(jPanelHomeLayout);
        jPanelHomeLayout.setHorizontalGroup(
            jPanelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHomeLayout.createSequentialGroup()
                .addGroup(jPanelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelHomeLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelHomeLayout.createSequentialGroup()
                        .addGap(329, 329, 329)
                        .addComponent(jLabelHomeTitle)))
                .addContainerGap(701, Short.MAX_VALUE))
        );
        jPanelHomeLayout.setVerticalGroup(
            jPanelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHomeLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabelHomeTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(888, Short.MAX_VALUE))
        );

        jScrollPaneHome.setViewportView(jPanelHome);

        jPanelMainDisplay.add(jScrollPaneHome, "card2");

        jLabelHistoryTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelHistoryTitle.setText("History");

        jButton6.setText("Logged In Customer Order Report");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton8.setText("Customer Spending Report");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Refund Report");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Orders Report");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButtonStaffReport.setText("Staff Report");
        jButtonStaffReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStaffReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelHistoryLayout = new javax.swing.GroupLayout(jPanelHistory);
        jPanelHistory.setLayout(jPanelHistoryLayout);
        jPanelHistoryLayout.setHorizontalGroup(
            jPanelHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHistoryLayout.createSequentialGroup()
                .addGroup(jPanelHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelHistoryLayout.createSequentialGroup()
                        .addGap(345, 345, 345)
                        .addComponent(jLabelHistoryTitle))
                    .addGroup(jPanelHistoryLayout.createSequentialGroup()
                        .addGap(260, 260, 260)
                        .addGroup(jPanelHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonStaffReport, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(900, Short.MAX_VALUE))
        );
        jPanelHistoryLayout.setVerticalGroup(
            jPanelHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHistoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelHistoryTitle)
                .addGap(44, 44, 44)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonStaffReport, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(971, Short.MAX_VALUE))
        );

        jScrollPaneHistory.setViewportView(jPanelHistory);

        jPanelMainDisplay.add(jScrollPaneHistory, "card2");

        jButton2.setText("Dinner");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Breakfast");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Lunch");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelQueueSelectionLayout = new javax.swing.GroupLayout(jPanelQueueSelection);
        jPanelQueueSelection.setLayout(jPanelQueueSelectionLayout);
        jPanelQueueSelectionLayout.setHorizontalGroup(
            jPanelQueueSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelQueueSelectionLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanelQueueSelectionLayout.setVerticalGroup(
            jPanelQueueSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelQueueSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelQueueSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanelIntegratedQueue.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanelBreakfastQueueLayout = new javax.swing.GroupLayout(jPanelBreakfastQueue);
        jPanelBreakfastQueue.setLayout(jPanelBreakfastQueueLayout);
        jPanelBreakfastQueueLayout.setHorizontalGroup(
            jPanelBreakfastQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );
        jPanelBreakfastQueueLayout.setVerticalGroup(
            jPanelBreakfastQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
        );

        jScrollPaneBreakfastQueue.setViewportView(jPanelBreakfastQueue);

        jPanelIntegratedQueue.add(jScrollPaneBreakfastQueue, "card2");

        javax.swing.GroupLayout jPanelLunchQueueLayout = new javax.swing.GroupLayout(jPanelLunchQueue);
        jPanelLunchQueue.setLayout(jPanelLunchQueueLayout);
        jPanelLunchQueueLayout.setHorizontalGroup(
            jPanelLunchQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );
        jPanelLunchQueueLayout.setVerticalGroup(
            jPanelLunchQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        jScrollPaneLunchQueue.setViewportView(jPanelLunchQueue);

        jPanelIntegratedQueue.add(jScrollPaneLunchQueue, "card3");

        javax.swing.GroupLayout jPanelDinnerQueueLayout = new javax.swing.GroupLayout(jPanelDinnerQueue);
        jPanelDinnerQueue.setLayout(jPanelDinnerQueueLayout);
        jPanelDinnerQueueLayout.setHorizontalGroup(
            jPanelDinnerQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );
        jPanelDinnerQueueLayout.setVerticalGroup(
            jPanelDinnerQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 436, Short.MAX_VALUE)
        );

        jScrollPaneDinnerQueue.setViewportView(jPanelDinnerQueue);

        jPanelIntegratedQueue.add(jScrollPaneDinnerQueue, "card4");

        jLabelQueueTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelQueueTitle.setText("Queue");

        javax.swing.GroupLayout jPanelQueueLayout = new javax.swing.GroupLayout(jPanelQueue);
        jPanelQueue.setLayout(jPanelQueueLayout);
        jPanelQueueLayout.setHorizontalGroup(
            jPanelQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelQueueLayout.createSequentialGroup()
                .addGroup(jPanelQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelQueueSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelQueueLayout.createSequentialGroup()
                            .addGap(53, 53, 53)
                            .addComponent(jPanelIntegratedQueue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelQueueLayout.createSequentialGroup()
                            .addGap(331, 331, 331)
                            .addComponent(jLabelQueueTitle))))
                .addContainerGap(718, Short.MAX_VALUE))
        );
        jPanelQueueLayout.setVerticalGroup(
            jPanelQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelQueueLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelQueueTitle)
                .addGap(18, 18, 18)
                .addComponent(jPanelQueueSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jPanelIntegratedQueue, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(896, Short.MAX_VALUE))
        );

        jScrollPaneQueue.setViewportView(jPanelQueue);

        jPanelMainDisplay.add(jScrollPaneQueue, "card2");

        jLabelSettingsTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelSettingsTitle.setText("Settings");

        jButtonItems.setText("Items");
        jButtonItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonItemsActionPerformed(evt);
            }
        });

        jButtonBackUpSystem.setText("Back-Up System");
        jButtonBackUpSystem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackUpSystemActionPerformed(evt);
            }
        });

        jButtonRestoreSystem.setText("Restore System");
        jButtonRestoreSystem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRestoreSystemActionPerformed(evt);
            }
        });

        jButtonAccountManagement.setText("Account Management");
        jButtonAccountManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAccountManagementActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSettingsLayout = new javax.swing.GroupLayout(jPanelSettings);
        jPanelSettings.setLayout(jPanelSettingsLayout);
        jPanelSettingsLayout.setHorizontalGroup(
            jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSettingsLayout.createSequentialGroup()
                .addGroup(jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelSettingsLayout.createSequentialGroup()
                        .addGap(345, 345, 345)
                        .addComponent(jLabelSettingsTitle)
                        .addGap(24, 24, 24))
                    .addGroup(jPanelSettingsLayout.createSequentialGroup()
                        .addGap(297, 297, 297)
                        .addGroup(jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonRestoreSystem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonBackUpSystem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelSettingsLayout.createSequentialGroup()
                                .addComponent(jButtonItems, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jButtonAccountManagement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(921, Short.MAX_VALUE))
        );
        jPanelSettingsLayout.setVerticalGroup(
            jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelSettingsTitle)
                .addGap(18, 18, 18)
                .addComponent(jButtonItems, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonBackUpSystem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRestoreSystem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAccountManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1069, Short.MAX_VALUE))
        );

        jScrollPaneSettings.setViewportView(jPanelSettings);

        jPanelMainDisplay.add(jScrollPaneSettings, "card2");

        jLabelBasketTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelBasketTitle.setText("Basket");

        jButtonProceedToPayment.setText("Proceed To Payment");
        jButtonProceedToPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProceedToPaymentActionPerformed(evt);
            }
        });

        jButtonClearBasket.setText("Clear Basket");
        jButtonClearBasket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearBasketActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelBasketLayout = new javax.swing.GroupLayout(jPanelBasket);
        jPanelBasket.setLayout(jPanelBasketLayout);
        jPanelBasketLayout.setHorizontalGroup(
            jPanelBasketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBasketLayout.createSequentialGroup()
                .addGroup(jPanelBasketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBasketLayout.createSequentialGroup()
                        .addGap(338, 338, 338)
                        .addComponent(jLabelBasketTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelBasketLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(jButtonClearBasket, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(130, 130, 130)
                        .addComponent(jButtonProceedToPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(809, Short.MAX_VALUE))
        );
        jPanelBasketLayout.setVerticalGroup(
            jPanelBasketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBasketLayout.createSequentialGroup()
                .addComponent(jLabelBasketTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelBasketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonProceedToPayment)
                    .addComponent(jButtonClearBasket))
                .addGap(0, 1217, Short.MAX_VALUE))
        );

        jScrollPaneBasket.setViewportView(jPanelBasket);

        jPanelMainDisplay.add(jScrollPaneBasket, "card2");

        jLabelMenuTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelMenuTitle.setText("Menu");

        jButtonBreakfast.setText("Breakfast");
        jButtonBreakfast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBreakfastActionPerformed(evt);
            }
        });

        jButtonLunch.setText("Lunch");
        jButtonLunch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLunchActionPerformed(evt);
            }
        });

        jButtonDinner.setText("Dinner");
        jButtonDinner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDinnerActionPerformed(evt);
            }
        });

        jButtonDrinks.setText("Drinks");
        jButtonDrinks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDrinksActionPerformed(evt);
            }
        });

        jPanelIntegratedMenu.setPreferredSize(new java.awt.Dimension(605, 311));
        jPanelIntegratedMenu.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanelBreakfastMenuLayout = new javax.swing.GroupLayout(jPanelBreakfastMenu);
        jPanelBreakfastMenu.setLayout(jPanelBreakfastMenuLayout);
        jPanelBreakfastMenuLayout.setHorizontalGroup(
            jPanelBreakfastMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 705, Short.MAX_VALUE)
        );
        jPanelBreakfastMenuLayout.setVerticalGroup(
            jPanelBreakfastMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
        );

        jScrollPaneBreakfastMenu.setViewportView(jPanelBreakfastMenu);

        jPanelIntegratedMenu.add(jScrollPaneBreakfastMenu, "card2");

        javax.swing.GroupLayout jPanelLunchMenuLayout = new javax.swing.GroupLayout(jPanelLunchMenu);
        jPanelLunchMenu.setLayout(jPanelLunchMenuLayout);
        jPanelLunchMenuLayout.setHorizontalGroup(
            jPanelLunchMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 603, Short.MAX_VALUE)
        );
        jPanelLunchMenuLayout.setVerticalGroup(
            jPanelLunchMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 678, Short.MAX_VALUE)
        );

        jScrollPaneLunchMenu.setViewportView(jPanelLunchMenu);

        jPanelIntegratedMenu.add(jScrollPaneLunchMenu, "card2");

        javax.swing.GroupLayout jPanelDinnerMenuLayout = new javax.swing.GroupLayout(jPanelDinnerMenu);
        jPanelDinnerMenu.setLayout(jPanelDinnerMenuLayout);
        jPanelDinnerMenuLayout.setHorizontalGroup(
            jPanelDinnerMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 603, Short.MAX_VALUE)
        );
        jPanelDinnerMenuLayout.setVerticalGroup(
            jPanelDinnerMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 679, Short.MAX_VALUE)
        );

        jScrollPaneDinnerMenu.setViewportView(jPanelDinnerMenu);

        jPanelIntegratedMenu.add(jScrollPaneDinnerMenu, "card2");

        javax.swing.GroupLayout jPanelDrinksMenuLayout = new javax.swing.GroupLayout(jPanelDrinksMenu);
        jPanelDrinksMenu.setLayout(jPanelDrinksMenuLayout);
        jPanelDrinksMenuLayout.setHorizontalGroup(
            jPanelDrinksMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 698, Short.MAX_VALUE)
        );
        jPanelDrinksMenuLayout.setVerticalGroup(
            jPanelDrinksMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 687, Short.MAX_VALUE)
        );

        jScrollPaneDrinksMenu.setViewportView(jPanelDrinksMenu);

        jPanelIntegratedMenu.add(jScrollPaneDrinksMenu, "card2");

        jButtonCheckout.setText("Checkout");
        jButtonCheckout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelMenuLayout = new javax.swing.GroupLayout(jPanelMenu);
        jPanelMenu.setLayout(jPanelMenuLayout);
        jPanelMenuLayout.setHorizontalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMenuLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelIntegratedMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelMenuLayout.createSequentialGroup()
                                .addComponent(jButtonBreakfast, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jButtonLunch, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonDinner, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addComponent(jButtonDrinks, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanelMenuLayout.createSequentialGroup()
                        .addGap(335, 335, 335)
                        .addComponent(jLabelMenuTitle))
                    .addGroup(jPanelMenuLayout.createSequentialGroup()
                        .addGap(291, 291, 291)
                        .addComponent(jButtonCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(748, Short.MAX_VALUE))
        );
        jPanelMenuLayout.setVerticalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelMenuTitle)
                .addGap(18, 18, 18)
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonBreakfast)
                    .addComponent(jButtonLunch)
                    .addComponent(jButtonDinner)
                    .addComponent(jButtonDrinks))
                .addGap(18, 18, 18)
                .addComponent(jPanelIntegratedMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckout)
                .addContainerGap(892, Short.MAX_VALUE))
        );

        jScrollPaneMenu.setViewportView(jPanelMenu);

        jPanelMainDisplay.add(jScrollPaneMenu, "card2");

        jLabelAddItemTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelAddItemTitle.setText("Add Item");

        jLabelName.setText("Name:");

        jLabelPreparationTime.setText("Preparation Time:");

        jLabelPrice.setText("Price:");

        jLabelDescription.setText("Description:");

        jButtonAddToDatabase.setText("Add To Database");
        jButtonAddToDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddToDatabaseActionPerformed(evt);
            }
        });

        jButtonBackToItems1.setText("Back");
        jButtonBackToItems1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToItems1ActionPerformed(evt);
            }
        });

        jCheckBoxBreakfast.setText("Breakfast");
        jCheckBoxBreakfast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxBreakfastActionPerformed(evt);
            }
        });

        jCheckBoxLunch.setText("Lunch");
        jCheckBoxLunch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxLunchActionPerformed(evt);
            }
        });

        jCheckBoxDinner.setText("Dinner");
        jCheckBoxDinner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDinnerActionPerformed(evt);
            }
        });

        jCheckBoxDrinks.setText("Drinks");
        jCheckBoxDrinks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDrinksActionPerformed(evt);
            }
        });

        jLabel1.setText("Fill in details and select a menu:");

        jCheckBoxStarter.setText("Starter");
        jCheckBoxStarter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxStarterActionPerformed(evt);
            }
        });

        jLabelChooseCourse.setText("Choose a course type:");

        jCheckBoxMain.setText("Main");
        jCheckBoxMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMainActionPerformed(evt);
            }
        });

        jCheckBoxDessert.setText("Dessert");
        jCheckBoxDessert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDessertActionPerformed(evt);
            }
        });

        jCheckBoxDailySpecial.setText("Daily Special");
        jCheckBoxDailySpecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDailySpecialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelAddItemLayout = new javax.swing.GroupLayout(jPanelAddItem);
        jPanelAddItem.setLayout(jPanelAddItemLayout);
        jPanelAddItemLayout.setHorizontalGroup(
            jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddItemLayout.createSequentialGroup()
                .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAddItemLayout.createSequentialGroup()
                        .addComponent(jButtonBackToItems1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(190, 190, 190)
                        .addComponent(jLabelAddItemTitle))
                    .addGroup(jPanelAddItemLayout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelAddItemLayout.createSequentialGroup()
                                .addComponent(jLabelPreparationTime)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldPreparationTime, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelAddItemLayout.createSequentialGroup()
                                .addComponent(jLabelName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelAddItemLayout.createSequentialGroup()
                                .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelDescription)
                                    .addComponent(jLabelPrice))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(74, 74, 74)
                        .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxBreakfast)
                            .addComponent(jCheckBoxLunch)
                            .addComponent(jCheckBoxDinner)
                            .addComponent(jCheckBoxDrinks)
                            .addComponent(jCheckBoxDailySpecial)))
                    .addGroup(jPanelAddItemLayout.createSequentialGroup()
                        .addGap(298, 298, 298)
                        .addComponent(jLabel1))
                    .addGroup(jPanelAddItemLayout.createSequentialGroup()
                        .addGap(308, 308, 308)
                        .addComponent(jButtonAddToDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelAddItemLayout.createSequentialGroup()
                        .addGap(263, 263, 263)
                        .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelAddItemLayout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(jLabelChooseCourse))
                            .addGroup(jPanelAddItemLayout.createSequentialGroup()
                                .addComponent(jCheckBoxStarter)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBoxMain)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBoxDessert)))))
                .addContainerGap(905, Short.MAX_VALUE))
        );
        jPanelAddItemLayout.setVerticalGroup(
            jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddItemLayout.createSequentialGroup()
                .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelAddItemLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelAddItemTitle))
                    .addComponent(jButtonBackToItems1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAddItemLayout.createSequentialGroup()
                        .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelPreparationTime)
                            .addComponent(jTextFieldPreparationTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelPrice))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelDescription)
                            .addComponent(jTextFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelAddItemLayout.createSequentialGroup()
                        .addComponent(jCheckBoxBreakfast)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxLunch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxDinner)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxDrinks)))
                .addGap(1, 1, 1)
                .addComponent(jCheckBoxDailySpecial)
                .addGap(18, 18, 18)
                .addComponent(jLabelChooseCourse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxStarter)
                    .addComponent(jCheckBoxMain)
                    .addComponent(jCheckBoxDessert))
                .addGap(24, 24, 24)
                .addComponent(jButtonAddToDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(935, Short.MAX_VALUE))
        );

        jScrollPaneAddItem.setViewportView(jPanelAddItem);

        jPanelMainDisplay.add(jScrollPaneAddItem, "card2");

        jLabelAddStockTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelAddStockTitle.setText("Add Stock");

        jLabelName1.setText("Name:");

        jLabelPreparationTime1.setText("Quantity (if any):");

        jButtonAddStockToDatabase.setText("Add To Database");
        jButtonAddStockToDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddStockToDatabaseActionPerformed(evt);
            }
        });

        jButtonBackToStock1.setText("Back");
        jButtonBackToStock1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToStock1ActionPerformed(evt);
            }
        });

        jLabelFillDetails.setText("Fill in details:");

        jLabelName2.setText("Minimum Quantity: ");

        jLabelName3.setText("Maximum Quantity:");

        javax.swing.GroupLayout jPanelAddStockLayout = new javax.swing.GroupLayout(jPanelAddStock);
        jPanelAddStock.setLayout(jPanelAddStockLayout);
        jPanelAddStockLayout.setHorizontalGroup(
            jPanelAddStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddStockLayout.createSequentialGroup()
                .addGroup(jPanelAddStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAddStockLayout.createSequentialGroup()
                        .addComponent(jButtonBackToStock1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(190, 190, 190)
                        .addComponent(jLabelAddStockTitle))
                    .addGroup(jPanelAddStockLayout.createSequentialGroup()
                        .addGap(342, 342, 342)
                        .addComponent(jLabelFillDetails))
                    .addGroup(jPanelAddStockLayout.createSequentialGroup()
                        .addGap(253, 253, 253)
                        .addGroup(jPanelAddStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelAddStockLayout.createSequentialGroup()
                                .addComponent(jLabelPreparationTime1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelAddStockLayout.createSequentialGroup()
                                .addComponent(jLabelName1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldName1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelAddStockLayout.createSequentialGroup()
                                .addComponent(jLabelName3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldMax, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelAddStockLayout.createSequentialGroup()
                                .addComponent(jLabelName2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldMin, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanelAddStockLayout.createSequentialGroup()
                        .addGap(296, 296, 296)
                        .addComponent(jButtonAddStockToDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(952, Short.MAX_VALUE))
        );
        jPanelAddStockLayout.setVerticalGroup(
            jPanelAddStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddStockLayout.createSequentialGroup()
                .addGroup(jPanelAddStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelAddStockLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelAddStockTitle))
                    .addComponent(jButtonBackToStock1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addComponent(jLabelFillDetails)
                .addGap(18, 18, 18)
                .addGroup(jPanelAddStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelName1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelAddStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPreparationTime1)
                    .addComponent(jTextFieldQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelAddStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelName2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelAddStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelName3))
                .addGap(29, 29, 29)
                .addComponent(jButtonAddStockToDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1025, Short.MAX_VALUE))
        );

        jScrollPaneAddStock.setViewportView(jPanelAddStock);

        jPanelMainDisplay.add(jScrollPaneAddStock, "card2");

        jLabelUpdateQuantityTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelUpdateQuantityTitle.setText("Update Quantity");

        jButtonUpdateStockInDatabase.setText("Update");
        jButtonUpdateStockInDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateStockInDatabaseActionPerformed(evt);
            }
        });

        jButtonBackToStock3.setText("Back");
        jButtonBackToStock3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToStock3ActionPerformed(evt);
            }
        });

        jLabelChooseStock1.setText("Type which stock to update:");

        jTextAreaStockDisplay2.setColumns(20);
        jTextAreaStockDisplay2.setRows(5);
        jScrollPane3.setViewportView(jTextAreaStockDisplay2);

        jLabel2.setText("Name:");

        jLabel3.setText("Quantity to Add:");

        javax.swing.GroupLayout jPanelDeleteStock1Layout = new javax.swing.GroupLayout(jPanelDeleteStock1);
        jPanelDeleteStock1.setLayout(jPanelDeleteStock1Layout);
        jPanelDeleteStock1Layout.setHorizontalGroup(
            jPanelDeleteStock1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDeleteStock1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabelChooseStock1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelDeleteStock1Layout.createSequentialGroup()
                .addComponent(jButtonBackToStock3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(179, 179, 179)
                .addComponent(jLabelUpdateQuantityTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelDeleteStock1Layout.createSequentialGroup()
                .addGap(277, 277, 277)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(880, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDeleteStock1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelDeleteStock1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanelDeleteStock1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldStockQuantityToAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldStockName, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(317, 317, 317))
            .addGroup(jPanelDeleteStock1Layout.createSequentialGroup()
                .addGap(287, 287, 287)
                .addComponent(jButtonUpdateStockInDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelDeleteStock1Layout.setVerticalGroup(
            jPanelDeleteStock1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDeleteStock1Layout.createSequentialGroup()
                .addGroup(jPanelDeleteStock1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelDeleteStock1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelUpdateQuantityTitle))
                    .addComponent(jButtonBackToStock3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabelChooseStock1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanelDeleteStock1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldStockName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDeleteStock1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldStockQuantityToAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonUpdateStockInDatabase)
                .addGap(94, 94, 94))
        );

        jScrollPaneUpdateStockQuantity.setViewportView(jPanelDeleteStock1);

        jPanelMainDisplay.add(jScrollPaneUpdateStockQuantity, "card2");

        jLabelPayment.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelPayment.setText("Payment");

        jButtonBackToBasket.setText("Back");
        jButtonBackToBasket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToBasketActionPerformed(evt);
            }
        });

        jButtonAddNewCard.setText("Add New Card");
        jButtonAddNewCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddNewCardActionPerformed(evt);
            }
        });

        jButtonUseExistingCard.setText("Use Existing Card");
        jButtonUseExistingCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUseExistingCardActionPerformed(evt);
            }
        });

        jButtonBankTransfer.setText("Bank Transfer");
        jButtonBankTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBankTransferActionPerformed(evt);
            }
        });

        jLabelSelectPaymentMethod.setText("Select a payment method:");

        jPanelIntegratedPayment.setLayout(new java.awt.CardLayout());

        jScrollPaneCard.setPreferredSize(new java.awt.Dimension(596, 217));

        jLabelCardNameHolder.setText("jLabel4");

        jLabelCardNumber.setText("jLabel4");

        jLabelExpirationDate.setText("jLabel4");

        jLabel4.setText("Enter Security Number:");

        jButtonConfirmPayment.setText("Confirm Payment");
        jButtonConfirmPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfirmPaymentActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("You will be charged when clicking:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordSecurityNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonConfirmPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCardNameHolder)
                            .addComponent(jLabelCardNumber)
                            .addComponent(jLabelExpirationDate))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelCardNameHolder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelCardNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelExpirationDate)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jPasswordSecurityNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonConfirmPayment)
                .addContainerGap())
        );

        jPanel2.setFocusTraversalPolicyProvider(true);

        jLabelDiscountsApplied.setText("jLabel6");

        jLabelTotalPrice.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelTotalPrice.setText("jLabel6");

        jLabelIsCredit.setText("jLabel6");

        jLabelCardType.setText("jLabel6");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelTotalPrice)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelDiscountsApplied)
                            .addComponent(jLabelIsCredit)
                            .addComponent(jLabelCardType))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelBreakfast)
                            .addComponent(jLabelLunch)
                            .addComponent(jLabelDinner)
                            .addComponent(jLabelDrinks))
                        .addGap(84, 84, 84))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDiscountsApplied)
                    .addComponent(jLabelBreakfast))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelIsCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelLunch)
                    .addComponent(jLabelCardType))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jLabelDinner)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelDrinks)
                .addGap(59, 59, 59)
                .addComponent(jLabelTotalPrice)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout jPanelCardLayout = new javax.swing.GroupLayout(jPanelCard);
        jPanelCard.setLayout(jPanelCardLayout);
        jPanelCardLayout.setHorizontalGroup(
            jPanelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(145, Short.MAX_VALUE))
        );
        jPanelCardLayout.setVerticalGroup(
            jPanelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(67, Short.MAX_VALUE))
        );

        jScrollPaneCard.setViewportView(jPanelCard);

        jPanelIntegratedPayment.add(jScrollPaneCard, "card3");

        jScrollPaneBankTransfer.setPreferredSize(new java.awt.Dimension(596, 217));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Enter details to complete bank transfer:");

        jLabel6.setText("Bank Name:");

        jLabel7.setText("Account Number:");

        jLabel8.setText("Sort Code:");

        jLabel9.setText("Name Holder:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("You will be charged when clicking:");

        jButtonConfirmPayment1.setText("Confirm Payment");
        jButtonConfirmPayment1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfirmPayment1ActionPerformed(evt);
            }
        });

        jLabelTotalPrice1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelTotalPrice1.setText("jLabel6");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabelLunch2, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabelDinner2))
                    .addComponent(jLabelBreakfast2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelDrinks2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelBreakfast2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelLunch2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelDinner2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelDrinks2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabelDiscountsApplied1.setText("jLabel6");

        jLabel24.setText("Please use details from the Home page.");

        javax.swing.GroupLayout jPanelBankTransferLayout = new javax.swing.GroupLayout(jPanelBankTransfer);
        jPanelBankTransfer.setLayout(jPanelBankTransferLayout);
        jPanelBankTransferLayout.setHorizontalGroup(
            jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBankTransferLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonConfirmPayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelBankTransferLayout.createSequentialGroup()
                        .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldNameHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldSortCode, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldAccountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldBankName, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelBankTransferLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel10))
                    .addGroup(jPanelBankTransferLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel11)))
                .addGap(32, 32, 32)
                .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTotalPrice1)
                    .addGroup(jPanelBankTransferLayout.createSequentialGroup()
                        .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelDiscountsApplied1)
                            .addComponent(jLabel24))
                        .addGap(75, 75, 75)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelBankTransferLayout.setVerticalGroup(
            jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBankTransferLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBankTransferLayout.createSequentialGroup()
                        .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabelDiscountsApplied1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTextFieldBankName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextFieldAccountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldSortCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextFieldNameHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel24)))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBankTransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonConfirmPayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTotalPrice1))
                .addGap(99, 99, 99))
        );

        jScrollPaneBankTransfer.setViewportView(jPanelBankTransfer);

        jPanelIntegratedPayment.add(jScrollPaneBankTransfer, "card2");

        javax.swing.GroupLayout jPanelPaymentLayout = new javax.swing.GroupLayout(jPanelPayment);
        jPanelPayment.setLayout(jPanelPaymentLayout);
        jPanelPaymentLayout.setHorizontalGroup(
            jPanelPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPaymentLayout.createSequentialGroup()
                .addGroup(jPanelPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPaymentLayout.createSequentialGroup()
                        .addGroup(jPanelPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonBackToBasket, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelPaymentLayout.createSequentialGroup()
                                .addGap(147, 147, 147)
                                .addComponent(jButtonAddNewCard, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanelPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelPaymentLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabelPayment))
                            .addGroup(jPanelPaymentLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanelPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelSelectPaymentMethod)
                                    .addGroup(jPanelPaymentLayout.createSequentialGroup()
                                        .addComponent(jButtonUseExistingCard, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonBankTransfer, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanelPaymentLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jPanelIntegratedPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(793, Short.MAX_VALUE))
        );
        jPanelPaymentLayout.setVerticalGroup(
            jPanelPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPaymentLayout.createSequentialGroup()
                .addGroup(jPanelPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonBackToBasket, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPayment))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelSelectPaymentMethod)
                .addGap(21, 21, 21)
                .addGroup(jPanelPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAddNewCard)
                    .addComponent(jButtonUseExistingCard)
                    .addComponent(jButtonBankTransfer))
                .addGap(18, 18, 18)
                .addComponent(jPanelIntegratedPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1215, 1215, 1215))
        );

        jScrollPanePayment.setViewportView(jPanelPayment);

        jPanelMainDisplay.add(jScrollPanePayment, "card2");

        jLabelViewOrderTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelViewOrderTitle.setText("Order");

        jTextAreaItemsInOrder.setColumns(20);
        jTextAreaItemsInOrder.setRows(5);
        jScrollPane1.setViewportView(jTextAreaItemsInOrder);

        jButtonBackToQueue.setText("Back");
        jButtonBackToQueue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToQueueActionPerformed(evt);
            }
        });

        jLabelOrderNoOrder.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelOrderNoOrder.setText("Order No.");

        jLabelTotalPriceOrder.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelTotalPriceOrder.setText("Total Price:");

        jLabelCustomerIDOrder.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelCustomerIDOrder.setText("Customer ID:");

        jLabelPreparationTimeOrder.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelPreparationTimeOrder.setText("Preparation Time:");

        jLabelNoOfItemsOrder.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelNoOfItemsOrder.setText("No. of Items:");

        jLabelStatusOrder.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelStatusOrder.setText("Status:");

        jButtonFlagOrder.setText("Flag Order");
        jButtonFlagOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFlagOrderActionPerformed(evt);
            }
        });

        jLabel12.setText("Select the button below to start preparation:");

        jLabel13.setText("Or press back to return to queue.");

        jButtonSendNotification.setText("Send Notification");
        jButtonSendNotification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendNotificationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelViewOrderLayout = new javax.swing.GroupLayout(jPanelViewOrder);
        jPanelViewOrder.setLayout(jPanelViewOrderLayout);
        jPanelViewOrderLayout.setHorizontalGroup(
            jPanelViewOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelViewOrderLayout.createSequentialGroup()
                .addGroup(jPanelViewOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelViewOrderLayout.createSequentialGroup()
                        .addComponent(jButtonBackToQueue, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(213, 213, 213)
                        .addComponent(jLabelViewOrderTitle))
                    .addGroup(jPanelViewOrderLayout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addGroup(jPanelViewOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelOrderNoOrder)
                            .addComponent(jLabelTotalPriceOrder)
                            .addComponent(jLabelCustomerIDOrder)
                            .addComponent(jLabelPreparationTimeOrder)
                            .addComponent(jLabelNoOfItemsOrder)
                            .addComponent(jLabelStatusOrder)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonFlagOrder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13)
                            .addComponent(jButtonSendNotification, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(78, 78, 78)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(786, Short.MAX_VALUE))
        );
        jPanelViewOrderLayout.setVerticalGroup(
            jPanelViewOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelViewOrderLayout.createSequentialGroup()
                .addGroup(jPanelViewOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelViewOrderLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelViewOrderTitle))
                    .addComponent(jButtonBackToQueue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43)
                .addGroup(jPanelViewOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelViewOrderLayout.createSequentialGroup()
                        .addComponent(jLabelOrderNoOrder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTotalPriceOrder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCustomerIDOrder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelPreparationTimeOrder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelNoOfItemsOrder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelStatusOrder)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonFlagOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSendNotification)
                .addContainerGap(939, Short.MAX_VALUE))
        );

        jScrollPaneViewOrder.setViewportView(jPanelViewOrder);

        jPanelMainDisplay.add(jScrollPaneViewOrder, "card2");

        jLabelEditItemsTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelEditItemsTitle.setText("Edit Items");

        jButtonBack2.setText("Back");
        jButtonBack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBack2ActionPerformed(evt);
            }
        });

        jButton1.setText("Add Item");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButtonDeleteSelectedItem.setText("Delete Selected");
        jButtonDeleteSelectedItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteSelectedItemActionPerformed(evt);
            }
        });

        jButtonAddStockToItem.setText("Select Stock");
        jButtonAddStockToItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddStockToItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelEditItemsLayout = new javax.swing.GroupLayout(jPanelEditItems);
        jPanelEditItems.setLayout(jPanelEditItemsLayout);
        jPanelEditItemsLayout.setHorizontalGroup(
            jPanelEditItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditItemsLayout.createSequentialGroup()
                .addGroup(jPanelEditItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEditItemsLayout.createSequentialGroup()
                        .addComponent(jButtonBack2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(231, 231, 231)
                        .addComponent(jLabelEditItemsTitle))
                    .addGroup(jPanelEditItemsLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPaneIntegratedEditItems, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanelEditItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonDeleteSelectedItem, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                            .addComponent(jButtonAddStockToItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(699, Short.MAX_VALUE))
        );
        jPanelEditItemsLayout.setVerticalGroup(
            jPanelEditItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditItemsLayout.createSequentialGroup()
                .addGroup(jPanelEditItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEditItemsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelEditItemsTitle))
                    .addComponent(jButtonBack2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelEditItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEditItemsLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jScrollPaneIntegratedEditItems, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelEditItemsLayout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeleteSelectedItem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAddStockToItem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(916, Short.MAX_VALUE))
        );

        jScrollPaneEditItems.setViewportView(jPanelEditItems);

        jPanelMainDisplay.add(jScrollPaneEditItems, "card2");

        jLabelEditItemsTitle1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelEditItemsTitle1.setText("Edit Stock");

        jButtonAddStock.setText("Add Stock");
        jButtonAddStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddStockActionPerformed(evt);
            }
        });

        jButtonMakeOrder.setText("Make Order");
        jButtonMakeOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMakeOrderActionPerformed(evt);
            }
        });

        jButtonDeleteSelectedStock.setText("Delete Selected");
        jButtonDeleteSelectedStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteSelectedStockActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelEditStockLayout = new javax.swing.GroupLayout(jPanelEditStock);
        jPanelEditStock.setLayout(jPanelEditStockLayout);
        jPanelEditStockLayout.setHorizontalGroup(
            jPanelEditStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditStockLayout.createSequentialGroup()
                .addGroup(jPanelEditStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEditStockLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPaneIntegratedEditStock, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addGroup(jPanelEditStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonDeleteSelectedStock, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonMakeOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelEditStockLayout.createSequentialGroup()
                        .addGap(319, 319, 319)
                        .addComponent(jLabelEditItemsTitle1)))
                .addContainerGap(728, Short.MAX_VALUE))
        );
        jPanelEditStockLayout.setVerticalGroup(
            jPanelEditStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditStockLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelEditItemsTitle1)
                .addGap(1, 1, 1)
                .addGroup(jPanelEditStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEditStockLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jScrollPaneIntegratedEditStock, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelEditStockLayout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(jButtonAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeleteSelectedStock, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonMakeOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(916, Short.MAX_VALUE))
        );

        jScrollPaneEditStock.setViewportView(jPanelEditStock);

        jPanelMainDisplay.add(jScrollPaneEditStock, "card2");

        jLabelAccountManagementTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelAccountManagementTitle.setText("Account Management");

        jButtonStaff.setText("Staff");
        jButtonStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStaffActionPerformed(evt);
            }
        });

        jButton7.setText("Customer");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButtonBackToSettings.setText("Back");
        jButtonBackToSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToSettingsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelAccountManagementLayout = new javax.swing.GroupLayout(jPanelAccountManagement);
        jPanelAccountManagement.setLayout(jPanelAccountManagementLayout);
        jPanelAccountManagementLayout.setHorizontalGroup(
            jPanelAccountManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAccountManagementLayout.createSequentialGroup()
                .addComponent(jButtonBackToSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 784, Short.MAX_VALUE)
                .addComponent(jLabelAccountManagementTitle)
                .addGap(242, 242, 242))
            .addGroup(jPanelAccountManagementLayout.createSequentialGroup()
                .addGap(303, 303, 303)
                .addGroup(jPanelAccountManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelAccountManagementLayout.setVerticalGroup(
            jPanelAccountManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAccountManagementLayout.createSequentialGroup()
                .addGroup(jPanelAccountManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelAccountManagementLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelAccountManagementTitle))
                    .addComponent(jButtonBackToSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jButtonStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1140, Short.MAX_VALUE))
        );

        jScrollPaneAccountManagement.setViewportView(jPanelAccountManagement);

        jPanelMainDisplay.add(jScrollPaneAccountManagement, "card2");

        jLabelEditStaffAccount.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelEditStaffAccount.setText("Edit Staff Accounts");

        jButtonBackToAccountManagement.setText("Back");
        jButtonBackToAccountManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToAccountManagementActionPerformed(evt);
            }
        });

        jButton5.setText("Add Account");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButtonDeleteSelectedStaffAccount.setText("Delete Selected");
        jButtonDeleteSelectedStaffAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteSelectedStaffAccountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelEditStaffAccountLayout = new javax.swing.GroupLayout(jPanelEditStaffAccount);
        jPanelEditStaffAccount.setLayout(jPanelEditStaffAccountLayout);
        jPanelEditStaffAccountLayout.setHorizontalGroup(
            jPanelEditStaffAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditStaffAccountLayout.createSequentialGroup()
                .addGroup(jPanelEditStaffAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEditStaffAccountLayout.createSequentialGroup()
                        .addComponent(jButtonBackToAccountManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(189, 189, 189)
                        .addComponent(jLabelEditStaffAccount))
                    .addGroup(jPanelEditStaffAccountLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPaneIntegratedEditStaffAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addGroup(jPanelEditStaffAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonDeleteSelectedStaffAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelEditStaffAccountLayout.setVerticalGroup(
            jPanelEditStaffAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditStaffAccountLayout.createSequentialGroup()
                .addGroup(jPanelEditStaffAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonBackToAccountManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEditStaffAccount))
                .addGroup(jPanelEditStaffAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEditStaffAccountLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jScrollPaneIntegratedEditStaffAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelEditStaffAccountLayout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeleteSelectedStaffAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(916, Short.MAX_VALUE))
        );

        jScrollPaneEditStaffAccount.setViewportView(jPanelEditStaffAccount);

        jPanelMainDisplay.add(jScrollPaneEditStaffAccount, "card2");

        jLabelAddStaffTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelAddStaffTitle.setText("Add Staff");

        jLabelName4.setText("Username:");

        jLabelPreparationTime2.setText("Password:");

        jLabelPrice1.setText("Confirm Password:");

        jLabelDescription1.setText("First Name:");

        jButtonAddToDatabase1.setText("Add To Database");
        jButtonAddToDatabase1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddToDatabase1ActionPerformed(evt);
            }
        });

        jButtonBackToItems2.setText("Back");
        jButtonBackToItems2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToItems2ActionPerformed(evt);
            }
        });

        jCheckBoxKitchen.setText("Kitchen");
        jCheckBoxKitchen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxKitchenActionPerformed(evt);
            }
        });

        jCheckBoxWaiting.setText("Waiting");
        jCheckBoxWaiting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxWaitingActionPerformed(evt);
            }
        });

        jCheckBoxManager.setText("Manager");
        jCheckBoxManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxManagerActionPerformed(evt);
            }
        });

        jLabel14.setText("Fill in details and choose a staff type:");

        jLabelName5.setText("Surname:");

        javax.swing.GroupLayout jPanelAddStaffLayout = new javax.swing.GroupLayout(jPanelAddStaff);
        jPanelAddStaff.setLayout(jPanelAddStaffLayout);
        jPanelAddStaffLayout.setHorizontalGroup(
            jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                .addGroup(jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                        .addComponent(jButtonBackToItems2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(190, 190, 190)
                        .addComponent(jLabelAddStaffTitle))
                    .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addGroup(jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                                .addComponent(jLabelPreparationTime2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPasswordPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                                .addComponent(jLabelName4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                                .addGroup(jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelDescription1)
                                    .addComponent(jLabelPrice1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPasswordConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                                .addComponent(jLabelName5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(71, 71, 71)
                        .addGroup(jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxKitchen)
                            .addComponent(jCheckBoxWaiting)
                            .addComponent(jCheckBoxManager)))
                    .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                        .addGap(298, 298, 298)
                        .addComponent(jLabel14))
                    .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                        .addGap(311, 311, 311)
                        .addComponent(jButtonAddToDatabase1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(905, Short.MAX_VALUE))
        );
        jPanelAddStaffLayout.setVerticalGroup(
            jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                .addGroup(jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelAddStaffTitle))
                    .addComponent(jButtonBackToItems2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addGroup(jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelName4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                        .addGroup(jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jPasswordPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelPreparationTime2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelPrice1)
                            .addComponent(jPasswordConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelDescription1)
                            .addComponent(jTextFieldFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelAddStaffLayout.createSequentialGroup()
                        .addComponent(jCheckBoxKitchen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxWaiting)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxManager)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelAddStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelName5))
                .addGap(18, 18, 18)
                .addComponent(jButtonAddToDatabase1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1010, Short.MAX_VALUE))
        );

        jScrollPaneAddStaff.setViewportView(jPanelAddStaff);

        jPanelMainDisplay.add(jScrollPaneAddStaff, "card2");

        jLabelEditCustomerAccount.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelEditCustomerAccount.setText("Edit Customer Accounts");

        jButtonBackToAccountManagement1.setText("Back");
        jButtonBackToAccountManagement1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToAccountManagement1ActionPerformed(evt);
            }
        });

        jButtonDeleteSelectedCustomerAccount.setText("Delete Selected");
        jButtonDeleteSelectedCustomerAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteSelectedCustomerAccountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelEditCustomerAccountLayout = new javax.swing.GroupLayout(jPanelEditCustomerAccount);
        jPanelEditCustomerAccount.setLayout(jPanelEditCustomerAccountLayout);
        jPanelEditCustomerAccountLayout.setHorizontalGroup(
            jPanelEditCustomerAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditCustomerAccountLayout.createSequentialGroup()
                .addGroup(jPanelEditCustomerAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEditCustomerAccountLayout.createSequentialGroup()
                        .addComponent(jButtonBackToAccountManagement1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(172, 172, 172)
                        .addComponent(jLabelEditCustomerAccount))
                    .addGroup(jPanelEditCustomerAccountLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPaneIntegratedEditCustomerAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jButtonDeleteSelectedCustomerAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(718, Short.MAX_VALUE))
        );
        jPanelEditCustomerAccountLayout.setVerticalGroup(
            jPanelEditCustomerAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditCustomerAccountLayout.createSequentialGroup()
                .addGroup(jPanelEditCustomerAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonBackToAccountManagement1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEditCustomerAccount))
                .addGroup(jPanelEditCustomerAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEditCustomerAccountLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jScrollPaneIntegratedEditCustomerAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelEditCustomerAccountLayout.createSequentialGroup()
                        .addGap(167, 167, 167)
                        .addComponent(jButtonDeleteSelectedCustomerAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(916, Short.MAX_VALUE))
        );

        jScrollPaneEditCustomerAccount.setViewportView(jPanelEditCustomerAccount);

        jPanelMainDisplay.add(jScrollPaneEditCustomerAccount, "card2");

        jLabelCurrentPending1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelCurrentPending1.setText("Logged In Customers'");

        jButtonBackToHistory1.setText("Back");
        jButtonBackToHistory1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToHistory1ActionPerformed(evt);
            }
        });

        jButtonRefresh.setText("Refresh");
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });

        jLabelCurrentPending2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelCurrentPending2.setText("Pending Orders Report");

        jButtonSelectCustomer.setText("Select Customer");
        jButtonSelectCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectCustomerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCurrentPendingLayout = new javax.swing.GroupLayout(jPanelCurrentPending);
        jPanelCurrentPending.setLayout(jPanelCurrentPendingLayout);
        jPanelCurrentPendingLayout.setHorizontalGroup(
            jPanelCurrentPendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCurrentPendingLayout.createSequentialGroup()
                .addGroup(jPanelCurrentPendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCurrentPendingLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPaneIntegratedCurrentPending, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelCurrentPendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonSelectCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jButtonRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanelCurrentPendingLayout.createSequentialGroup()
                        .addComponent(jButtonBackToHistory1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addGroup(jPanelCurrentPendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCurrentPending2)
                            .addComponent(jLabelCurrentPending1))))
                .addContainerGap(703, Short.MAX_VALUE))
        );
        jPanelCurrentPendingLayout.setVerticalGroup(
            jPanelCurrentPendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCurrentPendingLayout.createSequentialGroup()
                .addGroup(jPanelCurrentPendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCurrentPendingLayout.createSequentialGroup()
                        .addComponent(jButtonBackToHistory1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(155, 155, 155)
                        .addComponent(jButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSelectCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCurrentPendingLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelCurrentPending1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCurrentPending2)
                        .addGap(23, 23, 23)
                        .addComponent(jScrollPaneIntegratedCurrentPending, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(916, Short.MAX_VALUE))
        );

        jScrollPaneCurrentPending.setViewportView(jPanelCurrentPending);

        jPanelMainDisplay.add(jScrollPaneCurrentPending, "card2");

        jLabelPendingOrders.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelPendingOrders.setText("Pending Orders For ");

        jButtonBackToHistory2.setText("Back");
        jButtonBackToHistory2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToHistory2ActionPerformed(evt);
            }
        });

        jButtonRefresh2.setText("Refresh");
        jButtonRefresh2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefresh2ActionPerformed(evt);
            }
        });

        jLabelCustomerNameID.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelCustomerNameID.setText("customer name (ID)");

        jButtonPrintPending.setText("Print Report");
        jButtonPrintPending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintPendingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPendingOrdersLayout = new javax.swing.GroupLayout(jPanelPendingOrders);
        jPanelPendingOrders.setLayout(jPanelPendingOrdersLayout);
        jPanelPendingOrdersLayout.setHorizontalGroup(
            jPanelPendingOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPendingOrdersLayout.createSequentialGroup()
                .addGroup(jPanelPendingOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPendingOrdersLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPaneIntegratedPendingOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelPendingOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonRefresh2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonPrintPending, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelPendingOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabelCustomerNameID)
                        .addGroup(jPanelPendingOrdersLayout.createSequentialGroup()
                            .addComponent(jButtonBackToHistory2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(78, 78, 78)
                            .addComponent(jLabelPendingOrders))))
                .addContainerGap(703, Short.MAX_VALUE))
        );
        jPanelPendingOrdersLayout.setVerticalGroup(
            jPanelPendingOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPendingOrdersLayout.createSequentialGroup()
                .addGroup(jPanelPendingOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelPendingOrders)
                    .addComponent(jButtonBackToHistory2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelPendingOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPendingOrdersLayout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(jButtonRefresh2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonPrintPending, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelPendingOrdersLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCustomerNameID)
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPaneIntegratedPendingOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(916, Short.MAX_VALUE))
        );

        jScrollPanePendingOrders.setViewportView(jPanelPendingOrders);

        jPanelMainDisplay.add(jScrollPanePendingOrders, "card2");

        jLabelCustomerSpending.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelCustomerSpending.setText("Customer Spending Report");

        jButtonBackToHistory3.setText("Back");
        jButtonBackToHistory3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToHistory3ActionPerformed(evt);
            }
        });

        jButtonGenerateCustomerSpending.setText("Generate Report");
        jButtonGenerateCustomerSpending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateCustomerSpendingActionPerformed(evt);
            }
        });

        jLabel15.setText("Enter two dates to generate report:");

        jLabel16.setText("Date 1 (YYYY-MM-DD):");

        jLabel17.setText("Date 2 (YYYY-MM-DD):");

        jButtonPrintCustomerSpendingReport.setText("Print Report");
        jButtonPrintCustomerSpendingReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintCustomerSpendingReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCustomerSpendingLayout = new javax.swing.GroupLayout(jPanelCustomerSpending);
        jPanelCustomerSpending.setLayout(jPanelCustomerSpendingLayout);
        jPanelCustomerSpendingLayout.setHorizontalGroup(
            jPanelCustomerSpendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCustomerSpendingLayout.createSequentialGroup()
                .addGroup(jPanelCustomerSpendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCustomerSpendingLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPaneIntegratedCustomerSpending, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelCustomerSpendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jTextFieldDate2)
                            .addComponent(jTextFieldDate1)
                            .addComponent(jButtonGenerateCustomerSpending, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonPrintCustomerSpendingReport, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)))
                    .addGroup(jPanelCustomerSpendingLayout.createSequentialGroup()
                        .addComponent(jButtonBackToHistory3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addGroup(jPanelCustomerSpendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabelCustomerSpending))))
                .addContainerGap(738, Short.MAX_VALUE))
        );
        jPanelCustomerSpendingLayout.setVerticalGroup(
            jPanelCustomerSpendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCustomerSpendingLayout.createSequentialGroup()
                .addGroup(jPanelCustomerSpendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonBackToHistory3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelCustomerSpendingLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelCustomerSpending)
                        .addGap(38, 38, 38)
                        .addComponent(jLabel15)
                        .addGroup(jPanelCustomerSpendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCustomerSpendingLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPaneIntegratedCustomerSpending, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelCustomerSpendingLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel16)
                                .addGap(5, 5, 5)
                                .addComponent(jTextFieldDate1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel17)
                                .addGap(5, 5, 5)
                                .addComponent(jTextFieldDate2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)
                                .addComponent(jButtonGenerateCustomerSpending, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonPrintCustomerSpendingReport, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(916, Short.MAX_VALUE))
        );

        jScrollPaneCustomerSpending.setViewportView(jPanelCustomerSpending);

        jPanelMainDisplay.add(jScrollPaneCustomerSpending, "card2");

        jLabelRefundReport.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelRefundReport.setText("Refund Report");

        jButtonBackToHistory4.setText("Back");
        jButtonBackToHistory4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToHistory4ActionPerformed(evt);
            }
        });

        jButtonGenerateRefundReport.setText("Generate Report");
        jButtonGenerateRefundReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateRefundReportActionPerformed(evt);
            }
        });

        jLabel18.setText("Enter two dates to generate report:");

        jLabel19.setText("Date 1 (DD-MM-YYYY)");

        jLabel20.setText("Date 2 (DD-MM-YYYY)");

        jButtonPrintRefundReport.setText("Print Report");
        jButtonPrintRefundReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintRefundReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelRefundReportLayout = new javax.swing.GroupLayout(jPanelRefundReport);
        jPanelRefundReport.setLayout(jPanelRefundReportLayout);
        jPanelRefundReportLayout.setHorizontalGroup(
            jPanelRefundReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRefundReportLayout.createSequentialGroup()
                .addGroup(jPanelRefundReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRefundReportLayout.createSequentialGroup()
                        .addComponent(jButtonBackToHistory4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(180, 180, 180)
                        .addComponent(jLabelRefundReport))
                    .addGroup(jPanelRefundReportLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanelRefundReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPaneIntegratedRefundReport, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelRefundReportLayout.createSequentialGroup()
                                .addGroup(jPanelRefundReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelRefundReportLayout.createSequentialGroup()
                                        .addGroup(jPanelRefundReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel19)
                                            .addComponent(jTextFieldDate3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(145, 145, 145)
                                        .addGroup(jPanelRefundReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel20)
                                            .addComponent(jTextFieldDate4, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(116, 116, 116))
                                    .addGroup(jPanelRefundReportLayout.createSequentialGroup()
                                        .addGap(219, 219, 219)
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanelRefundReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButtonGenerateRefundReport, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonPrintRefundReport, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(455, Short.MAX_VALUE))
        );
        jPanelRefundReportLayout.setVerticalGroup(
            jPanelRefundReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRefundReportLayout.createSequentialGroup()
                .addGroup(jPanelRefundReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelRefundReport)
                    .addComponent(jButtonBackToHistory4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanelRefundReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRefundReportLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addGroup(jPanelRefundReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelRefundReportLayout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(5, 5, 5)
                                .addComponent(jTextFieldDate3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelRefundReportLayout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addGap(5, 5, 5)
                                .addComponent(jTextFieldDate4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(33, 33, 33)
                        .addComponent(jScrollPaneIntegratedRefundReport, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelRefundReportLayout.createSequentialGroup()
                        .addComponent(jButtonGenerateRefundReport, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonPrintRefundReport, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(832, Short.MAX_VALUE))
        );

        jScrollPaneRefundReport.setViewportView(jPanelRefundReport);

        jPanelMainDisplay.add(jScrollPaneRefundReport, "card2");

        jLabelOrdersReport.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelOrdersReport.setText("Orders Report");

        jButtonBackToHistory5.setText("Back");
        jButtonBackToHistory5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToHistory5ActionPerformed(evt);
            }
        });

        jButtonGenerateOrdersReport.setText("Generate Report");
        jButtonGenerateOrdersReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateOrdersReportActionPerformed(evt);
            }
        });

        jLabel21.setText("Enter two dates to generate report:");

        jLabel22.setText("Date 1 (DD-MM-YYYY)");

        jLabel23.setText("Date 2 (DD-MM-YYYY)");

        jButtonPrintOrdersReport.setText("Print Report");
        jButtonPrintOrdersReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintOrdersReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelOrdersReportLayout = new javax.swing.GroupLayout(jPanelOrdersReport);
        jPanelOrdersReport.setLayout(jPanelOrdersReportLayout);
        jPanelOrdersReportLayout.setHorizontalGroup(
            jPanelOrdersReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOrdersReportLayout.createSequentialGroup()
                .addGroup(jPanelOrdersReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelOrdersReportLayout.createSequentialGroup()
                        .addComponent(jButtonBackToHistory5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(180, 180, 180)
                        .addComponent(jLabelOrdersReport))
                    .addGroup(jPanelOrdersReportLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanelOrdersReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPaneIntegratedOrdersReport, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelOrdersReportLayout.createSequentialGroup()
                                .addGroup(jPanelOrdersReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOrdersReportLayout.createSequentialGroup()
                                        .addGroup(jPanelOrdersReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel22)
                                            .addComponent(jTextFieldDate5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(145, 145, 145)
                                        .addGroup(jPanelOrdersReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel23)
                                            .addComponent(jTextFieldDate6, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(116, 116, 116))
                                    .addGroup(jPanelOrdersReportLayout.createSequentialGroup()
                                        .addGap(219, 219, 219)
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanelOrdersReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButtonGenerateOrdersReport, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonPrintOrdersReport, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(455, Short.MAX_VALUE))
        );
        jPanelOrdersReportLayout.setVerticalGroup(
            jPanelOrdersReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOrdersReportLayout.createSequentialGroup()
                .addGroup(jPanelOrdersReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelOrdersReport)
                    .addComponent(jButtonBackToHistory5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanelOrdersReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelOrdersReportLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(18, 18, 18)
                        .addGroup(jPanelOrdersReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelOrdersReportLayout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addGap(5, 5, 5)
                                .addComponent(jTextFieldDate5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelOrdersReportLayout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addGap(5, 5, 5)
                                .addComponent(jTextFieldDate6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(33, 33, 33)
                        .addComponent(jScrollPaneIntegratedOrdersReport, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelOrdersReportLayout.createSequentialGroup()
                        .addComponent(jButtonGenerateOrdersReport, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonPrintOrdersReport, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(832, Short.MAX_VALUE))
        );

        jScrollPaneOrdersReport.setViewportView(jPanelOrdersReport);

        jPanelMainDisplay.add(jScrollPaneOrdersReport, "card2");

        jLabelAddItemStock.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelAddItemStock.setText("Add Stock To Item");

        jButtonBack3.setText("Back");
        jButtonBack3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBack3ActionPerformed(evt);
            }
        });

        jButtonAddStockItem2.setText("Add Stock(s) To Item");
        jButtonAddStockItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddStockItem2ActionPerformed(evt);
            }
        });

        jLabelEditingFor.setText("Editing for: ");

        javax.swing.GroupLayout jPanelAddItemStockLayout = new javax.swing.GroupLayout(jPanelAddItemStock);
        jPanelAddItemStock.setLayout(jPanelAddItemStockLayout);
        jPanelAddItemStockLayout.setHorizontalGroup(
            jPanelAddItemStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddItemStockLayout.createSequentialGroup()
                .addGroup(jPanelAddItemStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAddItemStockLayout.createSequentialGroup()
                        .addComponent(jButtonBack3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(197, 197, 197)
                        .addComponent(jLabelAddItemStock))
                    .addGroup(jPanelAddItemStockLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPaneIntegratedAddItemStock, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelAddItemStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelEditingFor)
                            .addComponent(jButtonAddStockItem2, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(712, Short.MAX_VALUE))
        );
        jPanelAddItemStockLayout.setVerticalGroup(
            jPanelAddItemStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddItemStockLayout.createSequentialGroup()
                .addGroup(jPanelAddItemStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonBack3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAddItemStock))
                .addGroup(jPanelAddItemStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAddItemStockLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jScrollPaneIntegratedAddItemStock, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelAddItemStockLayout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(jLabelEditingFor)
                        .addGap(63, 63, 63)
                        .addComponent(jButtonAddStockItem2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(916, Short.MAX_VALUE))
        );

        jScrollPaneAddItemStock.setViewportView(jPanelAddItemStock);

        jPanelMainDisplay.add(jScrollPaneAddItemStock, "card2");

        jLabelStaffSelection.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelStaffSelection.setText("Staff History Selection");

        jButtonBackToHistory6.setText("Back");
        jButtonBackToHistory6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToHistory6ActionPerformed(evt);
            }
        });

        jButtonRefresh1.setText("Refresh");
        jButtonRefresh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefresh1ActionPerformed(evt);
            }
        });

        jButtonSelectStaff.setText("Select Staff");
        jButtonSelectStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectStaffActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelStaffSelectionLayout = new javax.swing.GroupLayout(jPanelStaffSelection);
        jPanelStaffSelection.setLayout(jPanelStaffSelectionLayout);
        jPanelStaffSelectionLayout.setHorizontalGroup(
            jPanelStaffSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStaffSelectionLayout.createSequentialGroup()
                .addGroup(jPanelStaffSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelStaffSelectionLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPaneIntegratedStaffSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelStaffSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonSelectStaff, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jButtonRefresh1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanelStaffSelectionLayout.createSequentialGroup()
                        .addComponent(jButtonBackToHistory6, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(jLabelStaffSelection)))
                .addContainerGap(703, Short.MAX_VALUE))
        );
        jPanelStaffSelectionLayout.setVerticalGroup(
            jPanelStaffSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStaffSelectionLayout.createSequentialGroup()
                .addGroup(jPanelStaffSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelStaffSelectionLayout.createSequentialGroup()
                        .addComponent(jButtonBackToHistory6, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(155, 155, 155)
                        .addComponent(jButtonRefresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSelectStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelStaffSelectionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelStaffSelection)
                        .addGap(58, 58, 58)
                        .addComponent(jScrollPaneIntegratedStaffSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(916, Short.MAX_VALUE))
        );

        jScrollPaneStaffSelection.setViewportView(jPanelStaffSelection);

        jPanelMainDisplay.add(jScrollPaneStaffSelection, "card2");

        jLabelPendingOrders1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelPendingOrders1.setText("Staff Performance ");

        jButtonBackToHistory7.setText("Back");
        jButtonBackToHistory7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToHistory7ActionPerformed(evt);
            }
        });

        jButtonRefresh3.setText("Generate Report");
        jButtonRefresh3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefresh3ActionPerformed(evt);
            }
        });

        jButtonPrintStaffPerformanceReport.setText("Print Report");
        jButtonPrintStaffPerformanceReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintStaffPerformanceReportActionPerformed(evt);
            }
        });

        jLabelViewingFor.setText("Viewing for:");

        jLabel25.setText("Date 1: (DD-MM-YYYY)");

        jLabel26.setText("Date 2: (DD-MM-YYYY)");

        javax.swing.GroupLayout jPanelStaffPerformanceLayout = new javax.swing.GroupLayout(jPanelStaffPerformance);
        jPanelStaffPerformance.setLayout(jPanelStaffPerformanceLayout);
        jPanelStaffPerformanceLayout.setHorizontalGroup(
            jPanelStaffPerformanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStaffPerformanceLayout.createSequentialGroup()
                .addGroup(jPanelStaffPerformanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelStaffPerformanceLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPaneIntegratedStaffPerformance, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelStaffPerformanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonRefresh3, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jButtonPrintStaffPerformanceReport, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jLabelViewingFor)
                            .addComponent(jTextFieldDate7)
                            .addComponent(jTextFieldDate8)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26)))
                    .addGroup(jPanelStaffPerformanceLayout.createSequentialGroup()
                        .addComponent(jButtonBackToHistory7, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78)
                        .addComponent(jLabelPendingOrders1)))
                .addContainerGap(703, Short.MAX_VALUE))
        );
        jPanelStaffPerformanceLayout.setVerticalGroup(
            jPanelStaffPerformanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStaffPerformanceLayout.createSequentialGroup()
                .addGroup(jPanelStaffPerformanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanelStaffPerformanceLayout.createSequentialGroup()
                        .addGroup(jPanelStaffPerformanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelPendingOrders1)
                            .addComponent(jButtonBackToHistory7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(57, 57, 57)
                        .addComponent(jScrollPaneIntegratedStaffPerformance, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelStaffPerformanceLayout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jLabelViewingFor)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldDate7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldDate8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addComponent(jButtonRefresh3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonPrintStaffPerformanceReport, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(916, Short.MAX_VALUE))
        );

        jScrollPaneStaffPerformance.setViewportView(jPanelStaffPerformance);

        jPanelMainDisplay.add(jScrollPaneStaffPerformance, "card2");

        javax.swing.GroupLayout jPanelMainFrameLayout = new javax.swing.GroupLayout(jPanelMainFrame);
        jPanelMainFrame.setLayout(jPanelMainFrameLayout);
        jPanelMainFrameLayout.setHorizontalGroup(
            jPanelMainFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMainFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelInformationBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelMainFrameLayout.createSequentialGroup()
                        .addGroup(jPanelMainFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanelSideBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelChickCafeLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelMainFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelUpperBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelMainDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanelMainFrameLayout.setVerticalGroup(
            jPanelMainFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMainFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelUpperBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelChickCafeLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelMainFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelSideBar, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelMainDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelInformationBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelMainFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 904, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMainFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Logs out the user.
     */
    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed
        try {
            system.CoDStart();
            if(staff == null){
                con.changeUser("Is_Logged_In", customer.getUsername(), "0");
            }
            if(customer == null){
               con.changeUser("Is_Logged_In", staff.getUsername(), "0");
            }
        } catch (Exception ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        logoutPressed = true;
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    /**
     * Sets the color textures for the buttons, labels and panels.
     */
    public void setColours(){
        jButtonMenu.setBackground(buttonColor);
        jButtonHistory.setBackground(buttonColor);
        jButtonStock.setBackground(buttonColor);
        jButtonQueue.setBackground(buttonColor);
        jButtonSettings.setBackground(buttonColor);
        jButtonHome.setBackground(buttonColor);
        jButtonBasket.setBackground(buttonColor);
        jButtonLogout.setBackground(buttonColor);
        jPanelUpperBar.setBackground(panelColor);
        jPanelSideBar.setBackground(panelColor);
        jPanelInformationBar.setBackground(panelColor);
        jPanelMainFrame.setBackground(mainBackground);
        
        jButtonMenu.setForeground(buttonTextColor);
        jButtonHistory.setForeground(buttonTextColor);
        jButtonStock.setForeground(buttonTextColor);
        jButtonQueue.setForeground(buttonTextColor);
        jButtonSettings.setForeground(buttonTextColor);
        jButtonHome.setForeground(buttonTextColor);
        jButtonBasket.setForeground(buttonTextColor);
        jButtonLogout.setForeground(buttonTextColor);
        
        jButtonMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonHistory.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonStock.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonQueue.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonSettings.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonHome.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBasket.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonLogout.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelUpperBar.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelSideBar.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelInformationBar.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelMainDisplay.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelMainFrame.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        
        jLabelFenixSystems.setForeground(Color.BLACK);
        jLabelLoggedInAs.setForeground(Color.BLACK);
        jLabelDate.setForeground(Color.BLACK);
        jLabelTime.setForeground(Color.BLACK);
        
        jScrollPane1.setBackground(panelColor);
        jScrollPane2.setBackground(panelColor);
        jScrollPane3.setBackground(panelColor);
        jScrollPaneAccountManagement.setBackground(panelColor);
        jScrollPaneAddItem.setBackground(panelColor);
        jScrollPaneAddItemStock.setBackground(panelColor);
        jScrollPaneAddStaff.setBackground(panelColor);
        jScrollPaneAddStock.setBackground(panelColor);
        jScrollPaneBankTransfer.setBackground(panelColor);
        jScrollPaneBasket.setBackground(panelColor);
        jScrollPaneBreakfastMenu.setBackground(panelColor);
        jScrollPaneBreakfastQueue.setBackground(panelColor);
        jScrollPaneCard.setBackground(panelColor);
        jScrollPaneCurrentPending.setBackground(panelColor);
        jScrollPaneCustomerSpending.setBackground(panelColor);
        jScrollPaneDinnerMenu.setBackground(panelColor);
        jScrollPaneDinnerQueue.setBackground(panelColor);
        jScrollPaneDrinksMenu.setBackground(panelColor);
        jScrollPaneEditCustomerAccount.setBackground(panelColor);
        jScrollPaneEditItems.setBackground(panelColor);
        jScrollPaneEditStaffAccount.setBackground(panelColor);
        jScrollPaneEditStock.setBackground(panelColor);
        jScrollPaneHistory.setBackground(panelColor);
        jScrollPaneHome.setBackground(panelColor);
        jScrollPaneIntegratedAddItemStock.setBackground(panelColor);
        jScrollPaneIntegratedCurrentPending.setBackground(panelColor);
        jScrollPaneIntegratedCustomerSpending.setBackground(panelColor);
        jScrollPaneIntegratedEditCustomerAccount.setBackground(panelColor);
        jScrollPaneIntegratedEditItems.setBackground(panelColor);
        jScrollPaneIntegratedEditStaffAccount.setBackground(panelColor);
        jScrollPaneIntegratedEditStock.setBackground(panelColor);
        jScrollPaneIntegratedOrdersReport.setBackground(panelColor);
        jScrollPaneIntegratedPendingOrders.setBackground(panelColor);
        jScrollPaneIntegratedRefundReport.setBackground(panelColor);
        jScrollPaneIntegratedStaffPerformance.setBackground(panelColor);
        jScrollPaneIntegratedStaffSelection.setBackground(panelColor);
        jScrollPaneLunchMenu.setBackground(panelColor);
        jScrollPaneLunchQueue.setBackground(panelColor);
        jScrollPaneMenu.setBackground(panelColor);
        jScrollPaneOrdersReport.setBackground(panelColor);
        jScrollPanePayment.setBackground(panelColor);
        jScrollPanePendingOrders.setBackground(panelColor);
        jScrollPaneQueue.setBackground(panelColor);
        jScrollPaneRefundReport.setBackground(panelColor);
        jScrollPaneSettings.setBackground(panelColor);
        jScrollPaneStaffPerformance.setBackground(panelColor);
        jScrollPaneStaffSelection.setBackground(panelColor);
        jScrollPaneUpdateStockQuantity.setBackground(panelColor);
        jScrollPaneViewOrder.setBackground(panelColor);
        
        jScrollPane1.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPane2.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPane3.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneAccountManagement.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneAddItem.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneAddItemStock.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneAddStaff.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneAddStock.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneBankTransfer.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneBasket.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneBreakfastMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneBreakfastQueue.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneCard.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneCurrentPending.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneCustomerSpending.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneDinnerMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneDinnerQueue.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneDrinksMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneEditCustomerAccount.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneEditItems.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneEditStaffAccount.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneEditStock.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneHistory.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneHome.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneIntegratedAddItemStock.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneIntegratedCurrentPending.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneIntegratedCustomerSpending.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneIntegratedEditCustomerAccount.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneIntegratedEditItems.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneIntegratedEditStaffAccount.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneIntegratedEditStock.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneIntegratedOrdersReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneIntegratedPendingOrders.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneIntegratedRefundReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneIntegratedStaffPerformance.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneIntegratedStaffSelection.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneLunchMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneLunchQueue.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneOrdersReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPanePayment.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPanePendingOrders.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneQueue.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneRefundReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneSettings.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneStaffPerformance.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneStaffSelection.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneUpdateStockQuantity.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jScrollPaneViewOrder.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        
        jPanel1.setBackground(panelColor);
        jPanel2.setBackground(panelColor);
        jPanel3.setBackground(panelColor);
        jPanel4.setBackground(panelColor);
        jPanelAccountManagement.setBackground(panelColor);
        jPanelAddItem.setBackground(panelColor);
        jPanelAddItemStock.setBackground(panelColor);
        jPanelAddStaff.setBackground(panelColor);
        jPanelAddStock.setBackground(panelColor);
        jPanelBankTransfer.setBackground(panelColor);
        jPanelBasket.setBackground(panelColor);
        jPanelBreakfastMenu.setBackground(panelColor);
        jPanelBreakfastQueue.setBackground(panelColor);
        jPanelCard.setBackground(panelColor);
        jPanelCurrentPending.setBackground(panelColor);
        jPanelCustomerSpending.setBackground(panelColor);
        jPanelDeleteStock1.setBackground(panelColor);
        jPanelDinnerMenu.setBackground(panelColor);
        jPanelDinnerQueue.setBackground(panelColor);
        jPanelDrinksMenu.setBackground(panelColor);
        jPanelEditCustomerAccount.setBackground(panelColor);
        jPanelEditItems.setBackground(panelColor);
        jPanelEditStaffAccount.setBackground(panelColor);
        jPanelEditStock.setBackground(panelColor);
        jPanelHistory.setBackground(panelColor);
        jPanelHome.setBackground(panelColor);
        jPanelInformationBar.setBackground(panelColor);
        jPanelIntegratedMenu.setBackground(panelColor);
        jPanelIntegratedPayment.setBackground(panelColor);
        jPanelIntegratedQueue.setBackground(panelColor);
        jPanelLunchMenu.setBackground(panelColor);
        jPanelLunchQueue.setBackground(panelColor);
        jPanelMainDisplay.setBackground(panelColor);
        jPanelMenu.setBackground(panelColor);
        jPanelOrdersReport.setBackground(panelColor);
        jPanelPayment.setBackground(panelColor);
        jPanelPendingOrders.setBackground(panelColor);
        jPanelQueue.setBackground(panelColor);
        jPanelQueueSelection.setBackground(panelColor);
        jPanelRefundReport.setBackground(panelColor);
        jPanelSettings.setBackground(panelColor);
        jPanelSideBar.setBackground(panelColor);
        jPanelStaffPerformance.setBackground(panelColor);
        jPanelStaffSelection.setBackground(panelColor);
        jPanelUpperBar.setBackground(panelColor);
        jPanelViewOrder.setBackground(panelColor);
        
        jPanel1.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanel2.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanel3.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanel4.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelAccountManagement.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelAddItem.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelAddItemStock.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelAddStaff.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelAddStock.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelBankTransfer.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelBasket.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelBreakfastMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelBreakfastQueue.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelCard.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelCurrentPending.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelCustomerSpending.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelDeleteStock1.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelDinnerMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelDinnerQueue.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelDrinksMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelEditCustomerAccount.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelEditItems.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelEditStaffAccount.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelEditStock.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelHistory.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelHome.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelInformationBar.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelIntegratedMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelIntegratedPayment.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelIntegratedQueue.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelLunchMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelLunchQueue.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelMainDisplay.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelMainFrame.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelOrdersReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelPayment.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelPendingOrders.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelQueue.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelQueueSelection.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelRefundReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelSettings.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelSideBar.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelStaffPerformance.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelStaffSelection.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelUpperBar.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jPanelViewOrder.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        
        jButton1.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButton10.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButton2.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButton3.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButton4.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButton5.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButton6.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButton7.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButton8.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButton9.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonAccountManagement.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonAddNewCard.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonAddStock.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonAddStockItem2.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonAddStockToDatabase.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonAddStockToItem.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonAddToDatabase.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonAddToDatabase1.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBack2.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBack3.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToAccountManagement.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToAccountManagement1.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToBasket.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToHistory1.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToHistory2.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToHistory3.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToHistory4.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToHistory5.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToHistory6.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToHistory7.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToItems1.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToItems2.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToQueue.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToSettings.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToStock1.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackToStock3.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBackUpSystem.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBankTransfer.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBasket.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonBreakfast.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonCheckout.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonClearBasket.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonConfirmPayment.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonConfirmPayment1.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonDeleteSelectedCustomerAccount.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonDeleteSelectedItem.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonDeleteSelectedStaffAccount.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonDeleteSelectedStock.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonDinner.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonDrinks.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonFlagOrder.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonGenerateCustomerSpending.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonGenerateOrdersReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonGenerateRefundReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonHistory.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonHome.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonItems.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonLogout.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonLunch.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonMakeOrder.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonMenu.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonPrintCustomerSpendingReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonPrintOrdersReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonPrintPending.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonPrintRefundReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonPrintStaffPerformanceReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonProceedToPayment.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonQueue.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonRefresh.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonRefresh1.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonRefresh2.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonRefresh3.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonRestoreSystem.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonSelectCustomer.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonSelectStaff.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonSendNotification.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonSettings.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonStaff.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonStaffReport.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonStock.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonUpdateStockInDatabase.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        jButtonUseExistingCard.setBorder(BorderFactory.createLineBorder(buttonBorderColor));
        
        jButton1.setBackground(buttonColor);
        jButton10.setBackground(buttonColor);
        jButton2.setBackground(buttonColor);
        jButton3.setBackground(buttonColor);
        jButton4.setBackground(buttonColor);
        jButton5.setBackground(buttonColor);
        jButton6.setBackground(buttonColor);
        jButton7.setBackground(buttonColor);
        jButton8.setBackground(buttonColor);
        jButton9.setBackground(buttonColor);
        jButtonAccountManagement.setBackground(buttonColor);
        jButtonAddNewCard.setBackground(buttonColor);
        jButtonAddStock.setBackground(buttonColor);
        jButtonAddStockItem2.setBackground(buttonColor);
        jButtonAddStockToDatabase.setBackground(buttonColor);
        jButtonAddStockToItem.setBackground(buttonColor);
        jButtonAddToDatabase.setBackground(buttonColor);
        jButtonAddToDatabase1.setBackground(buttonColor);
        jButtonBack2.setBackground(buttonColor);
        jButtonBack3.setBackground(buttonColor);
        jButtonBackToAccountManagement.setBackground(buttonColor);
        jButtonBackToAccountManagement1.setBackground(buttonColor);
        jButtonBackToBasket.setBackground(buttonColor);
        jButtonBackToHistory1.setBackground(buttonColor);
        jButtonBackToHistory2.setBackground(buttonColor);
        jButtonBackToHistory3.setBackground(buttonColor);
        jButtonBackToHistory4.setBackground(buttonColor);
        jButtonBackToHistory5.setBackground(buttonColor);
        jButtonBackToHistory6.setBackground(buttonColor);
        jButtonBackToHistory7.setBackground(buttonColor);
        jButtonBackToItems1.setBackground(buttonColor);
        jButtonBackToItems2.setBackground(buttonColor);
        jButtonBackToQueue.setBackground(buttonColor);
        jButtonBackToSettings.setBackground(buttonColor);
        jButtonBackToStock1.setBackground(buttonColor);
        jButtonBackToStock3.setBackground(buttonColor);
        jButtonBackUpSystem.setBackground(buttonColor);
        jButtonBankTransfer.setBackground(buttonColor);
        jButtonBasket.setBackground(buttonColor);
        jButtonBreakfast.setBackground(buttonColor);
        jButtonCheckout.setBackground(buttonColor);
        jButtonClearBasket.setBackground(buttonColor);
        jButtonConfirmPayment.setBackground(buttonColor);
        jButtonConfirmPayment1.setBackground(buttonColor);
        jButtonDeleteSelectedCustomerAccount.setBackground(buttonColor);
        jButtonDeleteSelectedItem.setBackground(buttonColor);
        jButtonDeleteSelectedStaffAccount.setBackground(buttonColor);
        jButtonDeleteSelectedStock.setBackground(buttonColor);
        jButtonDinner.setBackground(buttonColor);
        jButtonDrinks.setBackground(buttonColor);
        jButtonFlagOrder.setBackground(buttonColor);
        jButtonGenerateCustomerSpending.setBackground(buttonColor);
        jButtonGenerateOrdersReport.setBackground(buttonColor);
        jButtonGenerateRefundReport.setBackground(buttonColor);
        jButtonHistory.setBackground(buttonColor);
        jButtonHome.setBackground(buttonColor);
        jButtonItems.setBackground(buttonColor);
        jButtonLogout.setBackground(buttonColor);
        jButtonLunch.setBackground(buttonColor);
        jButtonMakeOrder.setBackground(buttonColor);
        jButtonMenu.setBackground(buttonColor);
        jButtonPrintCustomerSpendingReport.setBackground(buttonColor);
        jButtonPrintOrdersReport.setBackground(buttonColor);
        jButtonPrintPending.setBackground(buttonColor);
        jButtonPrintRefundReport.setBackground(buttonColor);
        jButtonPrintStaffPerformanceReport.setBackground(buttonColor);
        jButtonProceedToPayment.setBackground(buttonColor);
        jButtonQueue.setBackground(buttonColor);
        jButtonRefresh.setBackground(buttonColor);
        jButtonRefresh1.setBackground(buttonColor);
        jButtonRefresh2.setBackground(buttonColor);
        jButtonRefresh3.setBackground(buttonColor);
        jButtonRestoreSystem.setBackground(buttonColor);
        jButtonSelectCustomer.setBackground(buttonColor);
        jButtonSelectStaff.setBackground(buttonColor);
        jButtonSendNotification.setBackground(buttonColor);
        jButtonSettings.setBackground(buttonColor);
        jButtonStaff.setBackground(buttonColor);
        jButtonStaffReport.setBackground(buttonColor);
        jButtonStock.setBackground(buttonColor);
        jButtonUpdateStockInDatabase.setBackground(buttonColor);
        jButtonUseExistingCard.setBackground(buttonColor);
        
        jButton1.setForeground(buttonTextColor);
        jButton10.setForeground(buttonTextColor);
        jButton2.setForeground(buttonTextColor);
        jButton3.setForeground(buttonTextColor);
        jButton4.setForeground(buttonTextColor);
        jButton5.setForeground(buttonTextColor);
        jButton6.setForeground(buttonTextColor);
        jButton7.setForeground(buttonTextColor);
        jButton8.setForeground(buttonTextColor);
        jButton9.setForeground(buttonTextColor);
        jButtonAccountManagement.setForeground(buttonTextColor);
        jButtonAddNewCard.setForeground(buttonTextColor);
        jButtonAddStock.setForeground(buttonTextColor);
        jButtonAddStockItem2.setForeground(buttonTextColor);
        jButtonAddStockToDatabase.setForeground(buttonTextColor);
        jButtonAddStockToItem.setForeground(buttonTextColor);
        jButtonAddToDatabase.setForeground(buttonTextColor);
        jButtonAddToDatabase1.setForeground(buttonTextColor);
        jButtonBack2.setForeground(buttonTextColor);
        jButtonBack3.setForeground(buttonTextColor);
        jButtonBackToAccountManagement.setForeground(buttonTextColor);
        jButtonBackToAccountManagement1.setForeground(buttonTextColor);
        jButtonBackToBasket.setForeground(buttonTextColor);
        jButtonBackToHistory1.setForeground(buttonTextColor);
        jButtonBackToHistory2.setForeground(buttonTextColor);
        jButtonBackToHistory3.setForeground(buttonTextColor);
        jButtonBackToHistory4.setForeground(buttonTextColor);
        jButtonBackToHistory5.setForeground(buttonTextColor);
        jButtonBackToHistory6.setForeground(buttonTextColor);
        jButtonBackToHistory7.setForeground(buttonTextColor);
        jButtonBackToItems1.setForeground(buttonTextColor);
        jButtonBackToItems2.setForeground(buttonTextColor);
        jButtonBackToQueue.setForeground(buttonTextColor);
        jButtonBackToSettings.setForeground(buttonTextColor);
        jButtonBackToStock1.setForeground(buttonTextColor);
        jButtonBackToStock3.setForeground(buttonTextColor);
        jButtonBackUpSystem.setForeground(buttonTextColor);
        jButtonBankTransfer.setForeground(buttonTextColor);
        jButtonBasket.setForeground(buttonTextColor);
        jButtonBreakfast.setForeground(buttonTextColor);
        jButtonCheckout.setForeground(buttonTextColor);
        jButtonClearBasket.setForeground(buttonTextColor);
        jButtonConfirmPayment.setForeground(buttonTextColor);
        jButtonConfirmPayment1.setForeground(buttonTextColor);
        jButtonDeleteSelectedCustomerAccount.setForeground(buttonTextColor);
        jButtonDeleteSelectedItem.setForeground(buttonTextColor);
        jButtonDeleteSelectedStaffAccount.setForeground(buttonTextColor);
        jButtonDeleteSelectedStock.setForeground(buttonTextColor);
        jButtonDinner.setForeground(buttonTextColor);
        jButtonDrinks.setForeground(buttonTextColor);
        jButtonFlagOrder.setForeground(buttonTextColor);
        jButtonGenerateCustomerSpending.setForeground(buttonTextColor);
        jButtonGenerateOrdersReport.setForeground(buttonTextColor);
        jButtonGenerateRefundReport.setForeground(buttonTextColor);
        jButtonHistory.setForeground(buttonTextColor);
        jButtonHome.setForeground(buttonTextColor);
        jButtonItems.setForeground(buttonTextColor);
        jButtonLogout.setForeground(buttonTextColor);
        jButtonLunch.setForeground(buttonTextColor);
        jButtonMakeOrder.setForeground(buttonTextColor);
        jButtonMenu.setForeground(buttonTextColor);
        jButtonPrintCustomerSpendingReport.setForeground(buttonTextColor);
        jButtonPrintOrdersReport.setForeground(buttonTextColor);
        jButtonPrintPending.setForeground(buttonTextColor);
        jButtonPrintRefundReport.setForeground(buttonTextColor);
        jButtonPrintStaffPerformanceReport.setForeground(buttonTextColor);
        jButtonProceedToPayment.setForeground(buttonTextColor);
        jButtonQueue.setForeground(buttonTextColor);
        jButtonRefresh.setForeground(buttonTextColor);
        jButtonRefresh1.setForeground(buttonTextColor);
        jButtonRefresh2.setForeground(buttonTextColor);
        jButtonRefresh3.setForeground(buttonTextColor);
        jButtonRestoreSystem.setForeground(buttonTextColor);
        jButtonSelectCustomer.setForeground(buttonTextColor);
        jButtonSelectStaff.setForeground(buttonTextColor);
        jButtonSendNotification.setForeground(buttonTextColor);
        jButtonSettings.setForeground(buttonTextColor);
        jButtonStaff.setForeground(buttonTextColor);
        jButtonStaffReport.setForeground(buttonTextColor);
        jButtonStock.setForeground(buttonTextColor);
        jButtonUpdateStockInDatabase.setForeground(buttonTextColor);
        jButtonUseExistingCard.setForeground(buttonTextColor);
    }
    
    /**
     * Updates the menu and hides certain menus depending on the time.
     */
    public void menuEnabler() throws ParseException{
        String closingTime = "22:00:00";
        Date currentTime = dF2.parse(dF2.format(Calendar.getInstance().getTime()));
        Date breakfastDate = dF2.parse(breakfastStartTime);
        Date lunchDate = dF2.parse(lunchStartTime);
        Date dinnerDate = dF2.parse(dinnerStartTime);
        Date closingDate = dF2.parse(closingTime);
        
        if(currentTime.compareTo(breakfastDate)>0 && currentTime.compareTo(lunchDate)<0){
            //enabled all;
            jButtonBreakfast.setEnabled(true);
            jButtonLunch.setEnabled(true);
            jButtonDinner.setEnabled(true);
            jButtonDrinks.setEnabled(true);
        } else if(currentTime.compareTo(lunchDate)>0 && currentTime.compareTo(dinnerDate)<0){
            //enabled lunch/dinner;
            jButtonBreakfast.setEnabled(false);
            jButtonLunch.setEnabled(true);
            jButtonDinner.setEnabled(true);
            jButtonDrinks.setEnabled(true);
        } else if(currentTime.compareTo(dinnerDate)>0 && currentTime.compareTo(closingDate)<0){
            //enabled dinner;
            jButtonBreakfast.setEnabled(false);
            jButtonLunch.setEnabled(false);
            jButtonDinner.setEnabled(true);
            jButtonDrinks.setEnabled(true);
        } else {
            //enabled all;
            jButtonBreakfast.setEnabled(true);
            jButtonLunch.setEnabled(true);
            jButtonDinner.setEnabled(true);
            jButtonDrinks.setEnabled(true);
        } 
    
    }
    
    /**
     * Displays the basket.
     */
    private void jButtonBasketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBasketActionPerformed
        displayBasket();
    }//GEN-LAST:event_jButtonBasketActionPerformed

    
    /**
     * Updates the queue in real time.
     */
    public void queueUpdater(){
        Thread clockTicker;
        clockTicker = new Thread(){
            @Override
            public void run(){
                for(;;){
                    if(jScrollPaneQueue.isShowing()){
                        if(jScrollPaneBreakfastQueue.isShowing()){
                            try {
                                switchQueuePanelDisplay("Breakfast");
                            } catch (IOException ex) {
                                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ParseException ex) {
                                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        if(jScrollPaneLunchQueue.isShowing()){
                            try {
                                switchQueuePanelDisplay("Lunch");
                            } catch (IOException ex) {
                                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ParseException ex) {
                                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } 

                        if(jScrollPaneDinnerQueue.isShowing()){
                            try {
                                switchQueuePanelDisplay("Dinner");
                            } catch (IOException ex) {
                                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ParseException ex) {
                                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } 
                    }
                    try {
                        sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        
        clockTicker.start(); 
    }
    
    
    public JPanel getJPanelBasket(){
        return jPanelBasket;
    }
    
    public Order getTotalOrder(){
        return totalOrder;
    }

    public Order getBreakfastOrder() {
        return breakfastOrder;
    }

    public Order getLunchOrder() {
        return lunchOrder;
    }

    public Order getDinnerOrder() {
        return dinnerOrder;
    }

    public Order getDrinksOrder() {
        return drinksOrder;
    }
    
    /**
     * Displays the basket.
     */
    public void displayBasket(){
        switchMainDisplayTo(jScrollPaneBasket);
        
        jPanelBasket.removeAll();
        jPanelBasket.repaint();
        jPanelBasket.revalidate();
        jPanelBasket.add(jLabelBasketTitle);
        jPanelBasket.add(jButtonClearBasket);
        jPanelBasket.add(jButtonProceedToPayment);
        breakfastOrder.getItemList().clear();
        lunchOrder.getItemList().clear();
        dinnerOrder.getItemList().clear();
        drinksOrder.getItemList().clear();
        
        orderPanelCount = 0;
        
        isBreakfastOrderUp = false;
        isLunchOrderUp = false;
        isDinnerOrderUp = false;
        isDrinksOrderUp = false;
        
        for(int i = 0; i < totalOrder.getItemList().size(); i++){
            jPanelBasket.add(new BasketItemPanel(totalOrder.getItemList().get(i), panelYPos+(basketPanelYDistance*i), this));
            
            if(totalOrder.getItemList().get(i).getMenuType().equals("Breakfast")){
                breakfastOrder.getItemList().add(totalOrder.getItemList().get(i));
                isBreakfastOrderUp = true;
            }
            else if(totalOrder.getItemList().get(i).getMenuType().equals("Lunch")){
                lunchOrder.getItemList().add(totalOrder.getItemList().get(i));
                isLunchOrderUp = true;
            }
            else if(totalOrder.getItemList().get(i).getMenuType().equals("Dinner")){
                dinnerOrder.getItemList().add(totalOrder.getItemList().get(i));
                isDinnerOrderUp = true;
            }
            else if(totalOrder.getItemList().get(i).getMenuType().equals("Drinks")){
                drinksOrder.getItemList().add(totalOrder.getItemList().get(i));
                isDrinksOrderUp = true;
            }
        }

        displayOrderItemPanels();
        jPanelBasket.repaint();
        jPanelBasket.revalidate();
        updateBasketPrice();
    }
    
    /**
     * Displays the panels for all the different orders.
     */
    private void displayOrderItemPanels(){
        if(isBreakfastOrderUp){
            jPanelBasket.add(new OrderItemPanel(breakfastOrder, panelYPos+(orderPanelYDistance*orderPanelCount), this));
            orderPanelCount++;
        }
        if(isLunchOrderUp){
            jPanelBasket.add(new OrderItemPanel(lunchOrder, panelYPos+(orderPanelYDistance*orderPanelCount), this));
            orderPanelCount++;
        }
        if(isDinnerOrderUp){
            jPanelBasket.add(new OrderItemPanel(dinnerOrder, panelYPos+(orderPanelYDistance*orderPanelCount), this));
            orderPanelCount++;
        }
        if(isDrinksOrderUp){
            jPanelBasket.add(new OrderItemPanel(drinksOrder, panelYPos+(orderPanelYDistance*orderPanelCount), this));
            orderPanelCount++;
        }
    }
       
    /**
     * Switches main display to the home page.
     */
    private void jButtonHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHomeActionPerformed
        switchMainDisplayTo(jScrollPaneHome);
    }//GEN-LAST:event_jButtonHomeActionPerformed

    /**
     * Switches main display to the Settings page.
     */
    private void jButtonSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSettingsActionPerformed
        switchMainDisplayTo(jScrollPaneSettings);
    }//GEN-LAST:event_jButtonSettingsActionPerformed

    /**
     * Switches main display to the queue page.
     */
    private void jButtonQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQueueActionPerformed
        switchMainDisplayTo(jScrollPaneQueue);
    }//GEN-LAST:event_jButtonQueueActionPerformed

    /**
     * Switches main display to the stock page.
     */
    private void jButtonStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStockActionPerformed
        try {
            generateStockTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        switchMainDisplayTo(jScrollPaneEditStock);
    }//GEN-LAST:event_jButtonStockActionPerformed

    /**
     * Switches main display to the History page.
     */
    private void jButtonHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHistoryActionPerformed
        /*if(globalUserType.equals("Kitchen") || globalUserType.equals("Waiting")){
            jButton6.setEnabled(false);
            jButton8.setEnabled(false);
            jButton9.setEnabled(false);
            jButton10.setEnabled(false);
            jButtonStaffReport.setEnabled(true);
        }
        */
        switchMainDisplayTo(jScrollPaneHistory);
    }//GEN-LAST:event_jButtonHistoryActionPerformed

    /**
     * Switches main display to the menu page.
     */
    private void jButtonMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMenuActionPerformed
        switchMainDisplayTo(jScrollPaneMenu);
    }//GEN-LAST:event_jButtonMenuActionPerformed
    
    /**
     * Switches the integrated menu panel to a different menu type.
     */
    private void switchMenuPanelDisplay(String menuType) throws IOException, SQLException{
        int yDistance = 45;
        int yPos = 0;
        String [][] databaseItems = con.selectItem("Menu_Type", menuType);
        JPanel currentPanel = null;

        jPanelIntegratedMenu.removeAll();
        jPanelIntegratedMenu.repaint();
        jPanelIntegratedMenu.revalidate(); 
        
        switch (menuType) {
            case "Breakfast":
                jPanelIntegratedMenu.add(jScrollPaneBreakfastMenu);
                currentPanel = jPanelBreakfastMenu;
                break;
            case "Lunch":
                jPanelIntegratedMenu.add(jScrollPaneLunchMenu);
                currentPanel = jPanelLunchMenu;
                break;
            case "Dinner":
                jPanelIntegratedMenu.add(jScrollPaneDinnerMenu);
                currentPanel = jPanelDinnerMenu;
                break;
            default:
                jPanelIntegratedMenu.add(jScrollPaneDrinksMenu);
                currentPanel = jPanelDrinksMenu;
                break;
        }
        
        for(int i = 0; i < databaseItems.length; i++){
            boolean isAvailable = false;
            boolean isSpecial = false;
            
            if(databaseItems[i][2].equals("1")){
                isAvailable = true;
            }
            if(databaseItems[i][6].equals("1")){
                isSpecial = true;
            }
            
            Item itemToAdd = new Item(databaseItems[i][0], databaseItems[i][1], Double.parseDouble(databaseItems[i][7]), 
            databaseItems[i][8], isAvailable, Integer.parseInt(databaseItems[i][4]), databaseItems[i][3],
            databaseItems[i][5], isSpecial);
          
            String[][] itemStock = con.selectItemStock("ItemID", databaseItems[i][0]);
            
            
            
            
            MenuItemPanel panelToAdd = new MenuItemPanel(itemToAdd, yPos+(yDistance*i), this);
            
            if(menuType.equals("Breakfast") && !isBreakfastMenuPainted){
                currentPanel.add(panelToAdd);
                if(i == (databaseItems.length-1)){
                    isBreakfastMenuPainted = true;
                }
            }
            if(menuType.equals("Lunch") && !isLunchMenuPainted){
                currentPanel.add(panelToAdd);
                if(i == (databaseItems.length-1)){
                    isLunchMenuPainted = true;
                }
            }
            if(menuType.equals("Dinner") && !isDinnerMenuPainted){
                currentPanel.add(panelToAdd);
                if(i == (databaseItems.length-1)){
                    isDinnerMenuPainted = true;
                }
            }
            if(menuType.equals("Drinks") && !isDrinksMenuPainted){
                currentPanel.add(panelToAdd);
                if(i == (databaseItems.length-1)){
                    isDrinksMenuPainted = true;
                }
            }
            
            menuPanel.add(panelToAdd);
            currentMenuItems.add(itemToAdd);
        }
        
        jPanelIntegratedMenu.repaint();
        jPanelIntegratedMenu.revalidate();
    }
    
    /**
     * Switches the integrated queue panel to a different queue type.
     */
    private void switchQueuePanelDisplay(String menuType) throws IOException, ParseException, SQLException{
        String [][] sortedOrders = sortOrders(menuType);
                
        JPanel currentPanel = null;

        jPanelIntegratedQueue.removeAll();
        jPanelIntegratedQueue.repaint();
        jPanelIntegratedQueue.revalidate(); 
               
        switch (menuType) {
            case "Breakfast":
                jPanelIntegratedQueue.add(jScrollPaneBreakfastQueue);
                currentPanel = jPanelBreakfastQueue;
                break;
            case "Lunch":
                jPanelIntegratedQueue.add(jScrollPaneLunchQueue);
                currentPanel = jPanelLunchQueue;
                break;
            case "Dinner":
                jPanelIntegratedQueue.add(jScrollPaneDinnerQueue);
                currentPanel = jPanelDinnerQueue;
                break;
        }
        
        currentPanel.removeAll();
        
        quadroOrderSelector(sortedOrders, menuType, currentPanel);
        
        jPanelIntegratedQueue.repaint();
        jPanelIntegratedQueue.revalidate();
    }
    
    /**
     * Checks to see if an order is late.
     */
    public String checkLate(String[] a, String menuType) throws ParseException, SQLException{
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat onlyDate = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = df.parse(a[3]+" "+a[4]);
        Date nowDate = df.parse(df.format(Calendar.getInstance().getTime()));
        Date LimitedDate = orderDate;
        Date defaultDate = nowDate;
        Date LimitedDefaultDate;
        int preparationTimeLimit;
        
        if(a[8].equals("1")){
            preparationTimeLimit = Integer.parseInt(a[11])*2;
        } else {
            preparationTimeLimit = Integer.parseInt(a[11])*4;
        }
        
        LimitedDate.setMinutes(LimitedDate.getMinutes()+preparationTimeLimit);
        
        if(menuType.equals("Breakfast")){
            defaultDate = df.parse(onlyDate.format(Calendar.getInstance().getTime())+" "+breakfastStartTime);           
        } else if (menuType.equals("Lunch")){
            defaultDate = df.parse(onlyDate.format(Calendar.getInstance().getTime())+" "+lunchStartTime);
        } else if (menuType.equals("Dinner")){
            defaultDate = df.parse(onlyDate.format(Calendar.getInstance().getTime())+" "+dinnerStartTime);
        }
        LimitedDefaultDate = defaultDate;
        if(orderDate.compareTo(defaultDate)<0){
                //if advanced order do this: check whether time now is more than time in 
            LimitedDefaultDate.setMinutes(LimitedDefaultDate.getMinutes()+preparationTimeLimit);
            if(nowDate.compareTo(LimitedDefaultDate)>0){
                
                con.changeOrder("Is_Late", a[0], "1");
                String[][] managers = con.selectStaff("Staff_Type", "Manager", "", false);
                for(int j = 0; j<managers.length; j++){
                    con.changeStaff("Notification", managers[j][1], "Order " + a[0] + " is Late!");
                }
                a[12]="1";
            }
        } else {
                //if not advanced order do this
            if(nowDate.compareTo(LimitedDate)>0){
                con.changeOrder("Is_Late", a[0], "1");
                String[][] managers = con.selectStaff("Staff_Type", "Manager", "", false);
                for(int j = 0; j<managers.length; j++){
                    con.changeStaff("Notification", managers[j][1], "Order " + a[0] + " is Late!");
                }
                a[12]="1";
            }
        }
        return a[12];
    }
    
    /**
     * Checks for notifications for late orders.
     */
    public void staffNotificationUpdater(final Staff staff){
        Thread clockTicker;
        clockTicker = new Thread(){
            @Override
            public void run(){
                for(;;){
                    
                    if(logoutPressed){
                        logoutPressed = false;
                        break;
                    }
                    
                    try {
                        String[][] thisStaff = con.selectStaff("StaffID", staff.getStaffID(), "", false);
                        if(!thisStaff[0][5].equals("None") && globalUserType.equals("Staff")){
                            Notification not = new Notification(thisStaff[0][5]);
                            not.setVisible(true);
                            not.setResizable(false);
                            not.setFocusable(true);
                        }                        
                        try {
                            sleep(30000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        };
        
        clockTicker.start(); 
    }
    
    /**
     * Checks for customer order notification.
     */
    public void customerNotificationUpdater(final Customer customer){
        Thread clockTicker;
        clockTicker = new Thread(){
            @Override
            public void run(){
                for(;;){
                    
                    if(logoutPressed){
                        logoutPressed = false;
                        break;
                    }
                    
                    try {
                        String[][] thisCustomer = con.selectCustomer("CustomerID", customer.getCustomerID(), "", false);
                        if(!thisCustomer[0][17].equals("None") && globalUserType.equals("Customer")){
                            Notification not = new Notification(thisCustomer[0][17]);
                            not.setVisible(true);
                            not.setResizable(false);
                            not.setFocusable(true);
                            con.changeCustomer("Notification", "None", customer.getID());
                        }                        
                        try {
                            sleep(30000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        };
        
        clockTicker.start(); 
    }
    
    /**
     * Selects orders from database and assigns them their items.
     */
    public void quadroOrderSelector(String[][] sortedOrders, String menuType,
            JPanel currentPanel) throws IOException, SQLException, ParseException{
        
        int yDistance = 41;
        int yPos = 0;
        
        for(int i = 0; i < sortedOrders.length; i++){
            String [][] orderItem = con.selectOrderItem("OrderID", sortedOrders[i][0]);
            sortedOrders[i][12] = checkLate(sortedOrders[i], menuType);
            
            boolean isAvailable = false;
            boolean isSpecial = false;
                        
            Order orderToAdd = new Order(sortedOrders[i][0], sortedOrders[i][2], menuType);
            
            for(int j = 0; j < orderItem.length; j++){
                String[][] databaseItems = con.selectItem("ItemID", orderItem[j][1]);
                
                for (String[] databaseItem : databaseItems) {
                    if (databaseItem[2].equals("1")) {
                        isAvailable = true;
                    }
                    if (databaseItem[6].equals("1")) {
                        isSpecial = true;
                    }
                    Item itemToAdd = new Item(databaseItem[0], databaseItem[1], Double.parseDouble(databaseItem[7]), databaseItem[8], isAvailable, Integer.parseInt(databaseItem[4]), databaseItem[3], databaseItem[5], isSpecial);
                    for(int y = 0; y < Integer.parseInt(orderItem[j][2]); y++){
                        orderToAdd.getItemList().add(itemToAdd);
                    }
                }   
            }
                                  
            if(sortedOrders[i][8].equals("1")){
                orderToAdd.setIsPriority(true);
            }
            if(sortedOrders[i][7].equals("Flagged")){
                orderToAdd.setIsFlagged(true);
            }
            if(sortedOrders[i][12].equals("1")){
                orderToAdd.setStatus("Late");
            }
            orderToAdd.setTime(sortedOrders[i][4]);
            
            QueueItemPanel panelToAdd = new QueueItemPanel(orderToAdd, yPos+(yDistance*i), this, Double.parseDouble(sortedOrders[i][5]));

            currentPanel.add(panelToAdd);
            queuePanel.add(panelToAdd);
            currentOrderItems.add(orderToAdd);
        }
    }
    
    /**
     * Sorts the order of orders in a queue.
     */
    public String[][] sortOrders(String menuType) throws ParseException, SQLException{
        String [][] incompleteOrders = con.selectOrder("Order_Status", "Incomplete", "", false);
        String [][] flaggedOrders = con.selectOrder("Order_Status", "Flagged", "", false);
        ArrayList<String[]> priorityOrders = new ArrayList<>();
        ArrayList<String[]> notPriorityOrders = new ArrayList<>();
        ArrayList<String[]> sortedOrders = new ArrayList<>();
        for(int i = 0; i<incompleteOrders.length; i++){
            if(incompleteOrders[i][10].equals(menuType)){
                if(incompleteOrders[i][8].equals("1")){
                    priorityOrders.add(incompleteOrders[i]);
                } else {
                    notPriorityOrders.add(incompleteOrders[i]);
                }
            }
        }
        
        for(int i = 0; i<flaggedOrders.length; i++){
            if(flaggedOrders[i][10].equals(menuType)){
                if(flaggedOrders[i][8].equals("1")){
                    priorityOrders.add(flaggedOrders[i]);
                } else {
                    notPriorityOrders.add(flaggedOrders[i]);
                }
            }
        }
        
       
        while(!priorityOrders.isEmpty()){
            int i = FindLeastDate(priorityOrders);
            sortedOrders.add(priorityOrders.get(i));
            priorityOrders.remove(i);
            
        }
        
        while(!notPriorityOrders.isEmpty()){
            int i = FindLeastDate(notPriorityOrders);
            sortedOrders.add(notPriorityOrders.get(i));
            notPriorityOrders.remove(i);
        }
        
        return (String[][]) sortedOrders.toArray(new String[sortedOrders.size()][13]);
    }
    
    /**
     * Finds the least date and returns the value.
     */
    public int FindLeastDate(ArrayList<String[]> a) throws ParseException{
        int leastIndex = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i = 0; i<a.size(); i++){
            String rawI = a.get(i)[3]+" "+a.get(i)[4];
            String rawLeast = a.get(leastIndex)[3]+" "+a.get(leastIndex)[4];
            Date dateI = df.parse(rawI);
            Date dateLeast = df.parse(rawLeast);
            if(dateI.compareTo(dateLeast)<=0){
                leastIndex = i;
            }
        }
        return leastIndex;
    }
    
    /**
     * Automatically backs up the system every month.
     */
    public final void AUTOupdate() throws SQLException, ParseException, IOException, InterruptedException{
        String updateDateString = con.selectAutoUpdate();
        Date updateDate = dF1.parse(updateDateString);
        Date nowDate = dF1.parse(dF1.format(Calendar.getInstance().getTime()));
        if(nowDate.compareTo(updateDate) >= 0){
            con.createBackup();
            updateDate.setMonth(updateDate.getMonth()+1);
            con.changeAutoUpdate(dF1.format(updateDate));
        }        
    }
    
    public ArrayList<MenuItemPanel> getMenuPanel(){
        return menuPanel;
    }
    
    /**
     * Updates the basket price.
     */
    public void updateBasketPrice(){
        double totalPrice = breakfastOrder.getTotalPrice() + lunchOrder.getTotalPrice() +
                dinnerOrder.getTotalPrice() + drinksOrder.getTotalPrice();
        totalOrder.setTotalPrice(totalPrice);
        jButtonBasket.setText("Basket: £" + dF.format(totalPrice));
    }
    
    /**
     * Adds Item to database.
     */
    private void jButtonAddToDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddToDatabaseActionPerformed
        String menuType = null;
        String courseType = null;
        String isSpecial = "0";
        
        if(jCheckBoxDailySpecial.isSelected()){
            isSpecial = "1";
        }
        if(jCheckBoxBreakfast.isSelected()){
            menuType = "Breakfast";
        }
        else if(jCheckBoxLunch.isSelected()){
            menuType = "Lunch";
        }
        else if(jCheckBoxDinner.isSelected()){
            menuType = "Dinner";
            if(jCheckBoxStarter.isSelected()){
                courseType = "Starter";
            }
            if(jCheckBoxMain.isSelected()){
                courseType = "Main";
            }
            if(jCheckBoxDessert.isSelected()){
                courseType = "Dessert";
            }
        }
        else if(jCheckBoxDrinks.isSelected()){
            menuType = "Drinks";
        }

        try {
            con.insertItem(jTextFieldName.getText(), 1,
                    menuType, jTextFieldPreparationTime.getText(),
                    courseType, Integer.parseInt(isSpecial), jTextFieldPrice.getText(),
                    jTextFieldDescription.getText());
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        emptyFormFields("Add Item");
    }//GEN-LAST:event_jButtonAddToDatabaseActionPerformed

    private void jButtonBackToItems1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToItems1ActionPerformed
        try {
            generateItemTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        switchMainDisplayTo(jScrollPaneEditItems);
    }//GEN-LAST:event_jButtonBackToItems1ActionPerformed

    private void jButtonItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonItemsActionPerformed
        try {
            generateItemTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        switchMainDisplayTo(jScrollPaneEditItems);
        showCourseType(false);
    }//GEN-LAST:event_jButtonItemsActionPerformed

    public JScrollPane getJScrollPaneAddItemStock(){
        return jScrollPaneAddItemStock;
    }
    
    /**
     * Generates the table needed for items.
     */
    public void generateItemTable() throws SQLException{
        String[][] pristineTable = con.selectItem("", "");
        
        for(int i = 0; i<pristineTable.length; i++){
            if(pristineTable[i][6].equals("1")){
                pristineTable[i][6]="Yes";
            } else {
                pristineTable[i][6]="No";
            }
            if(pristineTable[i][2].equals("1")){
                pristineTable[i][2]="Yes";
            } else {
                pristineTable[i][2]="No";
            }
            if(pristineTable[i][5].equals("null")){
                pristineTable[i][5] = "";
            }
        }
        
        EditItemTable editItems = new EditItemTable(pristineTable, 
                con.selectItemHeaders(), jButtonDeleteSelectedItem, this, jButtonAddStockToItem);
        jScrollPaneIntegratedEditItems.getViewport().add(editItems);
    }
    
    /**
     * Generates the table needed for staff.
     */
    public void generateStaffTable() throws SQLException {
        String[][] pristineTable = con.selectStaff("4", "", "", false);
        
        EditStaffTable editStaff = new EditStaffTable(pristineTable, 
                con.selectStaffHeaders(), jButtonDeleteSelectedStaffAccount, this);
        jScrollPaneIntegratedEditStaffAccount.getViewport().add(editStaff);
    }
    
    /**
     * Generates the table needed for customers.
     */
    public void generateCustomerTable() throws SQLException {
        String[][] pristineTable = con.selectCustomer("", "", "", false);
        EditCustomerTable editCustomer = new EditCustomerTable(pristineTable, 
                con.selectCustomerHeaders(), jButtonDeleteSelectedCustomerAccount, this);
        jScrollPaneIntegratedEditCustomerAccount.getViewport().add(editCustomer);
    }
    
    public JScrollPane getJScrollPaneEditItems(){
        return jScrollPaneEditItems;
    }
    
    private void jCheckBoxBreakfastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxBreakfastActionPerformed
        untickMenuCourseBoxes("Menu");
        showCourseType(false);
        jCheckBoxBreakfast.setSelected(true);
    }//GEN-LAST:event_jCheckBoxBreakfastActionPerformed

    private void jCheckBoxLunchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxLunchActionPerformed
        untickMenuCourseBoxes("Menu");
        showCourseType(false);
        jCheckBoxLunch.setSelected(true);
    }//GEN-LAST:event_jCheckBoxLunchActionPerformed

    private void jCheckBoxDinnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDinnerActionPerformed
        untickMenuCourseBoxes("Menu");
        showCourseType(true);
        jCheckBoxDinner.setSelected(true);
    }//GEN-LAST:event_jCheckBoxDinnerActionPerformed

    private void jCheckBoxDrinksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDrinksActionPerformed
        untickMenuCourseBoxes("Menu");
        showCourseType(false);
        jCheckBoxDrinks.setSelected(true);
    }//GEN-LAST:event_jCheckBoxDrinksActionPerformed

    private void jCheckBoxStarterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxStarterActionPerformed
        untickMenuCourseBoxes("Course");
        jCheckBoxStarter.setSelected(true);
    }//GEN-LAST:event_jCheckBoxStarterActionPerformed

    private void jCheckBoxMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMainActionPerformed
        untickMenuCourseBoxes("Course");
        jCheckBoxMain.setSelected(true);
    }//GEN-LAST:event_jCheckBoxMainActionPerformed

    private void jCheckBoxDessertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDessertActionPerformed
        untickMenuCourseBoxes("Course");
        jCheckBoxDessert.setSelected(true);
    }//GEN-LAST:event_jCheckBoxDessertActionPerformed

    /**
     * Adds the stock to database to save.
     */
    private void jButtonAddStockToDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddStockToDatabaseActionPerformed
        if(jTextFieldQuantity.getText().isEmpty()){
            try {
                con.insertStock(jTextFieldName1.getText(), Integer.toString(0), 
                        jTextFieldMin.getText(), jTextFieldMax.getText());
            } catch (SQLException ex) {
                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            try {
                con.insertStock(jTextFieldName1.getText(), jTextFieldQuantity.getText(), 
                        jTextFieldMin.getText(), jTextFieldMax.getText());
            } catch (SQLException ex) {
                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        emptyFormFields("Add Stock");
    }//GEN-LAST:event_jButtonAddStockToDatabaseActionPerformed

    private void jButtonBackToStock1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToStock1ActionPerformed
        try {
            generateStockTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        switchMainDisplayTo(jScrollPaneEditStock);
    }//GEN-LAST:event_jButtonBackToStock1ActionPerformed
    
    /**
     * Generates the table needed for stock.
     */
    public void generateStockTable() throws SQLException{
            EditStockTable editStock = new EditStockTable(con.selectStock("", ""),
                    con.selectStockHeaders(), jButtonDeleteSelectedStock, this);
            jScrollPaneIntegratedEditStock.getViewport().add(editStock);
    }
    
    /**
     * Generic method that populates text fields.
     */
    private void populateAreaDisplay(JTextArea tA, String type) throws SQLException{
        tA.setText("");
        String[][] areaType = null;
        
        switch (type) {
            case "Stock":
                areaType = con.selectStock("Name", "");
                tA.setRows(areaType.length);
                break;
            case "Item":
                areaType = con.selectItem("Name", "");
                tA.setRows(areaType.length);
                break;
        }

        for (String[] areaRow : areaType) {
            if(tA == jTextAreaStockDisplay2){
                tA.append(areaRow[1] + ": " + areaRow[2] + "\n");
            }
        }
    }
    
    /**
     * Updates stock in database.
     */
    private void jButtonUpdateStockInDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateStockInDatabaseActionPerformed
        try {
            String[][] stock = con.selectStock("Name", jTextFieldStockName.getText());
            int newQuantity = Integer.parseInt(stock[0][2]) +
                    Integer.parseInt(jTextFieldStockQuantityToAdd.getText());
            con.changeStock("Quantity", jTextFieldStockName.getText(), Integer.toString(newQuantity));
            populateAreaDisplay(jTextAreaStockDisplay2, "Stock");
            jTextFieldStockName.setText("");
            jTextFieldStockQuantityToAdd.setText("");
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonUpdateStockInDatabaseActionPerformed

    private void jButtonBackToStock3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToStock3ActionPerformed
        switchMainDisplayTo(jScrollPaneEditStock);
    }//GEN-LAST:event_jButtonBackToStock3ActionPerformed

    private void jButtonBreakfastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBreakfastActionPerformed
        try {
            switchMenuPanelDisplay("Breakfast");
        } catch (IOException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonBreakfastActionPerformed

    private void jButtonLunchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLunchActionPerformed
        try {
            switchMenuPanelDisplay("Lunch");
        } catch (IOException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonLunchActionPerformed

    private void jButtonDinnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDinnerActionPerformed
        try {
            switchMenuPanelDisplay("Dinner");
        } catch (IOException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonDinnerActionPerformed

    private void jButtonDrinksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDrinksActionPerformed
        try {
            switchMenuPanelDisplay("Drinks");
        } catch (IOException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonDrinksActionPerformed

    private void jButtonCheckoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckoutActionPerformed
        displayBasket();
    }//GEN-LAST:event_jButtonCheckoutActionPerformed

    private void jButtonClearBasketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearBasketActionPerformed
        clearBasket();
    }//GEN-LAST:event_jButtonClearBasketActionPerformed

    /**
     * Clears the basket.
     */
    private void clearBasket(){
        totalOrder.getItemList().clear();
        breakfastOrder.getItemList().clear();
        lunchOrder.getItemList().clear();
        dinnerOrder.getItemList().clear();
        drinksOrder.getItemList().clear();
        breakfastOrder.setIsPriority(false);
        lunchOrder.setIsPriority(false);
        dinnerOrder.setIsPriority(false);
        drinksOrder.setIsPriority(false);
        
        for(int i = 0; i < menuPanel.size(); i++){
            menuPanel.get(i).getCombo().setSelectedIndex(0);
        }
        
        displayBasket();
    }
    
    private void jButtonProceedToPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProceedToPaymentActionPerformed
        switchMainDisplayTo(jScrollPanePayment);
        jPanelIntegratedPayment.removeAll();
        showMenuPictures();
    }//GEN-LAST:event_jButtonProceedToPaymentActionPerformed

    private void jButtonBackToBasketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToBasketActionPerformed
        /*if(!totalOrder.getItemList().isEmpty()){
            jButtonClearBasket.setEnabled(true);
            jButtonProceedToPayment.setEnabled(true);
        }
        else{
            jButtonProceedToPayment.setEnabled(false);
            jButtonClearBasket.setEnabled(false);
        }
                */
        switchMainDisplayTo(jScrollPaneBasket);
    }//GEN-LAST:event_jButtonBackToBasketActionPerformed

    private void jButtonAddNewCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddNewCardActionPerformed
        AddNewCard newCard = null;
        try {
            newCard = new AddNewCard(this, customer);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        newCard.setVisible(true);
        newCard.setResizable(false);
        newCard.setFocusable(true);
    }//GEN-LAST:event_jButtonAddNewCardActionPerformed

    private void jButtonUseExistingCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUseExistingCardActionPerformed
        try {
            String[][] dataCust = con.selectCustomer("CustomerID", customer.getCustomerID(), "", false);
            
            if(dataCust[0][12].equals("None")){
                AddCardFirst cardFirst = new AddCardFirst();
            }
            else{
                switchIntegratedPaymentPanel(jScrollPaneCard);
            }
            
            updateCardDetails();
            getDiscountType(jLabelDiscountsApplied);
            double totalPrice = breakfastOrder.getTotalPrice() + lunchOrder.getTotalPrice() +
                    dinnerOrder.getTotalPrice() + drinksOrder.getTotalPrice();
            double discountedPrice = applyDiscount(totalPrice);
            jLabelTotalPrice.setText("Total Price: £" + dF.format(discountedPrice));
            jLabelIsCredit.setText("Credit/Debit: " + dataCust[0][13]);
            jLabelCardType.setText("Card Type: " + dataCust[0][14]);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonUseExistingCardActionPerformed

    /**
     * Returns the data needed for customer spending report.
     */
    public String[][] customerSpendingsReport(String Date1, String Date2) throws ParseException, SQLException{
        String[][] customerArray =null;
        
        if(globalUserType.equals("Customer")){
            customerArray = con.selectCustomer("UserID", customer.getID(), "", false);
        }
        else{
            customerArray = con.selectCustomer("", "", "", false);
        }
        
        String[][] reportData = new String[customerArray.length][4];
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = df.parse(Date1+" 00:00:00");
        Date date2 = df.parse(Date2+" 00:00:00");
        for(int i = 0; i<customerArray.length; i++){
            String[][] orderInfo = con.selectOrder("CustomerID", customerArray[i][0], "", false);
            double totalSpendingAtTimes = 0;
            if(orderInfo.length > 0){
                
                Date orderDate = df.parse(orderInfo[0][3] +" "+ orderInfo[0][4]);
                for(int j = 0; j<orderInfo.length; j++){
                    if(orderDate.compareTo(date1)>0 && orderDate.compareTo(date2)<0){
                        totalSpendingAtTimes += Double.parseDouble(orderInfo[0][5]);
                    }
                }
            }
                reportData[i][0] = customerArray[i][0]; // Customer ID
                reportData[i][1] = customerArray[i][2]; // Customer Name
                reportData[i][2] = customerArray[i][3]; // Customer Surname
                reportData[i][3] = Double.toString(totalSpendingAtTimes); //total Spent from date 1 to date 2.
            
        } 
        
        return reportData;
    }
    
    /**
     * Returns the data needed for refund report.
     */
    public String[][] refundReport(String Date1, String Date2) throws ParseException, SQLException{
        String[][] ordersArray = con.selectOrder("Order_Date", Date1, Date2, true);
        ArrayList<String[]> reportData = new ArrayList<>();
        for(int i = 0; i<ordersArray.length; i++){
            if(ordersArray[i][6].equals("1")){
                
                if(ordersArray[i][8].equals("1")) { ordersArray[i][8] = "Yes"; } else { ordersArray[i][8] = "No"; }
                if(ordersArray[i][12].equals("1")) { ordersArray[i][12] = "Yes"; } else { ordersArray[i][12] = "No"; }
                String[][] customerInfo = con.selectCustomer("CustomerID", ordersArray[i][2], "", false);
                String[] info = new String[10];
                info[0] = ordersArray[i][2]; //  Customer ID
                info[1] = customerInfo[0][2]; // Customer Name
                info[2] = customerInfo[0][3]; // Customer Surname
                info[3] = ordersArray[i][0]; //  OrderID
                info[4] = ordersArray[i][3]; //  Order Date
                info[5] = ordersArray[i][4]; //  Order Time
                info[6] = ordersArray[i][7]; //  Order_Status 
                info[7] = ordersArray[i][8]; //  Is_Priority
                info[8] = ordersArray[i][12]; // Is_Late
                info[9] = ordersArray[i][1]; //  StaffID
                reportData.add(info);
            }
        }
        return (String[][]) reportData.toArray(new String[reportData.size()][10]);
    }
    
    /**
     * Returns the data needed for logged in customers report.
     */
    public String[][] loggedInCustomersReport() throws ParseException, SQLException{
        String[][] customerArray = con.selectCustomer("1", "", "", false);
        String[][] reportData = new String[customerArray.length][3];
        
        for(int i = 0; i<customerArray.length; i++){
            reportData[i][0] = customerArray[i][0]; // Customer ID
            reportData[i][1] = customerArray[i][2]; // Customer Name
            reportData[i][2] = customerArray[i][3]; // Customer Surname
        } 
        return reportData;
    }
    
    /**
     * Returns the data needed for pending orders for logged in customers report.
     */
    public String[][] pendingOrdersForLoggedInCustomersReport(String customerID) throws ParseException, SQLException{
        String[][] orderArray = con.selectOrder("CustomerID", customerID, "", false);
        ArrayList<String[]> reportData = new ArrayList<>();
        
        DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        DateFormat onlyDate = new SimpleDateFormat("YYYY-MM-dd 00:00:00");
        Date today = onlyDate.parse(onlyDate.format(Calendar.getInstance().getTime()));
        
        for(int i = 0; i<orderArray.length; i++){
            Date orderDate = df.parse(orderArray[0][3] +" "+ orderArray[0][4]);
            if(orderDate.compareTo(today)>0 && !orderArray[i][7].equals("Complete")){
                String[] orderRecord = new String[6];
                if(orderArray[i][8].equals("1")) { orderArray[i][8] = "Yes"; } else { orderArray[i][8] = "No"; }
                if(orderArray[i][12].equals("1")) { orderArray[i][12] = "Yes"; } else { orderArray[i][12] = "No"; }
                orderRecord[0] = orderArray[i][0]; //  OrderID
                orderRecord[1] = orderArray[i][3]; //  Order Date
                orderRecord[2] = orderArray[i][4]; //  Order Time
                orderRecord[3] = orderArray[i][7]; //  Order_Status 
                orderRecord[4] = orderArray[i][8]; //  Is_Priority
                orderRecord[5] = orderArray[i][12]; // Is_Late
                reportData.add(orderRecord);
            }
        }
        
        return (String[][])reportData.toArray(new String[reportData.size()][6]);
    }
    
    /**
     * Returns the data needed for orders report.
     */
    public String[][] ordersReport(String Date1, String Date2) throws ParseException, SQLException{
        String[][] ordersArray = con.selectOrder("Order_Date", Date1, Date2, true);
        String[][] reportData = new String[ordersArray.length][10];
        for(int i = 0; i<ordersArray.length; i++){
            if(ordersArray[i][8].equals("1")) { ordersArray[i][8] = "Yes"; } else { ordersArray[i][8] = "No"; }
            if(ordersArray[i][12].equals("1")) { ordersArray[i][12] = "Yes"; } else { ordersArray[i][12] = "No"; }
            String[][] customerInfo = con.selectCustomer("CustomerID", ordersArray[i][2], "", false);
            reportData[i][0] = ordersArray[i][2]; //  Customer ID
            reportData[i][1] = customerInfo[0][2]; // Customer Name
            reportData[i][2] = customerInfo[0][3]; // Customer Surname
            reportData[i][3] = ordersArray[i][0]; //  OrderID
            reportData[i][4] = ordersArray[i][3]; //  Order Date
            reportData[i][5] = ordersArray[i][4]; //  Order Time
            reportData[i][6] = ordersArray[i][7]; //  Order_Status 
            reportData[i][7] = ordersArray[i][8]; //  Is_Priority
            reportData[i][8] = ordersArray[i][12]; // Is_Late
            reportData[i][9] = ordersArray[i][1]; //  StaffID
        } 
        return reportData;
    }
    
    /**
     * Shows the pictures for the menu icons.
     */
    private void showMenuPictures(){
        
        jLabelBreakfast.setVisible(true);
        jLabelLunch.setVisible(true);
        jLabelDinner.setVisible(true);
        jLabelDrinks.setVisible(true);
        jLabelBreakfast2.setVisible(true);
        jLabelLunch2.setVisible(true);
        jLabelDinner2.setVisible(true);
        jLabelDrinks2.setVisible(true);
        
        if(!breakfastOrder.getItemList().isEmpty()){
            breakfastIcon = new ImageIcon(breakfastPath);
            jLabelBreakfast.setToolTipText("Breakfast");
            jLabelBreakfast2.setToolTipText("Breakfast");
            breakfastImg = breakfastIcon.getImage();
            Image newImg1 = breakfastImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            jLabelBreakfast.setIcon(new ImageIcon(newImg1));
            jLabelBreakfast2.setIcon(new ImageIcon(newImg1));
        }
        else{
            jLabelBreakfast.setVisible(false);
            jLabelBreakfast2.setVisible(false);
        }
        
        if(!lunchOrder.getItemList().isEmpty()){
            lunchIcon = new ImageIcon(lunchPath);
            jLabelLunch.setToolTipText("Lunch");
            jLabelLunch2.setToolTipText("Lunch");
            lunchImg = lunchIcon.getImage();
            Image newImg2 = lunchImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            jLabelLunch.setIcon(new ImageIcon(newImg2));
            jLabelLunch2.setIcon(new ImageIcon(newImg2));
        }
        else{
            jLabelLunch.setVisible(false);
            jLabelLunch2.setVisible(false);
        }
        
        if(!dinnerOrder.getItemList().isEmpty()){
            dinnerIcon = new ImageIcon(dinnerPath);
            jLabelDinner.setToolTipText("Dinner");
            jLabelDinner2.setToolTipText("Dinner");
            dinnerImg = dinnerIcon.getImage();
            Image newImg3 = dinnerImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            jLabelDinner.setIcon(new ImageIcon(newImg3));
            jLabelDinner2.setIcon(new ImageIcon(newImg3));
        }
        else{
            jLabelDinner.setVisible(false);
            jLabelDinner2.setVisible(false);
        }
        
        if(!drinksOrder.getItemList().isEmpty()){
            drinksIcon = new ImageIcon(drinksPath);
            jLabelDrinks.setToolTipText("Drinks");
            jLabelDrinks2.setToolTipText("Drinks");
            drinksImg = drinksIcon.getImage();
            Image newImg4 = drinksImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            jLabelDrinks.setIcon(new ImageIcon(newImg4));
            jLabelDrinks2.setIcon(new ImageIcon(newImg4));
        }
        else{
            jLabelDrinks.setVisible(false);
            jLabelDrinks2.setVisible(false);
        }
    }
    
    /**
     * Updates the card details.
     */
    public void updateCardDetails() throws SQLException{
        String[][] dataCust = con.selectCustomer("CustomerID", customer.getCustomerID(), "", false);
        
        jLabelCardNameHolder.setText("Card Name Holder: " + dataCust[0][12]);
        jLabelCardNumber.setText("Card Number: " + dataCust[0][9]);
        jLabelExpirationDate.setText("Expiration Date: " + dataCust[0][16]);
    }
    
    /**
     * Returns the discount type.
     */
    public void getDiscountType(JLabel label) throws SQLException{
        String[][] dataCust = con.selectCustomer("CustomerID", customer.getCustomerID(), "", false);
        
        switch (dataCust[0][7]) {
            case "Silver":
                label.setText("Discounts Applied: Silver");
                break;
            case "Gold":
                label.setText("Discounts Applied: Gold");
                break;
            case "Diamond":
                label.setText("Discounts Applied: Diamond");
                break;
            default:
                label.setText("Discounts Applied: None");
                break;
        }
    }
    
    /**
     * Confirms the payment for a card payment.
     */
    private void jButtonConfirmPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfirmPaymentActionPerformed
        try {
            String[][] dataCust = con.selectCustomer("CustomerID", customer.getCustomerID(), "", false);
            String[][] discounts = con.selectDiscount("", "");
            if(jPasswordSecurityNumber.getText().equals(dataCust[0][15])){
                try {
                    
                    appendDrinks();
                    addOrdersToDatabase(breakfastOrder, "Card Payment");
                    addOrdersToDatabase(lunchOrder, "Card Payment");
                    addOrdersToDatabase(dinnerOrder, "Card Payment");
                    
                    int numberOfOrders = 0;
                    if(!breakfastOrder.getItemList().isEmpty()){
                        numberOfOrders += 1;
                    }
                    if(!lunchOrder.getItemList().isEmpty()){
                        numberOfOrders += 1;
                    }
                    if(!dinnerOrder.getItemList().isEmpty()){
                        numberOfOrders += 1;
                    }
                
                    double totalAmount = applyDiscount(totalOrder.getTotalPrice());
                    con.insertPayment(customer.getCustomerID(), dataCust[0][13], dataCust[0][14], dF.format(totalAmount), Integer.toString(numberOfOrders));
                    
                    clearBasket();
                    for(int i = 0; i<discounts.length; i++){
                        if(customer.getTotalSpent()>Double.parseDouble(discounts[i][3])){
                            customer.setVIPStatus(discounts[i][0]);
                            con.changeCustomer("VIP_Status", discounts[i][0], customer.getID());
                            if(customer.getPercentageDiscount().equals("None")){
                                customer.setPercentageDiscount("Fixed");
                                con.changeCustomer("Discount_Type", "Fixed", customer.getID());
                            }
                        }
                    }
                    
                    resetSession("Card Payment");
                } catch (ParseException ex) {
                    Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonConfirmPaymentActionPerformed

    /**
     * Confirms the payment for a bank transfer.
     */
    private void jButtonBankTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBankTransferActionPerformed
        try {
            switchIntegratedPaymentPanel(jScrollPaneBankTransfer);
            getDiscountType(jLabelDiscountsApplied1);
            double totalPrice = breakfastOrder.getTotalPrice() + lunchOrder.getTotalPrice() +
                    dinnerOrder.getTotalPrice() + drinksOrder.getTotalPrice();
            double discountedPrice = applyDiscount(totalPrice);
            jLabelTotalPrice1.setText("Total Price: £" + dF.format(discountedPrice));
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonBankTransferActionPerformed
    
    /**
     * Applies a discount to the total spenditure.
     */
    public double applyDiscount(double totalSpent) throws SQLException, ParseException{
        String[][] orders = con.selectOrder("CustomerID", customer.getCustomerID(), "", false);
        String[][] discounts = con.selectDiscount("", "");
        double discountedPrice = totalSpent;
        int discount;
        double spentLastMonth = 0;
        Date orderDate;
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateMonthAgo = df.parse(df.format(Calendar.getInstance().getTime()));
        dateMonthAgo.setMonth(dateMonthAgo.getMonth()-1);
        
        for (int i = 0; i < orders.length; i++ ){
            orderDate = df.parse(orders[i][3]+" "+orders[i][4]);
            if(orderDate.compareTo(dateMonthAgo)>0){
                spentLastMonth += Double.parseDouble(orders[i][5]);
            }            
        }
        
        if(customer.getPercentageDiscount().equals("Fixed")) {
            switch (customer.getVIPStatus()) {
            case "Silver":
                discountedPrice = (discountedPrice*(100 - Integer.parseInt(discounts[2][1])))/100;
                break;
            case "Gold":
                discountedPrice = (discountedPrice*(100 - Integer.parseInt(discounts[1][1])))/100;
                break;
            case "Diamond":
                discountedPrice = (discountedPrice*(100 - Integer.parseInt(discounts[0][1])))/100;
                break;
            }
        } else if(customer.getPercentageDiscount().equals("Flexible")){
            
            switch (customer.getVIPStatus()) {
            case "Silver":
                discount = Integer.parseInt(discounts[0][1]);
                discountedPrice = (discountedPrice*(100 - ((1/(-pow(1.01, spentLastMonth))+1)*discount)))/100;
                break;
            case "Gold":
                discount = Integer.parseInt(discounts[0][1]);
                discount = Integer.parseInt(discounts[0][1]);
                discountedPrice = (discountedPrice*(100 - ((1/(-pow(1.01, spentLastMonth))+1)*discount)))/100;
                break;
            case "Diamond":
                discount = Integer.parseInt(discounts[0][1]);
                discount = Integer.parseInt(discounts[0][1]);
                discountedPrice = (discountedPrice*(100 - ((1/(-pow(1.01, spentLastMonth))+1)*discount)))/100;
                break;
            }
        }
        return discountedPrice;
    }
    
    /**
     * Adds drinks to a specified menu type.
     */
    private void appendDrinks(){
        if(!drinksOrder.getItemList().isEmpty()){
            
            for(int i = 0; i < drinksOrder.getItemList().size(); i++){
                
                if(drinksOrder.getDrinkMenu().equals("Breakfast")){
                    breakfastOrder.getItemList().add(drinksOrder.getItemList().get(i));
                }
                if(drinksOrder.getDrinkMenu().equals("Lunch")){
                    lunchOrder.getItemList().add(drinksOrder.getItemList().get(i));
                }
                if(drinksOrder.getDrinkMenu().equals("Dinner")){
                    dinnerOrder.getItemList().add(drinksOrder.getItemList().get(i));
                }
            }
        }        
    }
    
    /**
     * Confirms the payment for the bank transfer.
     */
    private void jButtonConfirmPayment1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfirmPayment1ActionPerformed
        try {
            String[][] discounts = con.selectDiscount("", "");
            appendDrinks();
            addOrdersToDatabase(breakfastOrder, "Bank Transfer");
            addOrdersToDatabase(lunchOrder, "Bank Transfer");
            addOrdersToDatabase(dinnerOrder, "Bank Transfer");
            
            int numberOfOrders = 0;
            if(!breakfastOrder.getItemList().isEmpty()){
                numberOfOrders += 1;
            }
            if(!lunchOrder.getItemList().isEmpty()){
                numberOfOrders += 1;
            }
            if(!dinnerOrder.getItemList().isEmpty()){
                numberOfOrders += 1;
            }
                
            double totalAmount = applyDiscount(totalOrder.getTotalPrice());
            con.insertPayment(customer.getCustomerID(), "Bank Transfer", "", dF.format(totalAmount), Integer.toString(numberOfOrders));
            
            clearBasket();
            
            for(int i = 0; i<discounts.length; i++){
                        if(customer.getTotalSpent()>Double.parseDouble(discounts[i][3])){
                            customer.setVIPStatus(discounts[i][0]);
                            con.changeCustomer("VIP_Status", discounts[i][0], customer.getID());
                            if(customer.getPercentageDiscount().equals("None")){
                                customer.setPercentageDiscount("Fixed");
                                con.changeCustomer("Discount_Type", "Fixed", customer.getID());
                            }
                        }
            }
            
            resetSession("Bank Transfer");
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonConfirmPayment1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            switchQueuePanelDisplay("Breakfast");
        } catch (IOException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            switchQueuePanelDisplay("Lunch");
        } catch (IOException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            switchQueuePanelDisplay("Dinner");
        } catch (IOException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButtonBackToQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToQueueActionPerformed
        switchMainDisplayTo(jScrollPaneQueue);
    }//GEN-LAST:event_jButtonBackToQueueActionPerformed

    /**
     * Flags an order.
     */
    private void jButtonFlagOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFlagOrderActionPerformed
        try {
            con.changeOrder("Order_Status", orderToFlag.getID(), "Flagged");
            con.changeOrder("StaffID", orderToFlag.getID(), staff.getStaffID());
            con.changeOrder("Date_Flagged", orderToFlag.getID(), DF.format(Calendar.getInstance().getTime()));
            jButtonFlagOrder.setEnabled(false);
            jButtonSendNotification.setEnabled(true);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonFlagOrderActionPerformed
   
    public JButton getJButtonSendNotification(){
        return jButtonSendNotification;
    }
    
    private void jButtonBack2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBack2ActionPerformed
        switchMainDisplayTo(jScrollPaneSettings);
    }//GEN-LAST:event_jButtonBack2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        switchMainDisplayTo(jScrollPaneAddItem);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * Sends a notification to a specific account.
     */
    private void jButtonSendNotificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendNotificationActionPerformed
        try {
            con.changeOrder("Order_Status", orderToFlag.getID(), "Complete");
            String [][] b = con.selectCustomer("CustomerID", orderToFlag.getCustomerID(), "", false);
            con.changeCustomer("Notification", "Your order (" + orderToFlag.getID() + ") is ready", b[0][1]);
            
            String[][] managers = con.selectStaff("Staff_Type", "Manager", "", false);
            for(int i = 0; i<managers.length; i++){
                if(managers[i][5].contains(orderToFlag.getID())){
                    con.changeStaff("Notification", "None", managers[i][1]);
                    System.out.println("true");
                }                
            }
            
            String[][] a = con.selectOrder("OrderID", orderToFlag.getID(), "", false);
            Date dateFlagged = DF.parse(a[0][14]);
            Date nowDate = Calendar.getInstance().getTime();
            String actualTime = Long.toString((nowDate.getTime()-dateFlagged.getTime())/60000);
            con.changeOrder("Actual_Time", orderToFlag.getID(), actualTime);
            switchMainDisplayTo(jScrollPaneQueue);
            //Send notification.
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonSendNotificationActionPerformed

    /**
     * Backs up the system.
     */
    private void jButtonBackUpSystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackUpSystemActionPerformed
        try {
            con.createBackup();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonBackUpSystemActionPerformed

    /**
     * Restores the system.
     */
    private void jButtonRestoreSystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRestoreSystemActionPerformed
        try {
            con.restoreDatabase();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonRestoreSystemActionPerformed

    private void jCheckBoxDailySpecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDailySpecialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxDailySpecialActionPerformed

    private void jButtonDeleteSelectedItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteSelectedItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonDeleteSelectedItemActionPerformed

    private void jButtonAddStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddStockActionPerformed
        switchMainDisplayTo(jScrollPaneAddStock);
    }//GEN-LAST:event_jButtonAddStockActionPerformed

    /**
     * Makes an order.
     */
    private void jButtonMakeOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMakeOrderActionPerformed
        try {
            String[][] orderStock = con.selectStock("", "");
            
            for(int i = 0; i < orderStock.length; i++){
                if(Integer.parseInt(orderStock[i][2]) < Integer.parseInt(orderStock [i][3])){
                    con.changeStock("Quantity", orderStock[i][0], orderStock[i][4]);
                }
            }
            generateStockTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButtonMakeOrderActionPerformed

    private void jButtonDeleteSelectedStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteSelectedStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonDeleteSelectedStockActionPerformed

    private void jButtonAccountManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAccountManagementActionPerformed
        switchMainDisplayTo(jScrollPaneAccountManagement);
    }//GEN-LAST:event_jButtonAccountManagementActionPerformed

    private void jButtonBackToSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToSettingsActionPerformed
        switchMainDisplayTo(jScrollPaneSettings);
    }//GEN-LAST:event_jButtonBackToSettingsActionPerformed

    private void jButtonBackToAccountManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToAccountManagementActionPerformed
        switchMainDisplayTo(jScrollPaneAccountManagement);
    }//GEN-LAST:event_jButtonBackToAccountManagementActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        switchMainDisplayTo(jScrollPaneAddStaff);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButtonStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStaffActionPerformed
        try {
            generateStaffTable();
            switchMainDisplayTo(jScrollPaneEditStaffAccount);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonStaffActionPerformed

    private void jButtonAddToDatabase1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddToDatabase1ActionPerformed
        String staffType = null;

        if(jCheckBoxKitchen.isSelected()){
            staffType = "Kitchen";
        }
        else if(jCheckBoxWaiting.isSelected()){
            staffType = "Waiting";
        }
        else if(jCheckBoxManager.isSelected()){
            staffType = "Manager";
        }
        
        if(jPasswordPassword.getText().equals(jPasswordConfirmPassword.getText())){
            try {
                con.insertStaffRecord(jTextFieldFirstName.getText(), jTextFieldSurname.getText(),
                        staffType, jTextFieldUsername.getText(), jPasswordPassword.getText());
            } catch (SQLException ex) {
                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
            emptyFormFields("Staff");
            untickMenuCourseBoxes("Staff");
        }
    }//GEN-LAST:event_jButtonAddToDatabase1ActionPerformed

    private void jButtonBackToItems2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToItems2ActionPerformed
        try {
            generateStaffTable();
            switchMainDisplayTo(jScrollPaneEditStaffAccount);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonBackToItems2ActionPerformed

    private void jCheckBoxKitchenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxKitchenActionPerformed
        untickMenuCourseBoxes("Staff");
        jCheckBoxKitchen.setSelected(true);
    }//GEN-LAST:event_jCheckBoxKitchenActionPerformed

    private void jCheckBoxWaitingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxWaitingActionPerformed
        untickMenuCourseBoxes("Staff");
        jCheckBoxWaiting.setSelected(true);
    }//GEN-LAST:event_jCheckBoxWaitingActionPerformed

    private void jCheckBoxManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxManagerActionPerformed
        untickMenuCourseBoxes("Staff");
        jCheckBoxManager.setSelected(true);
    }//GEN-LAST:event_jCheckBoxManagerActionPerformed

    private void jButtonBackToAccountManagement1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToAccountManagement1ActionPerformed
        switchMainDisplayTo(jScrollPaneAccountManagement);
    }//GEN-LAST:event_jButtonBackToAccountManagement1ActionPerformed

    private void jButtonDeleteSelectedCustomerAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteSelectedCustomerAccountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonDeleteSelectedCustomerAccountActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            generateCustomerTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        switchMainDisplayTo(jScrollPaneEditCustomerAccount);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButtonDeleteSelectedStaffAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteSelectedStaffAccountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonDeleteSelectedStaffAccountActionPerformed

    private void jButtonBackToHistory1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToHistory1ActionPerformed
        switchMainDisplayTo(jScrollPaneHistory);
    }//GEN-LAST:event_jButtonBackToHistory1ActionPerformed

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
        try {
            generateCurrentLoggedInTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonRefreshActionPerformed

    private void jButtonSelectCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectCustomerActionPerformed
        switchMainDisplayTo(jScrollPanePendingOrders);
    }//GEN-LAST:event_jButtonSelectCustomerActionPerformed

    public JScrollPane getJScrollPanePendingOrders(){
        return jScrollPanePendingOrders;
    }
    
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            generateCurrentLoggedInTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        switchMainDisplayTo(jScrollPaneCurrentPending);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButtonBackToHistory2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToHistory2ActionPerformed
        switchMainDisplayTo(jScrollPaneCurrentPending);
    }//GEN-LAST:event_jButtonBackToHistory2ActionPerformed

    public JLabel getJLabelCustomerNameID(){
        return jLabelCustomerNameID;
    }
    
    private void jButtonRefresh2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefresh2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonRefresh2ActionPerformed

    private void jButtonBackToHistory3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToHistory3ActionPerformed
        switchMainDisplayTo(jScrollPaneHistory);
    }//GEN-LAST:event_jButtonBackToHistory3ActionPerformed

    private void jButtonGenerateCustomerSpendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateCustomerSpendingActionPerformed
        try {
            generateCustomerSpendingTable(jTextFieldDate1.getText(), jTextFieldDate2.getText());
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonGenerateCustomerSpendingActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        switchMainDisplayTo(jScrollPaneCustomerSpending);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButtonBackToHistory4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToHistory4ActionPerformed
        switchMainDisplayTo(jScrollPaneHistory);
    }//GEN-LAST:event_jButtonBackToHistory4ActionPerformed

    private void jButtonGenerateRefundReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateRefundReportActionPerformed
        try {
            generateRefundReportTable(jTextFieldDate3.getText(), jTextFieldDate4.getText());
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonGenerateRefundReportActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        switchMainDisplayTo(jScrollPaneRefundReport);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButtonBackToHistory5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToHistory5ActionPerformed
        switchMainDisplayTo(jScrollPaneHistory);
    }//GEN-LAST:event_jButtonBackToHistory5ActionPerformed

    private void jButtonGenerateOrdersReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateOrdersReportActionPerformed
        try {
            generateOrdersReportTable(jTextFieldDate5.getText(), jTextFieldDate6.getText());
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonGenerateOrdersReportActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        switchMainDisplayTo(jScrollPaneOrdersReport);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButtonPrintCustomerSpendingReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintCustomerSpendingReportActionPerformed

    }//GEN-LAST:event_jButtonPrintCustomerSpendingReportActionPerformed

    private void jButtonAddStockToItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddStockToItemActionPerformed
    }//GEN-LAST:event_jButtonAddStockToItemActionPerformed

    private void jButtonBack3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBack3ActionPerformed
        switchMainDisplayTo(jScrollPaneEditItems);
    }//GEN-LAST:event_jButtonBack3ActionPerformed

    private void jButtonAddStockItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddStockItem2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAddStockItem2ActionPerformed

    private void jButtonPrintPendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintPendingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrintPendingActionPerformed

    private void jButtonPrintRefundReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintRefundReportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrintRefundReportActionPerformed

    private void jButtonPrintOrdersReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintOrdersReportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrintOrdersReportActionPerformed

    private void jButtonBackToHistory6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToHistory6ActionPerformed
        switchMainDisplayTo(jScrollPaneHistory);
    }//GEN-LAST:event_jButtonBackToHistory6ActionPerformed

    private void jButtonRefresh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefresh1ActionPerformed
        try {
            generateStaffSelectionTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonRefresh1ActionPerformed

    private void jButtonSelectStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectStaffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSelectStaffActionPerformed

    /**
     * Relocates to the staff report.
     */
    private void jButtonStaffReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStaffReportActionPerformed
        try {
            if(globalUserType.equals("Staff")){
                generateStaffPerformanceTable(staff.getID(), staff.getFirstName());
                switchMainDisplayTo(jScrollPaneStaffPerformance);
            }
            else{
                generateStaffSelectionTable();
                switchMainDisplayTo(jScrollPaneStaffSelection);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonStaffReportActionPerformed

    private void jButtonBackToHistory7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToHistory7ActionPerformed
        switchMainDisplayTo(jScrollPaneStaffSelection);
    }//GEN-LAST:event_jButtonBackToHistory7ActionPerformed

    private void jButtonRefresh3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefresh3ActionPerformed
        try {
            generateStaffPerformanceTable(staffIDDisplay, staffNameDisplay);
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonRefresh3ActionPerformed

    private void jButtonPrintStaffPerformanceReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintStaffPerformanceReportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrintStaffPerformanceReportActionPerformed
    
    /**
     * Generates the staff selection table.
     */
    public void generateStaffSelectionTable() throws SQLException{
        StaffSelectionTable staffSelec = new StaffSelectionTable(con.selectStaff("", "", "", false), 
        con.selectStaffHeaders(), jButtonSelectStaff, this);
        jScrollPaneIntegratedStaffSelection.getViewport().add(staffSelec);
    }
    
    /**
     * Generates the stock item table.
     */
    public void generateStockItemTable(String itemID) throws SQLException{
        ItemStockTable itemStock = new ItemStockTable(con.selectStock("", ""), con.selectStockHeaders(), jButtonAddStockItem2, itemID);
        jScrollPaneIntegratedAddItemStock.getViewport().add(itemStock);
    }
    
    /**
     * Generates the orders report table.
     */
    public void generateOrdersReportTable(String date1, String date2) throws SQLException, ParseException{
        OrderReportTable orders = new OrderReportTable(ordersReport(date1, date2), 
                con.refundReportHeaders(), jButtonPrintOrdersReport);
        jScrollPaneIntegratedOrdersReport.getViewport().add(orders);
    }
    
    /**
     * Generates the refund report table.
     */
    public void generateRefundReportTable(String date1, String date2) throws SQLException, ParseException{
        RefundReportTable refund = new RefundReportTable(refundReport(date1, date2), 
                con.refundReportHeaders(), jButtonPrintRefundReport);
        jScrollPaneIntegratedRefundReport.getViewport().add(refund);
    }
    
    /**
     * Generates the currently logged in table.
     */
    public void generateCurrentLoggedInTable() throws SQLException, ParseException{
        CurrentlyLoggedInTable currentLoggedIn = new CurrentlyLoggedInTable(loggedInCustomersReport(), 
                con.selectLoggedInCustomerHeaders(), jButtonSelectCustomer, this);
        jScrollPaneIntegratedCurrentPending.getViewport().add(currentLoggedIn);
    }
    
    /**
     * Generates the pending orders table.
     */
    public void generatePendingOrdersTable(String customerID, String name) throws SQLException, ParseException{
        PendingOrdersTable pending = new PendingOrdersTable(pendingOrdersForLoggedInCustomersReport(customerID), 
                con.selectPendingOrderHeaders(), this, name, customerID, jButtonRefresh2, jButtonPrintPending);
        jScrollPaneIntegratedPendingOrders.getViewport().add(pending);
    }
    
    /**
     * Generates the staff performance table.
     */
    public void generateStaffPerformanceTable(String staffID, String name) throws SQLException, ParseException{
        StaffPerformanceTable performance = new StaffPerformanceTable(staffPerformanceReport(staffID, jTextFieldDate7.getText(), 
                jTextFieldDate8.getText()), con.selectStaffPerformanceHeaders(), jButtonPrintStaffPerformanceReport);
        jScrollPaneIntegratedStaffPerformance.getViewport().add(performance);
    }
    
    /**
     * Switches the display of the staff name and ID.
     */
    public void switchDisplayStaff(String staffID, String name){ 
        jLabelViewingFor.setText("Viewing For: " + name + " (ID: " + staffID + ")");
        staffNameDisplay = name;
        staffIDDisplay = staffID;
    }
    
    /**
     * Generates the customer spending table.
     */
    public void generateCustomerSpendingTable(String date1, String date2) throws SQLException, ParseException{
        CustomerSpendingTable spending = new CustomerSpendingTable(customerSpendingsReport(date1, date2), 
                con.customerSpendingHeaders(), jButtonPrintCustomerSpendingReport);
        jScrollPaneIntegratedCustomerSpending.getViewport().add(spending);
    }
    
    public JLabel getJLabelEditingFor(){
        return jLabelEditingFor;
    }
    
    
    /**
     * Generates the staff report table.
     */
    public String[][] staffReport() throws SQLException{
        String[][] staffArray = con.selectStaff("", "", "", false);
        String[][] reportData = new String[staffArray.length][4];
        for(int i = 0; i<staffArray.length; i++){
            reportData[i][0] = staffArray[i][0]; //  StaffID
            reportData[i][1] = staffArray[i][2]; //  First_Name
            reportData[i][2] = staffArray[i][3]; //  Surname
            reportData[i][3] = staffArray[i][4]; //  Staff_Type 
        } 
        return reportData;
    }
    
    public JScrollPane getJScrollPaneStaffPerformance(){
        return jScrollPaneStaffPerformance;
    }
    
    /**
     * Generates the staff performance report.
     */
    public String[][] staffPerformanceReport(String staffID, String Date1, String Date2) throws ParseException, SQLException{
        String[][] ordersArray = con.selectOrder("Order_Date", Date1, Date2, true);
        int[][] counters = {{0,0},{0,0},{0,0}};
        String[][] reportData = new String[3][3];
        
        for(int i = 0; i<ordersArray.length; i++){
            if(ordersArray[i][1].equals(staffID)){
                if(ordersArray[i][7].equals("Complete")){
                    if(ordersArray[i][10].equals("Breakfast")){
                        if(!ordersArray[i][11].equals(null)){
                            counters[0][0] += Integer.parseInt(ordersArray[i][11]);
                        }
                        if(!ordersArray[i][13].equals(null)){
                            counters[0][1] += Integer.parseInt(ordersArray[i][13]);
                        }
                    }
                    if(ordersArray[i][10].equals("Lunch")){
                        if(!ordersArray[i][11].equals(null)){
                            counters[1][0] += Integer.parseInt(ordersArray[i][11]);
                        }
                        if(!ordersArray[i][13].equals(null)){
                            counters[1][1] += Integer.parseInt(ordersArray[i][13]);
                        }
                    }
                    if(ordersArray[i][10].equals("Dinner")){
                        if(!ordersArray[i][11].equals(null)){
                            counters[2][0] += Integer.parseInt(ordersArray[i][11]);
                        }
                        if(!ordersArray[i][13].equals(null)){
                            counters[2][1] += Integer.parseInt(ordersArray[i][13]);
                        }
                    }
                }
            }
        } 
     
        reportData[0][0] = "Breakfast";
        reportData[0][1] = Integer.toString(counters[0][0]);
        reportData[0][2] = Integer.toString(counters[0][1]);
        reportData[1][0] = "Lunch";
        reportData[1][1] = Integer.toString(counters[1][0]);
        reportData[1][2] = Integer.toString(counters[1][1]);
        reportData[2][0] = "Dinner";
        reportData[2][1] = Integer.toString(counters[2][0]);
        reportData[2][2] = Integer.toString(counters[2][1]);
        
        return reportData;
    }
    
    /**
     * Adds orders to the database.
     */
    private void addOrdersToDatabase(Order sessionOrder, String paymentType) throws ParseException, SQLException{ 
        ArrayList<String[]> items = new ArrayList<>();
        
        if(!sessionOrder.getItemList().isEmpty()){
            String priority = "0";
            
            if(sessionOrder.getIsPriority()){
                priority = "1";
            }
            
            for(int i = 0; i < sessionOrder.getItemList().size(); i++){
                int quantity = 0;
                int y = -1;
                boolean isfirst = false;
                for(int x = 0; x <= i; x++){
                    if(sessionOrder.getItemList().get(x).getID().equals(sessionOrder.getItemList().get(i).getID())){
                        for(int z = 0; z<=items.size(); z++){
                            if(!isfirst){
                                y = z;
                            }
                        }
                        quantity++;
                    }
                }
                if(quantity == 1){
                    String[] a = {sessionOrder.getItemList().get(i).getID(), Integer.toString(quantity)};
                    items.add(a);
                } 
                else{
                    if(y!=-1){
                        items.get(y-1)[1] = Integer.toString(quantity);
                    }
                }
                
            }
            
            String totalSpent = dF.format(applyDiscount(sessionOrder.getTotalPrice()));
            double totalSpent2 = Double.parseDouble(dF.format(applyDiscount(sessionOrder.getTotalPrice())));
                con.insertOrder(customer.getCustomerID(), dF1.format(Calendar.getInstance().getTime()), 
                dF2.format(Calendar.getInstance().getTime()), totalSpent, 
                "0", "Incomplete", priority, sessionOrder.getMenuType(), 
                Integer.toString(sessionOrder.getTotalPrepTime()), paymentType, (String[][]) items.toArray(new String[items.size()][2]));
                con.changeCustomer("Total_Spent", Double.toString(customer.getTotalSpent() + totalSpent2), 
                        customer.getID());
                customer.setTotalSpent(customer.getTotalSpent() + totalSpent2);
        }
    }
    
    /**
     * Resets the session.
     */
    private void resetSession(String paymentType){
        totalOrder.getItemList().clear();
        breakfastOrder.getItemList().clear();
        lunchOrder.getItemList().clear();
        dinnerOrder.getItemList().clear();
        drinksOrder.getItemList().clear();
        switchMainDisplayTo(jScrollPaneHome);
        emptyFormFields(paymentType);
    }
    
    /**
     * Switches the integrated payment panel.
     */
    private void switchIntegratedPaymentPanel(JScrollPane newPane){
        jPanelIntegratedPayment.removeAll();
        jPanelIntegratedPayment.repaint();
        jPanelIntegratedPayment.revalidate();
        jPanelIntegratedPayment.add(newPane);
        jPanelIntegratedPayment.repaint();
        jPanelIntegratedPayment.revalidate();
    }
    
    /**
     * Unticks the menu course boxes.
     */
    private void untickMenuCourseBoxes(String menuCourse){
        switch (menuCourse) {
            case "Menu":
                jCheckBoxBreakfast.setSelected(false);
                jCheckBoxLunch.setSelected(false);
                jCheckBoxDinner.setSelected(false);
                jCheckBoxDrinks.setSelected(false);
                jCheckBoxDailySpecial.setSelected(false);
                break;
            case "Course":
                jCheckBoxStarter.setSelected(false);
                jCheckBoxMain.setSelected(false);
                jCheckBoxDessert.setSelected(false);
                break;
            case "Staff":
                jCheckBoxWaiting.setSelected(false);
                jCheckBoxKitchen.setSelected(false);
                jCheckBoxManager.setSelected(false);
        }
    }
    
    /**
     * Empties the form fields.
     */
    private void emptyFormFields(String form){
        switch (form) {
            case "Add Item":
                untickMenuCourseBoxes("Menu");
                untickMenuCourseBoxes("Course");
                showCourseType(false);
                jTextFieldName.setText("");
                jTextFieldPreparationTime.setText("");
                jTextFieldPrice.setText("");
                jTextFieldDescription.setText("");
                break;
            case "Add Stock":
                jTextFieldName1.setText("");
                jTextFieldQuantity.setText("");
                jTextFieldMin.setText("");
                jTextFieldMax.setText("");
                break;
            case "Bank Transfer":
                jTextFieldSortCode.setText("");
                jTextFieldNameHolder.setText("");
                jTextFieldAccountNumber.setText("");
                jTextFieldBankName.setText("");
                break;
            case "Card Payment":
                jPasswordSecurityNumber.setText("");
            case "Staff":
                jTextFieldUsername.setText("");
                jPasswordPassword.setText("");
                jPasswordConfirmPassword.setText("");
                jTextFieldFirstName.setText("");
                jTextFieldSurname.setText("");
                break;
        }
    }
    
    /**
     * Shows the course type.
     */
    private void showCourseType(boolean isShown){
        if(isShown){
            jLabelChooseCourse.setVisible(true);
            jCheckBoxStarter.setVisible(true);
            jCheckBoxMain.setVisible(true);
            jCheckBoxDessert.setVisible(true);
        }
        else{
            jLabelChooseCourse.setVisible(false);
            jCheckBoxStarter.setVisible(false);
            jCheckBoxMain.setVisible(false);
            jCheckBoxDessert.setVisible(false);
        }
    }
    
    /**
     * Switches main display to a new scroll pane.
     */
    public void switchMainDisplayTo(JScrollPane newPane){
        jPanelMainDisplay.removeAll();
        jPanelMainDisplay.repaint();
        jPanelMainDisplay.revalidate();
        jPanelMainDisplay.add(newPane);
        jPanelMainDisplay.repaint();
        jPanelMainDisplay.revalidate();
    }
    
    public JPanel getJPanelMainDisplay(){
        return jPanelMainDisplay;
    }
    
    public JScrollPane getJScrollPaneHome(){
        return jScrollPaneHome;
    }
    
    /**
     * Returns the current date and time and updates it every second.
     */
    public final void currentDateTime(){
        Thread clockTicker;
        clockTicker = new Thread(){
            @Override
            public void run(){
                for(;;){                                    
                    try {                                    
                        jLabelDate.setText(dF1.format(Calendar.getInstance().getTime()));
                        jLabelTime.setText(dF2.format(Calendar.getInstance().getTime()));
                        menuEnabler();
                        
                        try {
                            sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        
        clockTicker.start();
    }

    public Order getOrder() {
        return totalOrder;
    }

    public Customer getCustomer() {
        return customer;
    }
    
    public JButton getBasketButton(){
        return jButtonBasket;
    }
    
    public JScrollPane getJScrollPaneViewOrder(){
        return jScrollPaneViewOrder;
    }
    
    public JLabel getJLabelOrderNoOrder(){
        return jLabelOrderNoOrder;
    }
    
    public JLabel getJLabelTotalPriceOrder(){
        return jLabelTotalPriceOrder;
    }
    
    public JLabel getJLabelCustomerIDOrder(){
        return jLabelCustomerIDOrder;
    }
    
    public JLabel getJLabelPreparationTimeOrder(){
        return jLabelPreparationTimeOrder;
    }
    
    public JLabel getJLabelNoOfItems(){
        return jLabelNoOfItemsOrder;
    }
    
    public JLabel getJLabelStatusOrder(){
        return jLabelStatusOrder;
    }
    
    public JTextArea getJTextAreaItemsInOrder(){
        return jTextAreaItemsInOrder;
    }

    public Order getOrderToFlag() {
        return orderToFlag;
    }
       
    public void setOrderToFlag(Order orderToFlag){
        this.orderToFlag = orderToFlag;
    }
    
    public JButton getJButtonFlagOrder(){
        return jButtonFlagOrder;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonAccountManagement;
    private javax.swing.JButton jButtonAddNewCard;
    private javax.swing.JButton jButtonAddStock;
    private javax.swing.JButton jButtonAddStockItem2;
    private javax.swing.JButton jButtonAddStockToDatabase;
    private javax.swing.JButton jButtonAddStockToItem;
    private javax.swing.JButton jButtonAddToDatabase;
    private javax.swing.JButton jButtonAddToDatabase1;
    private javax.swing.JButton jButtonBack2;
    private javax.swing.JButton jButtonBack3;
    private javax.swing.JButton jButtonBackToAccountManagement;
    private javax.swing.JButton jButtonBackToAccountManagement1;
    private javax.swing.JButton jButtonBackToBasket;
    private javax.swing.JButton jButtonBackToHistory1;
    private javax.swing.JButton jButtonBackToHistory2;
    private javax.swing.JButton jButtonBackToHistory3;
    private javax.swing.JButton jButtonBackToHistory4;
    private javax.swing.JButton jButtonBackToHistory5;
    private javax.swing.JButton jButtonBackToHistory6;
    private javax.swing.JButton jButtonBackToHistory7;
    private javax.swing.JButton jButtonBackToItems1;
    private javax.swing.JButton jButtonBackToItems2;
    private javax.swing.JButton jButtonBackToQueue;
    private javax.swing.JButton jButtonBackToSettings;
    private javax.swing.JButton jButtonBackToStock1;
    private javax.swing.JButton jButtonBackToStock3;
    private javax.swing.JButton jButtonBackUpSystem;
    private javax.swing.JButton jButtonBankTransfer;
    private javax.swing.JButton jButtonBasket;
    private javax.swing.JButton jButtonBreakfast;
    private javax.swing.JButton jButtonCheckout;
    private javax.swing.JButton jButtonClearBasket;
    private javax.swing.JButton jButtonConfirmPayment;
    private javax.swing.JButton jButtonConfirmPayment1;
    private javax.swing.JButton jButtonDeleteSelectedCustomerAccount;
    private javax.swing.JButton jButtonDeleteSelectedItem;
    private javax.swing.JButton jButtonDeleteSelectedStaffAccount;
    private javax.swing.JButton jButtonDeleteSelectedStock;
    private javax.swing.JButton jButtonDinner;
    private javax.swing.JButton jButtonDrinks;
    private javax.swing.JButton jButtonFlagOrder;
    private javax.swing.JButton jButtonGenerateCustomerSpending;
    private javax.swing.JButton jButtonGenerateOrdersReport;
    private javax.swing.JButton jButtonGenerateRefundReport;
    private javax.swing.JButton jButtonHistory;
    private javax.swing.JButton jButtonHome;
    private javax.swing.JButton jButtonItems;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonLunch;
    private javax.swing.JButton jButtonMakeOrder;
    private javax.swing.JButton jButtonMenu;
    private javax.swing.JButton jButtonPrintCustomerSpendingReport;
    private javax.swing.JButton jButtonPrintOrdersReport;
    private javax.swing.JButton jButtonPrintPending;
    private javax.swing.JButton jButtonPrintRefundReport;
    private javax.swing.JButton jButtonPrintStaffPerformanceReport;
    private javax.swing.JButton jButtonProceedToPayment;
    private javax.swing.JButton jButtonQueue;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JButton jButtonRefresh1;
    private javax.swing.JButton jButtonRefresh2;
    private javax.swing.JButton jButtonRefresh3;
    private javax.swing.JButton jButtonRestoreSystem;
    private javax.swing.JButton jButtonSelectCustomer;
    private javax.swing.JButton jButtonSelectStaff;
    private javax.swing.JButton jButtonSendNotification;
    private javax.swing.JButton jButtonSettings;
    private javax.swing.JButton jButtonStaff;
    private javax.swing.JButton jButtonStaffReport;
    private javax.swing.JButton jButtonStock;
    private javax.swing.JButton jButtonUpdateStockInDatabase;
    private javax.swing.JButton jButtonUseExistingCard;
    private javax.swing.JCheckBox jCheckBoxBreakfast;
    private javax.swing.JCheckBox jCheckBoxDailySpecial;
    private javax.swing.JCheckBox jCheckBoxDessert;
    private javax.swing.JCheckBox jCheckBoxDinner;
    private javax.swing.JCheckBox jCheckBoxDrinks;
    private javax.swing.JCheckBox jCheckBoxKitchen;
    private javax.swing.JCheckBox jCheckBoxLunch;
    private javax.swing.JCheckBox jCheckBoxMain;
    private javax.swing.JCheckBox jCheckBoxManager;
    private javax.swing.JCheckBox jCheckBoxStarter;
    private javax.swing.JCheckBox jCheckBoxWaiting;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAccountManagementTitle;
    private javax.swing.JLabel jLabelAddItemStock;
    private javax.swing.JLabel jLabelAddItemTitle;
    private javax.swing.JLabel jLabelAddStaffTitle;
    private javax.swing.JLabel jLabelAddStockTitle;
    private javax.swing.JLabel jLabelBasketTitle;
    private javax.swing.JLabel jLabelBreakfast;
    private javax.swing.JLabel jLabelBreakfast2;
    private javax.swing.JLabel jLabelCardNameHolder;
    private javax.swing.JLabel jLabelCardNumber;
    private javax.swing.JLabel jLabelCardType;
    private javax.swing.JLabel jLabelChickCafeLogo;
    private javax.swing.JLabel jLabelChooseCourse;
    private javax.swing.JLabel jLabelChooseStock1;
    private javax.swing.JLabel jLabelCurrentPending1;
    private javax.swing.JLabel jLabelCurrentPending2;
    private javax.swing.JLabel jLabelCustomerIDOrder;
    private javax.swing.JLabel jLabelCustomerNameID;
    private javax.swing.JLabel jLabelCustomerSpending;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelDescription1;
    private javax.swing.JLabel jLabelDinner;
    private javax.swing.JLabel jLabelDinner2;
    private javax.swing.JLabel jLabelDiscountsApplied;
    private javax.swing.JLabel jLabelDiscountsApplied1;
    private javax.swing.JLabel jLabelDrinks;
    private javax.swing.JLabel jLabelDrinks2;
    private javax.swing.JLabel jLabelEditCustomerAccount;
    private javax.swing.JLabel jLabelEditItemsTitle;
    private javax.swing.JLabel jLabelEditItemsTitle1;
    private javax.swing.JLabel jLabelEditStaffAccount;
    private javax.swing.JLabel jLabelEditingFor;
    private javax.swing.JLabel jLabelExpirationDate;
    private javax.swing.JLabel jLabelFenixSystems;
    private javax.swing.JLabel jLabelFillDetails;
    private javax.swing.JLabel jLabelHistoryTitle;
    private javax.swing.JLabel jLabelHomeTitle;
    private javax.swing.JLabel jLabelIsCredit;
    private javax.swing.JLabel jLabelLoggedInAs;
    private javax.swing.JLabel jLabelLunch;
    private javax.swing.JLabel jLabelLunch2;
    private javax.swing.JLabel jLabelMenuTitle;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelName1;
    private javax.swing.JLabel jLabelName2;
    private javax.swing.JLabel jLabelName3;
    private javax.swing.JLabel jLabelName4;
    private javax.swing.JLabel jLabelName5;
    private javax.swing.JLabel jLabelNoOfItemsOrder;
    private javax.swing.JLabel jLabelOrderNoOrder;
    private javax.swing.JLabel jLabelOrdersReport;
    private javax.swing.JLabel jLabelPayment;
    private javax.swing.JLabel jLabelPendingOrders;
    private javax.swing.JLabel jLabelPendingOrders1;
    private javax.swing.JLabel jLabelPreparationTime;
    private javax.swing.JLabel jLabelPreparationTime1;
    private javax.swing.JLabel jLabelPreparationTime2;
    private javax.swing.JLabel jLabelPreparationTimeOrder;
    private javax.swing.JLabel jLabelPrice;
    private javax.swing.JLabel jLabelPrice1;
    private javax.swing.JLabel jLabelQueueTitle;
    private javax.swing.JLabel jLabelRefundReport;
    private javax.swing.JLabel jLabelSelectPaymentMethod;
    private javax.swing.JLabel jLabelSettingsTitle;
    private javax.swing.JLabel jLabelStaffSelection;
    private javax.swing.JLabel jLabelStatusOrder;
    private javax.swing.JLabel jLabelTime;
    private javax.swing.JLabel jLabelTotalPrice;
    private javax.swing.JLabel jLabelTotalPrice1;
    private javax.swing.JLabel jLabelTotalPriceOrder;
    private javax.swing.JLabel jLabelUpdateQuantityTitle;
    private javax.swing.JLabel jLabelViewOrderTitle;
    private javax.swing.JLabel jLabelViewingFor;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelAccountManagement;
    private javax.swing.JPanel jPanelAddItem;
    private javax.swing.JPanel jPanelAddItemStock;
    private javax.swing.JPanel jPanelAddStaff;
    private javax.swing.JPanel jPanelAddStock;
    private javax.swing.JPanel jPanelBankTransfer;
    private javax.swing.JPanel jPanelBasket;
    private javax.swing.JPanel jPanelBreakfastMenu;
    private javax.swing.JPanel jPanelBreakfastQueue;
    private javax.swing.JPanel jPanelCard;
    private javax.swing.JPanel jPanelCurrentPending;
    private javax.swing.JPanel jPanelCustomerSpending;
    private javax.swing.JPanel jPanelDeleteStock1;
    private javax.swing.JPanel jPanelDinnerMenu;
    private javax.swing.JPanel jPanelDinnerQueue;
    private javax.swing.JPanel jPanelDrinksMenu;
    private javax.swing.JPanel jPanelEditCustomerAccount;
    private javax.swing.JPanel jPanelEditItems;
    private javax.swing.JPanel jPanelEditStaffAccount;
    private javax.swing.JPanel jPanelEditStock;
    private javax.swing.JPanel jPanelHistory;
    private javax.swing.JPanel jPanelHome;
    private javax.swing.JPanel jPanelInformationBar;
    private javax.swing.JPanel jPanelIntegratedMenu;
    private javax.swing.JPanel jPanelIntegratedPayment;
    private javax.swing.JPanel jPanelIntegratedQueue;
    private javax.swing.JPanel jPanelLunchMenu;
    private javax.swing.JPanel jPanelLunchQueue;
    private javax.swing.JPanel jPanelMainDisplay;
    private javax.swing.JPanel jPanelMainFrame;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JPanel jPanelOrdersReport;
    private javax.swing.JPanel jPanelPayment;
    private javax.swing.JPanel jPanelPendingOrders;
    private javax.swing.JPanel jPanelQueue;
    private javax.swing.JPanel jPanelQueueSelection;
    private javax.swing.JPanel jPanelRefundReport;
    private javax.swing.JPanel jPanelSettings;
    private javax.swing.JPanel jPanelSideBar;
    private javax.swing.JPanel jPanelStaffPerformance;
    private javax.swing.JPanel jPanelStaffSelection;
    private javax.swing.JPanel jPanelUpperBar;
    private javax.swing.JPanel jPanelViewOrder;
    private javax.swing.JPasswordField jPasswordConfirmPassword;
    private javax.swing.JPasswordField jPasswordPassword;
    private javax.swing.JPasswordField jPasswordSecurityNumber;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPaneAccountManagement;
    private javax.swing.JScrollPane jScrollPaneAddItem;
    private javax.swing.JScrollPane jScrollPaneAddItemStock;
    private javax.swing.JScrollPane jScrollPaneAddStaff;
    private javax.swing.JScrollPane jScrollPaneAddStock;
    private javax.swing.JScrollPane jScrollPaneBankTransfer;
    private javax.swing.JScrollPane jScrollPaneBasket;
    private javax.swing.JScrollPane jScrollPaneBreakfastMenu;
    private javax.swing.JScrollPane jScrollPaneBreakfastQueue;
    private javax.swing.JScrollPane jScrollPaneCard;
    private javax.swing.JScrollPane jScrollPaneCurrentPending;
    private javax.swing.JScrollPane jScrollPaneCustomerSpending;
    private javax.swing.JScrollPane jScrollPaneDinnerMenu;
    private javax.swing.JScrollPane jScrollPaneDinnerQueue;
    private javax.swing.JScrollPane jScrollPaneDrinksMenu;
    private javax.swing.JScrollPane jScrollPaneEditCustomerAccount;
    private javax.swing.JScrollPane jScrollPaneEditItems;
    private javax.swing.JScrollPane jScrollPaneEditStaffAccount;
    private javax.swing.JScrollPane jScrollPaneEditStock;
    private javax.swing.JScrollPane jScrollPaneHistory;
    private javax.swing.JScrollPane jScrollPaneHome;
    private javax.swing.JScrollPane jScrollPaneIntegratedAddItemStock;
    private javax.swing.JScrollPane jScrollPaneIntegratedCurrentPending;
    private javax.swing.JScrollPane jScrollPaneIntegratedCustomerSpending;
    private javax.swing.JScrollPane jScrollPaneIntegratedEditCustomerAccount;
    private javax.swing.JScrollPane jScrollPaneIntegratedEditItems;
    private javax.swing.JScrollPane jScrollPaneIntegratedEditStaffAccount;
    private javax.swing.JScrollPane jScrollPaneIntegratedEditStock;
    private javax.swing.JScrollPane jScrollPaneIntegratedOrdersReport;
    private javax.swing.JScrollPane jScrollPaneIntegratedPendingOrders;
    private javax.swing.JScrollPane jScrollPaneIntegratedRefundReport;
    private javax.swing.JScrollPane jScrollPaneIntegratedStaffPerformance;
    private javax.swing.JScrollPane jScrollPaneIntegratedStaffSelection;
    private javax.swing.JScrollPane jScrollPaneLunchMenu;
    private javax.swing.JScrollPane jScrollPaneLunchQueue;
    private javax.swing.JScrollPane jScrollPaneMenu;
    private javax.swing.JScrollPane jScrollPaneOrdersReport;
    private javax.swing.JScrollPane jScrollPanePayment;
    private javax.swing.JScrollPane jScrollPanePendingOrders;
    private javax.swing.JScrollPane jScrollPaneQueue;
    private javax.swing.JScrollPane jScrollPaneRefundReport;
    private javax.swing.JScrollPane jScrollPaneSettings;
    private javax.swing.JScrollPane jScrollPaneStaffPerformance;
    private javax.swing.JScrollPane jScrollPaneStaffSelection;
    private javax.swing.JScrollPane jScrollPaneUpdateStockQuantity;
    private javax.swing.JScrollPane jScrollPaneViewOrder;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextAreaItemsInOrder;
    private javax.swing.JTextArea jTextAreaStockDisplay2;
    private javax.swing.JTextField jTextFieldAccountNumber;
    private javax.swing.JTextField jTextFieldBankName;
    private javax.swing.JTextField jTextFieldDate1;
    private javax.swing.JTextField jTextFieldDate2;
    private javax.swing.JTextField jTextFieldDate3;
    private javax.swing.JTextField jTextFieldDate4;
    private javax.swing.JTextField jTextFieldDate5;
    private javax.swing.JTextField jTextFieldDate6;
    private javax.swing.JTextField jTextFieldDate7;
    private javax.swing.JTextField jTextFieldDate8;
    private javax.swing.JTextField jTextFieldDescription;
    private javax.swing.JTextField jTextFieldFirstName;
    private javax.swing.JTextField jTextFieldMax;
    private javax.swing.JTextField jTextFieldMin;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldName1;
    private javax.swing.JTextField jTextFieldNameHolder;
    private javax.swing.JTextField jTextFieldPreparationTime;
    private javax.swing.JTextField jTextFieldPrice;
    private javax.swing.JTextField jTextFieldQuantity;
    private javax.swing.JTextField jTextFieldSortCode;
    private javax.swing.JTextField jTextFieldStockName;
    private javax.swing.JTextField jTextFieldStockQuantityToAdd;
    private javax.swing.JTextField jTextFieldSurname;
    private javax.swing.JTextField jTextFieldUsername;
    // End of variables declaration//GEN-END:variables

}
