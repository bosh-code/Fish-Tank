import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author ryanbosher
 */
public final class FishTank extends JPanel implements ActionListener {
	public static final int DELAY = 10;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	public static final int BUTTON_PANEL_HEIGHT = 40;
	private static final long serialVersionUID = -790333510812946675L;
	private final DrawPanel drawPanel;
	private final JButton addButton;
	private final FishShoal fishShoal;
	
	public FishTank() {
		super(new BorderLayout());
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
			}
		});
		
		fishShoal = new FishShoal();
		
		drawPanel = new DrawPanel();
		add(drawPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(WIDTH, BUTTON_PANEL_HEIGHT));
		addButton = new JButton("Add a Fish");
		addButton.addActionListener(this);
		
		buttonPanel.add(addButton);
		add(buttonPanel, BorderLayout.SOUTH);
		
		Timer timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public static void main(String... args) {
		JFrame fishTank = new JFrame("Fish Tank");
		
		fishTank.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		fishTank.getContentPane().add(new FishTank());
		fishTank.pack();
		
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		Dimension screenDimension = defaultToolkit.getScreenSize();
		fishTank.setSize(new Dimension(WIDTH, HEIGHT));
		fishTank.setLocation((screenDimension.width - WIDTH) / 2, (screenDimension.height - HEIGHT) / 2);
		
		fishTank.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == addButton) {
			// 50 fish test
			//						for (int i = 0; i < 50; i++) {
			//							Fish newFish = new Fish(fishShoal);
			//							fishShoal.add(newFish);
			//							Thread newThread = new Thread(newFish);
			//							newThread.start();
			//						}
			Fish newFish = new Fish(fishShoal);
			fishShoal.add(newFish);
			Thread newThread = new Thread(newFish);
			newThread.start();
		}
		drawPanel.repaint();
	}
	
	/**
	 * Private inner class for the draw panel.
	 */
	private final class DrawPanel extends JPanel {
		private static final long serialVersionUID = -4409792249877108008L;
		
		private DrawPanel() {
			setBackground(Color.WHITE);
		}
		
		@Override
		public void paintComponent(Graphics graphics) {
			super.paintComponent(graphics);
			Graphics2D g2d = (Graphics2D) graphics.create();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			Fish.setWorldWidth(drawPanel.getWidth());
			Fish.setWorldHeight(drawPanel.getHeight());
			
			fishShoal.draw(g2d);
			g2d.dispose();
		}
	}
}