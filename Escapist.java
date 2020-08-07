import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Escapist {

	List<TextLine> ciphers = new ArrayList<>();
	List<String> dictionary = new ArrayList<>();
	HashMap<String, List<String>> patternMap;
	HashMap<Character, Character> charMap = new HashMap<>();
	Scanner scanner = new Scanner(System.in);

	void run() {
		System.out.println("Loading default dictionary:");
		loadDictionary("dictionary.txt");
		System.out.println("Enter 'help' for a list of commands.");
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
		} else if ("ps".equals(cmd) || "pa".equals(cmd)) {
			printSolutionSoFar();
		} else if ("ac".equals(cmd)) {
			addCipherText(in.substring(in.indexOf(" ") + 1));
		} else if ("shift".equals(cmd)) {
			shiftLines(components);
		} else if ("map".equals(cmd)) {
			map(components[1], components[2]);
		} else if ("ld".equals(cmd)) {
			loadDictionary(components[1]);
		} else if ("lc".equals(cmd)) {
			loadCipherFromFile(components[1]);
		} else if ("wc".equals(cmd)) {
			writeCipher(components[1]);
		} else if ("wp".equals(cmd)) {
			writePlaintext(components[1]);
		} else if ("cpm".equals(cmd)) {
			countPatternMatches();
		} else if ("frequency".equals(cmd)) {
			buildFrequencyMap();
		} else if ("cc".equals(cmd)) {
			clearCipher();
		} else if ("clear".equals(cmd)) {
			charMap = new HashMap<>();
		} else if ("charLoop".equals(cmd)) {
			charLoop();
		}

		else if ("pd".equals(cmd)) {
			printDictionary();
		} else if ("pat".equals(cmd)) {
			System.out.println(patternize(components[1]));
		} else if ("match".equals(cmd)) {
			printMatchingPatterns(components[1]);
		} else if ("help".equals(cmd)) {
			printHelp();
		}

		else if ("exit".equals(cmd))

		{
			scanner.close();
			System.exit(0);
		} else {
			System.out.println("unable to process: " + in);
		}
	}

	private void charLoop() {
		for ( char a = 'a'; a<='z'; a+=1) {
			System.out.print(a);
		}
	}

	private void map(String cipher, String plain) {
		for (int i = 0; i < cipher.length(); i++) {
			charMap.put(cipher.charAt(i), plain.charAt(i));
		}
		printSolutionSoFar();
	}

	private void clearCipher() {
		ciphers = new ArrayList<>();
	}

	private void buildFrequencyMap() {
		HashMap<Character, Integer> freq = new HashMap<>();
		for (TextLine line : ciphers) {
			for (Character c : line.chars) {
				if (c >= 'a' && c <= 'z') {
					freq.put(c, freq.getOrDefault(c, 0) + 1);
				}
			}
		}
		System.out.println(freq);
	}

	private void countPatternMatches() {
		String count = "";
		for (TextLine line : ciphers) {
			for (String word : line.toString().split(" ")) {
				String pattern = patternize(word);
				count += patternMap.get(pattern).size() + " ";
			}
		}
		System.out.println(count);
	}

	private void printHelp() {
		System.out.println("Displays commands -- must implement");
	}

	private void writePlaintext(String fileName) {
		// TODO write the solution, not the ciphers
		Path output = Paths.get(fileName);
		try {
			Files.write(output, ciphers, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeCipher(String fileName) {
		Path output = Paths.get(fileName);
		try {
			Files.write(output, ciphers, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printMatchingPatterns(String string) {
		String p = patternize(string);
		if (!patternMap.containsKey(p)) {
			System.out.println("No pattern matches " + p);
		} else {
			patternMap.get(p).stream().forEach(System.out::println);
		}

	}

	private void printDictionary() {
		for (String s : dictionary) {
			System.out.println(s);
		}
	}

	private void loadDictionary(String fileName) {
		try {
			dictionary = Files.readAllLines(Paths.get(fileName));
			buildPatternMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void buildPatternMap() {
		patternMap = new HashMap<>();
		for (String s : dictionary) {
			String p = patternize(s);
			if (!patternMap.containsKey(p)) {
				patternMap.put(p, new ArrayList<String>());
			}
			patternMap.get(p).add(s);
		}
	}

	private void loadCipherFromFile(String fileName) {
		ciphers.clear();
		try {
			List<String> readAllLines = Files
					.readAllLines(Paths.get(fileName));
			for (String s : readAllLines) {
				addCipherText(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addCipherText(String s) {
		TextLine line = new TextLine(s);
		ciphers.add(line);
	}

	void shiftLines(String[] components) {
		String shiftAmount = components[1];
		int amt = Integer.parseInt(shiftAmount);
		for (TextLine line:ciphers) {
			map(line.toString(), TextLine.shift(line, amt).toString());
		}
	}

	void printSolutionSoFar() {
		ArrayList<TextLine> solution = new ArrayList<>();
		for (TextLine line : ciphers) {
			solution.add(applyMap(line));
		}
		for (int i = 0; i < ciphers.size(); i++) {
			System.out.println(ciphers.get(i));
			System.out.println(solution.get(i));
		}
	}

	private TextLine applyMap(TextLine line) {
		char[] chars = new char[line.length()];
		for (int i = 0; i < line.chars.length; i++) {
			if (line.chars[i] < 'a' || line.chars[i] > 'z') {
				chars[i] = line.chars[i];
			} else
				chars[i] = charMap.getOrDefault(line.chars[i], '-');
		}
		return new TextLine(chars);
	}

	void printCipherText() {
		for (TextLine line : ciphers) {
			System.out.println(line);
		}
	}

	/**
	 * Constructs a pattern string from the letters in s Ex. apple - 12234 Ex.
	 * banana - 123232 Ex. system - 121345
	 * 
	 * NOTE: word being patterned should contain <=26 unique characters
	 * 
	 * @param s
	 * @return a pattern as demonstrated in the examples
	 */
	public static String patternize(String s) {
		int next = 1;
		char[] orig = s.toCharArray();
		int[] keys = new int[128];
		for (int i = 0; i < orig.length; i++) {
			if (keys[orig[i]] == 0) { // have never replaced this character
				keys[orig[i]] = next;
				next++;
			}
		}
		StringBuilder sb = new StringBuilder();
		for (char c : orig) {
			// start from 'a' so will look good if printed
			sb.append((char) (keys[c] + 96));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		Escapist e = new Escapist();
		e.run();
	}

}
