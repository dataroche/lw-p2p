package com.p2p.serializing;

import nativeSerializers.ConnectionAnswerSerializer;
import nativeSerializers.ConnectionAttemptSerializer;
import nativeSerializers.PasswordSerializer;
import nativeSerializers.SocketIdSerializer;
import nativeSerializers.StringSerializer;

public class SerializingTableBuilder {
	public static SerializingTable newEmptyTable(){
		return new SerializingTable();
	}
	
	/**Creates a new table with the default serializers.
	 * 
	 * @return
	 */
	public static SerializingTable newDefaultTable(){
		SerializingTable table = new SerializingTable();
		table.addSerializer(new StringSerializer());
		table.addSerializer(new SocketIdSerializer());
		table.addSerializer(new PasswordSerializer());
		table.addSerializer(new ConnectionAttemptSerializer());
		table.addSerializer(new ConnectionAnswerSerializer());
		return table;
	}
}
