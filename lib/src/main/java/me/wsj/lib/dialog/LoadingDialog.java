package me.wsj.lib.dialog;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import me.wsj.lib.R;
import me.wsj.lib.view.LoadingDrawable;

/**
 * 自定义加载进度对话框
 */

public class LoadingDialog extends BaseDialog {
    private ImageView ivLoading;
    private TextView tvLoadingTip;
    LoadingDrawable loadingDrawable;

    public LoadingDialog(@NonNull Context context) {
        super(context, 0.4f, 0);
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_loading;
    }

    @Override
    public void initView() {
        ivLoading = findViewById(R.id.ivLoading);
        tvLoadingTip = findViewById(R.id.tvLoadingTip);
        loadingDrawable = new LoadingDrawable(getContext().getResources().getDrawable(R.drawable.ic_loading_sun),
                getContext().getResources().getDrawable(R.drawable.ic_loading_cloud));
        ivLoading.setImageDrawable(loadingDrawable);
    }

    @Override
    public void initListener() {

    }

    public void setTip(String tip) {
        if (tip != null && !tip.isEmpty() && tvLoadingTip != null) {
            tvLoadingTip.setText(tip);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void show() {
        super.show();
        if (loadingDrawable != null) {
            ((Animatable) loadingDrawable).start();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (loadingDrawable != null) {
            ((Animatable) loadingDrawable).stop();
        }
    }
}
