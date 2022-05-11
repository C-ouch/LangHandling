package main;

/*
 * TextAreaDemo.java requires no other files.
 */

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.GroupLayout.*;




public class TextBox extends JFrame
        implements DocumentListener {

    private JLabel jLabel1;
    private JScrollPane jScrollPane1;
    private JTextArea textArea;

    private static final String COMMIT_ACTION = "commit";
    private static enum Mode { INSERT, COMPLETION };

    private ArrayList<String> prevWords;
    private ArrayList<String> books;
    private HashMap<String, Integer> possibleWords;

    private Mode mode = Mode.INSERT;

    private int startPos;
    private int endPos;
    private char endChar;
    private String bugFix = null;

    boolean punctuation = false;
    private String currWord ="";



    public TextBox() throws FileNotFoundException {
        super("TextAreaDemo");
        initComponents();

        textArea.getDocument().addDocumentListener(this);

        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();
        im.put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        am.put(COMMIT_ACTION, new CommitAction());

        books = new ArrayList<String>();
        File file = new File("big.txt");
        Scanner input = new Scanner(file);
        while(input.hasNext()){
            books.add(input.next().toLowerCase());
        }
        prevWords = new ArrayList<String>(3);
        possibleWords = new HashMap<String, Integer>();
    }


    private void initComponents() {
        jLabel1 = new JLabel("Try typing 'spectacular' or 'Swing'...");

        textArea = new JTextArea();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);

        jScrollPane1 = new JScrollPane(textArea);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        //Create a parallel group for the horizontal axis
        ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        //Create a sequential and a parallel groups
        SequentialGroup h1 = layout.createSequentialGroup();
        ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
        //Add a scroll panel and a label to the parallel group h2
        h2.addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE);
        h2.addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE);

        //Add a container gap to the sequential group h1
        h1.addContainerGap();
        // Add the group h2 to the group h1
        h1.addGroup(h2);
        h1.addContainerGap();
        //Add the group h1 to hGroup
        hGroup.addGroup(Alignment.TRAILING,h1);
        //Create the horizontal group
        layout.setHorizontalGroup(hGroup);

        //Create a parallel group for the vertical axis
        ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        //Create a sequential group
        SequentialGroup v1 = layout.createSequentialGroup();
        //Add a container gap to the sequential group v1
        v1.addContainerGap();
        //Add a label to the sequential group v1
        v1.addComponent(jLabel1);
        v1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        //Add scroll panel to the sequential group v1
        v1.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE);
        v1.addContainerGap();
        //Add the group v1 to vGroup
        vGroup.addGroup(v1);
        //Create the vertical group
        layout.setVerticalGroup(vGroup);
        pack();

    }
    // Listener methods

    public void changedUpdate(DocumentEvent ev) {
    }

    public void removeUpdate(DocumentEvent ev) {
    }

    public void insertUpdate(DocumentEvent ev) {

        if (ev.getLength() != 1) {
            return;
        }

        int pos = ev.getOffset(); // getOffset() Gives position off of 'ev'
//        System.out.println(pos);

        String content = null;
        try {
            content = textArea.getText(0, pos + 1);
            System.out.println("Content: " + content);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }


        System.out.println("pos: " + pos);
        System.out.println("char_pos: " + content.charAt(pos));

        // Find where the word starts
        if (Character.isLetter(content.charAt(pos))) {
            int w;
            for (w = pos; w >= 0; w--) {

//                if (!Character.isLetter(content.charAt(w))) { //This checks if there is a non-alphabet and if so it breaks and only stops at the position before the whitespace
                if (content.charAt(w) == ' ') {
                    break;
                }
            }
            System.out.println("w: " + w);

            startPos = w;

            if (pos - w < 2) {
                // Too few chars
                return;
            }

            System.out.println("char_w: " + content.charAt(w + 1));

           
        }else{

            endPos = pos;


            currWord = content.substring(startPos+1,endPos);

            //This replaces all none alphabetical numbers with empty space and then lower cases them
            currWord = currWord.replaceAll("[^A-Za-z]+", "").toLowerCase();


           if(prevWords.size() < 3){
                prevWords.add(currWord);
            }
            else{
                if(bugFix == null) {
                    prevWords.remove(0);
                    prevWords.add(currWord);
                }else{
                    prevWords.remove(0);
                    prevWords.add(bugFix);
                    currWord = "";
                    bugFix = null;
                }

            }

            if(prevWords.size() >= 3){
                for(int i = 0; i < books.size(); i++){
                    if(books.get(i).matches(prevWords.get(0))){
                        if(books.get(i+1).matches(prevWords.get(1))){
                            if(books.get(i+2).matches(prevWords.get(2))){
                                if(possibleWords.get(books.get(i+3)) != null) {
                                    int freq = possibleWords.get(books.get(i + 3));
                                    possibleWords.replace(books.get(i + 3),freq,freq+1);
                                }
                                else{
                                    possibleWords.put(books.get(i+3),1);
                                    System.out.println("New Word Added: " + possibleWords.get(books.get(i+3)));
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Current Word: " + currWord+"\n");
            String mostCommon = "";
            int max = 0;
            for (String key : possibleWords.keySet()) {
                if (possibleWords.get(key) > max){
                    max = possibleWords.get(key);
                    mostCommon = key;
                }
                System.out.println(key + ":" + possibleWords.get(key));
            }
            if(max == 0){
                mode = Mode.INSERT;
            }
            else{
                possibleWords.clear();
                System.out.println("Most Common: " + mostCommon);
                SwingUtilities.invokeLater(
                        new CompletionTask(mostCommon, pos + 1));
            }


        }
    }

    private class CompletionTask implements Runnable {
        String completion;
        int position;

        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        public void run() {
            textArea.insert(completion, position);
            bugFix = completion;

//            textArea.insert("cool",position);
            textArea.setCaretPosition(position + completion.length()); //setCaretPosition allows for changing predictive words after shown.  (Mouse highlighting text thing)
            textArea.moveCaretPosition(position);                      //Moves start of highlighting
            mode = Mode.COMPLETION;
        }
    }

    //When tab is hit this is the auto fill
    private class CommitAction extends AbstractAction {
        public void actionPerformed(ActionEvent ev) {
            if (mode == Mode.COMPLETION) {
                int pos = textArea.getSelectionEnd();
                textArea.insert(" ", pos);
                textArea.setCaretPosition(pos + 1);
                mode = Mode.INSERT;
            } else {
                textArea.replaceSelection("\n");
            }
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                try {
                    new TextBox().setVisible(true);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }}


