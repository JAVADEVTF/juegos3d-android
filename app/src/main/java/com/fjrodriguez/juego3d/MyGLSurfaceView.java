package com.fjrodriguez.juego3d;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by fjrodriguez on 2/07/14.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Render for drawing on the GLSurfaceView
        setRenderer( new MyGLRenderer(context));
    }

    private class MyGLRenderer implements Renderer {

        private Context m_Context;
        private PointLight m_PointLight;
        private Camera m_Camera;
        private int m_ViewPortWidth;
        private int m_ViewPortHeight;
        private Cube m_Cube;

        public MyGLRenderer(Context context) {
            m_Context = context;
        }

        void SetupLights() {

            // Set Light Characteristics
            Vector3 LightPosition = new Vector3(0,125,125);

            float[] AmbientColor = new float [3];
            AmbientColor[0] = 0.0f;
            AmbientColor[1] = 0.0f;
            AmbientColor[2] = 0.0f;

            float[] DiffuseColor = new float [3];
            DiffuseColor[0] = 1.0f;
            DiffuseColor[1] = 1.0f;
            DiffuseColor[2] = 1.0f;

            float[] SpecularColor = new float [3];
            SpecularColor[0] = 1.0f;
            SpecularColor[1] = 1.0f;
            SpecularColor[2] = 1.0f;

            m_PointLight.SetPosition(LightPosition);
            m_PointLight.SetAmbientColor(AmbientColor);
            m_PointLight.SetDiffuseColor(DiffuseColor);
            m_PointLight.SetSpecularColor(SpecularColor);
        }

        void SetupCamera() {

            // Set Camera View
            Vector3 Eye = new Vector3(0,0,8);
            Vector3 Center = new Vector3(0,0,-1);
            Vector3 Up = new Vector3(0,1,0);

            float ratio = (float) m_ViewPortWidth / m_ViewPortHeight;
            float Projleft = -ratio;
            float Projright = ratio;
            float Projbottom = -1;
            float Projtop = 1;
            float Projnear = 3;
            float Projfar = 50; // 100;

            m_Camera = new Camera(m_Context,
                    Eye,
                    Center,
                    Up,
                    Projleft, Projright,
                    Projbottom, Projtop,
                    Projnear, Projfar);
        }

        void CreateCube (Context iContext) {

            // Create Cube Shader
            Shader shader = new Shader(iContext, R.raw.vsonelight, R.raw.fsonelight);

            MeshEx CubeMesh = new MeshEx(8,0,3,5,Cube.CubeData,Cube.CubeDrawOrder);

            // Create Material for this object
            Material Material1 = new Material();

            // Create Texture
            Texture TexAndroid = new Texture(iContext, R.drawable.ic_launcher);
            Texture[] CubeTex = new Texture[1];
            CubeTex[0] = TexAndroid;

            m_Cube = new Cube(iContext,
                    CubeMesh,
                    CubeTex,
                    Material1,
                    shader);

            // Set Initial Position and Orientation.
            Vector3 Axis = new Vector3(0,1,0);
            Vector3 Position = new Vector3(0.0f,0.0f,0.0f);
            Vector3 Scale = new Vector3(1.0f,1.0f,1.0f);

            m_Cube.m_Orientation.SetPosition(Position);
            m_Cube.m_Orientation.SetRotationAxis(Axis);
            m_Cube.m_Orientation.SetScale(Scale);

            // m_Cube.m_Orientation.AddRotation(45);
        }

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

            m_PointLight = new PointLight(m_Context);
            SetupLights();
            CreateCube(m_Context);
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {

            // Ignore the passed-in GL10 interface, and use the GLES20
            // class's static methods instead.
            GLES20.glViewport(0,0,width,height);
            m_ViewPortWidth = width;
            m_ViewPortHeight = height;
            SetupCamera();
        }

        @Override
        public void onDrawFrame(GL10 gl10) {

            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            m_Camera.UpdateCamera();
            m_Cube.m_Orientation.AddRotation(1);
            m_Cube.DrawObject(m_Camera, m_PointLight);
        }
    }
}
