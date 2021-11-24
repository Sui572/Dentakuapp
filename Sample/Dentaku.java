package Sample;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JTextField;

public class Dentaku extends JFrame {
	private static final long serialVersionUID = 1L;

	JPanel contentPane = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();

    //計算結果を表示するテキストフィールド
    String initialValue = "0";
	JTextField result = new JTextField(initialValue);

    //演算子ボタンを押す前にテキストフィールドに表示されていた値
    double stackedValue = 0.0;

    //エラーメッセージが出力されたかどうか
    boolean error = false;

    //stackedValueに数値を入力したかどうか
    boolean isStacked = false;

    //消費税ボタンを押した後かどうか
    boolean afterzeibutton = false;
    //どっちの消費税ボタンを押したか
    boolean addzeibutton = false;
    //どっちの消費税ボタンを押したか(2回目)
    int afterCalcaddzeibutton = 0;

    //演算子ボタンを押した後かどうか
    boolean afterCalc = false;
    boolean afterCalczei = false;

    //小数点ボタンを押した後かどうか
    boolean afterShousuten = false;

    //押された演算子ボタンの名前
    String currentOp = "";
    String zeibuttonOp = "";

	//フレームのビルド
	public Dentaku() {
		contentPane.setLayout(borderLayout1);
		this.setSize(new Dimension(250, 300));
		this.setTitle("電卓アプリ");
		this.setContentPane(contentPane);

        //テキストフィールドを配置
        result.setHorizontalAlignment(JTextField.RIGHT);
		contentPane.add(result, BorderLayout.NORTH);

        //ボタンを配置するパネルを用意
		JPanel keyPanel = new JPanel();

        //5行4列のGridLayoutにする
		keyPanel.setLayout(new GridLayout(5, 4));
		contentPane.add(keyPanel, BorderLayout.CENTER);

        //ボタンをレイアウトにはめこんでいく
        keyPanel.add(new ClearButton(), 0);
        keyPanel.add(new ZeiButton("税(追)"), 1);
        //一時的に空ボタンを配置
        keyPanel.add(new ZeiButton("税(消)"), 2);
        keyPanel.add(new CalcButton("÷"), 3);

		keyPanel.add(new NumberButton("7"), 4);
		keyPanel.add(new NumberButton("8"), 5);
		keyPanel.add(new NumberButton("9"), 6);
        keyPanel.add(new CalcButton("×"), 7);

		keyPanel.add(new NumberButton("4"), 8);
		keyPanel.add(new NumberButton("5"), 9);
		keyPanel.add(new NumberButton("6"), 10);
        keyPanel.add(new CalcButton("－"), 11);

		keyPanel.add(new NumberButton("1"), 12);
		keyPanel.add(new NumberButton("2"), 13);
		keyPanel.add(new NumberButton("3"), 14);
        keyPanel.add(new CalcButton("＋"), 15);

		keyPanel.add(new NumberButton("0"), 16);
        keyPanel.add(new KaraButton(), 17);
		keyPanel.add(new NumberButton("."), 18);
		keyPanel.add(new CalcButton("＝"), 19);

		this.setVisible(true);
	}

    //テキストフィールドに引数の文字列をつなげる
    public void appendResult(String c) {
        double i = Double.parseDouble(result.getText());
        if(i < 100000000){
            if(!afterCalc) {
                //演算子ボタンを押す前の処理
                //押したボタンの名前(数値)をつなげる
                if(initialValue == "0"){
                    if(c == "."){
                        result.setText(result.getText() + c);
                        initialValue = result.getText();
                    } else {
                        result.setText(c);
                        initialValue = result.getText();
                    }
                } else {
                    result.setText(result.getText() + c);
                }
            } else {
                //演算子ボタンを押した後
                //押したボタンの文字列だけを設定
                afterShousuten = false;
                result.setText(c);
                afterCalc = false;
            }
        } else {
            result.setText("エラー9桁");
            error = true;
        }
    }

    //数字を入力するボタンの定義
    public class NumberButton extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;

