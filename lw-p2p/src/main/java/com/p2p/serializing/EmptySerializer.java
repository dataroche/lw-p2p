package com.p2p.serializing;

import io.netty.buffer.ByteBuf;

/**
 * @author WILL
 *
 *A Serializer that does not do anything. Useful for token objects that dont contain any data.
 *
 * @param <Item>
 */
abstract public class EmptySerializer<Item> extends NetworkSerializer<Item> {

	@Override
	protected void write(ByteBuf stream, Item object, SerializingTable table)
			throws RuntimeException {
		
	}

	@Override
	protected Item read(ByteBuf stream, int bytesToRead, SerializingTable table)
			throws RuntimeException {
		return null;
	}

}
