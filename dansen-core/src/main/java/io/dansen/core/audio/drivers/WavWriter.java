package io.dansen.core.audio.drivers;

import java.io.*;

/**
 * Minimal 16-bit stereo WAV writer.
 */
public class WavWriter implements Closeable {
    private final RandomAccessFile raf;
    private long dataBytes = 0;


    public WavWriter(File file, int sampleRate) throws IOException {
        raf = new RandomAccessFile(file, "rw");
        raf.setLength(0);
        // Write header placeholders
        raf.writeBytes("RIFF");
        raf.writeInt(0); // size later
        raf.writeBytes("WAVEfmt ");
        raf.writeInt(Integer.reverseBytes(16));
        raf.writeShort(Short.reverseBytes((short) 1)); // PCM
        raf.writeShort(Short.reverseBytes((short) 2)); // channels
        raf.writeInt(Integer.reverseBytes(sampleRate));

        int byteRate = sampleRate * 2 * 2;
        raf.writeInt(Integer.reverseBytes(byteRate));
        raf.writeShort(Short.reverseBytes((short) (2 * 2))); // block align
        raf.writeShort(Short.reverseBytes((short) 16)); // bits
        raf.writeBytes("data");
        raf.writeInt(0); // data size later
    }


    public void writeBlock(float[] left, float[] right, int frames) throws IOException {
        byte[] buf = new byte[frames * 4];
        int bi = 0;
        for (int i = 0; i < frames; i++) {
            int l = (int) (Math.max(-1f, Math.min(1f, left[i])) * 32767);
            int r = (int) (Math.max(-1f, Math.min(1f, right[i])) * 32767);
            buf[bi++] = (byte) (l & 0xFF);
            buf[bi++] = (byte) ((l >> 8) & 0xFF);
            buf[bi++] = (byte) (r & 0xFF);
            buf[bi++] = (byte) ((r >> 8) & 0xFF);
        }
        raf.write(buf);
        dataBytes += buf.length;
    }


    @Override
    public void close() throws IOException {
        raf.seek(4);
        raf.writeInt(Integer.reverseBytes((int) (36 + dataBytes)));
        raf.seek(40);
        raf.writeInt(Integer.reverseBytes((int) dataBytes));
        raf.close();
    }
}
