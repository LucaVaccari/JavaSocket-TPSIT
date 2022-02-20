# JavaSocket-TPSIT

Socket exercises with Java, taken from "consegne.pdf"

Both the client and the server have a command line interface.

Brainstorming:

- comandi client per connettersi ai vari servizi
- messaggi interpretati dal client, convertiti in json e inviati
- comando help contestualizzato
- utenti identificati da uno username univoco
- messaggio JSON formato da:
	- nome utente (univoco)
	- servizio attuale (ZERO servizio base)
	- azione da compiere
	- parametri
- classe per la crittografia
- comandi server
	- kick
	- stop
	- help
	- enable/disable

TODO:

- client actions/commands/messages
	- aliquot
	- guessTheNumber:
		- start game
		- guess
	- chat
	- help
- server actions/commands/messages
	- enable, disable
	- kick
	- help
