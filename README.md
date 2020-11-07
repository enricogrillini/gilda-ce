

# ![logo](src/main/webapp/img/favicon.png) Gilda

Gilda rappresenta l'infrastruttura base per la realizzazione di intranet. Il progetto è supportato da [IT Distribuzione](http://www.itdistribuzione.com/)

- http://localhost:8080/gilda/swagger-ui/

```shell
# Con lo schema di default 
mvn sloth:refreshdb -DgenPackage=it.itdistribuzione.gilda.gen

# Con uno schema specifico
mvn sloth:refreshdb -DdbSchema=<dbSchema.xml>
```