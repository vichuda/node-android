package com.iwebpp.node;

import android.util.Log;

import com.iwebpp.libuvpp.LibUV;
import com.iwebpp.libuvpp.cb.TimerCallback;
import com.iwebpp.libuvpp.handles.LoopHandle;
import com.iwebpp.libuvpp.handles.TimerHandle;

// Node.android libUV loop context
public final class NodeContext {
    private static final String TAG = "NodeContext";
	
	static {
		// call an idempotent LibUV method just to ensure that the native lib is loaded
		LibUV.cwd();
	}

	private LoopHandle loop;

	/**
	 * @return the loop
	 */
	public LoopHandle getLoop() {
		return loop;
	}

	public NodeContext() {
		this.loop = new LoopHandle();
	}

	/*
	 * DOM-style timers
	 */
	// setTimeout/clearTimeout
	public static interface TimeoutCallback{
		public void onTimeout() throws Exception;
	}
	// after: ms
    public TimerHandle setTimeout(final TimeoutCallback callback, int after) {
    	final TimerHandle timer = new TimerHandle(loop);
    	
    	timer.setCloseCallback(new TimerCallback() {
            @Override
            public void onTimer(final int i) throws Exception {
                Log.d(TAG, "setTimeout timer closed");
            }
        });

        timer.setTimerFiredCallback(new TimerCallback() {
            @Override
            public void onTimer(final int status) throws Exception {
                Log.d(TAG, "setTimeout timer fired");

                callback.onTimeout();
                
                timer.close();
            }
        });

        timer.start(after, 0);
    	
    	return timer;
    }
    public void clearTimeout(TimerHandle timer) {
        timer.close();
    }
    
    // setInterval/clearInterval
	public static interface IntervalCallback{
		public void onInterval() throws Exception;
	}
	// repeat: ms
    public TimerHandle setInterval(final IntervalCallback callback, int repeat) {
    	final TimerHandle timer = new TimerHandle(loop);
    	
    	timer.setCloseCallback(new TimerCallback() {
            @Override
            public void onTimer(final int i) throws Exception {
                Log.d(TAG, "setInterval timer closed");
            }
        });

        timer.setTimerFiredCallback(new TimerCallback() {
            @Override
            public void onTimer(final int status) throws Exception {
                Log.d(TAG, "setInterval timer fired");

                callback.onInterval();
            }
        });

        timer.start(repeat, repeat);
    	
    	return timer;
    }
    public void clearInterval(TimerHandle timer) {
        timer.close();
    }
    
    
}

