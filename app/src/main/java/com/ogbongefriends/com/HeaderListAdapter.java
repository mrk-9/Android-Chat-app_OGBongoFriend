package com.ogbongefriends.com;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


 
/**
 * @author vivek
 */
// ====================================================================
public abstract class HeaderListAdapter extends BaseAdapter {

	protected ArrayList<Object> items = new ArrayList<Object>();
	private int HeaderView = 1, ItemView = 2, CustView = 3;

	// =============================================
	@Override
	public int getCount() {
		return items.size();
	}

	// =============================================
	public ArrayList<Object> getAll() {
		return items;
	}

	// =============================================
	public void clear() {
		items.clear();
	}

	// =============================================
	@Override
	public int getViewTypeCount() {
		
		return 4;/* With 2 it was crashing, don't know why */
	}

	// =============================================
	@Override
	public Object getItem(int arg0) {

		Object o = items.get(arg0);
		if (o instanceof Header)
			return ((Header) o).o;
		else if (o instanceof Item)
			return ((Item) o).o;
		else
			return ((Cust) o).o;
	}

	// =============================================
	@Override
	public int getItemViewType(int pos) {
		if (items.get(pos) instanceof Header)
			return HeaderView;
		else if (items.get(pos) instanceof Item)
			return ItemView;
		else 
			return CustView;
	}

	// =============================================
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	// =============================================
	@Override
	public View getView(int pos, View arg1, ViewGroup arg2) {

		Object item = items.get(pos);
		if (item instanceof Header)
			return getHeaderView(pos, arg1, arg2, ((Header) item).o);
		else if(item instanceof Item)
			return getItemView(pos, arg1, arg2, ((Item) item).o);
		else
			return getCustView(pos, arg1, arg2, ((Cust) item).o);
	}

	// =============================================
	public Cust addcustum(Object o) {
		Cust c = new Cust(o);
		items.add(c);
		return c;
	}

	// =============================================
	public Header addHeader(Object o) {
		Header h = new Header(o);
		items.add(h);
		return h;
	}

	// =============================================
	public Item addItem(Object o) {
		Item i = new Item(o);
		items.add(i);
		return i;
	}

	// =============================================
	public void addAtpos(int pos, Object o) {
		items.add(pos, o);
	}

	// =============================================
	public abstract View getCustView(int pos, View v, ViewGroup vg, Object o);

	// =============================================
	public abstract View getHeaderView(int pos, View v, ViewGroup vg, Object o);

	// =============================================
	public abstract View getItemView(int pos, View v, ViewGroup vg, Object o);

	// ====================================================================
	public class Header {

		private Object o;

		// =============================================
		public Header(Object o) {
			this.o = o;
		}
	}

	// ====================================================================
	public class Item {

		private Object o;

		// =============================================
		public Item(Object o) {
			this.o = o;
		}
	}

	// ====================================================================
	public class Cust {

		private Object o;

		// =============================================
		public Cust(Object o) {
			this.o = o;
		}
	}
}
