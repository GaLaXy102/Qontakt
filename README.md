# Qontakt

Qontakt ist eine Anwendung zur Nachverfolgung von Infektionsketten im Sinne des Infektionsschutzgesetzes. Öffentliche Orte wie Läden und Restaurants können Lokal-Profile erstellen. In diesen Lokalen können Endnutzer nach einmaliger Registrierung einchecken. Während des Aufenthaltes können sich Lokal-Inhaber mit einem QR-Code, welcher auf dem Endgerät des Kunden angezeigt wird, über die erfolgte Anmeldung informieren, sodass zusätzlich die analoge Kontaktverfolgung ermöglicht wird. Beim Verlassen eines Lokals muss der Kunde sich selbstständig auschecken. Wenn er dies nicht tut, erfolgt ein automatischer Check-Out jeweils zu einer vom Lokal-Inhaber bestimmten Uhrzeit (bspw. 03:30 Uhr). Zu dieser Uhrzeit werden außerdem alle Einträge, welcher älter sind als es die rechtliche Aufbewahrungsfrist vorsieht, gelöscht.

## User Stories

### U-N-00

Ein Nutzer möchte sich gegenüber der Anwendung registrieren. Dafür gibt er folgende Daten an:

* Name und Vorname
* E-Mail-Adresse
* Anwendungspasswort
* Heimatbundesland und die erforderlichen Daten nach [Tab. 1]

Die Registrierung muss nicht vom Kunden per E-Mail bestätigt werden. [Zusatz: doch]

Nur ein am System registrierter Nutzer kann sich am System anmelden.

### U-N-01

Ein Nutzer möchte seinen Besuch in einem Qontakt-Lokal erfassen. Hierfür scannt er einen vom Lokal generierten QR-Code [U-L-05] am Eingang des Lokals und wird auf eine Willkommensseite weitergeleitet. Dort kann er sich registrieren [U-N-00] oder sich mit E-Mail-Adresse und Passwort anmelden. [Zusatz: Anonyme Check-Ins werden ebenfalls unterstützt.]

Der Check-In wird vom Nutzer bestätigt, woraufhin er zur Hauptseite [U-N-02] weitergeleitet wird.

### U-N-02

Der Nutzer möchte sich Details zu in der Anwendung gespeicherten Daten anzeigen lassen. Auf der Hauptseite kann er seine Nutzerdaten einsehen und ändern, sowie eine Historie der gespeicherten Check-Ins für den Nutzer erhalten.

### U-N-03

Der in einem Lokal eingecheckte Nutzer wird vom Personal aufgefordert, seinen Check-In nachzuweisen. Hierfür öffnet er auf der Hauptseite [U-N-02] seinen aktiven Check-In und bekommt einen QR-Code mit folgenden Informationen angezeigt:

* UUID des Nutzers [1]
* UUID des Check-Ins [2]
* Timestamp der Anforderung seit Epoch in Sekunden als 64-bit Integer [3]

Das Datenformat des kodierten Strings ist `///[1]/[2]//[3]///`.

### U-N-04

Der eingecheckte Nutzer möchte aus dem Lokal auschecken. Dafür scannt er erneut den QR-Code aus [U-N-01] und wird aufgrund des bestehenden Check-Ins auf die Übersichtsseite [U-N-02] weitergeleitet. Dort gibt es einen Button zum Durchführen des Check-Out. Nach dem Check-Out wird er auf die Übersichtsseite zurückgeleitet.

### U-N-05

Ein angemeldeter Nutzer kann sich am System abmelden.

### U-L-01

Jeder Nutzer möchte ein oder mehrere Lokal(e) anmelden können. Durch diese Aktion wird der anmeldende Nutzer zum **Lokal-Inhaber**. Hierfür müssen folgende Informationen eingegeben werden:

* Name des Lokals
* Anschrift und/oder Koordinaten
* Benennung Datenschutzbeauftragter/Inhaber
* E-Mail-Adresse
* Auto-Checkout-Zeit
* Bundesland (für die zu übergebenden Daten und die Aufbewahrungsfrist [siehe Tab. 1])

Der Lokal-Inhaber bekommt eine UUID und ein Passwort für sein Lokal. Das Passwort wird nur gehasht gespeichert und kann dadurch nur einmal angezeigt werden und muss sicher notiert werden. Die UUID kann im Klartext gespeichert werden. Die Lokalverwaltungsseite wird dadurch freigeschaltet. Auf dieser kann ein Lokal-Inhaber im Namen des Lokals die untenstehenden User Stories [U-L-02], [U-L-03] und [U-L-04] durchführen.

### U-L-02

Um den Datenschutzanforderungen nachzukommen, möchte der Lokal-Inhaber Nutzerdaten löschen können. Dafür muss der Nutzer seine UUID (ein Pseudonym) dem Lokalinhaber mitteilen, welcher dann manuell die UUID eingibt und alle Besuche in einem von ihm verwalteten Lokal löscht. Hierbei sollte gemäß IfSG darauf hingewiesen werden, dass dadurch der Vorgang in Papierform übertragen wird.

