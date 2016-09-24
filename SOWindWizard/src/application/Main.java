package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application
{
	private TextField wind;
	private TextField range;
	private Label xVal;
	private Label yVal;
	private Label weather;
	private IntegerProperty selectedDir;
	private String outFormat = "%10.1f y | %.1f %%";
	private double weatherFactor = 1.0;


	public void start(Stage primaryStage)
	{
		try
		{
			primaryStage.setTitle("SO WindVector");
			selectedDir = new SimpleIntegerProperty();
			selectedDir.addListener(listener -> {
				calculateAndShow();
			});
			BorderPane root = new BorderPane();

			root.setOnKeyReleased(event -> {
				if (event.getCode().equals(KeyCode.F1))
				{
					new InfoDlg(primaryStage);
				}
				else if (event.getCode().equals(KeyCode.R))
				{
					weatherFactor = .87;
					try
					{
						double rangeVal = getRangeDouble() * weatherFactor;
						weather.setText(String.format("Rain %.1f", rangeVal));
						calculateAndShow();
					}
					catch (Exception e)
					{
					}
				}
				else if (event.getCode().equals(KeyCode.S))
				{
					weatherFactor = 1.0;
					try
					{
						double rangeVal = getRangeDouble() * weatherFactor;
						weather.setText(String.format("Sun %.1f", rangeVal));
						calculateAndShow();
					}
					catch (Exception e)
					{
					}
				}
				else if (event.getCode().equals(KeyCode.C))
				{
					weatherFactor = .93;
					try
					{
						double rangeVal = getRangeDouble() * weatherFactor;
						weather.setText(String.format("Clouds %.1f", rangeVal));
						calculateAndShow();
					}
					catch (Exception e)
					{
					}
				}

			});
			StackPane pane = new StackPane();
			Button info = new Button("?");
			info.setTranslateY(-65.0);
			info.setTranslateX(-15.0);
			info.setOnMouseClicked(event -> {
				new InfoDlg(primaryStage);
			});
			Button close = new Button("X");
			close.setTranslateY(-65.0);
			close.setTranslateX(15.0);
			close.setOnMouseClicked(event -> {
				Platform.exit();
			});
			weather = new Label("Sun");
			weather.setTranslateY(65.0);

			pane.getChildren().add(createWindRose());
			pane.getChildren().add(createIOGui());
			pane.getChildren().add(info);
			pane.getChildren().add(close);
			pane.getChildren().add(weather);

			root.setCenter((Node) pane);
			Scene scene = new Scene(root, 200.0, 200.0);
			scene.setFill(null);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			addMouseListeners(primaryStage, root);
			range.requestFocus();
			wind.requestFocus();
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void main(String[] args)
	{
		Main.launch((String[]) args);
	}


	private Node createIOGui()
	{
		xVal = new Label();
		xVal.setId("textnormal");
		yVal = new Label();
		yVal.setId("textnormal");
		wind = new TextField("1.0");
		wind.setId("textnormal");
		wind.setMaxWidth(50.0);
		wind.focusedProperty().addListener(listener -> {
			try
			{
				wind.setText("" + getWindDouble());
				calculateAndShow();
				wind.setId("textnormal");
				wind.selectPositionCaret(range.getText().length());
			}
			catch (Exception ex)
			{
				wind.setId("textred");
			}
		});
		range = new TextField("100.0");
		range.setId("textnormal");
		range.setMaxWidth(50.0);
		range.focusedProperty().addListener(listener -> {
			try
			{
				range.setText("" + getRangeDouble());
				calculateAndShow();
				range.setId("textnormal");
				range.selectPositionCaret(range.getText().length());
			}
			catch (Exception ex)
			{
				range.setId("textred");
			}
		});
		VBox vb = new VBox();
		vb.setMaxSize(100.0, 100.0);
		vb.getChildren().add(createIO("Wind", wind));
		vb.getChildren().add(createIO("Range", range));
		StackPane p = new StackPane();
		Line ln = new Line(100.0, 0.0, 200.0, 0.0);
		StackPane.setMargin(ln, new Insets(8.0, 8.0, 8.0, 8.0));
		p.getChildren().add(ln);
		vb.getChildren().add(p);
		vb.getChildren().add(createIO("", yVal));
		vb.getChildren().add(createIO("", xVal));
		return vb;
	}


	private Node createIO(String text, Node ioNode)
	{
		HBox hb = new HBox();
		if (!text.isEmpty())
		{
			Label lb = new Label(text);
			lb.getStyleClass().add("icons");
			lb.setMinWidth(50.0);
			hb.getChildren().add(lb);
		}
		hb.getChildren().add(ioNode);
		return hb;
	}


	private Node createWindRose()
	{
		double radiant = Math.toRadians(22.5);
		double radius = 90.0;
		ToggleGroup group = new ToggleGroup();
		Pane pane = new Pane();
		int idx = 0;
		while (idx < 16)
		{
			double x1 = radius * Math.sin(radiant * (double) idx) + 88.0;
			double y1 = radius * Math.cos(radiant * (double) idx) + 85.0;
			RadioButton node = new RadioButton();
			node.setId("" + idx);
			node.setTranslateX(x1);
			node.setTranslateY(y1);
			node.setToggleGroup(group);
			node.selectedProperty().addListener(listener -> {
				selectedDir.set(Integer.parseInt(node.getId()));
			});
			pane.getChildren().add(node);
			++idx;
		}
		return pane;
	}


	private void calculateAndShow()
	{
		double[] windsVec = WindVectorCalculator.calcWindVectors(selectedDir.get(), getWindDouble());
		showX(windsVec[0]);
		try
		{
			showY(windsVec[1]);
		}
		catch (Exception ex)
		{
			// empty catch block
		}
	}


	private void showX(Double value)
	{
		int direction = selectedDir.get();
		String directionSign = direction > 1 && direction < 10 ? "> " : "< ";
		xVal.setText(String.format(directionSign + outFormat, 0.0, value));
	}


	private void showY(Double value) throws Exception
	{
		String directionSign = value > 0.0 ? "^" : "v";
		double rangeVal = getRangeDouble() * weatherFactor;
		yVal.setText(String.format(directionSign + outFormat, value / 100.0 * rangeVal, value));
	}


	private double getWindDouble() throws NumberFormatException
	{
		return Double.parseDouble(getWindString());
	}


	private String getWindString()
	{
		return wind.getText().trim().replace(",", ".");
	}


	private double getRangeDouble() throws Exception
	{
		return Double.parseDouble(getRangeString());
	}


	private String getRangeString()
	{
		return range.getText().trim().replace(",", ".");
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

	private class Delta
	{
		double x;
		double y;
	}

	private class InfoDlg
	{
		public InfoDlg(Stage primaryStage)
		{
			Stage dialog = new Stage(StageStyle.TRANSPARENT);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(primaryStage);
			HBox hb = new HBox();
			Label lb = new Label();
			String text = "Driver 1,6\nW3/5  1,3\nI3/4   1\ni5      .8\ni6      .7\ni7-9   .6\nPW    .55\nAW    .5\nSW    1\n\nBy Shotlong \u00a9 2016 V1.1";
			lb.setText(text);
			lb.setId("textnormal");
			hb.setId("modal-dialog");
			Button bt = new Button();
			bt.setOnKeyReleased(event -> {
				if (event.getCode().equals(KeyCode.ESCAPE))
				{
					primaryStage.getScene().getRoot().setEffect(null);
					dialog.close();
				}

			});
			// bt.setVisible(false);
			hb.getChildren().add(bt);
			hb.getChildren().add(lb);
			lb.requestFocus();
			dialog.setScene(new Scene(hb, Color.TRANSPARENT));
			dialog.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.getScene().getRoot().setEffect((Effect) new BoxBlur());
			dialog.setX(primaryStage.getX());
			dialog.setY(primaryStage.getY());
			dialog.setWidth(200);
			dialog.setHeight(200);
			dialog.show();
		}
	}
}
