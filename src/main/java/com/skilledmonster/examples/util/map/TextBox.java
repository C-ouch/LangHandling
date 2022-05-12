package com.skilledmonster.examples.util.map;

/*
 * TextAreaDemo.java requires no other files.
 */

import com.google.common.collect.Multimap;

import javax.swing.*;
import java.io.IOException;
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
    private final ArrayList<String> words;
    private Mode mode = Mode.INSERT;

    private int startPos;
    private int endPos;
    private int i=0; //used to combat multiple whitespaces after a punctuation
    private int n=0; //used to combat multiple whitespaces

    private String currWord ="";



    public TextBox() throws IOException {
        super("TextAreaDemo");
        initComponents();
        Multimap<String,String>  missMap = TextCorrect.Misspelled();

        textArea.getDocument().addDocumentListener(this);

        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();
        im.put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        am.put(COMMIT_ACTION, new CommitAction());


        words = new ArrayList<String>(5);
        words.add("spark");
        words.add("special");
        words.add("spectacles");
        words.add("spectacular");
        words.add("swing");
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

        if(pos == n && content.charAt(pos) == ' ') {
            n++;
            return;
        }

        // Find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
//          if (!Character.isLetter(content.charAt(w)))  //This checks if there is a non-alphabet and if so it breaks and only stops at the position before the whitespace
            if (content.charAt(w) == ' ')
                break;
        }

        System.out.println("w: " + w);


        if( (Pattern.matches("\\p{Punct}", Character.toString(content.charAt(pos))) || Character.isDigit(content.charAt(pos)))) {
            endPos = pos;
            i=0;
        }

//      if (Character.isLetter(content.charAt(pos))) {
        if (content.charAt(pos) != ' ') {//This checks if there is a whitespace and if so it breaks and only stops at the position before the whitespace
            i=0; //used to combat multiple whitespaces
//            System.out.println("w: " + w);

            startPos = w;



            if (pos - w < 2) {
                // Too few chars
                return;
            }

            if (Character.isDigit(content.charAt(w+1)) )//This checks if there is a numeric value and if so it returns sp there is no error
                return;

            System.out.println("char_w: " + content.charAt(w + 1));

            String prefix = content.substring(w + 1).toLowerCase();
            System.out.println("prefix: " +prefix+"\n");

            int n = Collections.binarySearch(words, prefix);
            if (n < 0 && -n <= words.size()) {
                String match = words.get(-n - 1);
                if (match.startsWith(prefix)) {
                    // A completion is found
                    String completion = match.substring(pos - w);
                    // We cannot modify Document from within notification,
                    // so we submit a task that does the change later
                    SwingUtilities.invokeLater(
                            new CompletionTask(completion, pos + 1));
                }
            } else {
                // Nothing found
                mode = Mode.INSERT;
            }
        }else{
            try {


                System.out.println("startPos: " + startPos);
                System.out.println("char_startPos: " + content.charAt(startPos + 1));

                System.out.println("endPos: " + endPos);
                System.out.println("char_end: " + content.charAt(endPos));

                System.out.println("i: " + i);

                //check for punctuation at end of word and made to not mess up with multiple spaces
                if ((pos - (endPos + i)) == 1) {
                    currWord = content.substring(startPos + 1, endPos);
                    System.out.println("here");

                } else {
                    currWord = content.substring(startPos + 1, pos);
                }

                //This replaces all none alphabetical numbers with empty space and then lower cases them
                currWord = currWord.replaceAll("[^A-Za-z|']+", "_").toLowerCase();

                System.out.println("Current Word: " + currWord + "\n");

                i++;
            }catch(IndexOutOfBoundsException iob){
                System.err.println("IndexOutOfBoundsException for a substring: "+ iob.getMessage());
            }
        }
        /*System.out.println("pos: " + pos);
        System.out.println("char_pos: " + content.charAt(pos));

        System.out.println("startPos: " + startPos);
        System.out.println("char_startPos: " + content.charAt(startPos + 1));

        System.out.println("endPos: " + endPos);
        System.out.println("char_end: " + content.charAt(endPos));

        System.out.println("Current Word: " + currWord+"\n");*/
        n++;

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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }}


