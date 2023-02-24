package test;

import org.postgis.PGgeometry;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    private static void getGeometries(String request) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/homework1";
        java.sql.Connection conn = DriverManager.getConnection(url, "postgres", "WinPos");
        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery(request);
        while (r.next()) {
            PGgeometry geom = (PGgeometry) r.getObject(1);
            int id = r.getInt(2);
            System.out.println(id + ": " + geom.toString());
        }
        s.close();
        conn.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Scanner sc = new Scanner(System.in);
        Boolean cont = true;
        while (cont) {
            System.out.print("Enter a number: ");
            int num = sc.nextInt();
            switch (num) {
                case 1:
                    try {
                        getGeometries("select geom,id from objects");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    int a, b, c;
                    a = sc.nextInt();
                    b = sc.nextInt();
                    c = sc.nextInt();
                    try {
                        getGeometries("select geom, id from objects where ST_DWithin(geom, ST_GeomFromText('POINT("+a+" "+b+")', 32637),"+c+")\n");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    cont = false;
            }
        }
        sc.close();
    }
}
