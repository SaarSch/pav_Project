public class Test {

//	public void A(Benchmarks.SLL head) {
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
//		Benchmarks.SLL temp = new Benchmarks.SLL(1, null);
//
//		pav.PexynLogger.logCmd("Benchmarks.SLL temp = new Benchmarks.SLL(1, null);");
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
//	public static class AEnv extends JavaEnv {
//		public Benchmarks.SLL head; // method argument
//		public Benchmarks.SLL temp; // local
//	}

    public void A(SLL head) {
        SLL temp = new SLL(1, null);
        head.next = temp;
    }

    public static void main(String[] args) {
        // add call to soot compiler with our analysis (transform) added
        Test test = new Test();
        test.A(new SLL(2, null));
    }
}
