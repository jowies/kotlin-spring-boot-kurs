# Spring Boot med Kotlin

Vi skal lage et enkelt api med Spring Boot der vi bruker reactive webclient og kotlin coroutines. Vi skal bruke https://pokeapi.co/ for å hente data.


## Ting dere trenger på forhånd

* Java 11 eller nyere
* Maven 3.5 eller nyere

## Initialiser prosjektet

Gå inn på <https://start.spring.io/>. Spring Initializr er en løsning for kjapt genere et spring boot prosjekt. Det kan tenkes på litt som en create-react-app.

Velg følgende konfigurasjon:
![initializr](./images/initializr.png)

1. Project: **Maven**
2. Language: **Kotlin**
3. Spring Boot: **2.7.1**
4. Project metadata: **Vær kreativ**
5. Packaging: **Jar**
6. Java: **17**
7. Dependencies:
**Spring Reactive Web**,
**Spring Boot DevTools**,

Last ned og extract mappen

## Første kjøring

Nå skal vi inn i terminalen. Gå til folderen som ble pakket ut. Trykk `ls` for å se innholdet. Det burde se noenlunde sånn her ut

```zsh
src/
HELP.md
mvnw
mvnw.cmd
pom.xml
```

1. **src** - Her ligger all koden var
2. **HELP.MD** - Liste over dokumentasjon og noen guider
3. **mvnw** - Kan brukes til a kjore maven pa unix-systemer. Ikke nodvendig hvis man har det installert på pcen, men kan vare greit med tanke på versjon
4. **mvnw.cmd** - Samme som over men for windows
5. **pom.xml** - Brukes til a konfigurere applikasjonen, definere dependencies etc.

Hvis du er på windows skriv:
```./mvnw.cmd spring-boot:run```
På unix, skriv:
```./mvnw spring-boot:run```

Forhåpentligvis kræsjer det ikke...Du kan gå til `localhost:8080`. Står det /error er du good

Jeg ville ha denne oppe hvis dere ikke er så kjent med Kotlin. Her står det meste: <https://kotlinlang.org/docs/getting-started.html>

Noen flere dependencier trengs for å jobbe med coroutines i kotlin. Legg til følgende i pom.xml:

```
<dependency>
  <groupId>org.jetbrains.kotlinx</groupId>
  <artifactId>kotlinx-coroutines-core</artifactId>
  <version>1.6.2</version>
</dependency>
<dependency>
  <groupId>org.jetbrains.kotlinx</groupId>
  <artifactId>kotlinx-coroutines-core-jvm</artifactId>
  <version>1.6.2</version>
</dependency>
```

## Første endepunkt

Åpne prosjektet i favoritteditoren din. Naviger til `src/main/.../SpringKursApplication.kt`
Dette er startpunktet for applikasjonen deres. Dette burde ikke se så fremmed ut hvis du er kjent med kotlin. Det er en ganske enkel klasse, med en main funksjon. Det som er interessant er egentlig ```@SpringBootApplication``` som indikerer hvor Spring skal starte applikasjonen.

Nå skal vi lage et enkelt GET-endepunkt. Lag en ny mappe ved siden av `src/main/.../SpringKursApplication.kt` som dere kaller `controller`  Lag en ny fil som heter `PokemonController.kt`. I den skriver dere følgende:

```kotlin
@RestController
@RequestMapping("v1")
class PokemonController() {
    @GetMapping("pokemon/{name}")
    suspend fun getSinglePokemon(@PathVariable name: String) = name
}
```

Kjør applikasjonen igjen og gå inn på <http://localhost:8080/v1/pokemon/pikachu>. Og du burde se `Pikachu`.

**Så hva skjer her?**

* `@RestController`: Denne definerer at klassen skal lastes inn av Spring til å fungere som controller. Egentlig en shorthand for `@Controller` og `@ResponseBody`, Her får vi mye gratis. Blant annet blir det automatisk parset til og fra JSON.

* `@RequestMapping`: Definerer hvor i URL-pathen og hva slags request som skal gjelde. I dette tilfelle vil requests mot `/v1` gå mot denne controlleren.

* `@GetMapping`: Shorthand for `@RequestMapping(method=GET)`, definerer hvilken HTTP-metode som skal matches

