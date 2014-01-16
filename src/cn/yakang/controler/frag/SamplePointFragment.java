package cn.yakang.controler.frag;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.yakang.controler.MainActivity;
import cn.yakang.controler.R;
import cn.yakang.controler.adapter.HeadGridViewAdapter;
import cn.yakang.controler.adapter.SubsListViewAdapter;
import cn.yakang.controler.adapter.CategoryListViewAdapter;
import cn.yakang.controler.entity.CommanUrl;
import cn.yakang.controler.queryTask.IQueryTask;
import cn.yakang.controler.queryTask.QueryTask;

public class SamplePointFragment extends H2Fragment{
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		setQueryUrl(CommanUrl.SAMPLE_POINT);
	}
}
