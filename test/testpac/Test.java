package testpac;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {

    public static void main(String[] args) {

//        Object o = 9;
//        System.out.println(o.getClass().getSimpleName());


//        Connection conn = null;
//        try (Statement statement = conn.createStatement()) {
//            System.out.println();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Test", "postgres", "smartdoc");
//            System.out.println(conn.getMetaData().getDatabaseProductName());
            DatabaseMetaData data = conn.getMetaData();
            ResultSet rs = data.getTypeInfo();//getAttributes(null, "", "", "");
            int[] sizes = new int[rs.getMetaData().getColumnCount()];

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
                sizes[i - 1] = rs.getMetaData().getColumnLabel(i).length() + 3;
                System.out.print(rs.getMetaData().getColumnLabel(i) + "   ");
            }
            System.out.println();

            String space = "                                                                                                            ";
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
                System.out.print(rs.getMetaData().getColumnTypeName(i) + space.substring(0, sizes[i - 1] - rs.getMetaData().getColumnTypeName(i).length()));
            }
            System.out.println();

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
                System.out.print(rs.getMetaData().getColumnType(i) + space.substring(0, sizes[i - 1] - String.valueOf(rs.getMetaData().getColumnType(i)).length()));
            }
            System.out.println();
            System.out.println();
//
////            HashMap<Integer, String> map = new HashMap<>();
            while (rs.next()) {
////                if(map.get(rs.getInt("DATA_TYPE")) == null)
////                    map.put(rs.getInt("DATA_TYPE"), rs.getString("TYPE_NAME"));
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
                    String val = String.valueOf(rs.getString(i));
                    if (val.length() > sizes[i - 1])
                        val = val.substring(0, sizes[i - 1]);
                    System.out.print(val + space.substring(0, sizes[i - 1] - val.length()));
                }
                System.out.println();
            }
//
////            map.forEach((i, s) -> System.out.println(i + " " + s));
////            PreparedStatement s = conn.prepareStatement("select id, name from tags where id > ?");
////
////            Connection c2 = s.getConnection();
////            System.out.println(conn.isClosed());
////            System.out.println(s.isClosed());
////            s.close();
////            System.out.println(conn.isClosed());
////            System.out.println(s.isClosed());
////            System.out.println(c2.isClosed());
////            PreparedStatement p = conn.prepareStatement("insert into tags(id, name) values(?, ?)");
////            s.setInt(1, 0);
////            p.setInt(1, 9);
////            p.setString(2, "rororororor");
////            ResultSet rs = s.executeQuery();
////            p.executeUpdate();
////            if (rs.next()) {
////                System.out.println("name = " + rs.getString("name") + "   id = " + rs.getInt("id"));
////            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
