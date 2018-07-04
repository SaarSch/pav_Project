import java.io.PrintWriter;

public class Main {

	public static void main(String[] args) {
		// add call to soot compiler with our analysis (transform) added
		Test test = new Test();
		test.A(new SLL(2, null));
		
		PrintWriter writer;
		try {
			String res = PexynLogger.getString();
			System.out.println(res);
			writer = new PrintWriter(test.getClass().getName() + ".spec", "UTF-8");
			writer.println(res);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
