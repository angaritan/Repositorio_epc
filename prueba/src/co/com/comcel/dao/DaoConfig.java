package co.com.comcel.dao;
import java.io.Reader;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.DaoManagerBuilder;

public class DaoConfig {
	private static final Logger log = Logger.getLogger("alarm");
	public DaoConfig(){

	}

	private static final DaoManager daoManager;

	/**
	 * Retorna una referencia al DaoManager
	 */

	static {
		try {
			String resource = "co/com/comcel/sqlmap/dao.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			daoManager = DaoManagerBuilder.buildDaoManager(reader);

		} catch (Exception e) {
			log.error("Error obteniendo el DAO",e);
			throw new RuntimeException("Description.  Cause: " + e, e);
		}
	}

	/**
	 * Obtiene la referencia de un objeto DaoManager, que permite la administracion de
	 * los DAO registrador en ibatis.
	 */
	public static DaoManager getDaoManager(){
		return daoManager;
	}

}