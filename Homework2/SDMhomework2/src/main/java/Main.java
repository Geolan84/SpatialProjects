import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.postgis.PGgeometry;


public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        Boolean cont = true;
        while (cont) {
            System.out.print("Enter a number (jdbc: 1, h2: 2, exit: other keys): ");
            int num = sc.nextInt();
            switch (num) {
                case 1:
                    try {
                        getJDBC();

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                       //getH2DB();
                        System.out.println("Unfortunately, this method is not fully implemented(");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    cont = false;
            }
        }
        sc.close();
    }

    public static void getH2DB(){
        String jdbcURL = "jdbc:h2:mem://testdb";
        try(Connection conJDCB = DriverManager.getConnection(jdbcURL)) {
            System.out.println("Connected to H2 in server mode.");
            String CreateQuery = "CREATE TABLE Stations(gid integer, railway nvarchar(9), geom geometry(Point,32637))";
            var rs = conJDCB.createStatement().execute(CreateQuery);
            CreateQuery = "CREATE TABLE Points(gid integer, shop nvarchar(41), geom geometry(Point,32637))";
            rs = conJDCB.createStatement().execute(CreateQuery);

            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }
            String url = "jdbc:postgresql://localhost:5432/SDMhomework2";
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url, "postgres", "WinPos");
                Statement s = conn.createStatement();
                ResultSet r = s.executeQuery("SELECT gid, railway, geom FROM public.\"Stations\";");
                while (r.next()) {
                    int id = r.getInt(1);
                    String field = r.getString(2);
                    PGgeometry geom = (PGgeometry) r.getObject(3);
                    //conJDCB.createStatement().execute("INSERT INTO Stations (gid, railway, geom) VALUES ("+id+", "+field+", ST_GeomFromText("+geom.toString()+", 32637)");
                    //System.out.println("INSERT INTO Stations (gid, railway, geom) VALUES ("+id+", "+field+", ST_GeomFromText("+geom.toString()+", 32637)");
                }
                s.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void getJDBC(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        String url = "jdbc:postgresql://localhost:5432/SDMhomework2";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, "postgres", "WinPos");
            Statement s = conn.createStatement();
            ResultSet r = s.executeQuery("SELECT ST_MakeLine(s.geom, p.geom) as geom FROM public.\"Stations\" s INNER JOIN public.\"Points\" p ON ST_Intersects(ST_Buffer(s.geom, 200), p.geom) WHERE p.shop IS NOT NULL AND s.railway = 'tram_stop';");
            int counter = 1;
            while (r.next()) {
                PGgeometry geom = (PGgeometry) r.getObject(1);
                System.out.println("Line " + counter + ": " + geom.toString());
                counter++;
            }
            s.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
