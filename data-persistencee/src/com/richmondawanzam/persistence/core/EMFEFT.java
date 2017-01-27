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


public class EMFEFT implements ServletContextListener {
	
	private static Logger logger = Logger.getLogger(EMFEFT.class.getSimpleName());
	
	private static String persistenceUnitNameEFT = "";
	private static String pathToPropertyFileEFT = "";
	private static String propertyFileEFT = "";
	private static EntityManagerFactory emfEft;
	
	
	@Override
	public void contextInitialized(ServletContextEvent context) {
		logger.debug("Entering contextInitialized()");
		
		
		ServletContext c = context.getServletContext();
		
		// BAM SPECIFIC IMPL.
		if (c != null) {
			logger.debug("ServletContext is not null");
			
			// EFT
			if (c.getInitParameter("persistence_path_eft") != null) {       
				pathToPropertyFileEFT = c.getInitParameter("persistence_path");
				
				logger.debug("pathToPropertyFileEFT = " + pathToPropertyFileEFT);
			}
			if (c.getInitParameter("persistence_file_eft") != null) {       
				propertyFileEFT = c.getInitParameter("persistence_file_eft");
				
				logger.debug("propertyFileEFT = " + propertyFileEFT);
			}
			if (c.getInitParameter("persistence_unit_eft") != null) {       
				persistenceUnitNameEFT = c.getInitParameter("persistence_unit_eft");
				
				logger.debug("persistenceUnitNameEFT = " + persistenceUnitNameEFT);
			}
			
		}
		
		
		try {
			long begin = System.currentTimeMillis();
			
			// EFT
			if(emfEft == null){
				emfEft = Persistence.createEntityManagerFactory(persistenceUnitNameEFT, getPersistenceProperties(pathToPropertyFileEFT, propertyFileEFT, true));
				logger.debug("EntityManagerFactory EFT ( singleton ) created for " + (System.currentTimeMillis() - begin) + "ms");
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
		emfEft.close();
	}
	
	public static EntityManager createEntityManagerEft() {
		if(emfEft== null) {
			throw new IllegalStateException("Context is not initialized");
		}
		
		return emfEft.createEntityManager();
	}
	
	public static void reloadPersistence() {
		logger.info("Reloading EFT Persistence Unit....");
		emfEft = Persistence.createEntityManagerFactory(persistenceUnitNameEFT, getPersistenceProperties(pathToPropertyFileEFT, propertyFileEFT, true));
		logger.info("Reloaded EFT Persistence Unit");
	}
	
	
	/**
	 * 
	 * @return persistence properties
	 */
	private static final Properties getPersistenceProperties(String pathToProperties, String propertyFile ,Boolean loadViaResource){
		

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
