package org.LBlockBreak.lBlockBreak.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlockCommand implements CommandExecutor, TabCompleter {
    
    private final Plugin plugin;
    
    public BlockCommand(Plugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("blockbreak.admin")) {
            sender.sendMessage(getMessage("no_permission"));
            return true;
        }
        
        if (args.length == 0) {
            sender.sendMessage("§6/blockbreak commands:");
            sender.sendMessage("§e/bb add <para> §7- Elindeki blocku ekler");
            sender.sendMessage("§e/bb remove §7- Elindeki blocku kaldırır");
            sender.sendMessage("§e/bb set <para> §7- Elindeki blockun parasını değiştirir");
            sender.sendMessage("§e/bb list §7- Kayıtlı blockları listeler");
            sender.sendMessage("§e/bb reload §7- Config'i yeniler");
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "add":
                return handleAdd(sender, args);
            case "remove":
                return handleRemove(sender);
            case "set":
                return handleSet(sender, args);
            case "list":
                return handleList(sender);
            case "reload":
                return handleReload(sender);
            default:
                sender.sendMessage("§cBilinmeyen komut! /bb yazarak komutları görebilirsiniz.");
                return true;
        }
    }
    
    private boolean handleAdd(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cBu komutu sadece oyuncular kullanabilir!");
            return true;
        }
        
        if (args.length != 2) {
            sender.sendMessage("§cKullanım: /bb add <para>");
            return true;
        }
        
        Player player = (Player) sender;
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage(getMessage("no_block_in_hand"));
            return true;
        }
        
        try {
            double money = Double.parseDouble(args[1]);
            if (money <= 0) {
                sender.sendMessage(getMessage("invalid_number"));
                return true;
            }
            
            Material blockType = player.getInventory().getItemInMainHand().getType();
            String blockPath = "blocks." + blockType.name();
            
            if (plugin.getConfig().contains(blockPath)) {
                sender.sendMessage(getMessage("block_already_exists"));
                return true;
            }
            
            plugin.getConfig().set(blockPath, money);
            plugin.saveConfig();
            
            String message = getMessage("block_added");
            message = message.replace("%block%", blockType.name())
                           .replace("%money%", String.format("%.2f", money));
            sender.sendMessage(message);
            
        } catch (NumberFormatException e) {
            sender.sendMessage(getMessage("invalid_number"));
        }
        
        return true;
    }
    
    private boolean handleRemove(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cBu komutu sadece oyuncular kullanabilir!");
            return true;
        }
        
        Player player = (Player) sender;
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage(getMessage("no_block_in_hand"));
            return true;
        }
        
        Material blockType = player.getInventory().getItemInMainHand().getType();
        String blockPath = "blocks." + blockType.name();
        
        if (!plugin.getConfig().contains(blockPath)) {
            sender.sendMessage(getMessage("block_not_found"));
            return true;
        }
        
        plugin.getConfig().set(blockPath, null);
        plugin.saveConfig();
        
        String message = getMessage("block_removed");
        message = message.replace("%block%", blockType.name());
        sender.sendMessage(message);
        
        return true;
    }
    
    private boolean handleSet(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cBu komutu sadece oyuncular kullanabilir!");
            return true;
        }
        
        if (args.length != 2) {
            sender.sendMessage("§cKullanım: /bb set <para>");
            return true;
        }
        
        Player player = (Player) sender;
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage(getMessage("no_block_in_hand"));
            return true;
        }
        
        try {
            double money = Double.parseDouble(args[1]);
            if (money <= 0) {
                sender.sendMessage(getMessage("invalid_number"));
                return true;
            }
            
            Material blockType = player.getInventory().getItemInMainHand().getType();
            String blockPath = "blocks." + blockType.name();
            
            if (!plugin.getConfig().contains(blockPath)) {
                sender.sendMessage(getMessage("block_not_found"));
                return true;
            }
            
            plugin.getConfig().set(blockPath, money);
            plugin.saveConfig();
            
            String message = getMessage("money_set");
            message = message.replace("%block%", blockType.name())
                           .replace("%money%", String.format("%.2f", money));
            sender.sendMessage(message);
            
        } catch (NumberFormatException e) {
            sender.sendMessage(getMessage("invalid_number"));
        }
        
        return true;
    }
    
    private boolean handleList(CommandSender sender) {
        Set<String> blocks = plugin.getConfig().getConfigurationSection("blocks").getKeys(false);
        
        if (blocks.isEmpty()) {
            sender.sendMessage(getMessage("no_blocks_registered"));
            return true;
        }
        
        sender.sendMessage(getMessage("block_list_header"));
        for (String blockName : blocks) {
            double money = plugin.getConfig().getDouble("blocks." + blockName);
            String message = getMessage("block_list_item");
            message = message.replace("%block%", blockName)
                           .replace("%money%", String.format("%.2f", money));
            sender.sendMessage(message);
        }
        
        return true;
    }
    
    private boolean handleReload(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage(getMessage("config_reloaded"));
        return true;
    }
    
    private String getMessage(String path) {
        return plugin.getConfig().getString("messages." + path, "&cMesaj bulunamadı: " + path)
                    .replace("&", "§");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.add("add");
            completions.add("remove");
            completions.add("set");
            completions.add("list");
            completions.add("reload");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set"))) {
            completions.add("10");
            completions.add("50");
            completions.add("100");
            completions.add("500");
        }
        
        return completions;
    }
}
