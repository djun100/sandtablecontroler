package cn.yakang.controler.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> list;
	public MainViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	public MainViewPagerAdapter(FragmentManager fm,List<Fragment> list){
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
		
	}
	@Override
	public int getCount() {
		return list.size();
	}

}
