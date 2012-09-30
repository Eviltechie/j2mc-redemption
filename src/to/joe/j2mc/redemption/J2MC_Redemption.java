package to.joe.j2mc.redemption;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class J2MC_Redemption extends JavaPlugin implements Listener {
    
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }
    
}
