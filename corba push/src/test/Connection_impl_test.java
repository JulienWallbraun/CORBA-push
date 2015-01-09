package test;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

import dialogue.Connection;
import dialogue.ConnectionHelper;
import dialogue.Emitter;
import dialogue.Receiver;
import dialogueApp.Connection_impl;
import dialogueApp.Emitter_impl;
import dialogueApp.Receiver_impl;

public class Connection_impl_test {
    private final static Logger LOGGER = Logger.getLogger(Connection_impl_test.class.getName());

    private static Receiver_impl receiverImpl = new Receiver_impl();
    private static Receiver receiver;
    private static Connection_impl connectionImpl = null;

    @Test
    public void testConnect() {
        String[] tabPseudosClientsAConnecter = { "pseudo du client" };

        lancementAppli(tabPseudosClientsAConnecter, null);
        assertTrue(connectionImpl.getMapReceivers().containsKey("pseudo du client"));
        assertTrue(connectionImpl.getMapEmitters().containsKey("pseudo du client"));

    }

    @Test
    public void testDisconnect() {
        String[] tabPseudosClientsAConnecter = { "pseudo du client" };
        String[] tabPseudosClientsADeconnecter = { "pseudo du client" };

        lancementAppli(tabPseudosClientsAConnecter, tabPseudosClientsADeconnecter);
        assertTrue(!connectionImpl.getMapReceivers().containsKey("pseudo du client"));
        assertTrue(!connectionImpl.getMapEmitters().containsKey("pseudo du client"));
    }

    /*
     * m�thodes auxiliaires permettant de lancer l'application et de connecter/d�connecter des clients
     */
    public void lancementAppli(String[] tabPseudosClientsConnexion, String[] tabPseudosClientsDeconnexion) {
        java.util.Properties props = System.getProperties();
        int status = 0;
        ORB orb = null;
        try {
            orb = ORB.init((String[]) null, props);
            org.omg.CORBA.Object obj;
            org.omg.PortableServer.POA rootPOA = org.omg.PortableServer.POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

            org.omg.PortableServer.POAManager manager = rootPOA.the_POAManager();

            // cr�ation et activation du servant connexion
            connectionImpl = new Connection_impl();
            Connection connection = connectionImpl._this(orb);

            // cr�ation et activation du servant emitter
            Emitter_impl emitterImpl = new Emitter_impl(connectionImpl, null);
            Emitter emitter = emitterImpl._this(orb);

            // utilisation d'un name service
            obj = orb.resolve_initial_references("NameService");
            NamingContext ctx = NamingContextHelper.narrow(obj);
            if (ctx == null) {
                System.out.println("Le composant NameService n'est pas un repertoire");
            }

            NameComponent[] name = new NameComponent[1];

            // ajout du binding de type connexion dans le naming context
            name[0] = new NameComponent("Connection", "");
            ctx.rebind(name, connection);

            // ajout du binding de type connexion dans le naming context
            name[0] = new NameComponent("Emitter", "");
            ctx.rebind(name, emitter);

            System.out.println("Serveur d�marr� normalement...");

            manager.activate();

            connectAndDisconnectClients(tabPseudosClientsConnexion, tabPseudosClientsDeconnexion);

        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "context", ex);
            status = 1;
        }

        if (orb != null) {
            try {
                orb.destroy();
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "context", ex);
                status = 1;
            }
        }
    }

    public void connectAndDisconnectClients(String[] tabPseudosClientsConnexion, String[] tabPseudosClientsDeconnexion) {
        org.omg.CORBA.ORB orb = null;

        try {
            orb = ORB.init((String[]) null, null);

            org.omg.PortableServer.POA rootPOA = null;
            try {
                rootPOA = org.omg.PortableServer.POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            } catch (InvalidName e1) {
                LOGGER.log(Level.WARNING, "context", e1);
            }
            org.omg.PortableServer.POAManager manager = rootPOA.the_POAManager();

            // r�cup�ration du name service
            org.omg.CORBA.Object obj = null;
            try {
                obj = orb.resolve_initial_references("NameService");
            } catch (InvalidName e) {
                LOGGER.log(Level.WARNING, "context", e);
                System.exit(1);
            }
            NamingContext ctx = NamingContextHelper.narrow(obj);
            if (ctx == null) {
                System.out.println("Le composant NameService n'est pas un repertoire");
                System.exit(1);
            }

            NameComponent[] name = new NameComponent[1];

            // r�cup�ration de l'objet connection
            name[0] = new NameComponent("Connection", "");
            try {
                obj = ctx.resolve(name);
            } catch (Exception e) {
                System.out.println("Composant inconnu");
                LOGGER.log(Level.WARNING, "context", e);
                System.exit(1);
            }
            Connection connection = ConnectionHelper.narrow(obj);

            try {
                manager.activate();
            } catch (AdapterInactive e) {
                LOGGER.log(Level.WARNING, "context", e);
            }

            receiver = receiverImpl._this(orb);

            // connexion des clients
            if (tabPseudosClientsConnexion != null) {
                for (String pseudoClient : tabPseudosClientsConnexion) {
                    connection.connect(pseudoClient, receiver);
                }
            }

            // d�connexion des clients
            if (tabPseudosClientsDeconnexion != null) {
                for (String pseudoClient : tabPseudosClientsDeconnexion) {
                    connection.disconnect(pseudoClient);
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "context", ex);
        }

        if (orb != null) {
            try {
                orb.destroy();
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "context", ex);
            }
        }

    }
}
