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

public class EditStockTable extends JTable implements TableModelListener, ActionListener {
    
    private JTable table;
    private DBConnect con;
    private final JButton delete;
    private final MainDashboard dash;
    private static final Color panelColor = new Color(199, 159, 79);
    
    public EditStockTable(String[][] data, String[] headers, JButton delete,
            MainDashboard dash) throws SQLException{
        super(data, headers);
        setDragEnabled(false);
        doLayout();
        this.setBackground(panelColor);
        getColumnModel().getColumn(0).setPreferredWidth(35);
        getColumnModel().getColumn(1).setPreferredWidth(120);
        getColumnModel().getColumn(2).setPreferredWidth(40);
        getColumnModel().getColumn(3).setPreferredWidth(90);
        getColumnModel().getColumn(4).setPreferredWidth(90);
        
        con = new DBConnect();
        table = this;
        this.delete = delete;
        this.dash = dash;
        delete.addActionListener(this);
                
        getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                
                try {
                    con.changeStock(table.getColumnName(table.getSelectedColumn()), table.getValueAt(
                            table.getSelectedRow(), 0).toString(), table.getValueAt(table.getSelectedRow(),
                                table.getSelectedColumn()).toString());
                } catch (SQLException ex) {
                    Logger.getLogger(EditStockTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /**
     * Deletes stock from database.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            con.deleteStock(table.getValueAt(table.getSelectedRow(), 0).toString());
            try {
                dash.generateStockTable();
            } catch (SQLException ex) {
                Logger.getLogger(EditStockTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditStockTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
