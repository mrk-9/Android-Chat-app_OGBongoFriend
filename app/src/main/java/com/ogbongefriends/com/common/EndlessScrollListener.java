package com.ogbongefriends.com.common;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class EndlessScrollListener implements OnScrollListener {

    private int visibleThreshold = 2;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;

    public EndlessScrollListener() {
    }
    
    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
    	
    	//Log.d("ash_scroll","On Scroll Loading Status == "+loading+" Total Items == "+totalItemCount+" Previous Total == "+previousTotal);
    	
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        
        //Log.d("ash_scroll","First Visibile Item Count === "+firstVisibleItem+" Visible Item Count == "+visibleItemCount);
        
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
        	
        	Log.d("ash_scroll","Loading Page === "+currentPage);
        	
        	new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					loadPage(currentPage);
				}
			}).start();
        	
        	loading = true;
        }
        
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
    
    public abstract void loadPage(int page);
    
}
