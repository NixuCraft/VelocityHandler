package me.nixuge.velocityhandler.messages;

import me.nixuge.velocityhandler.VelocityHandler;
import me.nixuge.velocityhandler.callbacks.StartedServerCallback;
import me.nixuge.velocityhandler.callbacks.StartedServerCallback.IStartedServerReceiver;
import me.nixuge.velocityhandler.message.AMessage;

public class RequestNewServerMessage extends AMessage {
    public RequestNewServerMessage(String game, String version, IStartedServerReceiver callbackRoom) {
        this(game, version, callbackRoom, null);
    }
    public RequestNewServerMessage(String game, String version, IStartedServerReceiver callbackRoom, String map) {
        super("RequestNewServer");

        int packetId = VelocityHandler.getInstance().addPacketCallback(new StartedServerCallback(callbackRoom));
        out.writeInt(packetId);
        
        out.writeUTF(game);
        out.writeUTF(version);
        if (map != null)
            out.writeUTF(map);
    }
}
