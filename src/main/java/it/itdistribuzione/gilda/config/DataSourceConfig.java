package it.itdistribuzione.gilda.config;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.eg.sloth.db.manager.DataConnectionManager;
import lombok.extern.slf4j.Slf4j;

/**
 * Project: gilda-ce
 * Copyright (C) 2019-2020 Enrico Grillini
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Enrico Grillini
 */
@Slf4j
@Configuration
public class DataSourceConfig {
  
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
        .password(username)
        .build();
  }

  @PostConstruct
  public void init() throws SQLException, IOException {
    // Inizializzazione del data source
    log.info("Init: DataSource");
    DataConnectionManager.getInstance().registerDataSource(getDataSource());
  }
}
