public class Test {

    public void simpleSLL(SLLNode head, SLLNode temp, SLLNode anotherTemp) {
        head.data = 3;
        head.next = temp;
        head.data = 4;
        anotherTemp.data = 5;
    }

    public void simpleNumericTest(int num) {
        int temp = num;
        num = temp;
        num++;
    }

    /*public void factorial(int x) {
        int y = 1;
        while (x != 1) {
            y = y * x;
            x = x - 1;
        }
        System.out.println(y);
    }

    public void gcd(int a, int b) {
        while (a != b) {
            if (a > b)
                a = a - b;
            else
                b = b - a;
        }
        System.out.println(a);
    }

    public void findMax(SLLNode head) {
        SLLNode t = head;
        int max = head.data;
        while (t != null) {
            if (t.data > max)
                max = t.data;
            t = t.next;
        }
        System.out.println(max);
    }*/

    public static void main(String[] args) {
        Test test = new Test();

        // Numeric tests:
        test.simpleNumericTest(2);
        //test.factorial(5);
        //test.gcd(3, 54);

        // SLL tests:
        SLLNode head = new SLLNode(2, null);
        SLLNode temp = new SLLNode(1, null);
        SLLNode anotherTemp = new SLLNode(1, null);
        test.simpleSLL(head, temp, anotherTemp);
        //test.findMax(head);
    }
}
