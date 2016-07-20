package com.example.shelockapp.model;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by shini_000 on 7/19/2016.
 */
public class SpaceItem extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItem(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.bottom = space;
        outRect.top = space;
        outRect.left = space;
        outRect.right = space;
    }
}
