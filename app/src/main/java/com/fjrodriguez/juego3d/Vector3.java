package com.fjrodriguez.juego3d;

import android.util.FloatMath;

/**
 * Created by fjrodriguez on 2/07/14.
 */
public class Vector3 {

    public float x;
    public float y;
    public float z;

    public Vector3(float _x, float _y, float _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    //////////////////// Vector Operators ////////////////////////////////////

    void Multiply(float v) {
        x *= v;
        y *= v;
        z *= v;
    }

    ///////////// Static Vector Operations ///////////////////////////////////

    static Vector3 Add(Vector3 vec1, Vector3 vec2) {
        Vector3 result = new Vector3(0,0,0);

        result.x = vec1.x + vec2.x;
        result.y = vec1.y + vec2.y;
        result.z = vec1.z + vec2.z;

        return result;
    }

    /////////////////////////////////////////////////////////////////////////

    void Set(float _x, float _y, float _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    void Normalize() {
        float l = Length();

        x = x/l;
        y = y/l;
        z = z/l;
    }

    float Length() {
        return FloatMath.sqrt(x * x + y * y + z * z);
        //return java.lang.Math.sqrt(x*x + y*y + z*z);
    }

    static Vector3 CrossProduct(Vector3 a, Vector3 b) {
        Vector3 result = new Vector3(0,0,0);

        result.x= (a.y*b.z) - (a.z*b.y);
        result.y= (a.z*b.x) - (a.x*b.z);
        result.z= (a.x*b.y) - (a.y*b.x);

        return result;
    }

    void Negate() {
        x = -x;
        y = -y;
        z = -z;
    }

}
