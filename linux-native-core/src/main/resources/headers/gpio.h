#include <linux/types.h>

#define GPIO_MAX_NAME_SIZE 32
#define GPIOHANDLES_MAX 64

typedef struct gpiochip_info {
    char name[GPIO_MAX_NAME_SIZE];
    char label[GPIO_MAX_NAME_SIZE];
    __u32 lines;
} gpiochip_info;


typedef struct gpioline_info {
    __u32 line_offset;
    __u32 flags;
    char name[GPIO_MAX_NAME_SIZE];
    char consumer[GPIO_MAX_NAME_SIZE];
} gpioline_info;


typedef struct gpiohandle_request {
    __u32 lineoffsets[GPIOHANDLES_MAX];
    __u32 flags;
    __u8 default_values[GPIOHANDLES_MAX];
    char consumer_label[GPIO_MAX_NAME_SIZE];
    __u32 lines;
    int fd;
} gpiohandle_request;

typedef struct gpiohandle_data {
    __u8 values[GPIOHANDLES_MAX];
} gpiohandle_data;
