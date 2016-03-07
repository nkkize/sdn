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

public class ABC extends ApplicationFrame {
	
	private static final String TITLE = "CSV plot";
	
	private XYSeries series;

	public ABC(String title) {
		super(title);
		this.series = createInitialSeries();
		XYSeriesCollection dataset = new XYSeriesCollection(this.series);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
	}
	
	private XYSeries createInitialSeries() {
		XYSeries newSeries = new XYSeries("Chart");
		try {
			DataInputStream dis = null;
			String record = null;
			int recCount = 0;

			File file = new File("D://sampleCSV.csv"); // The CSV file.
			file = file.getAbsoluteFile();
			String path = String.valueOf(file);
			System.out.println(path);

			File f = new File(path);
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
				newSeries.update(x_val, y_val);
			}
		} catch (Exception e) {
		}
		return newSeries;
	}
	
	private JFreeChart createChart(XYDataset dataset) {

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

	public static void main(final String[] args) {
        final ABC demo = new ABC(TITLE);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

        //final Thread updater = demo.new UpdaterThread();
        //updater.setDaemon(true);
       // updater.start();
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

            while (true) {
                final int x = (int) (Math.random() * 10);
                final int y = (int) (Math.random() * 10);

                series.addOrUpdate(x, y);

                try {
                    sleep(50);
                }
                catch (InterruptedException e) {
                    // suppress
                }
            }
        }
    }

}
