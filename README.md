
## Development

1. Start SQL Server with docker:
   * `docker run --name brukergrupper-db -e "ACCEPT_EULA=y" -e "SA_PASSWORD=yourStrong(!)Password" -p 1433:1433 -d mcr.microsoft.com/mssql/server:2019-latest`
   * (To restart, simply run `docker start brukergrupper-db`)
2. Copy `brukergrupper.properties.template` to `brukergrupper.properties` and fill in the missing properties 
3. Start `PortalServer`


## Building and deploying 

1. Build the whole project from this directory with `mvn package`
   * This builds the React code and runs frontend tests
   * This packages the whole application together in `target/brukergrupper-server-*.jar`


## Nøkler som må brukes til lokal innlogging og testing
For å kunne bruke Azure AD (SSO) trengs enkelte nøkler og koblinger til callback-urler.
Det viktigste per lokal maskin er forklart nedenfor.

De fleste stegene som må tas for å bruke AD lokalt finnes på denne docen:
* https://security.labs.nais.io/pages/utvikling/lokalt.html#testklienter

Urlen ovenfor henviser til en vault-lenke som ikke kan nås dersom naisdevice ikke kjører.

Start naisdevice for å komme til lenken nedenfor.
* https://vault.adeo.no/ui/vault/secrets/secret/show/.common/idporten/ver2

Nøklene som en finner her skal lages som systemvariabler på lokal maskin