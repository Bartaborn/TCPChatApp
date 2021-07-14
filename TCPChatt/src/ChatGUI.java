import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


@SuppressWarnings("serial") // Vad i helvete �r det h�r?
public class ChatGUI extends JFrame {
	private final JTextField m_chatInput;
	private final JTextArea m_chatOutput;

	public ChatGUI(ActionListener listener, String userName) {
			setSize(700,500);
			setTitle("Chat client for "+ userName);
			m_chatOutput = new JTextArea(10,15);
			m_chatInput = new JTextField(20);
			m_chatOutput.setWrapStyleWord(true);
			m_chatOutput.setLineWrap(true);
			m_chatOutput.setEditable(false);
			m_chatOutput.setBackground(Color.BLACK);
			m_chatOutput.setForeground(Color.GREEN);
			Container pane = getContentPane();
			pane.add(m_chatOutput, BorderLayout.NORTH);
			pane.add(m_chatInput, BorderLayout.SOUTH);
			pane.add(new JScrollPane(m_chatOutput), BorderLayout.CENTER);
			m_chatInput.addActionListener(listener);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
	}
	
	public void setInput(String message) {
		m_chatOutput.append(message+"\n"); //L�gger till
	}

	public String getInput() {
		return m_chatInput.getText();
	}
	
	public void clearInput() {
		m_chatInput.setText("");
	}

}