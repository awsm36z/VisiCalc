import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class JframeUI {
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
    }
}
