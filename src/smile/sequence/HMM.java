/*******************************************************************************
 * Copyright (c) 2010 Haifeng Li
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package smile.sequence;

import java.util.HashMap;
import java.util.Map;
import smile.math.Math;

/**
 * First-order Hidden Markov Model. A hidden Markov model (HMM) is a statistical
 * Markov model in which the system being modeled is assumed to be a Markov
 * process with unobserved (hidden) states. An HMM can be considered as the
 * simplest dynamic Bayesian network. <p> In a regular Markov model, the state
 * is directly visible to the observer, and therefore the state transition
 * probabilities are the only parameters. In a hidden Markov model, the state is
 * not directly visible, but output, dependent on the state, is visible. Each
 * state has a probability distribution over the possible output tokens.
 * Therefore the sequence of tokens generated by an HMM gives some information
 * about the sequence of states.
 *
 * @author Haifeng Li
 */
public class HMM<O> implements SequenceLabeler<O> {

    /**
     * The number of states.
     */
    private int numStates;
    /**
     * The number of emission symbols.
     */
    private int numSymbols;
    /**
     * Initial state probabilities.
     */
    private double[] pi;
    /**
     * State transition probabilities.
     */
    private double[][] a;
    /**
     * Symbol emission probabilities.
     */
    private double[][] b;
    /**
     * The mapping from emission symbols to indices.
     */
    private Map<O, Integer> symbols;

    /**
     * Constructor.
     *
     * @param numStates the number of states.
     * @param numSymbols the number of emission symbols.
     */
    private HMM(int numStates, int numSymbols) {
        if (numStates <= 0) {
            throw new IllegalArgumentException("Invalid number of states: " + numStates);
        }

        if (numSymbols <= 0) {
            throw new IllegalArgumentException("Invalid number of emission symbols: " + numSymbols);
        }

        this.numStates = numStates;
        this.numSymbols = numSymbols;
        pi = new double[numStates];
        a = new double[numStates][numStates];
        b = new double[numStates][numSymbols];
    }

    /**
     * Constructor.
     *
     * @param pi the initial state probabilities.
     * @param a the state transition probabilities, of which a[i][j] is P(s_j |
     * s_i);
     * @param b the symbol emission probabilities, of which b[i][j] is P(o_j |
     * s_i).
     */
    public HMM(double[] pi, double[][] a, double[][] b) {
        this(pi, a, b, null);
    }

    /**
     * Constructor.
     *
     * @param pi the initial state probabilities.
     * @param a the state transition probabilities, of which a[i][j] is P(s_j |
     * s_i);
     * @param b the symbol emission probabilities, of which b[i][j] is P(o_j |
     * s_i).
     * @param symbols the list of emission symbols.
     */
    public HMM(double[] pi, double[][] a, double[][] b, O[] symbols) {
        if (pi.length == 0) {
            throw new IllegalArgumentException("Invalid initial state probabilities.");
        }

        if (pi.length != a.length) {
            throw new IllegalArgumentException("Invalid state transition probability matrix.");
        }

        if (a.length != b.length) {
            throw new IllegalArgumentException("Invalid symbol emission probability matrix.");
        }

        if (symbols != null) {
            if (b[0].length != symbols.length) {
                throw new IllegalArgumentException("Invalid size of emission symbol list.");
            }

            this.symbols = new HashMap<O, Integer>();
            for (int i = 0; i < symbols.length; i++) {
                this.symbols.put(symbols[i], i);
            }
        }

        numStates = pi.length;
        numSymbols = b[0].length;

        for (int i = 0; i < numStates; i++) {
            if (a[i].length != numStates) {
                throw new IllegalArgumentException("Invalid state transition probability matrix.");
            }

            double sum = 0.0;

            for (int j = 0; j < numStates; j++) {
                if (a[i][j] < 0 || a[i][j] > 1.0) {
                    throw new IllegalArgumentException("Invalid state transition probability: " + a[i][j]);
                }
                sum += a[i][j];
            }

            if (Math.abs(1.0 - sum) > 1E-7) {
                throw new IllegalArgumentException(String.format("The row %d of state transition probability matrix doesn't sum to 1.", i));
            }
        }

        for (int i = 0; i < numStates; i++) {
            if (b[i].length != numSymbols) {
                throw new IllegalArgumentException("Invalid symbol emission probability matrix.");
            }

            double sum = 0.0;

            for (int j = 0; j < numSymbols; j++) {
                if (b[i][j] < 0 || b[i][j] > 1.0) {
                    throw new IllegalArgumentException("Invalid symbol emission probability: " + b[i][j]);
                }
                sum += b[i][j];
            }

            if (Math.abs(1.0 - sum) > 1E-7) {
                throw new IllegalArgumentException(String.format("The row %d of symbol emission probability matrix doesn't sum to 1.", i));
            }
        }

        this.pi = pi;
        this.a = a;
        this.b = b;
    }

