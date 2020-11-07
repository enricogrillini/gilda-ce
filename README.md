

# ![logo](src/main/webapp/img/favicon.png) Gilda

Gilda rappresenta l'infrastruttura base per la realizzazione di intranet. Il progetto è supportato da [IT Distribuzione](http://www.itdistribuzione.com/)

- http://localhost:8080/gilda/swagger-ui/

```shell
# Con lo schema di default 
mvn sloth:refreshdb -DgenPackage=it.itdistribuzione.gilda.gen

# Con uno schema specifico
mvn sloth:refreshdb -DdbSchema=<dbSchema.xml>
```


# Oracle JDBC Driver
Oracle non ha rilasciato pubblicamente i driver JDBC su repo Maven. Per utilizzare i driver Oracle è necessario scaricarli dal [sito](https://www.oracle.com/database/technologies/jdbcdriver-ucp-downloads.html)

## Per Oracle 11g

```shell
mvn install:install-file "-Dfile=ojdbc6.jar" "-DgroupId=com.oracle" "-DartifactId=ojdbc6" "-Dversion=11.2.0.4" "-Dpackaging=jar"
```

Nel pom aggiungere la dipendenza:

```xml
<dependency>
     <groupId>com.oracle</groupId>
     <artifactId>ojdbc6</artifactId>
     <version>11.2.0.4</version>
</dependency>
```