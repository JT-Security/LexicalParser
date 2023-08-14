import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

class LexicalParser {

    // Global items for easy access across both lexical analyzer and parser.
    public static String token = "";
    public static String line = "";
    public static boolean nextLine = false;
    public static String ignoreWhitespace = "";
    public static int currChar = 0;
    public static int currLine = 1;
    public static int charPos = 1;
    public static int linePos = 1;
    public static int storeLength = 0;
    public static String lexKind = "";
    public static String lexValue = "";
    public static BufferedReader buffReader;

    public static void main(String[] args) throws IOException {
        // Take in user's file name to be tokenized.
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.print("Enter text file name including the extension, it must be in the same directory as this .java file (Ex. abc.txt): ");
        String fileName = input.next();
        input.close();
        System.out.println();

        // Creates Buffer Reader to read character by character through the file.
        FileReader fileReader = new FileReader(new File(fileName));
        buffReader = new BufferedReader(fileReader);

        // Read entire line at once to be then read character by character.
        line = buffReader.readLine();

        // Traverses line by line creating tokens from the file.
        readFirstChar(line);
        // Checks if current line had ended and proceeds to next line in the file.
        while(nextLine == true) {
            line = "";
            line = buffReader.readLine();
            currLine = currLine + 1;
            currChar = 0;
            nextLine = false;
            readFirstChar(line);
        }

        // Calls the parser to begin.
        Program();
    }

    // START OF LEXICAL ANALYZER

    // Read first character of a token and decide if its an identifier, number, symbol or error.
    public static void readFirstChar(String line) throws IOException {
        if (line != null) {
            if (currChar >= 0 && currChar < line.length()) {
                if (Character.isDigit(line.charAt(currChar))) {
                    getDigit(line);
                } else if (Character.isLetter(line.charAt(currChar))) {
                    getLetter(line);
                } else if (Character.isWhitespace(line.charAt(currChar))) {
                    getWhitespace(line);
                } else if (line.charAt(currChar) == ':' || line.charAt(currChar) == ';' || line.charAt(currChar) == '<' || line.charAt(currChar) == '=' || line.charAt(currChar) == '>' || line.charAt(currChar) == '+' || line.charAt(currChar) == '-' || line.charAt(currChar) == '*' || line.charAt(currChar) == '/' || line.charAt(currChar) == '(' || line.charAt(currChar) == ')' || line.charAt(currChar) == '_' || line.charAt(currChar) == '!') {
                    getSymbol(line);
                } else {
                    charPos = currChar;
                    linePos = currLine;
                    System.out.println("Invalid character detected at Line " + linePos + ", Char " + (charPos + 1) + " -> " + line.charAt(currChar));
                    System.exit(-1);
                }
            } else {
                nextLine = true;
            }
        }
    }

    // Creates a token string containing a number.
    public static void getDigit(String line) {
        charPos = currChar;
        linePos = currLine;
        for(int i = currChar; i < line.length(); i++) {
            if(Character.isDigit(line.charAt(i))) {
                token = token + line.charAt(i);
                storeLength = token.length();
                currChar = i;
            } else {
                break;
            }
        }
        lexKind = "NUM";
        lexValue = token;
        //System.out.println("Line " + linePos + ", Char " + (charPos + 1) + ": " + "'" + lexKind + "'" + " " + lexValue);
        currChar = currChar + 1;
        token = "";
    }

