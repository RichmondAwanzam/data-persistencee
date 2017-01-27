package com.richmondawanzam.persistence.paginator;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.richmondawanzam.persistence.core.EMF;

public abstract class DataPaginator implements IPaginator{
	private Logger logger = Logger.getLogger(DataPaginator.class.getName());
	
	
	private EntityManager entityManager;
	
	
	
	public DataPaginator() {
		numbersToGet = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("resultsPerPageSessionValue");
		if(numbersToGet == null) {
			numbersToGet = 25;
		}
	}



	protected Paginator paginator;
	
	/**
	 * First results offset
	 */
	protected int offset = 0;
	
	/**
	 * Number of records in the database
	 */
	protected int totalRecords;
	
	/**
	 * Maximum records to get per query
	 */
	protected Integer numbersToGet;
	
	
	public Paginator getPaginator() {
		return paginator;
	}


	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
	
	

	public int getOffset() {
		return offset;
	}


	public void setOffset(int offset) {
		this.offset = offset;
	}


	public int getTotalRecords() {
		return totalRecords;
	}


	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}


	public int getNumbersToGet() {
		return numbersToGet;
	}


	public void setNumbersToGet(int numbersToGet) {
		this.numbersToGet = numbersToGet;
	}


	public EntityManager getEntityManager() {
		if(entityManager == null) {
			logger.debug("EntityManager is null, creating a new object");
			entityManager = EMF.createEntityManager();
			logger.debug("New EntityManager object " + entityManager + " created");
		}
		return entityManager;
	}


	@Override
	public void resultsPerPageListener(ValueChangeEvent ce) {
		
		Integer result = new Integer(0);
		String resultString = ce.getNewValue().toString();
		setResultsPerPageSessionValue();
		if (resultString != null) {
			result = new Integer(resultString);
			paginator.setRecords(result);
			paginator.setFirstRecord(0);
			getPaginatedDataList(paginator.getFirstRecord(), paginator.getRecords());
			paginator.updatePages();
			paginator.first();
			logger.debug(this.getClass().getName() + "\n" + "resultsPerPage : " + result);
		}
		
		logger.debug(this.getClass().getName() + "\n" + "Exiting resultsPerPageListener(ValueChangeEvent ce)");
		
	}


	@Override
	public void firstPage() {
		paginator.first();
		getPaginatedDataList(paginator.getFirstRecord(), paginator.getRecords());
		
	}


	@Override
	public void nextPage() {
		paginator.next();
		getPaginatedDataList(paginator.getFirstRecord(), paginator.getRecords());
		
	}


	@Override
	public void prevPage() {
		paginator.prev();
		getPaginatedDataList(paginator.getFirstRecord(), paginator.getRecords());
		
	}


	@Override
	public void lastPage() {
		paginator.last();
		getPaginatedDataList(paginator.getFirstRecord(), paginator.getRecords());
		
	}
	
	protected abstract void getPaginatedDataList(int offset, int maxResults);
	
	
	
	public void setResultsPerPageSessionValue() {
		if (numbersToGet == null || numbersToGet == 0)
			numbersToGet = 25;
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("resultsPerPageSessionValue", numbersToGet);
	}
	
	protected void setTotalRecordAndInitializePaginator(String jpaQuery, Object... params ) {
		
		TypedQuery<Long> query = getEntityManager().createQuery(jpaQuery, Long.class);
		try {
			int i = 1;
			for(Object param : params) {
				query.setParameter(i, param);
				i++;
			}
			Long result = query.getSingleResult();
			totalRecords =result.intValue();
			logger.debug("Total number of Records for payment: " + totalRecords);
		} catch (NoResultException e) {
			logger.debug("There are no records for Payments");
		} finally {
			getEntityManager().close();
		}
		
		setPaginator(new Paginator(offset, numbersToGet, totalRecords));
	}

}
