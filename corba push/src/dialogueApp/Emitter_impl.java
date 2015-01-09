package dialogueApp;

import dialogue.EmitterPOA;
import dialogue.Receiver;

public class Emitter_impl extends EmitterPOA {
    private Connection_impl connectionImpl;
    private String pseudo;

    public Connection_impl getConnectionImpl() {
        return connectionImpl;
    }

    public void setConnectionImpl(Connection_impl connectionImpl) {
        this.connectionImpl = connectionImpl;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Emitter_impl() {
        super();
        this.pseudo = "";
        this.connectionImpl = new Connection_impl();
    }

    public Emitter_impl(Connection_impl connectionImpl, String pseudo) {
        super();
        this.pseudo = pseudo;
        this.connectionImpl = connectionImpl;
    }

    @Override
    public void sendMessage(String to, String message) throws dialogue.notExistingPseudo {
        Receiver receiver = connectionImpl.getMapReceivers().get(to);
        receiver.receive(pseudo, message);
    }

}
