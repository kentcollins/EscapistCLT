import java.util.Arrays;

public class TextLine implements CharSequence{

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

	@Override
	public String toString() {
		return new String(chars);
	}

	@Override
	public int length() {
		return chars.length;
	}

	@Override
	public char charAt(int index) {
		return chars[index];
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i<end; i++) {
			sb.append(chars[i]);
		}
		return sb;
	}

}
