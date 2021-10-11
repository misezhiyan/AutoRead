import javax.sound.sampled.AudioFileFormat;

public class Main {
    public static void main(String[] args) {

        Recorder2 recorder = new Recorder2();
        recorder.recordShort("F:\\workspace\\workspace_kimmy\\workspace_sound\\record", AudioFileFormat.Type.WAVE, "NUM");
    }
}
