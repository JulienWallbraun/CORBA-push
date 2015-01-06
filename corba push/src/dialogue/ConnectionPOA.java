package dialogue;


/**
* dialogue/ConnectionPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from connection.idl
* samedi 3 janvier 2015 11 h 54 CET
*/

public abstract class ConnectionPOA extends org.omg.PortableServer.Servant
 implements dialogue.ConnectionOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("connect", new java.lang.Integer (0));
    _methods.put ("disconnect", new java.lang.Integer (1));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // dialogue/Connection/connect
       {
         String pseudo = in.read_string ();
         dialogue.Receiver rcv = dialogue.ReceiverHelper.read (in);
         dialogue.Emitter $result = null;
         $result = this.connect (pseudo, rcv);
         out = $rh.createReply();
         dialogue.EmitterHelper.write (out, $result);
         break;
       }

       case 1:  // dialogue/Connection/disconnect
       {
         try {
           String pseudo = in.read_string ();
           this.disconnect (pseudo);
           out = $rh.createReply();
         } catch (dialogue.notExistingPseudo $ex) {
           out = $rh.createExceptionReply ();
           dialogue.notExistingPseudoHelper.write (out, $ex);
         }
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:dialogue/Connection:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Connection _this() 
  {
    return ConnectionHelper.narrow(
    super._this_object());
  }

  public Connection _this(org.omg.CORBA.ORB orb) 
  {
    return ConnectionHelper.narrow(
    super._this_object(orb));
  }


} // class ConnectionPOA
