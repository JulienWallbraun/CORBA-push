package dialogueApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import dialogue.ConnectionPOA;
import dialogue.Emitter;
import dialogue.EmitterHelper;
import dialogue.Receiver;
import dialogue.notExistingPseudo;

public class Connection_impl extends ConnectionPOA {
    static final private Logger LOGGER = Logger.getLogger(Server.class.getName());

    private Map<String, Emitter> mapEmitters = new HashMap<String, Emitter>();
    private Map<String, Receiver> mapReceivers = new HashMap<String, Receiver>();

    public Map<String, Emitter> getMapEmitters() {
        return mapEmitters;
    }

    public void setMapEmitters(Map<String, Emitter> mapEmitters) {
        this.mapEmitters = mapEmitters;
    }

    public Map<String, Receiver> getMapReceivers() {
        return mapReceivers;
    }

    public void setMapReceivers(Map<String, Receiver> mapReceivers) {
        this.mapReceivers = mapReceivers;
    }

    @Override
    public Emitter connect(String pseudo, Receiver rcv) {
        // création de l'emitter et ajout du client à la liste des emitters
        Emitter_impl emitterImpl = new Emitter_impl(this, pseudo);
        Emitter emitter = null;
        try {
            emitter = EmitterHelper.narrow(_default_POA().servant_to_reference(emitterImpl));
        } catch (ServantNotActive e) {
            LOGGER.log(Level.WARNING, "context", e);
        } catch (WrongPolicy e) {
            LOGGER.log(Level.WARNING, "context", e);
        }
        mapEmitters.put(pseudo, emitter);

        // ajout du nouveau client pour chacun des receivers
        for (Receiver receiver : mapReceivers.values()) {
            try {
                receiver.addClient(pseudo);
            } catch (notExistingPseudo e) {
                LOGGER.log(Level.WARNING, "context", e);
            }
        }

        // mise à jour (initialisation) de la liste des clients dans le nouveau receiver
        ArrayList<String> listeClients = new ArrayList<String>();
        if (!mapReceivers.isEmpty()) {
            listeClients.addAll(mapReceivers.keySet());
        }

        // on ajoute le client lui m�me � la liste pour qu'il puisse s'envoyer des messages � lui-m�me
        listeClients.add(pseudo);
        String[] tableauListeClients = new String[listeClients.size()];
        listeClients.toArray(tableauListeClients);
        rcv.initClients(tableauListeClients);

        // ajout du nouveau receiver � la liste des receivers
        mapReceivers.put(pseudo, rcv);

        System.out.println("le client '" + pseudo + "' s'est connecté.");

        // retour de l'emitter créé
        return emitter;
    }

    @Override
    public void disconnect(String pseudo) throws dialogue.notExistingPseudo {
        for (Receiver receiver : mapReceivers.values()) {
            receiver.remClient(pseudo);
        }
        mapEmitters.remove(pseudo);
        mapReceivers.remove(pseudo);

        System.out.println("le client '" + pseudo + "' s'est déconnecté.");
    }

}
