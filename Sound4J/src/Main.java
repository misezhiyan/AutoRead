import jmp123.demo.MiniPlayer;
import jmp123.output.Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {

//        playMp3("F:\\workspace\\workspace_kimmy\\workspace_sound\\music\\姚贝娜 - 暗香.mp3");

        File pkgWav = new File("F:\\workspace\\workspace_kimmy\\workspace_sound\\record");
        for (File wavFile: pkgWav.listFiles()) {
            playWav(wavFile.getAbsolutePath());
        }

//        playPackage("F:\\workspace\\workspace_kimmy\\workspace_sound\\music\\");
    }

    private static void playPackage(String packagePath) throws IOException {

        File pkg = new File(packagePath);
        File[] files = pkg.listFiles();

        for (File file : files) {
            if (isMp3(file)) {
                System.out.println("播放mp3: " + file.getName());
                playMp3(file.getPath());
            }
        }
    }

    private static boolean isMp3(File file) {
        String fileName = file.getName();
        if (fileName.contains(".") && fileName.toLowerCase().endsWith("mp3")) {
            return true;
        }
        return false;
    }

    /**
     * 播放wav
     * @param wavFullPath
     */
    private static void playWav(String wavFullPath) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        System.out.println("播放wav: " + wavFullPath);

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(wavFullPath));
        AudioFormat format = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        byte[] b = new byte[1024];
        int len = 0;

        line.open(format);
        line.start();

        while ((len = audioInputStream.read(b)) > 0) {
            line.write(b, 0, len);
        }
        audioInputStream.close();
        line.drain();
        line.close();
    }

    /**
     * 播放mp3
     * @param mp3FullPath
     * @throws IOException
     */
    private static void playMp3(String mp3FullPath) throws IOException {
        MiniPlayer miniPlayer = new MiniPlayer(new Audio());
        miniPlayer.open(mp3FullPath);
        int frameCount = miniPlayer.getFrameCount();
        miniPlayer.run();
    }
}
