import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class SimplePlagiarismChecker {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimplePlagiarismChecker::new);
    }

    private JTextArea textArea1;
    private JTextArea textArea2;
    private JLabel resultLabel;

    public SimplePlagiarismChecker() {
        JFrame frame = new JFrame("Plagiarism Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

        // Text areas
        textArea1 = new JTextArea(10, 20);
        textArea2 = new JTextArea(10, 20);
        JScrollPane scroll1 = new JScrollPane(textArea1);
        JScrollPane scroll2 = new JScrollPane(textArea2);

        // Buttons
        JButton load1 = new JButton("Load File 1");
        JButton load2 = new JButton("Load File 2");
        JButton checkButton = new JButton("Check Similarity");

        // Result label
        resultLabel = new JLabel("Plagiarism Score: --");

        // Layout panels
        JPanel topPanel = new JPanel();
        topPanel.add(load1);
        topPanel.add(load2);
        topPanel.add(checkButton);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(scroll1);
        textPanel.add(scroll2);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(resultLabel);

        // Add panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(textPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        load1.addActionListener(e -> textArea1.setText(loadFileContent()));
        load2.addActionListener(e -> textArea2.setText(loadFileContent()));
        checkButton.addActionListener(e -> checkSimilarity());

        frame.setVisible(true);
    }

    private String loadFileContent() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                return new String(java.nio.file.Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading file.");
            }
        }
        return "";
    }

    private void checkSimilarity() {
        String text1 = textArea1.getText().toLowerCase();
        String text2 = textArea2.getText().toLowerCase();

        Set<String> words1 = new HashSet<>(Arrays.asList(text1.replaceAll("[^a-zA-Z0-9 ]", "").split("\\s+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(text2.replaceAll("[^a-zA-Z0-9 ]", "").split("\\s+")));

        Set<String> commonWords = new HashSet<>(words1);
        commonWords.retainAll(words2);

        Set<String> allWords = new HashSet<>(words1);
        allWords.addAll(words2);

        double score = 0.0;
        if (!allWords.isEmpty()) {
            score = ((double) commonWords.size() / allWords.size()) * 100;
        }

        resultLabel.setText(String.format("Plagiarism Score: %.2f%%", score));
    }
}
