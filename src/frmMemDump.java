import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;

public class frmMemDump extends JDialog {
	private final int	MAX_DUMP_LENGTH	= 128;

	private JTextField	txtStartingAddress;
	private JButton		btnShowMemory;
	private JScrollPane	scrollPane;
	private JTextArea	txtMem;

	private byte[][]	_data			= null;
	private JButton		btnClose;

	/**
	 * Create the dialog.
	 */
	public frmMemDump() {
		setBounds(100, 100, 563, 424);
		getContentPane().setLayout(null);

		JLabel lblStartingAddress = new JLabel("Starting Address:");
		lblStartingAddress.setBounds(6, 20, 133, 16);
		getContentPane().add(lblStartingAddress);

		txtStartingAddress = new JTextField();
		txtStartingAddress.setBounds(121, 14, 190, 28);
		getContentPane().add(txtStartingAddress);
		txtStartingAddress.setColumns(10);

		btnShowMemory = new JButton("Show Memory");
		btnShowMemory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMemory();
			}
		});
		btnShowMemory.setBounds(323, 15, 117, 29);
		getContentPane().add(btnShowMemory);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 48, 551, 348);
		getContentPane().add(scrollPane);

		txtMem = new JTextArea();
		txtMem.setFont(new Font("Lucida Console", Font.PLAIN, 11));
		txtMem.setEditable(false);
		scrollPane.setViewportView(txtMem);

		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmMemDump.this.setVisible(false);
			}
		});
		btnClose.setBounds(440, 15, 117, 29);
		getContentPane().add(btnClose);

	}

	public void setMemory(byte[][] data) {
		this._data = data;
	}

	public void setStartingAddress(String value) {
		this.txtStartingAddress.setText(value);
	}

	public void showMemory() {
		if (txtStartingAddress.getText().length() == 0)
			return;

		txtMem.setText("");
		txtMem.append("        0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f\n");
		StringBuilder sb = new StringBuilder();
		try {
			int startAddress = Integer.parseInt(txtStartingAddress.getText(),
					16);
			int bankNum = _data.length;
			int bankSize = _data[0].length;
			int bankid = 0;
			int count = 0;
			int outputcount = 0;
			int linenum = startAddress;
			txtMem.append(String.format("%05X: ", linenum));
			linenum += 16;
			while (outputcount < MAX_DUMP_LENGTH) {
				if (startAddress >= bankSize) {
					bankid = 1;
					startAddress = 0;
				}

				byte v = _data[bankid][startAddress++];
				char c = (char) v;
				if (c < 32 || c > 126)
					c = '.';
				sb.append(c);
				txtMem.append(String.format("%02X ", v));
				outputcount++;
				count++;
				if (count == 16) {
					txtMem.append(String.format(" %s\n%05X: ", sb.toString(),
							linenum));
					linenum += 16;
					count = 0;
					sb.delete(0, sb.length());
				}
			}
		} catch (NumberFormatException err) {
			JOptionPane.showMessageDialog(null,
					"Address must be HEX numbers (without preceding 0x).",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
