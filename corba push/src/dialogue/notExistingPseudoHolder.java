package dialogue;

/**
* dialogue/notExistingPseudoHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from receiver.idl
* samedi 3 janvier 2015 12 h 04 CET
*/

public final class notExistingPseudoHolder implements org.omg.CORBA.portable.Streamable
{
  public dialogue.notExistingPseudo value = null;

  public notExistingPseudoHolder ()
  {
  }

  public notExistingPseudoHolder (dialogue.notExistingPseudo initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = dialogue.notExistingPseudoHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    dialogue.notExistingPseudoHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return dialogue.notExistingPseudoHelper.type ();
  }

}