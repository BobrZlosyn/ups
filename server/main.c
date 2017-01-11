#if defined(_WIN32)
	#define  WIN               /* WIN for Winsock and BSD for BSD sockets*/
#else 
	#define  BSD  
#endif

/*----- Include files ---------------------------------------------------------*/
#include <stdio.h>          /* Needed for printf()*/
#include <string.h>         /* Needed for memcpy() and strcpy()*/
#include <stdlib.h>         /* Needed for exit()*/
#include <errno.h>
#include <pthread.h>
#include <unistd.h>
#include <semaphore.h>

#include "players.h"
#include "room.h"
#include "rooms.h"
#include "player.h"
#include "decodeMessage.h"
#include "serverFunctions.h"
#include "fileWriter.h"

#ifdef WIN
  #include <windows.h>      /* Needed for all Winsock stuff*/
#endif
#ifdef BSD
  #include <sys/types.h>    /* Needed for sockets stuff*/
  #include <netinet/in.h>   /* Needed for sockets stuff*/
  #include <sys/socket.h>   /* Needed for sockets stuff*/
  #include <arpa/inet.h>    /* Needed for sockets stuff*/
  #include <fcntl.h>        /* Needed for sockets stuff*/
  #include <netdb.h>        /* Needed for sockets stuff*/
#endif

/*----- Defines ---------------------------------------------------------------*/
#define  PORT_NUM   1234    /* Arbitrary port number for the server*/

sem_t listInFirst;
sem_t listInRoom;
PLAYERS *first;
ROOMS *firstRoom = NULL;
ENV *environment = NULL;

struct data_for_thread {
	int 				 connect_s;
	struct sockaddr_in   client_addr;     
	struct in_addr       client_ip_addr;  
};

PLAYERS *handle_lost_contact(PLAYERS *first, int socket, char *sendMsg);

/*
	zavirani navazujiciho socketu
*/
int close_welcome_socket(int welcome_s){
	int retcode;
	
	#ifdef WIN
	  retcode = closesocket(welcome_s);
	  if (retcode < 0) {
	  	print_error(environment,"welcome closesocket() failed");
	    exit(-1);
	  }
	#endif
	
	#ifdef BSD
	  retcode = close(welcome_s);
	  
	  if (retcode < 0)  {
	  	print_error(environment,"welcome close() failed");
	    exit(-1);
	  }
	#endif
	
	return 0;
}

/*
	zavirani socketu pro spojeni s klientem
*/
int close_connect_socket(int connect_s){
	int retcode = 0;
	
	#ifdef WIN
	  retcode = closesocket(connect_s);
	  
	  if (retcode < 0) {
	  	print_error(environment,"closesocket() failed");
	  }
	#endif
	
	#ifdef BSD
	  retcode = close(connect_s);
	  if (retcode < 0) {
	  	print_error(environment,"close() failed");
	  }
	#endif
	
	return retcode;
}

/*
	dokoncovani zpravy o utoku pro hrace
*/
void completeAttackAction(int playingID, int playerID, char *message) {
	
	if(playingID == playerID) {
		char newPokus [sizeof(message)] = "1;;";
		strcpy(newPokus + 3, message);
		strcpy(message, newPokus);	
	}else {
		char newPokus [sizeof(message)] = "0;;";
		strcpy(newPokus + 3, message);
		strcpy(message, newPokus);
	}
}

/*
	odesilani zpravy na klienta
*/
int sendMessage(char *message_to_send, int connect_s){
	int retcode = 0;
	char log[200];
	char message[200];
	
	sprintf(message, "%s\n", message_to_send);
	sprintf(log, "posilam na socket %d zpravu %s", connect_s, message_to_send);
	print_log(environment, log);
	retcode = send(connect_s, message, (strlen(message_to_send) + 1), 0);	
	if (retcode < 0) {
		print_error(environment,"send() failed");
	    return -1;
	}
	
	environment->MESSAGE_SEND_COUNT++;
	environment->BYTES_SEND_COUNT += strlen(message_to_send) + 1;
	
	return 0;
}

