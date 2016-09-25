#define  WIN               /* WIN for Winsock and BSD for BSD sockets*/

/*----- Include files ---------------------------------------------------------*/
#include <stdio.h>          /* Needed for printf()*/
#include <string.h>         /* Needed for memcpy() and strcpy()*/
#include <stdlib.h>         /* Needed for exit()*/
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


int addConnectionToArray(int connection, int * connections, int length ){
	int i = 0;
	for(i = 0; i < length; i++){
		
		/* pripojeni nalezeno - nepridava se */
		if(connections[i] == connection){
			return 1;
		}
		
		/* pole je prazdne a proto se muze priradit */
		if(connections[i] == -1){
			connections[i] = connection;
			return 0;
		}
		
	}
	
	/* pole je plne */
	return 2;
}

int clearConnectionArray(int * connections, int length){
	int i;
	for (i = 0; i < length; i++){
		connections[i] = -1;
	}
	return 0;
}

int printArray(int * array, int length){
	int i;
	for (i = 0; i < length; i++){
		printf("%d \n",array[i]);
	}
	return 0;
}


int closeSockets(int welcome_s, int connect_s){
	int retcode;
	
	#ifdef WIN
	  retcode = closesocket(welcome_s);
	  if (retcode < 0)
	  {
	    printf("*** ERROR - closesocket() failed \n");
	    exit(-1);
	  }
	  retcode = closesocket(connect_s);
	  
	  printf("zacatek %d ", connect_s);
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
	  retcode = close(connect_s);
	  if (retcode < 0)
	  {
	    printf("*** ERROR - close() failed \n");
	    exit(-1);
	  }
	#endif
	
	return 0;
}

int sendMessage(char * receivedMessage, int connect_s){
	char out_buf[4096];   /* Output buffer for data*/
	int retcode = 0;
	
	if (strncmp(receivedMessage, "connect", strlen("connect")) == 0){
		strcpy(out_buf, "success");
		retcode = send(connect_s, out_buf, (strlen(out_buf) + 1), 0);
	} else{
		strcpy(out_buf, "fail");
		retcode = send(connect_s, out_buf, (strlen(out_buf) + 1), 0);
	}
	
	
	if (retcode < 0) {
	    printf("*** ERROR - send() failed \n");
	    exit(-1);
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



/*===== Main program ==========================================================*/
int main()
{
	
#ifdef WIN
  WORD wVersionRequested = MAKEWORD(1,1);       /*Stuff for WSA functions*/
  WSADATA wsaData;                              /* Stuff for WSA functions*/
#endif
  int                  welcome_s;       /* Welcome socket descriptor*/
  struct sockaddr_in   server_addr;     /* Server Internet address*/
  int                  connect_s;       /* Connection socket descriptor*/
  struct sockaddr_in   client_addr;     /* Client Internet address*/
  struct in_addr       client_ip_addr;  /* Client IP address*/
  int                  addr_len;        /* Internet address length*/
  
  char                 in_buf[4096];    /* Input buffer for data*/
  int                  retcode;         /* Return code*/
  int 				   connections[10];
#ifdef WIN
  /* This stuff initializes winsock*/
  
  WSAStartup(wVersionRequested, &wsaData);
#endif

 /* >>> Step #2 <<<
   Fill-in server (my) address information and bind the welcome socket*/
server_addr.sin_family = AF_INET;                 /* Address family to use*/
server_addr.sin_port = htons(PORT_NUM);           /* Port number to use*/
server_addr.sin_addr.s_addr = htonl(INADDR_ANY);  /* Listen on any IP address*/

clearConnectionArray(connections, 10);
printArray(connections, 10);

 /* >>> Step #1 <<<
   Create a welcome socket
     - AF_INET is Address Family Internet and SOCK_STREAM is streams*/
  	  
	while(1){
		
		welcome_s = initializeWelcomeSocket(server_addr);	
 		listen(welcome_s, 1);
 		
			  /* >>> Step #4 <<<*/
			  /* Accept a connection.  The accept() will block and then return with*/
			  /* connect_s assigned and client_addr filled-in.*/
			  printf("Waiting for accept() to complete... \n");
			  addr_len = sizeof(client_addr);
			  connect_s = accept(welcome_s, (struct sockaddr *)&client_addr, &addr_len);
			  printf("stred %d \n", connect_s);
			  if (connect_s < 0)
			  {
			    printf("*** ERROR - accept() failed \n");
			    exit(-1);
			  }
			
			  /* Copy the four-byte client IP address into an IP address structure*/
			  memcpy(&client_ip_addr, &client_addr.sin_addr.s_addr, 4);
			
			  /* Print an informational message that accept completed*/
			  printf("Accept completed (IP address of client = %s  port = %d) \n",
			    inet_ntoa(client_ip_addr), ntohs(client_addr.sin_port));
			
			  /* >>> Step #6 <<<*/
			  /* Receive from the client using the connect socket*/
			  retcode = recv(connect_s, in_buf, sizeof(in_buf), 0);
			  if (retcode < 0)
			  {
			    printf("*** ERROR - recv() failed \n");
			    exit(-1);
			  }
			  printf("Received from client: %s \n", in_buf);			  
			  
			  sendMessage(in_buf, connect_s);		  		
			  closeSockets(welcome_s, connect_s);
			  
		}
				
		#ifdef WIN
		  /* Clean-up winsock*/
		  WSACleanup();
		#endif

  /* Return zero and terminate*/
  return(0);
}





