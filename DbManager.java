import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbManager {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String USER = "system";
    private static final String PASS = "dbms123";

    public static List<Packet> loadRules() {
        List<Packet> rules = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT direction, protocol, port, ip, destination_ip, action FROM firewall_rules1")) {
            while (rs.next()) {
                Packet p = new Packet(
                    rs.getString("direction"),
                    rs.getString("protocol"),
                    rs.getInt("port"),
                    rs.getString("ip"),
                    rs.getString("destination_ip"),
                    rs.getString("action")
                );
                rules.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rules;
    }

    public static void insertRule(String direction, String protocol, int port,
                                 String sourceIp, String destinationIp, String action) {
        String sql = "INSERT INTO firewall_rules1 (rule_id, direction, protocol, port, ip, destination_ip, action) " +
                     "VALUES (seq_rule_id.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, direction);
            pstmt.setString(2, protocol);
            pstmt.setInt(3, port);
            pstmt.setString(4, sourceIp);
            pstmt.setString(5, destinationIp);
            pstmt.setString(6, action);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
