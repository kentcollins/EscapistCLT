import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Escapist {

	List<TextLine> ciphers = new ArrayList<>();
	List<TextLine> plain = new ArrayList<>();
	List<String> dictionary = new ArrayList<>();
	Scanner scanner = new Scanner(System.in);

	void run() {
		System.out.println("Enter:");
		String input = scanner.nextLine().trim();
		while (true) {
			process(input);
			input = scanner.nextLine().trim();
		}
	}

	void process(String in) {
		String[] components = in.split(" ");
		String cmd = components[0];
		if ("pc".equals(cmd)) {
			printCipherText();
		} else if ("pp".equals(cmd)) {
			printPlainText();
		} else if ("ps".equals(cmd) || "pa".equals(cmd)) {
			printSolutionSoFar();
		} else if ("ac".equals(cmd)) {
			addCipherText(in.substring(in.indexOf(' ')));
		} else if ("shift".equals(cmd)) {
			shiftLines(components);
		} else if ("map".equals(cmd)) {
			// map a value in cipher to a plain text
			
		} else if ("exit".equals(cmd))

		{
			scanner.close();
			System.exit(0);
		} else {
			System.out.println("unable to process: " + in);
		}
	}
	
	private void addCipherText(String text) {
		TextLine line = new TextLine(text);
		ciphers.add(line);
		plain.add(new TextLine(text.length()));		
	}

	void shiftLines(String[] components) {
		String shiftAmount = components[1];
		int amt = Integer.parseInt(shiftAmount);
		for (int i = 0; i<ciphers.size(); i++) {
			plain.set(i, TextLine.shift(ciphers.get(i), amt));
		}
	}

	void printSolutionSoFar() {
		for (int i = 0; i < ciphers.size(); i++) {
			System.out.println(ciphers.get(i));
			System.out.println(plain.get(i));
		}
	}

	void printPlainText() {
		for (TextLine line : plain) {
			System.out.println(line);
		}
	}

	void printCipherText() {
		// print cipher text
		for (TextLine line : ciphers) {
			System.out.println(line);
		}
	}

	public static void main(String[] args) {
		Escapist e = new Escapist();
		e.run();
	}

}
