package org.LBlockBreak.lBlockBreak.listeners;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import net.milkbowl.vault.economy.Economy;
import org.LBlockBreak.lBlockBreak.LBlockBreak;

public class BlockBreakListener implements Listener {
    
    private final LBlockBreak plugin;
    private final Economy economy;
    
    public BlockBreakListener(LBlockBreak plugin, Economy economy) {
        this.plugin = plugin;
        this.economy = economy;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        // Eğer event iptal edildiyse para verme (koruma varsa)
        if (event.isCancelled()) {
            return;
        }
        
        // Oyuncunun block kırma izni var mı kontrol et
        Player player = event.getPlayer();
        if (!player.hasPermission("blockbreak.earn")) {
            return;
        }
        
        Material blockType = event.getBlock().getType();
        
        FileConfiguration config = plugin.getConfig();
        String blockPath = "blocks." + blockType.name();
        
        if (config.contains(blockPath)) {
            double baseMoney = config.getDouble(blockPath);
            
            if (baseMoney > 0) {
                // VIP çarpanını hesapla
                double multiplier = getVipMultiplier(player, config);
                double finalMoney = baseMoney * multiplier;
                
                // Ekonomi sistemi aktif ise para ver
                if (plugin.isEconomyEnabled() && economy != null) {
                    economy.depositPlayer(player, finalMoney);
                    
                    String message = config.getString("messages.money_received", "&e%money% akce verildi!");
                    message = message.replace("%money%", String.format("%.2f", finalMoney));
                    
                    // Eğer VIP çarpanı uygulanmışsa bilgi ver
                    if (multiplier > 1.0) {
                        message += " &7(VIP x" + String.format("%.1f", multiplier) + ")";
                    }
                    
                    player.sendMessage(message);
                } else {
                    // Ekonomi yoksa sadece bilgi ver
                    String message = config.getString("messages.no_economy", "&eBlock kırıldı! (%money% akce - ekonomi yok)");
                    message = message.replace("%money%", String.format("%.2f", finalMoney));
                    player.sendMessage(message);
                }
            }
        }
    }
    
    private double getVipMultiplier(Player player, FileConfiguration config) {
        // VIP sistemi kapalı ise 1.0 döndür
        if (!config.getBoolean("vip.enabled", true)) {
            return 1.0;
        }
        
        // VIP seviyelerini kontrol et (en yüksek çarpanı al)
        double highestMultiplier = 1.0;
        
        if (config.contains("vip.multipliers")) {
            for (String vipLevel : config.getConfigurationSection("vip.multipliers").getKeys(false)) {
                String permission = config.getString("vip.multipliers." + vipLevel + ".permission");
                double multiplier = config.getDouble("vip.multipliers." + vipLevel + ".multiplier");
                
                if (player.hasPermission(permission) && multiplier > highestMultiplier) {
                    highestMultiplier = multiplier;
                }
            }
        }
        
        return highestMultiplier;
    }
}
