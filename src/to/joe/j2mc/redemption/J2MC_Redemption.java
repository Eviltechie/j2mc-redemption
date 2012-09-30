package to.joe.j2mc.redemption;

import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.redemption.command.RedeemCommand;

public class J2MC_Redemption extends JavaPlugin {
    
    @Override
    public void onEnable() {
        this.getCommand("redeem").setExecutor(new RedeemCommand(this));
    }
    
}
