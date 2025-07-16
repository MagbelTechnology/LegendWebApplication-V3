package com.magbel.admin.bus;

public class BinaryHolder{

private byte[] data;

public BinaryHolder(byte[] data){

	this.data = data;
}

public byte[] toByteArray(){
	return this.data;
}

}