    // Creates a token string containing an identifier.
    public static void getLetter(String line) {
        charPos = currChar;
        linePos = currLine;
        for(int i = currChar; i < line.length(); i++) {
            if(Character.isLetter(line.charAt(i)) || Character.isDigit(line.charAt(i)) || line.charAt(i) == '_') {
                token = token + line.charAt(i);
                storeLength = token.length();
                currChar = i;
            } else {
                break;
            }
        }
        if(token.equals("program") || token.equals("end") || token.equals("bool") || token.equals("int") || token.equals("if") || token.equals("then") || token.equals("else") || token.equals("fi") || token.equals("while") || token.equals("do") || token.equals("od") || token.equals("print") || token.equals("or") || token.equals("and") || token.equals("not") || token.equals("false") || token.equals("true")) {
            lexKind = token;
            lexValue = "";
            //System.out.println("Line " + linePos + ", Char " + (charPos + 1) + ": " + "'" + lexKind + "'");
            currChar = currChar + 1;
            token = "";
        } else {
            lexKind = "ID";
            lexValue = token;
            //System.out.println("Line " + linePos + ", Char " + (charPos + 1) + ": " + "'" + lexKind + "'" + " " + lexValue);
            currChar = currChar + 1;
            token = "";
        }
    }

    // Creates a token string containing a symbol.
    public static void getSymbol(String line) {
        charPos = currChar;
        linePos = currLine;
        if(currChar >= 0 && currChar + 1 < line.length()) {
            if(line.charAt(currChar) == ':' && line.charAt(currChar + 1) == '=') {
                token = token + line.charAt(currChar) + line.charAt(currChar + 1);
                storeLength = token.length();
                lexKind = token;
                lexValue = "";
                //System.out.println("Line " + linePos + ", Char " + (charPos + 1) + ": " + "'" + lexKind + "'");
                currChar = currChar + 2;
                token = "";
            } else if(line.charAt(currChar) == '=' && line.charAt(currChar + 1) == '<') {
                token = token + line.charAt(currChar) + line.charAt(currChar + 1);
                storeLength = token.length();
                lexKind = token;
                lexValue = "";
                //System.out.println("Line " + linePos + ", Char " + (charPos + 1) + ": " + "'" + lexKind + "'");
                currChar = currChar + 2;
                token = "";
            } else if(line.charAt(currChar) == '!' && line.charAt(currChar + 1) == '=') {
                token = token + line.charAt(currChar) + line.charAt(currChar + 1);
                storeLength = token.length();
                lexKind = token;
                lexValue = "";
                //System.out.println("Line " + linePos + ", Char " + (charPos + 1) + ": " + "'" + lexKind + "'");
                currChar = currChar + 2;
                token = "";
            } else if(line.charAt(currChar) == '>' && line.charAt(currChar + 1) == '=') {
                token = token + line.charAt(currChar) + line.charAt(currChar + 1);
                storeLength = token.length();
                lexKind = token;
                lexValue = "";
                //System.out.println("Line " + linePos + ", Char " + (charPos + 1) + ": " + "'" + lexKind + "'");
                currChar = currChar + 2;
                token = "";
            } else if(line.charAt(currChar) == '/' && line.charAt(currChar + 1) == '/') {
                nextLine = true;
            } else if(line.charAt(currChar) == '!' && line.charAt(currChar) != '=') {
                System.out.println("Invalid character detected at Line " + linePos + ", Char " + (charPos + 1) + " -> " + line.charAt(currChar));
                System.exit(-1);
            } else {
                token = token + line.charAt(currChar);
                storeLength = token.length();
                lexKind = token;
                lexValue = "";
                //System.out.println("Line " + linePos + ", Char " + (charPos + 1) + ": " + "'" + lexKind + "'");
                currChar = currChar + 1;
                token = "";
            }
        } else {
            if(line.charAt(currChar) == '!') {
                System.out.println("Invalid character detected at Line " + linePos + ", Char " + (charPos + 1) + " -> " + line.charAt(currChar));
                System.exit(-1);
            } else {
                token = token + line.charAt(currChar);
                storeLength = token.length();
                lexKind = token;
                lexValue = "";
                //System.out.println("Line " + linePos + ", Char " + (charPos + 1) + ": " + "'" + lexKind + "'");
                currChar = currChar + 1;
                token = "";
            }
        }
    }

