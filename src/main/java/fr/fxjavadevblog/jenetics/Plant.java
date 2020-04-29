package fr.fxjavadevblog.jenetics;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Plant
{  
    TOMATE(3.5, 1.5, 5, 2.8), 
    POMME_DE_TERRE(1.2,1,7,1.8), 
    FRAISE(6,1.4,1.3,9), 
    RADIS(0.5,2.0,2.2,1.15),
    CAROTTE(0.75,0.7,3,1.25);

    private final double cout;      // prix des graines par m²     
    private final double arrosage;  // prix de l'arrosage par m²   
    private final double rendement; // nb kg par m²   
    private final double prix;      // prix de revente.
    
    public double calculerBenefice(int taille)
    {
        double totalCout = (cout + arrosage) * taille;
        double totalRevente = rendement * prix * taille;               
        return totalRevente - totalCout;     
    }

}
