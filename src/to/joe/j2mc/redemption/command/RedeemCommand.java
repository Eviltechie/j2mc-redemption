package to.joe.j2mc.redemption.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.redemption.J2MC_Redemption;

public class RedeemCommand extends MasterCommand {
    
    J2MC_Redemption plugin;

    public RedeemCommand(J2MC_Redemption redemption) {
        super(redemption);
        this.plugin = redemption;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        /*
         * redeem list -> shows a list of what coupons you can redeem
         * redeem details <number> -> shows what you will get when you redeem
         * redeem coupon <code> -> redeems a coupon
         * redeem <number> -> redeems items
         */
        if (!isPlayer) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command");
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/redeem list -> shows a list of what you may redeem");
            sender.sendMessage(ChatColor.RED + "/redeem details <number> -> shows a list of items you will get");
            sender.sendMessage(ChatColor.RED + "/redeem coupon <code> -> redeems a coupon");
            sender.sendMessage(ChatColor.RED + "/redeem <number> -> redeems items");
            return;
        }
        if (args[0].equalsIgnoreCase("list") && args.length == 1) {
            try {
                PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT * FROM coupons WHERE player LIKE ? AND (expiry > ? OR expiry IS NULL) AND (server = ? OR server IS NULL) AND redeemed IS NULL");
                ps.setString(1, player.getName());
                ps.setLong(2, System.currentTimeMillis() / 1000L);
                ps.setInt(3, J2MC_Manager.getServerID());
                ResultSet rs = ps.executeQuery(); 
                if (rs.next()) {
                    sender.sendMessage(ChatColor.GREEN + "You have the following ID's to redeem");
                    StringBuilder sb = new StringBuilder(ChatColor.YELLOW + "");
                    do {
                        sb.append(rs.getInt("id")).append(", ");
                    } while (rs.next());
                    sender.sendMessage(sb.substring(0, sb.length()-2));
                    sender.sendMessage(ChatColor.GREEN + "Type /redeem details <id> to see what items that ID contains");
                } else {
                    sender.sendMessage(ChatColor.RED + "You have nothing to redeem");
                }
            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Error getting list of things to redeem", e);
            }
            return;
        }
        if (args[0].equalsIgnoreCase("details") && args.length == 2) {
            try {
                PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT * FROM coupons WHERE player LIKE ? AND (expiry > ? OR expiry IS NULL) AND (server = ? OR server IS NULL) AND id = ? and redeemed IS NULL");
                ps.setString(1, player.getName());
                ps.setLong(2, System.currentTimeMillis() / 1000L);
                ps.setInt(3, J2MC_Manager.getServerID());
                ps.setInt(4, Integer.parseInt(args[1]));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    PreparedStatement ps2 = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT * FROM items WHERE id = ?");
                    ps2.setInt(1, Integer.parseInt(args[1]));
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        sender.sendMessage(ChatColor.GREEN + "ID " + Integer.parseInt(args[1]) + " contains the following items");
                        do {
                            sender.sendMessage(ChatColor.GREEN + ""+ rs2.getInt("quantity") + "x " + ChatColor.GOLD + Material.getMaterial(rs2.getInt("item")));
                        } while (rs2.next());
                    } else {
                        sender.sendMessage(ChatColor.RED + "ID " + Integer.parseInt(args[1]) + " has no items to redeem");
                    }
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Please enter a valid ID number");
            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Error getting details", e);
            }
            return;
        }
        if (args[0].equalsIgnoreCase("coupon") && args.length == 2) {
            try {
                PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT * FROM coupons WHERE player IS NULL AND code LIKE ? AND (expiry > ? OR expiry IS NULL) AND (server = ? OR server IS NULL) AND redeemed IS NULL");
                ps.setString(1, args[1].replaceAll("'", ""));
                ps.setLong(2, System.currentTimeMillis() / 1000L);
                ps.setInt(3, J2MC_Manager.getServerID());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    PreparedStatement ps2 = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT * FROM items WHERE id = ?");
                    ps2.setInt(1, rs.getInt("id"));
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        do {
                            player.getInventory().addItem(new ItemStack(rs2.getInt("item"), rs2.getInt("quantity")));
                            sender.sendMessage(ChatColor.GREEN + ""+ rs2.getInt("quantity") + "x " + ChatColor.GOLD + Material.getMaterial(rs2.getInt("item")));
                        } while (rs2.next());
                        PreparedStatement ps3 = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("UPDATE coupons SET player = ?, redeemed = ? WHERE id = ?");
                        ps3.setString(1, player.getName());
                        ps3.setLong(2, System.currentTimeMillis() / 1000L);
                        ps3.setInt(3, rs.getInt("id"));
                        ps3.execute();
                        this.plugin.getLogger().info(player.getName() + " has redeemed coupon with id " + rs.getInt("id"));
                        sender.sendMessage(ChatColor.GREEN + "Coupon successfully redeemed!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "That coupon code is not valid. It may have been redeemed already or may not exist.");
                }
            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Error redeeming coupon", e);
            }
            return;
        }
        if (args.length == 1) {
            try {
                PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT * FROM coupons WHERE player LIKE ? AND (expiry > ? OR expiry IS NULL) AND (server = ? OR server IS NULL) AND id = ? and redeemed IS NULL");
                ps.setString(1, player.getName());
                ps.setLong(2, System.currentTimeMillis() / 1000L);
                ps.setInt(3, J2MC_Manager.getServerID());
                ps.setInt(4, Integer.parseInt(args[0]));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    PreparedStatement ps2 = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT * FROM items WHERE id = ?");
                    ps2.setInt(1, rs.getInt("id"));
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        do {
                            player.getInventory().addItem(new ItemStack(rs2.getInt("item"), rs2.getInt("quantity")));
                            sender.sendMessage(ChatColor.GREEN + ""+ rs2.getInt("quantity") + "x " + ChatColor.GOLD + Material.getMaterial(rs2.getInt("item")));
                        } while (rs2.next());
                        PreparedStatement ps3 = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("UPDATE coupons SET redeemed = ? WHERE id = ?");
                        ps3.setLong(1, System.currentTimeMillis() / 1000L);
                        ps3.setInt(2, rs.getInt("id"));
                        ps3.execute();
                        this.plugin.getLogger().info(player.getName() + " has redeemed items with id " + rs.getInt("id"));
                        sender.sendMessage(ChatColor.GREEN + "Items successfully redeemed!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "That ID number is not valid. It may have been redeemed already or may not exist.");
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Please enter a valid ID number");
            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Error redeeming items", e);
            }
            return;
        }
    }

}