    // Creates a dummy string to skip over whitespace.
    public static void getWhitespace(String line) throws IOException {
        for(int i = currChar; i < line.length(); i++) {
            if(Character.isWhitespace(line.charAt(i))) {
                ignoreWhitespace = ignoreWhitespace + line.charAt(i);
                currChar = i;
            } else {
                break;
            }
        }
        currChar = currChar + 1;
        readFirstChar(line);
    }

    // Creates a token string containing end-of-file.
    public static void getEOF() {
        charPos = storeLength;
        token = "end-of-file";
        lexKind = token;
        lexValue = "";
        //System.out.println("Line " + linePos + ", Char " + (charPos + 1) + ": " + "'" + lexKind + "'");
    }

    // START OF PARSER

    // Compare expected kind to current lexeme kind, then moves forward or throws an error.
    public static void Match(String passedSymbol) throws IOException {
        if(lexKind.equals(passedSymbol)) {
            readFirstChar(line);
            while(nextLine == true) {
                line = "";
                line = buffReader.readLine();
                currLine = currLine + 1;
                currChar = 0;
                nextLine = false;
                readFirstChar(line);
            }
        } else {
            System.out.println("Error at line " + linePos + ", Char " + (charPos + 1) + ": I see " + lexKind + " but expect " + passedSymbol);
            System.out.println("Parser Output: false");
            System.exit(-1);
        }
    }

    // Processes entire file and ends when end of file is reached.
    public static void Program() throws IOException {
        Match("program");
        Match("ID");
        Match(":");
        Body();
        Match("end");
        System.out.println("Parser Output: true");
    }

    // Handles body statements.
    public static void Body() throws IOException {
        if(lexKind.equals("int") || lexKind.equals("bool")) {
            Declarations();
        }
        Statements();
    }

    // Handles declarations.
    public static void Declarations() throws IOException {
        Declaration();
        while(lexKind.equals("int") || lexKind.equals("bool")) {
            Declaration();
        }
    }

    // Handles declaration statements.
    public static void Declaration() throws IOException {
        if(lexKind.equals("int") || lexKind.equals("bool")) {
            readFirstChar(line);
            while(nextLine == true) {
                line = "";
                line = buffReader.readLine();
                currLine = currLine + 1;
                currChar = 0;
                nextLine = false;
                readFirstChar(line);
            }
            Match("ID");
            Match(";");
        }
    }

    // Handles statements.
    public static void Statements() throws IOException {
        Statement();
        while(lexKind.equals(";")) {
            readFirstChar(line);
            while(nextLine == true) {
                line = "";
                line = buffReader.readLine();
                currLine = currLine + 1;
                currChar = 0;
                nextLine = false;
                readFirstChar(line);
            }
            Statement();
        }
    }

    // Handles a statement.
    public static void Statement() throws IOException {
        if(lexKind.equals("ID")) {
            AssignmentStatement();
        } else if(lexKind.equals("if")) {
            ConditionalStatement();
        } else if(lexKind.equals("while")) {
            IterativeStatement();
        } else if(lexKind.equals("print")) {
            PrintStatement();
        } else {
            //Expected();
            System.out.println("Error at line " + linePos + ", Char " + (charPos + 1) + ": I see " + lexKind + " but expected a single... ID, if, while, print");
            System.out.println("Parser Output: false");
            System.exit(-1);
        }
    }

    // Handles assignment statements.
    public static void AssignmentStatement() throws IOException {
        Match("ID");
        Match(":=");
        Expression();
    }

    // Handles expression processing.
    public static void Expression() throws IOException {
        SimpleExpression();
        if(lexKind.equals("<") || lexKind.equals(">") || lexKind.equals("=<") || lexKind.equals(">=") || lexKind.equals("!=") || lexKind.equals("=")) {
            readFirstChar(line);
            while(nextLine == true) {
                line = "";
                line = buffReader.readLine();
                currLine = currLine + 1;
                currChar = 0;
                nextLine = false;
                readFirstChar(line);
            }
            SimpleExpression();
        }
    }

