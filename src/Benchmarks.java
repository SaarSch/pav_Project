public class Benchmarks {

    public int simpleNumericTest(int n) {
        int x = n;
        x++;
        x = x/3;
        return n+x;
    }

    public int gcd(int a, int b) {
        int res;
        while (a != b) {
            if (a > b)
                a = a - b;
            else
                b = b - a;
        }
        res = a;
        return res;
    }

    public int factorial(int x) {
        int y = 1;
        while (x != 1) {
            y = y * x;
            x = x - 1;
        }
        return y;
    }

    public void simpleSLL(SLLNode head, SLLNode temp, SLLNode temp2) {
        head.data = 3;
        head.next = temp;
        head.data = 9;
        temp2.data = 5;
    }

    public int findMax(SLLNode head) {
        SLLNode t = head;
        int max = head.data;
        while (t != null) {
            if (t.data > max)
                max = t.data;
            t = t.next;
        }
        return max;
    }

    public SLLNode reverse(SLLNode head) {
        SLLNode t, result;
        // The target code.
        result = null;
        while (head != null) {
            t = head.next;
            head.next = result;
            result = head;
            head = t;
        }
        return result;
    }

    public static void main(String[] args) {
        Benchmarks benchmarks = new Benchmarks();

        // Numeric tests:
        benchmarks.factorial(5);
        benchmarks.factorial(3);
        benchmarks.factorial(2);

        //benchmarks.simpleNumericTest(2);
        benchmarks.gcd(3, 54);

        // SLL tests:

        SLLNode head = new SLLNode(2, null);
        SLLNode temp = new SLLNode(1, null);
        SLLNode temp2 = new SLLNode(1, temp);
        //temp.next = temp2;
        benchmarks.simpleSLL(head, temp, temp2);
        benchmarks.findMax(head); // Note: this would get into an infinite loop if there's a cycle in the SLL, like above
        //benchmarks.reverse(head); // Same as above
    }
}
