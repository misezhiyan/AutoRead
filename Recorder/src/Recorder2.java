import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Recorder2 {

    private AudioFormat audioFormat;

    private AudioFormat getAudioFormat() {

        if (null != audioFormat) {
            return audioFormat;
        }

        // 获得指定的音频格式
        //取得录音输入流
        float sampleRate = 16000;
        // 8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        // 8,16
        int channels = 1;
        // 1,2
        boolean signed = true;
        // true,false
        boolean bigEndian = false;
        // true,false
        audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        return audioFormat;
    }

    // 短音频记录
    public void recordShort(String packageFullPath, AudioFileFormat.Type fileType, String fileNameType) {

        AudioFormat audioFormat = getAudioFormat();

        //声音录入的权值
        int weight = 7;
        //判断是否停止的计数
        int downSum = 0;

        boolean recordExist = false;
        boolean flag = true;

        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AudioInputStream ais = null;

        try {

            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            byte[] fragment = new byte[1024];

            // 循环录制
            for (int i = 0; weight > 0; i++) {

                File audioFile = new File(packageFullPath + "\\wav" + i + ".wav");

                ais = new AudioInputStream(targetDataLine);
                while (flag) {
                    targetDataLine.read(fragment, 0, fragment.length);
                    //当数组末位大于weight时开始存储字节（有声音传入），一旦开始不再需要判断末位
                    if (Math.abs(fragment[fragment.length - 1]) > weight || baos.size() > 0) {
                        baos.write(fragment);
                        System.out.println("守卫：" + fragment[0] + ",末尾：" + fragment[fragment.length - 1] + ",lenght: " + fragment.length);
                        //判断语音是否停止
                        if (Math.abs(fragment[fragment.length - 1]) <= weight) {
                            downSum++;
                        } else {
                            System.out.println("重置奇数");
                            downSum = 0;
                        }
                        //计数超过20说明此段时间没有声音传入(值也可更改)
                        if (downSum > 5) {
                            System.out.println("停止录入");
                            recordExist = true;
                            flag = false;
                        }
                    }
                }

                //定义最终保存的文件名
                if (recordExist) {
                    byte audioData[] = baos.toByteArray();
                    bais = new ByteArrayInputStream(audioData);
                    ais = new AudioInputStream(bais, audioFormat, audioData.length / audioFormat.getFrameSize());
                    System.out.println("开始生成语音文件");
                    AudioSystem.write(ais, fileType, audioFile);
                    downSum = 0;
                    flag = true;
                    recordExist = false;
                    baos.reset();
                }
            }

            targetDataLine.stop();
            targetDataLine.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ais.close();
                bais.close();
                baos.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}