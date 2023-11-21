# PGR301Exam2021
PGR301 Exam for kandidat 2021

# Oppgave 1a

##### For at workflowen laget til denne oppgaven skal fungere hos andre, må man lage AWS access keys i IAM og legge dem til som secret eget repo. Da under navnene 
##### - AWS_ACCESS_KEY_ID og AWS_SECRET_ACCESS_KEY
##### Kan også være en ide og endre navn på bucket i denne delen av flowen til dette eksemplet:
      name: Deploy SAM Application
      if: github.ref == 'refs/heads/main' && github.event_name == 'push'
      run: sam deploy --no-confirm-changeset --no-fail-on-empty-changeset --stack-name kand2021 --capabilities CAPABILITY_IAM --region eu-west-1 --resolve-s3
##### --- --s3-bucket kand2021imagebucket 🍎 +++ --resolve-s3 🍏


[![SAM Build and Deploy if Main](https://github.com/SorensenMartin/PGR301Exam2021/actions/workflows/2021_sam.yml/badge.svg)](https://github.com/SorensenMartin/PGR301Exam2021/actions/workflows/2021_sam.yml)

###### Hva som har blitt gjort for å endre koden til Kjell: 
- Endret hardkoding i app.py til å lese BUCKET_Name
- Oppdatert template.yaml sin target bucket, samt lagt til noen policeies for tilganger som trengtes for dette
- Verifisert at det funker for meg i mitt Cloud9-miljø
- Lagt til workflow som bygger og deployer SAM aplikasjonen ved push på filer i /kjell katalogen. Om pushet ikke er på main, vil kun bygging skje og ikke deployment


#### Her ser vi at lambda funksjonen er opp å går med statuscode 200! 👍
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/81cd8e8b-1bfd-47b2-8dbe-c18a97fa83d0)
#### Her ser vi svaret etter at jeg la til et bilde fra en byggeplass (veldig relevant :) ) i min S3 Image bucket.
![image](https://github.com/SorensenMartin/PGR301Exam2021/assets/89515797/a39d204e-be4c-4ec2-a273-1c5b2ac45f83)

