# PGR301Exam2021
PGR301 Eksamen for kandidat 2021

## Oppgave 1a

##### For at workflowen laget til denne oppgaven skal fungere hos andre, må man lage AWS access keys i IAM og legge dem til som secrets i sitt eget repo. Da under navnene:
##### - AWS_ACCESS_KEY_ID og AWS_SECRET_ACCESS_KEY
##### Det kan også være en god ide å endre navn på bøtten i denne delen av flowen til dette eksemplet eller egen bucket:
- name: Deploy SAM Application
- if: github.ref == 'refs/heads/main' && github.event_name == 'push'
- run: sam deploy --no-confirm-changeset --no-fail-on-empty-changeset --stack-name kand2021 --capabilities CAPABILITY_IAM --region eu-west-1 --resolve-s3
##### --- --s3-bucket kand2021imagebucket 🍎 +++ --resolve-s3 🍏

[![SAM Build and Deploy if Main](https://github.com/SorensenMartin/PGR301Exam2021/actions/workflows/2021_sam.yml/badge.svg)](https://github.com/SorensenMartin/PGR301Exam2021/actions/workflows/2021_sam.yml)

###### Endringer gjort for å tilpasse koden til Kjell: 
- Endret hardkoding i app.py til å lese BUCKET_NAME
- Oppdatert template.yaml sin target bucket, samt lagt til noen policies for tilganger som trengtes for dette
- Verifisert at det funker for meg i mitt Cloud9-miljø
- Lagt til en workflow som bygger og deployer SAM-applikasjonen ved push på filer i /kjell-katalogen. Om pushet ikke er på main, vil kun bygging skje og ikke deployment

#### Her ser vi at lambda-funksjonen er oppe og går med statuskode 200! 👍
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/81cd8e8b-1bfd-47b2-8dbe-c18a97fa83d0)
#### Her ser vi svaret etter at jeg la til et bilde fra en byggeplass (veldig relevant :) ) i min S3 Image bucket.
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/a39d204e-be4c-4ec2-a273-1c5b2ac45f83)

## Oppgave 1b

##### Det var for så vidt ganske rett frem å lage Dockerfilen, men hadde noen problemer da det kom til å vise resultatene. Fant ut at dette var på grunn av typen filer i kjellsimagebucket. Dette ble også adressert på Canvas, men ser ut som at filene har klart å lure seg inn igjen på ett eller annet vis 😖

##### Fikk først dette svaret da jeg prøvde å kjøre appen fra containeren.
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/a0a463be-9ac2-4c86-9668-6bbf8f129180)

##### La til en sjekk for filendelsen som sjekket for filutvidelser før den ga respons via rekognition. Filtypene jeg nå har "godkjent" er jpg og jpeg, men flere bildetyper kan legges til enkelt ved å legge til flere or-statements i app.py-koden. Etter dette var gjort fikk jeg dette svaret når jeg kjørte denne koden i terminalen: 
docker build -t kjellpy .
docker run -e AWS_ACCESS_KEY_ID=XXX -e AWS_SECRET_ACCESS_KEY=YYY -e BUCKET_NAME=kjellsimagebucket kjellpy
#### Result: 
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/111fccc0-431d-4371-8dde-f02ab98f6642)

## Oppgave 2a 

##### Fant ikke Maven i mitt Cloud9-miljø til å starte med, så måtte installere manuelt fra nettsiden til Apache Maven. 
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/58d1de31-4833-4f53-98c4-c3e2d74d4efd)
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/43d07369-5db1-4851-9654-3b0320291a2d)

##### Etter dette hadde jeg fortsatt trøbbel med at det lå feil filformat i bucket, derfor la jeg til en sjekk for filendelsen i RekognitionController.java som sørger for at bare filer med jpg eller jpeg blir hentet ut! (Kan alltids legge til flere godkjente filtyper her også om ønskelig) Etter dette var gjort fikk jeg ønskelig resultat av curl kommandoen. Her ser vi også at koden er betydelig enklere og lese en for et menneske en python koden.

![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/467924d9-41e4-4cec-9637-a535f9045512)

##### Så testet jeg at dockerfilen fungerte (etter å ha rotet med litt plassmangel som måtte ryddes i docker) og kjørte dockerfilen. 
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/715ff4a4-c0b5-4452-94c6-67453e026663)
##### Fikk også her det samme resultatet av curl kommandoen: 
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/234774fb-d6bd-4eef-9787-a22a98bf9b28)

# Oppgave 2b

