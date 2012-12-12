package org.anddev.andengine.examples;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.client.BaseServerConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.client.IServerMessageSwitch;
import org.anddev.andengine.extension.multiplayer.protocol.client.ServerConnector;
import org.anddev.andengine.extension.multiplayer.protocol.client.ServerMessageExtractor;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseClientConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseClientMessageSwitch;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseServer;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseServer.IServerStateListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnector;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientMessageExtractor;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseConnector;
import org.anddev.andengine.input.touch.IOnSceneTouchListener;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.Debug;

import android.os.Handler;
import android.view.MotionEvent;

public class MultiplayerExample extends BaseExampleGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private static final String SERVER_IP = "127.0.0.1";
  private static final int SERVER_PORT = 4444;

  private static final short FLAG_ADD_FACE = 1;

  private Camera mCamera;

  private Texture mTexture;
  private TextureRegion mFaceTextureRegion;

  private BaseServer mServer;
  private ServerConnector mServerConnector;

  @Override
  public Engine onLoadEngine() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        MultiplayerExample.this.initServer();

        // wait some time after the server has been started, so it actually can
        // start up
        try {
          Thread.sleep(500);
        }
        catch (final Throwable t) {
          Debug.e("Error", t);
        }

        MultiplayerExample.this.initClient();
      }
    }, 500);
