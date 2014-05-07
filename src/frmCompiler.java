import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import cisc_sim.Encoder;
import cisc_sim.EncoderEventListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

public class frmCompiler extends JDialog implements EncoderEventListener {
	private JTextField	txtSourceFile;
	private JButton		btnSelectSourceFile;
	private File		_sourceFile			= null;
	private boolean		_compiledSucceeded	= false;

	/**
	 * Create the dialog.
	 */
	public frmCompiler() {
		setBounds(100, 100, 340, 104);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Compiler");
		getContentPane().setLayout(null);

		JLabel lblSource = new JLabel("Source:");
		lblSource.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSource.setBounds(6, 12, 54, 16);
		getContentPane().add(lblSource);

		txtSourceFile = new JTextField();
		txtSourceFile.setEditable(false);
		txtSourceFile.setColumns(10);
		txtSourceFile.setBounds(72, 6, 212, 28);
		getContentPane().add(txtSourceFile);

		btnSelectSourceFile = new JButton(">>");
		btnSelectSourceFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser(new File(
						MainGui.__INIT_DIR__));
				if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(null)) {
					_sourceFile = fc.getSelectedFile();
					txtSourceFile.setText(_sourceFile.getAbsolutePath());
				}
			}
		});
		btnSelectSourceFile.setBounds(296, 13, 37, 16);
		getContentPane().add(btnSelectSourceFile);

		JButton btnCompile = new JButton("Compile");
		btnCompile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (_sourceFile == null || !_sourceFile.exists()) {
					JOptionPane.showMessageDialog(null,
							"An existing SOURCE file must be selected.",
							"Error", JOptionPane.ERROR_MESSAGE);
					txtSourceFile.setText("");
					btnSelectSourceFile.doClick();
					return;
				}

				_compiledSucceeded = false;
				Encoder en = new Encoder(_sourceFile.getAbsolutePath(), 0);
				en.addEventListener(frmCompiler.this);
				en.Encode();
				if (_compiledSucceeded)
					frmCompiler.this.dispose();
			}
		});
		btnCompile.setBounds(216, 46, 117, 29);
		getContentPane().add(btnCompile);
	}

	@Override
	public void invokeEncoderSucceededEvent(String msg) {
		_compiledSucceeded = true;
		JOptionPane.showMessageDialog(null, msg, "Info",
				JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void invokeEncoderFailedEvent(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}
}
