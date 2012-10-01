package to.joe.j2mc.redemption;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.redemption.command.RedeemCommand;

public class J2MC_Redemption extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("redeem").setExecutor(new RedeemCommand(this));
    }

    public static int newCoupon(String playerCode, boolean isCode, String creator, long expiry, int server) throws SQLException {
        PreparedStatement ps;
        if (isCode) {
            ps = J2MC_Manager.getMySQL().getFreshPreparedStatementWithGeneratedKeys("INSERT INTO coupons (created, creator, code, expiry, server) VALUES (?,?,?,?,?)");
        } else {
            ps = J2MC_Manager.getMySQL().getFreshPreparedStatementWithGeneratedKeys("INSERT INTO coupons (created, creator, player, expiry, server) VALUES (?,?,?,?,?)");
        }
        ps.setLong(1, System.currentTimeMillis() / 1000L);
        ps.setString(2, creator);
        ps.setString(3, playerCode);
        ps.setLong(4, expiry);
        ps.setInt(5, server);
        ps.execute();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        else
            return -1;
    }
    
    public static int newCoupon(String playerCode, boolean isCode, String creator, long expiry) throws SQLException {
        PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementWithGeneratedKeys("INSERT INTO coupons (created, creator, ?, expiry) VALUES (?,?,?,?)");
        if (isCode) {
            ps = J2MC_Manager.getMySQL().getFreshPreparedStatementWithGeneratedKeys("INSERT INTO coupons (created, creator, code, expiry) VALUES (?,?,?,?)");
        } else {
            ps = J2MC_Manager.getMySQL().getFreshPreparedStatementWithGeneratedKeys("INSERT INTO coupons (created, creator, player, expiry) VALUES (?,?,?,?)");
        }
        ps.setLong(1, System.currentTimeMillis() / 1000L);
        ps.setString(2, creator);
        ps.setString(3, playerCode);
        ps.setLong(4, expiry);
        ps.execute();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        else
            return -1;
    }
    
    public static int newCoupon(String playerCode, boolean isCode, String creator, int server) throws SQLException {
        PreparedStatement ps;
        if (isCode) {
            ps = J2MC_Manager.getMySQL().getFreshPreparedStatementWithGeneratedKeys("INSERT INTO coupons (created, creator, code, server) VALUES (?,?,?,?)");
        } else {
            ps = J2MC_Manager.getMySQL().getFreshPreparedStatementWithGeneratedKeys("INSERT INTO coupons (created, creator, player, server) VALUES (?,?,?,?)");
        }
        ps.setLong(1, System.currentTimeMillis() / 1000L);
        ps.setString(2, creator);
        ps.setString(3, playerCode);
        ps.setInt(4, server);
        ps.execute();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        else
            return -1;
    }
    
    public static int newCoupon(String playerCode, boolean isCode, String creator) throws SQLException {
        PreparedStatement ps;
        if (isCode) {
            ps = J2MC_Manager.getMySQL().getFreshPreparedStatementWithGeneratedKeys("INSERT INTO coupons (created, creator, code) VALUES (?,?,?)");
        } else {
            ps = J2MC_Manager.getMySQL().getFreshPreparedStatementWithGeneratedKeys("INSERT INTO coupons (created, creator, player) VALUES (?,?,?)");
        }
        ps.setLong(1, System.currentTimeMillis() / 1000L);
        ps.setString(2, creator);
        ps.setString(3, playerCode);
        ps.execute();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        else
            return -1;
    }

    public static void addItem(int id, int item, int quantity) throws SQLException {
        PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("INSERT INTO items (id, item, quantity) VALUES (?,?,?)");
        ps.setInt(1, id);
        ps.setInt(2, item);
        ps.setInt(3, quantity);
        ps.execute();
    }
    
    public static void addItem(int id, int item) throws SQLException {
        PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("INSERT INTO items (id, item, quantity) VALUES (?,?,1)");
        ps.setInt(1, id);
        ps.setInt(2, item);
        ps.execute();
    }
}