    /**
     * Returns the number of states.
     */
    public int numStates() {
        return numStates;
    }

    /**
     * Returns the number of emission symbols.
     */
    public int numSymbols() {
        return numSymbols;
    }

    /**
     * Returns the initial state probabilities.
     */
    public double[] getInitialStateProbabilities() {
        return pi;
    }

    /**
     * Returns the state transition probabilities.
     */
    public double[][] getStateTransitionProbabilities() {
        return a;
    }

    /**
     * Returns the symbol emission probabilities.
     */
    public double[][] getSymbolEmissionProbabilities() {
        return b;
    }

    /**
     * Returns natural log without underflow.
     */
    private static double log(double x) {
        double y;
        if (x < 1E-300) {
            y = -690.7755;
        } else {
            y = java.lang.Math.log(x);
        }
        return y;
    }

    /**
     * Returns the joint probability of an observation sequence along a state
     * sequence given this HMM.
     *
     * @param o an observation sequence.
     * @param s a state sequence.
     * @return the joint probability P(o, s | H) given the model H.
     */
    public double p(int[] o, int[] s) {
        return Math.exp(logp(o, s));
    }

    /**
     * Returns the log joint probability of an observation sequence along a
     * state sequence given this HMM.
     *
     * @param o an observation sequence.
     * @param s a state sequence.
     * @return the log joint probability P(o, s | H) given the model H.
     */
    public double logp(int[] o, int[] s) {
        if (o.length != s.length) {
            throw new IllegalArgumentException("The observation sequence and state sequence are not the same length.");
        }

        int n = s.length;
        double p = log(pi[s[0]]) + log(b[s[0]][o[0]]);
        for (int i = 1; i < n; i++) {
            p += log(a[s[i - 1]][s[i]]) + log(b[s[i]][o[i]]);
        }

        return p;
    }

    /**
     * Returns the probability of an observation sequence given this HMM.
     *
     * @param o an observation sequence.
     * @return the probability of this sequence.
     */
    public double p(int[] o) {
        return Math.exp(logp(o));
    }

    /**
     * Returns the logarithm probability of an observation sequence given this
     * HMM. A scaling procedure is used in order to avoid underflows when
     * computing the probability of long sequences.
     *
     * @param o an observation sequence.
     * @return the log probability of this sequence.
     */
    public double logp(int[] o) {
        double[][] alpha = new double[o.length][numStates];
        double[] scaling = new double[o.length];

        forward(o, alpha, scaling);

        double p = 0.0;
        for (int t = 0; t < o.length; t++) {
            p += java.lang.Math.log(scaling[t]);
        }

        return p;
    }

    /**
     * Normalize alpha[t] and put the normalization factor in scaling[t].
     */
    private void scale(double[] scaling, double[][] alpha, int t) {
        double[] table = alpha[t];

        double sum = 0.0;
        for (int i = 0; i < table.length; i++) {
            sum += table[i];
        }

        scaling[t] = sum;
        for (int i = 0; i < table.length; i++) {
            table[i] /= sum;
        }
    }

