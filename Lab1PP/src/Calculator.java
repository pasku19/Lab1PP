import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.Stack;

public class Calculator extends JFrame{
    JButton digits[] ={
            new JButton(" 0 "),
            new JButton(" 1 "),
            new JButton(" 2 "),
            new JButton(" 3 "),
            new JButton(" 4 "),
            new JButton(" 5 "),
            new JButton(" 6 "),
            new JButton(" 7 "),
            new JButton(" 8 "),
            new JButton(" 9 ")
    };

    JButton operators[] ={
            new JButton(" + "), //0 fI
            new JButton(" - "), //1
            new JButton(" * "), //2
            new JButton(" / "), //3
            new JButton(" = "), //4
            new JButton(" C "), //5
            new JButton(" ( "), //6
            new JButton(" ) ") //7
    };

    String oper_values[] ={"+", "-", "*", "/", "=", "", "(", ")"};

    JTextArea area = new JTextArea(3, 5);

    public static void main(String[] args){
        Calculator calculator = new Calculator();
        calculator.setSize(350, 250);
        calculator.setTitle(" Java-Calc, PP Lab1 ");
        calculator.setResizable(false);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Calculator(){
        add(new JScrollPane(area), BorderLayout.NORTH);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout());

        for (int i = 0; i < 10; i++)
            buttonpanel.add(digits[i]);

        for (int i = 0; i < operators.length; i++)
            buttonpanel.add(operators[i]);

        add(buttonpanel, BorderLayout.CENTER);
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        for (int i = 0; i < 10; i++){
            int fI = i;
            digits[i].addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent actionEvent){
                    area.append(Integer.toString(fI));
                }
            });
        }

        for (int i = 0; i < operators.length; i++){
            int fI = i;
            operators[i].addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent actionEvent){
                    if (fI == 5) // Butonul "C"
                        area.setText("");
                    else if (fI == 4){ // Butonul "="
                        try{
                            String expression = area.getText();
                            String postfix = postfix(expression);
                            double rez = f(postfix);
                            area.append(" = " + rez);
                        } catch (Exception e){
                            area.setText(" !!!Probleme!!! ");
                        }
                    } else{
                        area.append(oper_values[fI]);
                    }
                }
            });
        }
    }

    public String postfix(String expression){
        StringBuilder rez=new StringBuilder();
        Stack<Character> stack=new Stack<>();
        for (int i=0;i<expression.length();i++)
        {
            char c=expression.charAt(i);
            if (Character.isDigit(c))
                rez.append(c);
            else if (c=='(')
            {
                stack.push(c);
            }
            else if (c==')')
            {
                while (!stack.isEmpty() && stack.peek()!='(')
                {
                    rez.append(" ").append(stack.pop());
                }
                stack.pop();
            }
            else if (isOperator(c))
            {
                rez.append(" ");
                while (!stack.isEmpty() && prioritate(stack.peek())>=prioritate(c))
                {
                    rez.append(stack.pop()).append(" ");
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty())
        {
            rez.append(" ").append(stack.pop());
        }

        return rez.toString();
    }

    public double f(String expression){
        Stack<Double> stack=new Stack<>();
        String[] tokens=expression.split(" ");

        for (String elem : tokens)
        {
            if (Character.isDigit(elem.charAt(0)))
                stack.push(Double.valueOf(elem));
            else if (isOperator(elem.charAt(0)))
            {
                double b=stack.pop();
                double a=stack.pop();
                switch (elem.charAt(0))
                {
                    case '+':
                        stack.push(a + b);
                        break;
                    case '-':
                        stack.push(a - b);
                        break;
                    case '*':
                        stack.push(a * b);
                        break;
                    case '/':
                        stack.push(a / b);
                        break;
                }
            }
        }
        return stack.pop();
    }

    public boolean isOperator(char c){
        return c=='+' || c=='-' || c=='*' || c=='/';
    }

    public int prioritate(char c){
        switch (c){
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }
}