##### ECR opprettet og workflow laget. Bygger på push til alle branches, men er man på main så pushes det også til ECR. Siste image har fått latest på tag. 
[![Java CI/CD to AWS ECR](https://github.com/SorensenMartin/PGR301Exam2021/actions/workflows/2021_ecr_publish.yml/badge.svg)](https://github.com/SorensenMartin/PGR301Exam2021/actions/workflows/2021_ecr_publish.yml)

# Oppgave 3a

##### Endringer gjort på terraform koden til Kjell er som følger:
###### Hardkoding av service name byttet til noe passende for meg: "edu-apprunner-service-kand-2021"
###### Alle "hardkodede" verdier har blitt byttet ut med var.ettellerannent slik at man enkelt kan endre variabler i egen variables.tf fil.

## Søkte i dokumentasjonen og fant dette: 

![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/9c73da3e-a3d6-4af5-98b1-c78064f23453)

## Endret da min konfigurasjon etter denne malen, noe som ser ut til å ha funket fint: 

![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/70a0dfc8-11a9-4769-8183-8354abfbf269) 
      
###### Se main.tf med variables.tf for alle endringer gjort fra Kjells originale skript. 
###### Etter dette var gjort startet jeg en terraform for å lage min egen IAM rolle for app runneren, samt kjøre fra mitt ECR fra tidligere oppgave.

# Oppgave 3b

##### Workflowen har blitt separert inn i to egen oppgaver, en for å bygge og en for å deploye image til ECR og terraform på main. Se Workflow: 2021_ecr_publish.yml
##### La også til i main.tf endringer for backend og aws terraform provider. Brukte her bucket fra tidligere øvinger pgr301-2021-terraform-state for å lagre min state. 

### For at workflowen skal fungere på fork, er det noen ting man må gjøre. 
- Som i oppgave 1a er det eneste som er uunngåelig at en fork må inneholde egne aws_access_key_id og aws_secret_access_key. 
- Så lenge disse nøklene gir samme tilgang på aws som de jeg har blitt tildelt, skal det ikke være nødvendig å endre noe annet for å kjøre workflowen.
- Om man vil, kan man alltid endre variablene i variables.tf for å bruke sitt eget ECR, Bucket, Apprunner eller annet. Men det jeg har laget fungerer da altså greit for andre med samme rettigheter.


##### Under kan man se flowen 👇
[![Java CI/CD to AWS ECR](https://github.com/SorensenMartin/PGR301Exam2021/actions/workflows/2021_ecr_publish.yml/badge.svg)](https://github.com/SorensenMartin/PGR301Exam2021/actions/workflows/2021_ecr_publish.yml)

##### REF instanser av Apprunner oversteget: Jeg slettet min apprunner om det skulle være noe spørsmål om logger osv.
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/1cc6843d-15f8-49bf-bd0b-94bbad2a2107)


# Oppgave 4a
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/b1ee2c9f-8bc6-4bc0-b48e-40e141ca0e22)
## For denne oppgaven har jeg gjort om applikasjonen til å være en service mitt sikkerhetsfirma tilbyr ulike byggeplasser!
##### Vernevokterene har fått nye ting å bryne seg på, og fra ett kamera på vår tildelte byggeplass strømmer det daglig inn bilder for sikkerhetssjekk! Endepunktene reflekterer søk som daglig leder for byggeprosjektet har bedt oss være på utkikk etter!
##### Har for denne oppgaven lagt til to nye endepunkter. Begge bruker rekognition sin label detection, men til ulike hensikter. Under går jeg igjennom alle endepuktene i applikasjonen, hva de gjør, hvordan data som blir registrert og lagret som metrics i cloudwatch, og begrunnelse rundt alle valgene.
### OBS! Er også beskrivelse rundt valg og de ulike endepunktene i RekognitionController.java!!! 

# Endepunkt 1: Sjekker om arbeiderene har tatt på seg hjelmen sin før de går inn på byggeplassen!

##### Dette endepunktet er for det mest uendret funksjonelt, men har byttet ut munnbeskyttelse med hjelm for det den skal søke etter. 
##### Metoden har fått inn noen metrics, disse er som følger: 
#####      - Timer: Registrerer data på hvor lang tid API kallet bruker på å få tilbake de ferdig sjekkede bildene.
#####      - Sjekker filene i bucket: Sjekker om noen av bildene tatt av sikkerhetskameraet er korrupte, som gir en indikasjon på helsen til hele systemt.

# Endepunkt 2: Sjekker om noen av arbeiderene prøver å ta meg seg våpen inn på arbeidsplassen!

##### Dette endepunktet bruker som nevt tidligere rekognintion sin label detection, og skanner etter noen forhåndsdefinerte objekter som anses som farlige.
##### Trygghet på arbeidsplassen handler ikke bare om å ikke få noe tungt i hodet, det handler også om å ikke bli skutt av kollegaen din.
##### Metoden har fått inn noen metrics, disse er som følger:
#####      - Timer: Også her en timer, livsviktig at sikkerhetsvaktene kan oppfatte farlige situasjoner fort, slik at de kan gjøre noe med det! Denne metoden bør gå enda fortere enn hjelm scan.
#####      - Counter for arbeidere scannet, samt om de hadde eller ikke hadde med våpen: Sjekker antall av arbeiderene som tar med farlig våpen på jobb.
#####      - Tank Detector: Byggeplassen hadde tidligere opplevd problemer med en arbeider som tok med en tanks på jobb. Derfor ville de ha en alarm om dette skulle skje igjen..

# Endepunkt 3: Sjekker om noen av arbeiderne har med seg substanser som vil gjøre arbeid på byggeplass farlig! ☠

##### Dette endepunktet benytter seg også av label detection, men dette endepunktet sjekker om arbeiderene har med seg stoffer som alkohol og piller.
##### Edruelighet er en viktig faktor for at arbeidet på byggeplassen skal foregå på en trygg og forutsigbar måte! Man må kunne forholde seg til kollegaene sine på en ordentlig måte.
##### Metoden har fått inn noen metrics, disse er som følger: 
#####      - Arraylist: Metoden holder en liste som legger inn alle de ulovlige substansene tatt med inn på arbeidet. Dette gjør at man kan finne ut av hvem som har med ulovlige midler, samt sjekke hva som trender av ulovlige substanser blant de ansatte. 

# Cloudwatch dashbord

##### Med disse endepunktene og dataen som blir samlet opp, har jeg laget dette dashbordet for enkel oversikt over viktige sikkerhetshensyn. De ulike elementene kan slettes/endres via dashboard.tf: 

![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/c9dbc4f0-e6b0-45ac-8d08-7278b46874d3)

### La oss gå dypere inn på hvert enkelt element i dashbordet. 

### Substanser tatt in på arbeidsplassen. 

![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/e74f6070-05b6-4ea5-9232-f0fb8ee67033)

##### Her kan vi se de fire ulike substansene jeg per dags dato tracker. Disse er fordelt utover i søyler for å gi et raskt og effektivt innsyn i hvilke substanser som er de mest popuælere på arbeidsplassen. Her ser vi at det trender mot bruk av piller, og HR kan sette igang med forebyggende tiltak rundt dette. 

### Fordeling av våpen blant ansatte.

![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/1c21ce27-7116-468a-9e85-3cc26b218409)

##### Her kan vi se at 25% av de ansatte har tatt med seg våpen inn på arbeidsplassen. Dette gir en indikasjon til ledelsen om hvem som skal få lov til å være med videre som ansatt, samt hvem som må gå. Her kan man da over tid se trender basert på hvem man ansetter og sparker (noen vil si at 25% er noe høyt på en arbeidsplass 🙂)

### Våpenscan tidsbruk.

![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/f8c78d84-ba6b-43f1-b0cb-89e48a3eff4b)

##### For at sikkerheten på arbeidsplassen skal kunne arbeide optimalt, er det viktig at selve scannen for våpen skjer hurtig. Derfor har vi ett meter som registrer maksimum tid på scan siste tre timer, med noen tresholds. Helst vil vi se at det tar under 5 sekunder og scanne, vi aksepterer under 12 sekunder, men ved over 12 sekunder må vi gjøre endringer/forbedringer på applikasjonen slikk at sikkerhetspersonellet kan være på område før våpen kan bli brukt med ondsinnede hensikter.

### Timer for sjekk av hjelm

![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/1ed1d7d8-77b0-4c19-a9f7-5741a594de0b)

##### Her kan vi se gjennomsnittstiden på scan av hodeplagg. Her er det ikke satt opp noen tresholds, men det er fornuftig for bruker av dashbord og kunne se at scanningen ikke tar for lang tid, samt at scannen ikke henger seg opp eller andre tekniske problemer. 

### Alarm modulen for dashbordet. 

![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/89f6c148-a856-4277-9bf5-dc067d126e55)

##### Her får vi en oversikt over et av de viktigste hjertesakene for ledelsen på arbeidsplassen: "Dette er en tanks fri sone!". Om det våpenscan endepunktet finner en tanks, vil den umiddelbart stige over tresholdet og sende en alarm til ledelsen. Grafen viser også hvor ofte det har blitt funnet tanks, hvor en flant linje helt nederst er det enhver "dashboard-manager" ønsker og se. Selvom det er en egen widget, ligger det også her en henvisning til alarmen, den viser om alarmen er utløst eller ikke. I denne situasjonen ser vi at det har blitt oppdaget en tank, og alarmen har blitt utløst. 

# Oppgave 4b

##### For denne oppgaven valgte jeg som tidligere vist å lage en modul som ville kunne utløse en alarm om det kjørte en tanks inn på arbeidsplassen. Dette gjorde jeg for å sikre tryggheten på arbeidsplassen, samtidig som at dette var noe ledelsen spesifikt ønsket seg ( noe om en tidligere hendelse, ref. bildet )
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/072a28a1-414a-467a-abe4-3236cdb4d1ca)

##### For å få til dette måtte jeg gjøre ett par ting:

#####      - Satte opp en modul i infra katalogen med en main tf som ser slik ut:
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/0ea65a03-85e1-42d6-bfb6-77c63148921e)
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/f7a2f381-739d-4d40-b482-e50a4813fb6f)

