package com.seeme.daniel.nowyouseeme;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/20 15:51
 */
public class EffectController implements EffectListener.EffectFlinger, EffectListener.OnEffectCheckListener {


    private Activity act;
    private View container;
    private EffectListener.EffectFlinger mFlinger;
    private RecyclerView mEffectList;
    EffectAdapter mEffAdapter;

    public EffectController(final Activity act, View container, EffectListener.EffectFlinger flinger) {
        this.act = act;
        this.container = container;
        this.mFlinger = flinger;

        mEffectList = act.findViewById(R.id.mEffectList);
        mEffectList.setLayoutManager(new GridLayoutManager(act.getApplicationContext(), 5));
        mEffAdapter = new EffectAdapter(act);
        mEffectList.setAdapter(mEffAdapter);
        mEffAdapter.setEffectChangedListener(this);
        mEffAdapter.setEffectCheckListener(this);
    }

    public void show() {
        container.setVisibility(View.VISIBLE);
    }

    public void hide() {
        container.setVisibility(View.GONE);
    }

    @Override
    public void onEffectChanged(int key, String path) {
        mFlinger.onEffectChanged(key, path);
    }

    @Override
    public boolean onEffectChecked(int pos, String path) {
        if (pos == -1) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            act.startActivityForResult(Intent.createChooser(intent, "请选择一个json文件"), 101);
            return true;
        }
        return false;
    }
}
