package com.uchump.prime.Core;

import java.util.regex.Pattern;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class DefaultResources {

	public static BitmapFont DEFAULT_FONT;
	public static final String DEFAULT_FITTING = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

	public static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	public static final String HexDigits = "0123456789ABCDEF";
	public static final String HEX = "0123456789ABCDEF";

	public static final String PERMISSIBLE_CHARS = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmno"
			+ "pqrstuvwxyz{|}~Â¡Â¢Â£Â¤Â¥Â¦Â§Â¨Â©ÂªÂ«Â¬Â®Â¯Â°Â±Â²Â³Â´ÂµÂ¶Â·Â¸Â¹ÂºÂ»Â¼Â½Â¾Â¿Ã€Ã?Ã‚ÃƒÃ„Ã…Ã†Ã‡ÃˆÃ‰ÃŠÃ‹ÃŒÃ?ÃŽÃ?Ã?Ã‘Ã’Ã“Ã�?Ã•Ã–Ã—Ã˜Ã™ÃšÃ›ÃœÃ?ÃžÃŸÃ Ã¡"
			+ "Ã¢Ã£Ã¤Ã¥Ã¦Ã§Ã¨Ã©ÃªÃ«Ã¬Ã­Ã®Ã¯Ã°Ã±Ã²Ã³Ã´ÃµÃ¶Ã·Ã¸Ã¹ÃºÃ»Ã¼Ã½Ã¾Ã¿Ä€Ä?Ä‚ÄƒÄ„Ä…Ä†Ä‡ÄˆÄ‰ÄŠÄ‹ÄŒÄ?ÄŽÄ?Ä?Ä‘Ä’Ä“Ä�?Ä•Ä–Ä—Ä˜Ä™ÄšÄ›ÄœÄ?ÄžÄŸÄ Ä¡Ä¢Ä£Ä¤Ä¥Ä¦Ä§Ä¨Ä©ÄªÄ«Ä¬Ä­Ä®Ä¯Ä°Ä±"
			+ "Ä´ÄµÄ¶Ä·Ä¹ÄºÄ»Ä¼Ä½Ä¾Ä¿Å€Å?Å‚ÅƒÅ„Å…Å†Å‡ÅˆÅŠÅ‹ÅŒÅ?ÅŽÅ?Å?Å‘Å’Å“Å�?Å•Å–Å—Å˜Å™ÅšÅ›ÅœÅ?ÅžÅŸÅ Å¡Å¢Å£Å¤Å¥Å¨Å©ÅªÅ«Å¬Å­Å®Å¯Å°Å±Å²Å³Å´ÅµÅ¶Å·Å¸Å¹ÅºÅ»Å¼Å½Å¾Å¿Æ’ÇºÇ»Ç¼Ç½Ç¾Ç¿"
			+ "È˜È™ÈšÈ›È·Ë†Ë‡Ë‰Ë‹Ë˜Ë™ËšË›ËœË?Î„Î…Î†Î‡ÎˆÎ‰ÎŠÎŒÎŽÎ?Î?Î‘Î’Î“Î�?Î•Î–Î—Î˜Î™ÎšÎ›ÎœÎ?ÎžÎŸÎ Î¡Î£Î¤Î¥Î¦Î§Î¨Î©ÎªÎ«Î¬Î­Î®Î¯Î°Î±Î²Î³Î´ÎµÎ¶Î·Î¸Î¹ÎºÎ»Î¼Î½Î¾Î¿�?€�??�?‚�?ƒ�?„�?…"
			+ "�?†�?‡�?ˆ�?‰�?Š�?‹�?Œ�??�?Ž�?€�??�?‚�?ƒ�?„�?…�?†�?‡�?ˆ�?‰�?Š�?‹�?Œ�??�?Ž�??�??�?‘�?’�?“�?�?�?•�?–�?—�?˜�?™�?š�?›�?œ�??�?ž�?Ÿ�? �?¡�?¢�?£�?¤�?¥�?¦�?§�?¨�?©�?ª�?«�?¬�?­�?®�?¯�?°�?±�?²�?³�?´�?µ�?¶�?·�?¸�?¹�?º�?»�?¼�?½�?¾�?¿Ñ€Ñ?Ñ‚ÑƒÑ„Ñ…Ñ†"
			+ "Ñ‡ÑˆÑ‰ÑŠÑ‹ÑŒÑ?ÑŽÑ?Ñ?Ñ‘Ñ’Ñ“Ñ�?Ñ•Ñ–Ñ—Ñ˜Ñ™ÑšÑ›ÑœÑ?ÑžÑŸÑ´ÑµÒ?Ò‘áº€áº?áº‚áºƒáº„áº…á»²á»³â€“â€�?â€˜â€™â€šâ€›â€œâ€?â€žâ€ â€¡â€¢â€¦â€°â€¹â€ºâ?¿â‚¤â‚¬â„–â„¢â„¦â„®â†?â†‘â†’â†“âˆ†âˆ’âˆšâ‰ˆ"
			+ "â�?€â�?‚â�?Œâ�??â�?�?â�?˜â�?œâ�?¤â�?¬â�?´â�?¼â•?â•‘â•’â•“â•�?â••â•–â•—â•˜â•™â•šâ•›â•œâ•?â•žâ•Ÿâ• â•¡â•¢â•£â•¤â•¥â•¦â•§â•¨â•©â•ªâ•«â•¬â– â–¡â–²â–¼â—‹â—?â—¦â™€â™‚â™ â™£â™¥â™¦â™ª";

	public static final String BOX_DRAWING_SINGLE = "â�?€â�?‚â�?Œâ�??â�?�?â�?˜â�?œâ�?¤â�?¬â�?´â�?¼";
	public static final String BOX_DRAWING_DOUBLE = "â•?â•‘â•�?â•—â•šâ•?â• â•£â•¦â•©â•¬";
	public static final String BOX_DRAWING = "â�?€â�?‚â�?Œâ�??â�?�?â�?˜â�?œâ�?¤â�?¬â�?´â�?¼â•?â•‘â•’â•“â•�?â••â•–â•—â•˜â•™â•šâ•›â•œâ•?â•žâ•Ÿâ• â•¡â•¢â•£â•¤â•¥â•¦â•§â•¨â•©â•ªâ•«â•¬";
	public static final String VISUAL_SYMBOLS = "â†?â†‘â†’â†“â– â–¡â–²â–¼â—‹â—?â—¦â™€â™‚â™ â™£â™¥â™¦â™ª";
	public static final String DIGITS = "0123456789";
	public static final String MARKS = "~`^'Â¨Â¯Â°Â´Â¸Ë†Ë‡Ë‰Ë‹Ë˜Ë™ËšË›ËœË?Î„Î…â€˜â€™â€šâ€›";

	public static final String GROUPING_SIGNS_OPEN = "([{<Â«â€˜â€›â€œâ€¹";
	public static final String GROUPING_SIGNS_CLOSE = ")]}>Â»â€™â€™â€?â€º";
	public static final String COMMON_PUNCTUATION = "!\"%&'*+,-./:;<>?â€¢â€¦â€“â€�?";
	public static final String MODERN_PUNCTUATION = "@\\^_`|~Â¦Â©Â®â„¢Â´â„–â™€â™‚â™ª";
	public static final String UNCOMMON_PUNCTUATION = "Â§Â¶Â¨ÂªÂºÂ¯Â°Â·Â¸Â¡Â¿Î‡â€šâ€žâ€ â€¡";
	public static final String TECHNICAL_PUNCTUATION = "#%'*+,-./<=>^|Â¬Â°ÂµÂ±Â¹Â²Â³â?¿Â¼Â½Â¾Ã—Ã·â€°â„–â„¦â„®âˆ†âˆ’âˆšâ‰ˆ";
	public static final String PUNCTUATION = COMMON_PUNCTUATION + MODERN_PUNCTUATION + UNCOMMON_PUNCTUATION
			+ TECHNICAL_PUNCTUATION + GROUPING_SIGNS_OPEN + GROUPING_SIGNS_CLOSE;
	public static final String CURRENCY = "$Â¢Â£Â¤Â¥â‚¤â‚¬";
	public static final String SPACING = " ";

	public static final String ENGLISH_LETTERS_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ENGLISH_LETTERS_LOWER = "abcdefghijklmnopqrstuvwxyz";
	public static final String ENGLISH_LETTERS = ENGLISH_LETTERS_UPPER + ENGLISH_LETTERS_LOWER;

	public static final String LATIN_EXTENDED_LETTERS_UPPER = "Ã€Ã?Ã‚ÃƒÃ„Ã…Ã†Ã‡ÃˆÃ‰ÃŠÃ‹ÃŒÃ?ÃŽÃ?Ã?Ã‘Ã’Ã“Ã�?Ã•Ã–Ã˜Ã™ÃšÃ›ÃœÃ?ÃžÄ€Ä‚Ä„Ä†ÄˆÄŠÄŒÄŽÄ?Ä’Ä�?Ä–Ä˜ÄšÄœÄžÄ Ä¢Ä¤Ä¦Ä¨ÄªÄ¬Ä®Ä°Ä´Ä¶Ä¹Ä»Ä½Ä¿Å?ÅƒÅ…Å‡ÅŠÅŒÅŽÅ?Å’Å�?Å–Å˜ÅšÅœÅžÅ Å¢Å¤Å¨ÅªÅ¬Å®Å°Å²Å´Å¶Å¸Å¹Å»Å½ÇºÇ¼Ç¾È˜Èšáº€áº‚áº„á»²ÃŸSFJ";
	public static final String LATIN_EXTENDED_LETTERS_LOWER = "Ã Ã¡Ã¢Ã£Ã¤Ã¥Ã¦Ã§Ã¨Ã©ÃªÃ«Ã¬Ã­Ã®Ã¯Ã°Ã±Ã²Ã³Ã´ÃµÃ¶Ã¸Ã¹ÃºÃ»Ã¼Ã½Ã¾Ä?ÄƒÄ…Ä‡Ä‰Ä‹Ä?Ä?Ä‘Ä“Ä•Ä—Ä™Ä›Ä?ÄŸÄ¡Ä£Ä¥Ä§Ä©Ä«Ä­Ä¯Ä±ÄµÄ·ÄºÄ¼Ä¾Å€Å‚Å„Å†ÅˆÅ‹Å?Å?Å‘Å“Å•Å—Å™Å›Å?ÅŸÅ¡Å£Å¥Å©Å«Å­Å¯Å±Å³ÅµÅ·Ã¿ÅºÅ¼Å¾Ç»Ç½Ç¿È™È›áº?áºƒáº…á»³ÃŸÅ¿Æ’È·";
	public static final String LATIN_EXTENDED_LETTERS = LATIN_EXTENDED_LETTERS_UPPER + LATIN_EXTENDED_LETTERS_LOWER;

	public static final String LATIN_LETTERS_UPPER = ENGLISH_LETTERS_UPPER + LATIN_EXTENDED_LETTERS_UPPER;
	public static final String LATIN_LETTERS_LOWER = ENGLISH_LETTERS_LOWER + LATIN_EXTENDED_LETTERS_LOWER;
	public static final String LATIN_LETTERS = LATIN_LETTERS_UPPER + LATIN_LETTERS_LOWER;

	public static final String ITALIC_LETTERS_UPPER = "Î‘Î’Î“Î�?Î•Î–Î—Î˜Î™ÎšÎ›ÎœÎ?ÎžÎŸÎ Î¡Î£Î£Î¤Î¥Î¦Î§Î¨Î©Î†ÎˆÎ‰ÎŠÎŒÎŽÎ?ÎªÎ«ÎªÎ«";
	public static final String ITALIC_LETTERS_LOWER = "Î±Î²Î³Î´ÎµÎ¶Î·Î¸Î¹ÎºÎ»Î¼Î½Î¾Î¿�?€�??�?‚�?ƒ�?„�?…�?†�?‡�?ˆ�?‰Î¬Î­Î®Î¯�?Œ�??�?Ž�?Š�?‹Î?Î°";

	public static final String GREEK_LETTERS_UPPER = "ΑΒΓΔΕΖΗΘΙΚΛΜ�?ΞΟΠΡΣΣΤΥΦΧΨΩΆΈΉΊΌΎ�?ΪΫΪΫ";
	public static final String GREEK_LETTERS_LOWER = "αβγδεζηθικλμνξοπ�?ςστυφχψωάέήίό�?ώϊϋ�?ΰ";

	public static final String GREEK_LETTERS = GREEK_LETTERS_UPPER + GREEK_LETTERS_LOWER;

	public static final String CYRILLIC_ITALIC_LETTERS_UPPER = "�??�?‘�?’�?“�?�?�?•�?–�?—�?˜�?™�?š�?›�?œ�??�?ž�?Ÿ�? �?¡�?¢�?£�?¤�?¥�?¦�?§�?¨�?©�?ª�?«�?¬�?­�?®�?¯�?€�??�?‚�?ƒ�?„�?…�?†�?‡�?ˆ�?‰�?Š�?‹�?Œ�??�?Ž�??Ñ´Ò?";
	public static final String CYRILLIC_ITALIC_LETTERS_LOWER = "�?°�?±�?²�?³�?´�?µ�?¶�?·�?¸�?¹�?º�?»�?¼�?½�?¾�?¿Ñ€Ñ?Ñ‚ÑƒÑ„Ñ…Ñ†Ñ‡ÑˆÑ‰ÑŠÑ‹ÑŒÑ?ÑŽÑ?Ñ?Ñ‘Ñ’Ñ“Ñ�?Ñ•Ñ–Ñ—Ñ˜Ñ™ÑšÑ›ÑœÑ?ÑžÑŸÑµÒ‘";

	public static final String CYRILLIC_LETTERS_UPPER = "�?БВГДЕЖЗИЙКЛМ�?ОПРСТУФХЦЧШЩЪЫЬЭЮЯЀ�?ЂЃЄЅІЇЈЉЊЋЌ�?Ў�?Ѵ�?";
	public static final String CYRILLIC_LETTERS_LOWER = "абвгдежзийклмнопр�?туфхцчшщъыь�?ю�?�?ёђѓєѕіїјљњћќ�?ўџѵґ";

	public static final String CYRILLIC_LETTERS = CYRILLIC_LETTERS_UPPER + CYRILLIC_LETTERS_LOWER;

	public static final String LETTERS_UPPER = LATIN_LETTERS_UPPER + GREEK_LETTERS_UPPER + CYRILLIC_LETTERS_UPPER;
	public static final String LETTERS_LOWER = LATIN_LETTERS_LOWER + GREEK_LETTERS_LOWER + CYRILLIC_LETTERS_LOWER;
	public static final String LETTERS = LETTERS_UPPER + LETTERS_LOWER;
	public static final String LETTERS_AND_NUMBERS = LETTERS + DIGITS;

	static final String NEHE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n" //
			+ "abcdefghijklmnopqrstuvwxyz\n1234567890 \n" //
			+ "\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*\u0000\u007F";

	public static final String VECTOR_LABELS = "xyzwabcdefghijklmnopqrstuv";

	public DefaultResources() {
		DEFAULT_FONT = new BitmapFont();

	}

	private static final char[] empty = new char[0];
	
	
	// Let-ers lol
	public static String SUPER_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklnopqrstuvwxyzÀ�?ÂÃÄÅÆÇÈÉÊËÌ�?Î�?�?ÑÒÓÔÕÖØÙÚÛÜ�?ÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿĀ�?ĂăĄąĆćĈĉĊċČ�?Ď�?�?đĒēĔĕĖėĘęĚěĜ�?ĞğĠġĢģĤĥĦħĨĩĪīĬĭĮįİıĴĵĶķĸĹĺĻļĽľĿŀ�?łŃńŅņŇňŉŌ�?Ŏ�?�?őŒœŔŕŖŗŘřŚśŜ�?ŞşŠšŢţŤťŦŧŨũŪūŬŭŮůŰűŲųŴŵŶŷŸŹźŻżŽžǾǿȘșȚțΓΔΘΛΞΠΣΦΨΩαβγ";
	public static String SYMBOLS = DIGITS + SUPER_LETTERS;
	public static String BINARY_LOGIC_SYMBOLS;
	public static String BIT_LOGIC_SYMBOLS;

	public static final String emailRegexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
			+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	public static final Pattern EmailRegex = Pattern.compile(emailRegexPattern);

}
