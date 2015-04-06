package com.p2p.serializing;

import java.util.HashMap;

public class SerializerMap {
	
	private HashMap<Integer, NetworkSerializer<?>> serializers;
	private HashMap<Class<?>, NetworkSerializer<?>> serializerClassMap;
	private HashMap<Class<?>, NetworkSerializer<?>> objectClassMap;
	
	public SerializerMap(){
		serializers = new HashMap<Integer, NetworkSerializer<?>>();
		serializerClassMap = new HashMap<Class<?>, NetworkSerializer<?>>();
		objectClassMap = new HashMap<Class<?>, NetworkSerializer<?>>();
	}
	
	public void addMapping(int opcode, Class<?> objectType, NetworkSerializer<?> serializer){
		serializers.put(opcode, serializer);
		serializerClassMap.put(serializer.getClass(), serializer);
		objectClassMap.put(objectType, serializer);
	}
	
	public boolean contains(int opcode){
		return serializers.containsKey(opcode);
	}
	
	public boolean contains(Class<?> serializerClass){
		return serializerClassMap.containsKey(serializerClass);
	}
	
	public boolean containsSerializerFor(Class<?> objectType){
		return objectClassMap.containsKey(objectType);
	}
	
	public NetworkSerializer<?> getSerializer(int opcode){
		return serializers.get(opcode);
	}
	
	public <Type extends NetworkSerializer<?>> Type getSerializer(Class<Type> serializerType){
		return (Type) serializerClassMap.get(serializerType);
	}

	public NetworkSerializer<?> getSerializerFor(Class<?> objectType){
		return objectClassMap.get(objectType);
	}
	
}