    /**
     * Scaled forward procedure without underflow.
     *
     * @param o an observation sequence.
     * @param alpha on output, alpha(i, j) holds the scaled total probability of
     * ending up in state i at time j.
     * @param scaling on output, it holds scaling factors.
     */
    private void forward(int[] o, double[][] alpha, double[] scaling) {
        for (int k = 0; k < numStates; k++) {
            alpha[0][k] = pi[k] * b[k][o[0]];
        }
        scale(scaling, alpha, 0);

        for (int t = 1; t < o.length; t++) {
            for (int k = 0; k < numStates; k++) {
                double sum = 0.0;

                for (int i = 0; i < numStates; i++) {
                    sum += alpha[t - 1][i] * a[i][k];
                }

                alpha[t][k] = sum * b[k][o[t]];
            }
            scale(scaling, alpha, t);
        }
    }

    /**
     * Scaled backward procedure without underflow.
     *
     * @param o an observation sequence.
     * @param beta on output, beta(i, j) holds the scaled total probability of
     * starting up in state i at time j.
     * @param scaling on input, it should hold scaling factors computed by
     * forward procedure.
     */
    private void backward(int[] o, double[][] beta, double[] scaling) {
        int n = o.length - 1;
        for (int i = 0; i < numStates; i++) {
            beta[n][i] = 1.0 / scaling[n];
        }

        for (int t = n; t-- > 0;) {
            for (int i = 0; i < numStates; i++) {
                double sum = 0.;

                for (int j = 0; j < numStates(); j++) {
                    sum += beta[t + 1][j] * a[i][j] * b[j][o[t + 1]];
                }

                beta[t][i] = sum / scaling[t];
            }
        }
    }

    /**
     * Returns the most likely state sequence given the observation sequence by
     * the Viterbi algorithm, which maximizes the probability of
     * <code>P(I | O, HMM)</code>. In the calculation, we may get ties. In this
     * case, one of them is chosen randomly.
     *
     * @param o an observation sequence.
     * @return the most likely state sequence.
     */
    public int[] predict(int[] o) {
        // The porbability of the most probable path.
        double[][] trellis = new double[o.length][numStates];
        // Backtrace.
        int[][] psy = new int[o.length][numStates];
        // The most likely state sequence.
        int[] s = new int[o.length];

        // forward
        for (int i = 0; i < numStates; i++) {
            trellis[0][i] = log(pi[i]) + log(b[i][o[0]]);
            psy[0][i] = 0;
        }

        for (int t = 1; t < o.length; t++) {
            for (int j = 0; j < numStates; j++) {
                double maxDelta = Double.NEGATIVE_INFINITY;
                int maxPsy = 0;

                for (int i = 0; i < numStates; i++) {
                    double delta = trellis[t - 1][i] + log(a[i][j]);

                    if (maxDelta < delta) {
                        maxDelta = delta;
                        maxPsy = i;
                    }
                }

                trellis[t][j] = maxDelta + log(b[j][o[t]]);
                psy[t][j] = maxPsy;
            }
        }

        // trace back
        int n = o.length - 1;
        double maxDelta = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < numStates; i++) {
            if (maxDelta < trellis[n][i]) {
                maxDelta = trellis[n][i];
                s[n] = i;
            }
        }

        for (int t = n; t-- > 0;) {
            s[t] = psy[t + 1][s[t + 1]];
        }

