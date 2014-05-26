package org.landa.musicoll.core;

import java.io.File;

import org.landa.musicoll.model.Resource;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class EbeanServerProvider implements Provider<EbeanServer> {

	private final ServerConfig config;

	private EbeanServer ebeanServer;

	@Inject
	public EbeanServerProvider(@Named("basePath") final File basePath) {

		config = new ServerConfig();
		config.setName("musicoll");

		// Define DataSource parameters
		DataSourceConfig h2Db = new DataSourceConfig();
		h2Db.setDriver("org.h2.Driver");
		h2Db.setUsername("sa");
		h2Db.setPassword("");
		h2Db.setUrl("jdbc:h2:" + basePath.getAbsolutePath() + File.separator
				+ "musicolldb");
		h2Db.setHeartbeatSql("SELECT 1;");

		config.setDataSourceConfig(h2Db);

		// set DDL options...
		config.setDdlGenerate(false);
		config.setDdlRun(false);

		config.setDefaultServer(true);
		config.setRegister(false);

		config.addClass(Resource.class);

	}

	@Override
	public EbeanServer get() {
		if (null == ebeanServer) {
			ebeanServer = EbeanServerFactory.create(config);
		}
		return ebeanServer;
	}

}
