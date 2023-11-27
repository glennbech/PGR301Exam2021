# PGR301Exam2021
PGR301 Eksamen for kandidat 2021

## Oppgave 1a

##### For at workflowen laget til denne oppgaven skal fungere hos andre, må man lage AWS access keys i IAM og legge dem til som secrets i sitt eget repo. Da under navnene:
##### - AWS_ACCESS_KEY_ID og AWS_SECRET_ACCESS_KEY
##### Det kan også være en god ide å endre navn på bøtten i denne delen av flowen til dette eksemplet eller egen bucket:
      name: Deploy SAM Application
      if: github.ref == 'refs/heads/main' && github.event_name == 'push'
      run: sam deploy --no-confirm-changeset --no-fail-on-empty-changeset --stack-name kand2021 --capabilities CAPABILITY_IAM --region eu-west-1 --resolve-s3
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

##### Etter dette hadde jeg fortsatt trøbbel med at det lå feil filformat i bucket, derfor la jeg til en sjekk for filendelsen i RekognitionController.java som sørger for at bare filer med jpg eller jpeg blir hentet ut! (Kan alltids legge til flere godkjente filtyper her også om ønskelig) Etter dette var gjort fikk jeg ønskelig resultat av curl kommandoen.

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

##### Med disse endepunktene og dataen som blir samlet opp, har jeg laget dette dashbordet for enkel oversikt over viktige sikkerhetshensyn: 

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

##### Her får vi en oversikt over et av de viktigste hjertesakene for ledelsen på arbeidsplassen: "Dette er en tanks fri sone!". Om det våpenscan endepunktet finner en tanks, vil den umiddelbart stige over tresholdet og sende en alarm til ledelsen. Grafen viser også hvor ofte det har blitt funnet tanks, hvor en flant linje helt nederst er det enhver "dashboard-manager" ønsker og se. 

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











