package me.nixuge.velocityhandler.messages;

import me.nixuge.velocityhandler.message.AMessage;

public class ShutdownSelfMessage extends AMessage {
    public ShutdownSelfMessage(int delaySeconds) {
        super("ShutdownSelf");
        out.writeInt(delaySeconds);
    }
}
