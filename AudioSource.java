package JGEngine;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class AudioSource extends Component {
    Media sound;
    MediaPlayer player;
    public float volume = 1;

    @Override
    public void update() {
        if(volume > 1) volume = 1;
        if(volume < 0) volume = 0;
        Vector2D screenPosition = Camera2D.main.worldSpaceToScreen(gameObject);
        screenPosition.vector2Ddiv(RenderSystem.renderSystem.windowSize);
        player.setBalance(screenPosition.x * 2 - 1);
        player.setVolume(1 / Mathf.sqr(
                Vector2D.distance(
                        gameObject.getComponent(Transform2D.class).getGlobalPosition(),
                        Camera2D.main.gameObject().getComponent(Transform2D.class).getGlobalPosition()
                ) / Camera2D.main.orthographicWidth + 1) * volume);
    }

    public void setSound(String source) {
        sound = new Media(new File(source).toURI().toString());
        player = new MediaPlayer(sound);
    }

    public void playOneShot() {
        player.setOnEndOfMedia(() -> {});
        player.play();
    }

    public void playLoop() {
        player.setOnEndOfMedia(() -> {
            player.seek(Duration.ZERO);
            player.play();
        });
        player.play();
    }

    public void stop() {
        player.stop();
    }

    public float getRealVolume() {
        return (float)player.getVolume();
    }
}