#####      - I modulen er det mulighet for å endre en del variabler for å tilpasse etter ønsket, for å sikre god gjenbrukbarhet.
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/2ee35705-e670-4055-a9b6-e7e93d8678eb)
##### Sett modulen inn i din hoved main.tf i terraform katalogen, kopier over alarm_cloud_module, og endre på alle elementene under source for å gjenbruke modulen.

##### Etter at modulen var tilgjengelig, og det ble skannet for våpen på arbeidsplassen oppsto det ett treff. Dette førte da til at en varslings-epost ble sendt ut å sikkerhetsteamet fikk kontroll på hendelsen. 
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/20d9eff2-edb6-458a-b9a6-f11cb327d872)

# Oppgave 5a
##### Kontinuerlig Integrasjon.
Kontinuerlig integrasjon handler om at kodeendringer, gjerne fra flere ulike utviklere på ett team, regelmessig og automatisk blir lastet opp og integrert i ett delt github repository eller lignende. Dette er ett prinsipp i programvareutvikling som sørger for at man enklere kan opprettholde en høy kodekvalitet hvor alle arbeider mot det samme målet. CI som prinsipp handler også om å fremme nettopp det med hyppige pushes til et repo i et utviklingsteam, slik at man unngår problemer med integrering i størst mulig grad. 
En definisjon kan være: Automatisk bygging og testing av kode ved push til et repository. CI sørger for tidlig indentifisering av problemer samt sikre kontinuerlig og høy kodestandard på prosjektet ved å fremme en kultur for hyppig integrering. 
Fordeler og ulemper ved bruk av CI i et utviklingsprosjekt, både som team og alene: 

