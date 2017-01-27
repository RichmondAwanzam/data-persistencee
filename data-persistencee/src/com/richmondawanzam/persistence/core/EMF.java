package com.richmondawanzam.persistence.core;


import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.accolm.security.encryptiondecryption.EncryptDecrypt;


public class EMF implements ServletContextListener {
	
	private static Logger logger = Logger.getLogger(EMF.class.getSimpleName());
	
	// BACKUP
	private String persistenceUnitName = "";
	private String pathToPropertyFile = "";
	private String propertyFile = "";
	private static EntityManagerFactory emf;
	
	
	@Override
	public void contextInitialized(ServletContextEvent context) {
		logger.debug("Entering contextInitialized()");
		
		
		ServletContext c = context.getServletContext();
		
		if (c != null) {
			logger.debug("ServletContext is not null");
			
			if (c.getInitParameter("persistence_path") != null) {       
				pathToPropertyFile = c.getInitParameter("persistence_path");
				
				logger.debug("pathToPropertyFile = " + pathToPropertyFile);
			}
			if (c.getInitParameter("persistence_file") != null) {       
				propertyFile = c.getInitParameter("persistence_file");
				
				logger.debug("propertyFile = " + propertyFile);
			}
			if (c.getInitParameter("persistence_unit") != null) {       
				this.persistenceUnitName= c.getInitParameter("persistence_unit");
				
				logger.debug("persistenceUnitName = " + persistenceUnitName);
			}
		}
		try {
			long begin = System.currentTimeMillis();
			if(emf == null){
				emf = Persistence.createEntityManagerFactory(this.persistenceUnitName, this.getPersistenceProperties(pathToPropertyFile, propertyFile, true));
				emf.getCache().evictAll();
				logger.debug("EntityManagerFactory ( singleton ) created for " + (System.currentTimeMillis() - begin) + "ms");
			}
		} catch (RuntimeException e) {
			logger.debug("RuntimeException occurred : " + e);
			throw e;
		} catch (Error e){
			logger.debug("Error occurred : " + e);
			throw e;
		}

	}
	
	@Override
	public void contextDestroyed(ServletContextEvent context) {
		emf.close();
	}
	
	
	public static EntityManager createEntityManager() {
		if(emf== null) {
			throw new IllegalStateException("Context is not initialized");
		}
		
		return emf.createEntityManager();
	}
	
	/**
	 * 
	 * @return persistence properties
	 */
	private final Properties getPersistenceProperties(String pathToProperties, String propertyFile ,Boolean loadViaResource){
		

	       EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
			
			Properties persistenceProperties = null; 
			
			try {
				
				if (loadViaResource) {
					persistenceProperties = encryptDecrypt.readPropertyFileWithEncryptedValuesViaResource(propertyFile);
				} else {
					persistenceProperties = encryptDecrypt.readPropertyFileWithEncryptedValuesViaSpecificPath(pathToProperties, propertyFile);
				}
				
				if (!(persistenceProperties.getProperty("javax.persistence.jdbc.password").equals(""))) {
					
					//Decryption of the property is done behind the scenes as soon as 'getProperty' is called
					persistenceProperties.setProperty("javax.persistence.jdbc.password", persistenceProperties.getProperty("javax.persistence.jdbc.password"));
				}
				
			} catch (IOException e) {
				logger.debug("IOException when loading persistence properties : " + e);
			} catch (Exception e) {
				logger.debug("Exception when loading persistence properties : " + e);
			}
			return persistenceProperties;
		
	}
}
