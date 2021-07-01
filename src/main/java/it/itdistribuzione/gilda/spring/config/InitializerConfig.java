package it.itdistribuzione.gilda.spring.config;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;

import it.eg.sloth.db.manager.DataConnectionManager;
import it.eg.sloth.framework.common.exception.FrameworkException;
import it.eg.sloth.framework.configuration.ConfigSingleton;
import it.eg.sloth.framework.monitor.MonitorSingleton;
import lombok.extern.slf4j.Slf4j;

/**
 * Project: gilda-ce
 * Copyright (C) 2019-2020 Enrico Grillini
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Enrico Grillini
 */
@Slf4j
@Configuration
@PropertySource(value = "classpath:application.properties", name = "application.properties")
public class InitializerConfig {

  @Autowired
  AbstractEnvironment env;

  @Value("${datasource.default.driver-class-name}")
  private String driverClassName;

  @Value("${datasource.default.url}")
  private String url;

  @Value("${datasource.default.username}")
  private String username;

  @Value("${datasource.default.password}")
  private String password;

  @Bean
  public DataSource getDataSource() {
    return DataSourceBuilder.create()
        .driverClassName(driverClassName)
        .url(url)
        .username(username)
        .password(password)
        .build();
  }

  @PostConstruct
  public void init() throws SQLException, IOException, FrameworkException, SchedulerException, ClassNotFoundException {

    org.springframework.core.env.PropertySource<?> propertySource = ((AbstractEnvironment) env).getPropertySources().get("application.properties");

    Properties props = (Properties) propertySource.getSource();
    for (Object key : props.keySet()) {
      ConfigSingleton.getInstance().addProperty(key.toString(), env.getProperty(key.toString()));
    }

    // Inizializzazione del data source
    log.info("Init: DataSource");
    DataConnectionManager.getInstance().registerDataSource(getDataSource());

    // Monitor
    log.info("Init: Monitor");
    MonitorSingleton.getInstance().start();

  }
}
