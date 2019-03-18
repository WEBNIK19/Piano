package com.example.piano;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import android.util.SparseArray;

import java.io.InputStream;

class AudioSoundPlayer {

    private SparseArray<PlayThread> threadMap = null;
    private Context context;
    private static final SparseArray<String> SOUND_MAP = new SparseArray<>();
    public static final int MAX_VOLUME = 100, CURRENT_VOLUME = 90;

    static {
        // white keys sounds
        SOUND_MAP.put(1, "c");
        SOUND_MAP.put(2, "d");
        SOUND_MAP.put(3, "e");
        SOUND_MAP.put(4, "f");
        SOUND_MAP.put(5, "g");
        SOUND_MAP.put(6, "a");
        SOUND_MAP.put(7, "h");
        SOUND_MAP.put(8, "c2");
        SOUND_MAP.put(9, "d2");
        SOUND_MAP.put(10, "e2");
        SOUND_MAP.put(11, "f2");
        SOUND_MAP.put(12, "g2");
        SOUND_MAP.put(13, "a2");
        SOUND_MAP.put(14, "h2");
        SOUND_MAP.put(15, "c3");
        SOUND_MAP.put(16, "d3");
        SOUND_MAP.put(17, "e3");
        SOUND_MAP.put(18, "f3");
        SOUND_MAP.put(19, "g3");
        SOUND_MAP.put(20, "a3");
        SOUND_MAP.put(21, "h3");
        SOUND_MAP.put(22, "c4");
        SOUND_MAP.put(23, "d4");
        SOUND_MAP.put(24, "e4");
        SOUND_MAP.put(25, "f4");
        SOUND_MAP.put(26, "g4");
        SOUND_MAP.put(27, "a4");
        SOUND_MAP.put(28, "h4");

        SOUND_MAP.put(29, "cis");
        SOUND_MAP.put(30, "dis");
        SOUND_MAP.put(31, "fis");
        SOUND_MAP.put(32, "gis");
        SOUND_MAP.put(33, "ais");
        SOUND_MAP.put(34, "c2is");
        SOUND_MAP.put(35, "d2is");
        SOUND_MAP.put(36, "f2is");
        SOUND_MAP.put(37, "g2is");
        SOUND_MAP.put(38, "a2is");
        SOUND_MAP.put(39, "c3is");
        SOUND_MAP.put(40, "d3is");
        SOUND_MAP.put(41, "f3is");
        SOUND_MAP.put(42, "g3is");
        SOUND_MAP.put(43, "a3is");
        SOUND_MAP.put(44, "c4is");
        SOUND_MAP.put(45, "d4is");
        SOUND_MAP.put(46, "f4is");
        SOUND_MAP.put(47, "g4is");
        SOUND_MAP.put(48, "a4is");
    }
    
    public AudioSoundPlayer(Context context) {
        this.context = context;
        threadMap = new SparseArray<>();
    }

    public boolean isNotePlaying(int note) {
        return threadMap.get(note) != null;
    }

    public void playNote(int note) {
        if (!isNotePlaying(note)) {
            PlayThread thread = new PlayThread(note);
            thread.start();
            threadMap.put(note, thread);
        }
    }

    public void stopNote(int note) {
        PlayThread thread = threadMap.get(note);

        if (thread != null) {
            threadMap.remove(note);
        }
    }

    private class PlayThread extends Thread {
        int note;
        AudioTrack audioTrack;

        public PlayThread(int note) {
            this.note = note;
        }

        @Override
        public void run() {
            try {
                String path = "cello/" + SOUND_MAP.get(note) + ".wav";

                AssetManager assetManager = context.getAssets();

                AssetFileDescriptor ad = assetManager.openFd(path);

                long fileSize = ad.getLength();
                int bufferSize = 4096;
                byte[] buffer = new byte[bufferSize];

                audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

                float logVolume = (float) (1 - (Math.log(MAX_VOLUME - CURRENT_VOLUME) / Math.log(MAX_VOLUME)));
                audioTrack.setStereoVolume(logVolume, logVolume);

                audioTrack.play();
                int headerOffset = 0x2C; long bytesWritten = 4096*4; int bytesRead;

                InputStream audioStream = assetManager.open(path);
                audioStream.read(buffer, 0, headerOffset);
                audioStream.read(buffer, 0, bufferSize);
                audioStream.read(buffer, 0, bufferSize);
                audioStream.read(buffer, 0, bufferSize);
                while (bytesWritten < /*fileSize - headerOffset - 4096*8*/ 73774 - 4096*6) {
                    Log.i("Size", "" + fileSize);
                    bytesRead = audioStream.read(buffer, 0, bufferSize);
                    bytesWritten += audioTrack.write(buffer, 0, bytesRead);
                }

                audioTrack.stop();
                audioTrack.release();

            } catch (Exception e) {

            } finally {
                if (audioTrack != null) {
                    audioTrack.release();
                }
            }
        }
    }
}
