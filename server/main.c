#define  WIN               /* WIN for Winsock and BSD for BSD sockets*/

/*----- Include files ---------------------------------------------------------*/
#include <stdio.h>          /* Needed for printf()*/
#include <string.h>         /* Needed for memcpy() and strcpy()*/
#include <stdlib.h>         /* Needed for exit()*/
#include <errno.h>
#include <pthread.h>
#include <unistd.h>

#include "players.h"
#include "room.h"
#include "rooms.h"
#include "player.h"
#include "decodeMessage.h"

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


PLAYERS *first = NULL;
ROOMS *firstRoom = NULL;
struct data_for_thread {
	int 				 connect_s;
	struct sockaddr_in   client_addr;     
	struct in_addr       client_ip_addr;  
};


int close_welcome_socket(int welcome_s){
	int retcode;
	
	#ifdef WIN
	  retcode = closesocket(welcome_s);
	  if (retcode < 0)
	  {
	    printf("*** ERROR - closesocket() failed \n");
	    exit(-1);
	  }
	#endif
	
	#ifdef BSD
	  retcode = close(welcome_s);
	  
	  if (retcode < 0)
	  {
	    printf("*** ERROR - close() failed \n");
	    exit(-1);
	  }
	#endif
	
	return 0;
}

int close_connect_socket(int connect_s){
	int retcode = 0;
	
	#ifdef WIN
	  retcode = closesocket(connect_s);
	  
	  if (retcode < 0) {
	    printf("*** ERROR - closesocket() failed \n");
	  }
	#endif
	
	#ifdef BSD
	  retcode = close(connect_s);
	  if (retcode < 0) {
	    printf("*** ERROR - close() failed \n");
	  }
	#endif
	
	return retcode;
}

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

int sendMessage(char *message_to_send, int connect_s){
	int retcode = 0;
	retcode = send(connect_s, message_to_send, (strlen(message_to_send) + 1), 0);
	
	if (retcode < 0) {
	    printf("*** ERROR - send() failed \n");
	    return -1;
	}
	
	return 0;
}


int attack_action(PLAYERS *first, MESSAGE *msg, char *sendMsg) {
	printf("attack \n");
	PLAYERS *players = find_player(first, msg->playerID);
	if (players == NULL) {
		printf("error \n");
		sprintf(sendMsg, "<E;chyba ve zpracovani hrace>\n");
		return 1;
	}
	
	ROOM *room = players->room;
	if (room == NULL){
		printf("error \n");
		sprintf(sendMsg, "<E;chyba ve zpracovani mistnosti>\n");
		return 2;
	}
	
	if (room->player2 == NULL){
		printf("error \n");
		sprintf(sendMsg, "<E;chyba dalsi hrac neexistuje>\n");
		return 3;
	}
	
	if(room->isPlayingID == msg->playerID){
		
		if (room->isPlayingID == room->player1->player->playerID) {
			room->isPlayingID = room->player2->player->playerID;
		} else {
			room->isPlayingID = room->player1->player->playerID;
		}		
		
		/*order sending*/
		sprintf(sendMsg, "<O;%d>\n", room->isPlayingID);
		sendMessage(sendMsg, room->player1->player->socket);
		sendMessage(sendMsg, room->player2->player->socket);
		
		
		sprintf(sendMsg, "<A;0;;0;;0;;%s>\n", msg->data);
		if(room->isPlayingID == room->player1->player->playerID){
			sendMessage(sendMsg, room->player1->player->socket);
		}else{
			sendMessage(sendMsg, room->player2->player->socket);
		}
				
		sprintf(sendMsg, "<A;1;;0;;0;;%s>\n", msg->data);
			
	}else{
		printf("error \n");
		sprintf(sendMsg, "<E;hrac neni na rade>\n");	
	}
	
	return 0;
}

