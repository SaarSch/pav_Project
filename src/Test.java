import bgu.cs.util.ReflectionUtils;
import jminor.java.JavaEnv;

public class Test {
	
	public void A(SLL head) {
		PexynLogger.init(ReflectionUtils.getMethodByName(Test.class, "A"), AEnv.class);
		AEnv env = new AEnv();
		// the first generation should call init +
		// new Env +
		// assign env.head=head for each method argument +
		// assign env.temp=null for each local variable
		env.head = head;
		env.temp = null;
		PexynLogger.logEnv(env); // TODO: remove?

		SLL temp = new SLL(1, null);

		PexynLogger.logCmd("SLL temp = new SLL(1, null);");
		env.head = head;
		env.temp = temp;
		PexynLogger.logEnv(env);

		head.next = temp;

		PexynLogger.logCmd("head.next = temp;");
		env.head = head;
		env.temp = temp;
		PexynLogger.logEnv(env);
		PexynLogger.end(this.getClass().getName());
	}


	public static class AEnv extends JavaEnv {
		public SLL head; // method argument
		public SLL temp; // local
	}

	public void B(SLL head) {
		SLL temp = new SLL(1, null);
		head.next = temp;
	}

	public static void main(String[] args) {
		// add call to soot compiler with our analysis (transform) added
		Test test = new Test();
		test.B(new SLL(2, null));
	}}
