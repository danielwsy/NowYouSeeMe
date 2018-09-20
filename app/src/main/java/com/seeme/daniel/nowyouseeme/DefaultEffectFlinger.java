package com.seeme.daniel.nowyouseeme;

import android.content.Context;

import com.aiyaapp.aavt.core.Renderer;
import com.aiyaapp.aavt.gl.BaseFilter;
import com.aiyaapp.aavt.gl.LazyFilter;
import com.aiyaapp.aiya.SluggardSvEffectTool;
import com.aiyaapp.aiya.filter.AyTrackFilter;
import com.aiyaapp.aiya.filter.SvBlackMagicFilter;
import com.aiyaapp.aiya.filter.SvBlackWhiteTwinkleFilter;
import com.aiyaapp.aiya.filter.SvCutSceneFilter;
import com.aiyaapp.aiya.filter.SvDysphoriaFilter;
import com.aiyaapp.aiya.filter.SvFinalZeligFilter;
import com.aiyaapp.aiya.filter.SvFluorescenceFilter;
import com.aiyaapp.aiya.filter.SvFourScreenFilter;
import com.aiyaapp.aiya.filter.SvHallucinationFilter;
import com.aiyaapp.aiya.filter.SvRollUpFilter;
import com.aiyaapp.aiya.filter.SvSeventysFilter;
import com.aiyaapp.aiya.filter.SvShakeFilter;
import com.aiyaapp.aiya.filter.SvSpiritFreedFilter;
import com.aiyaapp.aiya.filter.SvSplitScreenFilter;
import com.aiyaapp.aiya.filter.SvThreeScreenFilter;
import com.aiyaapp.aiya.filter.SvTimeTunnelFilter;
import com.aiyaapp.aiya.filter.SvVirtualMirrorFilter;
import com.aiyaapp.aiya.render.AiyaGiftFilter;

import java.util.LinkedList;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/20 15:39
 */
public class DefaultEffectFlinger implements EffectListener.EffectFlinger, Renderer {

    private AiyaGiftFilter mEffectFilter;
    private AyTrackFilter mTrackFilter;
    private BaseFilter mShowFilter;
    private Class<? extends BaseFilter> mNowSvClazz;
    private SluggardSvEffectTool mSvTool = SluggardSvEffectTool.getInstance();
    private Context context;
    private LinkedList<Runnable> mTask = new LinkedList<>();
    private int mWidth, mHeight;

    private Class[] filter_clazzs = new Class[]{
            LazyFilter.class, SvSpiritFreedFilter.class, SvShakeFilter.class,
            SvBlackMagicFilter.class, SvVirtualMirrorFilter.class, SvFluorescenceFilter.class,
            SvTimeTunnelFilter.class, SvDysphoriaFilter.class, SvFinalZeligFilter.class,
            SvSplitScreenFilter.class, SvHallucinationFilter.class, SvSeventysFilter.class,
            SvRollUpFilter.class, SvFourScreenFilter.class, SvThreeScreenFilter.class,
            SvBlackWhiteTwinkleFilter.class, SvCutSceneFilter.class
    };

    public DefaultEffectFlinger(Context context) {
        this.context = context;
        mShowFilter = new LazyFilter();
        mEffectFilter = new AiyaGiftFilter(context, null);
        mTrackFilter = new AyTrackFilter(context);
    }


    @Override
    public void onEffectChanged(final int key, final String path) {
        if (key == 0) {
            mEffectFilter.setEffect(null);
        } else {
            mEffectFilter.setEffect(path);
        }

    }

    @Override
    public void create() {
        mTrackFilter.create();
        mEffectFilter.create();
        mShowFilter.create();
    }

    @Override
    public void sizeChanged(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        mEffectFilter.sizeChanged(width, height);
        mTrackFilter.sizeChanged(width, height);
        mShowFilter.sizeChanged(width, height);

    }

    @Override
    public void draw(int texture) {
        while (!mTask.isEmpty()) {
            mTask.removeFirst().run();
        }

        mEffectFilter.setFaceDataID(mTrackFilter.getFaceDataID());
        texture = mEffectFilter.drawToTexture(texture);

        //短视频特效处理
        if (mNowSvClazz != null) {
            texture = mSvTool.processTexture(texture, mWidth, mHeight, mNowSvClazz);
        }
        mShowFilter.draw(texture);
    }

    @Override
    public void destroy() {
        mEffectFilter.destroy();
        mTrackFilter.destroy();
        mShowFilter.destroy();
    }

    public void release() {
        if (mEffectFilter != null) {
            mEffectFilter.release();
        }
        if (mTrackFilter != null) {
            mTrackFilter.release();
        }
    }
}
