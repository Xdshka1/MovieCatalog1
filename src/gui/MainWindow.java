package gui;

import model.Movie;
import model.MovieDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class MainWindow {
    private final MovieDatabase db;
    private JFrame frame;
    private DefaultListModel<Movie> listModel;

    public MainWindow(MovieDatabase db) {
        this.db = db;
    }

    public void initAndShow() {
        frame = new JFrame("Movie Catalog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel top = new JPanel(new BorderLayout(6, 6));
        JTextField search = new JTextField();
        JButton btn = new JButton("Search");
        top.add(search, BorderLayout.CENTER);
        top.add(btn, BorderLayout.EAST);

        listModel = new DefaultListModel<>();
        JList<Movie> results = new JList<>(listModel);
        JScrollPane sc = new JScrollPane(results);

        JTextArea details = new JTextArea();
        details.setEditable(false);
        details.setLineWrap(true);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sc, new JScrollPane(details));
        split.setDividerLocation(400);

        frame.getContentPane().add(top, BorderLayout.NORTH);
        frame.getContentPane().add(split, BorderLayout.CENTER);

        ActionListener doSearch = e -> {
            listModel.clear();
            String q = search.getText().trim();
            if (q.isEmpty()) return;
            List<Movie> r = db.search(q);
            for (Movie m : r) listModel.addElement(m);
        };
        btn.addActionListener(doSearch);
        search.addActionListener(doSearch);

        results.addListSelectionListener(ev -> {
            Movie m = results.getSelectedValue();
            if (m != null) {
                details.setText("Title: " + m.name +
                        "\nGenre: " + m.genre +
                        "\nYear: " + m.year +
                        "\nRating: " + m.rating +
                        "\nScore: " + m.score +
                        "\nVotes: " + m.votes +
                        "\nDirector: " + m.director +
                        "\nWriter: " + m.writer +
                        "\nStar: " + m.star +
                        "\nCountry: " + m.country +
                        "\nCompany: " + m.company +
                        "\nRuntime: " + m.runtime + " min" +
                        "\nBudget: $" + m.budget +
                        "\nGross: $" + m.gross);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        MovieDatabase db = new MovieDatabase("src/movies.json");
        db.init();
        SwingUtilities.invokeLater(() -> new MainWindow(db).initAndShow());
    }
}