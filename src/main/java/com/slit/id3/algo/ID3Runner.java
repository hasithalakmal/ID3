package com.slit.id3.algo;

import com.slit.id3.bean.CalculationLevel;
import com.slit.id3.bean.ClassificationEntropy;
import com.slit.id3.bean.CriteriaEnum;
import com.slit.id3.bean.DataSet;
import com.slit.id3.bean.Entropy;
import com.slit.id3.bean.InformationGain;
import com.slit.id3.bean.Node;
import com.slit.id3.bean.Recode;
import com.slit.id3.bean.Tree;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ID3Runner {

    static int nodeId = 1;
    static int round = 0;
    static DataSet trainingDataSet;

    public static void main(String[] args) {
        //read initial data set
        trainingDataSet = getTrainingDataset();

        Tree tree = new Tree();

        List<String> criteriaList = new ArrayList<String>();
        Map<Integer, String> searchSteps = new HashMap<Integer, String>();
        List<Node> listOfNoneLeafNodes = new ArrayList<Node>();
        List<Node> listOfNoneLeafNodesNextLevel = new ArrayList<Node>();

        ClassificationEntropy classificationEntropy = calculateClassificationEntropy(trainingDataSet);

        trainingDataSet = RecursiveMethod(tree, criteriaList, searchSteps, classificationEntropy, listOfNoneLeafNodes, listOfNoneLeafNodesNextLevel);

        Node rootNode = tree.getNodeByID(1);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(jsonInString);
        System.out.printf(">>>>>>>>>>>>>>>>>>>>>>>>>>>");

    }

    private static DataSet RecursiveMethod(Tree tree, List<String> criteriaList, Map<Integer, String> searchSteps, ClassificationEntropy classificationEntropy, List<Node> listOfNoneLeafNodes, List<Node> listOfNoneLeafNodesNextLevel) {
        int upperNodeId = 0;
        Map<String, InformationGain> mapOfInformationGain = getMapOfInformationGain(trainingDataSet, classificationEntropy);

        CalculationLevel calculationLevel = new CalculationLevel();
        calculationLevel.setLevelNumber(round);
        calculationLevel.setListOfInformationGain(mapOfInformationGain);
        calculationLevel.setClassificationEntropy(classificationEntropy);
        calculationLevel.setDataSet(trainingDataSet);

        String keyOfHighestInformationGain = null;
        double highestInformationGainValue = 0.00;
        for (InformationGain informationGain : mapOfInformationGain.values()) {
            if (keyOfHighestInformationGain == null) {
                highestInformationGainValue = informationGain.getInformationGain();
                keyOfHighestInformationGain = informationGain.getCriteria();
            } else if (highestInformationGainValue < informationGain.getInformationGain()) {
                highestInformationGainValue = informationGain.getInformationGain();
                keyOfHighestInformationGain = informationGain.getCriteria();
            }
            criteriaList.add(informationGain.getCriteria());
        }
        //on this pint we have selected node
        criteriaList.remove(keyOfHighestInformationGain);
        searchSteps.put(round, keyOfHighestInformationGain);

        //run only for create root node
        if (tree.getListOfNodes().isEmpty()) {
            Node rootNode = new Node();
            rootNode.setNodeId(nodeId);
            rootNode.setNodeName(keyOfHighestInformationGain);
            rootNode.setUpperNodeId(0);
            rootNode.setLeafNode(false);
            tree.addNode(rootNode);
            upperNodeId = nodeId;
            nodeId++;
            lowerNodeCreator(tree, upperNodeId, mapOfInformationGain, keyOfHighestInformationGain, listOfNoneLeafNodes, listOfNoneLeafNodesNextLevel);
            for (Node node : listOfNoneLeafNodesNextLevel) {
                listOfNoneLeafNodes.add(node);
            }
            listOfNoneLeafNodesNextLevel.clear();
        } else {
            if (listOfNoneLeafNodes.isEmpty() && !listOfNoneLeafNodesNextLevel.isEmpty()) {
                for (Node node : listOfNoneLeafNodesNextLevel) {
                    listOfNoneLeafNodes.add(node);
                }
                listOfNoneLeafNodesNextLevel.clear();
            } else {
                for (Node upperNode : listOfNoneLeafNodes) {
                    if (!upperNode.isChecked()) {
                        upperNode.setNodeName(keyOfHighestInformationGain);
                        upperNodeId = upperNode.getNodeId();
                        upperNode.setChecked(true);
                        lowerNodeCreator(tree, upperNodeId, mapOfInformationGain, keyOfHighestInformationGain, listOfNoneLeafNodes, listOfNoneLeafNodesNextLevel);
                    }
                }
                resetTrainingDataSetOnlyFeild(trainingDataSet, keyOfHighestInformationGain);
                listOfNoneLeafNodes.clear();
            }
        }

        if (!criteriaList.isEmpty() && !mapOfInformationGain.isEmpty()) {
            round++;
            RecursiveMethod(tree, criteriaList, searchSteps, classificationEntropy, listOfNoneLeafNodes, listOfNoneLeafNodesNextLevel);
        }
        return trainingDataSet;
    }

    private static DataSet lowerNodeCreator(Tree tree, int upperNodeId, Map<String, InformationGain> mapOfInformationGain, String keyOfHighestInformationGain, List<Node> listOfNoneLeafNodes, List<Node> listOfNoneLeafNodesNextLevel) {
        //create lower nodes
        Map<String, Node> lowerNodeMap = new HashMap<String, Node>();
        for (Entropy entropy : mapOfInformationGain.get(keyOfHighestInformationGain).getMapOfEntropy().values()) {
            Node node = new Node();
            node.setNodeId(nodeId);
            if (entropy.getNumberOfPositives() == 0) {
                node.setNodeName("Loss");
                node.setLeafNode(true);
                node.setUpperNodeId(upperNodeId);
                trainingDataSet = resetTrainingDataSet(trainingDataSet, entropy.getProperty(), keyOfHighestInformationGain, false);
            } else if (entropy.getNumberOfNegatives() == 0) {
                node.setNodeName("Profit");
                node.setLeafNode(true);
                node.setUpperNodeId(upperNodeId);
                trainingDataSet = resetTrainingDataSet(trainingDataSet, entropy.getProperty(), keyOfHighestInformationGain, true);
            } else {
                node.setNodeName("Undefine");
                node.setLeafNode(false);
                node.setUpperNodeId(upperNodeId);
                listOfNoneLeafNodesNextLevel.add(node);
            }
            tree.addNode(node);
            lowerNodeMap.put(entropy.getProperty(), node);
            nodeId = nodeId + 1;
        }
        if (tree.getNodeByID(upperNodeId) != null) {
            tree.getNodeByID(upperNodeId).setLowerNodeMap(lowerNodeMap);
        }
        return trainingDataSet;
    }

    private static DataSet resetTrainingDataSetOnlyFeild(DataSet trainingDataSet, String keyOfHighestInformationGain) {
        DataSet tempDataSet = new DataSet();
        List<Recode> tempRecodeSet = trainingDataSet.getListOfRecodes();
        tempDataSet.setListOfRecodes(tempRecodeSet);
        for (Recode recode : tempDataSet.getListOfRecodes()) {
            if (keyOfHighestInformationGain.equals(CriteriaEnum.DIRECTORS_FB_LIKES.toString())) {
                recode.setDirectorFacebookLikes(null);
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.ACTOR_ONE_NAME.toString())) {
                recode.setActorOneName(null);
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.ACTOR_TWO_NAME.toString())) {
                recode.setActorTwoName(null);
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.ACTOR_THREE_NAME.toString())) {
                recode.setActorThreeName(null);
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.COUNTRY.toString())) {
                recode.setCountry(null);
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.DURATION.toString())) {
                recode.setDuration(null);
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.LANGUAGE.toString())) {
                recode.setLanguage(null);
            } else {
                System.err.println("Wrong key");
            }
        }
        return tempDataSet;
    }

    private static DataSet resetTrainingDataSet(DataSet trainingDataSet, String property, String keyOfHighestInformationGain, boolean isProfitable) {
        DataSet tempDataSet = new DataSet();
        Map<Integer, Recode> recodeMap = new HashMap<Integer, Recode>();

        int i = 0;
        for (Recode recode : trainingDataSet.getListOfRecodes()) {
            if (keyOfHighestInformationGain.equals(CriteriaEnum.DIRECTORS_FB_LIKES.toString())) {
                if (recode.isProfitable() == isProfitable && recode.getDirectorFacebookLikes() == property) {
                } else {
                    recodeMap.put(i, recode);
                }
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.ACTOR_ONE_NAME.toString())) {
                if (recode.isProfitable() == isProfitable && recode.getActorOneName() == property) {
                } else {
                    recodeMap.put(i, recode);
                }
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.ACTOR_TWO_NAME.toString())) {
                if (recode.isProfitable() == isProfitable && recode.getActorTwoName() == property) {
                } else {
                    recodeMap.put(i, recode);
                }
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.ACTOR_THREE_NAME.toString())) {
                if (recode.isProfitable() == isProfitable && recode.getActorThreeName() == property) {
                } else {
                    recodeMap.put(i, recode);
                }
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.COUNTRY.toString())) {
                if (recode.isProfitable() == isProfitable && recode.getCountry() == property) {
                } else {
                    recodeMap.put(i, recode);
                }
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.DURATION.toString())) {
                if (recode.isProfitable() == isProfitable && recode.getDuration() == property) {
                } else {
                    recodeMap.put(i, recode);
                }
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.LANGUAGE.toString())) {
                if (recode.isProfitable() == isProfitable && recode.getLanguage() == property) {
                } else {
                    recodeMap.put(i, recode);
                }
            } else {
                System.err.println("Wrong key");
            }
            i++;
        }

        List<Recode> tempRecodeSet = new ArrayList<Recode>(recodeMap.values());
        tempDataSet.setListOfRecodes(tempRecodeSet);
        for (Recode recode : tempDataSet.getListOfRecodes()) {
            if (keyOfHighestInformationGain.equals(CriteriaEnum.DIRECTORS_FB_LIKES.toString())) {
                recode.setDirectorFacebookLikes(null);
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.ACTOR_ONE_NAME.toString())) {
                recode.setActorOneName(null);
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.ACTOR_TWO_NAME.toString())) {
                recode.setActorTwoName(null);
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.ACTOR_THREE_NAME.toString())) {
                recode.setActorThreeName(null);
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.COUNTRY.toString())) {
                recode.setCountry(null);
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.DURATION.toString())) {
                recode.setDuration(null);
            } else if (keyOfHighestInformationGain.equals(CriteriaEnum.LANGUAGE.toString())) {
                recode.setLanguage(null);
            } else {
                System.err.println("Wrong key");
            }
        }
        return tempDataSet;
    }

    private static Map<String, InformationGain> getMapOfInformationGain(DataSet trainingDataSet, ClassificationEntropy classificationEntropy) {
        Map<String, InformationGain> mapOfInformationGain = new HashMap<String, InformationGain>();
        for (CriteriaEnum criteriaEnum : CriteriaEnum.values()) {
            String criteria = criteriaEnum.toString();
            InformationGain informationGain;
            if (criteria.equals(CriteriaEnum.DIRECTORS_FB_LIKES.toString())) {
                informationGain = calculateInformationGainOnDirectorsFBLikes(trainingDataSet, classificationEntropy, criteria);
            } else if (criteria.equals(CriteriaEnum.ACTOR_ONE_NAME.toString())) {
                informationGain = calculateInformationGainOnActorOneName(trainingDataSet, classificationEntropy, criteria);
            } else if (criteria.equals(CriteriaEnum.ACTOR_TWO_NAME.toString())) {
                informationGain = calculateInformationGainOnActorTwoName(trainingDataSet, classificationEntropy, criteria);
            } else if (criteria.equals(CriteriaEnum.ACTOR_THREE_NAME.toString())) {
                informationGain = calculateInformationGainOnActorThreeName(trainingDataSet, classificationEntropy, criteria);
            } else if (criteria.equals(CriteriaEnum.COUNTRY.toString())) {
                informationGain = calculateInformationGainOnCountry(trainingDataSet, classificationEntropy, criteria);
            } else if (criteria.equals(CriteriaEnum.DURATION.toString())) {
                informationGain = calculateInformationGainOnDuration(trainingDataSet, classificationEntropy, criteria);
            } else if (criteria.equals(CriteriaEnum.LANGUAGE.toString())) {
                informationGain = calculateInformationGainOnLangauge(trainingDataSet, classificationEntropy, criteria);
            } else {
                informationGain = null;
            }

            if (informationGain != null && !informationGain.getMapOfEntropy().isEmpty()) {
                mapOfInformationGain.put(criteria, informationGain);
            }
        }
        return mapOfInformationGain;
    }

    private static InformationGain calculateInformationGainOnDirectorsFBLikes(DataSet trainingDataSet, ClassificationEntropy classificationEntropy, String criteria) {
        Map<String, Entropy> mapOfEntropy = new HashMap<String, Entropy>();
        for (Recode recode : trainingDataSet.getListOfRecodes()) {
            String property = recode.getDirectorFacebookLikes();
            if (StringUtils.isNotBlank(property)) {
                populateMapOfEntropy(mapOfEntropy, recode, property);
            }
        }
        return getInformationGain(classificationEntropy, criteria, mapOfEntropy);
    }

    private static InformationGain calculateInformationGainOnActorOneName(DataSet trainingDataSet, ClassificationEntropy classificationEntropy, String criteria) {
        Map<String, Entropy> mapOfEntropy = new HashMap<String, Entropy>();
        for (Recode recode : trainingDataSet.getListOfRecodes()) {
            String property = recode.getActorOneName();
            if (StringUtils.isNotBlank(property)) {
                populateMapOfEntropy(mapOfEntropy, recode, property);
            }
        }
        return getInformationGain(classificationEntropy, criteria, mapOfEntropy);
    }

    private static InformationGain calculateInformationGainOnActorTwoName(DataSet trainingDataSet, ClassificationEntropy classificationEntropy, String criteria) {
        Map<String, Entropy> mapOfEntropy = new HashMap<String, Entropy>();
        for (Recode recode : trainingDataSet.getListOfRecodes()) {
            String property = recode.getActorTwoName();
            if (StringUtils.isNotBlank(property)) {
                populateMapOfEntropy(mapOfEntropy, recode, property);
            }
        }
        return getInformationGain(classificationEntropy, criteria, mapOfEntropy);
    }

    private static InformationGain calculateInformationGainOnCountry(DataSet trainingDataSet, ClassificationEntropy classificationEntropy, String criteria) {
        Map<String, Entropy> mapOfEntropy = new HashMap<String, Entropy>();
        for (Recode recode : trainingDataSet.getListOfRecodes()) {
            String property = recode.getCountry();
            if (StringUtils.isNotBlank(property)) {
                populateMapOfEntropy(mapOfEntropy, recode, property);
            }
        }
        return getInformationGain(classificationEntropy, criteria, mapOfEntropy);
    }

    private static InformationGain calculateInformationGainOnActorThreeName(DataSet trainingDataSet, ClassificationEntropy classificationEntropy, String criteria) {
        Map<String, Entropy> mapOfEntropy = new HashMap<String, Entropy>();
        for (Recode recode : trainingDataSet.getListOfRecodes()) {
            String property = recode.getActorThreeName();
            if (StringUtils.isNotBlank(property)) {
                populateMapOfEntropy(mapOfEntropy, recode, property);
            }
        }
        return getInformationGain(classificationEntropy, criteria, mapOfEntropy);
    }

    private static InformationGain calculateInformationGainOnDuration(DataSet trainingDataSet, ClassificationEntropy classificationEntropy, String criteria) {
        Map<String, Entropy> mapOfEntropy = new HashMap<String, Entropy>();
        for (Recode recode : trainingDataSet.getListOfRecodes()) {
            String property = recode.getDuration();
            if (StringUtils.isNotBlank(property)) {
                populateMapOfEntropy(mapOfEntropy, recode, property);
            }
        }
        return getInformationGain(classificationEntropy, criteria, mapOfEntropy);
    }

    private static InformationGain calculateInformationGainOnLangauge(DataSet trainingDataSet, ClassificationEntropy classificationEntropy, String criteria) {
        Map<String, Entropy> mapOfEntropy = new HashMap<String, Entropy>();
        for (Recode recode : trainingDataSet.getListOfRecodes()) {
            String property = recode.getLanguage();
            if (StringUtils.isNotBlank(property)) {
                populateMapOfEntropy(mapOfEntropy, recode, property);
            }
        }
        return getInformationGain(classificationEntropy, criteria, mapOfEntropy);
    }


    private static void populateMapOfEntropy(Map<String, Entropy> mapOfEntropy, Recode recode, String property) {
        if (!mapOfEntropy.containsKey(property)) {
            Entropy entropy = new Entropy();
            entropy.setProperty(property);
            mapOfEntropy.put(property, entropy);
        }
        if (recode.isProfitable()) {
            mapOfEntropy.get(property).increaseNumberOfPositiveResponse();
        } else {
            mapOfEntropy.get(property).increaseNumberOfNegativeResponse();
        }
    }

    private static InformationGain getInformationGain(ClassificationEntropy classificationEntropy, String criteria, Map<String, Entropy> mapOfEntropy) {
        for (Entropy entropy : mapOfEntropy.values()) {
            double entropyValue = calculateEntropy(entropy.getNumberOfPositives(), entropy.getNumberOfNegatives());
            entropy.setEntropy(entropyValue);
            entropy.setTotalResponses(entropy.getNumberOfPositives() + entropy.getNumberOfNegatives());
        }
        InformationGain informationGain = new InformationGain();
        informationGain.setCriteria(criteria);
        informationGain.setMapOfEntropy(mapOfEntropy);
        double informationGainValue = calculateInformationGain(classificationEntropy, mapOfEntropy);
        informationGain.setInformationGain(informationGainValue);

        return informationGain;
    }

    private static double calculateInformationGain(ClassificationEntropy classificationEntropy, Map<String, Entropy> mapOfEntropy) {
        double informationGain;
        double classificationEntropyValue = classificationEntropy.getClassificationEntropy();
        double propertyEntropyValueTotal = 0.00;
        for (Entropy entropy : mapOfEntropy.values()) {
            propertyEntropyValueTotal = propertyEntropyValueTotal + (((double) entropy.getTotalResponses() / (double) classificationEntropy.getNumberOfTotalRecodes())
                    * entropy.getEntropy());
        }
        informationGain = classificationEntropyValue - propertyEntropyValueTotal;
        return informationGain;
    }

    private static ClassificationEntropy calculateClassificationEntropy(DataSet trainingDataSet) {
        ClassificationEntropy classificationEntropy = new ClassificationEntropy();

        double classificationEntropyValue = 0.00;
        int numberOfPositives = 0;
        int numberOfNegatives = 0;
        for (Recode recode : trainingDataSet.getListOfRecodes()) {
            if (recode.isProfitable()) {
                numberOfPositives++;
            } else {
                numberOfNegatives++;
            }
        }
        classificationEntropy.setNumberOfTotalNegatives(numberOfNegatives);
        classificationEntropy.setNumberOfTotalPositives(numberOfPositives);
        classificationEntropy.setNumberOfTotalRecodes(numberOfNegatives + numberOfPositives);
        classificationEntropyValue = calculateEntropy(numberOfPositives, numberOfNegatives);
        classificationEntropy.setClassificationEntropy(classificationEntropyValue);

        return classificationEntropy;
    }

    private static double calculateEntropy(int numberOfPositives, int numberOfNegatives) {
        double entropy;
        if (numberOfPositives == 0 || numberOfNegatives == 0) {
            entropy = 0;
        } else if (numberOfPositives == numberOfNegatives) {
            entropy = 1;
        } else {
            int totalRecodes = numberOfPositives + numberOfNegatives;
            double positiveProbability = (double) numberOfPositives / (double) totalRecodes;
            double negativeProbability = (double) numberOfNegatives / (double) totalRecodes;
            entropy = ((-1) * positiveProbability * log(positiveProbability, 2)) + ((-1) * negativeProbability * log(negativeProbability, 2));
        }

        return entropy;
    }

    private static double log(double x, int base) {
        return (Math.log(x) / Math.log(base));
    }

    private static DataSet getTrainingDataset() {
        /**
         * rainy - 100, Overcast - 200, sunny - 300
         * hot - aaa, mild - bbb, cool - ccc
         * High - English, Normal - Hindi
         * true - India, false - USA
         */
        Recode r1 = new Recode("1", "100", "aaa", "xxx", "ppp", "English", "USA", false);
        Recode r2 = new Recode("1", "100", "aaa", "YYY", "ppp", "English", "India", false);
        Recode r3 = new Recode("1", "200", "aaa", "xxx", "QQQ", "English", "USA", true);
        Recode r4 = new Recode("1", "300", "bbb", "xxx", "QQQ", "English", "USA", true);
        Recode r5 = new Recode("1", "300", "ccc", "YYY", "QQQ", "Hindi", "USA", true);
        Recode r6 = new Recode("1", "300", "ccc", "YYY", "ppp", "Hindi", "India", false);
        Recode r7 = new Recode("1", "200", "ccc", "YYY", "ppp", "Hindi", "USA", true);
        Recode r8 = new Recode("2", "100", "bbb", "xxx", "ppp", "English", "USA", false);
        Recode r9 = new Recode("2", "100", "ccc", "xxx", "ppp", "English", "USA", true);
        Recode r10 = new Recode("2", "300", "bbb", "YYY", "QQQ", "Hindi", "USA", true);
        Recode r11 = new Recode("2", "100", "bbb", "YYY", "QQQ", "Hindi", "India", true);
        Recode r12 = new Recode("2", "200", "bbb", "YYY", "QQQ", "English", "India", true);
        Recode r13 = new Recode("2", "200", "aaa", "xxx", "QQQ", "Hindi", "USA", true);
        Recode r14 = new Recode("2", "300", "bbb", "YYY", "ppp", "English", "India", false);

//        Recode r15 = new Recode("Logng","100","ranjan ramanayaka","xxxx", "YYY","sinhala","srilanka",true);

        List<Recode> recodeList = new ArrayList<Recode>();
        recodeList.add(r1);
        recodeList.add(r2);
        recodeList.add(r3);
        recodeList.add(r4);
        recodeList.add(r5);
        recodeList.add(r6);
        recodeList.add(r7);
        recodeList.add(r8);
        recodeList.add(r9);
        recodeList.add(r10);
        recodeList.add(r11);
        recodeList.add(r12);
        recodeList.add(r13);
        recodeList.add(r14);

        DataSet dataSet = new DataSet();
        dataSet.setListOfRecodes(recodeList);

        return dataSet;
    }
}
