package Assinment4;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Demo 
{

	public static void main(String[] args) throws IOException 
	{
		Scanner input = new Scanner(System.in);
		int choice;
		String fileName;
		BackPropagation bp = null;
		FeedForward ff = null;
		while(true)
		{
			System.out.println("1) Back Propagation");
			System.out.println("2) Feed Forward");
			System.out.println("-1 to terminate");
			System.out.print("Choice: ");
			choice = input.nextInt();
			if(choice == 1)
			{
				System.out.print("Enter the file name: ");
				fileName = input.next();
				System.out.println(fileName);
				bp = new BackPropagation(new FileReader(new File(fileName)));
				bp.run();
			}
			else if(choice == 2)
			{
				System.out.print("Enter the file name: ");
				fileName = input.next();
				ff = new FeedForward(new FileReader(new File(fileName)));
				ff.run();
			}
			else
				break;
			System.out.println();
		}
	}

}