int attack_action(PLAYERS *first, MESSAGE *msg, char *sendMsg) {
	PLAYERS *players = find_player(first, msg->playerID);
	if (players == NULL) {
		sprintf(sendMsg, "<E;chyba ve zpracovani hrace>");
		return 1;
	}
	
	ROOM *room = players->room;
	if (room == NULL){
		sprintf(sendMsg, "<E;chyba ve zpracovani mistnosti>");
		return 2;
	}
	
	if (room->player2 == NULL){
		sprintf(sendMsg, "<E;chyba dalsi hrac neexistuje>");
		return 3;
	}
	
	if(room->isPlayingID == msg->playerID){
		
		if (room->isPlayingID == room->player1->player->playerID) {
			room->isPlayingID = room->player2->player->playerID;
		} else {
			room->isPlayingID = room->player1->player->playerID;
		}		
		
		/*order sending*/
		sprintf(sendMsg, "<O;%d>", room->isPlayingID);
		sendMessage(sendMsg, room->player1->player->socket);
		sendMessage(sendMsg, room->player2->player->socket);
		
		
		sprintf(sendMsg, "<A;0;;0;;0;;%s>", msg->data);
		if(room->isPlayingID == room->player1->player->playerID){
			sendMessage(sendMsg, room->player1->player->socket);
		}else{
			sendMessage(sendMsg, room->player2->player->socket);
		}
				
		sprintf(sendMsg, "<A;1;;0;;0;;%s>", msg->data);
			
	}else{
		sprintf(sendMsg, "<E;hrac neni na rade>");	
	}
	
	return 0;
}

int quit_action(PLAYERS *player, char *sendMsg){
	
	if(player == NULL){
		sprintf(sendMsg, "<E; hrac nebyl nalezen>");
		return -1;
	}
	
	ROOM *room = player->room;
	if (room != NULL) {
		PLAYERS *player1 = room->player1;
		PLAYERS *player2 = room->player2;
		
		environment->ROOM_COUNT--;
		if (player1 != NULL && player1->player->playerID != player->player->playerID){	
			sprintf(sendMsg, "<R;%d>", player1->player->playerID);
			sendMessage(sendMsg, room->player1->player->socket);
			room->player2 = NULL;
			
		}else{
			
			if (player2 != NULL && player2->player->playerID != player->player->playerID) {
				sprintf(sendMsg, "<R;%d>", player2->player->playerID);
				sendMessage(sendMsg, room->player2->player->socket);
				room->player1 = NULL;	
			}
		}
		
		if (player1 != NULL) {
			first = remove_player(first, player1->player->playerID);			
		}
		
		if (player2 != NULL) {
			first = remove_player(first, player2->player->playerID);
		}
	}else {
		first = remove_player(first, player->player->playerID);
	}
		
	return -1;
	
}

int start_game(PLAYERS *player, char *sendMsg) {
	if (player == NULL) {
		sprintf(sendMsg, "<E;hrace nelze najit>");
		return 1;
	}
	
	
	ROOM *room = find_free_room(first, player->player->playerID);
	if (room == NULL) {		
						
		room = create_room(player);
		sprintf(sendMsg, "<W; cekani na hrace>");
		environment->ROOM_COUNT++;
		environment->TOTAL_ROOM_COUNT++;
	} else {
		add_second_player(player, room);
		
		/*order sending*/
		generate_starting_id(room); 
		sprintf(sendMsg, "<O;%d>", room->isPlayingID);
		sendMessage(sendMsg, room->player1->player->socket);
		sendMessage(sendMsg, room->player2->player->socket);
		
		/* ship info sending*/
		sprintf(sendMsg, "<S;%s>", room->player2->player->shipInfo);
		sendMessage(sendMsg, room->player1->player->socket);
		
		sprintf(sendMsg, "<S;%s>", room->player1->player->shipInfo);
	}
	return 0;
}

