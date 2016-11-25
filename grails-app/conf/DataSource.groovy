dataSource {
  pooled = true
  jmxExport = true
  driverClassName = "org.h2.Driver"
  username = "sa"
  password = ""
}
hibernate {
  cache.use_second_level_cache = true
  cache.use_query_cache = false
//    cache.region.factory_class = 'org.hibernate.cache.SingletonEhCacheRegionFactory' // Hibernate 3
  cache.region.factory_class = 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory' // Hibernate 4
  singleSession = true // configure OSIV singleSession mode
  flush.mode = 'manual' // OSIV session flush mode outside of transactional context
}

// environment specific settings
environments {
  development {
    dataSource {
      logSql = false
      username = "root"
      password = "admin"
      dbCreate = "update"
      driverClassName = "com.mysql.jdbc.Driver"
      url = "jdbc:mysql://localhost/genrocket_bank"
      dialect = org.hibernate.dialect.MySQL5InnoDBDialect
      properties {
        maxActive = 50
        maxIdle = 25
        minIdle = 5
        initialSize = 5
        minEvictableIdleTimeMillis = 60000
        timeBetweenEvictionRunsMillis = 60000
        maxWait = 10000
        validationQuery = "/* ping */"
      }
    }
  }
  test {
    dataSource {
      dbCreate = "update"
      url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
    }
  }
  production {
    dataSource {
      logSql = false
      username = "root"
      password = "adminadmin"
      dbCreate = "update"
      driverClassName = "com.mysql.jdbc.Driver"
      url = "jdbc:mysql://genrocketbank1-cluster-1.cluster-cfdg3cgk3ajh.us-west-2.rds.amazonaws.com:3306/genrocket_bank"
      dialect = org.hibernate.dialect.MySQL5InnoDBDialect
      properties {
        maxActive = 50
        maxIdle = 25
        minIdle = 5
        initialSize = 5
        minEvictableIdleTimeMillis = 60000
        timeBetweenEvictionRunsMillis = 60000
        maxWait = 10000
        validationQuery = "/* ping */"
      }
    }
  }
}
