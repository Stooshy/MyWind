package application.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class InfoDlg
{
	public InfoDlg(Stage primaryStage)
	{
		Stage dialog = new Stage(StageStyle.TRANSPARENT);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(primaryStage);
		HBox hb = new HBox();
		Label lb = new Label();
		String text = "Driver 1,6\nW3/5  1,3\nI3/4   1\ni5      .8\ni6      .7\ni7-9   .6\nPW    .55\nAW    .5\nSW    1\nWeather: F2 - F4\nBy Shotlong \u00a9 2016 V2.2";
		lb.setText(text);
		lb.setId("textnormal");
		hb.setId("modal-dialog");
		Button bt = new Button("X");
		bt.setOnKeyReleased(event -> {
			if (event.getCode().equals(KeyCode.ESCAPE) || (event.getCode().equals(KeyCode.F1)))
			{
				primaryStage.getScene().getRoot().setEffect(null);
				dialog.close();
			}
		});
		bt.setOnMouseClicked(event -> {
			primaryStage.getScene().getRoot().setEffect(null);
			dialog.close();
		});
		hb.getChildren().add(lb);
		hb.getChildren().add(bt);
		lb.requestFocus();
		dialog.setScene(new Scene(hb, Color.TRANSPARENT));
		dialog.getScene().getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		dialog.setX(primaryStage.getX());
		dialog.setY(primaryStage.getY());
		dialog.setWidth(200);
		dialog.setHeight(200);
		dialog.show();
	}
}