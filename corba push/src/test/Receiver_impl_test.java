package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import dialogue.notExistingPseudo;
import dialogueApp.Receiver_impl;

public class Receiver_impl_test {

	@Test
	public void testReceive() {
		Receiver_impl receiverImpl = new Receiver_impl();
		//le client reçoit le message "message de l'émetteur" envoyé par le client "émetteur"
		receiverImpl.receive("émetteur", "message de l'émetteur");
		assertTrue(receiverImpl.getMapMessages().containsKey("émetteur"));
		assertTrue(receiverImpl.getMapMessages().get("émetteur").contains("message de l'émetteur"));
	}

	@Test
	public void testInitClients() {
		Receiver_impl receiverImpl = new Receiver_impl();
		String[] clients = {"client 1", "client 2", "client 3"};
		//on initialise la liste des clients avec "client 1", "client 2" et "client 3"
		receiverImpl.initClients(clients);
		ArrayList<String> listeClients = new ArrayList<String>();
		listeClients.add("client 1");
		listeClients.add("client 2");
		listeClients.add("client 3");
		assertEquals(receiverImpl.getListeClients(), listeClients);
	}

	@Test
	public void testAddClient() throws notExistingPseudo {
		Receiver_impl receiverImpl = new Receiver_impl();
		//on ajoute le client 1 à la liste des clients auparavant vide
		receiverImpl.addClient("client 1");
		ArrayList<String> listeClients = new ArrayList<String>();
		listeClients.add("client 1");
		assertEquals(receiverImpl.getListeClients(), listeClients);
	}

	@Test
	public void testRemClient() throws notExistingPseudo {
		Receiver_impl receiverImpl = new Receiver_impl();
		receiverImpl.addClient("client 1");
		receiverImpl.addClient("client 2");
		receiverImpl.addClient("client 3");
		//on enlève le client 2 à la liste des clients
		receiverImpl.remClient("client 2");
		ArrayList<String> listeClients = new ArrayList<String>();
		listeClients.add("client 1");
		listeClients.add("client 3");
		assertEquals(receiverImpl.getListeClients(), listeClients);
	}

}
