package dialogueApp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import dialogue.Connection;
import dialogue.Emitter;

public class Server {

    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());
    static Connection_impl connectionImpl;

    public static void main(String args[]) {
        java.util.Properties props = System.getProperties();

        int status = 0;

        ORB orb = null;

        try {
            orb = ORB.init(args, props);
            run(orb);
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

        System.exit(status);
    }

    static int run(ORB orb) throws Exception {
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
            return 0;
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
        orb.run();

        return 0;
    }
}