int doActionByMessage(struct message *msg, char *ip_client, char *sendMsg, int socket) {
	sprintf(sendMsg, " ");
	PLAYERS *player = find_player(first, msg->playerID);
	
	switch(msg->action){
		case 'C':{
			first = add_player(first, ip_client, socket);
			strcpy(first->player->shipInfo, msg->data);	
			sprintf(sendMsg, "<I;%d>\n", first->player->playerID);
			
		} break;
		
		case 'M':{
			if(player == NULL){
				printf("hrace %d nelze nalezt \n", msg->playerID);
				sprintf(sendMsg, "<E;hrace nelze najit>\n");
				break;
			}
			
			ROOM *room = player->room;
			if (room == NULL){
				break;
			}
			
			if(room->player1 != NULL && room->player1->player->playerID == msg->playerID){
				if(room->player2 != NULL){
					sprintf(sendMsg, "<M;%s>\n", msg->data);
					sendMessage(sendMsg, room->player2->player->socket);
					sprintf(sendMsg, " ");
				}
				break;				
			}
			
			if(room->player2 != NULL && room->player2->player->playerID == msg->playerID){
				if(room->player1 != NULL){
					sprintf(sendMsg, "<M;%s>\n", msg->data);
					sendMessage(sendMsg, room->player1->player->socket);
					sprintf(sendMsg, " ");
				}
				break;
			}
			
		} break;
		
		case 'Q':{
			printf("ukonceni \n");	
			
			if(player == NULL){
				printf("hrac nenalezen %d \n", msg->playerID);
				sprintf(sendMsg, "<E; hrac nebyl nalezen>");
				return -1;
			}
			
			ROOM *room = player->room;
			if (room != NULL) {
				PLAYERS *player1 = room->player1;
				if (player1 != NULL && player1->player->playerID != msg->playerID){	
					sprintf(sendMsg, "<R;%d>\n", player1->player->playerID);
					sendMessage(sendMsg, room->player1->player->socket);
					room->player2 = NULL;
				}else{
					PLAYERS *player2 = room->player2;
					if (player2 != NULL && player2->player->playerID != msg->playerID) {
						sprintf(sendMsg, "<R;%d>\n", player2->player->playerID);
						sendMessage(sendMsg, room->player2->player->socket);
						room->player1 = NULL;	
					}
				}
			}
			
			first = remove_player(first, msg->playerID);				
			return -1;
		} break;
		
		case 'G':{
			printf("game \n");
			ROOM *room = find_free_room(first, msg->playerID);
			if (room == NULL) {
				if(player == NULL){
					printf("hrace %d nelze nalezt \n", msg->playerID);
					sprintf(sendMsg, "<E;hrace nelze najit>\n");
					break;
				}
								
				room = create_room(player);
				sprintf(sendMsg, "<W; cekani na hrace>\n");
			} else {
				if(player == NULL){
					printf("hrace %d nelze nalezt \n", msg->playerID);
					sprintf(sendMsg, "<E;hrace nelze najit>\n");
					break;
				}
				
				add_second_player(player, room);
				
				/*order sending*/
				generate_starting_id (struct room *room); 
				sprintf(sendMsg, "<O;%d>\n", room->isPlayingID);
				sendMessage(sendMsg, room->player1->player->socket);
				sendMessage(sendMsg, room->player2->player->socket);
				
				/* ship info sending*/
				sprintf(sendMsg, "<S;%s>\n", room->player2->player->shipInfo);
				sendMessage(sendMsg, room->player1->player->socket);
				
				sprintf(sendMsg, "<S;%s>\n", room->player1->player->shipInfo);
			}
			
		} break;
		
		case 'A':attack_action(first, msg, sendMsg); break;
		
		case 'L':{
			printf("lost - surrrender %d \n", msg->playerID);	
			
			if(player == NULL){
				printf("hrac nenalezen %d \n", msg->playerID);
				sprintf(sendMsg, "<E; hrac nebyl nalezen>");
				break;
			}
			
			
			ROOM *room = player->room;
			if (room != NULL) {
				PLAYERS *player1 = room->player1;
									
				if (player1 != NULL && player1->player->playerID != msg->playerID){	
					sprintf(sendMsg, "<R;%d>\n", room->player1->player->playerID);
					sendMessage(sendMsg, room->player1->player->socket);
				}else{
					PLAYERS *player2 = room->player2;
					if (player2 != NULL && player2->player->playerID != msg->playerID) {
						sprintf(sendMsg, "<R;%d>\n", room->player2->player->playerID);
						sendMessage(sendMsg, room->player2->player->socket);	
					}
				}
				free(room);
			}
			
			
		} break;
		
		default :{
			printf("nenalezeno \n");
			sprintf(sendMsg, "<E;action not found>\n");
			return -1;
			break;
		}
	}

	return 0;	
}

