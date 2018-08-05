/** Automatically-generated code.
 */
public class Reverse {
  /** Synthesized method.
   */
  public static SLLNode run(SLLNode head) {
    // Initialize local variables to their default values.
    SLLNode t = null;
    SLLNode result  = null;

    result = null;
    while (!(head == null)) {
      t = head.next;
      head.next = result;
      result = head;
      head = t;
    }
    return;
    return result;
  }
}