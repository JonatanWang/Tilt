package com.wang.tilt_z;

import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by zyw on 2017-12-09.
 * Graph class to set series data & colors
 */

public class Graph {

    private GraphView graphView;
    private long beginTime;
    private final int SAMPLE_NUMBER = (int) Constants.GRAPH_SAMPLE_NUMBER.getValue();

    private LineGraphSeries<DataPoint>[] series = new LineGraphSeries[3];

    public Graph () {

    }

    public Graph(GraphView graphView){
        this.graphView = graphView;
        beginTime = System.currentTimeMillis();
        render();
    }

    public void setSeries(float[] data){
        // Time Unit
        long time = (System.currentTimeMillis() - beginTime);
        for(int i = 0; i < 3; i++){
            series[i].appendData(new DataPoint(time, data[i]), false, SAMPLE_NUMBER);
        }
    }

    public void render(){
        for(int i = 0; i < 3; i++){
            series[i] = new LineGraphSeries();
            graphView.setBackgroundColor(Color.CYAN);
            graphView.setTitle("Red:values[0],Yellow:values[1], Blue:values[2]");
            graphView.setTitleColor(Color.MAGENTA);
            graphView.setTitleTextSize(45);
            graphView.clearSecondScale();
            graphView.computeScroll();
            graphView.animate();
            graphView.addSeries(series[i]);
        }
        series[0].setColor(Color.RED);
        series[1].setColor(Color.YELLOW);
        series[2].setColor(Color.BLUE);
    }

    public GraphView getGraphView() {
        return graphView;
    }

    public void setGraphView(GraphView graphView) {
        this.graphView = graphView;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }
}
