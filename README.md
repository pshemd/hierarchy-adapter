# Hierarchy Adapter
[![Build Status](http://udp-portal-dev.lukoil.com/jenkins/buildStatus/icon?job=hierarchy-adapter%2Fdev&subject=Hierarchy%20Adapter%20(dev))](http://udp-portal-dev.lukoil.com/jenkins/job/hierarchy-adapter/job/dev/)
[![Build Status](http://udp-portal-dev.lukoil.com/jenkins/buildStatus/icon?job=hierarchy-adapter%2Fdmaster&subject=Hierarchy%20Adapter%20(master))](http://udp-portal-dev.lukoil.com/jenkins/job/hierarchy-adapter/job/master/)
[<img src="https://wiki.jenkins.io/download/attachments/2916393/logo-title.png"  width="120">](http://udp-portal-dev.lukoil.com/jenkins/job/hierarchy-adapter)

[![Maintainability Rating](http://udp-portal-dev.lukoil.com/sonarqube/api/project_badges/measure?project=hierarchy-adapter&metric=sqale_rating)](http://udp-portal-dev.lukoil.com/sonarqube/dashboard?id=hierarchy-adapter)
[![Bugs](http://udp-portal-dev.lukoil.com/sonarqube/api/project_badges/measure?project=hierarchy-adapter&metric=bugs)](http://udp-portal-dev.lukoil.com/sonarqube/dashboard?id=hierarchy-adapter)
[![Code Smells](http://udp-portal-dev.lukoil.com/sonarqube/api/project_badges/measure?project=hierarchy-adapter&metric=code_smells)](http://udp-portal-dev.lukoil.com/sonarqube/dashboard?id=hierarchy-adapter)
[![Lines of Code](http://udp-portal-dev.lukoil.com/sonarqube/api/project_badges/measure?project=hierarchy-adapter&metric=ncloc)](http://udp-portal-dev.lukoil.com/sonarqube/dashboard?id=hierarchy-adapter)
[![Security Rating](http://udp-portal-dev.lukoil.com/sonarqube/api/project_badges/measure?project=hierarchy-adapter&metric=security_rating)](http://udp-portal-dev.lukoil.com/sonarqube/dashboard?id=hierarchy-adapter)
[![Technical Debt](http://udp-portal-dev.lukoil.com/sonarqube/api/project_badges/measure?project=hierarchy-adapter&metric=sqale_index)](http://udp-portal-dev.lukoil.com/sonarqube/dashboard?id=hierarchy-adapter)
[![Vulnerabilities](http://udp-portal-dev.lukoil.com/sonarqube/api/project_badges/measure?project=hierarchy-adapter&metric=vulnerabilities)](http://udp-portal-dev.lukoil.com/sonarqube/dashboard?id=hierarchy-adapter)


[![UI Tests Dasboard](https://img.shields.io/badge/selenoid-UI%20Tests%20Dasboard-informational)](http://104.46.49.208:8080/#/)
[![UI Tests Dasboard](https://img.shields.io/badge/selenoid-stats-inactive)](http://udp-portal-dev.lukoil.com/selenoid/status)

[![Sonatype Nexus](https://img.shields.io/badge/nexus-dev-green)](http://udp-portal-dev.lukoil.com/nexus/#browse/browse:docker-hosted:v2%2Fecp-lukoil%2Fhierarchy-adapter%2Ftags%2Fdev)

---
### Pull image
 
```bash
docker pull http://udp-portal-dev.lukoil.com/nexus/ecp-lukoil/hierarchy-adapter:dev
ее
```

---
### Variables:
 
#### Swager autorize
* `AUTH_SERVER_URL` - http://udp-portal-dev.lukoil.com/auth
* `REALM` - master
* `CLIENT` - portal

#### Publish
* `LOCAL_PORT` - 8096

#### OpenID
* `OAUTH2-CLIENT-ID` - om-service
* `OAUTH2-CLIENT-SECRET` - aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee
* `OAUTH2-CLIENT-TOKEN-URI` - http://udp-portal-dev.lukoil.com/auth/realms/master/protocol/openid-connect/token
* `JWKS_URL` - http://udp-portal-dev.lukoil.com/auth/realms/master/protocol/openid-connect/certs