-	Fordeler:

-	Kontinuerlig testing og oppdaging av feil tidlig: Med en pipeline sørger systemet for at alle oppdateringer av en applikasjon blir testet, på denne måten kan man               raskt oppdage og rette opp i Bugs uten at systemet oppdaterer seg med disse feilene. Pipelinen er også flink til å lokalisere nøyaktig hvor feilen skjer, noe som               gjør feilsøking og fiksing mye enklere. 
-	Automatisering av hverdagslige oppgaver: Oppgaver som egentlig bare blir rutine for programmerer slik som bygging og testing blir tatt hånd om av systemet, slik at             de kan fokusere på ny god kode og funksjonalitet. 
-	Konflikthåndtering: Med hyppig kodeoppdatering, vil det minske sannsynligheten for at to utviklere som arbeider på samme kode får konfliktproblemer, noe som sørger             for en mer smidig utvikling.  
-	Samarbeid: Alt handler ikke om det tekniske. Hyppig deling av nye kodesnutter kan hjelpe med å skape diskusjon i et team, hvor alle kan komme med sine                          tilbakemeldinger.
-	Skalerbarhet: Med kontinuerlig integrasjon kan nye medlemmer effektiv og smertefritt hoppe inn å bidra til koden uten at det forstyrrer noen av de andre sine                   arbeidsflyter. Det hjelper også med tanke på at man kan teste og utforske, samtidig som det er lettere å rulle tilbake til før et eventuelt problem oppsto.


-	Ulemper: Det er greit å sette lys på at ingen utviklings prinsipper er uten mangler eller problemer.

