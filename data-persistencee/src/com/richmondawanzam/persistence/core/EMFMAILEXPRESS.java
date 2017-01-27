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


public class EMFMAILEXPRESS implements ServletContextListener {
	
	private static Logger logger = Logger.getLogger(EMFMAILEXPRESS.class.getSimpleName());
	
	private static String persistenceUnitNameMaileExpress = "";
	private static String pathToPropertyFileMaileExpress = "";
	private static String propertyFileMaileExpress = "";
	private static EntityManagerFactory emfMailExpress;
	
	
	@Override
	public void contextInitialized(ServletContextEvent context) {
		logger.debug("Entering contextInitialized()");
		
		
		ServletContext c = context.getServletContext();
		
		// BAM SPECIFIC IMPL.
		if (c != null) {
			logger.debug("ServletContext is not null");
			
			// MAILEXPRESS
			if (c.getInitParameter("persistence_path_mailexpress") != null) {       
				pathToPropertyFileMaileExpress = c.getInitParameter("persistence_path");
				
				logger.debug("pathToPropertyFileMaileExpress = " + pathToPropertyFileMaileExpress);
			}
			if (c.getInitParameter("persistence_file_mailexpress") != null) {       
				propertyFileMaileExpress = c.getInitParameter("persistence_file_mailexpress");
				
				logger.debug("propertyFileMaileExpress = " + propertyFileMaileExpress);
			}
			if (c.getInitParameter("persistence_unit_mailexpress") != null) {       
				persistenceUnitNameMaileExpress = c.getInitParameter("persistence_unit_mailexpress");
				
				logger.debug("persistenceUnitNameMaileExpress = " + persistenceUnitNameMaileExpress);
			}
		}
		
		
		try {
			long begin = System.currentTimeMillis();
			
			// MailExpress
			if(emfMailExpress == null){
				emfMailExpress = Persistence.createEntityManagerFactory(persistenceUnitNameMaileExpress, getPersistenceProperties(pathToPropertyFileMaileExpress, propertyFileMaileExpress, true));
				logger.debug("EntityManagerFactory MAILEXPRESS ( singleton ) created for " + (System.currentTimeMillis() - begin) + "ms");
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
		emfMailExpress.close();
	}
	
	public static EntityManager createEntityManagerMailExpress() {
		if(emfMailExpress== null) {
			throw new IllegalStateException("Context is not initialized");
		}
		
		return emfMailExpress.createEntityManager();
	}
	
	public static void reloadPersistence() {
		logger.info("Reloading EFT Persistence Unit....");
		emfMailExpress = Persistence.createEntityManagerFactory(persistenceUnitNameMaileExpress, getPersistenceProperties(pathToPropertyFileMaileExpress, propertyFileMaileExpress, true));
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
