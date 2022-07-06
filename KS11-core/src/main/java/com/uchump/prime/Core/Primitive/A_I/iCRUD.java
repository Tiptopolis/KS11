package com.uchump.prime.Core.Primitive.A_I;

public interface iCRUD<C extends CharSequence, T> {

	//aNumber implements CharSequence ergo aVector does, too
	
	public void create(T entry);

	public T read(C indx);

	public void update(C index, T entry); // CRUD

	public void delete(C index);

}
