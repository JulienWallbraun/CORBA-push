package dialogue;

/**
 * dialogue/EmitterHelper.java . Generated by the IDL-to-Java compiler (portable), version "3.2" from emitter.idl samedi 3 janvier 2015 12 h 03 CET
 */

public abstract class EmitterHelper {
    private static String _id = "IDL:dialogue/Emitter:1.0";

    public static void insert(org.omg.CORBA.Any a, dialogue.Emitter that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static dialogue.Emitter extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    synchronized public static org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            __typeCode = org.omg.CORBA.ORB.init().create_interface_tc(dialogue.EmitterHelper.id(), "Emitter");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static dialogue.Emitter read(org.omg.CORBA.portable.InputStream istream) {
        return narrow(istream.read_Object(_EmitterStub.class));
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, dialogue.Emitter value) {
        ostream.write_Object((org.omg.CORBA.Object) value);
    }

    public static dialogue.Emitter narrow(org.omg.CORBA.Object obj) {
        if (obj == null)
            return null;
        else if (obj instanceof dialogue.Emitter)
            return (dialogue.Emitter) obj;
        else if (!obj._is_a(id()))
            throw new org.omg.CORBA.BAD_PARAM();
        else {
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
            dialogue._EmitterStub stub = new dialogue._EmitterStub();
            stub._set_delegate(delegate);
            return stub;
        }
    }

    public static dialogue.Emitter unchecked_narrow(org.omg.CORBA.Object obj) {
        if (obj == null)
            return null;
        else if (obj instanceof dialogue.Emitter)
            return (dialogue.Emitter) obj;
        else {
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
            dialogue._EmitterStub stub = new dialogue._EmitterStub();
            stub._set_delegate(delegate);
            return stub;
        }
    }

}
