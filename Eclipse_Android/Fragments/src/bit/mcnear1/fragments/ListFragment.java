package bit.mcnear1.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflator.inflate(R.layout.listview_fragment, container, false);
		String[] items = getResources().getStringArray(R.array.list_items);
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item, items);
		
		ListView itemList = (ListView)v.findViewById(R.id.fragmentListView);
		itemList.setAdapter(listAdapter);
		
		return v;
	}
	
}
