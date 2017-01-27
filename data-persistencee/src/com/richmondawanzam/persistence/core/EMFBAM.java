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


public class EMFBAM implements ServletContextListener {
	
	private static Logger logger = Logger.getLogger(EMFBAM.class.getSimpleName());
	
	private static String persistenceUnitNameBAM = "";
	private static String pathToPropertyFileBAM = "";
	private static String propertyFileBAM = "";
	private static EntityManagerFactory emfBam;
	
	@Override
	public void contextInitialized(ServletContextEvent context) {
		logger.debug("Entering contextInitialized()");
		
		
		ServletContext c = context.getServletContext();
		
		// BAM SPECIFIC IMPL.
		if (c != null) {
			logger.debug("ServletContext is not null");
			
			// BAM
			if (c.getInitParameter("persistence_path_bam") != null) {       
				pathToPropertyFileBAM = c.getInitParameter("persistence_path");
				
				logger.debug("pathToPropertyFileBAM = " + pathToPropertyFileBAM);
			}
			if (c.getInitParameter("persistence_file_bam") != null) {       
				propertyFileBAM = c.getInitParameter("persistence_file_bam");
				
				logger.debug("propertyFileBAM = " + propertyFileBAM);
			}
			if (c.getInitParameter("persistence_unit_bam") != null) {       
				persistenceUnitNameBAM = c.getInitParameter("persistence_unit_bam");
				
				logger.debug("persistenceUnitNameBAM = " + persistenceUnitNameBAM);
			}
			
		}
		
		
		try {
			long begin = System.currentTimeMillis();
			
			// BAM
			if(emfBam == null){
				emfBam = Persistence.createEntityManagerFactory(persistenceUnitNameBAM, getPersistenceProperties(pathToPropertyFileBAM, propertyFileBAM, true));
				logger.debug("EntityManagerFactory BAM ( singleton ) created for " + (System.currentTimeMillis() - begin) + "ms");
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
		emfBam.close();
	}
	
	public static EntityManager createEntityManagerBam() {
		if(emfBam== null) {
			throw new IllegalStateException("Context is not initialized");
		}
		
		return emfBam.createEntityManager();
	}
	
	public static void reloadPersistence() {
		logger.info("Reloading EFT Persistence Unit....");
		emfBam = Persistence.createEntityManagerFactory(persistenceUnitNameBAM, getPersistenceProperties(pathToPropertyFileBAM, propertyFileBAM, true));
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
