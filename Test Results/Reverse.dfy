/** Automatically-generated code.
 */

/** Synthesized method.
 */
method reverse(head : SLLNode?) returns (result : SLLNode?)  {
  // Initialize local variables to their default values.
  var t : SLLNode? := null;
  result  := null;

  assert true;
  result := null;
  while (!(head == null))
  invariant true;
  {
    assert true;
    t := head.next;
    assert true;
    head.next := result;
    assert true;
    result := head;
    assert true;
    head := t;
  }
  return;
  return result;
}