package me.nixuge.velocityhandler.messages;

import me.nixuge.velocityhandler.message.AMessage;

public class PlayerSwitchServerMessage extends AMessage {
    public PlayerSwitchServerMessage(String server) {
        super("PlayerSwitchServer");

        out.writeUTF(server);
    }
}
