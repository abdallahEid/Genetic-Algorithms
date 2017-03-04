import java.util.ArrayList;

public class Operation {
	
	void fuzzification(ArrayList<Variable> variables){
		boolean intersection = false ;
		for(int i=0 ; i < variables.size() ; i++){
			System.out.println(variables.get(i).name + " = " + variables.get(i).crisp);
			for(int j=0 ; j < variables.get(i).sets.size() ; j++){
				for(int k=0 ; k < variables.get(i).sets.get(j).x_values.size()-1 ; k++){
					if(variables.get(i).crisp >= variables.get(i).sets.get(j).x_values.get(k) 
					&& variables.get(i).crisp <= variables.get(i).sets.get(j).x_values.get(k+1)){
						intersection = true ;
						if(variables.get(i).sets.get(j).type.equals("triangle")){
							if(k == 0){
								float y = 1 - ((1 - 0) * (float)(variables.get(i).sets.get(j).x_values.get(k+1) - variables.get(i).crisp) / (float)(variables.get(i).sets.get(j).x_values.get(k+1) - variables.get(i).sets.get(j).x_values.get(k))) ;
								variables.get(i).yOfCrisp.add(y) ;
							}
							else{
								float y = (float)(variables.get(i).sets.get(j).x_values.get(k+1) - variables.get(i).crisp) / (float)(variables.get(i).sets.get(j).x_values.get(k+1) - variables.get(i).sets.get(j).x_values.get(k));
								variables.get(i).yOfCrisp.add(y) ;
							}
						}
						else{
							if(k == 0){
								float y = 1 - (float)((1 - 0) * (variables.get(i).sets.get(j).x_values.get(k+1) - variables.get(i).crisp) / (float)(variables.get(i).sets.get(j).x_values.get(k+1) - variables.get(i).sets.get(j).x_values.get(k))) ;
								variables.get(i).yOfCrisp.add(y) ;
							}
							else if(k == 1){
								variables.get(i).yOfCrisp.add((float) 1) ;
							}
							else{
								float y = (float)(variables.get(i).sets.get(j).x_values.get(k+1) - variables.get(i).crisp) / (float)(variables.get(i).sets.get(j).x_values.get(k+1) - variables.get(i).sets.get(j).x_values.get(k));
								variables.get(i).yOfCrisp.add(y) ;
							}
						}
					}
				}
				if(intersection == false)
					variables.get(i).yOfCrisp.add((float) 0) ;
				intersection = false ;
				System.out.println("M" + variables.get(i).sets.get(j).name + "(" + variables.get(i).name + " = " + variables.get(i).crisp + ") = " + variables.get(i).yOfCrisp.get(variables.get(i).yOfCrisp.size()-1));
			}
			System.out.println("-------------------------------------------------");
		}
	}
	
	void inferenceOfRules(){
		
	}
}
