package com.richmondawanzam.persistence.paginator;

import javax.faces.event.ValueChangeEvent;

public interface IPaginator {
	
	public void resultsPerPageListener(ValueChangeEvent ce);
	
	public void firstPage();

	
	public void nextPage();
	
	public void prevPage();
	
	public void lastPage();

}
