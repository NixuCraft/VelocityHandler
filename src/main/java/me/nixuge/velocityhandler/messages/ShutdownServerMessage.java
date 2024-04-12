package me.nixuge.velocityhandler.messages;

import me.nixuge.velocityhandler.message.AMessage;

public class ShutdownServerMessage extends AMessage {
    public ShutdownServerMessage(String serverToShutdown) {
        this(serverToShutdown, 0);
    }
    public ShutdownServerMessage(String serverToShutdown, int delayInSeconds) {
        super("ShutdownServer");
        out.writeUTF(serverToShutdown);
        out.writeInt(delayInSeconds);
    }
}
