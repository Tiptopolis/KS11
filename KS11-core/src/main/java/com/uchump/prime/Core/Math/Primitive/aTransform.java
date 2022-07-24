package com.uchump.prime.Core.Math.Primitive;

import java.util.Iterator;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Primitive.A_I.iTransform;
import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct._Map.Entry;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Utils.StringUtils;

public class aTransform extends aNode<aTransform> implements iTransform {

	public static aTransform SpatialBasis = new aTransform(true);

	protected aVector _localPosition;
	protected aVector _localRotation;
	protected aVector _localScale;
	protected aVector _localNormal;

	protected aTransform _Parent;
	protected aSet<aTransform> _Children = new aSet<aTransform>();

	private aTransform(boolean nuts) {
		super();
		this._localPosition = new aVector(0f, 0f, 0f);
		this._localRotation = new aVector(0f, 1f, 0f, -1f);
		this._localNormal = new aVector(0f, 0f, 1f);
		this._localScale = new aVector(1f, 1f, 1f);

		this.shared.put("Transform", this);

		this.shared.put("_localPosition", this._localPosition); // function lol
		this.shared.put("_localRotation", this._localRotation);
		this.shared.put("_localScale", this._localScale);
		this.shared.put("_localNormal", this._localNormal);

		this.shared.put("_Parent", this._Parent);
		this.shared.put("_Children", this._Children);

		if (!nuts)
			this.setParent(SpatialBasis);
	}

	public aTransform() {
		super();
		this._localPosition = new aVector(0f, 0f, 0f);
		this._localRotation = new aVector(0f, 0f, 0f, -1);
		this._localScale = new aVector(1f, 1f, 1f);
		this._localNormal = new aVector(0f, 1f, 0f);

		this.shared.put("Transform", this);

		this.shared.put("_localPosition", this._localPosition); // function lol
		this.shared.put("_localRotation", this._localRotation);
		this.shared.put("_localScale", this._localScale);
		this.shared.put("_localNormal", this._localNormal);

		this.shared.put("_Parent", this._Parent);
		this.shared.put("_Children", this._Children);
		this.setParent(SpatialBasis);
	}

	public aTransform(iTransform from) {
		this();
		this.position(from.position());
		this.rotation(from.rotation());
		this.scale(from.scale());
		this.normal(from.normal());

	}

	public aTransform(aVector at) {
		this(true);
		this.setPosition(at);
	}

	public aTransform(aVector at, aTransform basis) {
		this(false);		
		this.setPosition(at);
		this.setParent(basis);
	}

	public aTransform(aVector pos, aVector rot, aVector scl) {
		this(pos, rot, scl, new aVector(0f, 1f, 0, 0));

	}

	public aTransform(aVector pos, aVector rot, aVector scl, aVector nor) {
		this();
		this.removeParent();
		this._localPosition = (pos);
		this._localRotation = (rot);
		this._localScale = (scl);
		this._localNormal = (nor);
		this.setParent(SpatialBasis);

	}

	public aTransform(aTransform parent, aVector... data) {
		this();
		int dim = parent.getPosition().size();

		if (data.length < 4)
			this._localNormal = new aVector().filled(dim, 0f);
		else
			this._localNormal = data[3];

		if (data.length < 3)
			this._localScale = new aVector().filled(dim, 0f);
		else
			this._localScale = data[2];

		if (data.length < 2) {
			this._localRotation = new aVector().filled(dim + 1, 0f);
			this._localRotation.setAt(1, 1);
			this._localRotation.setAt(dim, -1);
		} else
			this._localRotation = data[1];

		if (data.length == 0)
			this._localPosition = new aVector().filled(dim, 0f);
		else
			this._localPosition = data[0];

		this.shared.put("_localPosition", this._localPosition); // function lol
		this.shared.put("_localRotation", this._localRotation);
		this.shared.put("_localScale", this._localScale);
		this.shared.put("_localNormal", this._localNormal);

		this.shared.put("_Parent", this._Parent);
		this.shared.put("_Children", this._Children);

		this.setParent(parent);
	}

	public aTransform(aMultiMap<String, Object> params) {
		aMultiMap<String, Entry<String, Object>> found = iMap.find(params, "position", "rotation", "scale", "normal");

		if (found.containsKeys("position", "rotation", "scale", "normal")) {
			Entry<String, Object> E = null;
			E = found.get("position");
			this._localPosition = (aVector) E.getValue();

			E = found.get("rotation");
			this._localRotation = (aVector) E.getValue();
			this._localRotation.setAt(1, 1);
			this._localRotation.setAt(3, -1);

			E = found.get("scale");
			this._localScale = (aVector) E.getValue();

			E = found.get("normal");
			this._localNormal = (aVector) E.getValue();
		} else {
			this._localPosition = new aVector().filled(3, 0f);
			this._localRotation = new aVector().filled(3 + 1, 0f);
			this._localRotation.setAt(1, 1);
			this._localRotation.setAt(3, -1);
			this._localScale = new aVector().filled(3, 0f);
			this._localNormal = new aVector().filled(3, 0f);
		}

		this.shared.put("_localPosition", this._localPosition); // function lol
		this.shared.put("_localRotation", this._localRotation);
		this.shared.put("_localScale", this._localScale);
		this.shared.put("_localNormal", this._localNormal);

		this.shared.put("_Parent", this._Parent);
		this.shared.put("_Children", this._Children);

	}

