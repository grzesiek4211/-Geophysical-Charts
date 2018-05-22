package sample;

import com.emxsys.chart.extension.LogarithmicAxis;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Reflection;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javafx.scene.chart.ValueAxis;
import javafx.util.Duration;
import javafx.util.StringConverter;
import sun.tools.jar.CommandLine;

import static java.lang.Math.pow;


public class Main extends Application {

    public List<Float> X;
    public List<Float> Y;
    public List<Float> Parameters;

    public static int s = 38;

    public ArrayList<XYChart.Series> array;

    public LineChart<Double,Double> chart;
    public Scene scene;



    @Override
    public void start(Stage stage) {


        long start = System.currentTimeMillis();
        String fileName = "BAZA2.bin";
        //String fileName = "BAZA22.bin";
        this.X = new ArrayList<>();
        this.Parameters = new ArrayList<>();
        this.Y= new ArrayList<>();

        try {
            FileInputStream fileIs = new FileInputStream(fileName);
            DataInputStream is = new DataInputStream(fileIs);

            for (int i = 0; i < 120; i++)
                X.add(Float.intBitsToFloat(Integer.reverseBytes(is.readInt())));

            boolean endOfFile = false;
            while(!endOfFile)
            {
                try
                {
                    for (int i = 0; i < 3; i++)
                        Parameters.add(Float.intBitsToFloat(Integer.reverseBytes(is.readInt())));

                    for (int i = 0; i < 120; i++)
                        Y.add(Float.intBitsToFloat(Integer.reverseBytes(is.readInt())));
                }
                catch(EOFException ex)
                {
                    endOfFile = true;
                }
            }

            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long stop=System.currentTimeMillis();
        System.out.println("Czas czytania pliku binarnego [s]: "+(stop-start)/1000.0);

        float minx = 0.1f;
        float miny = 0.1f;
        float maxx = 100.0f;
        float maxy = 10000.0f;
        LogarithmicAxis xAxis = new LogarithmicAxis("L/d", minx, maxx);
        LogarithmicAxis yAxis = new LogarithmicAxis("Ra/Rm", miny, maxy);

        chart = new LineChart(xAxis,yAxis);
        chart.setAnimated(false);

        array = new ArrayList<>();

        for (int i =0; i<38;i++)
        {
            array.add(new XYChart.Series());
            array.get(i).setName(Parameters.get((3*i)+2).toString());
        }


        for(int i =0; i<38;i++)
        {
            for(int j = 0; j < 120;j++)
            {
                int temp = i*120+j;
                array.get(i).getData().add(new XYChart.Data(pow(10,X.get(j)),pow(10,Y.get(temp))));
            }
        }

        StackPane spLineChart = new StackPane();
        spLineChart.getChildren().add(chart);

        Button button = new Button("Up");
        Button button2 = new Button("Down");
        Button button3 = new Button("10xDown");
        Button button4 = new Button("10xUp");
        Button button5 = new Button("100xUp");
        button.setOnMouseClicked((event)->{

            s++;
            update(button,button2,button3,button4,button5);
        });
        button.setTranslateX(50);

        button2.setOnMouseClicked((event)->{

            s--;
            update(button,button2,button3,button4,button5);
        });

        button3.setTranslateX(104);
        button3.setOnMouseClicked((event)->{

            if(s>=10)
            {
                s -= 10;
                //button3.setDisable(false);
            }
            else
            {
                s = 0;
                //button3.setDisable(true);
            }
            update(button,button2,button3,button4,button5);
        });

        button4.setTranslateX(173);
        button4.setOnMouseClicked((event)->{

            if(s<=597)
            {
                s += 10;
                //button4.setDisable(false);
            }
            else
            {
                s = 607;
                //button4.setDisable(true);
            }
            update(button,button2,button3,button4,button5);
        });
        button5.setTranslateX(250);
        button5.setOnMouseClicked((event)->{

            if(s<=507)
            {
                s += 100;
                //button4.setDisable(false);
            }
            else
            {
                s = 607;
                //button4.setDisable(true);
            }
            update(button,button2,button3,button4,button5);
        });

        StackPane spButton = new StackPane();

        spButton.setAlignment(Pos.TOP_LEFT);
        spButton.getChildren().addAll(button,button2,button3,button4,button5);

        VBox vbox = new VBox();
        VBox.setVgrow(spLineChart, Priority.ALWAYS);//Make line chart always grow vertically
        vbox.getChildren().addAll( spButton,spLineChart);

        scene  = new Scene(vbox,800,600);
        chart.getData().addAll(array.get(0),array.get(1),array.get(2),array.get(3),array.get(4),array.get(5),array.get(6),array.get(7),array.get(8),array.get(9),array.get(10),array.get(11),array.get(12),array.get(13),array.get(14),array.get(15),array.get(16),array.get(17),array.get(18),array.get(19),array.get(20),array.get(21),array.get(22),array.get(23),array.get(24),array.get(25),array.get(26),array.get(27),array.get(28),array.get(29),array.get(30),array.get(31),array.get(32),array.get(33),array.get(34),array.get(35),array.get(36),array.get(37));
        chart.setTitle("D/d: "+Parameters.get(3*38*s)+" Ri/Rm: "+Parameters.get(3*38*s+1));
        chart.setCreateSymbols(false);
        chart.setLegendSide(Side.RIGHT);



        chart.applyCss();
        chart.layout();


        setCtrlC();

        stage.setScene(scene);
        stage.show();


    }



    public void screenshot()
    {
        WritableImage image = chart.snapshot(new SnapshotParameters(),null);

        chart.snapshot(new SnapshotParameters(),image);
        ClipboardContent cc = new ClipboardContent();
        cc.putImage(image);
        Clipboard.getSystemClipboard().setContent(cc);
    }

    public void update(Button b1,Button b2,Button b3,Button b4, Button b5) {
        System.out.println(s);

        if (s >= 607) {
            b1.setDisable(true); // "up"
            //b4.setDisable(true); // "10xup"
            b2.setDisable(false); // "down"
            //b3.setDisable(false); // "10xdown"
        }
        if (s >= 597)
        {
            //b1.setDisable(true); // "up"
            b4.setDisable(true); // "10xup"
            //b2.setDisable(false); // "down"
            b3.setDisable(false); // "10xdown"
        }
        if (s >= 507)
        {
            b5.setDisable(true); // "100xup"
        }
        if (s <= 0) {
            b2.setDisable(true);
            b3.setDisable(true);
            b1.setDisable(false);
            b4.setDisable(false);
            b5.setDisable(false);
        }
        if (s <= 10) {
            b2.setDisable(true);
            b3.setDisable(true);
        }
        if (s >= 100 && s <= 507) {
            b5.setDisable(false);
            b4.setDisable(false);
            b3.setDisable(false);
            b2.setDisable(false);
            b1.setDisable(false);
        }
        else if (s >= 10 && s <= 597) {
            b4.setDisable(false);
            b3.setDisable(false);
            b2.setDisable(false);
            b1.setDisable(false);
        }
        else if (s > 0 && s < 607) {
            b2.setDisable(false);
            //b3.setDisable(false);
            b1.setDisable(false);
            //b4.setDisable(false);
        }


        for (int i = 0; i < 38; i++) {
            array.get(i).getData().clear();
            for (int j = 0; j < 120; j++) {

                int temp = s * (120 * 38) + (i * 120) + j;
                array.get(i).getData().add(new XYChart.Data(pow(10, X.get(j)), pow(10, Y.get(temp))));

            }
        }
        chart.setTitle("D/d: "+Parameters.get(3*38*s)+" Ri/Rm: "+Parameters.get(3*38*s+1));
    }


    final KeyCombination keyCombinationShiftC = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);

    public void setCtrlC() {

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (keyCombinationShiftC.match(event)) {
                    screenshot();
                }
            }
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}