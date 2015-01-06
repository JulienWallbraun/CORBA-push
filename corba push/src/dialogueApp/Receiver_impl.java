package dialogueApp;

import java.util.ArrayList;
import java.util.HashMap;

import dialogue.ReceiverPOA;
import dialogue.notExistingPseudo;



public class Receiver_impl extends ReceiverPOA {
	private ArrayList<String> listeClients = new ArrayList<String>();
	private HashMap<String,ArrayList<String>> mapMessages = new HashMap<String,ArrayList<String>>();
	

	public ArrayList<String> getListeClients() {
		return listeClients;
	}



	public void setListeClients(ArrayList<String> listeClients) {
		this.listeClients = listeClients;
	}



	public HashMap<String, ArrayList<String>> getMapMessages() {
		return mapMessages;
	}



	public void setMapMessages(HashMap<String, ArrayList<String>> mapMessages) {
		this.mapMessages = mapMessages;
	}



	@Override
	public void receive(String from, String message) {
		ArrayList<String> listeMessageDeLEmetteurRecus = new ArrayList<String>();
		//si le destinataire a déjà reçu des messages de l'émetteur, on récupère cette liste de messages
		if (mapMessages.containsKey(from)){
			listeMessageDeLEmetteurRecus = mapMessages.get(from);
		}
		//on ajoute le nouveau message à la liste des messages reçus de l'émetteur
		listeMessageDeLEmetteurRecus.add(message);
		mapMessages.put(from, listeMessageDeLEmetteurRecus);		
		
		System.out.println("message reçu du client '"+from+"' : "+message+"\n");
	}

	@Override
	public void initClients(String[] clients) {
		listeClients = new ArrayList<String>();
		if (clients != null){
			for (String client : clients){
				listeClients.add(client);
			}
		}
		afficherClientsConnectes();
	}

	@Override
	public void addClient(String client) throws notExistingPseudo {
		listeClients.add(client);
		System.out.println("un nouveau client s'est connecté avec le pseudo : '"+client+"', vous pouvez désormais communiquer avec lui!\n");
		afficherClientsConnectes();
	}

	@Override
	public void remClient(String client) throws notExistingPseudo {
		listeClients.remove(client);
			System.out.println("le client '"+client+"' s'est déconnecté : il n'est plus possible de communiquer avec lui!\n");
			afficherClientsConnectes();
	}
	
	public void afficherClientsConnectes(){
		System.out.println("Liste des clients actuellement connectés :");
		for (String client : listeClients){
			System.out.println(client);
		}
		System.out.println();
	}
}
