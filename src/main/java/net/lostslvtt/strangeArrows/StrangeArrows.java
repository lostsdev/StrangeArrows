package net.lostslvtt.strangeArrows;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class StrangeArrows extends JavaPlugin {

    public static String convertBowTypeToArrowType(String type) {
        StringBuilder returning = new StringBuilder(type);
        returning.deleteCharAt(type.length()-1);
        returning.deleteCharAt(type.length()-2);
        returning.deleteCharAt(type.length()-3);
        returning.append("Arrow");
		
        return returning.toString();
    }

    public static String[] bowTypes = new String[]{ "SneezeBow", "EnchantBow", "ExplosionBow", "FireExplosionBow", "WaterBow", "FatigueBow", "SculkBow" };
    public static String[] arrowTypes = new String[]{ "SneezeArrow", "EnchantArrow", "ExplosionArrow", "FireExplosionArrow", "WaterArrow", "FatigueArrow", "SculkArrow" };
    public static Component orangeText(String arg) {
        return MiniMessage.miniMessage().deserialize("<color:#ffba7d>"+arg+"</color>");
    }
    public static Component errorText(String arg) {
        return MiniMessage.miniMessage().deserialize("<color:#ff8f66>"+arg+"</color>");
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new SAListener(), this);
        Bukkit.getPluginCommand("getbow").setExecutor(new BowCmdManager());
        Bukkit.getPluginCommand("getbow").setTabCompleter(new BowComplete());
        Bukkit.getPluginCommand("getarrow").setExecutor(new ArrowCmdManager());
        Bukkit.getPluginCommand("getarrow").setTabCompleter(new ArrowComplete());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
