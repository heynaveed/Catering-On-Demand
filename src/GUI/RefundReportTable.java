package GUI;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTable;

public class RefundReportTable extends JTable implements ActionListener {
    
    private final JButton print;
    private static final Color panelColor = new Color(199, 159, 79);
    
    public RefundReportTable(String[][] data, String[] headers, JButton print) throws SQLException{
        super(data, headers);
        setDragEnabled(false);
        this.print = print;
        this.setBackground(panelColor);
        //removeColumn(getColumnModel().getColumn(0));
        
        doLayout();
        //getColumnModel().getColumn(0).setPreferredWidth(145);
        print.addActionListener(this);
    }

    /**
     * Prints the report to pdf.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Document document = new Document();
            PdfWriter writer;
            
            writer = PdfWriter.getInstance(document, new FileOutputStream("Refund_Report_Table.pdf"));
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
