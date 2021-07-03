package me.wsj.fengyun.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mList;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        super(fm);
        this.mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }
}
