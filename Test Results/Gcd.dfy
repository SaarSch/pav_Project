/** Automatically-generated code.
 */

/** Synthesized method.
 */
method gcd(a : int?, b : int?) returns (res : int?)  {
  // Initialize local variables to their default values.
  res  := 0;

  while (a < b)
  invariant true;
  {
    assert true;
    b := (b - a);
  }
  assert true;
  res := a;
  return;
  return res;
}