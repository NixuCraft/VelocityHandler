package me.nixuge.velocityhandler.message;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.nixuge.velocityhandler.VelocityHandler;

public abstract class AMessage {
    private final VelocityHandler handlerInstance;
    protected final ByteArrayDataOutput out;

    public AMessage(String packetName) {
        handlerInstance = VelocityHandler.getInstance();

        out = ByteStreams.newDataOutput();
        out.writeUTF(packetName);
    } 

    public void sendMessage() {
        sendMessage(Iterables.getFirst(Bukkit.getOnlinePlayers(), null));
    }

    public void sendMessage(Player p) {
        p.sendPluginMessage(handlerInstance.getPlugin(), handlerInstance.getChannel(), out.toByteArray());
    }
}
