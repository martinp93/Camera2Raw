Sammendrag
-----------------------------------
I denne hovedrapporten av bacheloroppgaven presenteres arbeidet som er gjort i prosjektet “Bildeprosesserings-app og -API for Android Mobiler”. Rapporten er skrevet av to Dataingeniørstudenter ved NTNU og ble skrevet i tidsrommet januar 2017 til mai 2017.
Målet med oppgaven var å lage et API som tar i bruk algoritmer utformet av andre ansatte ved NTNU og bruke disse på en Android-enhet. APIet skal testes på en Android telefon og resultatet vil bli vurdert visuelt. En annen type vurdering er sett på som unødvendig og utenfor denne oppgavens retningslinjer.
Vår visjon er å lage et raskt og enkelt verktøy som kan brukes til å utføre algoritmer som viser (visuelle) detaljer og informasjon i bildet som ellers ikke kommer så godt fram i det originale bildet. Men informasjon og utdypning om dette kommer senere i oppgaven.
Oppgaven ble gitt av Hans Jakob, med interesse for bilderedigering. Hans Jakob er førsteamenuensis ved instituttet for datateknologi og informatikk med en rekke publikasjoner omhandlende bildebehandlingsalgoritmer.


Takk til
-----------------------------

Takk til følgende git prosjekt som har hjulpet oss med kameradelen i denne koden:
https://github.com/googlesamples/android-Camera2Raw


Informasjon
----------------------------

Deler av applikasjonen er ikke vår egen kode. Det er derfor viktig å merke seg at bildetakingen i denne applikasjonen ikke er vår egen. Se git prosjektet for lånt kode. Koden er beskyttet av Apache License 2.0 [1]http://www.apache.org/licenses/LICENSE-2.0. Videre arbeid med denne koden må derfor beholde denne lisensen. Android-applikasjonen består av 3 views som brukeren navigerer rundt. Disse er koblet opp til ulike kontrollører i applikasjonen som håndterer dataflyten, og hva som kan vises for brukeren betinget av konteksten. Figuren nedenfor viser hvordan hvordan “Views”, “Controllers” og Helper (fungerer som en “Model”), fungerer sammen for å bruke API’et.

Lisens for lånt kode
----------------------------

Copyright 2016 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
