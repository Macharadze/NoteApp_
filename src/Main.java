import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static final String CONNECTION = "jdbc:mysql://localhost:3306/note";
    private static Connection conn;
    public static void main(String[] args) throws InterruptedException {
        int num = 0;
        DB note = new DB();
        note.open();
        System.out.println("Welcome");
        while (num!=5) {
            try {
                num = getNumber();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
          if (num == 1)
              note.addNote();
          if (num == 2)
              note.deleteNote();
          if (num == 3)
              note.rename();
          if (num == 4)
              note.updateNote();
      Thread.sleep(500);
      }
//        )
        note.close();

    }
    public static int getNumber(){
        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("choose a number: \n 1 - add a new note \n 2 - delete a note \n 3 - rename a note \n 4 - update a note \n 5 -close the program");
        Scanner sc =new Scanner(System.in);
        int num = sc.nextInt();
        return num;
    }
}