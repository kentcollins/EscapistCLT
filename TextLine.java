import java.util.Arrays;

public class TextLine {

	char[] chars;

	TextLine(String text) {
		chars = text.toCharArray();
	}

	TextLine(int length) {
		chars = new char[length];
	}
	
	TextLine(char[] chars) {
		this.chars = chars;
	}

	public String toString() {
		return new String(chars);
	}

	static TextLine shift(TextLine orig, int amount) {
		char[] result = Arrays.copyOf(orig.chars, orig.chars.length);
		amount %= 26; // [-25, 25]
		if (amount<0) amount+=26; // [0, 25]
		for (int i = 0; i < orig.chars.length; i++) {
			if (Character.isUpperCase( result[i])) {
				result[i] += amount;
				if (result[i] > 'Z')
					result[i] -= 26;
			} else if (Character.isLowerCase(result[i])) {
				result[i] += amount;
				if (result[i] > 'z')
					result[i] -= 26;
			}
			// non alphabetic characters disregarded
		}
		return new TextLine(result);
	}
	
	public static void main(String[] args) {
		TextLine sample = new TextLine("This is an example.");
		System.out.println(shift(sample, 1));
		System.out.println(shift(sample, 27));
		System.out.println(shift(sample, -1));
		System.out.println(shift(sample, -27));
	}

}
