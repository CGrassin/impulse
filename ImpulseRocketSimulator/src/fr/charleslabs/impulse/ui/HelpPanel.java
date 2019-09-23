package fr.charleslabs.impulse.ui;

import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * This JScrollPane shows the help, in
 * HTML format.
 * 
 * @author Charles Grassin
 *
 */
public class HelpPanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	/** The path of the HTML help file. */
	private static final String helpURL = "/res/help.html";

	public HelpPanel() throws IOException {
		super();
		JTextPane html = new JTextPane();
		html.setContentType("text/html");
		//JEditorPane html = new JEditorPane();
		html.setEditable(false);
		html.setPage(getClass().getResource(helpURL));
		this.getViewport().add(html);
	}
}
