package ru.evgesha728.moontpbow;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class MoonTPBowCommand implements CommandExecutor
{
    private final MoonTPBow plugin;
    
	@Override
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly for players");
            return true;
        }
        if (args.length == 0) {
         if(!sender.hasPermission("MoonTPBow.Use")) {
        	sender.sendMessage(this.getPlugin().getC().getNoPermission());
        	return true;
         }
         if(((Player) sender).getInventory().contains(this.getPlugin().getBowStack())) {
             for(ItemStack bow : ((Player) sender).getInventory().all(this.getPlugin().getBowStack()).values()) {
                 ((Player) sender).getInventory().remove(bow);
        	 }
             sender.sendMessage(this.getPlugin().getC().getBowTaken());
             return true;
        	 }
         if (((Player) sender).getInventory().firstEmpty() == -1) {
   		     sender.sendMessage(this.getPlugin().getC().getNoFreeSpace());
   		     return true;
         }
	   	 ((Player) sender).getInventory().addItem(this.getPlugin().getBowStack());
		 sender.sendMessage(this.getPlugin().getC().getBowGiven());
		 return true;
        }
        final String action = args[0].toLowerCase();
        switch (action) {
            case "info": {
                sender.sendMessage("§eПлагин разработан сообществом §bMoonStudio §e& §bEvgesha728\n§eЕсли Вы нашли баг или уязвимость, незамедлительно сообщите нам!\n§9§lVK §f- §7§ovk.com/moonstudio_mс");
                break;
            }
            default: {
            	sender.sendMessage(this.getPlugin().getC().q("&cНеизвестное действие: &f" + action));
            	break;
            }
        }
		return false;
	}

	public MoonTPBow getPlugin() {
		return this.plugin;
	}
	
	public MoonTPBowCommand(final MoonTPBow plugin) {
		this.plugin = plugin;
	}
}