int initializeWelcomeSocket(struct sockaddr_in server_addr){
	int welcome_s, retcode;
	welcome_s = socket(AF_INET, SOCK_STREAM, 0);
    if (welcome_s < 0) {
    	printf("*** ERROR - socket() failed \n");
	    exit(-1);
	}
	
	retcode = bind(welcome_s, (struct sockaddr *)&server_addr, sizeof(server_addr));
	if (retcode < 0) {
		printf("*** ERROR - bind() failed \n");
	    exit(-1);
	}
	
	return welcome_s;
}

void *user_thread(void *t_param){
	char                 in_buf[100];    /* Input buffer for data*/
	char 				 out_buf[100];   /* Output buffer for data*/
	struct data_for_thread *param = (struct data_for_thread *)t_param;	
	int retcode;
	int 				 socket = param->connect_s;
	
	while(1){
		MESSAGE *message = (MESSAGE *)malloc(sizeof(MESSAGE));
		/* Receive from the client using the connect socket*/
		retcode = recv(socket, in_buf, sizeof(in_buf), 0);
		if (retcode < 0) {
			printf("*** ERROR - recv() failed \n");
			break;
		}
	
		retcode = decode_message(in_buf, message, 4);
		if (retcode > 0) {
			free(message);
			continue;	
		}	  
		printf("player %d received %c --- %s \n", message->playerID, message->action, message->data);
		
		retcode = doActionByMessage(message, inet_ntoa(param->client_ip_addr), out_buf, socket);
		if(retcode == 0) {
			printf("send %s \n", out_buf);
			sendMessage(out_buf, socket);
		}
		
		free(message);
		
		if(retcode < 0){
			break;
		}	  	
	}
		  	
	printf("zaviram %d \n",socket);
	close_connect_socket(socket);
	return NULL;
}


/*===== Main program ==========================================================*/
int main() {
	
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
		struct in_addr       client_ip_addr;  /* Client IP address*/
		
	#ifdef WIN
		/* This stuff initializes winsock*/
		
		WSAStartup(wVersionRequested, &wsaData);
	#endif
	
	/* >>> Step #2 <<<
	Fill-in server (my) address information and bind the welcome socket*/
	server_addr.sin_family = AF_INET;                 /* Address family to use*/
	server_addr.sin_port = htons(PORT_NUM);           /* Port number to use*/
	server_addr.sin_addr.s_addr = htonl(INADDR_ANY);  /* Listen on any IP address*/
		
	/* >>> Step #1 <<<
	Create a welcome socket
 	- AF_INET is Address Family Internet and SOCK_STREAM is streams*/
	  
	while (1) {
		
		/*tisknuti pridanych uzivatelu*/
		print_players(first);
		welcome_s = initializeWelcomeSocket(server_addr);	
		listen(welcome_s, 5);
		
		/* Accept a connection.  The accept() will block and then return with*/
		/* connect_s assigned and client_addr filled-in.*/
		printf("Waiting for accept() to complete... \n");
		addr_len = sizeof(client_addr);
		connect_s = accept(welcome_s, (struct sockaddr *)&client_addr, &addr_len);
		if (connect_s < 0) {
			printf("*** ERROR - accept() failed \n");
			exit(-1);
		}
		/* Copy the four-byte client IP address into an IP address structure*/
		memcpy(&data.client_ip_addr, &client_addr.sin_addr.s_addr, 4);
		data.client_addr = client_addr;
		data.connect_s = connect_s;
		
		pthread_create(&thread,NULL,user_thread,&data);
		
		close_welcome_socket(welcome_s);	  
	}
				
	#ifdef WIN
	  /* Clean-up winsock*/
	  WSACleanup();
	#endif
	
	/* Return zero and terminate*/
	return(0);
}





