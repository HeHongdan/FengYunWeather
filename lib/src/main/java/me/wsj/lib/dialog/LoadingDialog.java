package me.wsj.lib.dialog;

import android.content.Context;
import android.graphics.drawable.Animatable;

import androidx.annotation.NonNull;

import me.wsj.lib.R;
import me.wsj.lib.databinding.DialogLoadingBinding;
import me.wsj.lib.view.LoadingDrawable;

/**
 * 自定义加载进度对话框
 */

public class LoadingDialog extends BaseDialog<DialogLoadingBinding> {
    LoadingDrawable loadingDrawable;

    public LoadingDialog(@NonNull Context context) {
        super(context, 0.38f, 0);
    }


    @Override
    public DialogLoadingBinding bindView() {
        return DialogLoadingBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        loadingDrawable = new LoadingDrawable(getContext().getResources().getDrawable(R.drawable.ic_loading_sun),
                getContext().getResources().getDrawable(R.drawable.ic_loading_cloud));
        mBinding.ivLoading.setImageDrawable(loadingDrawable);
    }

    @Override
    public void initListener() {

    }

    public void setTip(String tip) {
        if (tip != null && !tip.isEmpty() && mBinding.tvLoadingTip != null) {
            mBinding.tvLoadingTip.setText(tip);
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
