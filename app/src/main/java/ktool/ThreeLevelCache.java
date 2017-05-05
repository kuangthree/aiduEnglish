package ktool;

import android.support.annotation.Nullable;

/**
 * Created by Kuangthree on 2017/4/24.
 * 简单的三级缓存实现
 */

abstract public class ThreeLevelCache<K,V> {

    public interface onAccessCompleteListener<V>{
        void onAccessComplete(boolean isSuccess,V object);
    }

    private class AccessThread extends Thread{

        onAccessCompleteListener<V> mListener;
        K mKey;
        public AccessThread(@Nullable onAccessCompleteListener<V> listener, K key){
            super();
            mListener = listener;
            mKey = key;
        }
        @Override
        public void run(){
            V object = getObjectFromWebServer(mKey);
            if(object != null){
                restoreObjectToRAM(mKey,object);
                restoreObjectToDisk(mKey,object);
                if(mListener!=null){
                    mListener.onAccessComplete(true,object);
                }
            }
            if(mListener!=null){
                mListener.onAccessComplete(false,null);
            }
        }
    }

    protected V getObjectWithListener(K key,@Nullable onAccessCompleteListener<V> listener){
        V object = this.getObjectFromRAM(key);
        if(object != null) return object;
        object = this.getObjectFromDisk(key);
        if(object != null){
            restoreObjectToRAM(key,object);
            return object;
        }
        new AccessThread(listener,key).start();
        return null;
    }

    protected V getObject(K key){
        V object = this.getObjectFromRAM(key);
        if(object != null) return object;
        object = this.getObjectFromDisk(key);
        if(object != null){
            restoreObjectToRAM(key,object);
            return object;
        }
        object = this.getObjectFromWebServer(key);
        if(object != null){
            restoreObjectToRAM(key,object);
            restoreObjectToDisk(key,object);
            return object;
        }
        return null;
    }

    abstract protected V getObjectFromRAM(K key);

    abstract protected V getObjectFromDisk(K key);

    abstract protected V getObjectFromWebServer(K key);

    abstract protected void restoreObjectToDisk(K key,V value);

    abstract protected void restoreObjectToRAM(K key,V value);

    abstract public void cleanDiskCache();

    abstract public long getDiskCacheSize();

}