        return s;
    }

    /**
     * Learn an HMM from labeled observation sequences by maximum likelihood
     * estimation.
     *
     * @param observations the observation sequences, of which symbols take
     * values in [0, n), where n is the number of unique symbols.
     * @param labels the state labels of observations, of which states take
     * values in [0, p), where p is the number of hidden states.
     */
    public HMM(int[][] observations, int[][] labels) {
        if (observations.length != labels.length) {
            throw new IllegalArgumentException("The number of observation sequences and that of label sequences are different.");
        }

        numStates = 0;
        numSymbols = 0;

        for (int i = 0; i < observations.length; i++) {
            if (observations[i].length != labels[i].length) {
                throw new IllegalArgumentException(String.format("The length of observation sequence %d and that of corresponding label sequence are different.", i));
            }

            numStates = Math.max(numStates, Math.max(labels[i]) + 1);
            numSymbols = Math.max(numSymbols, Math.max(observations[i]) + 1);
        }

        pi = new double[numStates];
        a = new double[numStates][numStates];
        b = new double[numStates][numSymbols];

        for (int i = 0; i < observations.length; i++) {
            pi[labels[i][0]]++;
            b[labels[i][0]][observations[i][0]]++;
            for (int j = 1; j < observations[i].length; j++) {
                a[labels[i][j - 1]][labels[i][j]]++;
                b[labels[i][j]][observations[i][j]]++;
            }
        }

        Math.unitize1(pi);
        for (int i = 0; i < numStates; i++) {
            Math.unitize1(a[i]);
            Math.unitize1(b[i]);
        }
    }

    /**
     * Learn an HMM from labeled observation sequences by maximum likelihood
     * estimation.
     *
     * @param observations the observation sequences.
     * @param labels the state labels of observations, of which states take
     * values in [0, p), where p is the number of hidden states.
     */
    public HMM(O[][] observations, int[][] labels) {
        if (observations.length != labels.length) {
            throw new IllegalArgumentException("The number of observation sequences and that of label sequences are different.");
        }

        int index = 0;
        symbols = new HashMap<O, Integer>();
        for (int i = 0; i < observations.length; i++) {
            if (observations[i].length != labels[i].length) {
                throw new IllegalArgumentException(String.format("The length of observation sequence %d and that of corresponding label sequence are different.", i));
            }

            for (int j = 0; j < observations[i].length; j++) {
                Integer sym = symbols.get(observations[i][j]);
                if (sym == null) {
                    symbols.put(observations[i][j], index++);
                }
            }
        }

        int[][] obs = new int[observations.length][];
        for (int i = 0; i < obs.length; i++) {
            obs[i] = translate(symbols, observations[i]);
        }

        numStates = 0;
        numSymbols = 0;

        for (int i = 0; i < obs.length; i++) {
            numStates = Math.max(numStates, Math.max(labels[i]) + 1);
            numSymbols = Math.max(numSymbols, Math.max(obs[i]) + 1);
        }

        pi = new double[numStates];
        a = new double[numStates][numStates];
        b = new double[numStates][numSymbols];

        for (int i = 0; i < obs.length; i++) {
            pi[labels[i][0]]++;
            b[labels[i][0]][obs[i][0]]++;
            for (int j = 1; j < obs[i].length; j++) {
                a[labels[i][j - 1]][labels[i][j]]++;
                b[labels[i][j]][obs[i][j]]++;
            }
        }

        Math.unitize1(pi);
        for (int i = 0; i < numStates; i++) {
            Math.unitize1(a[i]);
            Math.unitize1(b[i]);
        }
    }

    /**
     * With this HMM as the initial model, learn an HMM by the Baum-Welch
     * algorithm.
     *
     * @param observations the observation sequences on which the learning is
     * based. Each sequence must have a length higher or equal to 2.
     * @param iterations the number of iterations to execute.
     * @return the updated HMM.
     */
    public HMM<O> learn(O[][] observations, int iterations) {
        int[][] obs = new int[observations.length][];
        for (int i = 0; i < obs.length; i++) {
            obs[i] = translate(observations[i]);
        }

        return learn(obs, iterations);
    }

    /**
     * With this HMM as the initial model, learn an HMM by the Baum-Welch
     * algorithm.
     *
     * @param observations the observation sequences on which the learning is
     * based. Each sequence must have a length higher or equal to 2.
     * @param iterations the number of iterations to execute.
     * @return the updated HMM.
     */
    public HMM<O> learn(int[][] observations, int iterations) {
        HMM<O> hmm = this;

        for (int iter = 0; iter < iterations; iter++) {
            hmm = hmm.iterate(observations);
        }

        return hmm;
    }

    /**
     * Performs one iteration of the Baum-Welch algorithm.
     *
     * @param sequences the training observation sequences.
     *
     * @return an updated HMM.
     */
    private HMM<O> iterate(int[][] sequences) {
        HMM<O> hmm = new HMM<O>(numStates, numSymbols);
        hmm.symbols = symbols;

        // gamma[n] = gamma array associated to observation sequence n
        double gamma[][][] = new double[sequences.length][][];

        // a[i][j] = aijNum[i][j] / aijDen[i]
        // aijDen[i] = expected number of transitions from state i
        // aijNum[i][j] = expected number of transitions from state i to j
        double aijNum[][] = new double[numStates][numStates];
        double aijDen[] = new double[numStates];

        for (int k = 0; k < sequences.length; k++) {
            if (sequences[k].length <= 2) {
                throw new IllegalArgumentException(String.format("Traning sequence %d is too short.", k));
            }

            int[] o = sequences[k];
            double[][] alpha = new double[o.length][numStates];
            double[][] beta = new double[o.length][numStates];
            double[] scaling = new double[o.length];
            forward(o, alpha, scaling);
            backward(o, beta, scaling);

            double xi[][][] = estimateXi(o, alpha, beta);
            double g[][] = gamma[k] = estimateGamma(xi);

            int n = o.length - 1;
            for (int i = 0; i < numStates; i++) {
                for (int t = 0; t < n; t++) {
                    aijDen[i] += g[t][i];

                    for (int j = 0; j < numStates; j++) {
                        aijNum[i][j] += xi[t][i][j];
                    }
                }
            }
        }

        for (int i = 0; i < numStates; i++) {
            if (aijDen[i] == 0.0) // State i is not reachable
            {
                System.arraycopy(a[i], 0, hmm.a[i], 0, numStates);
            } else {
                for (int j = 0; j < numStates; j++) {
                    hmm.a[i][j] = aijNum[i][j] / aijDen[i];
                }
            }
        }

        /*
         * initial state probability computation
         */
        for (int j = 0; j < sequences.length; j++) {
            for (int i = 0; i < numStates; i++) {
                hmm.pi[i] += gamma[j][0][i];
            }
        }

        for (int i = 0; i < numStates; i++) {
            hmm.pi[i] /= sequences.length;
        }

        /*
         * emission probability computation
         */
        for (int i = 0; i < numStates; i++) {
            double sum = 0.0;

            for (int j = 0; j < sequences.length; j++) {
                int[] o = sequences[j];
                for (int t = 0; t < o.length; t++) {
                    hmm.b[i][o[t]] += gamma[j][t][i];
                    sum += gamma[j][t][i];
                }
            }

            for (int j = 0; j < numSymbols; j++) {
                hmm.b[i][j] /= sum;
            }
        }

        return hmm;
    }

    /**
     * Here, the xi (and, thus, gamma) values are not divided by the probability
     * of the sequence because this probability might be too small and induce an
     * underflow. xi[t][i][j] still can be interpreted as P(q_t = i and q_(t+1)
     * = j | O, HMM) because we assume that the scaling factors are such that
     * their product is equal to the inverse of the probability of the sequence.
     */
    private double[][][] estimateXi(int[] o, double[][] alpha, double[][] beta) {
        if (o.length <= 1) {
            throw new IllegalArgumentException("Observation sequence is too short.");
        }

        int n = o.length - 1;
        double xi[][][] = new double[n][numStates][numStates];

        for (int t = 0; t < n; t++) {
            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numStates; j++) {
                    xi[t][i][j] = alpha[t][i] * a[i][j] * b[j][o[t + 1]] * beta[t + 1][j];
                }
            }
        }

        return xi;
    }

    /**
     * gamma[][] could be computed directly using the alpha and beta arrays, but
     * this (slower) method is preferred because it doesn't change if the xi
     * array has been scaled (and should be changed with the scaled alpha and
     * beta arrays).
     */
    private double[][] estimateGamma(double[][][] xi) {
        double[][] gamma = new double[xi.length + 1][numStates];

        for (int t = 0; t < xi.length; t++) {
            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numStates; j++) {
                    gamma[t][i] += xi[t][i][j];
                }
            }
        }

        int n = xi.length - 1;
        for (int j = 0; j < numStates; j++) {
            for (int i = 0; i < numStates; i++) {
                gamma[xi.length][j] += xi[n][i][j];
            }
        }

        return gamma;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("HMM (%d states, %d emission symbols)\n", numStates, numSymbols));

        sb.append("\tInitial state probability:\n\t\t");
        for (int i = 0; i < numStates; i++) {
            sb.append(String.format("%.4g ", pi[i]));
        }

        sb.append("\n\tState transition probability:");
        for (int i = 0; i < numStates; i++) {
            sb.append("\n\t\t");
            for (int j = 0; j < numStates; j++) {
                sb.append(String.format("%.4g ", a[i][j]));
            }
        }

        sb.append("\n\tSymbol emission probability:");
        for (int i = 0; i < numStates; i++) {
            sb.append("\n\t\t");
            for (int j = 0; j < numSymbols; j++) {
                sb.append(String.format("%.4g ", b[i][j]));
            }
        }

        return sb.toString();
    }

    /**
     * Translate an observation sequence to internal representation.
     */
    private int[] translate(O[] o) {
        return translate(symbols, o);
    }

    /**
     * Translate an observation sequence to internal representation.
     */
    private static <O> int[] translate(Map<O, Integer> symbols, O[] o) {
        if (symbols == null) {
            throw new IllegalArgumentException("No availabe emission symbol list.");
        }

        int[] seq = new int[o.length];
        for (int i = 0; i < o.length; i++) {
            Integer sym = symbols.get(o[i]);
            if (sym != null) {
                seq[i] = sym;
            } else {
                throw new IllegalArgumentException("Invalid observation symbol: " + o[i]);
            }
        }

        return seq;
    }

    /**
     * Returns the joint probability of an observation sequence along a state
     * sequence given this HMM.
     *
     * @param o an observation sequence.
     * @param s a state sequence.
     * @return the joint probability P(o, s | H) given the model H.
     */
    public double p(O[] o, int[] s) {
        return Math.exp(logp(o, s));
    }

    /**
     * Returns the log joint probability of an observation sequence along a
     * state sequence given this HMM.
     *
     * @param o an observation sequence.
     * @param s a state sequence.
     * @return the log joint probability P(o, s | H) given the model H.
     */
    public double logp(O[] o, int[] s) {
        return logp(translate(o), s);
    }

    /**
     * Returns the probability of an observation sequence given this HMM.
     *
     * @param o an observation sequence.
     * @return the probability of this sequence.
     */
    public double p(O[] o) {
        return Math.exp(logp(o));
    }

    /**
     * Returns the logarithm probability of an observation sequence given this
     * HMM. A scaling procedure is used in order to avoid underflows when
     * computing the probability of long sequences.
     *
     * @param o an observation sequence.
     * @return the log probability of this sequence.
     */
    public double logp(O[] o) {
        return logp(translate(o));
    }

    /**
     * Returns the most likely state sequence given the observation sequence by
     * the Viterbi algorithm, which maximizes the probability of
     * <code>P(I | O, HMM)</code>. In the calculation, we may get ties. In this
     * case, one of them is chosen randomly.
     *
     * @param o an observation sequence.
     * @return the most likely state sequence.
     */
    @Override
    public int[] predict(O[] o) {
        return predict(translate(o));
    }
}
