package ru.evgesha728.moontpbow;

import java.time.Duration;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public final class MoonTPBowConfig
{
	private final Duration cooldown;
    private final String bowTitle;
    private final String shootSound;
    private final String noPermission;
    private final String noFreeSpace;
    private final String bowGiven;
    private final String bowTaken;
    private final String inCooldown;

	public Duration getCooldown() {
		return this.cooldown;
	}

	public String getBowTitle() {
		return this.bowTitle;
	}
	
	public String getShootSound() {
		return this.shootSound;
	}

	public String getNoPermission() {
		return this.noPermission;
	}

	public String getNoFreeSpace() {
		return this.noFreeSpace;
	}

	public String getBowGiven() {
		return this.bowGiven;
	}

	public String getBowTaken() {
		return this.bowTaken;
	}
	
	public String getInCooldown() {
		return this.inCooldown;
	}

    public final String q(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    
	public MoonTPBowConfig(final ConfigurationSection c) {
		this.cooldown = Duration.ofSeconds(c.getLong("cooldown"));
		this.bowTitle = q(c.getString("bowTitle"));
		this.shootSound = c.getString("shootSound");
		this.noPermission = q(c.getString("messages.noPermission"));
		this.noFreeSpace = q(c.getString("messages.noFreeSpace"));
		this.bowGiven = q(c.getString("messages.bowGiven"));
		this.bowTaken = q(c.getString("messages.bowTaken"));
		this.inCooldown = q(c.getString("messages.inCooldown"));
	}
}
