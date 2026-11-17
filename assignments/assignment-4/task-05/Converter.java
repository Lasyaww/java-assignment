import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Task 5 - Four event-handling styles for a currency converter. */
public class Converter extends JFrame implements ActionListener {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> from = new JComboBox<>(new String[]{"INR", "USD", "EUR"});
    private final JComboBox<String> to = new JComboBox<>(new String[]{"USD", "EUR", "INR"});
    private final JLabel result = new JLabel(" ");
    private static double inrRate(String currency) {
        switch (currency) {
            case "USD": return 83.0;
            case "EUR": return 90.0;
            default: return 1.0;
        }
    }

    public Converter() {
        super("Currency Converter - Frame Listener");
        setLayout(new FlowLayout());
        add(amount); add(from); add(to);
        JButton button = new JButton("Convert");
        add(button); add(result);
        button.addActionListener(this); // (a) frame implements ActionListener
        amount.addActionListener(this); // Enter key
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        pack();
    }

    @Override public void actionPerformed(ActionEvent e) { convert(); }

    private void convert() {
        try {
            double value = Double.parseDouble(amount.getText().trim());
            if (value < 0) throw new NumberFormatException();
            double inr = value * inrRate((String) from.getSelectedItem());
            double converted = inr / inrRate((String) to.getSelectedItem());
            String text = String.format("%.2f %s", converted, to.getSelectedItem());
            result.setText(text);
            JOptionPane.showMessageDialog(this, text);
        } catch (NumberFormatException ex) {
            result.setText("Invalid amount");
            JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static class NamedInnerConverter extends JFrame {
        private final JTextField amount = new JTextField(8);
        private final JLabel result = new JLabel(" ");
        NamedInnerConverter() {
            JButton button = new JButton("Convert");
            add(amount); add(button); add(result);
            button.addActionListener(new ConvertHandler()); // (b) named inner class
            amount.addActionListener(new ConvertHandler());
            pack();
        }
        private void convert() {
            try { result.setText(String.format("%.2f", Double.parseDouble(amount.getText()) * 0.012)); }
            catch (NumberFormatException e) { result.setText("Invalid amount"); }
        }
        private class ConvertHandler implements ActionListener {
            @Override public void actionPerformed(ActionEvent e) { convert(); }
        }
    }

    static class AnonymousConverter extends JFrame {
        private final JTextField amount = new JTextField(8);
        private final JLabel result = new JLabel(" ");
        AnonymousConverter() {
            JButton button = new JButton("Convert");
            add(amount); add(button); add(result);
            ActionListener handler = new ActionListener() { // (c) anonymous inner class
                @Override public void actionPerformed(ActionEvent e) {
                    try { result.setText(String.format("%.2f", Double.parseDouble(amount.getText()) * 0.012)); }
                    catch (NumberFormatException ex) { result.setText("Invalid amount"); }
                }
            };
            button.addActionListener(handler); amount.addActionListener(handler); pack();
        }
    }

    static class LambdaConverter extends JFrame {
        private final JTextField amount = new JTextField(8);
        private final JLabel result = new JLabel(" ");
        LambdaConverter() {
            JButton button = new JButton("Convert");
            add(amount); add(button); add(result);
            ActionListener handler = e -> {
                try { result.setText(String.format("%.2f", Double.parseDouble(amount.getText()) * 0.012)); }
                catch (NumberFormatException ex) { result.setText("Invalid amount"); }
            }; // (d) lambda expression
            button.addActionListener(handler); amount.addActionListener(handler); pack();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Converter().setVisible(true));
    }
}
