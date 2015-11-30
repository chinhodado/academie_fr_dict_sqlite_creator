import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * This will create a SQLite database of the Académie Française : Dictionnaire de l'Académie française de 1932-35 (8e édition)
 * book from a HTML file. Remember to change the path below.
 */
public class MakeDict {
    static Connection connection;
    static PreparedStatement psParms;
    public static void main (String args[]) throws InterruptedException, ExecutionException, JSONException, IOException, ClassNotFoundException, SQLException {
        long startTime = System.currentTimeMillis();
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        stmt = connection.createStatement();
        String sql = "DROP TABLE IF EXISTS Word;";
        stmt.executeUpdate(sql);

        stmt = connection.createStatement();
        sql = "CREATE TABLE Word " +
                "( word       TEXT NOT NULL, " +
                "  definition TEXT) ";
        stmt.executeUpdate(sql);
        stmt.close();

        psParms = connection.prepareStatement(
                "INSERT INTO Word (word, definition) VALUES (?,?)");

        File input = new File("C:\\Users\\Chin\\Downloads\\ebook conversion\\output.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        Element parent = doc.body().select("h2").first().parent();

        String word = null;
        StringBuilder def = new StringBuilder();
        int count = 0;
        boolean foundFirstH2 = false;
        for (Element e : parent.children()) {
            if (!e.tagName().equals("h2")){
                if (foundFirstH2) {
                    def.append(e);
                }
            }
            else {
                foundFirstH2 = true;

                if (word != null) {
                    psParms.setString(1, word);
                    psParms.setString(2, def.toString());
                    psParms.executeUpdate();
                }
                def = new StringBuilder();
                word = e.text().toLowerCase();
                System.out.println(e.text().toLowerCase());
            }
            count++;
            if (count == parent.children().size()) {
                psParms.setString(1,  word);
                psParms.setString(2,  def.toString());
                psParms.executeUpdate();
            }
        }

        stmt.executeUpdate("backup to academie.db");
        stmt.close();

        connection.close();
        System.out.println("Done");
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Time elapsed: " + elapsedTime / 1000 + "s");
    }
}


