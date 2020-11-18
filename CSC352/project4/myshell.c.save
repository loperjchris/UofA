/*
* AUTHOR: Ruben Tequida
* FILE: myshell.c
* ASSIGNMENT: Project4: A Shell
* COURSE: CSc352, Spring 2020
* PURPOSE: This program creates a custom shell that takes in the commands exit and cd
* as well as any other linux command and processes those commands.
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <limits.h>
#include <time.h>
#include <sys/wait.h>

#define DELIMITERS " \t\n()<>|&;"

/*
* Purpose: Handles errors that are thrown from the parent of child processes.
* Error codes and description obtained from https://www-numi.fnal.gov/offline_software/srt_public_context/WebDocs/Errors/unix_system_errors.html
*/
void err_handle(char *error) {
	if (errno == EPERM) {
		printf("Operation not permitted: %s\n", error);
	} else if (errno == ENOENT) {
		printf("No such file or directory: %s\n", error);
	} else if (errno == ESRCH) {
		printf("No such process: %s\n", error);
	} else if (errno == EINTR) {
		printf("Interrupted system call: %s\n", error);
	} else if (errno == EIO) {
		printf("I/O error: %s\n", error);
	} else if (errno == ENXIO) {
		printf("No such device or address: %s\n", error);
	} else if (errno == E2BIG) {
		printf("Arg list too long: %s\n", error);
	} else if (errno == ENOEXEC) {
		printf("Exec format error: %s\n", error);
	} else if (errno == EBADF) {
		printf("Bad file number: %s\n", error);
	} else if (errno == ECHILD) {
		printf("No child processes: %s\n", error);
	} else if (errno == EAGAIN) {
		printf("Try again: %s\n", error);
	} else if (errno == ENOMEM) {
		printf("Out of memory: %s\n", error);
	} else if (errno == EACCES) {
		printf("Permission denied: %s\n", error);
	} else if (errno == EFAULT) {
		printf("Bad address: %s\n", error);
	} else if (errno == ENOTBLK) {
		printf("Block device required: %s\n", error);
	} else if (errno == EBUSY) {
		printf("Device or resource busy: %s\n", error);
	} else if (errno == EEXIST) {
		printf("File exists: %s\n", error);
	} else if (errno == EXDEV) {
		printf("Cross-device link: %s\n", error);
	} else if (errno == ENODEV) {
		printf("No such device: %s\n", error);
	} else if (errno == ENOTDIR) {
		printf("Not a directory: %s\n", error);
	} else if (errno == EISDIR) {
		printf("Is a directory: %s\n", error);
	} else if (errno == EINVAL) {
		printf("Invalid argument: %s\n", error);
	} else if (errno == ENFILE) {
		printf("File table overflow: %s\n", error);
	} else if (errno == EMFILE) {
		printf("Too many open files: %s\n", error);
	} else if (errno == ENOTTY) {
		printf("Not a typewriter: %s\n", error);
	} else if (errno == ETXTBSY) {
		printf("Text file busy: %s\n", error);
	} else if (errno == EFBIG) {
		printf("File too large: %s\n", error);
	} else if (errno == ENOSPC) {
		printf("No space left on device: %s\n", error);
	} else if (errno == ESPIPE) {
		printf("Illegal seek: %s\n", error);
	} else if (errno == EROFS) {
		printf("Read-only file system: %s\n", error);
	} else if (errno == EMLINK) {
		printf("Too many links: %s\n", error);
	} else if (errno == EPIPE) {
		printf("Broken pipe: %s\n", error);
	} else if (errno == EDOM) {
		printf("Math argument out of domain of func: %s\n", error);
	} else if (errno == ERANGE) {
		printf("Math result not representable: %s\n", error);
	} else if (errno == EDEADLK) {
		printf("Resource deadlock would occur: %s\n", error);
	} else if (errno == ENAMETOOLONG) {
		printf("File name too long: %s\n", error);
	} else if (errno == ENOLCK) {
		printf("No record locks available: %s\n", error);
	} else if (errno == ENOSYS) {
		printf("Function not implemented: %s\n", error);
	} else if (errno == ENOTEMPTY) {
		printf("Directory not empty: %s\n", error);
	} else if (errno == ELOOP) {
		printf("Too many symbolic links encountered: %s\n", error);
	} else if (errno == EWOULDBLOCK) {
		printf("Operation would block: %s\n", error);
	} else if (errno == ENOMSG) {
		printf("No message of desired type: %s\n", error);
	} else if (errno == EIDRM) {
		printf("Identifier removed: %s\n", error);
	} else if (errno == ECHRNG) {
		printf("Channel number out of range: %s\n", error);
	} else if (errno == EL2NSYNC) {
		printf("Level 2 not synchronized: %s\n", error);
	} else if (errno == EL3HLT) {
		printf("Level 3 halted: %s\n", error);
	} else if (errno == EL3RST) {
		printf("Level 3 reset: %s\n", error);
	} else if (errno == ELNRNG) {
		printf("Link number out of range: %s\n", error);
	} else if (errno == EUNATCH) {
		printf("Protocol driver not attached: %s\n", error);
	} else if (errno == ENOCSI) {
		printf("No CSI structure available: %s\n", error);
	} else if (errno == EL2HLT) {
		printf("Level 2 halted: %s\n", error);
	} else if (errno == EBADE) {
		printf("Invalid exchange: %s\n", error);
	} else if (errno == EBADR) {
		printf("Invalid request descriptor: %s\n", error);
	} else if (errno == EXFULL) {
		printf("Exchange full: %s\n", error);
	} else if (errno == ENOANO) {
		printf("No anode: %s\n", error);
	} else if (errno == EBADRQC) {
		printf("Invalid request code: %s\n", error);
	} else if (errno == EBADSLT) {
		printf("Invalid slot: %s\n", error);
	} else if (errno == EBFONT) {
		printf("Bad font file format: %s\n", error);
	} else if (errno == ENOSTR) {
		printf("Device not a stream: %s\n", error);
	} else if (errno == ENODATA) {
		printf("No data available: %s\n", error);
	} else if (errno == ETIME) {
		printf("Timer expired: %s\n", error);
	} else if (errno == ENOSR) {
		printf("Out of streams resources: %s\n", error);
	} else if (errno == ENONET) {
		printf("Machine is not on the network: %s\n", error);
	} else if (errno == ENOPKG) {
		printf("Package not installed: %s\n", error);
	} else if (errno == EREMOTE) {
		printf("Object is remote: %s\n", error);
	} else if (errno == ENOLINK) {
		printf("Link has been severed: %s\n", error);
	} else if (errno == EADV) {
		printf("Advertise error: %s\n", error);
	} else if (errno == ESRMNT) {
		printf("Srmount error: %s\n", error);
	} else if (errno == ECOMM) {
		printf("Communication error on send: %s\n", error);
	} else if (errno == EPROTO) {
		printf("Protocol error: %s\n", error);
	} else if (errno == EMULTIHOP) {
		printf("Multihop attempted: %s\n", error);
	} else if (errno == EDOTDOT) {
		printf("RFS specific error: %s\n", error);
	} else if (errno == EBADMSG) {
		printf("Not a data message: %s\n", error);
	} else if (errno == EOVERFLOW) {
		printf("Value too large for defined data type: %s\n", error);
	} else if (errno == ENOTUNIQ) {
		printf("Name not unique on network: %s\n", error);
	} else if (errno == EBADFD) {
		printf("File descriptor in bad state: %s\n", error);
	} else if (errno == EREMCHG) {
		printf("Remote address changed: %s\n", error);
	} else if (errno == ELIBACC) {
		printf("Can not access a needed shared library: %s\n", error);
	} else if (errno == ELIBBAD) {
		printf("Accessing a corrupted shared library: %s\n", error);
	} else if (errno == ELIBSCN) {
		printf(".lib section in a.out corrupted: %s\n", error);
	} else if (errno == ELIBMAX) {
		printf("Attempting to link in too many shared libraries: %s\n", error);
	} else if (errno == ELIBEXEC) {
		printf("Cannot exec a shared library directly: %s\n", error);
	} else if (errno == EILSEQ) {
		printf("Illegal byte sequence: %s\n", error);
	} else if (errno == ERESTART) {
		printf("Interrupted system call should be restarted: %s\n", error);
	} else if (errno == ESTRPIPE) {
		printf("Streams pipe error: %s\n", error);
	} else if (errno == EUSERS) {
		printf("Too many users: %s\n", error);
	} else if (errno == ENOTSOCK) {
		printf("Socket operation on non-socket: %s\n", error);
	} else if (errno == EDESTADDRREQ) {
		printf("Destination address required: %s\n", error);
	} else if (errno == EMSGSIZE) {
		printf("Message too long: %s\n", error);
	} else if (errno == EPROTOTYPE) {
		printf("Protocol wrong type for socket: %s\n", error);
	} else if (errno == ENOPROTOOPT) {
		printf("Protocol not available: %s\n", error);
	} else if (errno == EPROTONOSUPPORT) {
		printf("Protocol not supported: %s\n", error);
	} else if (errno == ESOCKTNOSUPPORT) {
		printf("Socket type not supported: %s\n", error);
	} else if (errno == EOPNOTSUPP) {
		printf("Operation not supported on transport endpoint: %s\n", error);
	} else if (errno == EPFNOSUPPORT) {
		printf("Protocol family not supported: %s\n", error);
	} else if (errno == EAFNOSUPPORT) {
		printf("Address family not supported by protocol: %s\n", error);
	} else if (errno == EADDRINUSE) {
		printf("Address already in use: %s\n", error);
	} else if (errno == EADDRNOTAVAIL) {
		printf("Cannot assign requested address: %s\n", error);
	} else if (errno == ENETDOWN) {
		printf("Network is down: %s\n", error);
	} else if (errno == ENETUNREACH) {
		printf("Network is unreachable: %s\n", error);
	} else if (errno == ENETRESET) {
		printf("Network dropped connection because of reset: %s\n", error);
	} else if (errno == ECONNABORTED) {
		printf("Software caused connection abort: %s\n", error);
	} else if (errno == ECONNRESET) {
		printf("Connection reset by peer: %s\n", error);
	} else if (errno == ENOBUFS) {
		printf("No buffer space available: %s\n", error);
	} else if (errno == EISCONN) {
		printf("Transport endpoint is already connected: %s\n", error);
	} else if (errno == ENOTCONN) {
		printf("Transport endpoint is not connected: %s\n", error);
	} else if (errno == ESHUTDOWN) {
		printf("Cannot send after transport endpoint shutdown: %s\n", error);
	} else if (errno == ETOOMANYREFS) {
		printf("Too many references: cannot splice: %s\n", error);
	} else if (errno == ETIMEDOUT) {
		printf("Connection timed out: %s\n", error);
	} else if (errno == ECONNREFUSED) {
		printf("Connection refused: %s\n", error);
	} else if (errno == EHOSTDOWN) {
		printf("Host is down: %s\n", error);
	} else if (errno == EHOSTUNREACH) {
		printf("No route to host: %s\n", error);
	} else if (errno == EALREADY) {
		printf("Operation already in progress: %s\n", error);
	} else if (errno == EINPROGRESS) {
		printf("Operation now in progress: %s\n", error);
	} else if (errno == ESTALE) {
		printf("Stale NFS file handle: %s\n", error);
	} else if (errno == EUCLEAN) {
		printf("Structure needs cleaning: %s\n", error);
	} else if (errno == ENOTNAM) {
		printf("Not a XENIX named type file: %s\n", error);
	} else if (errno == ENAVAIL) {
		printf("No XENIX semaphores available: %s\n", error);
	} else if (errno == EISNAM) {
		printf("Is a named type file: %s\n", error);
	} else if (errno == EREMOTEIO) {
		printf("Remote I/O error: %s\n", error);
	} else if (errno == EDQUOT) {
		printf("Quota exceeded: %s\n", error);
	} else if (errno == ENOMEDIUM) {
		printf("No medium found: %s\n", error);
	} else if (errno == EMEDIUMTYPE) {
  		printf("Wrong medium type: %s\n", error);
	} else {
		printf("An unknown error has occured: %s\n", error);
	}
}

