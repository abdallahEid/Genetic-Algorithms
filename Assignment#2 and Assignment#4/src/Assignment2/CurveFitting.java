package Assignment2;

import java.util.Random;
import java.util.Vector;

import javax.swing.PopupFactory;

public class CurveFitting 
{
	private final float MIN_RANGE = -10;
	private final float MAX_RANGE = 10;
	public final static int POPULATION_SIZE = (int)1e4;
	
	private int numberOfCoeffecients;
	private int numberOfPoints;
	private Vector<Point> points;
	private Vector<float[]> population;
	private Vector<Float> fitness;
	private Vector<Float> rouletteWheel;
	Random random = new Random();
	private Vector<float[]> newPopulation;
	private float[] bestSolution;
	
	public CurveFitting(int numberOfCoeffecients, int numberOfPoints)
	{
		this.numberOfCoeffecients = numberOfCoeffecients;
		this.numberOfPoints = numberOfPoints;
		population = new Vector<float[]>();
		fitness = new Vector<Float>();
		newPopulation = new Vector<float[]>();
	}
	
	public void setPoints(Vector<Point> points)
	{
		this.points = points;
	}
	
	public void generatePopulation()
	{
		float[] chromosome = null;
		float temp;
		for(int i=0; i < POPULATION_SIZE; i++)
		{
			chromosome = new float[numberOfCoeffecients];
			for(int l=0; l < numberOfCoeffecients; l++)
			{
				temp = random.nextFloat() * (MAX_RANGE - MIN_RANGE) + MIN_RANGE;
				chromosome[l] = temp;
			}
			population.addElement(chromosome);
		}
	}
	
	public void calculateFitness()
	{
		float yCalculated;
		float MSE;
		float[] chromosome;
		for(int i=0; i < POPULATION_SIZE; i++)
		{
			MSE = 0;
			chromosome = population.get(i);
			for(int l=0; l < numberOfPoints; l++)
			{
				yCalculated = 0;
				for(int j=numberOfCoeffecients-1; j >= 0; j--)
					yCalculated += Math.pow(points.get(l).x, j) * chromosome[j];
				MSE += Math.pow((yCalculated - points.get(l).y) , 2);
			}
			fitness.addElement(MSE* ((float)1/numberOfPoints));
		}
	}

	
	public Vector<float[]> select()
	{
		createRoulleteWheel();
		float max = rouletteWheel.get(POPULATION_SIZE-1);
		float min = rouletteWheel.get(0);
		float firstRand = random.nextFloat() * (max-min) + min;
		float secondRand = random.nextFloat() * (max-min) + min;
		int firstChromosomeIndex, secondChromosomeIndex;
		
		firstChromosomeIndex = -1;
		secondChromosomeIndex = -1;
		for(int i=0; i < POPULATION_SIZE; i++)
		{	
			if(firstRand < rouletteWheel.get(i))
			{
				firstChromosomeIndex = i-1;
				break;
			}
		}
		for(int i=0; i < POPULATION_SIZE; i++)
		{
			if(secondRand < rouletteWheel.get(i))
			{
				secondChromosomeIndex = i-1;
				break;
			}
		}
		Vector<float[]> selectedChromosomes = new Vector<float[]>();
		selectedChromosomes.addElement(population.get(firstChromosomeIndex));
		selectedChromosomes.addElement(population.get(secondChromosomeIndex));
		return selectedChromosomes;
	}
	
	public Vector<float[]> crossover(float[] firstChromosome, float[] secondChromosome)
	{
		float randomNumber = random.nextFloat();
		Vector<float[]> chromosomesAfterCrossover = new Vector<float[]>();
		if(randomNumber < 0.4 || randomNumber > 0.7)
		{
			chromosomesAfterCrossover.addElement(firstChromosome);
			chromosomesAfterCrossover.addElement(secondChromosome);
			return chromosomesAfterCrossover;
		}
		else
		{
			int pointOfMutation = random.nextInt(numberOfCoeffecients);
			float temp;
			for(int i=pointOfMutation; i < numberOfCoeffecients; i++)
			{
				temp = firstChromosome[i];
				firstChromosome[i] = secondChromosome[i];
				secondChromosome[i] = temp;
			}
			chromosomesAfterCrossover.addElement(firstChromosome);
			chromosomesAfterCrossover.addElement(secondChromosome);
		}
		return chromosomesAfterCrossover;
	}
	
	public float[] mutation(float[] chromosome, int currentGeneration, int numberOfGenerations)
	{
		float rightLeftDecision = random.nextFloat();
		float y;
		float delta;
		float randomNumber1, randomNumber2;
		if(rightLeftDecision <= 0.5)
		{
			for(int i=0; i < numberOfCoeffecients; i++)
			{
				y = chromosome[i] - MIN_RANGE;
				randomNumber1 = random.nextFloat();
				randomNumber2 = random.nextFloat() * 4.5f + 0.5f;
				delta = (float) (y * (1.0f - Math.pow(randomNumber1, Math.pow(1.0f - (float)currentGeneration/(float)numberOfGenerations, randomNumber2))));
				chromosome[i] -= delta;
			}
		}
		else
		{
			for(int i=0; i < numberOfCoeffecients; i++)
			{
				y = MAX_RANGE - chromosome[i];
				randomNumber1 = random.nextFloat();
				randomNumber2 = random.nextFloat() * 4.5f + 0.5f;
				delta = (float) (y * (1.0f - Math.pow(randomNumber1, Math.pow(1.0f - (float)currentGeneration/(float)numberOfGenerations, randomNumber2))));
				chromosome[i] += delta;
			}
		}
		return chromosome;
	}
	
	public void addToNewPopulation(float[] chromosome)
	{
		newPopulation.addElement(chromosome);
	}
	
	public void nextPopulation()
	{
		population = newPopulation;
		newPopulation = new Vector<>();
	}

	public float maxFitness()
	{
		float min = fitness.get(0);
		bestSolution = new float[numberOfCoeffecients];
		bestSolution = population.get(0);
		for(int i=1; i < POPULATION_SIZE; i++)
		{
			if(min > fitness.get(i))
			{
				min = fitness.get(i);
				bestSolution = population.get(i);
			}
		}
		return min;
	}
	
	public float[] getBestSolution()
	{
		return bestSolution;
	}

	private void createRoulleteWheel()
	{
		rouletteWheel = new Vector<>();
		for(int i=0; i < POPULATION_SIZE; i++)
		{
			if(i == 0)
				rouletteWheel.addElement(fitness.elementAt(i));
			else
				rouletteWheel.addElement(rouletteWheel.get(i-1) + fitness.get(i));
		}
	}
}
