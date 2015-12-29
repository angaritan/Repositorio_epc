package co.com.amdocs.dao;


import java.io.Reader;
import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.DaoManagerBuilder;

public class DaoConfig {
	private static final Logger log = Logger.getLogger("alarm");
	private static final DaoManager daoManager;
	public DaoConfig(){	}
	
	
	static {
		try {
			String resource = "co/com/amdocs/sqlmap/dao.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			daoManager = DaoManagerBuilder.buildDaoManager(reader);

		} catch (Exception e) {
			log.error("Error obteniendo el DAO",e);
			throw new RuntimeException("Description.  Cause: " + e, e);
		}
	}
	
	public static DaoManager getDaoManager(){
		return daoManager;
	}
	

}
