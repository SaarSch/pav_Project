

*** exemple_1 ***
logCmd: temp = num
#LOCAL#_temp--> 2
#LOCAL#_num--> 2
logCmd: num = temp
#LOCAL#_temp--> 2
#LOCAL#_num--> 2
logCmd: num = num + 1
#LOCAL#_temp--> 2
#LOCAL#_num--> 3

*** exemple_2 ***
logCmd: head.<SLLNode: int data> = 3
#LOCAL#_anotherTemp--> SLLNode@11028347	data: 1	next: null
#LOCAL#_temp--> SLLNode@71bc1ae4	data: 1	next: null
#LOCAL#_head--> SLLNode@6ed3ef1	data: 3	next: null
logCmd: head.<SLLNode: SLLNode next> = temp
#LOCAL#_anotherTemp--> SLLNode@11028347	data: 1	next: null
#LOCAL#_temp--> SLLNode@71bc1ae4	data: 1	next: null
#LOCAL#_head--> SLLNode@6ed3ef1	data: 3	next: SLLNode@71bc1ae4	data: 1	next: null
logCmd: head.<SLLNode: int data> = 4
#LOCAL#_anotherTemp--> SLLNode@11028347	data: 1	next: null
#LOCAL#_temp--> SLLNode@71bc1ae4	data: 1	next: null
#LOCAL#_head--> SLLNode@6ed3ef1	data: 4	next: SLLNode@71bc1ae4	data: 1	next: null
logCmd: anotherTemp.<SLLNode: int data> = 5
#LOCAL#_anotherTemp--> SLLNode@11028347	data: 5	next: null
#LOCAL#_temp--> SLLNode@71bc1ae4	data: 1	next: null
#LOCAL#_head--> SLLNode@6ed3ef1	data: 4	next: SLLNode@71bc1ae4	data: 1	next: null
