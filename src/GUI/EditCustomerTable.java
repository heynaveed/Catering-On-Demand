package GUI;

import Database.DBConnect;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class EditCustomerTable extends JTable implements TableModelListener, ActionListener {
    
    private JTable table;
    private DBConnect con;
    private final JButton delete;
    private final MainDashboard dash;
    private static final Color panelColor = new Color(199, 159, 79);
    
    public EditCustomerTable(String[][] data, String[] headers, JButton delete,
            MainDashboard dash) throws SQLException{
        super(data, headers);
        setDragEnabled(false);
        this.setBackground(panelColor);
        removeColumn(getColumnModel().getColumn(0));
        removeColumn(getColumnModel().getColumn(2));
        removeColumn(getColumnModel().getColumn(3));
        removeColumn(getColumnModel().getColumn(6));
        removeColumn(getColumnModel().getColumn(6));
        removeColumn(getColumnModel().getColumn(6));
        removeColumn(getColumnModel().getColumn(6));
        removeColumn(getColumnModel().getColumn(6));
        removeColumn(getColumnModel().getColumn(6));
        removeColumn(getColumnModel().getColumn(6));
        doLayout();
        getColumnModel().getColumn(0).setPreferredWidth(45);
        getColumnModel().getColumn(1).setPreferredWidth(60);
        getColumnModel().getColumn(3).setPreferredWidth(63);
        getColumnModel().getColumn(4).setPreferredWidth(56);
        
        con = new DBConnect();
        table = this;
        this.delete = delete;
        this.dash = dash;
        delete.addActionListener(this);
                
        getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                
                try {
                    con.changeCustomer(table.getColumnName(table.getSelectedColumn()), table.getValueAt(table.getSelectedRow(),
                                table.getSelectedColumn()).toString(), table.getValueAt(
                            table.getSelectedRow(), 0).toString());
                } catch (SQLException ex) {
                    Logger.getLogger(EditItemTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /**
     * Generates the customer table.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            con.deleteCustomer(table.getValueAt(table.getSelectedRow(), 0).toString());
            try {
                dash.generateCustomerTable();
            } catch (SQLException ex) {
                //Logger.getLogger(EditItemTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(EditItemTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
