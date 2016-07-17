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

public class EditItemTable extends JTable implements TableModelListener, ActionListener {
    
    private JTable table;
    private DBConnect con;
    private final JButton delete;
    private final MainDashboard dash;
    private final JButton addStock;
    private static final Color panelColor = new Color(199, 159, 79);
    
    public EditItemTable(String[][] data, String[] headers, JButton delete,
            MainDashboard dash, JButton addStock) throws SQLException{
        super(data, headers);
        setDragEnabled(false);
        this.setBackground(panelColor);
        removeColumn(getColumnModel().getColumn(5));
        removeColumn(getColumnModel().getColumn(2));
        removeColumn(getColumnModel().getColumn(6));
        doLayout();
        getColumnModel().getColumn(1).setPreferredWidth(145);
        getColumnModel().getColumn(3).setPreferredWidth(115);
        getColumnModel().getColumn(5).setPreferredWidth(65);
        
        con = new DBConnect();
        table = this;
        this.delete = delete;
        this.addStock = addStock;
        this.dash = dash;
        delete.addActionListener(this);
        addStock.addActionListener(this);
                
        getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                
                try {
                    String value = null;
                    
                    if(table.getValueAt(table.getSelectedRow(),
                            table.getSelectedColumn()).toString().equals("Yes")){
                        value = "1";
                    }
                    else if(table.getValueAt(table.getSelectedRow(),
                            table.getSelectedColumn()).toString().equals("No")){
                        value = "0";
                    }
                    else{
                        value = table.getValueAt(table.getSelectedRow(),
                                table.getSelectedColumn()).toString();
                    }
                    
                    con.changeItem(table.getColumnName(table.getSelectedColumn()), table.getValueAt(
                            table.getSelectedRow(), 1).toString(), value);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(EditItemTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /**
     * Adds stocks or deletes them from database.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource().equals(delete)){
            try {
                con.deleteItem(table.getValueAt(table.getSelectedRow(), 1).toString());
                try {
                    dash.generateItemTable();
                } catch (SQLException ex) {
                    Logger.getLogger(EditItemTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(EditItemTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        else if(e.getSource().equals(addStock)){
            dash.switchMainDisplayTo(dash.getJScrollPaneAddItemStock());
            try {
                dash.generateStockItemTable(table.getValueAt(table.getSelectedRow(), 0).toString());
                dash.getJLabelEditingFor().setText("Editing For: " + table.getValueAt(table.getSelectedRow(), 1).toString());
            } catch (SQLException ex) {
                Logger.getLogger(EditItemTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
