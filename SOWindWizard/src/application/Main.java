package application;

import application.gui.InfoDlg;
import application.gui.InputControl;
import application.gui.WeatherResult;
import application.gui.XResult;
import application.gui.YResult;
import application.model.WeatherModel;
import application.model.WeatherModel.Weather;
import application.model.WindVectorCalculator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application
{
	private InputControl wind;
	private InputControl range;
	private XResult xVal;
	private YResult yVal;
	private WeatherResult weather;
	private IntegerProperty selectedDir;
	private WeatherModel weatherModel;
	private final double size = 200.0;

	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("SO MyWind");
		selectedDir = new SimpleIntegerProperty();
		selectedDir.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			calculateAndShow();
		});

		weatherModel = new WeatherModel();
		weatherModel
				.addListener((ObservableValue<? extends Weather> observable, Weather oldValue, Weather newValue) -> {
					calculateAndShow();
				});
		weather = new WeatherResult(weatherModel);
		BorderPane root = new BorderPane();
		root.setOnKeyReleased(event -> {
			if (event.getCode().equals(KeyCode.F1))
			{
				new InfoDlg(primaryStage);
			}
			else if (event.getCode().equals(KeyCode.F4))
			{
				weatherModel.setSelected(Weather.Rain);
			}
			else if (event.getCode().equals(KeyCode.F2))
			{
				weatherModel.setSelected(Weather.Sun);
			}
			else if (event.getCode().equals(KeyCode.F3))
			{
				weatherModel.setSelected(Weather.Clouds);
			}

		});

		
		VBox pane = new VBox();
		HBox hb = new HBox();
		Button info = new Button("?");
		info.setOnMouseClicked(event -> {
			new InfoDlg(primaryStage);
		});
		Button close = new Button("X");
		close.setOnMouseClicked(event -> {
			Platform.exit();
		});
		hb.getChildren().add(info);
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().add(close);
		hb.setMaxSize(30, 30);
		pane.setAlignment(Pos.CENTER);
		Node node2 = createIOGui();
		Node node1 = createWindRose(size);
		pane.getChildren().add(node1);
		pane.getChildren().add(hb);
		pane.getChildren().add(weather);
		pane.getChildren().add(node2);
		root.setTop(pane);
		Scene scene = new Scene(root, size, size);
		scene.setFill(null);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		addMouseListeners(primaryStage, root);

		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	public static void main(String[] args)
	{
		Main.launch((String[]) args);
	}


	private Node createIOGui()
	{
		xVal = new XResult();
		yVal = new YResult();
		wind = new InputControl("1.0");
		wind.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			calculateAndShow();
		});
		range = new InputControl("100.0");
		range.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			calculateAndShow();
		});
		VBox vb = new VBox();
		vb.setMaxSize(100.0, 100.0);
		vb.getChildren().add(createIO(wind, "Wind"));
		vb.getChildren().add(createIO(range, "Range"));
		StackPane p = new StackPane();
		Line ln = new Line(100.0, 0.0, 200.0, 0.0);
		StackPane.setMargin(ln, new Insets(8.0, 8.0, 8.0, 8.0));
		p.getChildren().add(ln);
		vb.getChildren().add(p);
		vb.getChildren().add(createIO(yVal));
		vb.getChildren().add(createIO(xVal));
		return vb;
	}


	private Node createIO(Node ioNode, String... text)
	{
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		if (text.length == 1)
		{
			Label lb = new Label(text[0]);
			lb.setMinWidth(50.0);
			hb.getChildren().add(lb);
			hb.setAlignment(Pos.CENTER);
		}
		hb.getChildren().add(ioNode);
		return hb;
	}


	private Node createWindRose(double size)
	{
		double radiant = Math.toRadians(22.5);
		double radius = size / 2 - 10;
		ToggleGroup group = new ToggleGroup();
		Pane pane = new Pane();
		int idx = 0;
		RadioButton node = null;
		while (idx < 16)
		{
			node = new RadioButton();
			double x1 = radius * Math.sin(radiant * (double) idx) + radius - 3 ;
			double y1 = radius * Math.cos(radiant * (double) idx) + radius - 3;
			node.setId("" + idx);
			node.setTranslateX(x1);
			node.setTranslateY(y1);
			node.setToggleGroup(group);
			final String id = node.getId();
			node.selectedProperty().addListener(listener -> {
				selectedDir.set(Integer.parseInt(id));
			});
			pane.getChildren().add(node);
			++idx;
		}
		node.setSelected(true);
		return pane;
	}


	private void calculateAndShow()
	{
		try
		{
			double[] windsVec = WindVectorCalculator.calcWindVectors(selectedDir.get(), wind.getValue());
			showX(windsVec[0]);
			showY(windsVec[1]);

			double rangeVal;
			rangeVal = getTrueRange();
			weather.setText(rangeVal);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	private void showX(Double xVector) throws NumberFormatException
	{
		xVal.setText((double) selectedDir.get(), xVector, getTrueRange());
	}


	private void showY(Double yVector) throws NumberFormatException
	{
		yVal.setText(yVector, yVector, getTrueRange());
	}


	private double getTrueRange() throws NumberFormatException
	{
		return range.getValue() * weatherModel.getSelected().windFactor + yVal.getDrift();
	}


	private void addMouseListeners(Stage stage, Node... nodes)
	{
		Node[] arrnode = nodes;
		int n = arrnode.length;
		int n2 = 0;
		while (n2 < n)
		{
			Node nodeToAdd = arrnode[n2];
			Delta dragDelta = new Delta();
			nodeToAdd.setOnMouseDragged(event -> {
				stage.setX(event.getScreenX() + dragDelta.x);
				stage.setY(event.getScreenY() + dragDelta.y);
			});
			nodeToAdd.setOnMousePressed(event -> {
				dragDelta.x = stage.getX() - event.getScreenX();
				dragDelta.y = stage.getY() - event.getScreenY();
			});
			++n2;
		}
	}

	private final static class Delta
	{
		double x;
		double y;
	}
}
