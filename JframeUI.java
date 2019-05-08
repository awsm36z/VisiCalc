import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.event.*;


public class JframeUI implements TableModelListener {
    // frame
    JFrame f;
    // Table
    JTable j;

    // Constructor
    JframeUI(Cell[][] cellsheet) {
        // Frame initiallization
        f = new JFrame();

        // Frame Title
        f.setTitle("VisiCalc");

        // Data to be displayed in the JTable
        Cell[][] data = cellsheet;

        // Column Names
        String[] columnNames = { "A", "B", "C", "D", "E", "F", "G" };

        // Initializing the JTable
        j = new JTable(data, columnNames);
        j.setBounds(30, 40, 200, 300);

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(j);
        f.add(sp);
        // Frame Size
        f.setSize(500, 200);
        // Frame Visible = true
        f.setVisible(true);

        f.getModel().addTableModelListener(this);

    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);

        
    }
}
