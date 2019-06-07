package app.gaussian.controllers;

import app.gaussian.common.Environment;
import app.gaussian.exception_handlers.TextFieldHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.ejml.data.Complex_F64;
import org.ejml.ops.ComplexMath_F64;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLineChart();
        initAxis();
    }

    private void initLineChart(){
        lineChart.setPrefSize(Environment.get().getChartWight()-50, Environment.get().getChartHeight());
        lineChart.setTitle(Environment.get().getChartTitle());
    }

    private void initAxis(){
        yAxis.setForceZeroInRange(false);
        //yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(1.2);
        yAxis.setTickUnit(0.1);
        yAxis.setLabel(Environment.get().getChartYLabel());
        /*yAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object.doubleValue());
            }

            @Override
            public Number fromString(String string) {
                return 0;
            }
        });*/
        xAxis.setLabel(Environment.get().getChartXLabel());
        xAxis.setForceZeroInRange(false);
        xAxis.setLowerBound(400e-9);
        xAxis.setUpperBound(800e-9);
    }


    public void modeling(ActionEvent event){
        try {
            Layer layer1 = new Layer(93.6e-9, 1.45);
            Layer layer2 = new Layer(61.7e-9, 2.20);

            LinkedList<Layer> structure = new LinkedList<>();
            structure.add(layer1);
            structure.add(layer2);
            structure.add(layer1);
            structure.add(layer2);
            structure.add(layer1);
            structure.add(layer2);
            structure.add(layer1);
            structure.add(layer2);
            structure.add(layer1);
            structure.add(layer2);
            structure.add(layer1);
            structure.add(layer2);
            structure.add(layer1);
            structure.add(layer2);

            XYChart.Series<Number,Number> dataSeries = new XYChart.Series<>();
            for (double x = 400e-9; x<= 800e-9; x+= 1e-10){
                dataSeries.getData().add(new XYChart.Data<>(x, calcT(structure, x)));
            }

            boolean flag = false;
            if (lineChart.getData().size() != 0){
                for (int i=0; i<lineChart.getData().size(); i++){
                    if (lineChart.getData().get(i).getName().equals(dataSeries.getName())){
                        TextFieldHandler.repeatingDataHandler(dataSeries.getName());
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag){
                lineChart.getData().add(dataSeries);
            }
        } catch (NumberFormatException e){
            //
        }
    }

    public void clear(ActionEvent event){
        lineChart.getData().clear();
    }


    public double calcT(LinkedList<Layer> structure, double lambda){

        List<Matrix> matrixStruct = new ArrayList<>();

        structure.forEach((value)-> {
                    double k = 2 * Math.PI / lambda;
                    double b = k * value.rafract_index * value.thikness;

                    Complex_F64 a11 = new Complex_F64(Math.cos(b), 0);
                    Complex_F64 a12 = new Complex_F64(0, -(value.rafract_index*Math.sin(b)));
                    Complex_F64 a21 = new Complex_F64(0, -1/(value.rafract_index*Math.sin(b)));
                    Complex_F64 a22 = new Complex_F64(Math.cos(b), 0);

                    matrixStruct.add(new Matrix(a11, a12, a21, a22));
                });

        Matrix res = matrixStruct.get(0);

        for (int i = 1; i<matrixStruct.size(); i++) {
            res = multMatrix(res, matrixStruct.get(i));
        }

        Complex_F64 matrixResSum = res.getA11().plus(res.getA12().plus(res.getA21()).plus(res.getA22()));

        Complex_F64 rMatrixRes = res.getA11().plus(res.getA12().minus(res.getA21().minus(res.getA22())));

        Complex_F64 rRes = rMatrixRes.divide(matrixResSum);
        Complex_F64 tRes = new Complex_F64(2, 0).divide(matrixResSum);

        double r = Math.pow(rRes.getReal(), 2) + Math.pow(rRes.getImaginary(), 2);
        double t = Math.pow(tRes.getReal(), 2) + Math.pow(tRes.getImaginary(), 2);
        return t;

    }

    public Matrix multMatrix(Matrix a, Matrix b){

        Complex_F64 a11b11 = new Complex_F64();
        Complex_F64 a11b12 = new Complex_F64();
        Complex_F64 a12b21 = new Complex_F64();
        Complex_F64 a12b22 = new Complex_F64();
        Complex_F64 a21b11 = new Complex_F64();
        Complex_F64 a21b12 = new Complex_F64();
        Complex_F64 a22b21 = new Complex_F64();
        Complex_F64 a22b22 = new Complex_F64();

        ComplexMath_F64.multiply(a.getA11(), b.getA11(), a11b11);
        ComplexMath_F64.multiply(a.getA11(), b.getA12(), a11b12);
        ComplexMath_F64.multiply(a.getA12(), b.getA21(), a12b21);
        ComplexMath_F64.multiply(a.getA12(), b.getA22(), a12b22);
        ComplexMath_F64.multiply(a.getA21(), b.getA11(), a21b11);
        ComplexMath_F64.multiply(a.getA21(), b.getA12(), a21b12);
        ComplexMath_F64.multiply(a.getA22(), b.getA21(), a22b21);
        ComplexMath_F64.multiply(a.getA22(), b.getA22(), a22b22);

        Matrix matrix = new Matrix(a11b11.plus(a12b21), a11b12.plus(a12b22), a21b11.plus(a22b21), a21b12.plus(a22b22));

        return matrix;

    }

    class Layer {
        private double thikness;
        private double rafract_index;

        public Layer(double thikness, double rafract_index) {
            this.thikness = thikness;
            this.rafract_index = rafract_index;
        }

        public double getThikness() {
            return thikness;
        }

        public double getRafract_index() {
            return rafract_index;
        }
    }

    class Structure {
        private List<Layer> layers;
        private int layerscCount;

        public Structure(List<Layer> layers, int layerscCount) {
            this.layers = layers;
            this.layerscCount = layerscCount;
        }

        public List<Layer> getLayers() {
            return layers;
        }

        public int getLayerscCount() {
            return layerscCount;
        }
    }

    class Matrix {
        private Complex_F64 a11;
        private Complex_F64 a12;
        private Complex_F64 a21;
        private Complex_F64 a22;

        public Matrix(Complex_F64 a11, Complex_F64 a12, Complex_F64 a21, Complex_F64 a22) {
            this.a11 = a11;
            this.a12 = a12;
            this.a21 = a21;
            this.a22 = a22;
        }

        public Complex_F64 getA11() {
            return a11;
        }

        public Complex_F64 getA12() {
            return a12;
        }

        public Complex_F64 getA21() {
            return a21;
        }

        public Complex_F64 getA22() {
            return a22;
        }
    }
}
