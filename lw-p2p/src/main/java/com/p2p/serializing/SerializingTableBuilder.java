package com.p2p.serializing;

import com.nativeSerializers.ConnectionAnswerSerializer;
import com.nativeSerializers.ConnectionAttemptSerializer;
import com.nativeSerializers.PasswordSerializer;
import com.nativeSerializers.SocketIdSerializer;
import com.nativeSerializers.StringSerializer;

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