-	Oppstart: Og sette opp et godt å robust pipeline /CI system kan være komplisert. Mye ressurser vil måtte gå til dette i oppstartfasen av ett prosjekt. 
-	Ressursbruk: Et CI system kan ofte kreve mye av en server, som ikke ville vært belastet på samme måte ved andre prinsipper.
-	Tekstkvalitet ekstra viktig: God kunnskap i teamet om det å skrive gode tester er viktig. Svake tester i pipeline kan føre til falske positiver og feil som sniker              seg igjennom systemet. Det er også viktig at noen på teamet jobber med å oppdatere disse testene til å holde stand med resten av kodebasen. Menneskelige ressurser kreves       også for å sørge for at CI-systemet blir vedlikeholdt på en god måte. 
-	Sikkerhet: Tar med en liten henvisning til det med sikkerhet. Pipelinen må ofte ha tilgang på sensitiv informasjon. Om man gjør feil så kan det eksempelvis hende               at noen med dårlige intensjoner får tilgang på ett AWS Miljø som kan koste Noen dyrt :)
      
I praksis så kan et team bruke CI via Github på samme måte som vi har gjort det i dette faget: Sette opp en github actions med ulike oppgaver. Dette kan være alt fra bygg på ulike branches og ren testing. Vi har også sett på det med pull-requests. Effektiv implementering av dette kan sørge for at all kode blir gjennomgått av for eksempel en lagleder som kan sørge for at all koden er konsistent og god før den blir lagt til på produksjon/main. 

En god feedback loop med tilbakemelding av actions prossesene er også essensielt for å sørge for en smidig håndtering av eventuelle feil i en av prosessene som de ulike actions filene kjører. God CI handler også om å benytte seg godt av nettopp av ulike branches man kan jobbe på. Dette kan være main/produksjon, ulike funksjoner som har hver sin branch, ett for utvikling, ett for å teste tester etc. 

Det er viktig og se CI som en helhet også. Det å implementere CI med CD er essensielt. Der CI har ansvar for testing og bygging, har CD (Kontinuerlig Levering) ansvar for at denne koden raskt og smertefritt blir levert til blant annet produksjonsmiljøet. Dette har vi sett på i denne oppgaven, hvor både bygging og push til AWS sørviser skjer ved verdt push. Her er også publiseringen avhengig av at byggingen faktisk ikke feiler. Her er kanskje sleve CI delen av programmet litt tynn på testing 😉 I et team som jobber under disse prinsippene vil forhåpentligvis kommunikasjonen og forståelsen for hverandre blant teamet god, samt at avstanden fra ny kode til produksjon vil være kort!

# Oppgave 5b

### Scrum vs Devops
Får å forstå fordelene og ulempene ved bruk av to ulike metodikker innenfor programvareutvikling er det først viktig å understreke hva de ulike metodenes kjennetegn og 
hovedtrekk er.

### Scrum/Smidig: 
Scrum er noe jeg har kjennskap til gjennom prosjektoppgaven forrige semester. Derfor har jeg litt erfaring her med tanke på hvordan det er å jobbe i ett team i praksis. Scrum er basert på «Agile Manifesto» Her blir viktigheten av fleksibilitet, kundesamarbeid og tilpasningsevne prioritert. Det å reagere på endring effektivt er det viktigste elementet for vellykket utvikling. 

Scrum handler om korte, iterative utviklingssykluser, kjent som sprinter. Disse sprintene kan variere i lengde, men typiske sprinter er fra en til fire uker. Under planlegging av sprinten setter man opp ulike punkter og mål, hvor fokuset er å fullføre en bestemt del av en leveranse. Etter sprinten er over er da målet at denne planlagte funksjonaliteten skal være leverbar og kvalitetsmessig sterk. I løpet av sprinten er det noen ritualer som er essensielle for å sikre flyt og fremdrift. Daily Standup: Korte daglige møter for å diskutere arbeid, eventuelle problemer og progognosen for sprinten. Sprint planning, review og retrospective. Dette er møter hvor man planlegger sprinten, går igjennom hva mål som er oppnådd etter sprint, og diskuterer/reflekterer over sprinten for å finne ut av hva som var bra og hva som kan forbedres. 
Ett utviklingsteam som arbeider med Scrum som metode, deles opp i ulike roller. Disse rollene er som følger: 
-	Produktteier: Ansvarlig for hva som er prosjektets mål. Prosjekteier skal veilede resten av teamet når det kommer til ønskelige funksjoner og prioriteringer under              prosjektets levetid. 
-	Scrummaster: På mange måter en lagleder eller kaptein. Scrummaster har ansvar for å kommunisere med produkteier, sette opp å lede planlegging av sprinter, samt å passe         på at alle i teamet følger prinsippene for scrum prosessen og at alle i teamet har en de kan komme til med sine tanker eller problemer. 
-	Utviklerlaget: Teamet som jobber med å gjennomføre sprinten, som selv velger sin workload og hva de mener de er kapable til å gjennomføre av de ulike målene for                sprinten. 
Noen styrker og svakheter ved scrum: 
-	Styrker:

