

*** Method: example_1 ***
temp = num; // [num==2 && temp==2 ]
num = temp; // []
num = num + 1; // [num==3 ]

*** Method: example_2 ***
head.<SLLNode: int data> = 3; // [head==SLLNode@e580929 .data==3 xxx.next==null&& temp==SLLNode@2812cbfa && .data==1 && xxx.next==null&& anotherTemp==SLLNode@2acf57e3 && .data==1 && xxx.next==null]
head.<SLLNode: SLLNode next> = temp; // [head==SLLNode@e580929 .data==3 xxx.next==SLLNode@2812cbfa xxx.next==null&& temp==SLLNode@2812cbfa && xxx.next==null&& anotherTemp==SLLNode@2acf57e3 && xxx.next==null]
head.<SLLNode: int data> = 4; // [head==SLLNode@e580929 .data==4 xxx.next==SLLNode@2812cbfa xxx.next==null&& temp==SLLNode@2812cbfa && xxx.next==null&& anotherTemp==SLLNode@2acf57e3 && xxx.next==null]
anotherTemp.<SLLNode: int data> = 5; // [head==SLLNode@e580929 .data==4 xxx.next==SLLNode@2812cbfa xxx.next==null&& temp==SLLNode@2812cbfa && xxx.next==null&& anotherTemp==SLLNode@2acf57e3 && .data==5 && xxx.next==null]
