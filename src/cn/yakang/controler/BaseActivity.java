package cn.yakang.controler;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import cn.yakang.controler.frag.SettingFragment;
import cn.yakang.controler.widget.slidingMenu.SlidingMenu;
import cn.yakang.controler.widget.slidingMenu.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

	protected SettingFragment mFrag;
	private boolean isTable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isTable = getResources().getBoolean(R.bool.isTable);
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new SettingFragment();
			t.replace(R.id.menu, mFrag);
			t.commit();
		} else {
			mFrag = (SettingFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu);
		}

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(isTable ? getResources().getDisplayMetrics().widthPixels/3 
				: (int)getResources().getDimension(R.dimen.slidingmenu_offset));
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

}