int lost_game(PLAYERS *player, char *sendMsg) {
	if(player == NULL){
		sprintf(sendMsg, "<E; hrac nebyl nalezen>");
		return 1;
	}
	
	
	ROOM *room = player->room;
	if (room != NULL) {
		PLAYERS *player1 = room->player1;
		PLAYERS *player2 = room->player2;
		
		/*posle zpravu hraci na prvni pozici*/					
		if (player1 != NULL && player1->player->playerID != player->player->playerID){	
			sprintf(sendMsg, "<R;%d>", player1->player->playerID);
			sendMessage(sendMsg, player1->player->socket);
			free(player1->room);
			player1->room = NULL;
		} else{	
			if (player2 != NULL && player2->player->playerID != player->player->playerID) {
				sprintf(sendMsg, "<R;%d>", player2->player->playerID);
				free(player2->room);
				player2->room = NULL;
				sendMessage(sendMsg, player2->player->socket);	
			}
		}
		
		environment->ROOM_COUNT--;
		free(room);
		player->room = NULL;
		if (player1 != NULL) {
			first = remove_player(first, player1->player->playerID);			
		}
		
		if (player2 != NULL) {
			first = remove_player(first, player2->player->playerID);
		}
			
	}
	return 0;
}

int modul_status(PLAYERS *player, char *sendMsg, MESSAGE *msg){
	if(player == NULL){
		sprintf(sendMsg, "<E;hrace nelze najit>");
		return 1;
	}
	
	ROOM *room = player->room;
	if (room == NULL){
		return 1;
	}
	
	sprintf(player->player->modulInfo, "%s", msg->data);
	if(room->player1 != NULL && room->player1->player->playerID == msg->playerID){
		if(room->player2 != NULL){
			sprintf(sendMsg, "<M;%s>", msg->data);
			sendMessage(sendMsg, room->player2->player->socket);
			sprintf(sendMsg, "<E; neni treba zpracovavat>");
		}
		return 1;			
	}
	
	if(room->player2 != NULL && room->player2->player->playerID == msg->playerID){
		if(room->player1 != NULL){
			sprintf(sendMsg, "<M;%s>", msg->data);
			sendMessage(sendMsg, room->player1->player->socket);
			sprintf(sendMsg, "<E; neni treba zpracovavat>");
		}
		return 1;
	}
	
	return 0;
}

int reconnection(PLAYERS *player, char *sendMsg, MESSAGE *msg, int socket, char *ip_client){
	if(player == NULL){
		first = add_player(first, ip_client, socket);
		strcpy(first->player->shipInfo, msg->data);	
		sprintf(sendMsg, "<I;%d>", first->player->playerID);
		return 0;
	}
	
	player->player->socket = socket;
	player->isAvaible = 1;
	int id = player->player->playerID;
	sprintf(sendMsg, "<B;%s>", msg->data);
	
	ROOM *room = player->room;
	if (room == NULL){
		return 0;
	}
	
	if(room->player1 != NULL && room->player1->player->playerID == id){
		if(room->player2 != NULL){
			sendMessage(sendMsg, room->player2->player->socket);
		}
		return 0;			
	}
	
	if(room->player2 != NULL && room->player2->player->playerID == id){
		if(room->player1 != NULL){
			sendMessage(sendMsg, room->player1->player->socket);
		}
	}
	
	return 0;
}


int doActionByMessage(struct message *msg, char *ip_client, char *sendMsg, int socket) {
	sprintf(sendMsg, " ");
	PLAYERS *player = find_player(first, msg->playerID);
	if(player == NULL && msg->playerID != 0 && msg->action != 'C'){
		print_error(environment, "hrac nebyl rozpoznan!");
		return 0;
	}
	
	switch(msg->action){
		case 'C':{
			first = add_player(first, ip_client, socket);
			strcpy(first->player->shipInfo, msg->data);	
			sprintf(sendMsg, "<I;%d>", first->player->playerID);
			
		} break;
		
		case 'M': modul_status(player, sendMsg, msg); break;
		
		case 'Q': return quit_action(player, sendMsg);
		
		case 'G': start_game(player, sendMsg); break;
		
		case 'A': attack_action(first, msg, sendMsg); break;
		
		case 'L': lost_game(player, sendMsg); break;
		
		/*case 'R': reconnection(player,sendMsg, msg, socket, ip_client); break; - reconnection*/
		
		default :{			
			sprintf(sendMsg, "<E;action not found>");
			break;
		}
	}

	return 0;	
}

