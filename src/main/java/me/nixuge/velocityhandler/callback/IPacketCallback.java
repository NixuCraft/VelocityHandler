package me.nixuge.velocityhandler.callback;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataInput;

public interface IPacketCallback {
    public void processCallback(Player p, ByteArrayDataInput in);
}
