#ifndef FILEWRITER_H
#define FILEWRITER_H

#define LOG_FILE "communication.log"
#define ERROR_FILE "errors.log"
#define STATISTIC_FILE "statistic.log"
#define LOG_FOLDER "./ups_logs"

void write_line(char *line, char *file);
void create_folder(); 

#endif
