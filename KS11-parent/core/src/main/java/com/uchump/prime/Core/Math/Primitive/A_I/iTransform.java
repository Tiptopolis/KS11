package com.uchump.prime.Core.Math.Primitive.A_I;


import static com.uchump.prime.Core.Math.N_Operator.*;

import com.uchump.prime.Core.Math.Primitive.aTransform;
import com.uchump.prime.Core.Math.Primitive.aVector;
public interface iTransform {

	
	
	public aTransform getTransform();
	
	
	
	// Position
		public default aVector position() {
			return this.position(false);
		}

		public default aVector position(boolean local) {
			aTransform T = this.getTransform();
			if (local)
				return T.getLocalPosition();
			else
				return T.getPosition();
		}

		public default void position(aVector position) {
			this.position(position, false);
		}

		public default void position(aVector position, boolean local) {
			aTransform T = this.getTransform();
			if (local)
				T.setLocalPosition(position);
			else
				T.setPosition(position);
		}

		// Rotation

		public default aVector rotation() {
			return this.rotation(false);
		}

		public default aVector rotation(boolean local) {
			aTransform T = this.getTransform();

			if (local)
				return T.getLocalRotation();
			else
				return T.getRotation();
		}

		public default void rotation(aVector rotation) {
			this.rotation(rotation, false);
		}

		public default void rotation(aVector rotation, boolean local) {
			aTransform T = this.getTransform();
			if (local)
				T.setLocalRotation(rotation);
			else
				T.setRotation(rotation);
		}


		public default void normal(aVector normal) {
			this.getTransform().setLocalNormal(normal);
		}

		public default aVector normal() {
			return this.getTransform().getLocalNormal();
		}

		public default aVector direction() {
			aVector d = this.rotation(true);
			aVector s = aTransform.SpatialBasis.getLocalRotation();
			if (isEqual(d, new aVector(0, 0, 0), 0.0001f))
				return s;
			else
				return d;

		}

		// Scale
		public default aVector scale() {
			return this.scale(false);
		}

		public default aVector scale(boolean local) {
			aTransform T = this.getTransform();
			if (local)
				return T.getLocalScale();
			else
				return T.getScale();
		}

		public default void scale(aVector scale) {
			this.scale(scale, false);
		}

		public default void scale(aVector scale, boolean local) {
			aTransform T = this.getTransform();
			if (local)
				T.setLocalScale(scale);
			else
				T.setScale(scale);
		}

		//////////////////////////////////////////////

		default public aVector forward()
		{
			return this.direction().nor();
		}

		public default aVector up()
		{
			return this.normal().nor();
		}
		
		public default aVector right()
		{
			return this.direction().cpy().crs(this.normal()).nor();
		}

		default aVector spinByForward(float deltaForward) {

			aVector tmp = new aVector(this.direction().nor().scl(deltaForward));

			return tmp;
		}

		default aVector spinByNormal(float deltaUp) {

			aVector tmp = new aVector(this.normal().nor().scl(deltaUp));

			return tmp;
		}

		default aVector SpinByRight(float deltaRight) {

			aVector tmp = new aVector(this.direction().crs(this.normal()).nor().scl(deltaRight));

			return tmp;
		}

		public default void rotate(aVector axis, float degrees) {
		
			aVector d = this.getTransform().getLocalRotation().cpy();

			if (isEqual(d, new aVector().filled(d.size(),0), 0.001f))
				d.set(aTransform.SpatialBasis.getLocalRotation());

			d.rotate(axis, degrees);
			this.getTransform().getLocalNormal().rotate(axis, degrees);
			this.getTransform().setLocalRotation(d);
			for (aTransform c : this.getTransform().getChildren()) {
				c.rotate(axis, degrees);
				rotOutter(c, axis, degrees);
			}
		}
		
		public static void rotOutter(aTransform c, aVector axis, float degrees) {
			aVector T = c.getLocalPosition();
			T.rotate(axis, degrees);
			
			c.setLocalPosition(new aVector(T));
		}
	
		public default void orbit(aVector axis, float degrees)
		{
			rotOutter(this.getTransform(),axis,degrees);
		}
}
