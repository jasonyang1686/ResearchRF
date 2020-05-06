package libSVM;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

public class LibLinear{
    public static void main(String[] args) throws Exception {
        //loading train data
        Feature[][] featureMatrix = new Feature[5][];
        Feature[] featureMatrix1 = { new FeatureNode(2, 0.1), new FeatureNode(3, 0.2) };
        Feature[] featureMatrix2 = { new FeatureNode(2, 0.1), new FeatureNode(3, 0.3), new FeatureNode(4, -1.2)};
        Feature[] featureMatrix3 = { new FeatureNode(1, 0.4) };
        Feature[] featureMatrix4 = { new FeatureNode(2, 0.1), new FeatureNode(4, 1.4), new FeatureNode(5, 0.5) };
        Feature[] featureMatrix5 = { new FeatureNode(1, -0.1), new FeatureNode(2, -0.2), new FeatureNode(3, 0.1), new FeatureNode(4, -1.1), new FeatureNode(5, 0.1) };
        featureMatrix[0] = featureMatrix1;
        featureMatrix[1] = featureMatrix2;
        featureMatrix[2] = featureMatrix3;
        featureMatrix[3] = featureMatrix4;
        featureMatrix[4] = featureMatrix5;
        //loading target value
        double[] targetValue = {1,-1,1,-1,0};
        
        Problem problem = new Problem();
        problem.l = 5; // number of training examples
        problem.n = 5; // number of features
        problem.x = featureMatrix; // feature nodes
        problem.y = targetValue; // target values

        SolverType solver = SolverType.L2R_LR; // -s 0
        double C = 1.0;    // cost of constraints violation
        double eps = 0.01; // stopping criteria
            
        Parameter parameter = new Parameter(solver, C, eps);
        Model model = Linear.train(problem, parameter);
        File modelFile = new File("model");
        model.save(modelFile);
        // load model or use it directly
        model = Model.load(modelFile);

        Feature[] testNode = { new FeatureNode(1, 0.4), new FeatureNode(3, 0.3) };//test node
        double prediction = Linear.predict(model, testNode);
        System.out.print("classification result: "+prediction);
    }
}
