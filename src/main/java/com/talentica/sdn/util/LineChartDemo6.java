package com.talentica.sdn.util;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class LineChartDemo6 extends ApplicationFrame {

	private XYSeries series = new XYSeries("Chart");

	private int recCount;

	File file = new File("D://sampleCSV.csv"); // The CSV file.

	public LineChartDemo6(final String title) {
		super(title);
		this.recCount = 0;
		final XYDataset dataset = createDataset();
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
	}

	private int getNumberOfRecords() {
		int rec = 0;
		try {
			DataInputStream dis = null;
			String record = null;
			File f = new File(String.valueOf(file.getAbsoluteFile()));
			FileInputStream fis = new FileInputStream(f);
			BufferedInputStream bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			record = dis.readLine();
			while ((record = dis.readLine()) != null) {
				rec++;
			}
		} catch (Exception e) {
		}
		return rec;
	}

	private XYDataset createDataset() {
		try {
			DataInputStream dis = null;
			String record = null;
			File f = new File(String.valueOf(file.getAbsoluteFile()));
			FileInputStream fis = new FileInputStream(f);
			BufferedInputStream bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			record = dis.readLine();
			while ((record = dis.readLine()) != null) {
				recCount++;
				String values = record;
				String comma = ",";
				int location = values.indexOf(comma);
				String xvalue = values.substring(0, location);
				String yvalue = values.substring(location + 1);
				long x_val = Integer.valueOf(xvalue);
				long y_val = Integer.valueOf(yvalue);
				series.add(x_val, y_val);
			}
		} catch (Exception e) {
		}
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		return dataset;

	}

	/*
	 * Creates a chart.
	 * 
	 * @param dataset the data for the chart.
	 * 
	 * @return a chart.
	 */

	private JFreeChart createChart(final XYDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart("Line Chart Demo 6 ", "X", "Y", dataset,
				PlotOrientation.VERTICAL,
				// Using PlotOrientation.HORIZONTAL would display 'x' values
				// 'vertically' & 'y' values horizontally.
				true, true, false);

		/*
		 * createXYLineChart(String title, String xAxisLabel, String
		 * yAxisLabel,XYDataset dataset, PlotOrientation, boolean
		 * 
		 * legend,boolean tooltips, boolean urls)
		 */

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		// final StandardLegend legend = (StandardLegend) chart.getLegend();
		// legend.setDisplaySeriesShapes(true);

		// get a reference to the plot for further customisation...
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(1, true);
		renderer.setSeriesShapesVisible(1, false);
		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}

	// Starting point for the demonstration application.
	// @param args ignored.

	public static void main(final String args[]) {

		final LineChartDemo6 demo = new LineChartDemo6("Line Chart Demo 6");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

		final Thread updater = demo.new UpdaterThread();
		updater.setDaemon(true);
		updater.start();

	}

	/**
	 * A thread for updating the dataset.
	 */
	private class UpdaterThread extends Thread {
		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			setPriority(MIN_PRIORITY); // be nice
			int rec = getNumberOfRecords();
			while (true) {
				if(rec>recCount){
				//READ from file the new values
					//add those values in series
				//series.addOrUpdate(i, j);
					
				}
				try {
					sleep(50);
				} catch (InterruptedException e) {
					// suppress
				}
			}
		}
	}

}
