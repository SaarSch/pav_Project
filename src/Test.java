public class Test {

    public void exemple_2(SLLNode head, SLLNode temp, SLLNode anotherTemp) {
        head.data = 3;
        head.next = temp;
        head.data = 4;
        anotherTemp.data = 5;
    }

    public void exemple_1(int num) {
        int temp = num;
        num = temp;
        num++;
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.exemple_1(2);

        SLLNode head = new SLLNode(2, null);
        SLLNode temp = new SLLNode(1, null);
        SLLNode anotherTemp = new SLLNode(1, null);
        test.exemple_2(head, temp, anotherTemp);
    }
}
