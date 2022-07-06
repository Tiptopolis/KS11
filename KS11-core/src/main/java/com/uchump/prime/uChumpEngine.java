package com.uchump.prime;

import static com.uchump.prime.Core.Math.N_Operator.*;
import static com.uchump.prime.Core.uAppUtils.*;
import static com.uchump.prime.Core.uSketcher.*;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.uchump.prime.Core.DefaultResources;
import com.uchump.prime.Core.uAppUtils;
import com.uchump.prime.Core.uSketcher;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Primitive._Evaluate;
import com.uchump.prime.Core.Math.Primitive.aMatrix;
import com.uchump.prime.Core.Math.Primitive._Evaluate;
import com.uchump.prime.Core.Math.Primitive.aNumber;
import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Math.Utils.Maths;
import com.uchump.prime.Core.Math.Utils.aGeom;
import com.uchump.prime.Core.Math.Utils.aMaths;
import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iEnum;
import com.uchump.prime.Core.Primitive.Struct._Map;
import com.uchump.prime.Core.Primitive.Struct.aLinkedList;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Primitive.Struct.aSetMap;
import com.uchump.prime.Core.Primitive.Utils.aCharIndexer;
import com.uchump.prime.Core.Primitive.Utils.aThingCounter;
import com.uchump.prime.Core.Primitive.Utils.aThingIndexer;
import com.uchump.prime.Core.Utils.StringUtils;
import com.uchump.prime.Core.Utils._Lambda;
import com.uchump.prime.Core.Utils.iCypher;
import com.uchump.prime.Core.Utils.X._AlgebraX;
import com.uchump.prime.Core.Utils.X._OperatorX;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class uChumpEngine extends ApplicationAdapter {

	public InputMultiplexer Multiplexer;

	private SpriteBatch batch;
	private Texture image;

	@Override
	public void create() {
		uSketcher s = new uSketcher();
		uAppUtils.update(0);

		this.Multiplexer = new InputMultiplexer();

		Gdx.input.setInputProcessor(this.Multiplexer);
		batch = new SpriteBatch();
		image = new Texture("libgdx.png");

		Log(new aNode(1));
		Log(N_Operator.resolve("1"));
		Log(N_Operator.resolve(1));
		Log(N_Operator.resolve(1f));
		Log(N_Operator.resolve(new aNumber(1f)));

		aNode A = new aNode(0);
		aNode B = new aNode(100);
		Log(A);
		Log(B);
		// Log(A>B);
		if (instanceOf(Integer.class, Byte.class, Short.class, Long.class).test(1))
			Log("ASS!!");

		if (instanceOf(Number.class).test(A.get()))
			Log("!!!!!1");
		Log(A.Get);

		Log(Add.apply(2, 5));
		Log(new aList<Integer>(1, 4, 7, 9));
		Log(new aSet<Integer>(1, 4, 1, 7, 9));
		Log(new aList(1, 2, 3).with(2, 88).join(new aSet(4, 5, 6)));

		aMap k = new aMap();
		k.put("ASS", 1);
		k.put("ASS", 1);
		k.put("ASS", 4);
		k.put("TITS", 1);
		k.put("ASS", 7);
		k.put("TITS", 9);
		Log(k.toLog());

		String context = "CONTEXT";
		aList L = new aList();
		aNode head = new aNode("HEAD");
		aNode shoulders = new aNode("SHOULDERS");
		aNode knees = new aNode("KNEES");
		aNode toes = new aNode("TOES");
		head.link(context, "NEXT", "PREVIOUS", shoulders);
		shoulders.link(context, "NEXT", "PREVIOUS", knees);
		knees.link(context, "NEXT", "PREVIOUS", toes);

		aList<aNode> Body = new aList<aNode>(head, shoulders, knees, toes);
		Log(Body.toLog());
		for (aNode n : Body) {
			Log(n.toLog());
		}

		aVector vA = new aVector(1f, 4, 9);
		aVector vB = new aVector(1f, 2, 2);
		Log("" + vA);
		Log("" + vB);
		Log("" + (vA.root(vB)));
		Log("" + (N_Operator.root(9, 2)));
		aLinkedList C = new aLinkedList(1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 3, 6);
		Log(C);
		Log(C.toLog());
		for (Object o : C)
			if (o instanceof aNode)
				Log(((aNode) o).toLog());
		aList a1 = new aList(1, 2, 3);
		aList a2 = new aList(4, 5, 6);
		aList a3 = new aList(7, 8, 9);
		aList a4 = new aList(3, 6, 9);
		aList a5 = new aList(10, 10, 10);
		Log(a1.cpy().union(a2.cpy()).union(a3.cpy()).union(a5.cpy()).toSet());
		Log(a1.cpy().hull(a4.cpy()));
		Log(a1.cpy().dif(a4.cpy()));
		Log(a1.cpy().intersection(a4.cpy()).toSet());

		String t1 = "[Object] <Type> List{1,2,3} (1,2,3) Array[5]{0,1,2,3,4}";
		aThingCounter<String> check = new aThingCounter<String>(charArray(t1));
		// Log(check.toLog());
		Log(iCypher.decompMap1(t1).toLog());
		String obj = "[A] [B] [C]";
		Log(obj.split("[\\ ||\\ ]")); // insert

		Log("___");
		Log("" + aVector.Range(-2, 2));
		Log("" + aVector.Range(-2, 2, 9));
		Log("" + aVector.Range(-2f, 2f, 9));
		// Log();
		// Log(aGeom.interpolate(-2f, 2f, 9));
		Log(iCypher.decompMap1(t1).toLog());
		Log(StringUtils.countSymbols("[ASS]"));
		Log(iCypher.permutation("GUAC"));
		aMap<String, Number> tst = new aMap<String, Number>();

		Function<Number, aMap.Entry<Number, String>> resolve = (Number a) -> {
			return new aMap.Entry<Number, String>(a, a.getClass().getSimpleName());
		};
		// direction of a O-M relation <K,V>|<V,K>
		tst.put("ONE", resolve.apply(1));
		tst.put("ONE", resolve.apply(1f));
		tst.put("ONE", resolve.apply(new aVector<Integer>(1)));
		tst.put("ONE", resolve.apply(new aVector<Float>(1f)));
		Log(tst.toLog());
		aThingIndexer<Number> Ind = new aThingIndexer<Number>(1, 0, 1, 0, 1, 0, 1, 0);
		Log(Ind.things.toLog());

		Predicate p1 = (o) -> {
			return N_Operator.isEqual((Number) o, 1f);
		};

		// doesnt work :/
		// Predicate<Number> X = _Lambda.anyPredicate(o->
		// N_Operator.isEqual((Number) o, 0),o->
		// N_Operator.isEqual((Number) o, 1));

		// Predicate<Number> X = (o)->{return N_Operator.isEqual(o, 1);};

		// Predicate X = _Lambda.instanceOf(Integer.class,Float.class);
		Predicate X = _Lambda.isAnyValue(1, 2);
		// Predicate X = _Lambda.instanceOf(1,1l);
		Log(X.test(1));
		Log(X.test(0));
		Log(X.test(1f));
		Log(X.test(0f));
		Log(X.test(2));
		Log(X.test(2f));

		BiFunction<Number, Number[], Boolean> f1 = (Number n, Number[] E) -> {

			for (Number N : E) {
				if (N_Operator.isEqual(n, N))
					return true;
			}
			return false;
		};

		_AlgebraX.Math[] m = _AlgebraX.Math.values();
		Log(m);
		Log(_OperatorX.Math.ADD.operate(2, 5, 3));// consumer, yeah?
		Log("------------------------------");
		aMap F = new aMap();
		F.put(0, "ASS");
		F.put(1, "ASS", "NUTS");
		F.put(2, "ASS", "BUTTS");
		F.put(3, "ASS");
		F.put(4, "ASS");
		Log(F.toLog());
		F.remove(1);
		Log();
		Log(F.toLog());
		F.remove(2);
		Log("_______");
		if (_Lambda.instanceOf(Byte.class, String.class, Short.class, Long.class).test("ASS"))
			Log("ASS");
		if (_Lambda.instanceOf(Number.class).test(A.get()))
			Log("@@@");
		Log(A.Get);
		Log();
		Log(F.toLog());
		_Map S = new aSetMap();
		S.put("TITS", 1);
		S.put("ASS", 1);
		S.put("BUTTS", 1);
		S.put("TITS", 2);
		Log(S.toLog());
		Log(new aList(1, 2, 3, 4, 5).get(2));
		Log(new aList(1, 2, 3, 4, 5).get(7));
		// Log(new aList(1,2,3,4,5).get(-2));
		// Log(new aList(1,2,3,4,5).get(-7));
		// __________i_s_______________________
		// InnerPos @2#10->2 @%#
		// OutterPos @12#10->2 @%#
		// InnerNeg @-2#10->8 abs(#+@)%#
		// OutterNeg @-12#10->8 abs(#+@)%#
		// need to make some Vector comparators & sorters
		Log();
		Log(new aList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).get(2));
		Log(new aList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).get(12));
		Log(new aList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).get(-2));
		Log(new aList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).get(-12));
		Log(new aVector(8, 8, 8).mul(new aVector(2, 4, 8)));

		Log(_AlgebraX.Math.ADD.operate(2, 5));// consumer, yeah?
		Log(aMaths.inverseLerp(0, 50, 25));
		Log(aMaths.inverseLerp(0, 20, 25));
		Log(aMaths.lerp(0, 50, .25f));
		Log(aMaths.lerp(0, 20, .25f));
		Log();
		Log(aMaths.inverseLerp(0, 100, 120));
		Log(aMaths.lerp(0, 100, 120));
		Log();
		// Log(iCypher.resolveModIndex(-320, 100));
		Log(aMaths.remap(320, 0, 100, 0, 10));
		Log(iCypher.decodeVector(new aVector(0, 25), DefaultResources.ENGLISH_LETTERS_LOWER));
		Log(iCypher.decodeVector(new aVector(0, 25), DefaultResources.ENGLISH_LETTERS_LOWER));
		Log(iCypher.NumericBaseAlphabet(25));
		Log(Maths.gcd(8, 6));
		Log(iCypher.encodeVector("abcassaaa"));
		S = new aSetMap();
		S.put("TITS", 1);
		S.put("ASS", 1);
		S.put("BUTTS", 1);
		S.put("TITS", 2);
		Log(S.toLog());
		Vector3 a = new Vector3(0,0,0);
		Vector3 b = new Vector3(10,10,10);
		Log(new aVector(0,0,0).ilerp(new aVector(10,10,10), 5));
		Log(a.lerp(b, 0.5f));
		Log(new aVector(0,0,0).lerp(new aVector(10,10,10), 0.5f));
		_Evaluate.Modifier SIN = new _Evaluate.Modifier(5f,(n)->{return N_Operator.sin(n);});
		Log(SIN);
		//(String symbol, Number def,  boolean exe, Function<Number,Number> doF, Number val)
		
		//approximates PI? wtf
		_Evaluate.Modifier SINE = new _Evaluate.Modifier("SINE",0d,(n)->{return N_Operator.sin(n.doubleValue());});
		Number g = 1d;
		
		//(1/10^d)
		float D_=1;
		float S_ = 1f/(N_Operator.pow(10f, D_).floatValue());
		float G_ = 1f/(N_Operator.pow(10f, D_+1).floatValue());
		
		for(float i =0; i < S_; i+= G_)
		{
			Log("_"+SINE.apply(i));
			g = N_Operator.add(g, SINE.apply(g));
			float I = aMaths.toPrecision(i, D_).floatValue();
			Log("   "+I+"="+g);
		}
		Log("=="+g.doubleValue());//3.14 wtf?
		
		
		
		Log(StringUtils.countSymbolsFrom("/ASS/HOLE.@(3,6,9) WHERE [(@%#)<0]", " ,./").toLog());
		Log(new aCharIndexer("/ASS/HOLE.@(3,6,9) WHERE [(@%#)<0]").things.toLog());
		_Evaluate.Modifier INC = new _Evaluate.Modifier("++",0f,(n)->{return N_Operator.add(n,1);});
		_Evaluate.Modifier ACC = new _Evaluate.Modifier("+=",0f,(n)->{return N_Operator.add(n,n);});
		_Evaluate.Mask ADD = new _Evaluate.Mask("+",0f,(nA,nB)->{return N_Operator.add(nA,nB);});
		
		
		int f = 0;
	
		for(int i =0; i < 10; i++)
		{
			f=INC.apply(f).intValue();
			Log(f);
			Log(ACC.apply(f));
		}
		
		Log(new aList<aVector>( aMaths.PascalTri(5)).toLog());
		String[] PT = iCypher.PascalTri(5);
		for(int i =0; i < PT.length; i++)
		{
			Log("["+i+"]{"+PT[i].split(" ").length+"}"+PT[i]);
		}
		Log();
		aList L1 = new aList(1,2,3);
		aList L2 = new aList(4,5,6);
		aList L3 = new aList(7,8,9);
		iCollection L_F = iCollection.Consolidate(L1,L2,L3);
		Log(L_F);
		Log(new aVector((iCollection<Number>) L_F).toMap());
		aMatrix tM = new aMatrix(aVector.fromCollections(L1,L2,L3));
		
	}

	aList<String> arch;
	aVector v;

	@Override
	public void render() {
		/*
		 * if (arch == null) arch = new aList<String>(iCypher.permutation("GUAC")); if
		 * (v == null) v = new aVector(0, 1, 2, 3, 4, 5, 6, 7, 8, 9); arch.shiftBy(-1);
		 * v.shiftBy(1);
		 */
		// Log(arch);
		// Log(v);
		float deltaTime = Gdx.graphics.getDeltaTime();

		uAppUtils.update(deltaTime);
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	}

	@Override
	public void resize(int width, int height) {
		uAppUtils.resize();

	}

	@Override
	public void dispose() {
		batch.dispose();
		image.dispose();

		iEnum._ALL.dispose();
	}

}