simpleNumericTest(mut n:int) -> (i0:int) {
  var x:int

  example {
    [n==2]
    -> x = n; // [n==2 && x==2]
    -> x = x + 1; // [n==2 && x==3]
    -> x = x / 3; // [n==2 && x==1]
    -> i0 = n + x; // [n==2 && x==1]
  }
}
