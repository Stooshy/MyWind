package application;

import application.gui.DoubleInputControl;
import application.gui.InfoDlg;
import application.gui.Result;
import application.gui.XResult;
import application.gui.YResult;
import application.model.RangeModel;
import application.model.WeatherModel;
import application.model.WeatherModel.Weather;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
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
	private final double width = 400.0;


	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("SO MyWind");

		WeatherModel weatherModel = new WeatherModel();
		RangeModel rangeModel = new RangeModel();
		Result result = new Result();
		weatherModel.addListener(result);
		weatherModel.addListener(rangeModel);
		rangeModel.addListener(result);
		weatherModel.setSelected(Weather.Sun);

		BorderPane root = createRoot(primaryStage, weatherModel);
		addMouseListeners(primaryStage, root);
		VBox vb = new VBox();
		vb.setAlignment(Pos.CENTER);
		vb.getChildren().add(createWindRose(width / 2 - 10, rangeModel));
		vb.getChildren().add(createButtons(primaryStage));
		vb.getChildren().add(result);
		vb.getChildren().add(createInputControls(rangeModel));
		root.setTop(vb);

		Scene scene = new Scene(root, width, width);
		scene.setFill(null);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	public static void main(String[] args)
	{
		Main.launch((String[]) args);
	}


	public BorderPane createRoot(Stage primaryStage, WeatherModel model)
	{
		BorderPane root = new BorderPane();
		root.setOnKeyReleased(event -> {
			switch (event.getCode())
			{
			case F1:
				new InfoDlg(primaryStage);
				break;
			case F2:
			case F3:
			case F4:
				model.setSelected(Weather.getWeather(event.getCode()));
				break;
			default:
				break;
			}
		});
		return root;
	}


	private Node createButtons(Stage primaryStage)
	{
		Button info = new Button("?");
		info.setOnMouseClicked(event -> {
			new InfoDlg(primaryStage);
		});
		Button close = new Button("X");
		close.setOnMouseClicked(event -> {
			Platform.exit();
		});
		HBox hb = new HBox();
		hb.getChildren().add(info);
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().add(close);
		hb.setMaxSize(30, 30);

		return hb;
	}


	private Node createInputControls(RangeModel rModel)
	{
		XResult xVal = new XResult();
		YResult yVal = new YResult();
		DoubleInputControl range = new DoubleInputControl();
		range.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			rModel.setRange(newValue);
		});
		rModel.addListener((InvalidationListener) xVal);
		rModel.addListener((InvalidationListener) yVal);
		DoubleInputControl wind = new DoubleInputControl();
		wind.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			rModel.setWind(newValue);
		});
		range.setValue(100);
		wind.setValue(1);

		VBox vb = new VBox();
		vb.setMaxSize(width / 2, width / 2);
		vb.getChildren().add(createIO(wind, "Wind"));
		vb.getChildren().add(createIO(range, "Range"));
		StackPane p = new StackPane();
		Line ln = new Line(width / 2, 0.0, width, 0.0);
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


	private Node createWindRose(double radius, RangeModel rModel)
	{
		int marks = 16;
		final IntegerProperty selectedDir = new SimpleIntegerProperty();
		selectedDir.addListener(rModel);
		double radiant = Math.toRadians(360 / marks);
		ToggleGroup group = new ToggleGroup();
		Pane pane = new Pane();
		int idx = 0;
		RadioButton node = null;
		while (idx < marks)
		{
			node = new RadioButton();
			double x1 = radius * Math.sin(radiant * (double) idx) + radius + 1;
			double y1 = radius * Math.cos(radiant * (double) idx) + radius + 1;
			node.setTranslateX(x1);
			node.setTranslateY(y1);
			node.setId("" + idx);
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
