# ![logo](src/main/webapp/img/favicon.png) Gilda

Gilda rappresenta l'infrastruttura base per la realizzazione di intranet. Il progetto è supportato da [IT Distribuzione](http://www.itdistribuzione.com/)

- http://localhost:8082/
- http://localhost:8082/swagger-ui/index.html#/

### Avvio
```shell
mvn spring-boot:run
```

### Note
```shell
# Con lo schema di default 
mvn sloth:refreshdb -DgenPackage=it.itdistribuzione.gilda.gen

# Con uno schema specifico
mvn sloth:refreshdb -DdbSchema=<dbSchema.xml>
```

### Configurazione maven

Per poter utilizzare gli artefatti del framework è necessaria la seguente configurazione maven (file .m2/settings.xml).

Nota: sostituire ###USERNAME### ###TOKEN### con le proprie credenziali git hub

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

 <activeProfiles>
  <activeProfile>github</activeProfile>
 </activeProfiles>

 <profiles>
  <profile>
   <id>github</id>
   <repositories>
    <repository>
     <id>central</id>
     <url>https://repo1.maven.org/maven2</url>
     <releases>
      <enabled>true</enabled>
     </releases>
     <snapshots>
      <enabled>true</enabled>
     </snapshots>
    </repository>
    <repository>
     <id>github-sloth-framework</id>
     <name>GitHub enricogrillini Apache Maven Packages</name>
     <url>https://maven.pkg.github.com/enricogrillini/sloth-framework</url>
    </repository>
   </repositories>
   <pluginRepositories>
    <pluginRepository>
     <id>github-sloth-plugin</id>
     <name>GitHub enricogrillini Apache Maven Packages</name>
     <url>https://maven.pkg.github.com/enricogrillini/sloth-plugin</url>
    </pluginRepository>
   </pluginRepositories>
  </profile>
 </profiles>

 <servers>
  <server>
   <id>github-sloth-framework</id>
   <username>###USERNAME###</username>
   <password>###TOKEN###</password>
  </server>
  <server>
   <id>github-sloth-plugin</id>
   <username>###USERNAME###</username>
   <password>###TOKEN###</password>
  </server>
 </servers>
</settings>
```