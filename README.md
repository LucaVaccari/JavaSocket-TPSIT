# JavaSocket-TPSIT

Svolgimento degli esercizi contenuti in "consegne.pdf".

### Premesse

Essendo la realizzazione dei singoli esercizi in se particolarmente semplice, è stata realizzata un'architettura che
permette lo svolgimento di tutti e otto all'interno dello stesso programma.

Particolare enfasi è stata quindi posta sulla struttura, motivo per cui sono stati realizzati solo i primi tre esercizi.

### Guida all'utilizzo

Una volta eseguito il server mette immediatamente a disposizione i propri servizi, mentre il client non si connette
all'avvio. È invece necessario eseguire sul client il commando "connect" oppure "conn" seguito dall'indirizzo del
server (per ora non è possibile cambiare la porta).

Per poter usufruire dei servizi bisogna però essere registrati sul server con uno username univoco. Il processo di
registrazione si effettua con il comando "login" o "log" seguito dallo username scelto. In caso di username duplicato
verrà mandato un messaggio di errore e la procedura sarà annullata. Uno username ritorna disponibile (e viene quindi
cancellato) ogni qual volta il client si disconnetta
(alla prossima connessione dovrà registrarsi di nuovo) o quando il server viene interrotto.

Con il comando "help" è possibile avere una lista di tutti i comandi eseguibili, sia sul client che sul server.

#### Architettura interna

Sia il client che il server presentano tre thread: uno principale che si occupa di far partire gli altri due, uno per la
lettura dell'input da console e uno per la comunicazione sulla rete.

###### Lettura da console

ClientMain e ServerMain fanno partire il thread ConsoleListener, che legge da tastiera tramite uno Scanner e invia la
lettura alle classi derivate da CommandProcessor. Queste ultime analizzano la riga di comando e si comportano di
conseguenza, gestendo gli eventuali casi d'inserimento errato dei comandi.

###### Comunicazione sulla rete

Ogni messaggio è mandato in formato json (conversioni eseguite dalla classe JsonSerializer), ed è costituito da
un'istanza della classe Message, contenente il tipo di messaggio, il servizio (da 1 a 8 in base all'esercizio, 0 per i
messaggi generici), il nome utente (utile principalmente per fini di debug, ma non solo), e informazioni aggiuntive
salvate in json.

Dal punto di vista del client la connessione è semplice, in quanto è presente un solo thread che gestisce l'input
(reindirizzando il message a varie classi di gestione in base al servizio) e l'output verso il server (tramite un
semplice metodo send).

Sul server sono gestiti più utenti, per cui è presente un thread di accettazione (ConnectionManager) che gestisce i vari
utenti e il loro essere registrati o meno e, per ciascuno di essi, un thread per lo scambio dei messaggi (di
funzionalità analoga a quello del client).

Ogni servizio gestisce i dati in maniera differente per ogni utente.

###### TODO:

- client actions/commands/messages
	- chat
- server actions/commands/messages
	- enable, disable
	- kick
