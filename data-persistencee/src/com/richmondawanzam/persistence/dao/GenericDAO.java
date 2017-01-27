package com.richmondawanzam.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Generic DAO providing basic insertor update and delete operation
 * @author Paul Baffoe
 *
 * @param <T> the entity type 
 * @param <ID> The primary key type
 */
public interface GenericDAO<T, ID extends Serializable> { 
 
      
    /** 
     * save an entity. This can be either a INSERT or UPDATE in the database.
     * 
     * @param entity the entity to save
     * 
     * @return the saved entity
     */
    void save(T entity);

    /**
     * delete an entity from the database.
     * 
     * @param entity the entity to delete
     */
    void delete(final T entity);
    void voidByNamedQuery(final String queryName, Object... params);
    
    
    /**
     * 
     * save an entity
     * 
     * @param entity the entity update
     */
    
    T update(final T entity);
    
    
    int updateByNamedQuery(final String queryName, Object... params);
    

	/**
	 * Get a sum using a named query with sum calculation
	 * 
	 * @param queryName the name of the query
	 * @return the sum
	 */	
	Long sumByNamedQuery(final String queryName);
	
	/**
	 * Get a list of sums using a named query with sum calculations
	 * 
	 * @param queryName the name of the query
	 * @return the sums
	 */	
    List<Long> sumsByNamedQuery(String queryName);
    
	/**
	 * Find the first entity using a named query
	 * 
	 * @param queryName the name of the query
	 * @return the entity
	 */
	T findSingle(final String queryName);
	
	/**
	 * Find the first entity using a named query
	 * 
	 * @param queryName the name of the query
	 * @param params the query parameters
	 * @return the entity
	 */
	T findSingle(final String queryName, Object... params);
	
	 /** 
     * Find an entity by its primary key  
     *
     * @param id the primary key
     * @return the entity 
     */
    T findById(final ID id);

    /**
     * Load all entities.
     *
     * @return the list of entities
     */
    List<T> findAll();
    List<T> findAll(final String queryName);
    List<T> findAll(int maxResults);
   
    /**
     * Find using a named query.
     *
     * @param queryName the name of the query
     * @param params the query parameters
     *
     * @return the list of entities
     */
    List<T> findByNamedQuery(final String queryName,   Object... params);

    /**
     * Find using a named query.
     * 
     * @param maxResults maximum number of results
     * @param queryName the name of the query
     * @return the list of entities
     */
	List<T> findByNamedQueryWithMaximum(int maxResults, final String queryName);
	
    /**
     * Find using a named query.
     * 
     * @param maxResults maximum number of results
     * @param queryName the name of the query
     * @param params the params
     * @return the list of entities
     */
	List<T> findByNamedQueryAndParamsWithMaximum(int maxResults, final String queryName, Object... params);
    
    /**
     * Find using a named query.
     *
     * @param queryName the name of the query
     * @param params the query parameters
     *
     * @return the list of entities
     */
    List<T> findByNamedQueryAndNamedParams(
        final String queryName,
        final Map<String, ?extends Object> params
    );

    /**
     * Count all entities.
     *
     * @return the number of entities
     */
    Long countAll();
    
	Long countAll(String queryName, Object... params);
    /**
     * Count all entities with a filter over description and title.
     *
     * @return the number of entities
     */
    Long countAllFiltered(final String queryName, Object... params);

    
	List<T> findAllFiltered(final String queryName, final Object... params);
    /**
     * Returns all entities with firstresult and maxresult for paging
     * 
     * @return
     */
	List<T> findAllFilteredWithPaging(
		int maxResults, 
		int firstResult, 
		final String queryName, 
		final Object... params
	);
	
	List<T> findAllWithPaging(
		int maxResults,
		int firstResult,
		final String queryName
	);
	
	boolean isManaged();

	Long countAllByNamedQuery(String queryName);

	/**
     * Find using a named query.
     * 
     * @param maxResults maximum number of results
     * @param firstResult first result
     * @param queryName the name of the query
     * @param params the params
     * @return the list of entities
     */
	List<T> findByNamedQueryAndParamsWithMaximumAndMinimum(int maxResults, int firstResult, String queryName, Object... params);

	Long sumByNamedQueryWithParams(String queryName, Object... params);

	Integer countAllInteger(String queryName, Object... params);

	List<T> findAllFilteredWithPagingNative(String queryName, Object[] params);

	List<Long> countSlaDowntime(String queryName, Object... params);
	
	List<T> findByNativeQuery(String queryName, Object... params);
	
	public BigDecimal sum(String nativeQuery, Object... params); 
	
	public List<T> findByNamedQueryWithMaximumAndMinimum(int firstResult, int maxResults, String queryName);
}
