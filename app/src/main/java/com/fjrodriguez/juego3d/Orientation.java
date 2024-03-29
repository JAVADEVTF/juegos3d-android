package com.fjrodriguez.juego3d;

import android.content.Context;
import android.opengl.Matrix;

/**
 * Created by fjrodriguez on 2/07/14.
 */
public class Orientation {

    private Context m_Context;

    // Local Axes
    private Vector3	m_Right;
    private Vector3	m_Up;
    private Vector3	m_Forward;

    // Position, angle of rotation in degrees, and scale of an Object
    private Vector3	m_Position;
    private float	m_RotationAngle; // In Degrees
    private Vector3	m_RotationAxis;
    private Vector3	m_Scale;

    // Orientation Matrices
    private float[] m_OrientationMatrix = new float[16];
    private float[] m_PositionMatrix = new float[16];
    private float[] m_RotationMatrix = new float[16];
    private float[] m_ScaleMatrix = new float[16];

    private float[] TempMatrix = new float[16];
    private Vector3 m_UpWorldVec 	  = new Vector3(0,0,0);
    private Vector3 m_RightWorldVec   = new Vector3(0,0,0);
    private Vector3 m_ForwardWorldVec = new Vector3(0,0,0);

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Orientation(Context context)
    {
        m_Context = context;

        m_Right 	=	new Vector3(1.0f,0.0f,0.0f);
        m_Up 		= 	new Vector3(0.0f,1.0f,0.0f);
        m_Forward	=	new Vector3(0.0f,0.0f,1.0f);

        m_Position	=	new Vector3(0.0f,0.0f,0.0f);
        m_Scale		= 	new Vector3(1.0f,1.0f,1.0f);

        m_RotationAngle = 0;
        m_RotationAxis 	= 	new Vector3(0.0f,1.0f,0.0f);

        Matrix.setIdentityM(m_OrientationMatrix, 0);
        Matrix.setIdentityM(m_RotationMatrix, 0);
    }

    /////////////////////////////// Local Axes /////////////////////////////////////////

    void SetForward(Vector3 value)
    {
        m_Forward = value;
    }

    void SetRight(Vector3 value)
    {
        m_Right = value;
    }

    void SetUp(Vector3 value)
    {
        m_Up = value;
    }

    Vector3 GetForward()
    {
        return m_Forward;
    }

    Vector3 GetRight()
    {
        return m_Right;
    }

    Vector3 GetUp()
    {
        return m_Up;
    }

    Vector3 GetUpWorldCoords()
    {
        float[] UpWorld 	= new float[4];
        float[] UpLocal 	= new float[4];

        UpLocal[0] = m_Up.x;
        UpLocal[1] = m_Up.y;
        UpLocal[2] = m_Up.z;
        UpLocal[3] = 1;

        //void multiplyMV (float[] resultVec, int resultVecOffset, float[] lhsMat, int lhsMatOffset, float[] rhsVec, int rhsVecOffset)
        Matrix.multiplyMV(UpWorld, 0, m_RotationMatrix, 0, UpLocal, 0);
        m_UpWorldVec.Set(UpWorld[0], UpWorld[1], UpWorld[2]);
        m_UpWorldVec.Normalize();

        return m_UpWorldVec;
    }

    Vector3 GetRightWorldCoords()
    {
        float[] RightWorld 	= new float[4];
        float[] RightLocal 	= new float[4];

        RightLocal[0] = m_Right.x;
        RightLocal[1] = m_Right.y;
        RightLocal[2] = m_Right.z;
        RightLocal[3] = 1;

        //void multiplyMV (float[] resultVec, int resultVecOffset, float[] lhsMat, int lhsMatOffset, float[] rhsVec, int rhsVecOffset)
        Matrix.multiplyMV(RightWorld, 0, m_RotationMatrix, 0, RightLocal, 0);
        m_RightWorldVec.Set(RightWorld[0], RightWorld[1], RightWorld[2]);
        m_RightWorldVec.Normalize();

        return m_RightWorldVec;
    }

