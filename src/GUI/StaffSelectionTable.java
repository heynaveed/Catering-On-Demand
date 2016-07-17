package GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTable;

public class StaffSelectionTable extends JTable implements ActionListener {

    private final JButton selectStaff;
    private final MainDashboard dash;
    private JTable table;
    private static final Color panelColor = new Color(199, 159, 79);
    
    public StaffSelectionTable(String[][] data, String[] headers, JButton selectStaff, MainDashboard dash){
        super(data, headers);
        this.selectStaff = selectStaff;
        this.dash = dash;
        this.setBackground(panelColor);
        setDragEnabled(false);
        doLayout();
        selectStaff.addActionListener(this);
        table = this;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        dash.switchMainDisplayTo(dash.getJScrollPaneStaffPerformance());
        dash.switchDisplayStaff(table.getValueAt(table.getSelectedRow(), 0).toString(),
                table.getValueAt(table.getSelectedRow(), 2).toString());
        try {
            dash.generateStaffPerformanceTable(table.getValueAt(table.getSelectedRow(), 0).toString(),
                    table.getValueAt(table.getSelectedRow(), 2).toString());
        } catch (SQLException ex) {
            Logger.getLogger(StaffSelectionTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(StaffSelectionTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
