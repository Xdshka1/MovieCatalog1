package model;

import java.io.*;
import java.util.*;

public class MovieDatabase {
    private final String filePath;
    private List<Movie> movies;

    public MovieDatabase(String filePath) {
        this.filePath = filePath;
        this.movies = new ArrayList<>();
    }

    public void init() throws IOException {
        StringBuilder json = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line.trim());
            }
        }


        String all = json.toString();
        all = all.substring(1, all.length() - 1);
        String[] objects = all.split("\\},\\{");

        for (String obj : objects) {
            obj = obj.replace("{", "").replace("}", "");
            Movie m = parseMovie(obj);
            if (m != null) movies.add(m);
        }
    }

    private Movie parseMovie(String obj) {
        Movie m = new Movie();
        String[] fields = obj.split(",\"");

        for (String f : fields) {
            f = f.replace("\"", "").trim();
            String[] kv = f.split(":", 2);
            if (kv.length < 2) continue;
            String key = kv[0].trim();
            String value = kv[1].trim();

            try {
                switch (key) {
                    case "name": m.name = value; break;
                    case "rating": m.rating = value; break;
                    case "genre": m.genre = value; break;
                    case "year": m.year = Integer.parseInt(value); break;
                    case "released": m.released = value; break;
                    case "score": m.score = Double.parseDouble(value); break;
                    case "votes": m.votes = Long.parseLong(value); break;
                    case "director": m.director = value; break;
                    case "writer": m.writer = value; break;
                    case "star": m.star = value; break;
                    case "country": m.country = value; break;
                    case "budget": m.budget = Long.parseLong(value); break;
                    case "gross": m.gross = Long.parseLong(value); break;
                    case "company": m.company = value; break;
                    case "runtime": m.runtime = Integer.parseInt(value); break;
                }
            } catch (Exception e) {

            }
        }
        return m;
    }

    public List<Movie> search(String q) {
        q = q.toLowerCase();
        List<Movie> result = new ArrayList<>();
        for (Movie m : movies) {
            if ((m.name != null && m.name.toLowerCase().contains(q)) ||
                    (m.genre != null && m.genre.toLowerCase().contains(q))) {
                result.add(m);
                if (result.size() >= 200) break;
            }
        }
        return result;
    }
}
