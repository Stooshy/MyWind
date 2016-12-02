package application.gui;

import application.model.UserData;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SettingsDlg
{

	public SettingsDlg(Stage primaryStage, UserData data)
	{
		Stage dialog = new Stage(StageStyle.TRANSPARENT);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(primaryStage);
		HBox hb = new HBox();
		Label lb = new Label();
		String text = "Windfactor:";
		DoubleInputControl dic = new DoubleInputControl();
		dic.setValue(data.getUserFactor());
		lb.setText(text);
		lb.setId("textnormal");
		hb.setId("modal-dialog");
		Button bt = new Button("X");
		bt.setOnKeyReleased(event -> {
			if (event.getCode().equals(KeyCode.ESCAPE) || (event.getCode().equals(KeyCode.F1)))
			{
				data.setUserFactor(dic.getValue());
				primaryStage.getScene().getRoot().setEffect(null);
				dialog.close();
			}
		});
		bt.setOnMouseClicked(event -> {
			data.setUserFactor(dic.getValue());
			primaryStage.getScene().getRoot().setEffect(null);
			dialog.close();
		});
		hb.getChildren().add(lb);
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().add(dic);
		hb.setAlignment(Pos.CENTER);
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
