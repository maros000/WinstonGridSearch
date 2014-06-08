package sk.upjs.winston.gridsearch.algorithms;

import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by stefan on 6/8/14.
 */
public class KNN {

    public static final int ERROR_DURING_CLASSIFICATION = -1;
    private static final int MAX_K = 100;

    /*
     * Performes kNN algorithm and evaluates results 10 times with 10-fold cross validation method.
     * Returnes the mean squared error for given model.
     * @param dataInstances dataset instances
     * @param k k parameter for kNN algorithm
     * @return root mean squared error
     */
    public double knn(Instances dataInstances, int k){
        IBk ibk = new IBk(k);
        Evaluation evaluation = null;
        try {
            evaluation = new Evaluation(dataInstances);
            evaluation.crossValidateModel(ibk, dataInstances, 10, new Random(1));
        } catch (Exception e) {
//            e.printStackTrace();
            return ERROR_DURING_CLASSIFICATION;
        }
        System.out.println(evaluation.rootMeanSquaredError());
        return evaluation.rootMeanSquaredError();
    }

    /*
     * Performs knn for k=1..{@MAX_K} and returns RMSE for every value.
     * When something goes wrong during search, the result of this search is not included in result set.
     * @param dataInstances dataset instances
     * @return Set of KnnSearchResult instances
     */
    public Set<SearchResult> knnSearch(Instances dataInstances){
        Set<SearchResult> results = new HashSet<>();
        for (int k = 1; k < MAX_K; k++) {
            double rmse = knn(dataInstances,k);
            if(rmse != ERROR_DURING_CLASSIFICATION) {
                SearchResult res = new KnnSearchResult(dataInstances.relationName(), rmse, k);
                results.add(res);
            }
        }
        return results;
    }

}