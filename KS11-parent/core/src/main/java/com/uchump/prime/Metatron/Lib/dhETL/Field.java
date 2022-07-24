package com.uchump.prime.Metatron.Lib.dhETL;

import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;

public class Field<S, C> extends aMultiMap<S, C> {

	private int position = -1;
	private aMultiMap<S, Integer> fieldNumber;

	public Field(int size) {
		this.resize(size);
		this.fieldNumber = new aMultiMap<S, Integer>();
		this.fieldNumber.resize(size);
	}

	public Field() {
		this(8);
	}

	public C getType(S fieldName) {
		return (C) this.get(fieldName);
	}

	public Integer getNumber(S fieldName) {
		return this.fieldNumber.get(fieldName);
	}

	public Field<S, C> setType(S fieldName, C type) {
		if (!this.containsKey(fieldName)) {
			synchronized (this) {
				this.position++;
				this.put(fieldName, type);
				this.fieldNumber.put(fieldName, position);
			}
		} else {
			synchronized (this) {
				this.put(fieldName, type);
				int oldPosition = this.fieldNumber.get(fieldName);
				this.fieldNumber.put(fieldName, oldPosition);
			}
		}

		return this;
	}

	public Field<S, C> setType(Field<S, C> field) {
		field.forEach((s, c) -> {
			this.setType(s, c);
		});
		// field.forEach((e->this.setType(e.getKey(),e.getValue())));
		return this;
	}

	private void updateNumberIndex(int number) {
		this.fieldNumber.forEach((k, v) -> {
			if (v > number) {
				this.fieldNumber.put(k, v - 1);
			}
		});

	}

	public Field<S, C> removeColumn(S name) {
		if (this.hasName(name) && this.hasNumber(name)) {
			synchronized (this) {
				int num = this.getNumber(name);
				this.removeKey(name);
				this.fieldNumber.removeKey(name);
				this.updateNumberIndex(num);
				position--;
			}
		}
		return this;
	}

	public Field<S, C> removeColumns(S... names) {
		for (S s : names) {
			removeColumn(s);
		}
		return this;
	}


	public boolean hasName(S name) {
		return this.containsKey(name);
	}

	public boolean hasNumber(S name) {
		return this.fieldNumber.containsKey(name);
	}

	@Override
	public int size() {
		return position + 1;
	}
	
	  public Field<S,C> filter(aSet<S> fs){
	        Field<S,C> field = new Field<>();
	        this.forEach((k,v)->{
	            if(fs.contains(k)){
	                field.setType(k,v);
	            }
	        });
	        return field;
	    }

	    @Override
	    public String toString() {
	        StringBuffer s = new StringBuffer();
	        this.forEach((k, v) -> {
	            s.append(k + ":" + fieldNumber.get(k) + "->" + v);
	            s.append(",\n");
	        });
	        return "Field{" + "size:" + size() + "\n" +
	                s +
	                '}';
	    }


	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        if (!super.equals(o)) return false;

	        Field<?, ?> field = (Field<?, ?>) o;

	        if (position != field.position) return false;
	        return fieldNumber.equals(field.fieldNumber);

	    }

	    @Override
	    public int hashCode() {
	        int result = super.hashCode();
	        result = 31 * result + position;
	        result = 31 * result + fieldNumber.hashCode();
	        return result;
	    }

	    public String[] toStringArray(){
	        String [] arr = new String[size()];
	        int[]i=new int[]{0};
	        this.getKeys().forEach(k->{
	            if(k==null)
	                arr[i[0]] = null;
	            else
	                arr[i[0]] = k.toString();
	            i[0]+=1;
	        });
	        return arr;
	    }

}