-	Selvorganisering: Alle teammedlemmene for mulighet til å selv definere sine roller og ønsker, noe som gjør prosjektet mer engasjerende og utvikleren kan ta eierskap over       sine roller og kode. 
-	Fleksibilitet: Ved prosjekter hvor kravene endres er scrum effektivt. Det er jevnlig diskusjon og revisjon om hva som skal gjøres, dette sørger for effektiv tilpassing         av mål.’
-	Forbedringsmuligheter: Via de ulike reviewen, får man som et team overblikk over hva som fungere, og hva som kanskje ikke fungerer fullt så bra. Dette sørger for at            fokuset alltid ligger på kontinuerlig forbedring og læring av tidligere sprinter. 

-	Svakheter: 

-	Krever dyktig produktteier: Det meste av fokuset for de ulike sprintene og teamet som en helhet blir satt av produkteier. Om produkteier er diffus og uklar i sin visjon,       kan det føre til ueffektive sprinter med dårlig fokus.
-	Godt team: Det må være god kjemi og dynamikk i ett team som skal arbeide med scrum. Om flere av deltakerne ikke spiller på lag, har dårlig kommunikasjon eller rett og          slett viser motvilje til å utføre scrum vil effektiviteten av metodikken krasjlande. 
-	Oppfattelse av møter: Metoden fremmer sterk kommunikasjon. Dette gjennom daglige møter og gjennomgang av de ulike sprintfasene. For en gruppe som ikke er vant til denne        måten å kommunisere på kan det virke unødvendig og overveldende. 

### Devops: 
Devops har jeg gått smått innpå i oppgave 5A, og dette er selve metodikken dette faget baserer seg på. Devops som prinsipp handler om å kutte ut avstanden mellom utvikler teamet og drifts teamet. Man kan dra paralleller til LEAN prinsippet, og strømlinje formet utvikling samt eliminering av sløsing blir prioritert. Det å skape en kultur for samarbeid og delt ansvar for sluttprodukt er essensielt for å implementere Devops som prinsipp i en bedrift, avstanden mellom de ulike lagene blir minsket. For å oppnå dette kreves det da planlegging mellom gruppene for hvordan hele prosessen for integrering og produksjon skal utføres, da via automatiserte prosesser som tidligere beskrevet og utført i denne eksamen. CI/CD, som jeg referer til oppgave A for å lese mer om, er ett av hoved elementene i Devops som kultur på arbeidsplassen. Devops som prinsipp kan og har revolusjonert måten programvare blir laget i sinn helhet på. En holistisk tilnærming til livssyklusen av programvare sikrer effektivitet, kvalitet og lagånd i en bedrift 
Det fokuserer på bruk av ulike verktøy for å automatisere helheten i prosjektet. Ett eksempel på et slikt verktøy kan være docker som er blitt brukt hyppig igjennom dette faget. Infrastruktur som kode er også et viktig element som sørger for at verktøy som for eksempel terraform sørger for at infrastruktur og vedlikehold kan foregå automatisert.  
Noen styrker og svakheter ved Devops: 
-	Styrker: 

-	Leveringen optimaliserer: Når de ulike lagene jobber sammen, i samspill med at de ulike stegene i utviklingen automatiseres, blir veien til distribusjon forkortet              betydelig.  
-	CI/CD for kvalitet- og stabilitetssikring: Som nevnt i oppgave a: Kontinuerlig integrering sørger for stabil og tidlig oppdagelse av feil. Dette hjelper også for å             opprettholde god kvalitet på kode. 
-	Effektivisering: Automatisering av monotone oppgaver frigjør ulike medlemmer av teamene til å arbeide med andre viktige oppgaver. 

-	Svakheter: 