/*
* Purpose: Takes in the users command and parses the string properly so it knows which
* command to execute and another other arguments that modify the command. Is able to
* recognize when the user wants to exit, change directory, modify the input or output
* streams or wants to perform any other default linux command.
*/
int main() {
	char command[1000];
	char *args[200];
	char arg_count;
	char *fgets_ret;
	char *input;
	FILE *read_file;
	char read;
	char *output;
	FILE *write_file;
	char write;
	char *command_tok;
	char skip;
	int status;
	char cwd[PATH_MAX];

	//Continue processing user commands until they type 'exit'
	while (1) {
		// Gets the user's current working directory to display where they are at
		if (getcwd(cwd, sizeof(cwd)) != NULL) {
			printf("%s$ ", cwd);
		}
		// Get user's input
		fgets_ret = fgets(command, 1000, stdin);
		// Quit if it equals NULL
		if (fgets_ret == NULL) {
			return 1;
		}
		// Only process commands that aren't blank
		if (strlen(command) > 1) {
			read = 0;
			write = 0;
			arg_count = 0;
			status = 0;
			// Find the place of any redirection symbols
			input = strchr(command, '<');
			output = strchr(command, '>');
			command_tok = strtok(command, DELIMITERS);
			// Flag input redirection
			if (input != NULL) {
				read = 1;
				input = strtok(input, DELIMITERS);
			}
			// Flag output redirection
			if (output != NULL) {
				write = 1;
				output = strtok(output, DELIMITERS);
			}
			// Process user's command
			if (command_tok != NULL) {
				// End program if user typed 'exit'
				if (strcmp(command_tok, "exit") == 0) {
					exit(0);
				}
				// Get all parts of the command that don't involve redirection
				while (command_tok != NULL) {
					skip = 0;
					if (read && strcmp(command_tok, input) == 0) {
						skip = 1;
					}
					if (write && strcmp(command_tok, output) == 0) {
						skip = 1;
					}
					if (!skip) {
						args[arg_count++] = command_tok;
					}
					command_tok = strtok(NULL, DELIMITERS);
				}
				// Set NULL terminator for args
				args[arg_count] = NULL;
				if (args[0] != NULL) {
					// Processing command for change directory
					if (strcmp(args[0], "cd") == 0) {
						char path[PATH_MAX];
						char *homedir;
						// Getting user's home directory
						homedir = getenv("HOME");
						strcpy(path, homedir);
						strcat(path, "/");
						// Command is just cd so go to home directory
						if (arg_count <= 1) {
							status = chdir(path);
						} else {
							// Go to home directory then continue processing
							if (args[1][0] == '~') {
								// Only '~' so go to home directory only
								if (strlen(args[1]) == 1) {
									status = chdir(path);
								// Change directory to path following '~'
								} else {
									// Add 2 to arguement to skip '~/' characters
									args[1] += 2;
									strcat(path, args[1]);
									status = chdir(path);
								}
							// Go to directory given by user
							} else {
								status = chdir(args[1]);
							}
						}
						// changing directory returned an error for processing
						if (status < 0) {
							err_handle(args[1]);
						}
					// Handling all other commands
					} else {
						// Creating a child process
						int process = fork();
						// Do this if you are a child process
						if (process == 0) {
							// Change input stream if read is 1
							if (read) {
								read_file = freopen(input, "r", stdin);
								// Error handling if user gave invalid input stream
								if (read_file == NULL) {
									err_handle(input);
									_exit(-1);
								}
							}
							// Change output stream if write is 1
							if (write && freopen(output, "w", stdout) == NULL) {
								write_file = freopen(output, "w", stdout);
								// Error handling if user gave invalid output stream
								if (write_file == NULL) {
									err_handle(output);
									_exit(-1);
								}
							}
							// Start a compleletly new procress out of the child process
							status = execvp(args[0], args);
							// Exit program normally if everything went well
							if (status >= 0) {
								_exit(0);
							// Exit with errors if any processes ran into issues
							} else {
								err_handle(args[0]);
								_exit(-1);
							}
						// Do this if you are the parent process
						} else if (process >= 0) {
							// Wait for a child process to end before doing anything
							wait(&status);
							if (status < 0) {
								err_handle(args[0]);
							}
						} else {
							err_handle(args[0]);
							_exit(-1);
						}
					}
				}
			}
		}
	}
	// Close read and write redirection files
	fclose(read_file);
	fclose(write_file);
	return 0;
}
