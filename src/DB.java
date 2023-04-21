import com.mysql.cj.xdevapi.Result;

import java.sql.*;
import java.util.*;

public class DB {
    public static Connection conn;
    public static final String note = "notes";
    public static final String CONNECTION = "jdbc:mysql://localhost:3306/note";

    public DB() {

    }

    public boolean open() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(CONNECTION, "root", "");
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(
                    "could not connection"
            );
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            System.out.println("could not close connection");
        }
    }

    public void addNote() {
        System.out.println("enter a name");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        System.out.println("enter a note");
        Scanner scanner = new Scanner(System.in);
        String note1 = scanner.nextLine();
        String str = "INSERT INTO " + note +
                " VALUES ( " + null + ",'" + name + "', '" + note1 + "')";
        try (Statement statement = conn.createStatement()) {
            statement.execute(str);
            System.out.println("Added");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteNote() {
        System.out.println("choose the note");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        String s = "DELETE FROM " + note + " WHERE name = '" + name + "'";
        if (checkNote(name)) {
            try (Statement statement = conn.createStatement()) {
                statement.execute(s);
                System.out.println("Deleted");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("does not exist");
        }
    }

    public void rename() {
        System.out.println("choose the note");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        System.out.println("write a new name");
        Scanner scanner = new Scanner(System.in);
        String newName = scanner.nextLine();
        String s = "UPDATE notes SET name = '" + newName + "'" +
                " WHERE name = '" + name + "'";
        if (checkNote(name)) {
            try (Statement statement = conn.createStatement()) {
                statement.execute(s);
                System.out.println("successfully renamed");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("search with words");
            Scanner scanner1 = new Scanner(System.in);
            String s1 = scanner1.nextLine();
            String s2 = "SELECT * FROM " + note;
            int n = 1;
            Map<Integer, String> map = new HashMap<>();
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(s2)) {
                while (resultSet.next()) {
                    String search = resultSet.getString("name");
                    if (search.toLowerCase().contains(s1.toLowerCase())) {
                        map.put(n, resultSet.getString("name"));
                        System.out.println(n++ + " - " + search);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("choose one of them - press 0 if there is not suitable note");
            Scanner scanner2 = new Scanner(System.in);
            int i = scanner2.nextInt();
            if (i == 0 || i > n)
                return;
             else {
                try {
                    String newOne = "UPDATE notes SET name = '" + newName + "'" +
                            " WHERE name = '" + map.get(i) + "'";
                    Statement statement = conn.createStatement();
                    statement.execute(newOne);
                    System.out.println("successfully renamed");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void updateNote(){
        System.out.println("choose the note");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        try(Statement statement =conn.createStatement()) {
            String s = "SELECT * FROM " + note + " WHERE name = '" + name + "'";
            statement.execute(s);
            ResultSet resul = statement.executeQuery(s);
            while (resul.next()) {
                System.out.println(resul.getString("text"));
                System.out.println();
                System.out.println("write a new note ");
                Scanner scanner = new Scanner(System.in);
                String newName = scanner.nextLine();
                String s1 = resul.getString("text");
                String[] word = newName.split("/");
                String s2 = s1.replace(word[0], word[1]);
                String s3 = "UPDATE notes SET text = '" + s2 + "'" +
                        " WHERE name = '" + name + "'";
                statement.execute(s3);
                System.out.println("added");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void searchNote() {
        System.out.println("choose the note");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        String s = "SELECT * FROM " + note;
//        List<String> list = new ArrayList<>();
        int n = 1;
        int check = 0;
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(s)) {
            while (resultSet.next()) {
                String search = resultSet.getString("name");
                if (search.toLowerCase().contains(name.toLowerCase())) {
                    System.out.println(n++ + " - " + resultSet.getString("text"));
                    check = 1;
                }
                if (check == 0) {
                    String s1 = resultSet.getString("text");
                    if (s1.toLowerCase().contains(name.toLowerCase()))
                        System.out.println(n++ + " - " + s1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkNote(String name) {
        String s = "SELECT * FROM " + note + " WHERE name = '" + name + "'";
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(s);
            while (resultSet.next()) {
                if (resultSet.getString("name") != null)
                    return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
