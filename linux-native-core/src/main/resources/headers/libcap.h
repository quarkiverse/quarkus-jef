#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <errno.h>
#include <string.h>

typedef struct __user_cap_header_struct {
	uint32_t version;
	uint32_t pid;
} __user_cap_header_struct;

typedef struct __user_cap_data_struct {
        uint32_t effective;
        uint32_t permitted;
        uint32_t inheritable;
} __user_cap_data_struct;



typedef struct _cap_struct {
    struct __user_cap_header_struct head;
    struct __user_cap_data_struct set;
} _cap_struct;
