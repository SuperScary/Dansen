package io.dansen.core.audio;

public final class BufferPool {

    private final float[] l;
    private final float[] r;

    public BufferPool(int frames) {
        l = new float[frames];
        r = new float[frames];
    }

   public float[] L() {
        return l;
   }

   public float[] R() {
       return r;
   }

}
