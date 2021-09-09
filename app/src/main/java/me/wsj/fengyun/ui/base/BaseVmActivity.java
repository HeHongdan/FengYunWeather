package me.wsj.fengyun.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public abstract class BaseVmActivity<T extends ViewBinding, V extends ViewModel> extends BaseActivity<T> {

    /** 自动维护数据(Activity 重建不影响数据的值)。 */
    protected V viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(getViewModelClass());
//        autoBindView();
        super.init();
    }

    private void autoBindView() {
        Class<T> vClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            Method inflate = vClass.getMethod("inflate", LayoutInflater.class);
            mBinding = (T) inflate.invoke(null,getLayoutInflater());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 ViewModel 的类型。
     *
     * @return 类型。
     */
    public Class<V> getViewModelClass() {
        //getActualTypeArguments：从一个泛型类型中获取第2个泛型参数的类型类。
        Class<V> xClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return xClass;
    }

    @Override
    public T bindView() {

        return null;
    }
}
