/** Automatically-generated code.
 */

/** Synthesized method.
 */
method factorial(x : int?) returns (y : int?)  {
  // Initialize local variables to their default values.
  y  := 0;

  assert true;
  y := 1;
  while (1 < x)
  invariant true;
  {
    assert true;
    y := (y * x);
    assert true;
    x := (x - 1);
  }
  return;
  return y;
}