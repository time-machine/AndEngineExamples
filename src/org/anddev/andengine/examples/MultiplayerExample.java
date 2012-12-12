package org.anddev.andengine.examples;

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
import org.anddev.andengine.extension.multiplayer.protocol.adt.cmd.AbstractCommand;
import org.anddev.andengine.extension.multiplayer.protocol.adt.cmd.connection.ConnectionCloseCommand;
import org.anddev.andengine.extension.multiplayer.protocol.adt.feedback.AbstractServerFeedback;
import org.anddev.andengine.extension.multiplayer.protocol.client.AbstractServerConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.client.BaseServerConnector;
import org.anddev.andengine.extension.multiplayer.protocol.client.IServerFeedbackSwitch;
import org.anddev.andengine.extension.multiplayer.protocol.server.AbstractClientConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.AbstractCommandSwitch;
import org.anddev.andengine.extension.multiplayer.protocol.server.AbstractServer;
import org.anddev.andengine.extension.multiplayer.protocol.server.AbstractServer.IServerStateListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseClientConnector;
import org.anddev.andengine.extension.multiplayer.protocol.shared.AbstractConnector;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.Debug;

public class MultiplayerExample extends BaseExampleGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;

  private Texture mTexture;
  private TextureRegion mFaceTextureRegion;
  private AbstractServer mServer;

  @Override
  public Engine onLoadEngine() {
    mServer = new AbstractServer(4444,
        new AbstractClientConnectionListener() {
          @Override
          protected void onConnectInner(final AbstractConnector<AbstractCommand> pConnector) {
            MultiplayerExample.this.log("Client connected: " + pConnector.getSocket());
          }

          @Override
          protected void onDisconnectInner(final AbstractConnector<AbstractCommand> pConnector) {
            MultiplayerExample.this.log("Client disconnected: " + pConnector.getSocket());
          }
        },
        new IServerStateListener() {
          @Override
          public void onTerminated(final int pPort) {
          }

          @Override
          public void onStarted(final int pPort) {
          }

          @Override
          public void onException(final Throwable pThrowable) {
          }
        }) {
      @Override
      protected BaseClientConnector newClientConnector(final Socket pClientSocket,
          final AbstractClientConnectionListener pClientConnectionListener)
              throws Exception {
        return new BaseClientConnector(pClientSocket, pClientConnectionListener,
            new AbstractCommandSwitch() {
              @Override
              public void doSwitch(final AbstractCommand pCommand)
                  throws IOException {
                MultiplayerExample.this.log("Command received: " +
                    pCommand.toString());
              }
            });
      }
    };
    mServer.start();

    try {
      Thread.sleep(500);
    }
    catch (final Throwable t) {
      Debug.e("Error", t);
    }

    try {
      final BaseServerConnector baseServerConnector = new BaseServerConnector(
          new Socket("127.0.0.1", 4444),
          new AbstractServerConnectionListener() {
            @Override
            protected void onConnectInner(
                final AbstractConnector<AbstractServerFeedback> pConnector) {
              MultiplayerExample.this.log("Connected to server.");
            }

            @Override
            protected void onDisconnectInner(
                final AbstractConnector<AbstractServerFeedback> pConnector) {
              MultiplayerExample.this.log("Disconnected from server...");
            }
          },
          new IServerFeedbackSwitch() {
            @Override
            public void doSwitch(final AbstractServerFeedback pServerFeedback)
                throws IOException {
              MultiplayerExample.this.log("ServerFeedback received: " +
                pServerFeedback.toString());
            }
          }
      );
      baseServerConnector.start();
      baseServerConnector.sendCommand(new ConnectionCloseCommand());
      baseServerConnector.getSocket().close();
    }
    catch (final Throwable t) {
      Debug.e("Error", t);
    }

    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera, false));
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

    // Create the face and add it to the scene
    final Sprite face = new Sprite(x, y, mFaceTextureRegion);
    scene.getTopLayer().addEntity(face);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }

  private void log(final String pMessage) {
    Debug.d(pMessage);
  }
}
