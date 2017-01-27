package com.richmondawanzam.persistence.paginator;


public class Paginator {
	private int firstRecord;
	private int records;
	private int recordsTotal;
	private int pageIndex;
	private int numberOfPages;
	
	// Constructor
	public Paginator() {
		
	}
	
	// Constructor 2
	public Paginator(int offset, int numbersToGet, int totalRecords) {
		setFirstRecord(offset);
		setRecords(numbersToGet);
		setRecordsTotal(totalRecords);
		updatePages();
		setPageIndex(1);
	}

	public void next() {
		int recordsTemp = firstRecord + records;
		if (recordsTemp < recordsTotal) {
			firstRecord = recordsTemp;
		}else {
			records = (recordsTotal - firstRecord);
		}
		
		if(getPageIndex() < getNumberOfPages()) {
            this.pageIndex++;
        }else {
			setPageIndex(this.numberOfPages);
		}
	}
	
	public void prev() {
		int recordsTemp = firstRecord - records;
		if (recordsTemp > 0) {
			firstRecord = recordsTemp;
		}else {
			setFirstRecord(0);
		}
		
		if (getPageIndex() > getNumberOfPages()) {
			setPageIndex(this.numberOfPages);
		}else if(this.pageIndex > 1) {
            this.pageIndex--;
        }
	}
	
	public void first() {
		setFirstRecord(0);
		setPageIndex(1);
	}
	
	public void last() {
		setPageIndex(getNumberOfPages());
		setFirstRecord(recordsTotal - records);
	}
	
	public void updatePages() {
		if ( (recordsTotal % records) == 0) {
			setNumberOfPages(recordsTotal/records);
		} else {
			setNumberOfPages((recordsTotal/records) + 1);
		}
	}
	
	public int getFirstRecord() {
		return firstRecord;
	}

	public void setFirstRecord(int firstRecord) {
		this.firstRecord = firstRecord;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	
}
