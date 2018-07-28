public class Benchmarks {

    public int simpleNumericTest(int n) {
        int x = n;
        x++;
        x = x/3;
        return n+x;
    }

    public int gcd(int a, int b) {
        while (a != b) {
            if (a > b)
                a = a - b;
            else
                b = b - a;
        }
        return a;
    }

    public int factorial(int x) {
        int y = 1;
        while (x != 1) {
            y = y * x;
            x = x - 1;
        }
        return y;
    }

    public void simpleSLL(SLLNode head, SLLNode temp, SLLNode anotherTemp) {
        head.data = 3;
        head.next = temp;
        head.data = 9;
        anotherTemp.data = 5;
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
        benchmarks.simpleNumericTest(2);
        benchmarks.factorial(5);
        benchmarks.gcd(3, 54);

        // SLL tests:
        SLLNode head = new SLLNode(2, null);
        SLLNode temp = new SLLNode(1, null);
        SLLNode anotherTemp = new SLLNode(1, null);
        benchmarks.simpleSLL(head, temp, anotherTemp);
        benchmarks.findMax(head);
        benchmarks.reverse(head);
    }
}
