]

*** Method: example_1 ***
temp = num; // [#LOCAL#_temp==2 && #LOCAL#_num==2 && ]
num = temp; // [#LOCAL#_temp==2 && #LOCAL#_num==2 && ]
num = num + 1; // [#LOCAL#_temp==2 && #LOCAL#_num==3 && ]

*** Method: example_2 ***
head.<SLLNode: int data> = 3; // [#LOCAL#_anotherTemp==SLLNode@880ec60	data: 1 && 	next: null#LOCAL#_temp==SLLNode@5b464ce8	next: null#LOCAL#_head==SLLNode@57829d67	data: 3 && 	next: null]
head.<SLLNode: SLLNode next> = temp; // [#LOCAL#_anotherTemp==SLLNode@880ec60	data: 1 && 	next: null#LOCAL#_temp==SLLNode@5b464ce8	next: null#LOCAL#_head==SLLNode@57829d67	data: 3 && 	next: SLLNode@5b464ce8	data: 1 && 	next: null]
head.<SLLNode: int data> = 4; // [#LOCAL#_anotherTemp==SLLNode@880ec60	next: null#LOCAL#_temp==SLLNode@5b464ce8	next: null#LOCAL#_head==SLLNode@57829d67	data: 4 && 	next: SLLNode@5b464ce8	data: 1 && 	next: null]
anotherTemp.<SLLNode: int data> = 5; // [#LOCAL#_anotherTemp==SLLNode@880ec60	data: 5 && 	next: null#LOCAL#_temp==SLLNode@5b464ce8	data: 1 && 	next: null#LOCAL#_head==SLLNode@57829d67	data: 4 && 	next: SLLNode@5b464ce8	data: 1 && 	next: null
