public class Test {

    public static void main(String[] args) {
        // add call to soot compiler with our analysis (transform) added
        Test test = new Test();
        test.nevo(2);
    }

    public void nevo(int num) {
        int temp = num;
    }


//	public void A(Benchmarks.SLLNode head) {
//		pav.PexynLogger.init(ReflectionUtils.getMethodByName(Benchmarks.Test.class, "A"), AEnv.class);
//		AEnv env = new AEnv();
//		// the first generation should call init +
//		// new Env +
//		// assign env.head=head for each method argument +
//		// assign env.temp=null for each local variable
//		env.head = head;
//		env.temp = null;
//		pav.PexynLogger.logEnv(env); // TODO: remove?
//
//		Benchmarks.SLLNode temp = new Benchmarks.SLLNode(1, null);
//
//		pav.PexynLogger.logCmd("Benchmarks.SLLNode temp = new Benchmarks.SLLNode(1, null);");
//		env.head = head;
//		env.temp = temp;
//		pav.PexynLogger.logEnv(env);
//
//		head.next = temp;
//
//		pav.PexynLogger.logCmd("head.next = temp;");
//		env.head = head;
//		env.temp = temp;
//		pav.PexynLogger.logEnv(env);
//		pav.PexynLogger.dumpSpecToFile(this.getClass().getName());
//	}
//
//
//	public static class A_Env extends JavaEnv {
//		public Benchmarks.SLLNode head; // method argument
//		public Benchmarks.SLLNode temp; // local
//	}

//    public static class SLLNode {
//        public SLLNode(int data, SLLNode next) {
//            this.data = data;
//            this.next = next;
//        }
//
//        public int data;
//        public SLLNode next;
//    }
//
//    public void nevo_mashiach(SLLNode head) {
//        SLLNode temp = new SLLNode(1, null);
//        head.next = temp;
//    }
//
//    public static void main(String[] args) {
//        // add call to soot compiler with our analysis (transform) added
//        Test test = new Test();
//        test.nevo_mashiach(new SLLNode(2, null));
//    }
}
