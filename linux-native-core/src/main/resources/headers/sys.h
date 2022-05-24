#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <errno.h>
#include <string.h>

typedef struct __passwd {
  char *pw_name;
  char *pw_passwd;
  __uid_t pw_uid;
  __gid_t pw_gid;
  char *pw_gecos;
  char *pw_dir;
  char *pw_shell;
} __passwd;

typedef struct __group
{
    char *gr_name;              /* Group name.  */
    char *gr_passwd;            /* Password.    */
    __gid_t gr_gid;             /* Group ID.    */
    char **gr_mem;              /* Member list. */
} __group;


#define __NEW_UTS_LEN 64


typedef struct __new_utsname {
        char sysname[__NEW_UTS_LEN + 1];
        char nodename[__NEW_UTS_LEN + 1];
        char release[__NEW_UTS_LEN + 1];
        char version[__NEW_UTS_LEN + 1];
        char machine[__NEW_UTS_LEN + 1];
        char domainname[__NEW_UTS_LEN + 1];
} __new_utsname;
