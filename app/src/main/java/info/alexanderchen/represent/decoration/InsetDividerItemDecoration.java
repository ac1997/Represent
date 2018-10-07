package info.alexanderchen.represent.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import info.alexanderchen.represent.R;

public class InsetDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private static int left;
    private int RID;

    public InsetDividerItemDecoration(Context context, int RID) {
        this.mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        this.left = 0;
        this.RID = RID;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int[] location = new int[2];
            if(child.findViewById(RID) != null) {
                child.findViewById(RID).getLocationOnScreen(location);
                left = location[0];
            }
            int right = parent.getWidth() - parent.getPaddingRight();
            int top = child.getBottom() + params.bottomMargin + (int) child.getTranslationY();
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}