### U-L-03

Der Laden-Inhaber möchte unter Einhaltung der Privatsphäre des Nutzers den Check-In eines Nutzers überprüfen. Dafür scannt er den QR-Code aus [U-N-03] und bekommt, falls der Check-In gültig ist, die Check-In-Zeit bestätigt. Ein Check-In ist gültig, wenn der Timestamp [3] nicht älter als 30 Sekunden ist UND der Check-In nicht älter ist als 24 Stunden UND es keinen mit dem Check-In asoziierten Check-Out gibt.

### U-L-04

Der Laden-Inhaber hat vom Gesundheitsamt die Aufforderung bekommen, Nutzerdaten preiszugeben. Der zu erstellende Datensatz ist eine Liste von Besuchen mit den folgenden Feldern:

* Name und Vorname des Gastes
* Persönliche Details je nach Bundesland
* Datum sowie Check-In- und Check-Out-Zeit

Um die Datensicherheit zu gewähren, wird die Datei mit dem öffentlichen RSA-Schlüssel des Gesundheitsamtes, welcher dem Lokalinhaber bspw. per E-Mail mitgeteilt wird und von diesem bei der Anfrage in die Anwendung geladen wird, verschlüsselt. Im Gesundheitsamt kann dieser Datensatz dann mit dem privaten Schlüssel entschlüsselt werden. Als Kryptosystem muss RSA mit 4096 bit langen Schlüsseln erzwungen werden.

Die Ausgabe erfolgt als verschlüsselte CSV-Datei. [Zusatz: Ebenfalls wird ein verschlüsseltes PDF-Dokument erzeugt.]

### Tabelle 1: Nachzuverfolgende Nutzerdaten nach Bundesland

| Bundesland | Aufbewahrungsdauer | Daten                                                        | Stand      |
| ---------- | ------------------ | ------------------------------------------------------------ | ---------- |
| BW         | 28 Tage            | Vor- und Nachname, Anschrift, Datum und Zeitraum der Anwesenheit und, soweit vorhanden, die Telefonnummer | 18.11.2020 |
| BY         | 31 Tage            | Vor- und Nachname, Datum und Zeitraum der Anwesenheit, sowie Telefonnummer oder E-Mail-Adresse oder Anschrift | 20.10.2020 |
| BE         | 28 Tage            | Vor- und Nachname, Datum und Zeitraum der Anwesenheit, Wohnort, sowie Telefonnummer oder E-Mail-Adresse oder Anschrift | 17.11.2020 |
| BB         | 28 Tage            | Vor- und Nachname, Datum und Zeitraum der Anwesenheit, sowie Telefonnummer oder E-Mail-Adresse oder Anschrift | 30.10.2020 |
| HB         | 21 Tage            | Vor- und Nachname, Datum und Zeitraum der Anwesenheit, sowie Telefonnummer oder E-Mail-Adresse oder Anschrift | 31.10.2020 |
| HH         | 28 Tage            | Vor- und Nachname, Anschrift, Datum und Zeitraum der Anwesenheit und, soweit vorhanden, die Telefonnummer | 23.11.2020 |
| HE         | 31 Tage            | Vor- und Nachname, Anschrift, Datum und Zeitraum der Anwesenheit und, soweit vorhanden, die Telefonnummer | 15.05.2020 |
| MV         | 28 Tage            | Vor- und Nachname, Anschrift, Datum und Zeitraum der Anwesenheit und, soweit vorhanden, die Telefonnummer | 11.05.2020 |
| NI         | 21 Tage            | Vor- und Nachname, Anschrift, Datum und Zeitraum der Anwesenheit und, soweit vorhanden, die Telefonnummer | 02.11.2020 |
| NW         | 28 Tage            | Vor- und Nachname, Anschrift, Datum und Zeitraum der Anwesenheit und, soweit vorhanden, die Telefonnummer | 06.11.2020 |
| RP         | 31 Tage            | Vor- und Nachname, Anschrift, Datum und Zeitraum der Anwesenheit und, soweit vorhanden, die Telefonnummer | 09.06.2020 |
| SL         | 31 Tage            | Vor- und Nachname, Datum und Zeitraum der Anwesenheit, Wohnort, sowie Telefonnummer oder E-Mail-Adresse oder Anschrift | 14.11.2020 |
| SN         | 31 Tage            | Vor- und Nachname, Datum und Zeitraum der Anwesenheit, Wohnort, sowie Telefonnummer oder E-Mail-Adresse oder Anschrift | 10.11.2020 |
| ST         | 28 Tage            | Vor- und Nachname, Datum und Zeitraum der Anwesenheit, vollständige Anschrift, Telefonnummer | 30.10.2020 |
| SH         | 28 Tage            | Vor- und Nachname, Datum und Zeitraum der Anwesenheit, Anschrift, sowie Telefonnummer oder E-Mail-Adresse | 01.11.2020 |
| TH         | 28 Tage            | Vor- und Nachname, Anschrift oder Telefonnummer, Datum und Zeitraum der Anwesenheit | 09.06.2020 |

## Technische Spezifikation

TODO
