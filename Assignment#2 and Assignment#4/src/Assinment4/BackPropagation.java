package Assinment4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class BackPropagation 
{
	private BufferedReader bufferedReader;
	private int trainingExamplesNumber;
	private int m;
	private int l;
	private int n;
	private ArrayList<ArrayList<Double>> inputVectors;
	private ArrayList<ArrayList<Double>> outputVectors;
	private ArrayList<ArrayList<Double>> hiddenNodesWeights;
	private ArrayList<ArrayList<Double>> outputNodesWeights;
	private final double LEARNING_RATE = 0.7;
	
	public BackPropagation(FileReader fileReader) 
	{
		bufferedReader = new BufferedReader(fileReader);
	}
	
	private void readFile() throws IOException
	{
		String temp;
		String[] arrayTemp;
		String innerTemp;
		ArrayList<Double> inputVector = null;
		ArrayList<Double> outputVector = null;
		inputVectors = new ArrayList<>();
		outputVectors = new ArrayList<>();
		
		temp = bufferedReader.readLine();
		arrayTemp = temp.split(" ");
		m = Integer.parseInt(arrayTemp[0]);
		l = Integer.parseInt(arrayTemp[1]);
		n = Integer.parseInt(arrayTemp[2]);
		temp = bufferedReader.readLine();
		trainingExamplesNumber = Integer.parseInt(temp);
		for(int i=0; i < trainingExamplesNumber; i++)
		{
			inputVector = new ArrayList<>();
			outputVector = new ArrayList<>();
			temp = bufferedReader.readLine();
			/*arrayTemp = temp.split(" ");
			for(int j=0; j < m; j++)
				inputVector.add(Double.parseDouble(arrayTemp[j]));
			for(int j=m+2; j < m+n+2; j++)
				outputVector.add(Double.parseDouble(arrayTemp[j]));*/
			innerTemp = "";
			int j=0;
			for(int k=0; j < temp.length() && k < m; j++)
			{
				if(temp.charAt(j) == ' ' && !innerTemp.isEmpty())
				{
					inputVector.add(Double.parseDouble(innerTemp));
					innerTemp = "";
					k++;
				}
				else if(temp.charAt(j) != ' ')
					innerTemp += temp.charAt(j);

				if(j == temp.length()-1 && !innerTemp.isEmpty())
					outputVector.add(Double.parseDouble(innerTemp));
			}
			innerTemp = "";
			for(int k=m; j < temp.length() && k < m+n; j++)
			{
				if(temp.charAt(j) == ' ' && !innerTemp.isEmpty())
				{
					outputVector.add(Double.parseDouble(innerTemp));
					innerTemp = "";
					k++;
				}
				else if(temp.charAt(j) != ' ')
					innerTemp += temp.charAt(j);
				
				if(j == temp.length()-1 && !innerTemp.isEmpty())
					outputVector.add(Double.parseDouble(innerTemp));
			}
			inputVectors.add(inputVector);
			outputVectors.add(outputVector);
		}
	}

	private void initializeWeights()
	{
		hiddenNodesWeights = new ArrayList<>();
		outputNodesWeights = new ArrayList<>();
		ArrayList<Double> hiddenNodesVector = null;
		ArrayList<Double> outputNodesVector = null;
		double randomNumber;
		Random rand = new Random();
		
		for(int j=0; j < l; j++)
		{
			hiddenNodesVector = new ArrayList<>();
			for(int i=0; i < m; i++)
			{
				randomNumber = rand.nextDouble() * 10 - 5;
				hiddenNodesVector.add(randomNumber);
			}
			hiddenNodesWeights.add(hiddenNodesVector);
		}
		
		for(int k=0; k < n; k++)
		{
			outputNodesVector = new ArrayList<>();
			for(int j=0; j < l; j++)
			{
				randomNumber = rand.nextDouble() * 10 - 5;
				outputNodesVector.add(randomNumber);
			}
			outputNodesWeights.add(outputNodesVector);
		}
	}

	private ArrayList<Double> computeHiddenLayerNetInput(int trainingExampleIndex)
	{
		double hiddenNodeNetInput;
		ArrayList<Double> hiddenLayerNetInput = new ArrayList<>();
		for(int j=0; j < l; j++)
		{
			hiddenNodeNetInput = 0;
			for(int i=0; i < m; i++)
				hiddenNodeNetInput += hiddenNodesWeights.get(j).get(i) * inputVectors.get(trainingExampleIndex).get(i);
			hiddenLayerNetInput.add(hiddenNodeNetInput);
		}
		return hiddenLayerNetInput;
	}

	private ArrayList<Double> computeHiddenLayerNetOutput(ArrayList<Double> hiddenLayerNetInput)
	{
		ArrayList<Double> hiddenLayerNetOutput = new ArrayList<>();
		for(int j=0; j < l; j++)
			hiddenLayerNetOutput.add(1.0/(1 + Math.pow(Math.E, -(hiddenLayerNetInput.get(j)))));
		return hiddenLayerNetOutput;
	}

	private ArrayList<Double> computeOutputLayerNetInput(ArrayList<Double> hiddenLayerNetOutput)
	{
		double outputNodeNetInput;
		ArrayList<Double> outputLayerNetInput = new ArrayList<>();
		for(int k=0; k < n; k++)
		{
			outputNodeNetInput = 0;
			for(int j=0; j < l; j++)
				outputNodeNetInput += outputNodesWeights.get(k).get(j) * hiddenLayerNetOutput.get(j);
			outputLayerNetInput.add(outputNodeNetInput);
		}
		return outputLayerNetInput;
	}

	private ArrayList<Double> computeOutputLayerNetOutput(ArrayList<Double> outputLayerNetInput)
	{
		ArrayList<Double> outputLayerNetOutput = new ArrayList<>();
		for(int k=0; k < n; k++)
			outputLayerNetOutput.add(1.0/(1 + Math.pow(Math.E, -(outputLayerNetInput.get(k)))));
		return outputLayerNetOutput;
	}	

	private ArrayList<Double> computeNetworkErrorTerms(int trainingExampleIndex, ArrayList<Double> outputLayerNetOutput)
	{
		ArrayList<Double> networkErrorTerms = new ArrayList<>();
		for(int k=0; k < n; k++)
			networkErrorTerms.add(outputVectors.get(trainingExampleIndex).get(k) - outputLayerNetOutput.get(k));
	
		return networkErrorTerms;
	}
	
	private ArrayList<Double> computeOutputLayerErrorTerms(ArrayList<Double> networkErrorTerms, ArrayList<Double> outputLayerNetOutput)
	{
		ArrayList<Double> outputLayerErrorTerms = new ArrayList<>();
		for(int k=0; k < n; k++)
			outputLayerErrorTerms.add(outputLayerNetOutput.get(k)*(1 - outputLayerNetOutput.get(k))*networkErrorTerms.get(k));
		return outputLayerErrorTerms;
	}
	
	private void updateOutputLayerWeights(ArrayList<Double> outputLayerErrorTerms, ArrayList<Double> hiddenLayerNetOutput)
	{
		double temp;
		ArrayList<Double> vTemp;
		for(int k=0; k < n; k++)
		{
			for(int j=0; j < l; j++)
			{
				vTemp = outputNodesWeights.get(k);
				temp = outputNodesWeights.get(k).get(j);
				temp += LEARNING_RATE * outputLayerErrorTerms.get(k) * hiddenLayerNetOutput.get(j);
				vTemp.set(j, temp);
				outputNodesWeights.set(k, vTemp);
			}
		}
	}

	private ArrayList<Double> computeHiddenLayerErrorTerms(ArrayList<Double> networkErrorTerms, ArrayList<Double> hiddenLayerNetOutput)
	{
		ArrayList<Double> hiddenLayerErrorTerms = new ArrayList<>();
		double temp;
		for(int j=0; j < l; j++)
		{
			temp = 0;
			hiddenLayerErrorTerms.add(hiddenLayerNetOutput.get(j) * (1 - hiddenLayerNetOutput.get(j)));
			for(int k=0; k < n; k++)
				temp += networkErrorTerms.get(k) * outputNodesWeights.get(k).get(j);
			temp *= hiddenLayerErrorTerms.get(j);
			hiddenLayerErrorTerms.set(j, temp);
		}
		return hiddenLayerErrorTerms;
	}

	private void updateHiddenLayerWeights(ArrayList<Double> hiddenLayerErrorTerms, int trainingExampleIndex)
	{
		for(int j=0; j < l; j++)
		{
			for(int i=0; i < m; i++)
				hiddenNodesWeights.get(j).set(i, hiddenNodesWeights.get(j).get(i) + LEARNING_RATE * hiddenLayerErrorTerms.get(j) * inputVectors.get(trainingExampleIndex).get(i));
		}
	}

	private double computeMSE(ArrayList<Double> networkErrorTerms)
	{
		double MSE=0;
		for(int k=0; k < n; k++)
			MSE += Math.pow(networkErrorTerms.get(k), 2);
		MSE *= 0.5;
		return MSE;
	}

	private void writeFile(FileWriter fileWriter) throws IOException
	{
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		for(int j=0; j < l; j++)
		{
			for(int i=0; i < m; i++)
			{
				bufferedWriter.write(hiddenNodesWeights.get(j).get(i).toString());
				bufferedWriter.write(' ');
			}
			bufferedWriter.newLine();
		}
		for(int k=0; k < n; k++)
		{
			for(int j=0; j < l; j++)
			{
				bufferedWriter.write(outputNodesWeights.get(k).get(j).toString());
				bufferedWriter.write(' ');
			}
			if(k != n-1)
				bufferedWriter.newLine();
		}
		bufferedWriter.flush();
		/*for(int i=0; i < l; i++)
		{
			bufferedWriter.write(hiddenNodesWeights.get(i).toString());
			bufferedWriter.newLine();
		}
		
		for(int i=0; i < n; i++)
		{
			bufferedWriter.write(outputNodesWeights.get(i).toString());
			if(i != n-1)
				bufferedWriter.newLine();
		}
		bufferedWriter.flush();*/
	}

	public void run() throws IOException
	{
		FileWriter fileWriter = new FileWriter(new File("BackPropagationResult.txt"));
		ArrayList<Double> hiddenLayerNetInput = null;
		ArrayList<Double> hiddenLayerNetOutput = null;
		ArrayList<Double> outputLayerNetInput = null;
		ArrayList<Double> outputLayerNetOutput = null;
		ArrayList<Double> networkErrorTerms = null;
		ArrayList<Double> outputLayerErrorTerms = null;
		ArrayList<Double> hiddenLayerErrorTerms = null;
		double MSE;
		
		readFile();
		initializeWeights();
		for(int trainingExampleIndex=0; trainingExampleIndex < trainingExamplesNumber; trainingExampleIndex++)
		{
			for(int i=0; i < 500; i++)
			{
				hiddenLayerNetInput = computeHiddenLayerNetInput(trainingExampleIndex);
				hiddenLayerNetOutput = computeHiddenLayerNetOutput(hiddenLayerNetInput);
				outputLayerNetInput = computeOutputLayerNetInput(hiddenLayerNetOutput);
				outputLayerNetOutput = computeOutputLayerNetOutput(outputLayerNetInput);
				networkErrorTerms = computeNetworkErrorTerms(trainingExampleIndex, outputLayerNetOutput);
				outputLayerErrorTerms = computeOutputLayerErrorTerms(networkErrorTerms, outputLayerNetOutput);
				updateOutputLayerWeights(outputLayerErrorTerms, hiddenLayerNetOutput);
				hiddenLayerErrorTerms = computeHiddenLayerErrorTerms(networkErrorTerms, hiddenLayerNetOutput);
				updateHiddenLayerWeights(hiddenLayerErrorTerms, trainingExampleIndex);
			}
			MSE = computeMSE(networkErrorTerms);
			System.out.println(MSE);
		}
		writeFile(fileWriter);
	}	

}
