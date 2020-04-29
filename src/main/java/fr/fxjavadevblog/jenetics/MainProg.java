package fr.fxjavadevblog.jenetics;

import java.util.stream.IntStream;

import io.jenetics.Chromosome;
import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.stat.DoubleMomentStatistics;
import io.jenetics.util.Factory;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class MainProg
{
    private static final int GENERATIONS = 50;
    private static final int POPULATION_SIZE = 80;
    
    private static final int PLANT_NUMBER = Plant.values().length;
    private static final int MAX_SIZE_FOR_ONE_PLANT = 50;
    private static final int MIN_SIZE_FOR_ONE_PLANT = 10;
    private static final int MAX_SIZE = 100;

    private static double getPlantEvalFromGeneIndice(Chromosome<IntegerGene> chromosome, int i)
    {    
        int geneValue = chromosome.get(i).intValue();
        return Plant.values()[i].calculerBenefice(geneValue);
    }

    private static Double evaluate(Genotype<IntegerGene> gt)
    {
        Chromosome<IntegerGene> chromosome = gt.chromosome();
        // calcul du bénéfice de cette solution
        double benefice =  IntStream.range(0, chromosome.length())
                                    .mapToDouble(i -> getPlantEvalFromGeneIndice(chromosome, i))
                                    .sum();
        // calcul de la taille totale de cette solution
        int tailleTotale = chromosome.stream()
                                     .mapToInt(IntegerGene::intValue)
                                     .sum();   
        log.info("Eval de {} : tailleTotale {} m², prix {} €", chromosome, tailleTotale, String.format("%.2f",benefice));
        return (tailleTotale > MAX_SIZE) ? 0 : benefice;
    }

    public static void main(String[] args)
    {
        // inits
        Factory<Genotype<IntegerGene>> gtf = Genotype.of(IntegerChromosome.of(MIN_SIZE_FOR_ONE_PLANT, MAX_SIZE_FOR_ONE_PLANT, PLANT_NUMBER));
        Engine<IntegerGene, Double> engine = Engine.builder(MainProg::evaluate, gtf)
                                                   .populationSize(POPULATION_SIZE)
                                                   .build();
        EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();
        
        // run!
        Phenotype<IntegerGene, Double> result = engine.stream()
                                                      .limit(GENERATIONS)
                                                      .peek(statistics)
                                                      .collect(EvolutionResult.toBestPhenotype());

        // stats & results
        System.out.println(statistics);
        System.out.println(result);
    }

}
