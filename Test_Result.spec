

*** Method: simpleNumericTest ***
[ n = 2 ]
x = n; // [ n = 2 && x = 2 ]
x = x + 1; // [ n = 2 && x = 3 ]
x = x / 3; // [ n = 2 && x = 1 ]
$i0 = n + x; // [ n = 2 && x = 1 ]

*** Method: factorial ***
[ x = 5 ]
y = 1; // [ x = 5 && y = 1 ]
y = y * x; // [ x = 5 && y = 5 ]
x = x - 1; // [ x = 4 && y = 5 ]
y = y * x; // [ x = 4 && y = 20 ]
x = x - 1; // [ x = 3 && y = 20 ]
y = y * x; // [ x = 3 && y = 60 ]
x = x - 1; // [ x = 2 && y = 60 ]
y = y * x; // [ x = 2 && y = 120 ]
x = x - 1; // [ x = 1 && y = 120 ]

*** Method: gcd ***
[ a = 3 && b = 54 ]
b = b - a; // [ a = 3 && b = 51 ][ a = 3 && b = 51 ]
b = b - a; // [ a = 3 && b = 48 ][ a = 3 && b = 48 ]
b = b - a; // [ a = 3 && b = 45 ][ a = 3 && b = 45 ]
b = b - a; // [ a = 3 && b = 42 ][ a = 3 && b = 42 ]
b = b - a; // [ a = 3 && b = 39 ][ a = 3 && b = 39 ]
b = b - a; // [ a = 3 && b = 36 ][ a = 3 && b = 36 ]
b = b - a; // [ a = 3 && b = 33 ][ a = 3 && b = 33 ]
b = b - a; // [ a = 3 && b = 30 ][ a = 3 && b = 30 ]
b = b - a; // [ a = 3 && b = 27 ][ a = 3 && b = 27 ]
b = b - a; // [ a = 3 && b = 24 ][ a = 3 && b = 24 ]
b = b - a; // [ a = 3 && b = 21 ][ a = 3 && b = 21 ]
b = b - a; // [ a = 3 && b = 18 ][ a = 3 && b = 18 ]
b = b - a; // [ a = 3 && b = 15 ][ a = 3 && b = 15 ]
b = b - a; // [ a = 3 && b = 12 ][ a = 3 && b = 12 ]
b = b - a; // [ a = 3 && b = 9 ][ a = 3 && b = 9 ]
b = b - a; // [ a = 3 && b = 6 ][ a = 3 && b = 6 ]
b = b - a; // [ a = 3 && b = 3 ]

*** Method: simpleSLL ***
[ head = SLLNode@1ae369b7 && head.data = 3 && head.next = null && temp = SLLNode@4ec6a292 && temp.data = 1 && temp.next = null && anotherTemp = SLLNode@1b40d5f0 && anotherTemp.data = 1 && anotherTemp.next = null ]
head.<SLLNode: int data> = 3; // [ head = SLLNode@1ae369b7 && head.data = 3 && head.next = null && temp = SLLNode@4ec6a292 && temp.data = 1 && temp.next = null && anotherTemp = SLLNode@1b40d5f0 && anotherTemp.data = 1 && anotherTemp.next = null ]
head.<SLLNode: SLLNode next> = temp; // [ head = SLLNode@1ae369b7 && head.data = 3 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null && temp = SLLNode@4ec6a292 && temp.data = 1 && temp.next = null && anotherTemp = SLLNode@1b40d5f0 && anotherTemp.data = 1 && anotherTemp.next = null ]
head.<SLLNode: int data> = 9; // [ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null && temp = SLLNode@4ec6a292 && temp.data = 1 && temp.next = null && anotherTemp = SLLNode@1b40d5f0 && anotherTemp.data = 1 && anotherTemp.next = null ]
anotherTemp.<SLLNode: int data> = 5; // [ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null && temp = SLLNode@4ec6a292 && temp.data = 1 && temp.next = null && anotherTemp = SLLNode@1b40d5f0 && anotherTemp.data = 5 && anotherTemp.next = null ]

*** Method: findMax ***
[ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null ]
t = head; // [ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null && t = SLLNode@1ae369b7 && t.data = 9 && t.next = SLLNode@4ec6a292 && t.next.data = 1 && t.next.next = null ]
max = head.<SLLNode: int data>; // [ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null && t = SLLNode@1ae369b7 && t.data = 9 && t.next = SLLNode@4ec6a292 && t.next.data = 1 && t.next.next = null && max = 9 ]
$i0 = t.<SLLNode: int data>; // [ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null && t = SLLNode@1ae369b7 && t.data = 9 && t.next = SLLNode@4ec6a292 && t.next.data = 1 && t.next.next = null && max = 9 ]
t = t.<SLLNode: SLLNode next>; // [ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null && t = SLLNode@4ec6a292 && t.data = 1 && t.next = null && max = 9 ]
$i0 = t.<SLLNode: int data>; // [ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null && t = SLLNode@4ec6a292 && t.data = 1 && t.next = null && max = 9 ]
t = t.<SLLNode: SLLNode next>; // [ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null && t = null && max = 9 ]

*** Method: reverse ***
[ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null ]
result = null; // [ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null && result = null ]
t = head.<SLLNode: SLLNode next>; // [ head = SLLNode@1ae369b7 && head.data = 9 && head.next = SLLNode@4ec6a292 && head.next.data = 1 && head.next.next = null && result = null && t = SLLNode@4ec6a292 && t.data = 1 && t.next = null ]
head.<SLLNode: SLLNode next> = result; // [ head = SLLNode@1ae369b7 && head.data = 9 && head.next = null && result = null && t = SLLNode@4ec6a292 && t.data = 1 && t.next = null ]
result = head; // [ head = SLLNode@1ae369b7 && head.data = 9 && head.next = null && result = SLLNode@1ae369b7 && result.data = 9 && result.next = null && t = SLLNode@4ec6a292 && t.data = 1 && t.next = null ]
head = t; // [ head = SLLNode@4ec6a292 && head.data = 1 && head.next = null && result = SLLNode@1ae369b7 && result.data = 9 && result.next = null && t = SLLNode@4ec6a292 && t.data = 1 && t.next = null ]
t = head.<SLLNode: SLLNode next>; // [ head = SLLNode@4ec6a292 && head.data = 1 && head.next = null && result = SLLNode@1ae369b7 && result.data = 9 && result.next = null && t = null ]
head.<SLLNode: SLLNode next> = result; // [ head = SLLNode@4ec6a292 && head.data = 1 && head.next = SLLNode@1ae369b7 && head.next.data = 9 && head.next.next = null && result = SLLNode@1ae369b7 && result.data = 9 && result.next = null && t = null ]
result = head; // [ head = SLLNode@4ec6a292 && head.data = 1 && head.next = SLLNode@1ae369b7 && head.next.data = 9 && head.next.next = null && result = SLLNode@4ec6a292 && result.data = 1 && result.next = SLLNode@1ae369b7 && result.next.data = 9 && result.next.next = null && t = null ]
head = t; // [ head = null && result = SLLNode@4ec6a292 && result.data = 1 && result.next = SLLNode@1ae369b7 && result.next.data = 9 && result.next.next = null && t = null ]
