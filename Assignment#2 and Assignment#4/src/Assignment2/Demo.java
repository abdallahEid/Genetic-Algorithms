package Assignment2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Demo 
{

	public static void main(String[] args) 
	{
		final int NUMBER_OF_GENERATIONS = 100;
		
		int numberOfDataSets;
		int numberOfPoints;
		int degree;
		Scanner input = null;
		CurveFitting curveFitting = null;
		Vector<Point> points = null;
		Point temp;
		Vector<float[]> selectedChromosomes;
		Vector<float[]> chromosomesAfterCrossover;
		float maxFitness;
		float[] solution;
		String writable;
		BufferedWriter output = null;
		
		try {
			input = new Scanner(new File("input.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("failed to read the file");
		}
		
		try {
			output = new BufferedWriter(new FileWriter(new File("output.txt")));
		} catch (IOException e) {
			System.out.println("Failed to make the output file or open it");
		}
		
		numberOfDataSets = input.nextInt();
		for(int i=0; i < numberOfDataSets; i++)
		{
			numberOfPoints = input.nextInt();
			degree = input.nextInt();
			points = new Vector<>();
			for(int k=0; k < numberOfPoints; k++)
			{
				temp = new Point();
				temp.x = input.nextFloat();
				temp.y = input.nextFloat();
				points.addElement(temp);
			}
			curveFitting = new CurveFitting(degree+1, numberOfPoints);
			curveFitting.setPoints(points);
			curveFitting.generatePopulation();
			try {
				output.write("dataset #" + i);
				output.newLine();
			} catch (IOException e) {
				System.err.println("dataset block");
			}
			for(int k=0; k < NUMBER_OF_GENERATIONS; k++)
			{
				curveFitting.calculateFitness();
				for(int j=0; j < CurveFitting.POPULATION_SIZE/2; j++)
				{
					selectedChromosomes = curveFitting.select();
					chromosomesAfterCrossover = curveFitting.crossover(selectedChromosomes.get(0), selectedChromosomes.get(1));
					curveFitting.addToNewPopulation(curveFitting.mutation(chromosomesAfterCrossover.get(0), k, NUMBER_OF_GENERATIONS));
					curveFitting.addToNewPopulation(curveFitting.mutation(chromosomesAfterCrossover.get(0), k, NUMBER_OF_GENERATIONS));
				}
				curveFitting.nextPopulation();
			}
			maxFitness = curveFitting.maxFitness();
			solution = curveFitting.getBestSolution();
			writable = String.valueOf(maxFitness);
			try {
				output.write("MSE= ");
				output.write(writable);
				output.newLine();
				for(int k=0; k < degree+1; k++)
				{
					writable = String.valueOf(solution[k]);
					output.write("C" + k + "= ");
					if(k != degree)
						output.write(writable + ", ");
					else
						output.write(writable + " ");
				}
				output.newLine();
			} catch (IOException e) {
				System.err.println("Solution block");
			}
		}
		try {
			output.flush();
		} catch (IOException e) {
			System.err.println("Cannot fluch");
		}
	}
}
