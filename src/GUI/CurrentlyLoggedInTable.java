package GUI;

import Database.DBConnect;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTable;

public class CurrentlyLoggedInTable extends JTable implements ActionListener {
    
    private JTable table;
    private DBConnect con;
    private final JButton selectCustomer;
    private final MainDashboard dash;
    private static final Color panelColor = new Color(199, 159, 79);
    
    public CurrentlyLoggedInTable(String[][] data, String[] headers, JButton selectCustomer,
            MainDashboard dash) throws SQLException{
        super(data, headers);
        setDragEnabled(false);
        this.setBackground(panelColor);
        //removeColumn(getColumnModel().getColumn(0));
        
        doLayout();
        //getColumnModel().getColumn(0).setPreferredWidth(145);
        con = new DBConnect();
        table = this;
        this.selectCustomer = selectCustomer;
        this.dash = dash;
        selectCustomer.addActionListener(this);
    }

    /**
     * Switches the main display to generate a orders report.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            dash.switchMainDisplayTo(dash.getJScrollPanePendingOrders());
            dash.generatePendingOrdersTable(table.getValueAt(table.getSelectedRow(), 0).toString(), 
                    table.getValueAt(table.getSelectedRow(), 1).toString());
        } catch (SQLException ex) {
            Logger.getLogger(CurrentlyLoggedInTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CurrentlyLoggedInTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
