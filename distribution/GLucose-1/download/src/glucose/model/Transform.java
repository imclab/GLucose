package glucose.model;

import java.util.Stack;

class Transform {
		
	private class Matrix {
		
		private double
			m11=1, m12=0, m13=0, m14=0,
			m21=0, m22=1, m23=0, m24=0,
			m31=0, m32=0, m33=1, m34=0,
			m41=0, m42=0, m43=0, m44=1;
		
		private Matrix() {}
		
		private Matrix(Matrix m) {
			m11 = m.m11; m12 = m.m12; m13 = m.m13; m14 = m.m14;
			m21 = m.m21; m22 = m.m22; m23 = m.m23; m24 = m.m24;
			m31 = m.m31; m32 = m.m32; m33 = m.m33; m34 = m.m34;
			m41 = m.m41; m42 = m.m42; m43 = m.m43; m44 = m.m44;
		}
		
		void multiply(Matrix m) {
			double a11 = m11*m.m11 + m12*m.m21 + m13*m.m31 + m14*m.m41;
			double a12 = m11*m.m12 + m12*m.m22 + m13*m.m32 + m14*m.m42;
			double a13 = m11*m.m13 + m12*m.m23 + m13*m.m33 + m14*m.m43;
			double a14 = m11*m.m14 + m12*m.m24 + m13*m.m34 + m14*m.m44;
			
			double a21 = m21*m.m11 + m22*m.m21 + m23*m.m31 + m24*m.m41;
			double a22 = m21*m.m12 + m22*m.m22 + m23*m.m32 + m24*m.m42;
			double a23 = m21*m.m13 + m22*m.m23 + m23*m.m33 + m24*m.m43;
			double a24 = m21*m.m14 + m22*m.m24 + m23*m.m34 + m24*m.m44;
			
			double a31 = m31*m.m11 + m32*m.m21 + m33*m.m31 + m34*m.m41;
			double a32 = m31*m.m12 + m32*m.m22 + m33*m.m32 + m34*m.m42;
			double a33 = m31*m.m13 + m32*m.m23 + m33*m.m33 + m34*m.m43;
			double a34 = m31*m.m14 + m32*m.m24 + m33*m.m34 + m34*m.m44;		

			double a41 = m41*m.m11 + m42*m.m21 + m43*m.m31 + m44*m.m41;
			double a42 = m41*m.m12 + m42*m.m22 + m43*m.m32 + m44*m.m42;
			double a43 = m41*m.m13 + m42*m.m23 + m43*m.m33 + m44*m.m43;
			double a44 = m41*m.m14 + m42*m.m24 + m43*m.m34 + m44*m.m44;		
		
			m11 = a11; m12 = a12; m13 = a13; m14 = a14;
			m21 = a21; m22 = a22; m23 = a23; m24 = a24;
			m31 = a31; m32 = a32; m33 = a33; m34 = a34;
			m41 = a41; m42 = a42; m43 = a43; m44 = a44;
		}
	}
	
	private final Stack<Matrix> matrices;
	
	Transform() {
		matrices = new Stack<Matrix>();
		matrices.push(new Matrix());
	}
	
	Matrix matrix() {
		return matrices.peek();
	}
	
	double x() {
		return matrix().m14; 
	}
	
	double y() {
		return matrix().m24;
	}
	
	double z() {
		return matrix().m34;
	}
	
	void translate(double tx, double ty, double tz) {
		Matrix m = new Matrix();
		m.m14 = tx;
		m.m24 = ty;
		m.m34 = tz;
		matrix().multiply(m);
	}
	
	void rotateX(double rx) {
		Matrix m = new Matrix();
		m.m22 = Math.cos(-rx);
		m.m23 = -Math.sin(-rx);
		m.m32 = -m.m23;
		m.m33 = m.m22;
		matrix().multiply(m);
	}
	
	void rotateY(double rx) {
		Matrix m = new Matrix();
		m.m11 = Math.cos(rx);
		m.m13 = -Math.sin(rx);
		m.m31 = -m.m13;
		m.m33 = m.m11;
		matrix().multiply(m);	
	}
	
	void rotateZ(double rx) {
		Matrix m = new Matrix();
		m.m11 = Math.cos(-rx);
		m.m12 = -Math.sin(-rx);
		m.m21 = -m.m12;
		m.m22 = m.m11;
		matrix().multiply(m);
	}
	
	void push() {
		matrices.push(new Matrix(matrices.peek()));
	}
	
	void pop() {
		matrices.pop();
	}

}
