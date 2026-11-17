import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/** Task 3 - Resizable library search screen using nested layout managers. */
public class LibrarySearchScreen extends Frame {
    private final TextField query = new TextField(24);
    private final Choice category = new Choice();
    private final List list = new List();
    private final Label status = new Label("Ready");

    public LibrarySearchScreen() {
        super("Campus Library Search");
        setLayout(new BorderLayout(8, 8));
        Panel search = new Panel(new FlowLayout(FlowLayout.LEFT));
        category.add("All"); category.add("Books"); category.add("Journals");
        search.add(new Label("Search")); search.add(query); search.add(category);
        Button searchButton = new Button("Search"); search.add(searchButton);
        Panel results = new Panel(new BorderLayout());
        results.add(list, BorderLayout.CENTER);
        Panel navigation = new Panel(new FlowLayout());
        Button prev = new Button("Prev"), next = new Button("Next"), reserve = new Button("Reserve");
        navigation.add(prev); navigation.add(next); navigation.add(reserve);
        add(search, BorderLayout.NORTH); add(results, BorderLayout.CENTER);
        add(navigation, BorderLayout.SOUTH); add(status, BorderLayout.PAGE_END);
        searchButton.addActionListener(e -> {
            list.removeAll();
            list.add("Search results for: " + query.getText());
            list.add("Java: The Complete Reference");
            list.add("Effective Java");
            status.setText("3 sample results");
        });
        reserve.addActionListener(e -> status.setText("Selected item reserved (demo)."));
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setSize(650, 420);
    }

    public static void main(String[] args) { EventQueue.invokeLater(() -> new LibrarySearchScreen().setVisible(true)); }
}