-	Kompleksitet: Det å sette opp en struktur som fremmer Devops kan være krevende. Det å velge viktige verktøy for ulike oppgaver og integrere dem i prosjektet kan være           utfordrende. Det å sette opp å automatisere CI/CD kan også være komplekst, og kan kreve hyppig gjennomgang av struktur og tester. 
-	Kultur: Samme som med scrum, krever Devops en endring i kulturen på en arbeidsplass. Dette kan være krevende i firmaer og organisasjoner med satte, faste rutiner og            arbeidsmetoder. 
-	Sikkerhet: Hyppighetene av leveransen samt utlevering av sensitiv informasjon til CI/CD kan fort føre til problemer med tanke på sikkerhet. 

### Så hvilken metode bør vi bruke?
Man kan tydelig se at både scrum og Devops har som mål å effektivisere hastigheten på leveranse og øke kvaliteten på sluttproduktet. De har allikevel ulike tilnærminger når det kommer til å nå disse målene. 

Scrum fokuserer på dynamikken i et team og en iterativ utviklingsprosess. Det er fokus på tilpassing og endring i møte med ulike oppgaver. Det forholder seg også tett til kunden å deres behov.  Devops foksuerer mer på hele livssyklusen til en applikasjon, og samspillet innad i flere ulike lag, både drift og utvikling, innad i en bedrift for å optimalisere denne syklusen. Automatisering av oppgaver er essensielt. 

Når det kommer til leveransetempo, fokuserer scrum på raske iterasjoner og feedback for forbedring. Dette øker hastigheten på leveranse noe kontra andre metodikker for programvareutvikling. Devops sitt fokus på automatisering øker til sammenligning leveransetempo kraftig. Veien fra utviklingsfasen til leveranse blir kort vi CI/CD. Om målet bare er rask leveranse, vil Devops være det optimale valget om teamet er villig til å legge ned grunnarbeidet som kreves. 
Når det kommer til programkvalitet er det også ulike tilnærminger, men her blir resultatet mer likt. Scrum fokuserer på feedback og revisjon av arbeidet utført i sprinter. Dette sørger for en sterk tilpasningsevne etter som hva som funker. Det at det daglig diskuteres gir også mer strukturert kode med god kodestandard og likhet. Devops forbedrer kodekvaliteten mer igjennom kontinuerlig testing og overvåking for å sile ut ineffektive metoder og feil. Begge sørger for bedre kodekvalitet, med hver sin styrke. 

##### Fordelaktige situasjoner for de ulike metodene:
Et eksempel på en fordelaktig situasjon hvor bruken av scrum vil være gunstig kan være en start-up. I en bedrift med stadig endrende produktkrav basert på kunders ønsker og markedet vil scrum sin fleksibilitet i møte med endringer være ideelt. Om start-up bedriften opererer i en bransje med endrende markedsbehov kan denne metoden være smart å integrere i teamet. Vi kan konkludere med at dynamiske prosjektmiljøer er spesielt godt egnet for å ta i bruk scrum som metodikk. 
For Devops kan en fordelaktig situasjon være en etablert, stor bedrift på nett. Om vi tar google som eksempel, vil viktigheten av kontinuerlig oppdatering og testing være essensielt. Samtidig som dette skjer, må man sørge for at nedetiden på nettsiden blir minimal. Devops sitt fokus på automatisering av integrering og testing er derfor i et slikt miljø optimalt.

For å konkludere kan vi si at både Devops og Scrum har sine fordeler og ulemper, selv om målet kan sies og være det samme. Begge metodikkene tar for seg kompleksiteten i programvareutvikling. Fokuset er på å bryte ned prosesser i mer menneskelig håndterbare biter for å øke kvaliteten på produktet. De har forskjellige stryker, som kan komplimentere hverandre. Valget av hvilken metode man vil benytte seg av må tas i sammenheng med kravene for prosjektet man setter ut på. Det er også mulig å kombinere bruken av disse metodene for å dra nytte av begges fordeler i utviklingsprosessen. 

# Oppgave 5c

### Feedback

Feedback som prinsipp i Devops handler om tilbakemelding. Det å skape ett system og kultur for kontinuerlig tilbakemeldinger på tvers av hele utvikling og driftsprosessen er fokuset. Feedback er avgjørende for hvorvidt en bedrift vil lykkes eller feiles med implementasjon av Devops. Jeg har ved flere anledninger i oppgave A og B påpekt viktigheten av en feedback loop. Hva og hvordan vi kan dra nytte av dette i en personlig applikasjon jeg jobber med, samt hvordan jeg kan implementere det, vil jeg diskutere her:

