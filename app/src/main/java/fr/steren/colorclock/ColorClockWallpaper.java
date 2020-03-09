/*
 * Copyright (C) 2009 The Android Open Source Project
 * Copyright (C) 2010 Steren Giannini steren.giannini@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.steren.colorclock;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.text.format.Time;
import android.view.SurfaceHolder;

public class ColorClockWallpaper extends WallpaperService {

    public static final String SHARED_PREFS_NAME = "cube2settings";

    public static final float SATURATION = 0.65f;
    public static final float VALUE = 0.8f;
    
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new ColorClockEngine();
    }

    class ColorClockEngine extends Engine 
        implements SharedPreferences.OnSharedPreferenceChangeListener {

        private final Handler mHandler = new Handler();

        private Time mTime;
        
        private final Runnable mDrawBackground = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private SharedPreferences mPrefs;

        ColorClockEngine() {
            mPrefs = ColorClockWallpaper.this.getSharedPreferences(SHARED_PREFS_NAME, 0);
            mPrefs.registerOnSharedPreferenceChangeListener(this);
            onSharedPreferenceChanged(mPrefs, null);
        }

        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

            //String shape = prefs.getString("cube2_shape", "cube");

        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
            
            mTime = new Time();
            mTime.setToNow();
        }
        

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawBackground);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                drawFrame();
                mTime = new Time();
                mTime.setToNow();
            } else {
                mHandler.removeCallbacks(mDrawBackground);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            drawFrame();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mHandler.removeCallbacks(mDrawBackground);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
            drawFrame();
        }


        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();
            final Rect frame = holder.getSurfaceFrame();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                	drawColorbackground(c);
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            mHandler.removeCallbacks(mDrawBackground);
            if (isVisible()) {
                mHandler.postDelayed(mDrawBackground, 1000 / 25);
            }
        }

        void drawColorbackground(Canvas c) {
        	c.save();

            final long millis = System.currentTimeMillis();
            mTime.set(millis);
            mTime.normalize(false);

        	float[] color = new float[3];
        	color[0] = ((mTime.minute * 60.0f + mTime.second) % 3600) * 360.0f / 3600.0f;
        	color[1] = SATURATION;
        	color[2] = VALUE;
        	
        	c.drawColor(Color.HSVToColor(color));
        	c.restore();
        }

    }
}