    Vector3 GetForwardWorldCoords()
    {
        float[] ForwardWorld 	= new float[4];
        float[] ForwardLocal 	= new float[4];

        ForwardLocal[0] = m_Forward.x;
        ForwardLocal[1] = m_Forward.y;
        ForwardLocal[2] = m_Forward.z;
        ForwardLocal[3] = 1;

        //void multiplyMV (float[] resultVec, int resultVecOffset, float[] lhsMat, int lhsMatOffset, float[] rhsVec, int rhsVecOffset)
        Matrix.multiplyMV(ForwardWorld, 0, m_RotationMatrix, 0, ForwardLocal, 0);
        m_ForwardWorldVec.Set(ForwardWorld[0], ForwardWorld[1], ForwardWorld[2]);
        m_ForwardWorldVec.Normalize();

        return m_ForwardWorldVec;
    }

    /////////////////////////////// Location, Rotation, and Scaling /////////////////////////////
    Vector3 GetPosition()
    {
        return m_Position;
    }

    float GetRotationAngle()
    {
        return m_RotationAngle;
    }

    Vector3 GetRotationAxis()
    {
        return m_RotationAxis;
    }

    Vector3 GetScale()
    {
        return m_Scale;
    }

    // Accessors for Matrices
    float[] GetPositonMatrix()
    {
        return	m_PositionMatrix;
    }

    float[] GetRotationMatrix()
    {
        return m_RotationMatrix;
    }

    float[] GetScaleMatrix()
    {
        return m_ScaleMatrix;
    }

    // Set Orientation Matrices
    void SetPositionMatrix(Vector3 position)
    {
        // Build Translation Matrix
        Matrix.setIdentityM(m_PositionMatrix, 0);
        Matrix.translateM(m_PositionMatrix, 0, position.x, position.y, position.z);
    }

    void SetRotationMatrix(float angle, Vector3 Axis)
    {
        // Build Rotation Matrix
        Matrix.setIdentityM(m_RotationMatrix, 0);
        Matrix.setRotateM(m_RotationMatrix, 0, angle, Axis.x, Axis.y, Axis.z);
    }

    void SetScaleMatrix(Vector3 Scale)
    {
        // Build Scale Matrix
        Matrix.setIdentityM(m_ScaleMatrix, 0);
        Matrix.scaleM(m_ScaleMatrix, 0, Scale.x, Scale.y, Scale.z);
    }

    /////////////////////////////////////// Position ////////////////////////////////////////////

    void SetPosition(Vector3 Pos)
    {
        m_Position = Pos;
    }

    ///////////////////////////////////////// Rotation ///////////////////////////////////////////

    void SetRotationAxis(Vector3 Axis)
    {
        m_RotationAxis = Axis;
    }

    void AddRotation(float AngleIncrementDegrees)
    {
        m_RotationAngle += AngleIncrementDegrees;

        //rotateM(float[] m, int mOffset, float a, float x, float y, float z)
        //Rotates matrix m in place by angle a (in degrees) around the axis (x, y, z)
        Matrix.rotateM(m_RotationMatrix, 0,
                AngleIncrementDegrees,
                m_RotationAxis.x,
                m_RotationAxis.y,
                m_RotationAxis.z);
    }



    ////////////////////////////////////////////// Scale ////////////////////////////////////////////

    void SetScale(Vector3 Scale)
    {
        m_Scale = Scale;
    }

    ///////////////////////////////// Creates General Matrix for updating Objects ////////////////////////////////

    float[] UpdateOrientation()
    {
        // Build Translation Matrix
        SetPositionMatrix(m_Position);

        // Build Scale Matrix
        SetScaleMatrix(m_Scale);

        // Then Rotate object around Axis then translate
        Matrix.multiplyMM(TempMatrix, 0, m_PositionMatrix, 0, m_RotationMatrix, 0);

        // Scale Object first
        Matrix.multiplyMM(m_OrientationMatrix, 0, TempMatrix, 0, m_ScaleMatrix, 0);

        return m_OrientationMatrix;
    }

}