//    mServer = new AbstractServer(4444,
//        new AbstractClientConnectionListener() {
//          @Override
//          protected void onConnectInner(final AbstractConnector<AbstractCommand> pConnector) {
//            MultiplayerExample.this.log("Client connected: " + pConnector.getSocket());
//          }
//
//          @Override
//          protected void onDisconnectInner(final AbstractConnector<AbstractCommand> pConnector) {
//            MultiplayerExample.this.log("Client disconnected: " + pConnector.getSocket());
//          }
//        },
//        new IServerStateListener() {
//          @Override
//          public void onTerminated(final int pPort) {
//          }
//
//          @Override
//          public void onStarted(final int pPort) {
//          }
//
//          @Override
//          public void onException(final Throwable pThrowable) {
//          }
//        }) {
//      @Override
//      protected BaseClientConnector newClientConnector(final Socket pClientSocket,
//          final AbstractClientConnectionListener pClientConnectionListener)
//              throws Exception {
//        return new BaseClientConnector(pClientSocket, pClientConnectionListener,
//            new AbstractCommandSwitch() {
//              @Override
//              public void doSwitch(final AbstractCommand pCommand)
//                  throws IOException {
//                MultiplayerExample.this.log("Command received: " +
//                    pCommand.toString());
//              }
//            });
//      }
//    };
//    mServer.start();
//
//
//
//
//    catch (final Throwable t) {
//      Debug.e("Error", t);
//    }

    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera, false));
  }

  @Override
  protected void onDestroy() {
    if (mServer != null) {
      mServer.interrupt();
    }

    if (mServerConnector != null) {
      mServerConnector.interrupt();
    }

    super.onDestroy();
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/boxface.png", 0, 0);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    // Calculate the coordinates for the face, so its centered on the camera
    final int x = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int y = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;

    addFace(scene, x, y);

    scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
      @Override
      public boolean onSceneTouchEvent(final Scene pScene, final MotionEvent pSceneMotionEvent) {
        if (pSceneMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
          try {
            mServer.sendBroadcastServerMessage(new AddFaceServerMessage(
                pSceneMotionEvent.getX(), pSceneMotionEvent.getY()));
          }
          catch (final IOException e) {
            Debug.e(e);
          }
          return true;
        }
        return false;
      }
    });

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }

  public void addFace(final Scene pScene, final float pX, final float pY) {
    // create the face and add it to the scene
    final Sprite face = new Sprite(pX, pY, mFaceTextureRegion);
    pScene.getTopLayer().addEntity(face);
  }

  private static void log(final String pMessage) {
    Debug.d(pMessage);
  }

  private void initServer() {
    mServer = new BaseServer(SERVER_PORT, new ExampleClientConnectionListener(),
        new ExampleServerStateListener()) {
          @Override
          protected ClientConnector newClientConnector(final Socket pClientSocket,
              final BaseClientConnectionListener pClientConnectionListener)
              throws Exception {
            return new ClientConnector(pClientSocket, pClientConnectionListener,
                new ClientMessageExtractor() {
                  @Override
                  public BaseClientMessage readMessage(final short pFlag,
                      final DataInputStream pDataInputStream) throws IOException {
                        return super.readMessage(pFlag, pDataInputStream);
                  }
                },
                new BaseClientMessageSwitch() {
                  @Override
                  public void doSwitch(final BaseClientMessage pClientMessage)
                      throws IOException {
                    MultiplayerExample.log("SERVER: ClientMessage received: " +
                      pClientMessage.toString());
                  }
                }
            );
          }
        };
    mServer.start();
  }


  private void initClient() {
    try {
      mServerConnector= new ServerConnector(new Socket(SERVER_IP, SERVER_PORT),
        new ExampleServerConnectionListener(),
        new ServerMessageExtractor() {
          @Override
          public BaseServerMessage readMessage(final short pFlag,
              final DataInputStream pDataInputStream) throws IOException {
            switch (pFlag) {
            case FLAG_ADD_FACE:
              return new AddFaceServerMessage(pDataInputStream);
            default:
              return super.readMessage(pFlag, pDataInputStream);
            }
          }
        },
        new IServerMessageSwitch() {
          @Override
          public void doSwitch(final BaseServerMessage pServerMessage) throws IOException {
            switch (pServerMessage.getFlag()) {
            case FLAG_ADD_FACE:
              final AddFaceServerMessage addFaceServerMessage =
                  (AddFaceServerMessage)pServerMessage;
              addFace(getEngine().getScene(), addFaceServerMessage.mX,
                  addFaceServerMessage.mY);
              break;
            default:
              MultiplayerExample.log("CLIENT: ServerMessage received: " +
                  pServerMessage.toString());
            }
          }
        }
    );
    mServerConnector.start();
  }
    catch (final Throwable t) {
      Debug.e("Error", t);
    }
  }

  private static class AddFaceServerMessage extends BaseServerMessage {
    public float mX;
    public float mY;

    public AddFaceServerMessage(final float pX, final float pY) {
      mX = pX;
      mY = pY;
    }

    public AddFaceServerMessage(final DataInputStream pDataInputStream)
        throws IOException {
      mX = pDataInputStream.readFloat();
      mY = pDataInputStream.readFloat();
    }

    @Override
    public short getFlag() {
      return FLAG_ADD_FACE;
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream)
        throws IOException {
      pDataOutputStream.writeFloat(mX);
      pDataOutputStream.writeFloat(mY);
    }

    @Override
    protected void onAppendTransmissionDataForToString(
        final StringBuilder pStringBuilder) {
    }
  }

  private static class ExampleServerConnectionListener
      extends BaseServerConnectionListener {
    @Override
    protected void onConnectInner(final BaseConnector<BaseServerMessage> pConnector) {
      MultiplayerExample.log("CLIENT: Connected to server.");
    }

    @Override
    protected void onDisconnectInner(final BaseConnector<BaseServerMessage> pConnector) {
      MultiplayerExample.log("CLIENT: Disconnected from server...");
    }
  }

  private static class ExampleServerStateListener implements IServerStateListener {
    @Override
    public void onStarted(final int pPort) {
      MultiplayerExample.log("SERVER: Started at port: " + pPort);
    }

    @Override
    public void onTerminated(final int pPort) {
      MultiplayerExample.log("SERVER: Terminated at port: " + pPort);
    }

    @Override
    public void onException(final Throwable pThrowable) {
      MultiplayerExample.log("SERVER: Exception: " + pThrowable);
    }
  }

  private static class ExampleClientConnectionListener
      extends BaseClientConnectionListener {
    @Override
    protected void onConnectInner(final BaseConnector<BaseClientMessage> pConnector) {
      MultiplayerExample.log("SERVER: Client connected:" + pConnector.getSocket());
    }

    @Override
    protected void onDisconnectInner(final BaseConnector<BaseClientMessage> pConnector) {
      MultiplayerExample.log("SERVER: Client disconnected:" + pConnector.getSocket());
    }
  }
}
