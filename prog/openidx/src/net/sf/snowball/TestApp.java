
package net.sf.snowball;

import java.lang.reflect.Method;
import java.io.Reader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.FileOutputStream;

/**
 *
 * @author xtern
 */
public class TestApp {

    /**
     *
     * @param args
     * @throws Throwable
     */
    public static void main(String [] args) throws Throwable {
	Class stemClass = Class.forName("net.sf.snowball.ext." +
					args[0] + "Stemmer");
        SnowballProgram stemmer = (SnowballProgram) stemClass.newInstance();
	Method stemMethod = stemClass.getMethod("stem", new Class[0]);

	Reader reader;
	reader = new InputStreamReader(new FileInputStream(args[1]));
	reader = new BufferedReader(reader);

	StringBuffer input = new StringBuffer();

        OutputStream outstream;

	if (args.length > 2 && args[2].equals("-o")) {
	    outstream = new FileOutputStream(args[3]);
	} else if (args.length == 2) {
	    System.err.println("Usage: TestApp <input file> [-o <output file>]");
	    return;
	} else {
	    outstream = System.out;
	}
	Writer output = new OutputStreamWriter(outstream);
	output = new BufferedWriter(output);

	int repeat = 1;
	if (args.length > 4) {
	    repeat = Integer.parseInt(args[4]);
	}

	Object [] emptyArgs = new Object[0];
	int character;
	while ((character = reader.read()) != -1) {
	    char ch = (char) character;
	    if (Character.isWhitespace((char) ch)) {
		if (input.length() > 0) {
		    stemmer.setCurrent(input.toString());
		    for (int i = repeat; i != 0; i--) {
			stemMethod.invoke(stemmer, emptyArgs);
		    }
		    output.write(stemmer.getCurrent());
		    output.write('\n');
		    input.delete(0, input.length());
		}
	    } else {
		input.append(Character.toLowerCase(ch));
	    }
	}
	output.flush();
    }
}
