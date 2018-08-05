/** Automatically-generated code.
 */
public class FindMax {
  /** Synthesized method.
   */
  public static int run(SLLNode head) {
    // Initialize local variables to their default values.
    SLLNode t = null;
    int i0 = 0;
    int max  = 0;

    t = head;
    max = head.data;
    while (!(i0 < max)) {
      i0 = t.data;
      t = t.next;
    }
    return;
    return max;
  }
}