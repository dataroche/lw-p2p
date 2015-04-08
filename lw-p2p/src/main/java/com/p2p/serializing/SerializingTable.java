package com.p2p.serializing;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

import com.backends.MessageInfo;

/**
 * A table of serializers. Keeps track of opcodes and assures their uniqueness. 
 * Used to write and read any object to a ByteBuf stream, as long as a correct serializer is present.
 * 
 * <p>
 * Default serializable objects (Use {@link SerializingTableBuilder} ):<br>
 * {@link String}<br>
 * {@link Password}<br>
 * {@link SocketId}<br>
 * {@link ConnectionAttempt}<br>
 * {@link ConnectionAnswer}<br>
 * {@link NetworkInformation}<br>
 * 
 * @author William Laroche
 *
 */
public class SerializingTable{
	public static class OpcodeAlreadyUsedException extends RuntimeException{
		/**
		 * 
		 */
		private static final long serialVersionUID = -9141347939330500865L;

		OpcodeAlreadyUsedException(int opcode, Class<?> clazz){
			super("The opcode returned by "+ clazz + ", " + opcode + ", is already in use. Choose another one.");
		}
	}
	
	public static class OpcodeNonConstantException extends RuntimeException{
		/**
		 * 
		 */
		private static final long serialVersionUID = -4327877279588257455L;

		OpcodeNonConstantException(Class<?> clazz){
			super("The opcode returned by "+ clazz + " varies from call to call. Make it constant.");
		}
	}
	
	public static class OpcodeUnknownException extends RuntimeException{
		
		private static final long serialVersionUID = 4347023305348019181L;

		OpcodeUnknownException(int opcode){
			super("The opcode " + opcode + " has no linked NetworkSerializer.");
		}
	}

	public static class NoSerializerForSerializableException extends RuntimeException{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 6540734466880767243L;

		NoSerializerForSerializableException(Class<?> clazz){
			super("A serializable object is requesting a serializer of type " + clazz + ", but it is not in the table.");
		}
	}
	
	private SerializerMap serializerMap;
	
	public SerializingTable(){
		serializerMap = new SerializerMap();
	}
	
	public void write(ByteBuf buffer, Object object){
		if(!canWrite(object)) throw new NoSerializerForSerializableException(object.getClass());
		
		NetworkSerializer<?> specificSerializer = serializerMap.getSerializerFor(object.getClass());
		
		//Write opcode
		buffer.writeShort(specificSerializer.getOpcode());
		
		//Write object
		specificSerializer.encodeCast(buffer, object, this);
	}
	
	public Object readNext(ByteBuf buffer, MessageInfo info, boolean notifyListeners) throws OpcodeUnknownException{
		//Read opcode
		int opcode = buffer.readShort();
		NetworkSerializer<?> serializer = getSerializer(opcode);
		
		//Read object
		return serializer.decode(buffer, info, this, notifyListeners);
	}
	
	/**Reads one object, without notifying listeners.
	 * 
	 * @param buffer
	 * @return
	 * @throws OpcodeUnknownException
	 */
	public Object readSimple(ByteBuf buffer) throws OpcodeUnknownException{
		return readNext(buffer, null, false);
	}
	
	/**
	 * Reads one object from the buffer.
	 * 
	 * @param buffer The buffer on which to read on
	 * @param info Message information to be sent to listeners
	 * @return The object that was read. Listeners will also be notified nonetheless.
	 * @throws OpcodeUnknownException The opcode that was read has no linked Serializer
	 */
	public Object readNext(ByteBuf buffer, MessageInfo info) throws OpcodeUnknownException{
		return readNext(buffer, info, true);
		
	}
	
	public Object readAll(ByteBuf buffer, MessageInfo info) throws OpcodeUnknownException{
		Object last = null;
		while(canRead(buffer))
			last = readNext(buffer, info);
		return last;
	}
	
	public boolean canRead(ByteBuf buffer){
		boolean canRead = false;
		if(buffer.isReadable()){
			buffer.markReaderIndex();
			short opcode = buffer.readShort();
			canRead = opcodeExists(opcode);
			buffer.resetReaderIndex();
		}
		return canRead;
	}
	
	public boolean canWrite(Object object){
		return canWrite(object.getClass());
	}
	
	public boolean canWrite(Class<?> objectClass){
		return serializerMap.containsSerializerFor(objectClass);
	}
	
	public <Type extends NetworkSerializer<?>> Type getSerializer(Class<Type> serializerClass){
		if(!serializerMap.contains(serializerClass))
			throw new NoSerializerForSerializableException(serializerClass);
		
		return serializerMap.getSerializer(serializerClass);
	}
	
	private NetworkSerializer<?> getSerializer(int opcode){
		if(!opcodeExists(opcode))
			throw new OpcodeUnknownException(opcode);
		return serializerMap.getSerializer(opcode);
	}
	
	public boolean opcodeExists(int opcode){
		
		return serializerMap.contains(opcode);
	}
	
	public void addSerializer(NetworkSerializer<?> serializer){
		int opcode = verifyAndDefineOpcode(serializer);
		verifyAndAddSerializer(opcode, serializer);
	}
	
	private int verifyAndDefineOpcode(NetworkSerializer<?> serializer){
		int opcode1 = serializer.getOpcode();
		int opcode2 = serializer.getOpcode();
		
		if(opcode1 != opcode2)
			throw new OpcodeNonConstantException(serializer.getClass());
		
		return opcode2;
	}
	
	private void verifyAndAddSerializer(int opcode, NetworkSerializer<?> serializer){
		if(containsDifferentSerializer(opcode, serializer))
			throw new OpcodeAlreadyUsedException(opcode, serializer.getClass());
		
		serializerMap.addMapping(opcode, serializer.getSerializableObjectType(), serializer);
	}
	
	private boolean containsDifferentSerializer(int opcode, NetworkSerializer<?> serializer){
		return serializerMap.contains(opcode) && 
				serializerMap.contains(serializer.getClass());
	}
}
