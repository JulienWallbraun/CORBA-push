package dialogueApp;

import java.util.Scanner;

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

final class Client {
	
	private static Receiver_impl receiverImpl = new Receiver_impl();
	private static Receiver receiver;

	public static void main(String args[]) {

		java.util.Properties props = System.getProperties();

		org.omg.CORBA.ORB orb = null;

		try {
			orb = ORB.init(args, props);
			run(orb);
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

		//	System.exit(status);
	}

	static void run(ORB orb)
	{
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
		
		//saisie pseudo
		@SuppressWarnings("resource")
		Scanner cs = new Scanner(System.in);
		System.out.println("Veuillez saisir votre pseudo :");
		String pseudo = cs.nextLine();
		
		//connexion du client, création de l'emitter et initialisation de la liste des clients connectés
		Emitter emitter = connection.connect(pseudo, receiver);
		
		while(true){
			System.out.println("Pour écrire un message, commencez par écrire le pseudo de votre destinataire ('stop' pour se déconnecter)");
			@SuppressWarnings("resource")
			Scanner in = new Scanner(System.in);
			String destinataire = in.nextLine();
			
			//si le client souhaite se déconnecter
			if (destinataire.equals("stop")){
				try {
					connection.disconnect(pseudo);
				} catch (notExistingPseudo e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			
			else{				
				System.out.println("Ecrivez votre message à destination de '"+destinataire+"'.");
				@SuppressWarnings("resource")
				Scanner inBis = new Scanner(System.in);
				String message = inBis.nextLine();
				try {
					emitter.sendMessage(destinataire, message);
					System.out.println("message envoyé au client '"+destinataire+"' : "+message+"\n");
				} catch (notExistingPseudo e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}
}