        public NumberButton(String keyTop) {
            //JButtonクラスのコンストラクタを呼び出す
            super(keyTop);

            //このボタンにアクションイベントのリスナを設定
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent evt) {
            afterzeibutton = false;
            addzeibutton = false;
            //ボタンの名前を取り出す
            String keyNumber = this.getText();

            //ボタンの名前をテキストフィールドにつなげる
            if(!error) {
                if(keyNumber == "."){
                    if(!afterShousuten){
                        appendResult(keyNumber);
                        afterShousuten = true;
                    }
                } else {
                    appendResult(keyNumber);
                }
            } else {
                result.setText(this.getText());
                error = false;
            }

            //isStacked = true;
        }
    }

    //演算子ボタンを定義
    public class CalcButton extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;

        public CalcButton(String op) {
            super(op);
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            afterzeibutton = false;
            addzeibutton = false;
            if(isStacked) {
                //以前に演算子ボタンが押されていたとき
                //計算結果を返す
                double resultValue = (Double.valueOf(result.getText())).doubleValue();

                //演算子に応じて計算する
                try{
                    if(currentOp.equals("＋"))
                        stackedValue += resultValue;
                    else if(currentOp.equals("－"))
                        stackedValue -= resultValue;
                    else if(currentOp.equals("×"))
                        stackedValue *= resultValue;
                    else if(currentOp.equals("÷"))
                        stackedValue /= resultValue;
                } catch(ArithmeticException ex) {
                    System.out.println("例外:" + ex);
                    result.setText("不可能な計算式です");
                }
                
                //計算結果をテキストフィールドに設定
                if(stackedValue > 100000000){
                    result.setText("計算エラー");
                    error = true;
                    isStacked = false;
                } else {
                    if(stackedValue == (int)stackedValue){
                        result.setText(String.valueOf((int)stackedValue));
                    } else {
                        result.setText(String.valueOf(stackedValue));
                    }
                }
            }
            //ボタン名から押されたボタンの演算子を取り出す
            currentOp = this.getText();
            stackedValue = (Double.valueOf(result.getText())).doubleValue();
            afterCalc = true;
            afterCalczei = true;
            if(currentOp.equals("=")){
                isStacked = false;
            } else {
                isStacked = true;
            }
        }
    }

    //クリアボタンの定義
    public class ClearButton extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;

        public ClearButton() {
            super("AC");
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent evt) {
            initialValue = "0";
            stackedValue = 0.0;
            error = false;
            isStacked = false;
            afterzeibutton = false;
            addzeibutton = false;
            afterCalc = false;
            afterCalczei = false;
            afterShousuten = false;
            currentOp = "";
            zeibuttonOp = "";            
            result.setText(String.valueOf(initialValue));
        }
    }

    //消費税ボタンの定義
    public class ZeiButton extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;

        public ZeiButton(String zei) {
            super(zei);
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent evt) {
            double resultValue = (Double.valueOf(result.getText())).doubleValue();
            zeibuttonOp = this.getText();

            //演算子ボタンを押す前
            //stackedValueに処理結果を格納する
            if(!afterCalczei){
                //if(isStacked) { //数字を入力した後でないと受け付けない
                    if(!afterzeibutton){ //初めて税ボタンを押す処理
                        if(zeibuttonOp.equals("税(追)")){
                            stackedValue = resultValue * 1.08;
                            addzeibutton = true;
                            afterCalcaddzeibutton = 1;
                        } else if(zeibuttonOp.equals("税(消)")) {
                            stackedValue = resultValue / 1.08;
                            addzeibutton = false;
                        }

                        //stackedValueの結果を返す
                        if(stackedValue == (int)stackedValue){
                            result.setText(String.valueOf((int)stackedValue));
                        } else {
                            result.setText(String.valueOf(stackedValue));
                        }

                        afterzeibutton = true;

                    } else if (afterzeibutton) { //2回目に税ボタンを押すとき
                        //同じ処理をせず、もう一方の処理のみを許す
                        if(!addzeibutton){
                            if(zeibuttonOp.equals("税(追)")){
                                stackedValue = resultValue * 1.08;
                                addzeibutton = true;
                            }
                        } else if(addzeibutton){
                            if(zeibuttonOp.equals("税(消)")) {
                                stackedValue = resultValue / 1.08;
                                addzeibutton = false;
                            }
                        }

                        //結果を返す
                        if(stackedValue == (int)stackedValue){
                            result.setText(String.valueOf((int)stackedValue));
                        } else {
                            result.setText(String.valueOf(stackedValue));
                        }
                    }
               // }
            }

            //演算子ボタンを押した後
            //resultValueに処理結果を格納
            else if (afterCalczei) {
                if(isStacked) { //数字を入力した後でないと受け付けない
                    if(!afterzeibutton){ //初めて税ボタンを押す処理
                        if(zeibuttonOp.equals("税(追)")){
                            resultValue = resultValue * 1.08;
                            addzeibutton = true;
                            afterCalcaddzeibutton = 1;
                        } else if(zeibuttonOp.equals("税(消)")) {
                            resultValue = resultValue / 1.08;
                            addzeibutton = false;
                            afterCalcaddzeibutton = 0;
                        }

                        //resultValueの結果を返す
                        if(resultValue == (int)resultValue){
                            result.setText(String.valueOf((int)resultValue));
                        } else {
                            result.setText(String.valueOf(resultValue));
                        }

                        afterzeibutton = true;

                    } else if (afterzeibutton) { //2回目に税ボタンを押すとき
                        //同じ処理をせず、もう一方の処理のみを許す
                        if(!addzeibutton){
                            if(zeibuttonOp.equals("税(追)")){
                                resultValue = resultValue * 1.08;
                                addzeibutton = true;
                                afterCalcaddzeibutton = 1;
                            }
                        } else if(addzeibutton){
                            if(zeibuttonOp.equals("税(消)")) {
                                resultValue = resultValue / 1.08;
                                addzeibutton = false;
                                afterCalcaddzeibutton = 0;
                            }
                        }

                        //結果を返す
                        if(resultValue == (int)resultValue){
                            result.setText(String.valueOf((int)resultValue));
                        } else {
                            result.setText(String.valueOf(resultValue));
                        }
                    }
                }

            }
        }
    }

    //空のボタンの定義
    public class KaraButton extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;

        public KaraButton() {
            super(" ");
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent evt) {}
    }
}