### La oss strukturere appen

Legg til følgende mapper:

```
- client
- config
- dto
- properties
- service
```

Ett av prinsippene Spring Boot er bygd etter er "Seperation of concerns". Vi splitter opp applikasjonen vår i seperate ansvarsområder. De forskjellige mappene definerer disse områdene:

* `client`: Klasser for å snakke med andre api'er, heter naturlig client fra client/server, der spring boot applicasjonen i dette tilfelle er clienten
* `config`: Klasser for å konfigurasjon
* `dto`: Står for "Data Transfer Objects". Det kan være lurt å definere egne modeller for dataen man skal sende tilbake fra requests. Det er riktignok en diskusjon om dette er "gammeldags", og fører til mye boilerplate
* `properties`: Brukes for å hente properties fra f.eks. environmentet eller application.yaml
* `service`: Service er der businesslogikken ligger, her henter man data, endrer på den osv.

### Properties
Siden vi skal hente data fra et eksternt api kan det være fint å definere properties et sted, vi har en egen properties mappe her. Det er ikk ealltid nødvendig, man kan gjerne putte propertiene direkte i filen der de skal brukes eller en hjelpeklasse ved siden av.

Lag følgende klasse i en ny fil `PokemonClientProperties.kt` i properties-mappen:


```kotlin
@ConfigurationProperties(prefix = "pokemon.client")
@Component
class PokemonClientProperties {
    lateinit var url: String
}
```
**Så hva skjer her?**

* `@ConfigurationProperties`: Brukes for å hente properties fra application.properties eller application.yml i formen av en klasse

* `@Component`: Defineres så spring plukker den opp og injecter propertiene

I application.properties kan du putte følgende pokemon.client.url=https://pokeapi.co/api/v2

### Client
Neste vi skal se på er clienten. Dette blir en klasse som abstraherer bort api-kallene mot `pokeapi.co`

I client trenger vi domeneobjektene som pokeapi brukes. Vi lager noen simplifiserte data-klasser for dette

Lag filene: 
`...client/domain/PokemonResponse.kt`
`...client/domain/EggGroupResponse.kt`

I de respektive file legg til følgende dataklasse

```kotlin
data class Ability(
    val name: String
)

data class AbilityInfo(
    val ability: Ability
)

data class PokemonResponse(
    val id: String,
    val name: String,
    @JsonProperty("base_experience")
    val baseExperience: Int,
    val height: Int,
    val is_default: Boolean,
    val weight: Int,
    val abilities: List<AbilityInfo>
)
```

```kotlin
data class PokemonSpecies(
    val name: String,
    val url: String
)


data class EggGroupResponse(
    val id: Int,
    val name: String,
    @JsonProperty("pokemon_species")
    val pokemonSpecies: List<PokemonSpecies>
)
```

Med data-klassene på plass kan vi lage clienten

Lag følgende klasse i en ny fil `..client/PokemonClient.kt` i client-mappen:


```kotlin
class PokemonClient(
    private val webClient: WebClient
) {
    suspend fun getSinglePokemon(name: String) =
        webClient
            .get()
            .uri("/pokemon/$name")
            .retrieve()
            .awaitBody<PokemonResponse>()

    suspend fun getEggGroup(name: String) =
        webClient
            .get()
            .uri("/egg-group/$name")
            .retrieve()
            .awaitBody<EggGroupResponse>()
}
```
**Så hva skjer her?**

* `suspend`: Lar oss bruke kotling coroutines. Dette er asynkrone funksjoner, som kan suspendes, altså vente på å kjøre videre. Tenk async/await fra javascript

* `awaitBody`: Sier at vi skal vente på responsen og definerer forventet responsebody

### Config
Som du kan se tar `PokemonClient` inn en webClient, den vil vi gjerne definere hvordan skal se ut. Det gjør vi i en config:

```kotling
@Configuration
class PokemonClientConfig(
    private val pokemonClientProperties: PokemonClientProperties
) {
    @Bean
    fun pokemonClient(): PokemonClient {
        // Dette er en hack siden pokeapi sender så insane mye data per pokemon
        val size = 16 * 1024 * 1024
        val strategies = ExchangeStrategies.builder()
            .codecs { codecs: ClientCodecConfigurer ->
                codecs.defaultCodecs().maxInMemorySize(size)
            }
            .build()
        
        val webClient = WebClient
            .builder()
            .exchangeStrategies(strategies)
            .baseUrl(pokemonClientProperties.url)
            .build()
        return PokemonClient(
            webClient
        )
    }
}
```

