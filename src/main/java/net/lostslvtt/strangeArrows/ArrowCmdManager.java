package net.lostslvtt.strangeArrows;

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

public class ArrowCmdManager implements CommandExecutor {

    public static ItemStack createArrow(String type) {
        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.getPersistentDataContainer().set(NamespacedKey.fromString("strangearrows"), PersistentDataType.STRING, type);
        arrow.setItemMeta(arrowMeta);

        return arrow;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(StrangeArrows.errorText("Wrong usage. Use /givearrow TYPE"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(StrangeArrows.errorText("Only player can call this command."));
            return true;
        }
        String arrowType = args[0];
        boolean isCorrectType = false;
        for (String type : StrangeArrows.arrowTypes) {
            if (arrowType.equalsIgnoreCase(type)) {
                isCorrectType = true;
            }
        }
        if (!isCorrectType) {
            String allTypes = Arrays.stream(StrangeArrows.arrowTypes).collect(Collectors.joining());
            sender.sendMessage(StrangeArrows.errorText("Invalid type. Choose from " + allTypes));
            return true;
        }
        ItemStack arrow = createArrow(arrowType);
        Player commandSender = (Player) sender;
        commandSender.getInventory().addItem(arrow);
        commandSender.sendMessage(StrangeArrows.orangeText("Arrow has been successfully moved to your inventory. Have fun!"));
        return true;
    }
}
