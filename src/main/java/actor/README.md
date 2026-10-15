# package actor

Actor-Framework und zugehörige Aktoren
AbstractActor
Actor
ActorContext
MessageProcessor
Writer
Reader
AskWithStream
ActorSystem

# Wozu dient das Schlüsselwort synchronized?
___
Synchronized verhindert Datenkonflikte, wenn mehrere Threads auf kritischen Code oder Methoden zugreifen(Nebenläufigkeit).
Es kontrolliert den Zugriff auf diese Codeblöcke oder Methoden, indem es andere Threads blockiert, sobald ein Thread 
diesen Bereich betritt und lässt die anderen Threads die auch darauf zugreifen wollen erst wieder frei, sobald der Thread fertig ist.

Außerdem werden Änderungen an Variablen direkt in den Hauptspeicher geschrieben, sodass andere Threads sofort den 
aktuellsten Wert sehen können.

Synchronized gewährt außerdem Atomarität, das bedeutet der gesamte Codeblock wird entweder ganz oder gar nicht ausgeführt.
___

 # Warum ist die tell-Methode synchronized?
___
Die tell-Methode ist synchronized, da das Actorenmodell auf single threaded Actoren basiert. jeder Actor hat eine 
Mailbox-Queue die er sequentiell abarbeiten muss, wäre die tell-Methode nicht synchronized, dann würden verschiedene 
tell Anfragen gleichzeitig die Struktur der Mailbox-Queue kaputt machen, da diese dort sich gegenseitig überschreiben 
würden oder ganze Nachrichten würden verloren gehen.
___

# Was ist ein ExecutorService?
___
ExecutorService ist eine Schnittstelle in Java, die das Managen und ausführen von asynchronen Aufgaben vereinfacht. statt 
manuell neue Threads zu erstellen, auszuführen und wieder zu löschen, verwaltet dieser einen Threadpool (Gruppe von wiederverwendbaren Worker Threads).
Man übergibt diesem pool eine Aufgabe, ein bereits existierender Worker Thread schnappt sich die Aufgabe, erledigt diese und wartet auf die nächste
ohne das ein neuer erstellt werden oder dieser nach erledigen dieser Aufgabe gelöscht werden muss. Dies spart bei vielen Aufgaben
Speicherplatz, da Threads zu erstellen eine teure Systemoperation ist und wird auch durch das wechseln der Threads das System nicht so 
stark ausbremsen wie manuelles verwalten von Threads.
___

# Was ist ein Demon Thread?
___
ein Demon Thread ist ein Java-Thread, der im Hintergrund läuft und Hilfsdienste für andere Threads bereitstellt. Sie sind
stark von User Threads abhängig, da Java die Virtual Machine sofort beendet und alle Demon Threads abbricht, sollten keine User Threads mehr aktiv sein.
Das liegt daran, das Demon Threads ausschließlich zur Hilfe der User Threads dienen. Ist kein User Thread mehr aktiv, so werden auch 
Hilfsdienste nicht mehr benötigt.
___

# Was ist ein User Thread?
___
Ein User Thread ist ein Java-Thread, der für die Ausführung der Programmlogik zuständig ist. Die Java Virtual Machine wartet konsequent,
bis der letzte aktive User Thread seine Aufgabe abgearbeitet hat und beendet erst dann das Programm. Jeder Thread der erstellt wird,
ist standardisiert ein User Thread, ist manuell über .setDaemon(true) zu ändern.
___

# Haben die Aktoren des Actor-Frameworks eine Message-Queue? Wenn ja, wo?
___
Jeder Actor muss eine Message-Queue besitzen, um die sequentielle Abarbeitung der Aufgaben zu gewährleisten. Es handelt
sich hierbei allerdings nicht um eine klassische List-Queue, in diesem Framework wird die Queue anders gelöst, nämlich über
den ExecutorService. Dieser hat nämlich intern eine Working-Queue, die die Aufgaben sequentiell an die Worker Threads abgibt.
Da jede Message an diesen ExecutorService weitergeleitet wird, wird somit diese Queue gleichzeitig zur globalen Message-Queue des Actoren.
Das synchronized der tell Methode sorgt gleichzeitig dafür, dass auch bei vielen gleichzeitigen aufrufen, die Nachrichten
serialisiert in der Work-Queue ankommen und somit der Zustand des Actors nicht korrumpiert wird.
___