    // Handles simple expression processing.
    public static void SimpleExpression() throws IOException {
        Term();
        while(lexKind.equals("+") || lexKind.equals("-") || lexKind.equals("or")) {
            readFirstChar(line);
            while(nextLine == true) {
                line = "";
                line = buffReader.readLine();
                currLine = currLine + 1;
                currChar = 0;
                nextLine = false;
                readFirstChar(line);
            }
            Term();
        }
    }

    // Handles term processing.
    public static void Term() throws IOException {
        Factor();
        while(lexKind.equals("*") || lexKind.equals("/") || lexKind.equals("and")) {
            readFirstChar(line);
            while(nextLine == true) {
                line = "";
                line = buffReader.readLine();
                currLine = currLine + 1;
                currChar = 0;
                nextLine = false;
                readFirstChar(line);
            }
            Factor();
        }
    }

    // Handles factor processing.
    public static void Factor() throws IOException {
        if(lexKind.equals("-") || lexKind.equals("not")) {
            readFirstChar(line);
            while(nextLine == true) {
                line = "";
                line = buffReader.readLine();
                currLine = currLine + 1;
                currChar = 0;
                nextLine = false;
                readFirstChar(line);
            }
        }
        if(lexKind.equals("true") || lexKind.equals("false") || lexKind.equals("NUM")) {
            Literal();
        } else if(lexKind.equals("ID")) {
            readFirstChar(line);
            while(nextLine == true) {
                line = "";
                line = buffReader.readLine();
                currLine = currLine + 1;
                currChar = 0;
                nextLine = false;
                readFirstChar(line);
            }
        } else if(lexKind.equals("(")) {
            readFirstChar(line);
            while(nextLine == true) {
                line = "";
                line = buffReader.readLine();
                currLine = currLine + 1;
                currChar = 0;
                nextLine = false;
                readFirstChar(line);
            }
            Expression();
            Match(")");
        } else {
            //Expected();
            System.out.println("Error at line " + linePos + ", Char " + (charPos + 1) + ": I see " + lexKind + " but expected a single... -, not, true, false, NUM, ID, (");
            System.out.println("Parser Output: false");
            System.exit(-1);
        }
    }

    // Handles literal processing.
    public static void Literal() throws IOException {
        if(lexKind.equals("true") || lexKind.equals("false") || lexKind.equals("NUM")) {
            if(lexKind.equals("NUM")) {
                readFirstChar(line);
                while(nextLine == true) {
                    line = "";
                    line = buffReader.readLine();
                    currLine = currLine + 1;
                    currChar = 0;
                    nextLine = false;
                    readFirstChar(line);
                }
            } else {
                BooleanLiteral();
            }
        }
    }

    // Handles boolean processing.
    public static void BooleanLiteral() throws IOException {
        if(lexKind.equals("true") || lexKind.equals("false")) {
            readFirstChar(line);
            while(nextLine == true) {
                line = "";
                line = buffReader.readLine();
                currLine = currLine + 1;
                currChar = 0;
                nextLine = false;
                readFirstChar(line);
            }
        }
    }

    // Handles conditional statements.
    public static void ConditionalStatement() throws IOException {
        Match("if");
        Expression();
        Match("then");
        Body();
        if(lexKind.equals("else")) {
            readFirstChar(line);
            while(nextLine == true) {
                line = "";
                line = buffReader.readLine();
                currLine = currLine + 1;
                currChar = 0;
                nextLine = false;
                readFirstChar(line);
            }
            Body();
        }
        Match("fi");
    }

    // Handles iterative statements.
    public static void IterativeStatement() throws IOException {
        Match("while");
        Expression();
        Match("do");
        Body();
        Match("od");
    }

    // Handles print statements.
    public static void PrintStatement() throws IOException {
        Match("print");
        Expression();
    }

}