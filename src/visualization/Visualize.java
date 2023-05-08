package visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pareto.ParetoFront;
import pareto.Solution;
import resource.Map;
import resource.UAV;

public class Visualize {
	
	JFrame frame;
	
	JPanel mainPanel;
	JPanel controlPanel;
	DrawPanel drawPanel;
	
	Color mstColor;
	
	JButton drawButton;
	
	ArrayList<Point> points;
	ArrayList<Line> lines;
	
	public Visualize(ParetoFront PF) {
		points = new ArrayList<Point>();
		lines = new ArrayList<Line>();
		
		mstColor = Color.GREEN;
		
		buildGUI(PF);
	}
	
	public void buildGUI(ParetoFront PF) {
		// setup frame
		frame = new JFrame("Bi-Criterion Ant Colony Optimization");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// setup main panel, root container
		mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		frame.getContentPane().add(mainPanel);
		
		// setup drawPanel
		drawPanel = new DrawPanel(points, lines);
		drawPanel.setPreferredSize(new Dimension(1000, 1000));
		mainPanel.add(drawPanel);
		
		// setup controlPanel
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(controlPanel);
		
		// setup drawButton
		drawButton = new JButton("Display");
		drawButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				getResult(PF);
			}
		});
		controlPanel.add(drawButton);
		
		// finish and display
		frame.pack();
		frame.setVisible(true);
	}
	
	public void getResult(ParetoFront PF) {
		points.clear();
		lines.clear();
		
		for (int i = 0; i < Map.totNum; i++) {
			Point point = new Point(Map.nodes[i].getX(), Map.nodes[i].getY());
			if (i < Map.staNum)
				point.setC(Color.RED);
			else
				point.setC(Color.BLACK);
			points.add(point);
		}
		
		
		for (Solution sol : PF.pf) {
			for (UAV uav : sol.uavList) {
				int size = uav.nodeSeq2.size();
				for (int i = 1; i < size; i++) {
					int x = uav.nodeSeq2.get(i - 1);
					int y = uav.nodeSeq2.get(i);
					lines.add(new Line(points.get(x), points.get(y), mstColor));
				}
			}
			
//			break;
		}
		
		drawPanel.repaint();
	}
	
//	public static void visualize(ParetoFront PF) {
//		
//	}
}
