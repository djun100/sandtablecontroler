package cn.yakang.controler.frag;

import cn.yakang.controler.entity.CommanUrl;
import android.app.Activity;


public class SearchInfoFragment extends H2Fragment{
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		setQueryUrl(CommanUrl.SERCHINFO_URL);
	}
}