Først og fremst, Feedback Loop: En feedback loop handler om hvordan man samler informasjon om et system. Det å samle inn, analysere og trekke begrunnelser basert på systemets ytelse og brukeropplevelser kontinuerlig er viktig for å styre fremtidige løsninger. Slik data kan vi samle via automatiserte tester, analyser av bruk samt tilbakemeldinger. 

For å forsikre meg om at funksjonalitet jeg har arbeidet med vil møte behovet til sluttbrukeren, er det flere steg jeg kan ta. Først vil det være logisk og sette opp ulike mekanismer som gir meg mulighet til å samle data fra feedback.
-	Undersøkelser: Brukerundersøkelser og intervju spørsmål til brukere av applikasjonen kan hjelpe med å filtrere ut nyttige og uønskede ideer. Dette kan gi en pekepinn på        hvordan forbrukere opplever det nåværende systemer, samt skape en forståelse for deres forventing med tanke på fremtidig funksjonalitet. 
-	Brukertester: Få brukere til å teste de nye funksjonene. Selv om en funksjon kan virke intuitiv og brukervennlig for meg som utvikler, kan den fort oppleves helt gresk         for en bruker. Dette sørger for at funksjonaliteten blir mer intuitiv. Dette kan gjøres gjennom prototype eller MVP av en funksjon.
-	Programvare relatert: Metrics, Github Issuess, Jira etc. 

Ved hjelp av disse mekanismene kan man få tilbakemelding fra de som faktisk skal bruke min applikasjon, noe som er livsviktig for den langsiktige suksessen til en applikasjon. Når det kommer til integrering av feedback under utvikling, BØR man ta i bruk denne dataen.  

Under planlegging kan man benytte seg av feedback samlet inn fra brukerne til å kartlegge hva som skal prioriteres i form av nye funksjoner. Under utviklingen kan man ha fokus på iterativ utvikling. Sette opp faste tidsperioder hvor man gjennomgår og implementerer ny funksjonalitet basert på feedbacken. 

Man kan under utvikling benytte seg av ulike feedback verktøy som Github issues for å spore bugs og innspill fra brukere. Disse kan hjelpe meg med prioritering og bugfixing ettersom den nye funksjonaliteten blir brukt. Det kan også være en ide og sette søkelys på loggføring og overvåking. Det å analysere data med tanke på hvordan applikasjonen blir brukt er viktig. Dette kan være brukerdata, frekvensen ulike elementer blir brukt, engasjement rundt funksjoner og atferd i forhold til ny og gammel funksjonalitet. Dette hjelper med å gi innsikt i hva som blir godt mottatt, og hva som er mindre ønsket. Det kan være smart å bruke verktøy for å kategorisere tilbakemeldinger, for å få en mer helhetlig forståelse av brukerens ønsker. Ved å undersøke disse kategoriene, kan man sette prioriteringer basert på viktigheten. Dette kan være kategorier som Bugs, forbedringsforslag, forespørsler om ny funksjonalitet osv. 

Det å sette opp ulike metrics for å måle verdien av ulike iterasjoner kan hjelpe med kontinuerlig læring. Etter at funksjonaliteten er lansert, er det viktig med kontinuerlig forbedring. Ta i bruk resultatene på de ulike mekanismene for innsamling av informasjon om den nye funksjonaliteten: Hva likte bruker? Hva ved funksjonen blir ikke brukt? Er det vanskelig å forstå seg på? Osv. Det å regelmessig lansere oppdateringer basert på denne feedbacken, samt følge med på responsen på den nye versjonen, vil hjelpe med å forbedre brukervennligheten og nytteverdien av den nye funksjonen gradvis. Dette sørger for at man sammen med brukere kan jobbe seg fram til en meget god løsning, i stedet for å sitte alene å produsere en versjon man selv tror er perfekt. Denne kunne man ha brukt lang tid på, men den blir ikke nødvendigvis tatt godt imot av brukerne av den grunn. 

Som solo utvikler kan det også være en ide og benytte seg av blant annet forumer eller sosiale medier for å kommunisere med brukeren. Er man delaktig her, viser man brukeren at man bryr seg og møter dem på deres arenaer. Dette kan føre til at det blir et samfunn rundt applikasjonen, hvor utvikler å brukere kan jobbe sammen for å produsere den perfekte applikasjonen for begge parter! Ved å være proaktiv og anvende feedback gjennom en hel utviklingsprosess, kan jeg sørge for at applikasjonen min kontinuerlig blir optimalisert basert på ekte brukerdata. Ikke bare vil dette føre til en bedre applikasjon, men en mer lojal og solid brukermasse. 


#EOF