int initializeWelcomeSocket(struct sockaddr_in server_addr){
	int welcome_s, retcode;
	welcome_s = socket(AF_INET, SOCK_STREAM, 0);
    if (welcome_s < 0) {
    	print_error(environment, "socket() failed");
	    exit(-1);
	}
	
	retcode = bind(welcome_s, (struct sockaddr *)&server_addr, sizeof(server_addr));
	if (retcode < 0) {
		print_error(environment, "bind() failed");
	    exit(-1);
	}
	
	return welcome_s;
}

void *user_thread(void *t_param){
	char in_buf[200];    /* Input buffer for data*/
	char out_buf[200];   /* Output buffer for data*/
	char logs[200];
	struct data_for_thread *param = (struct data_for_thread *)t_param;	
	int retcode;
	int socket = param->connect_s;
	environment->PLAYER_COUNT++;
	environment->TOTAL_PLAYER_COUNT++;
	/*reconnection
	#ifdef WIN
		DWORD timeout;
	#endif
	#ifdef BSD
		struct timeval timeout;
		timeout.tv_usec = 0;
		int SOCKET_ERROR = -1
	#endif
	int oldTimeout = -1;
	*/
	sprintf(logs, "pripojila se nova aplikace na socket %d", socket);
	print_log(environment, logs);
	while(1){
		
	/* reconnection
		if (oldTimeout != environment->TIMEOUT) {
			oldTimeout = environment->TIMEOUT;
			#ifdef WIN
				timeout = oldTimeout * 1000;
				setsockopt(socket, SOL_SOCKET, SO_RCVTIMEO, (char*)&timeout, sizeof(timeout));
			#endif
			#ifdef BSD
				timeout.tv_sec = oldTimeout;
				setsockopt(s, SOL_SOCKET, SO_RCVTIMEO, &timeout, sizeof(timeout));
			#endif
			
		}
		*/
		
		MESSAGE *message = create_message();
	
		retcode = recv(socket, in_buf, sizeof(in_buf), 0);
		if (retcode == SOCKET_ERROR){
			sprintf(logs, "recv() failed on socket %d", socket);
			print_error(environment, logs);
			sem_wait(&listInFirst);
			first = handle_lost_contact(first, socket, out_buf);
			sem_post(&listInFirst);
			
			break;
		}
		

		retcode = decode_message(in_buf, message, 4);
		if (retcode > 0) {
			sprintf(logs, "zpravu nelze zpracovat %s |socket %d", in_buf, socket);
			print_error(environment, logs);
			free(message);
			continue;	
		}
		
		sprintf(logs, "recv <%c;%d;%s>", message->action, message->playerID, message->data);
		print_log(environment, logs);
		
		environment->MESSAGE_RECV_COUNT++;
		environment->BYTES_RECV_COUNT += message->bytes;
		
		/* 
		reconnection
		if (message->action == 'X' ) {
			 sprintf(out_buf, "<X;I am here!>");
		}else {
			sem_wait(&listInFirst);
			retcode = doActionByMessage(message, inet_ntoa(param->client_ip_addr), out_buf, socket);		
			sem_post(&listInFirst);	
		}
		*/
		
		sem_wait(&listInFirst);
		retcode = doActionByMessage(message, inet_ntoa(param->client_ip_addr), out_buf, socket);		
		sem_post(&listInFirst);
			
		if(retcode < 0){
			free(message);
			break;
		}
		
		retcode = sendMessage(out_buf, socket);	
		free(message);		
		if(retcode < 0){
			sprintf(logs, "send() failed on socket %d", socket);
			print_error(environment, logs);
			sem_wait(&listInFirst);
			first = handle_lost_contact(first, socket, out_buf);
			sem_post(&listInFirst);
			
			break;
		}
	}
	
	environment->PLAYER_COUNT--;
	sprintf(logs, "zaviram socket s cislem %d",socket);
	close_connect_socket(socket);
	print_log(environment, logs);
	return NULL;
}

void clearApp(int dummy) {
    clear_players(first);
	print_variables(environment);		
	free(environment);
	exit(42);
}



