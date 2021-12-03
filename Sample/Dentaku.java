package Sample;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JTextField;
import java.awt.Color;
//import javafx.scene.paint.*;
import java.awt.Font;

public class Dentaku extends JFrame {
	private static final long serialVersionUID = 1L;

	JPanel contentPane = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();

    //計算結果を表示するテキストフィールド
    String initialValue = "0";
	JTextField result = new JTextField(initialValue);
    JLabel subresult = new JLabel(); //計算過程を表示するサブのテキストフィールド

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

    boolean aftershoki = false;

    //小数点ボタンを押した後かどうか
    boolean afterShousuten = false;

    //押された数字ボタンの数値
    String keyNumber = "";

    //押された演算子ボタンの名前
    String currentOp = "";
    String zeibuttonOp = "";

	//フレームのビルド
	public Dentaku() {
		contentPane.setLayout(borderLayout1);
		this.setSize(new Dimension(300, 360));
		this.setTitle("電卓アプリ");
		this.setContentPane(contentPane);

        //テキストフィールドを配置するパネルを用意
		JPanel textPanel = new JPanel();
        GridBagLayout grid = new GridBagLayout(); 
        textPanel.setLayout(grid); //2行1列のGridLayoutにする

        GridBagConstraints gbc = new GridBagConstraints();

        //2行1列のパネルにテキストフィールドをはめこむ
        gbc.weightx = 1.0d;
        gbc.fill = GridBagConstraints.BOTH;
        grid.setConstraints(subresult, gbc);

        gbc.gridy = 1;
        gbc.weightx = 1.0d;
        gbc.fill = GridBagConstraints.BOTH;
        grid.setConstraints(result, gbc);

        textPanel.add(subresult);
        textPanel.add(result);

        //テキストフィールドをはめこんだパネルを上に配置する
        contentPane.add(textPanel, BorderLayout.NORTH);

        //テキストフィールドの設定を決める
        //resultの設定
        result.setHorizontalAlignment(JTextField.RIGHT); //右側に入力値を表示
        result.setFont(new Font("明朝体", Font.BOLD, 40)); //表示される文字の設定
        //result.setPreferredSize(new Dimension(300, 60)); //NORTHのサイズを設定

        //subresultの設定
        subresult.setHorizontalAlignment(JLabel.RIGHT); //右側に入力値を表示
        subresult.setFont(new Font("メイリオ", Font.PLAIN, 16)); //表示される文字の設定
        //subresult.setPreferredSize(new Dimension(300, 30)); //NORTHのサイズを設定

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
    public void appendResult(String kn, String cp) { //kn=数値,cp=演算子
        double i = Double.parseDouble(result.getText());
        String sti = result.getText();

        if(i < 100000000){
            if(!afterCalc) {
                //演算子ボタンを押す前の処理
                //押したボタンの名前(数値)をつなげる
                if(sti.length() < 10){
                    if(initialValue == "0"){
                        if(kn.equals(".")){
                            result.setText(result.getText() + kn);
                            initialValue = result.getText();
                        } else {
                            result.setText(kn);
                            initialValue = result.getText();
                        }
                    } else {
                        result.setText(result.getText() + kn);
                    }
                }
            } else {
                //演算子ボタンを押した後
                //押したボタンの文字列だけを設定
                afterShousuten = false;
                afterCalc = false;
                if(!aftershoki){
                    result.setText(kn);
                } else {
                    if(sti.length() < 10){
                        result.setText(kn);
                    }
                }
            }
        } else {
            result.setText("エラー");
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
            
            this.setFont(new Font("明朝体", Font.BOLD, 32));
            this.setBackground(Color.WHITE);
        }

        public void actionPerformed(ActionEvent evt) {
            afterzeibutton = false;
            addzeibutton = false;
            //ボタンの名前を取り出す
            keyNumber = this.getText();

            //ボタンの名前をテキストフィールドにつなげる
            if(!error) {
                if(keyNumber == "."){
                    if(!afterShousuten){
                        appendResult(keyNumber, currentOp);
                        afterShousuten = true;
                    }
                } else {
                    appendResult(keyNumber, currentOp);
                }
            } else {
                result.setText(this.getText());
                error = false;
            }
        }
    }

    //演算子ボタンを定義
    public class CalcButton extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;

        public CalcButton(String op) {
            super(op);
            this.addActionListener(this);
            this.setFont(new Font("メイリオ", Font.PLAIN, 24));
            if(op.equals("＝")){
                this.setBackground(new Color(0,255,255));
            } else {
                this.setBackground(new Color(152,251,152));
            }
        }

        public void actionPerformed(ActionEvent e) {
            afterzeibutton = false;
            addzeibutton = false;
            if(isStacked) {
                //以前に演算子ボタンが押されていたとき
                //計算結果を返す
                double resultValue = (Double.valueOf(result.getText())).doubleValue();
                if(subresult.getText().equals("")){
                    subresult.setText(stackedValue + currentOp + resultValue);
                } else {
                    subresult.setText(subresult.getText() + currentOp + resultValue);
                }

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

                //double displayValue = stackedValue;
                
                //計算結果をテキストフィールドに設定
                //1億以上はエラー対象
                if(stackedValue >= 100000000){
                    result.setText("計算エラー");
                    error = true;
                    isStacked = false;
                } else {
                    String mojistackedValue = String.valueOf(stackedValue);
                    if(mojistackedValue.length() > 10) { //小数が続く数字に関す制御
                        double sisyagonyustv = 0; //四捨五入した値の初期値
                        for(int i=0; i<11; i++){
                            if(mojistackedValue.substring(i,i+1).equals(".")){ //小数点が何桁目にあるのかをiに格納
                                int sisuu = 9-i; //小数点の場所に応じて四捨五入する計算の10の指数を決める
                                int sisyagnyuten = (int) Math.pow(10, sisuu); //四捨五入で乗算する数字
                                sisyagonyustv = ((double)Math.round(stackedValue * sisyagnyuten))/sisyagnyuten;
                            }
                        }
                        result.setText(String.valueOf(sisyagonyustv)); //四捨五入した値をセット
                    } else {
                        if(stackedValue == (int)stackedValue){
                            result.setText(String.valueOf((int)stackedValue));
                        } else {
                            result.setText(String.valueOf(stackedValue));
                        }
                    }
                }
            }
            //ボタン名から押されたボタンの演算子を取り出す
            currentOp = this.getText();
            stackedValue = (Double.valueOf(result.getText())).doubleValue();
            afterCalc = true;
            afterCalczei = true;
            keyNumber = result.getText();
            //appendResult(keyNumber,currentOp);
            if(currentOp.equals("＝")){
                isStacked = false;
                subresult.setText(subresult.getText() + currentOp);
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
            this.setFont(new Font("メイリオ", Font.BOLD+Font.ITALIC, 24));
            this.setBackground(new Color(255,69,0));
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
            aftershoki = false;
            currentOp = "";
            zeibuttonOp = "";            
            result.setText(String.valueOf(initialValue));
            subresult.setText("");
        }
    }

    //消費税ボタンの定義
    public class ZeiButton extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;

        public ZeiButton(String zei) {
            super(zei);
            this.addActionListener(this);
            this.setBackground(new Color(255,255,0));
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
            this.setBackground(Color.WHITE);
        }

        public void actionPerformed(ActionEvent evt) {}
    }
}