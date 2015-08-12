/*
 * Copyright 1999-2002 Carnegie Mellon University.  
 * Portions Copyright 2002 Sun Microsystems, Inc.  
 * Portions Copyright 2002 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 * 
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL 
 * WARRANTIES.
 *
 */

package edu.cmu.sphinx.decoder.search;


import com.gs.collections.api.block.function.Function0;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import edu.cmu.sphinx.decoder.scorer.Scoreable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manager for pruned hypothesis
 * 
 * @author Joe Woelfel
 */
public class AlternateHypothesisManager {

    private final UnifiedMap<Token,List<Token>> viterbiLoserMap = new UnifiedMap();
    private final int maxEdges;


    /**
     * Creates an alternate hypotheses manager
     *
     * @param maxEdges the maximum edges allowed
     */
    public AlternateHypothesisManager(int maxEdges) {
        this.maxEdges = maxEdges;
    }


    /**
     * Collects adds alternate predecessors for a token that would have lost because of viterbi.
     *
     * @param token       - a token that has an alternate lower scoring predecessor that still might be of interest
     * @param predecessor - a predecessor that scores lower than token.getPredecessor().
     */

    public void addAlternatePredecessor(Token token, Token predecessor) {
        assert predecessor != token.getPredecessor();
        List<Token> list = viterbiLoserMap.getIfAbsentPut(token, new Function0<List<Token>>() {

            @Override
            public List<Token> value() {
                return new ArrayList<Token>();
            }
        });
//        if (list == null) {
//            list = new ArrayList<Token>();
//            viterbiLoserMap.put(token, list);
//        }
        list.add(predecessor);
    }


    /**
     * Returns a list of alternate predecessors for a token.
     *
     * @param token - a token that may have alternate lower scoring predecessor that still might be of interest
     * @return A list of predecessors that scores lower than token.getPredecessor().
     */
    final public List<Token> getAlternatePredecessors(final Token token) {
        return viterbiLoserMap.get(token);
    }


    /** Purge all but max number of alternate preceding token hypotheses. */
    public void purge() {

        int max = maxEdges - 1;

        viterbiLoserMap.forEachKeyValue( (k,list) -> {
            Collections.sort(list, Scoreable.COMPARATOR);
            List<Token> newList = list.subList(0, list.size() > max ? max : list.size());
            viterbiLoserMap.put(k, newList);
        });

    }

	public boolean hasAlternatePredecessors(Token token) {
		return viterbiLoserMap.containsKey(token);
	}
}

