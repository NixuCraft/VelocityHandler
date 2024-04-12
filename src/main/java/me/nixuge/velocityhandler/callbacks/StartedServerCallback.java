package me.nixuge.velocityhandler.callbacks;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataInput;

import me.nixuge.velocityhandler.callback.IPacketCallback;

public class StartedServerCallback implements IPacketCallback {
    
    public interface IStartedServerReceiver {
        public void setServerName(String serverName);
    }

    private final IStartedServerReceiver room;
    public StartedServerCallback(IStartedServerReceiver room) {
        this.room = room;
    }

    @Override
    public void processCallback(Player p, ByteArrayDataInput in) {
        // Force start from a command or somewhere else.
        if (room == null)
            return;
        
        String serverName = in.readUTF();
        in.readUTF(); //empty buffer, port in there, unused.

        room.setServerName(serverName);
    }
}
