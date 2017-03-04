package Assinment4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class FeedForward 
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
	
	public FeedForward(FileReader fileReader) 
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

	private void initializeWeights() throws IOException
	{
		String temp;
		String[] vTemp;
		ArrayList<Double> weightsTemp = null;
		hiddenNodesWeights = new ArrayList<>();
		outputNodesWeights = new ArrayList<>();
		bufferedReader = new BufferedReader(new FileReader(new File("BackPropagationResult.txt")));
		
		for(int j=0; j < l; j++)
		{
			temp = bufferedReader.readLine();
			vTemp = temp.split(" ");
			weightsTemp = new ArrayList<>();
			for(int i=0; i < m; i++)
				weightsTemp.add(Double.parseDouble(vTemp[i]));
			hiddenNodesWeights.add(weightsTemp);
		}
		
		for(int k=0; k < n; k++)
		{
			temp = bufferedReader.readLine();
			vTemp = temp.split(" ");
			weightsTemp = new ArrayList<>();
			for(int j=0; j < l; j++)
				weightsTemp.add(Double.parseDouble(vTemp[j]));
			outputNodesWeights.add(weightsTemp);
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
	
	private double computeMSE(ArrayList<Double> networkErrorTerms)
	{
		double MSE=0;
		for(int k=0; k < n; k++)
			MSE += Math.pow(networkErrorTerms.get(k), 2);
		MSE *= 0.5;
		return MSE;
	}
	
	public void run() throws IOException
	{
		ArrayList<Double> hiddenLayerNetInput = null;
		ArrayList<Double> hiddenLayerNetOutput = null;
		ArrayList<Double> outputLayerNetInput = null;
		ArrayList<Double> outputLayerNetOutput = null;
		ArrayList<Double> networkErrorTerms = null;
		double MSE;
		
		readFile();
		initializeWeights();
		for(int trainingExampleIndex=0; trainingExampleIndex < trainingExamplesNumber; trainingExampleIndex++)
		{
			hiddenLayerNetInput = computeHiddenLayerNetInput(trainingExampleIndex);
			hiddenLayerNetOutput = computeHiddenLayerNetOutput(hiddenLayerNetInput);
			outputLayerNetInput = computeOutputLayerNetInput(hiddenLayerNetOutput);
			outputLayerNetOutput = computeOutputLayerNetOutput(outputLayerNetInput);
			networkErrorTerms = computeNetworkErrorTerms(trainingExampleIndex, outputLayerNetOutput);
			MSE = computeMSE(networkErrorTerms);
			System.out.println(MSE);
		}
	}	
}
