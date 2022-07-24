package com.uchump.prime.Metatron.Lib.dhETL;


import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Metatron.Lib.dhETL.A_I.Strict_;
import com.uchump.prime.Metatron.Lib.dhETL.A_I.iRead;
import com.uchump.prime.Metatron.Lib.dhETL.A_I.iWrite;

public class Row<S, E> extends aList<E> implements Strict_, iRead, iWrite {
	private boolean canWrite = true;
	private boolean canRead = true;
	private boolean useStrict = true;
	private boolean isBlank = false;

	private Field<S, Class<?>> field;
	private Object lock = new Object();
	
    private String tableName ;

	public Row() {

	}

	public Row(Field<S, Class<?>> field, int size) {
		super();
		this.resize(size);
		this.field = field;
		setNull(size);
	}

	public Row(Field<S, Class<?>> field) {
		this(field, field.size());
	}

	private void setNull(int size) {
		for (int i = 0; i < size; i++) {
			this.append(null);
		}
	}

	public Row<S, E> setField(Field<S, Class<?>> field) {
		this.field = field;
		setNull(field.size());
		return this;
	}

	public Field<S, Class<?>> getField() {
		return this.field;
	}

	public Row<S, E> setColumn(int index, E e) {
		if (index < this.size()) {
			synchronized (this) {
				this.set(index, e);
			}
		} else {
			throw new RuntimeException("outflow row column number :" + index + ":" + e + ",size is " + this.size());
		}
		return this;
	}

	public Row<S, E> setColumn(S fieldName, E e) {

		if (this.field.containsKey(fieldName)) {
			if (!useStrict) {
				this.set(this.field.getNumber(fieldName), e);
				return this;
			}
			synchronized (this) {
				if (e == null) {
					this.set(this.field.getNumber(fieldName), e);
				} else if (e != null && e.getClass().equals(this.field.getType(fieldName))) {
					this.set(this.field.getNumber(fieldName), e);
				} else
					throw new RuntimeException("type `" + e.getClass() + "` of value `" + e + "` not matched `"
							+ this.field.getType(fieldName) + "`");
			}
		} else
			throw new RuntimeException("field name `" + fieldName + "` not found!");
		return this;
	}

	public Row<S, E> setUseStrict(boolean useStrict) {
		this.useStrict = useStrict;
		return this;
	}

	public Row<S, E> setColumn(S fieldName, E e, Class<?> c) {
		synchronized (lock) {
			if (this.field == null)
				this.field = new Field<>();
			if (!this.field.hasName(fieldName)) {
				this.append(null);
			}
			this.field.setType(fieldName, c);
			this.setColumn(fieldName, e);
		}
		return this;
	}

	public E getColumn(S fieldName) {
		if (field.containsKey(fieldName)) {
			int num = this.field.getNumber(fieldName);
			E e = this.get(num);
			return e;
		} else {
			throw new RuntimeException("Field do not exist:" + fieldName);
		}
	}
	
    public E getColumn(int num){
        E e = this.get(num);
        return e;
    }
    
    public Row<S, E> setColumn(Row<S, E> row) {
        if(this.field==null) this.setField( row.field);
        if(!this.field.equals(row.field)) this.field = row.field;
        row.field.getKeys().forEach(name -> {
            this.setColumn(name, row.getColumn(name), row.field.getType(name));
        });
        return this;
    }
    
    public Row<S, E> removeColumn(S... fieldNames) {
        for (S s : fieldNames) {
            removeColumn(s);
        }
        return this;
    }
    
    public E getNumber(S fieldName) {
        return this.get(this.field.getNumber(fieldName));
    }


    public static Row makeByPair(Field field, String fieldName1, String fieldName2, Object v1, Object v2) {
        Row row = new Row(field, field.size());
        row.setColumn(fieldName1, v1);
        row.setColumn(fieldName2, v2);
        return row;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();


        s.append("[");
        int i[] = new int[1];
        this.forEach(e -> {
            s.append(e);
            if (this.size() - 1 != i[0])
                s.append(",");
            i[0]++;
        });
        s.append("]\n");
        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Row<?, ?> row2 = (Row<?, ?>) o;

        return field.equals(row2.field);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + field.hashCode();
        return result;
    }


    public boolean isCanWrite() {
        return canWrite;
    }

    public Row setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
        return this;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public Row setCanRead(boolean canRead) {
        this.canRead = canRead;
        return this;
    }

    @Override
    public Row<S, E> setStrict(boolean isStrict) {
        this.useStrict = isStrict;
        return this;
    }


    public String[] toStringArray(){
        String [] arr = new String[size()];
        int[]i=new int[]{0};
        this.forEach(k->{
            if(k==null)
                arr[i[0]] = null;
            else
                arr[i[0]] = k.toString();
            i[0]+=1;
        });
        return arr;
    }

    public String getTableName() {
        return tableName;
    }

    public Row setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * It is a tag to determine hasRemaining
     * @return
     */
    public boolean isBlank() {
        return isBlank;
    }

    public Row setBlank(boolean blank) {
        isBlank = blank;
        return this;
    }
}
