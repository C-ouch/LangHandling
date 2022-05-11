package main;

/*
 * TextAreaDemo.java requires no other files.
 */

import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import javax.swing.GroupLayout.*;




public class Main extends JFrame
        implements DocumentListener {

    private JLabel jLabel1;
    private JScrollPane jScrollPane1;
    private JTextArea textArea;

    private static final String COMMIT_ACTION = "commit";
    private static enum Mode { INSERT, COMPLETION };
    private final List<String> words;
    //private final Map<String, String> words;
    //Map<String, String> words = new Map<>();
    private Mode mode = Mode.INSERT;

    private String currWord ="";
    private ArrayList<String> prevWords;
    private int startPos;


    public Main() {
        super("TextAreaDemo");
        initComponents();

        textArea.getDocument().addDocumentListener(this);

        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION);
        am.put(COMMIT_ACTION, new CommitAction());
        // Map<String, ArrayList<String>> words = new Map<String, ArrayList<String>>();
        words = new ArrayList<String>(5);

        File file = new File("big.txt");
        Scanner sin = null;
        try {
            sin = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sin.hasNext()) {
            words.add(sin.next());

        }

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

        int pos = ev.getOffset();
        String content = null;
        try {
            content = textArea.getText(0, pos + 1);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        // Find where the word starts

//        currWord = "";
//
//        if(content.charAt(pos) != ' '){
//            currWord = currWord + content.charAt(pos);
//        }

//        int endPos =0;
//
//        if(content.charAt(pos) == ' '){
//            endPos = pos-1;
//        }

        /*
         * I put an IF statement below to check if the word has ended
         * The ELSE statement connected to it to get the word between the positions with substring()
         * */

        if (Character.isLetter(content.charAt(pos))) {
            int w;
            for (w = pos; w >= 0; w--) {

                if (!Character.isLetter(content.charAt(w))) { //This checks if there is a whitespace and if so it breaks and only stops at the position before the whitespace
                    break;
                }
            }
            System.out.println("w: " + w);

            startPos = w;

            if (pos - w < 2) { //NEED THiS OR THERE IS AN ERROR
                // Too few chars
                return;
            }

            String prefix = content.substring(startPos + 1).toLowerCase();
            //
            int n = Collections.binarySearch(words, prefix);
            if (n < 0 && -n <= words.size()) {
                String match = words.get(-n - 1);
                if (match.startsWith(prefix)) {
                    // A completion is found
                    String completion = match.substring(pos - startPos);
                    // We cannot modify Document from within notification,
                    // so we submit a task that does the change later
                    SwingUtilities.invokeLater(
                            new CompletionTask(completion, pos + 1));
                }
            } else {
                // Nothing found
                mode = Mode.INSERT;
            }
        }else{ //This keeps track of if there is a space and keeps the position values we need

            /*
             * Still some stuff wrong with it. endpos can be wrong due to if a period gets typed and then whitespace.
             * Try it out to see what  I mean in TextBox Class
             * */
            int endPos = pos;
            System.out.println("endPos: " + endPos);
            System.out.println("char_end: " + content.charAt(endPos));


            System.out.println("startPos: " + startPos);
            System.out.println("char_startPos: " + content.charAt(startPos + 1));

            currWord = content.substring(startPos, endPos).toLowerCase();
            System.out.println(currWord);

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
            textArea.setCaretPosition(position + completion.length());
            textArea.moveCaretPosition(position);
            mode = Mode.COMPLETION;
        }
    }

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
                new Main().setVisible(true);
            }
        });
    }


}