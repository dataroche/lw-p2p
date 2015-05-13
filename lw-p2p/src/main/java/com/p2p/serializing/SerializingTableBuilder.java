package com.p2p.serializing;

import com.nativeSerializers.ConnectionAnswerSerializer;
import com.nativeSerializers.ConnectionAttemptSerializer;
import com.nativeSerializers.NetworkWelcomeSerializer;
import com.nativeSerializers.PasswordSerializer;
import com.nativeSerializers.SocketIdSerializer;
import com.nativeSerializers.StringSerializer;
import com.p2p.NetworkInformationSerializer;

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
		appendRequiredSerializers(table);
		return table;
	}
	
	public static SerializingTable appendRequiredSerializers(SerializingTable table){
		table.addSerializer(new StringSerializer());
		table.addSerializer(new SocketIdSerializer());
		table.addSerializer(new PasswordSerializer());
		table.addSerializer(new NetworkWelcomeSerializer());
		table.addSerializer(new ConnectionAttemptSerializer());
		table.addSerializer(new ConnectionAnswerSerializer());
		table.addSerializer(new NetworkInformationSerializer());
		return table;
	}
}
