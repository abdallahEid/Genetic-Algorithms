import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(new FileReader("sample.txt"));
		ArrayList<Variable> inputVariables = new ArrayList<>();
		ArrayList<Variable> outputVariables = new ArrayList<>();
		int numberOfInputVariables = sc.nextInt() ;
		int numberOfSets ;
		float value ;
		for(int i=0 ; i < numberOfInputVariables ; i++){
			Variable inputVariable = new Variable() ;
			inputVariable.name = sc.next();
			inputVariable.crisp = sc.nextFloat();
			numberOfSets = sc.nextInt();;
			inputVariable.sets.clear();
			for(int j=0; j < numberOfSets ; j++){
				Set set = new Set();
				set.name = sc.next();
				set.type = sc.next();
				if(set.type.equals("triangle")){
					for(int k=0 ; k < 3 ; k++){
						value = sc.nextFloat();
						set.x_values.add(value) ;
					}
				}
				else if(set.type.equals("trapezoidal")){
					for(int k=0 ; k < 4 ; k++){
						value = sc.nextFloat();
						set.x_values.add(value) ;
					}
				}
				inputVariable.sets.add(set) ;
			}
			inputVariables.add(inputVariable);
		}
		Operation operation = new Operation();
		operation.fuzzification(inputVariables);
		
		int numberOfSetsforOutput = 0 ;
		Variable outputVariable = new Variable() ;
		outputVariable.name = sc.next();
		numberOfSetsforOutput = sc.nextInt();
		for(int j=0; j < numberOfSetsforOutput ; j++){
			Set set = new Set();
			set.name = sc.next();
			set.type = sc.next();
			if(set.type.equals("triangle")){
				for(int k=0 ; k < 3 ; k++){
					value = sc.nextFloat();
					set.x_values.add(value) ;
				}
			}
			else if(set.type.equals("trapezoidal")){
				for(int k=0 ; k < 4 ; k++){
					value = sc.nextFloat();
					set.x_values.add(value) ;
				}
			}
			outputVariable.sets.add(set) ;
		}
		// inference rules 
		String varName = null , setName = null , ch , operator;
		ArrayList <String> operators = new ArrayList<>();
		ArrayList <Float> answers = new ArrayList<>();
		ArrayList <String> outputSets = new ArrayList<>();
		ArrayList <Float> outputAnswers = new ArrayList<>();
		int numberOfRules = sc.nextInt();
		for(int i=0 ; i < numberOfRules ; i++){
			answers.clear();
			operators.clear();
			int numberOfPremises = sc.nextInt();
			for(int l=0 ; l < numberOfPremises ; l++){
				varName = sc.next(); 
				ch = sc.next();
				setName = sc.next();
				if(l != numberOfPremises-1){
					operator = sc.next();
					operators.add(operator) ;
				}
				for(int o=0 ; o < inputVariables.size() ; o++){
					if(varName.equals(inputVariables.get(o).name)){
						for(int k=0 ; k < inputVariables.get(o).sets.size() ; k++){
							if(inputVariables.get(o).sets.get(k).name.equals(setName)) {
								answers.add(inputVariables.get(o).yOfCrisp.get(k)) ;
							}
						}
					}
				}
			}
			
			String outputName = sc.next(); 
			ch = sc.next() ;
			String outputValue = sc.next() ;
			
			for(int p=0 ; p < answers.size()-1; p++){
				if(operators.get(p).equals("AND"))
					answers.set(p+1, Math.min(answers.get(p), answers.get(p+1))) ;
				else
					answers.set(p+1, Math.max(answers.get(p), answers.get(p+1))) ;
			}
			outputAnswers.add(answers.get(answers.size()-1)) ;
			outputSets.add(outputValue) ;
			System.out.println(answers.get(answers.size()-1) + outputValue);
			
		}
		
		
		// defuzzificatin 
		float x_avg , sum=0;
		for(int i=0 ; i < inputVariables.size(); i++){
			for(int j=0 ; j < inputVariables.get(i).sets.size(); j++){
				for(int k=0 ; k < inputVariables.get(i).sets.get(j).x_values.size() ; k++){
					sum += inputVariables.get(i).sets.get(j).x_values.get(k) ;
				}
				x_avg = (sum / (inputVariables.get(i).sets.get(j).x_values.size())) ;
				inputVariables.get(i).averages.add(x_avg) ;
			}
		}
		sum=0;
		for(int i=0 ; i < inputVariables.size(); i++){
			for (int j=0 ; j <inputVariables.get(i).averages.size() ; j++){
				sum += outputAnswers.get(j) * inputVariables.get(i).averages.get(j) ;
			}
		}
		float outputSum = 0 ;
		for(int i=0 ; i < outputAnswers.size(); i++){
			outputSum += outputAnswers.get(i) ;
		}
		System.out.println("------------------------------------");
		float predict = (sum / outputSum) ;
		System.out.println(predict);
	}

}
