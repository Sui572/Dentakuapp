package Sample;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Sample3_1 extends Application {
    private Label lb;

    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage stage) throws Exception{
        // コントロールの作成
        lb = new Label();

        // コントロールの設定
        lb.setText("いらっしゃいませ");

        // ペインの作成
        BorderPane bp = new BorderPane();

        // ペインの追加
        bp.setCenter(lb);

        // シーンの作成
        Scene sc = new Scene(bp, 300, 200);

        // ステージの追加
        stage.setScene(sc);

        // ステージの表示
        stage.setTitle("サンプル");
        stage.show();
    }
}