/*===== Main program ==========================================================*/
int main(int argc, char *argv[]) {
	
	#ifdef WIN
		WORD wVersionRequested = MAKEWORD(1,1);       /*Stuff for WSA functions*/
		WSADATA wsaData;                              /* Stuff for WSA functions*/
	#endif
		int                  welcome_s;       /* Welcome socket descriptor*/
		struct sockaddr_in   server_addr;     /* Server Internet address*/
		int                  connect_s;       /* Connection socket descriptor*/
		int                  addr_len;        /* Internet address length*/
		pthread_t 			 thread;
		struct data_for_thread	data;
		struct sockaddr_in   client_addr;     /* Client Internet address*/
		
	#ifdef WIN
		/* This stuff initializes winsock*/
		
		WSAStartup(wVersionRequested, &wsaData);
	#endif
	environment = create_env();
	sem_init(&listInFirst,0,1);
	pthread_create(&thread, NULL, &server_env, &data);
	signal(SIGINT, clearApp);	
	create_folder();
	first = NULL;
	
	if(argc == 2) {
		validate_port(argv[1], environment);
	}
	
	/*Fill-in server (my) address information and bind the welcome socket*/
	server_addr.sin_family = AF_INET;                 /* Address family to use*/	
	server_addr.sin_addr.s_addr = htonl(INADDR_ANY);  /* Listen on any IP address*/
	
	/*- AF_INET is Address Family Internet and SOCK_STREAM is streams*/
	while (1) {
		server_addr.sin_port = htons(environment->SERVER_PORT); 
		welcome_s = initializeWelcomeSocket(server_addr);	
		if(listen(welcome_s, 5) < 0){
			print_error(environment, "listen error");
			continue;
		}
		
		/* Accept a connection.  The accept() will block and then return with*/
		/* connect_s assigned and client_addr filled-in.*/
		addr_len = sizeof(client_addr);
		connect_s = accept(welcome_s, (struct sockaddr *)&client_addr, &addr_len);
		if (connect_s < 0) {
			print_error(environment, "accept() failed");
			continue;
		}
		/* Copy the four-byte client IP address into an IP address structure*/
		memcpy(&data.client_ip_addr, &client_addr.sin_addr.s_addr, 4);
		data.client_addr = client_addr;
		data.connect_s = connect_s;
		
		pthread_create(&thread,NULL,user_thread,&data);
		
		close_welcome_socket(welcome_s);	 
	}
		
	clearApp(0);			
	#ifdef WIN
	  /* Clean-up winsock*/
	  WSACleanup();
	#endif
	
	/* Return zero and terminate*/
	return(0);
}


/*
	funkce pro vlakno, ktere se stara o prostredi serveru
*/
void *server_env(void *arg){
	printf("******  Server byl zapnut ******\n");
	write_line("******  Server byl zapnut ******", LOG_FILE);
	printf("$#> ");
	while (1) {
		functions(getline(), first, environment);
		printf("$#> ");	
	}
}



PLAYERS *handle_lost_contact(PLAYERS *first, int socket, char *sendMsg){
	
	PLAYERS *player = find_player_by_socket(first, socket);
	if (player == NULL) {
		return first;
	}
	
	/*odstranovani hrace*/
	ROOM *room = player->room;
	if(room == NULL) {
		return remove_player(first, player->player->playerID);
	}
	
	PLAYERS *player1 = room->player1;
	PLAYERS *player2 = room->player2;
	
	first = remove_player(first, player1->player->playerID);
	first = remove_player(first, player2->player->playerID);
	if (player1 != NULL && player1->player->playerID != player->player->playerID){
		sprintf(sendMsg, "<R;%d>", player1->player->playerID);
		sendMessage(sendMsg, player1->player->socket);
	}
	
	if (player2 != NULL && player2->player->playerID != player->player->playerID){
		sprintf(sendMsg, "<R;%d>", player2->player->playerID);
		sendMessage(sendMsg, player2->player->socket);
	}
	
	if (player1 != NULL) {
		first = remove_player(first, player1->player->playerID);			
	}
	
	if (player2 != NULL) {
		first = remove_player(first, player2->player->playerID);
	}
	
	return first;	
}


