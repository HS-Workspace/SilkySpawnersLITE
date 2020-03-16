package us.thezircon.play.silkyspawnerslite.commands.SilkySpawner.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import us.thezircon.play.silkyspawnerslite.SilkySpawnersLITE;
import us.thezircon.play.silkyspawnerslite.commands.CMDManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class give extends CMDManager {

    SilkySpawnersLITE plugin = SilkySpawnersLITE.getPlugin(SilkySpawnersLITE.class);

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "Gives a spawner to yourself or someone else.";
    }

    @Override
    public String getSyntax() {
        return "/Silky give [type] <user>";
    }

    @Override
    public void perform(Player player, String[] args) {

        String msgPrefix = ChatColor.translateAlternateColorCodes('&', plugin.getLangConfig().getString("msgPrefix"));
        String msgNoperm = ChatColor.translateAlternateColorCodes('&', plugin.getLangConfig().getString("msgNoPerms"));
        String msgSpawnerTypeError = ChatColor.translateAlternateColorCodes('&', plugin.getLangConfig().getString("msgSpawnerTypeError"));
        String msgGiveSelf = ChatColor.translateAlternateColorCodes('&', plugin.getLangConfig().getString("msgGiveSelf"));
        String msgGiveOther = ChatColor.translateAlternateColorCodes('&', plugin.getLangConfig().getString("msgGiveOther"));
        String msgReceiveSpawner = ChatColor.translateAlternateColorCodes('&', plugin.getLangConfig().getString("msgReceiveSpawner"));

        if (args.length>0){
            //Get Spawner Type
            String mobtype = args[1].toUpperCase();

            //Give or Drop Spawner
            ItemStack spawner_to_give = new ItemStack(Material.SPAWNER);
            BlockStateMeta meta = (BlockStateMeta) spawner_to_give.getItemMeta();
            CreatureSpawner csm = (CreatureSpawner) meta.getBlockState();

            csm.setSpawnedType(EntityType.valueOf(mobtype));

            meta.setBlockState(csm);
            meta.setDisplayName(ChatColor.AQUA + mobtype + " Spawner");
            meta.addItemFlags();
            spawner_to_give.setItemMeta(meta);

            if (args.length==2 && player.hasPermission("silkyspawners.give.self")) { // No User Name
                player.getInventory().addItem(spawner_to_give);
                player.sendMessage(msgPrefix+ " " + msgGiveSelf.replace("{TYPE}", mobtype));
            } else if (args.length==3 && player.hasPermission("silkyspawners.give.other")) { // User Name
                Player target = Bukkit.getPlayer(args[2]);

                target.getInventory().addItem(spawner_to_give);

                player.sendMessage(msgPrefix+" " + msgGiveOther.replace("{TYPE}", mobtype).replace("{TARGET}", target.getName().toString()));
                target.sendMessage(msgPrefix+" "+msgReceiveSpawner.replace("{TYPE}", mobtype));
            } else {
                player.sendMessage(msgPrefix+" "+msgNoperm);
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return Arrays.stream(EntityType.values()).map(EntityType::name).collect(Collectors.toList());
    }
}
