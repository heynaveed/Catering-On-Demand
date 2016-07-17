package GUI;

import Database.DBConnect;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTable;

public class PendingOrdersTable extends JTable implements ActionListener {
    
    private final JTable table;
    private final DBConnect con;
    private final MainDashboard dash;
    private final JButton refresh;
    private final String name;
    private final String iD;
    private final JButton print;
    private static final Color panelColor = new Color(199, 159, 79);
    
    public PendingOrdersTable(String[][] data, String[] headers, MainDashboard dash, String name, String iD, JButton refresh, JButton print) throws SQLException{
        super(data, headers);
        setDragEnabled(false);
        //removeColumn(getColumnModel().getColumn(0));
        dash.getJLabelCustomerNameID().setText(name + " (ID: " + iD + ")");
        doLayout();
        this.refresh = refresh;
        this.print = print;
        this.name = name;
        this.iD = iD;
        //getColumnModel().getColumn(0).setPreferredWidth(145);
        this.refresh.addActionListener(this);
        this.print.addActionListener(this);
        this.setBackground(panelColor);
        
        con = new DBConnect();
        table = this;
        this.dash = dash;
    }

    /**
     * Refreshes the table or prints it.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(refresh)){
            try {
                dash.generatePendingOrdersTable(iD, name);
            } catch (SQLException ex) {
                Logger.getLogger(PendingOrdersTable.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(PendingOrdersTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(e.getSource().equals(print)){
            try {
            Document document = new Document();
            PdfWriter writer;
            
            writer = PdfWriter.getInstance(document, new FileOutputStream("Pending_Order_Report.pdf"));
            document.open();
            PdfContentByte cb = writer.getDirectContent();

            PdfTemplate tp = cb.createTemplate(500, 500);
            Graphics2D g2;

            g2 = tp.createGraphicsShapes(500, 500);
            
            this.print(g2);
            g2.dispose();
            cb.addTemplate(tp, 30, 300);
            document.close();
            } catch (DocumentException | FileNotFoundException ex) {
                Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
