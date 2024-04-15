package me.nixuge.velocityhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import lombok.Getter;
import me.nixuge.velocityhandler.callback.IPacketCallback;
import me.nixuge.velocityhandler.messages.Pong;

public class VelocityHandler implements PluginMessageListener {
    private final List<String> callbackPacketNames;
    
    // May cause mem leaks if too many packets waiting without being returned.
    // Anyways, if that's the case, it means there's an error somewhere.
    // Velocity errors should ideally return packets if they're meant to have a callback.
    private final Map<Integer, IPacketCallback> waitingPacketIds;

    @Getter
    private final JavaPlugin plugin;
    @Getter
    private static final Logger logger = Logger.getLogger("VelocityHandler");
    @Getter
    private static VelocityHandler instance = null;
    @Getter
    private final String channel;

    private final Random rand = new Random();


    public int addPacketCallback(IPacketCallback callbackObject) {
        // Get id
        int packetCallbackId = rand.nextInt(Integer.MAX_VALUE);
        while (waitingPacketIds.keySet().contains(packetCallbackId)) 
            packetCallbackId = rand.nextInt(Integer.MAX_VALUE);
        
        // Set in map
        waitingPacketIds.put(packetCallbackId, callbackObject);

        return packetCallbackId;
    }

    public VelocityHandler(JavaPlugin plugin, String channel) {
        this(plugin, channel, new ArrayList<>());
    }

    public VelocityHandler(JavaPlugin plugin, String channel, List<String> callbackPacketNames) {
        if (instance != null)
            throw new RuntimeException("Tried to register another VelocityHandler while one is already registered.");
        instance = this;
        
        this.plugin = plugin;
        this.channel = channel;
        this.callbackPacketNames = callbackPacketNames;
        this.waitingPacketIds = new HashMap<>();

        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, channel, this);
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "global", this); // Messages for every server.
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        // logger.info("Received plugin message packet on channel " + channel);
        // logger.info("Meant for player: " + player);
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String packetName;
        try {
            packetName = in.readUTF();
        } catch(Exception e) {
            logger.warning("Couldn't read packet name !");
            return;
        }
        
        if (callbackPacketNames.contains(packetName))
            processCallback(packetName, player, in);
        else
            processNormal(packetName, player, in);
    }

    // Could split the two below in classes
    private void processCallback(String packetName, Player p, ByteArrayDataInput in) {
        // Get ID & perform checks
        int packetId;
        try {
            packetId = in.readInt();
        } catch(Exception e) {
            logger.warning("Got callback packet " + packetName + " but couldn't read its id.");
            return;
        }
        IPacketCallback callback = waitingPacketIds.remove(packetId);
        if (callback == null) {
            logger.warning("Got callback packet " + packetName + " but its id isn't present in the waiting callbacks.");
            return;
        }

        callback.processCallback(p, in);
    }

    private void processNormal(String packetName, Player p, ByteArrayDataInput in) {
        switch (packetName) {
            case "Ping":
                new Pong().sendMessage();
                break;
            default:
                logger.warning("Packet name not found in processNormal: " + packetName);
                break;
        }
    }
}
