package test;

import static org.junit.Assert.*;

import java.util.Scanner;

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
import dialogue.notExistingPseudo;
import dialogueApp.Connection_impl;
import dialogueApp.Emitter_impl;
import dialogueApp.Receiver_impl;
import dialogueApp.Server;

public class Connection_impl_test {

	private static Receiver_impl receiverImpl = new Receiver_impl();
	private static Receiver receiver;
	private static Connection_impl connectionImpl = null;
	
	@Test
	public void testConnect() {	
		String[] tabPseudosClientsAConnecter = {"pseudo du client"};
		
		lancementAppli(tabPseudosClientsAConnecter, null);
		assertTrue(connectionImpl.getMapReceivers().containsKey("pseudo du client"));
		assertTrue(connectionImpl.getMapEmitters().containsKey("pseudo du client"));
		
	}

	@Test
	public void testDisconnect() {
		String[] tabPseudosClientsAConnecter = {"pseudo du client"};
		String[] tabPseudosClientsADeconnecter = {"pseudo du client"};
		
		lancementAppli(tabPseudosClientsAConnecter, tabPseudosClientsADeconnecter);
		assertTrue(!connectionImpl.getMapReceivers().containsKey("pseudo du client"));
		assertTrue(!connectionImpl.getMapEmitters().containsKey("pseudo du client"));
	}
	
	
	/*
	 * méthodes auxiliaires permettant de lancer l'application et de connecter/déconnecter des clients
	 */
	public void lancementAppli(String[] tabPseudosClientsConnexion, String[] tabPseudosClientsDeconnexion){
		java.util.Properties props = System.getProperties();
		int status = 0;
		ORB orb = null;
		try
		{
			orb = ORB.init((String[]) null, props);
			org.omg.CORBA.Object obj;
			org.omg.PortableServer.POA rootPOA = org.omg.PortableServer.POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

			org.omg.PortableServer.POAManager manager = rootPOA.the_POAManager();

			//création et activation du servant connexion
			connectionImpl = new Connection_impl();
			Connection connection = connectionImpl._this(orb);
			
			//création et activation du servant emitter
			Emitter_impl emitterImpl = new Emitter_impl(connectionImpl, null);
			Emitter emitter = emitterImpl._this(orb);
			
			//utilisation d'un name service
			obj=orb.resolve_initial_references("NameService");
			NamingContext ctx = NamingContextHelper.narrow(obj);
			if (ctx==null)
			{
				System.out.println("Le composant NameService n'est pas un repertoire");
			}

			NameComponent[] name = new NameComponent[1];

			//ajout du binding de type connexion dans le naming context
			name[0]=new NameComponent("Connection","");
			ctx.rebind(name,connection);

			//ajout du binding de type connexion dans le naming context
			name[0]=new NameComponent("Emitter","");
			ctx.rebind(name,emitter);

			System.out.println("Serveur démarré normalement...");


			manager.activate();
			
			connectAndDisconnectClients(tabPseudosClientsConnexion, tabPseudosClientsDeconnexion);
		

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			status = 1;
		}

		if(orb != null)
		{
			try
			{
				orb.destroy();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				status = 1;
			}
		}
	}

	public void connectAndDisconnectClients(String[] tabPseudosClientsConnexion, String[] tabPseudosClientsDeconnexion){
		org.omg.CORBA.ORB orb = null;


		try {
			orb = ORB.init((String[]) null, null);


			org.omg.PortableServer.POA rootPOA = null;
			try {
				rootPOA = org.omg.PortableServer.POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			} catch (InvalidName e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			org.omg.PortableServer.POAManager manager = rootPOA.the_POAManager(); 

			//récupération du name service
			org.omg.CORBA.Object obj = null;
			try
			{
				obj=orb.resolve_initial_references("NameService");
			}
			catch(InvalidName e)
			{
				e.printStackTrace();
				System.exit(1);
			}
			NamingContext ctx = NamingContextHelper.narrow(obj);
			if (ctx==null)
			{
				System.out.println("Le composant NameService n'est pas un repertoire");
				System.exit(1);
			}

			NameComponent[] name = new NameComponent[1];

			//récupération de l'objet connection		
			name[0]=new NameComponent("Connection","");
			try
			{
				obj = ctx.resolve(name);
			}
			catch (Exception e)
			{
				System.out.println("Composant inconnu");
				e.printStackTrace();
				System.exit(1);
			}		
			Connection connection = ConnectionHelper.narrow(obj);

			try {
				manager.activate();
			} catch (AdapterInactive e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			receiver = receiverImpl._this(orb);
			
			//connexion des clients
			if (tabPseudosClientsConnexion != null){
				for (String pseudoClient : tabPseudosClientsConnexion){
					connection.connect(pseudoClient, receiver);	
				}
			}
			
			//déconnexion des clients
			if (tabPseudosClientsDeconnexion != null){
				for (String pseudoClient : tabPseudosClientsDeconnexion){			
					connection.disconnect(pseudoClient);	
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (orb != null) {
			try {
				orb.destroy();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}
}
