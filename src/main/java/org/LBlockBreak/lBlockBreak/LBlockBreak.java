package org.LBlockBreak.lBlockBreak;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.LBlockBreak.lBlockBreak.commands.BlockCommand;
import org.LBlockBreak.lBlockBreak.listeners.BlockBreakListener;

public final class LBlockBreak extends JavaPlugin {

    private static LBlockBreak instance;
    private Economy economy;
    private boolean economyEnabled = false;

    @Override
    public void onEnable() {
        instance = this;
        
        saveDefaultConfig();
        
        // Vault ve ekonomi sistemini kontrol et
        if (setupEconomy()) {
            economyEnabled = true;
            getLogger().info("Vault ekonomi sistemi başarıyla bağlandı!");
        } else {
            economyEnabled = false;
            getLogger().warning("Vault ekonomi sistemi bulunamadı! Para özellikleri devre dışı.");
        }
        
        // Event listener'ı kaydet
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this, economy), this);
        
        // Komutları kaydet
        getCommand("bb").setExecutor(new BlockCommand(this));
        
        getLogger().info("LBlockBreak eklentisi başarıyla aktif edildi!");
    }

    @Override
    public void onDisable() {
        getLogger().info("LBlockBreak eklentisi kapatıldı.");
    }
    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        
        economy = rsp.getProvider();
        return economy != null;
    }
    
    public Economy getEconomy() {
        return economy;
    }
    
    public boolean isEconomyEnabled() {
        return economyEnabled;
    }
    
    public static LBlockBreak getInstance() {
        return instance;
    }
}
