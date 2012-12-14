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
import org.anddev.andengine.extension.multiplayer.protocol.util.IPUtils;
import org.anddev.andengine.input.touch.IOnSceneTouchListener;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.Debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

public class MultiplayerExample extends BaseExampleGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private static final int SERVER_PORT = 4444;

  private static final short FLAG_ADD_FACE = 1;

  private static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = 0;
  private static final int DIALOG_ENTER_SERVER_IP_ID =
      DIALOG_CHOOSE_SERVER_OR_CLIENT_ID + 1;
  private static final int DIALOG_SHOW_SERVER_IP_ID =
      DIALOG_ENTER_SERVER_IP_ID + 1;

  private Camera mCamera;

  private Texture mTexture;
  private TextureRegion mFaceTextureRegion;

  private BaseServer mServer;
  private ServerConnector mServerConnector;

  private String mServerIP = "127.0.0.1";


  @Override
  @Deprecated
  protected Dialog onCreateDialog(final int id) {
    switch (id) {
    case DIALOG_SHOW_SERVER_IP_ID:
      return new AlertDialog.Builder(this)
      .setIcon(android.R.drawable.ic_dialog_info)
      .setTitle("Your Server-IP ...")
      .setMessage("The IP of your Server is:\n" + IPUtils.getIPAddress(this))
      .setPositiveButton(android.R.string.ok, null)
      .create();
    case DIALOG_ENTER_SERVER_IP_ID:
      final EditText ipEditText = new EditText(this);
      return new AlertDialog.Builder(this)
      .setIcon(android.R.drawable.ic_dialog_info)
      .setTitle("Enter Server-IP ...")
      .setView(ipEditText)
      .setPositiveButton("Connect", new OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
          mServerIP = ipEditText.getText().toString();
          initClient();
        }
      })
      .setNegativeButton(android.R.string.cancel, new OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
          finish();
        }
      })
      .create();
    case DIALOG_CHOOSE_SERVER_OR_CLIENT_ID:
      return new AlertDialog.Builder(this)
      .setIcon(android.R.drawable.ic_dialog_info)
      .setTitle("Be Server or Client ...")
      .setPositiveButton("Client", new OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
          showDialog(DIALOG_ENTER_SERVER_IP_ID);
        }
      })
      .setNeutralButton("Server", new OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
          initServer();
          showDialog(DIALOG_SHOW_SERVER_IP_ID);
        }
      })
      .setNegativeButton("Both", new OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
          initServerAndClient();
          showDialog(DIALOG_SHOW_SERVER_IP_ID);
        }
      })
      .create();
    default:
      return super.onCreateDialog(id);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public Engine onLoadEngine() {
    showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);

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
    mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/boxface.png", 0, 0);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
      @Override
      public boolean onSceneTouchEvent(final Scene pScene, final MotionEvent pSceneMotionEvent) {
        if (pSceneMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
          if (mServer != null) {
            try {
              mServer.sendBroadcastServerMessage(new AddFaceServerMessage(
                pSceneMotionEvent.getX(), pSceneMotionEvent.getY()));
            }
            catch (final IOException e) {
              Debug.e(e);
            }
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

  private void log(final String pMessage) {
    Debug.d(pMessage);
  }

  private void toast(final String pMessage) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(MultiplayerExample.this, pMessage, Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  private void initServerAndClient() {
    initServer();

    // Wait some time after the server has been started, so it actually can
    // start up
    try {
      Thread.sleep(500);
    }
    catch (final Throwable t) {
      Debug.e("Error", t);
    }

    initClient();
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
                    log("SERVER: ClientMessage received: " +
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
      mServerConnector= new ServerConnector(new Socket(mServerIP, SERVER_PORT),
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
                log("CLIENT: ServerMessage received: " +
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

  private class ExampleServerConnectionListener
      extends BaseServerConnectionListener {
    @Override
    protected void onConnectInner(final BaseConnector<BaseServerMessage> pConnector) {
      toast("CLIENT: Connected to server.");
    }

    @Override
    protected void onDisconnectInner(final BaseConnector<BaseServerMessage> pConnector) {
      log("CLIENT: Disconnected from server...");
    }
  }

  private class ExampleServerStateListener implements IServerStateListener {
    @Override
    public void onStarted(final int pPort) {
      log("SERVER: Started at port: " + pPort);
    }

    @Override
    public void onTerminated(final int pPort) {
      log("SERVER: Terminated at port: " + pPort);
    }

    @Override
    public void onException(final Throwable pThrowable) {
      log("SERVER: Exception: " + pThrowable);
    }
  }

  private class ExampleClientConnectionListener
      extends BaseClientConnectionListener {
    @Override
    protected void onConnectInner(final BaseConnector<BaseClientMessage> pConnector) {
      toast("SERVER: Client connected:" + pConnector.getSocket());
    }

    @Override
    protected void onDisconnectInner(final BaseConnector<BaseClientMessage> pConnector) {
      toast("SERVER: Client disconnected:" + pConnector.getSocket());
    }
  }
}
