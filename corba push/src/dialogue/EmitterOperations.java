package dialogue;


/**
* dialogue/EmitterOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from emitter.idl
* samedi 3 janvier 2015 12 h 03 CET
*/

public interface EmitterOperations 
{
  void sendMessage (String to, String message) throws dialogue.notExistingPseudo;
} // interface EmitterOperations