package net.lostslvtt.strangeArrows;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;


public class BowCmdManager implements CommandExecutor {


    private ItemStack createBow(String type) {
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemMeta bowMeta = bow.getItemMeta();
        bowMeta.getPersistentDataContainer().set(NamespacedKey.fromString("strangearrows"), PersistentDataType.STRING, type);
        bow.setItemMeta(bowMeta);
        return bow;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(StrangeArrows.errorText("Wrong usage. Use /givebow TYPE"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(StrangeArrows.errorText("Only player can call this command."));
            return true;
        }
        String bowType = args[0];
        boolean isCorrectType = false;
        for (String type : StrangeArrows.bowTypes) {
            if (bowType.equalsIgnoreCase(type)) {
                isCorrectType = true;
            }
        }
        if (!isCorrectType) {
            String allTypes = Arrays.stream(StrangeArrows.bowTypes).collect(Collectors.joining());
            sender.sendMessage(StrangeArrows.errorText("Invalid type. Choose from " + allTypes));
            return true;
        }
        ItemStack bow = createBow(bowType);
        Player playerSender = (Player) sender;
        playerSender.getInventory().addItem(bow);
        playerSender.sendMessage(StrangeArrows.orangeText("Bow has been successfully moved to your inventory. Have fun!"));
        return true;
    }
}
