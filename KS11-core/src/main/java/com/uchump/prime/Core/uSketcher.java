package com.uchump.prime.Core;

import space.earlygrey.shapedrawer.GraphDrawer;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.uchump.prime.Core.uAppUtils.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonBatch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class uSketcher implements Disposable {
	// MOVE DEFAULT/DEBUG RENDERING STUFF HERE

	public static uSketcher Sketcher;
	public static Matrix4 CurrentProjection;

	public static float CurrentPixelSize = 1;
	public static final float EPSILON = 0.001f;
	public static final Vector2 DefaultResolution = new Vector2(640, 480);

	// public SpriteBatch spriteBatch = new SpriteBatch();//
	public Batch currentBatch;
	private PolygonBatch polygonBatch = new PolygonSpriteBatch();
	private SpriteBatch spriteBatch = new SpriteBatch(5000);
	private PolygonSpriteBatch psBatch = new PolygonSpriteBatch(5000);
	private ModelBatch modelBatch = new ModelBatch();

	private static Matrix4 defaultProjection;
	private TextureRegion batchRegion = new TextureRegion();
	public static ShapeDrawer Drawer;
	public static GraphDrawer Grapher;
	// need to track batch's current projection for GeomUtils circle estimation
	// method

	public static Color thatColor = new Color(Color.WHITE);
	public static Vector3 thatPoint = new Vector3();

	public FreeTypeFontGenerator fontGen;
	public final BitmapFont defaultFont;
	public BitmapFont font;

	public int strokeWeight = 1;
	public int textWidth = 16;
	public int textHeight = 16;
	public int textSize = 16;

	// PixelSize stuff
	protected static final Matrix4 mat4 = new Matrix4();
	protected float pixelSize = 1, halfPixelSize = 0.5f * pixelSize;
	protected float offset = EPSILON * pixelSize;

	public boolean isDrawing = false;

	public uSketcher() {
		Sketcher = this;
		font = new BitmapFont();
		defaultFont = new BitmapFont();
		this.init();
		Log(">>uSketcher created");
		
	}

	private void init() {

		// Initiates ShapeDrawer
		this.currentBatch = this.polygonBatch;
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.drawPixel(0, 0);
		Texture texture = new Texture(pixmap); // remember to dispose of later
		pixmap.dispose();
		TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
		batchRegion = region;
		Drawer = new ShapeDrawer(polygonBatch, batchRegion);
		Grapher = new GraphDrawer(Drawer);
		defaultProjection = polygonBatch.getProjectionMatrix();
		//
	}

	public void begin() {
		this.isDrawing = true;
		// this.polygonBatch.begin();
		this.currentBatch.begin();
	}

	public void end() {
		// this.polygonBatch.end();
		this.currentBatch.end();
		this.isDrawing = false;
	}

	public void interupt() {
		if (this.isDrawing)
			this.end();
		this.begin();
	}

	public Batch getBatch() {
		return this.currentBatch;
	}

	public SpriteBatch SpriteBatch() {
		if (this.spriteBatch == null)
			this.spriteBatch = new SpriteBatch();

		this.currentBatch = this.spriteBatch;
		return this.spriteBatch;
	}

	public PolygonBatch PolygonBatch() {

		this.currentBatch = this.polygonBatch;
		return this.polygonBatch;
	}

	public PolygonSpriteBatch PolygonSpriteBatch() {
		if (this.psBatch == null)
			this.psBatch = new PolygonSpriteBatch();

		this.currentBatch = this.psBatch;
		return this.psBatch;
	}

	public ModelBatch ModelBatch() {
		if (modelBatch == null)
			this.modelBatch = new ModelBatch();

		return this.modelBatch;
	}

	public void setProjectionMatrix(Matrix4 projection) {
		CurrentProjection = projection;
		this.polygonBatch.setProjectionMatrix(projection);
		this.updatePixelSize();
	}
	
	public void setProjection(Camera projection)
	{
		CurrentProjection = projection.combined;
		this.polygonBatch.setProjectionMatrix(projection.combined);
		this.updatePixelSize();
	}

	public static Matrix4 DefaultProjection() {
		return defaultProjection;
	}

	private float updatePixelSize() {
		Matrix4 trans = getBatch().getTransformMatrix(), proj = getBatch().getProjectionMatrix();
		mat4.set(proj).mul(trans);
		float scaleX = mat4.getScaleX();
		float worldWidth = 2f / scaleX;
		float newPixelSize = worldWidth / Width;
		return setPixelSize(newPixelSize);
	}

	private float setPixelSize(float pixelSize) {
		float oldPixelSize = this.pixelSize;
		this.pixelSize = pixelSize;
		halfPixelSize = 0.5f * pixelSize;
		offset = EPSILON * pixelSize;
		CurrentPixelSize = this.pixelSize;
		return oldPixelSize;
	}

	public static float getPixelSize() {
		return CurrentPixelSize;
	}

	public static void setLineWidth(float l) {
		Drawer.setDefaultLineWidth(l);
	}

	public static float getLineWidth() {
		return Drawer.getDefaultLineWidth();
	}

	public static void setColor(Color c) {
		thatColor = c;
		Drawer.setColor(c);
	}

	public static void setColor(Quaternion q) {
		// rgba
		Color c = new Color(q.x % 256, q.y % 256, q.z % 256, q.w % 256);
		thatColor = c;
		Drawer.setColor(c);
	}

	public static void setColor(Vector3 q, float a) {
		// rgba
		Color c = new Color(q.x % 256, q.y % 256, q.z % 256, a % 256);
		thatColor = c;
		Drawer.setColor(c);
	}

	public static void setColor(float r, float g, float b, float a) {
		setColor(new Quaternion(r, g, b, a));
	}

	public static void setColor(float f) {
		// rgba
		Color c = new Color(f % 256, f % 256, f % 256, f % 256);
		thatColor = c;
		Drawer.setColor(c);
	}

	public static void setColor(int f) {
		// rgba
		Color c = new Color(f % 256, f % 256, f % 256, f % 256);
		thatColor = c;
		Drawer.setColor(c);
	}

	@Override
	public void dispose() {

		this.polygonBatch.dispose();
		this.font.dispose();
		this.defaultFont.dispose();
		// Disposables.disposeOf(defaultBatch);

	}

	//
	// COLOR
	//

	public static float colorDist(Color a, Color b) {
		return (float) Math.sqrt(Math.pow((a.r - b.r), 2) + Math.pow((a.g - b.g), 2) + Math.pow((a.b - b.b), 2));
	}

	public static float colorDiff(Color a, Color b) {
		return (float) (colorDist(a, b) / Math.sqrt(Math.pow(255, 2) * 3));
	}

	public static Color fromRGB(int r, int g, int b) {
		float RED = r / 255.0f;
		float GREEN = g / 255.0f;
		float BLUE = b / 255.0f;
		return new Color(RED, GREEN, BLUE, 1);
	}

	public static Color fromRGBA(int r, int g, int b, int a) {
		float RED = r / 255.0f;
		float GREEN = g / 255.0f;
		float BLUE = b / 255.0f;
		float ALPHA = a / 255.0f;
		return new Color(RED, GREEN, BLUE, ALPHA);
	}

	public Pixmap extractPixmapFromTextureRegion(TextureRegion textureRegion) {
		TextureData textureData = textureRegion.getTexture().getTextureData();
		if (!textureData.isPrepared()) {
			textureData.prepare();
		}
		Pixmap pixmap = new Pixmap(textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
				textureData.getFormat());
		pixmap.drawPixmap(textureData.consumePixmap(), // The other Pixmap
				0, // The target x-coordinate (top left corner)
				0, // The target y-coordinate (top left corner)
				textureRegion.getRegionX(), // The source x-coordinate (top left corner)
				textureRegion.getRegionY(), // The source y-coordinate (top left corner)
				textureRegion.getRegionWidth(), // The width of the area from the other Pixmap in pixels
				textureRegion.getRegionHeight() // The height of the area from the other Pixmap in pixels
		);
		return pixmap;
	}

	// ImageStream
	// a way of translating bufferedImages from swing/awt/etc

	/*
	 * public Texture textureFromBufferedImage(BufferedImage i) {
	 * 
	 * return new Pixmap();
	 * 
	 * }
	 */
	public ArrayList<Vector2> calcPolygon(Vector2 origin, Vector2 radius, int n) {
		ArrayList<Vector2> points = new ArrayList<Vector2>();
		points.ensureCapacity(n);
		float angle = MathUtils.PI2 / n;
		for (float a = 0; a < MathUtils.PI2; a += angle) {
			float fX = (float) (origin.x + (radius.x * Math.cos(a)));
			float fY = (float) (origin.y + (radius.y * Math.sin(a)));
			points.add(new Vector2(fX, fY));
		}

		return points;
	}

	// genPolygon will create an Polygon iDrawable

	public static Pixmap pixFromTx(Texture t) {
		TextureData d = t.getTextureData();
		if (!d.isPrepared())
			d.prepare();
		Pixmap p = d.consumePixmap();

		return p;
	}

	public static void flipPixmapA(Pixmap p) {
		int w = p.getWidth();
		int h = p.getHeight();
		int hold;

		// change blending to 'none' so that alpha areas will not show
		// previous orientation of image
		p.setBlending(Pixmap.Blending.None);
		for (int y = 0; y < h / 2; y++) {
			for (int x = 0; x < w / 2; x++) {
				// get color of current pixel
				hold = p.getPixel(x, y);
				// draw color of pixel from opposite side of pixmap to current position
				p.drawPixel(x, y, p.getPixel(w - x - 1, y));
				// draw saved color to other side of pixmap
				p.drawPixel(w - x - 1, y, hold);
				// repeat for height/width inverted pixels
				hold = p.getPixel(x, h - y - 1);
				p.drawPixel(x, h - y - 1, p.getPixel(w - x - 1, h - y - 1));
				p.drawPixel(w - x - 1, h - y - 1, hold);
			}
		}
		// set blending back to default
		p.setBlending(Pixmap.Blending.SourceOver);
	}

	public Pixmap flipPixmapB(Pixmap src) {
		Log("!)@");
		final int width = src.getWidth();
		final int height = src.getHeight();
		Pixmap flipped = new Pixmap(width, height, src.getFormat());

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				flipped.drawPixel(x, y, src.getPixel(width - x - 1, y));
			}
		}
		return flipped;
	}

	public static Pixmap rotatePixmap(Pixmap srcPix) {
		final int width = srcPix.getWidth();
		final int height = srcPix.getHeight();
		Pixmap rotatedPix = new Pixmap(height, width, srcPix.getFormat());

		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				rotatedPix.drawPixel(x, y, srcPix.getPixel(y, x));
			}
		}

		srcPix.dispose();
		return rotatedPix;
	}

}
