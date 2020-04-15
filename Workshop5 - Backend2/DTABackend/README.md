Workshop 3 - Aufgabe 3

In Anlehnung an die Definition vom Entry der App, hat dieses eine Priorität, diese fehlt jedoch im Server.

Fügt dem Datenbankenobject eine weitere Spalte priority hinzu.

Dies soll ebenfalls ein Enum sein, mit den Werten HIGH,MEDIUM, LOW

nachdem dem erneuten deploy überprüft die Datenbank, ihr seht nun eine weitere Spalte jedoch mit dem Wert "null" für alle exestierenden Einträge.

Sucht alle Einträge mit einer Query aus der Datenbank, wo das Feld priority null ist (für die Überprüfung innerhalb der where Klausel einer query auf null kann "is null" verwendet werden)

Setzt für alle entries diesen Wert auf MEDIUM.

Anschließend erstellt einen weiteren Eintrag mit der Priorität HIGH und Priorität LOW, falls noch nicht vorhanden.

Solllte das gelungen sein, sollten 4 Einträge in der Datenbank exestieren.

Schaut euch den geschrieben Code im TesterBean an -> es gibt mehrere Stellen wo ein neuer Entry erzeugt wird, das jeweils fast identisch aussieht. 

Lagert die Erzeugung des Entry objectes in eine Funktion "createEntry" aus, welche Parameter müssen übergeben werden?