# Was ist ein Semaphore?
___
Ein Semaphore ist von der funktionsweise ähnlich zu synchronized. Der wesentliche Unterschied liegt darin, das ein Schloss
bei synchronized einen festen Besitzer hat. Der Thread der gerade auf die Ressource zugreift und diese für andere Threads somit blockiert,
ist auch der Thread, der diese Ressource wieder freigeben muss, sobald dieser fertig ist. Ein Semaphore hat eine feste Anzahl
an Plätzen, die auf eine Ressource zugreifen können. Ist dieses Maximum erreicht, müssen die darauffolgenden Threads solange in
der Warteschlange warten, bis ein Platz wieder frei wird. Desweiteren kann ein Platz, der von einem Thread reserviert wurde
von einem völlig anderen Thread wieder freigegeben werden. Dies erfolgt über einen internen Zähler. ein Thread, der mit 
.acquire() einen Platz reserviert hat, darf direkt weiterarbeiten, wenn dieser Zähler > 0 ist. dieser Zähler wird dann um 1
verringert. Sollte der Zähler bei == 0 sein, so muss der Thread warten. mit .release() signalisiert ein Thread, dass er mit
seiner Aufgabe fertig ist und der Zähler wird wieder um 1 erhöht. Somit sind Semaphoren weitaus flexibler als synchronized,
was die Koordination von Threads auf gemeinsam genutzten Ressourcen angeht.
___

# Warum verwendet  das Ping-Pong Beispiel ein Semaphor?
___
Das Semaphor dient als Synchronisationsbrücke. der Synchrone Main Thread wird mit .acquire() der einzige existierende Permit
erteilt. Dadurch beginnt das Spiel zwischen den beiden Actors. Da das spiel über den seperaten Threadpool des ExecutorService laufen,
läuft das Spiel im Hintergrund. Aufgrund des zweiten .acquire() wird erneut ein Permit angefragt, welcher aktuell noch nicht frei ist.
Genau aus diesem Grund wird der Main Thread blockiert und muss warten, sodass das Spiel im Hintergrund durchlaufen kann.
Erst wenn das Spiel komplett durchgeführt wird, gibt der Referee den Permit mit .release() wieder frei. Dadurch kann der Main
Thread sich erst sauber beenden. Der Semaphor hält somit den Main Thread solange am laufen, bis das Spiel beendet wird.
___

# Ping-Pong ohne Semaphor:
___
Führt man das Ping-Pong-Beispiel ohne Semaphor aus, so lässt sich beobachten, dass das Spiel gar nicht erst beginnt, somit
auch nicht zuende geführt wird. das Programm terminiert direkt mit "Process finished with exit code 0". Das bedeutet,
dass das Programm zwar erfolgreich terminiert wird, allerdings das Spiel vollständig ignoriert wird. wie s.o. schon bei Semaphor
erwähnt, liegt dass daran, dass ohne Semaphor der Main-User-Thread nicht aktiv gehalten wird. Da dieser keine wirkliche Aufgabe
besitzt, sondern nur dazu dient, das Spiel über einen seperaten Threadpool am laufen zu halten, beendet das Programm direkt,
da die JVM das Spiel im Hintergrund laufen lässt solange der Main Thread läuft. Terminiert dieser User Thread direkt, so werden
die Hintergrund Threads direkt mitgeschlossen.
___

# Ping-Pong als User-Thread
___
Da die JVM erst terminiert, wenn keine User-Threads mehr aktiv sind, läuft das Programm weiter, da bei dem Actor Framework 
der Threadpool des ExecutorService signalisiert bekommt, dass es sich um einen User-Thread handelt, wird die JVM am laufen gehalten.
Die Work Threads werden also wie User-Threads behandelt und warten auf die nächste Aufgabe. der Threadpool des ExecutorService 
beendet sich also nicht automatisch, dieser muss bei User-Threads manuell mit .shutdown() beendet werden.
___

# Main Thread zum schlafen bringen
___
bringt man den Main Thread zum schlafen, gibt man den Daemon-Threads zeit, das spiel zueende zu bringen. Es geschiet genau
das "am laufen halten" wie mit semaphor. der Main-User-Thread ist während das Spiel im Hintergrund läuft noch aktiv und somit
wird die JVM nicht beendet, das Spiel kann bis zum Schluss durchgeführt werden und wenn der Main-Thread wieder aufwacht
ist seine Aufgabe erledigt und terminiert das Programm.
___
