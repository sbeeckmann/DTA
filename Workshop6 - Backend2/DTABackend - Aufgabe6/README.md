Workshop 5 Aufgabe 5

Vorbereitung einer Authentifizierung im Backend



Über die App soll es später möglich sein, sich zu registrieren und einzuloggen.

Dafür kann bereits jetzt im backend und auf der für die Datenbank die Arbeit erledigt werden.

Erstellt ein weiteres Datenbankobject, z.b. User_ (es darf bei Postgres keine Tabelle User heißen!!!!)

Diese soll ein Namen und ein Password enthalten.

Passwörter sollten niemals im Klartext gespeichert werden, sondern immer in Form eines Hashes 

Implementiert für die Speicherung des Passwortes einen Hash -> // Tutorial https://www.baeldung.com/java-password-hashing als Beispiel

Erstellt die notwendigen Rest requests für die Registrierung und Authentifizierung, sowie ein UserBean, wo die entsprechende Erstellung des Nutzers erfolgt bzw ob die Daten bei der Authentifizierung korrekt sind.

Das ganze kann wieder mit Postman (https://www.postman.com/) oder direkt als Chrome Plugin, getestet werden.