	public aVector getPosition() {
		// return this.getLocalPosition();
		// return this.getLocalPosition();
		if (this._Parent != null) {

			aVector pPos = _Parent.getPosition().cpy();
			aVector pDir = _Parent.getLocalRotation().cpy();
			aVector pRot = _Parent.getRotation().cpy();
			aVector scl = this.getScale().cpy();
			aVector up = _Parent._localNormal.cpy();

			return (pPos.add(this.getLocalPosition().scl(scl)));

		} else
			return this.getLocalPosition();
	}

	public aVector getLocalPosition() {
		return this._localPosition.cpy();
	}

	public void setPosition(aVector pos) {

		if (this._Parent != null) {
			aVector pPos = _Parent.getPosition().cpy();
			aVector pDir = _Parent.getLocalRotation().cpy();
			aVector pRot = _Parent.getRotation().cpy();
			aVector scl = this.getLocalScale().cpy();
			aVector up = _Parent._localNormal.cpy();
			this.setLocalPosition((aVector) N_Operator.div(pPos.sub(pos), (scl)));
		} else {
			this.setLocalPosition(pos);

		}
	}

	public void setLocalPosition(aVector pos) {
		this._localPosition.set(pos);
		this.updateMatrices();
	}

	public aVector getRotation() {
		if (this._Parent != null) {

			if (this._Parent.getRotation().cpy().mul(this.getLocalRotation()).len() == 0)
				return this._Parent.getRotation();
			else
				return this._Parent.getRotation().cpy().mul(this.getLocalRotation());
		} else {
			return this._localRotation.cpy();
		}
	}

	public aVector getLocalRotation() {
		return this._localRotation.cpy();

	}

	public void setRotation(aVector rot) {
		if (this._Parent == null)
			this.setLocalRotation(rot);
		else {
			this.setLocalRotation(this._Parent.getRotation().mul(rot));
		}
	}

	public void setLocalRotation(aVector rot) {
		this._localRotation.set(rot);
		this.updateMatrices();
	}

	public aVector getScale() {
		if (this._Parent != null) {
			return this._Parent.getScale().cpy().scl(this.getLocalScale());
		} else
			return this.getLocalScale();
	}

	public aVector getLocalScale() {
		return this._localScale.cpy();

	}

	public void setScale(aVector scale) {
		if (this._Parent == null)
			this.setLocalScale(scale);
		else {
			this.setLocalScale(this._Parent.getScale().scl(scale));
		}
	}

	public void setLocalScale(aVector scale) {
		this._localScale.set(scale);
		this.updateMatrices();
	}

	public aVector getLocalNormal() {
		return this._localNormal.cpy();
	}

	public aVector getNormal() {
		if (this._Parent != null) {

			if (this._Parent.getRotation().cpy().mul(this.getLocalRotation()).len() == 0)
				return this._Parent.getNormal();
			else
				return this._Parent.getNormal().cpy().scl(this.getLocalNormal());
		} else {
			return this._localNormal.cpy();
		}
	}

	public void setNormal(aVector nor) {
		if (this._Parent == null)
			this.setLocalNormal(nor);
		else {
			this.setLocalNormal(nor.mul(this._Parent.getRotation()));
		}
	}

	public void setLocalNormal(aVector nor) {
		this._localNormal.set(nor);
	}

	public aTransform getParent() {
		if (this._Parent != null)
			return this._Parent;
		else
			return this;
	}

	public void setParent(aTransform t) {
		if (t == this)
			return;
		if (this._Parent != null)
			this.removeParent();

		if (t._Children == null)
			t._Children = new aSet<aTransform>();
		t._Children.add(this);
		this._Parent = t;
		this.updateMatrices();
		this._Parent.updateMatrices();
	}

	public void removeParent() {
		if (this._Parent != null) {
			this._Parent._Children.remove(this);
			this._Parent = null;
		}
		this.updateMatrices();
	}

	public iCollection<aTransform> getChildren() {
		if (this._Children == null)
			this._Children = new aSet<aTransform>();
		return this._Children;
	}

	public void updateMatrices() {
		Object o = this;
		o = this.getScale();
		o = this.getRotation();
		o = this.getPosition();
		if (this._Children != null)
			for (aTransform T : this._Children)
				T.updateMatrices();
	}

	public aTransform cpyFrom(aTransform T) {
		this.setPosition(T.getPosition());
		this.setRotation(T.getRotation());
		this.setScale(T.getScale());
		return this;
	}

	public aTransform cpy() {
		return new aTransform(this);
	}

	@Override
	public aTransform getTransform() {
		return this;
	}

	@Override
	public String toString() {
		String s = "<aTransform>";

		aMultiMap.Entry e = new aMultiMap.Entry("LOCAL", "WORLD");
		s += "        " + e + "\n";
		e = new aMultiMap.Entry("Position",
				"" + this.getLocalPosition().toElementString() + ":" + this.getPosition().toElementString());
		s += e + "\n";
		e = new aMultiMap.Entry("Rotation",
				"" + this.getLocalRotation().toElementString() + ":" + this.getRotation().toElementString());
		s += e + "\n";
		e = new aMultiMap.Entry("Scale",
				"" + this.getLocalScale().toElementString() + ":" + this.getScale().toElementString());
		s += e + "\n";
		e = new aMultiMap.Entry("Normal",
				"" + this.getLocalNormal().toElementString() + ":" + this.getNormal().toElementString());
		s += e + "\n";
		if (this._Parent != null)
			s += "[PARENT] = aTransform@" + this._Parent.hashCode() + "\n";
		if (this._Children != null && !this._Children.isEmpty()) {
			s += "[CHILDREN]\n";
			for (aTransform t : this._Children)
				s += "   *aTransform@" + t.hashCode() + "\n";
		}
		return s;
	}

	@Override
	public String toLog() {
		return this.toString();
	}

}
