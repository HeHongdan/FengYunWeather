package me.wsj.fengyun.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.ParameterizedType;

public abstract class BaseVmActivity<T extends ViewBinding, V extends ViewModel> extends BaseActivity<T> {

    protected V viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(getViewModelClass());
        super.init();
    }

    public Class<V> getViewModelClass() {
        Class<V> xClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return xClass;
    }

}
