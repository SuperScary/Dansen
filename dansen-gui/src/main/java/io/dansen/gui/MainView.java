package io.dansen.gui;

import io.dansen.core.param.Schedulers;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;

/** Minimal UI: Load file, Play/Stop, Gain/Pan sliders with ramps. */
public class MainView extends BorderPane {
    private final EngineThread engine = new EngineThread();


    public MainView() {
        setPadding(new Insets(12));


        var top = new HBox(8);
        var btnLoad = new Button("Load File…");
        var btnPlay = new Button("Play");
        var btnStop = new Button("Stop");
        top.getChildren().addAll(btnLoad, btnPlay, btnStop);


        var grid = new GridPane();
        grid.setHgap(8); grid.setVgap(8);
        var gain = new Slider(0, 2.0, 0.8); gain.setShowTickMarks(true); gain.setMajorTickUnit(0.5);
        var pan = new Slider(-1.0, 1.0, 0.0); pan.setShowTickMarks(true); pan.setMajorTickUnit(0.5);
        grid.addRow(0, new Label("Gain"), gain);
        grid.addRow(1, new Label("Pan"), pan);


        setTop(top);
        setCenter(grid);


        btnLoad.setOnAction(e -> {
            var fc = new FileChooser();
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Audio", "*.wav", "*.aiff", "*.aif", "*.flac"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File f = fc.showOpenDialog(getScene().getWindow());
            if (f != null) engine.setSource(f);
        });
        btnPlay.setOnAction(e -> engine.play());
        btnStop.setOnAction(e -> engine.stop());


        gain.valueProperty().addListener((obs, o, v) ->
                engine.gainParam().rampTo(v.floatValue(), Schedulers.secondsToSamples(0.05, 44_100)));
        pan.valueProperty().addListener((obs, o, v) ->
                engine.panParam().rampTo(v.floatValue(), Schedulers.secondsToSamples(0.05, 44_100)));
    }
}
