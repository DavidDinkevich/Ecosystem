package main;

public class Color {
	
	private float r, g, b, a;
	
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color(float r, float g, float b) {
		this(r, g, b, 255f);
	}
	
	public Color(float grey, float a) {
		this(grey, grey, grey, a);
	}
	
	public Color(float grey) {
		this(grey, 255f);
	}
	
	public Color() {
		this(0f);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Color))
			return false;
		Color other = (Color)o;
		return r == other.r && g == other.g && b == other.b && a == other.a;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + Float.hashCode(r);
		result = 31 * result + Float.hashCode(g);
		result = 31 * result + Float.hashCode(b);
		result = 31 * result + Float.hashCode(a);
		return result;
	}
	
	@Override
	public String toString() {
		return "[ " + r + ", " + g + ", " + b + " ]";
	}
	
	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color set(float r, float g, float b) {
		set(r, g, b, 255f);
		return this;
	}
	
	public Color set(float v) {
		set(v, v, v);
		return this;
	}

	public float r() {
		return r;
	}

	public Color setR(float r) {
		this.r = r;
		return this;
	}

	public float g() {
		return g;
	}

	public Color setG(float g) {
		this.g = g;
		return this;
	}

	public float b() {
		return b;
	}

	public Color setB(float b) {
		this.b = b;
		return this;
	}

	public float a() {
		return a;
	}

	public Color setA(float a) {
		this.a = a;
		return this;
	}
	
}
