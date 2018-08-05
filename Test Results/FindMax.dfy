/** Automatically-generated code.
 */

/** Synthesized method.
 */
method findMax(head : SLLNode?) returns (max : int?)  {
  // Initialize local variables to their default values.
  var t : SLLNode? := null;
  var i0 : int? := 0;
  max  := 0;

  assert true;
  t := head;
  assert true;
  max := head.data;
  while (!(i0 < max))
  invariant true;
  {
    assert true;
    i0 := t.data;
    assert true;
    t := t.next;
  }
  return;
  return max;
}