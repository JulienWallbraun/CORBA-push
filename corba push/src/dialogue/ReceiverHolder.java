package dialogue;

/**
* dialogue/ReceiverHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from receiver.idl
* samedi 3 janvier 2015 12 h 04 CET
*/

public final class ReceiverHolder implements org.omg.CORBA.portable.Streamable
{
  public dialogue.Receiver value = null;

  public ReceiverHolder ()
  {
  }

  public ReceiverHolder (dialogue.Receiver initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = dialogue.ReceiverHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    dialogue.ReceiverHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return dialogue.ReceiverHelper.type ();
  }

}