**Så hva skjer her?**

* `@Configuration`: Indikerer at klassen deklarerer en eller flere "beans" og kan prosesseres av Spring IoC container for å genere "bean"-definisjoner

* `@Bean`: En bønne er et object som er opprettet, bygget, og håndtert av Spring IoC Containeren. Den ligger alltid på metodenivå til forskjell fra f.eks. `@Component`

* Hvis vi ser på hva som skjer her har vi en metode som returnerer en PokemonClient, vi tar også og oppretter en webclient som vi sender inn som parameter i instansieringingen. Denne instansen av pokemonClient vil nå bli brukt alle steder der vi definerer at vi vil ha en PokemonClient, gitt at den har `@Compenent` eller tilsvarende. 




### DTO

Vi lager en fil for å definere DTO'en vår, det er bare en og det er pokemon `../dto/PokemonDTO.kt`

```
data class PokemonDTO(
    val id: String,
    val name: String,
    val baseExperience: Int,
    val height: Int,
    val weight: Int,
    val numberOfAbilities: Int
)
```

Vi lager også en mapper for å mappe fra domene-objektene fra `pokeapi` til vår DTO

`..dto/mapper/PokemonDTOMapper.kt`

```kotlin
fun toPokemonDTO(pokemonResponse: PokemonResponse): PokemonDTO {
    return PokemonDTO(
        id = pokemonResponse.id,
        name = pokemonResponse.name,
        baseExperience = pokemonResponse.baseExperience,
        height = pokemonResponse.height,
        weight = pokemonResponse.weight,
        numberOfAbilities = pokemonResponse.abilities.count()
    )
}
```

Her har vi noe annet som er ganske kult med kotling, og det er at vi kan definere funksjonere på "top-level", ingen grunn til å definere en kall for en enkel input/output funksjon


### Service
Med DTO på plass gjenstår bare å knytte alt sammen. Det gjør vi med å lage en service-klasse.

`../service/PokemonService.kt`

```kotlin
@Service
class PokemonService(private val pokemonClient: PokemonClient) {
    suspend fun getSinglePokemon(name: String) =
        toPokemonDTO(pokemonClient.getSinglePokemon(name))

    suspend fun getAllPokemonInEggGroup(eggGroupName: String) = coroutineScope {
        val eggGroup = pokemonClient.getEggGroup(eggGroupName)

        eggGroup.pokemonSpecies.map {
            async {
                pokemonClient.getSinglePokemon(it.name);
            }
        }.awaitAll().map { toPokemonDTO(it) }
    }
}
```

**Så hva skjer her?**

* `@Service`: Egentlig det samme som @Component, vi bruker det bare for å definere service-laget

* `coroutineScope`: Brukes lager en et CoroutineScope. Brukes når man skal gjøre paralelle asynkrone kall. Les mer her: https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/coroutine-scope.html

* `async`: Sier at dette er en asynkront kall og returnerer et type Future (Promise hvis du kommer fra js-verden)

* `awaitAll`: Gjør akkurat det man tenker at det skal gjøre: venter på at alle futurene er fullført før resultatet blir returnert


### Tilbake til controlleren

Da er vi straks i mål. Det siste vi mangler er å knytte services til controlleren vår:

```kotlin
@RestController
@RequestMapping("v1")
class PokemonController(private val pokemonService: PokemonService) {
    @GetMapping("pokemon/{name}")
    suspend fun getSinglePokemon(@PathVariable name: String) =
        pokemonService.getSinglePokemon(name)

    @GetMapping("egg-group/{name}")
    suspend fun getAllPokemonInEggGroup(@PathVariable name: String) =
        pokemonService.getAllPokemonInEggGroup(name)
}
```

Og der er vil i mål!

Test det ut med å gjøre et get kall til følgende endepunkt:

`http://localhost:8080/v1/pokemon/mewtwo`
og 
`http://localhost:8080/v1/egg-group/monster`
