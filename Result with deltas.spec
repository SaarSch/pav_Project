

*** Method: simpleNumericTest ***
[ n = 2 ]
x = n; // [ x = 2 ]
x = x + 1; // [ x = 3 ]
x = x / 3; // [ x = 1 ]
$i0 = n + x; // [ ]

*** Method: factorial ***
[ x = 5 ]
y = 1; // [ y = 1 ]
y = y * x; // [ y = 5 ]
x = x - 1; // [ x = 4 ]
y = y * x; // [ y = 20 ]
x = x - 1; // [ x = 3 ]
y = y * x; // [ y = 60 ]
x = x - 1; // [ x = 2 ]
y = y * x; // [ y = 120 ]
x = x - 1; // [ x = 1 ]

*** Method: gcd ***
[ a = 3 && b = 54 ]
b = b - a; // [ b = 51 ][ ]
b = b - a; // [ b = 48 ][ ]
b = b - a; // [ b = 45 ][ ]
b = b - a; // [ b = 42 ][ ]
b = b - a; // [ b = 39 ][ ]
b = b - a; // [ b = 36 ][ ]
b = b - a; // [ b = 33 ][ ]
b = b - a; // [ b = 30 ][ ]
b = b - a; // [ b = 27 ][ ]
b = b - a; // [ b = 24 ][ ]
b = b - a; // [ b = 21 ][ ]
b = b - a; // [ b = 18 ][ ]
b = b - a; // [ b = 15 ][ ]
b = b - a; // [ b = 12 ][ ]
b = b - a; // [ b = 9 ][ ]
b = b - a; // [ b = 6 ][ ]
b = b - a; // [ b = 3 ]

*** Method: simpleSLL ***
[ head = SLLNode@1ae369b7 && head.data = 3 && head.next = null && temp = SLLNode@4ec6a292 && temp.data = 1 && temp.next = null && anotherTemp = SLLNode@1b40d5f0 && anotherTemp.data = 1 && anotherTemp.next = null ]
head.<SLLNode: int data> = 3; // [ ]
head.<SLLNode: SLLNode next> = temp; // [ head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null ]
head.<SLLNode: int data> = 9; // [ head.data = 9 ]
anotherTemp.<SLLNode: int data> = 5; // [ anotherTemp.data = 5 ]

*** Method: findMax ***
[ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null ]
t = head; // [ t = SLLNode@1ae369b7 && t.data = 9 && t.next = SLLNode@4ec6a292 && t.next.data = 1 && t.next.next = null ]
max = head.<SLLNode: int data>; // [ max = 9 ]
$i0 = t.<SLLNode: int data>; // [ ]
t = t.<SLLNode: SLLNode next>; // [ t = SLLNode@4ec6a292 && t.data = 1 && t.next = null ]
$i0 = t.<SLLNode: int data>; // [ ]
t = t.<SLLNode: SLLNode next>; // [ t = null ]

*** Method: reverse ***
[ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null ]
result = null; // [ result = null ]
t = head.<SLLNode: SLLNode next>; // [ t = SLLNode@4ec6a292 && t.data = 1 && t.next = null ]
head.<SLLNode: SLLNode next> = result; // [ head.next = null ]
result = head; // [ result = SLLNode@1ae369b7 && result.data = 9 && result.next = null ]
head = t; // [ head = SLLNode@4ec6a292 && head.data = 1 ]
t = head.<SLLNode: SLLNode next>; // [ t = null ]
head.<SLLNode: SLLNode next> = result; // [ head.next = SLLNode@1ae369b7 && head.next.data = 9 ]
result = head; // [ result = SLLNode@4ec6a292 && result.data = 1 && result.next = SLLNode@1ae369b7 && result.next.data = 9 && result.next.next = null ]
head = t; // [ head = null ]
