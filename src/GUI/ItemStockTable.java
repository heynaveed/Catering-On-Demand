package GUI;

import Database.DBConnect;
import Ordering.Stock;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ItemStockTable extends JTable implements ActionListener {
    
    private final ArrayList<Stock> stock;
    private final JTable table;
    private final JButton addStockItem;
    private final JCheckBox box;
    private final DBConnect con;
    private final String itemID;
    private static final Color panelColor = new Color(199, 159, 79);
    
    public ItemStockTable(String[][] data, String[] headers, JButton addStockItem, String itemID) throws SQLException{
        super(data, headers);
        stock = new ArrayList<>();
        table = this;
        this.itemID = itemID;
        this.addStockItem = addStockItem;
        con = new DBConnect();
        this.setBackground(panelColor);
        
        DefaultTableModel model = new DefaultTableModel();
        setModel(model);
        
        String[] stockID = new String[data.length];
        String[] names = new String[data.length];
        String[] bool = new String[data.length];
        String[] zero = new String[data.length];
        
        for(int i = 0; i < data.length; i++){
            stockID[i] = data[i][0];
            names[i] = data[i][1];
            bool[i] = "false";
            zero[i] = "0";
        }
        
        model.addColumn("StockID", stockID);
        model.addColumn("Name", names);
        model.addColumn("Quantity", zero);
        model.addColumn("Click To Toggle", bool);

        box = new JCheckBox();
        TableColumn boxColumn = getColumnModel().getColumn(3);
        boxColumn.setCellEditor(new DefaultCellEditor(box));
        box.addActionListener(this);
        this.addStockItem.addActionListener(this);
    }

    /**
     * Checks the box as true or adds stock to the selected item.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource().equals(box)){
            if(!table.getValueAt(table.getSelectedRow(), 2).toString().equals("0")){
                if(table.getValueAt(table.getSelectedRow(), 3).toString().equals("true")){
                    for(int i = 0; i < stock.size(); i++){
                        if(stock.get(i).getName().equals(table.getValueAt(table.getSelectedRow(), 1))){
                            stock.remove(i);
                        }
                    }
                    System.out.println("removed");
                }
                else if(table.getValueAt(table.getSelectedRow(), 3).toString().equals("false")){
                        stock.add(new Stock(table.getValueAt(table.getSelectedRow(), 1).toString(),
                        Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString()), 
                        table.getValueAt(table.getSelectedRow(), 0).toString()));
                        System.out.println("added");
                }
            }
        }
        
        if(e.getSource().equals(addStockItem)){
            System.out.println(stock.size());
            for(int i = 0; i < stock.size(); i++){
                try {
                    con.insertItemStock(itemID, stock.get(i).getStockID(), stock.get(i).getQuantity());
                } catch (SQLException ex) {
                    Logger.getLogger(ItemStockTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
