package com.talentica.sdn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Component;

/**
 * @author NarenderK
 *
 */
@Component
public class GraphUtil {

    private static final boolean SHOW_LEGEND = false;
    private static final boolean SHOW_TOOLTIPS = false;
    private static final boolean GENERATE_URLS = false;

    public JFreeChart draw(InputStream is) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    	
        // Get the x-axis label from the first token in the first line
        // and the y-axis label from the last token in the first line.
        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line, ",");
        String xLabel = st.nextToken();
        String yLabel = st.nextToken();
        while (st.hasMoreTokens()) {
            yLabel = st.nextToken();
        }

        String title = yLabel + " by " + xLabel;

        // Get the data to plot from the remaining lines.
        float minY = Float.MAX_VALUE;
        float maxY = -Float.MAX_VALUE;
        XYSeries series = new XYSeries("?");
        while (true) {
            line = br.readLine();
            if (line == null) {
                break;
            }
            st = new StringTokenizer(line, ",");

            // The first token is the x value.
            String xValue = st.nextToken();

            // The last token is the y value.
            String yValue = "";
            while (st.hasMoreTokens()) {
                yValue = st.nextToken();
            }

            float x = Float.parseFloat(xValue);
            float y = Float.parseFloat(yValue);
            series.add(x, y);

            minY = Math.min(y, minY);
            maxY = Math.max(y, maxY);
        }        

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);       
        
        JFreeChart chart = ChartFactory.createXYLineChart(
            title, xLabel, yLabel, dataset,
            PlotOrientation.VERTICAL,
            SHOW_LEGEND, SHOW_TOOLTIPS, GENERATE_URLS);
        
        XYPlot plot = chart.getXYPlot();
        plot.getRangeAxis().setRange(minY, maxY);
        
        return chart;
    }
}

