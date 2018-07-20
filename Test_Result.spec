

*** Method: example_1 ***
temp = num; // [num==2 && temp==2]
num = temp; // [num==2 && temp==2]
num = num + 1; // [num==3 && temp==2]

*** Method: example_2 ***
head.<SLLNode: int data> = 3; // [head==SLLNode@7e0ea639	data: 3 && 	next: nulltemp==SLLNode@2812cbfa	data: 1 && 	next: nullanotherTemp==SLLNode@2acf57e3	data: 1	next: null]
head.<SLLNode: SLLNode next> = temp; // [head==SLLNode@7e0ea639	data: 3 && 	next: SLLNode@2812cbfa	data: 1 && 	next: nulltemp==SLLNode@2812cbfa	data: 1 && 	next: nullanotherTemp==SLLNode@2acf57e3	data: 1	next: null]
head.<SLLNode: int data> = 4; // [head==SLLNode@7e0ea639	data: 4 && 	next: SLLNode@2812cbfa	data: 1 && 	next: nulltemp==SLLNode@2812cbfa	data: 1 && 	next: nullanotherTemp==SLLNode@2acf57e3	data: 1	next: null]
anotherTemp.<SLLNode: int data> = 5; // [head==SLLNode@7e0ea639	data: 4 && 	next: SLLNode@2812cbfa	data: 1 && 	next: nulltemp==SLLNode@2812cbfa	data: 1 && 	next: nullanotherTemp==SLLNode@2acf57e3	data: 5	next: null]
