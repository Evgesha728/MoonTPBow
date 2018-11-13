package ru.evgesha728.moontpbow;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
public class MoonTPBow extends JavaPlugin implements Listener
{
    private final MoonTPBowConfig c;
    public static MoonTPBow INSTANCE;
    private final ItemStack bowStack;
    public static final Companion Companion;
    private final HashMap<UUID, Instant> cooldown;

    public final ItemStack getBowStack() {
        return this.bowStack;
    }
    
    public final MoonTPBowConfig getC() {
    	return this.c;
    }
    
    @Override
    public void onEnable() {
    	super.onEnable();
		this.saveDefaultConfig();
		MoonTPBow.Companion.setINSTANCE(this);
        this.bowStack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        final ItemMeta bow = this.bowStack.getItemMeta();
        bow.setDisplayName(getC().getBowTitle());
        bow.spigot().setUnbreakable(true);
        bow.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE });
        this.bowStack.setItemMeta(bow);
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("tpbow").setExecutor((CommandExecutor)new MoonTPBowCommand(this));
    }
    
    public final boolean isTeleportBow(final ItemStack stack) {
        return (stack == null) ? (this.bowStack == null) : stack.equals(this.bowStack);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public final void onDrop(final PlayerDropItemEvent event) {
        final ItemStack itemStack = event.getItemDrop().getItemStack();
        if (this.isTeleportBow(itemStack)) {
            event.getItemDrop().remove();
            event.getPlayer().sendMessage(getC().getBowTaken());
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public final void onBowShoot(final EntityShootBowEvent event) {
        final ItemStack bow = event.getBow();
        if (!this.isTeleportBow(bow)) {
            return;
        }
        Object entity;
        if (!((entity = event.getEntity()) instanceof Player)) {
        	return;
        }
        final Player player = (Player)entity;
        if(!player.hasPermission("MoonTPBow.Use")) {
        	player.sendMessage(getC().getNoPermission());
        	event.setCancelled(true);
        	return;
        }
        final Instant now = Instant.now();
        final Instant cd = this.cooldown.get(player.getUniqueId());
        if (cd != null && now.compareTo(cd) < 0) {
        	event.setCancelled(true);
        	player.sendMessage(getC().getInCooldown().replaceAll("%time%", readable(Duration.between(now, cd))));
        	return;
        }
        final HashMap<UUID, Instant> hashMap = this.cooldown;
        final UUID uuid = player.getUniqueId();
        event.setCancelled(true);
        ((EnderPearl)player.launchProjectile((Class)EnderPearl.class)).setVelocity(event.getProjectile().getVelocity().multiply(1.1));
        player.playSound(player.getLocation(), Sound.valueOf(getC().getShootSound()), 1.0f, 1.0f);
        final Instant plus = now.plus((TemporalAmount)getC().getCooldown());
        hashMap.put(uuid, plus);
    }
    
    public static final String readable(final Duration duration) {
        final long seconds = duration.getSeconds() % 60L;
        if(seconds == 0L) { return "менее секунды"; }
    	return seconds + " сек";
    }
    
    static {
        Companion = new Companion(null);
    }
    
    public static final class Companion
    {
        public final MoonTPBow getINSTANCE() {
            final MoonTPBow moonTPBow = MoonTPBow.INSTANCE;
            if(moonTPBow != null) {
    			return moonTPBow;
            }
			return null;
        }
        
        public final void setINSTANCE(final MoonTPBow moonTPBow) {
        	MoonTPBow.INSTANCE = moonTPBow;
        }
        
        private Companion() {
        }
        
        public Companion(final DefaultConstructorMarker constructor_marker) {
            this();
        }
    }
    
    public MoonTPBow() {
        this.c = new MoonTPBowConfig((ConfigurationSection)this.getConfig());
        this.bowStack = new ItemStack(Material.BOW, 1);
        this.cooldown = new HashMap<UUID, Instant>(this.getServer().getMaxPlayers());
    }
}
