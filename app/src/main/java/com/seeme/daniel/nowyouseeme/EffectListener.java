package com.seeme.daniel.nowyouseeme;

import com.aiyaapp.aavt.gl.BaseFilter;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/20 15:41
 */
public class EffectListener {

    interface OnEffectChangedListener {
        /**
         * 特效被更改时调用
         *
         * @param key  index
         * @param path 特效资源路径
         */
        void onEffectChanged(int key, String path);
    }

    interface OnEffectCheckListener {
        boolean onEffectChecked(int pos, String path);
    }

    interface OnShortVideoEffectChangedListener{
        /**
         * 短视频特效被更改时的监听
         * @param key index
         * @param name 短视频特效名称
         * @param clazz 特效滤镜类
         */
        void onShortVideoEffectChanged(int key,String name,Class<? extends BaseFilter> clazz);

    }

    public interface EffectFlinger extends OnEffectChangedListener {

    }
}
